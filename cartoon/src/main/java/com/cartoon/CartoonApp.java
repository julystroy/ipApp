package com.cartoon;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.cartoon.account.AccountHelper;
import com.cartoon.data.Keys;
import com.cartoon.data.UserInfo;
import com.cartoon.data.game.DaoMaster;
import com.cartoon.data.game.DaoSession;
import com.cartoon.http.StaticField;
import com.cartoon.module.changxian.download.DownloadManager;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.utils.NetworkHelper;
import com.cartoon.utils.NetworkUtils;
import com.cartoon.utils.PreferenceManager;
import com.cartoon.utils.PreferenceUtil;
import com.cartton.library.utils.DebugLog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import cn.com.xuanjiezhimen.BuildConfig;
import cn.idianyun.streaming.DianyunSdk;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by David on 16/6/1.
 **/
public class CartoonApp extends Application {

    public static final int versionCode = BuildConfig.VERSION_CODE;
    private UserInfo userInfo;
    private UserInfo lastUserInfo;

    private        SharedPreferences sp;
    private static CartoonApp        sInstance;
  //  private List<QtPlayListener> playListeners;
    private        CacheControl      noCacheControl;
    private        DaoSession        daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = BuildConfig.DEBUG;
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);
      //  MobclickAgent.setDebugMode(true);//集成测试
        MobclickAgent.setCatchUncaughtExceptions(true); //是否需要错误统计功能
     //   MobclickAgent.openActivityDurationTrack(false); //来禁止默认的Activity页面统计方式。不需要统计的界面在oncreat方法里
        //dianyunsdk
       // DianyunSdk.initSdk(this, "a.test");
      //  DianyunSdk.initSdk(this, "a.17k");
        setupHttpCache();

        sInstance = this;
        DebugLog.initDebug(BuildConfig.DEBUG);
        this.sp = getSharedPreferences(Keys.CARTOON_PREFERENCE, Context.MODE_PRIVATE);

        CacheControl.Builder builder = new CacheControl.Builder();
        noCacheControl = builder.noCache().noStore().build();//选择器，不缓存设置


        initPlatfomInfo();

        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.setStatisticsEnable(false);
        JPushInterface.init(this);

        refreshJPushTag();


        //greendao
        setDatabase();

        PreferenceManager.init(this);
        NetworkHelper.init(this);
        DownloadManager.init(getApplicationContext());
        DianyunSdk.initSdk(this, "fanrenxiuxian");
        AccountHelper.init(this);
        AccountHelper.register(getApplicationContext());
    }
    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "chat.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();

    }
    public DaoSession getDaoSession() {
        return daoSession;
    }


    private void setupHttpCache() {

        //设置缓存路径
        File httpCacheDirectory = new File(getCacheDir(), "responses");
        //设置缓存 60M
        Cache cache = new Cache(httpCacheDirectory, 60 * 1024 * 1024);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new RewriteCacheControl())//加入拦截器
                .addInterceptor(new RewriteCacheControl())
               // .addInterceptor(new LoggingInterceptor())
                //.connectTimeout(20, TimeUnit.SECONDS)
                .cache(cache)
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }


    class RewriteCacheControl implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            boolean networkOnLine = NetworkUtils.isNetworkOnline(getApplicationContext());

            String apiUrl = request.url().url().toString();
            if (apiUrl.contains(StaticField.URL_NEXT_QUESTION)
                        || apiUrl.contains(StaticField.URL_DAY_QUESTION_GET_ANSWER)
                        || apiUrl.contains(StaticField.URL_DAY_QUESTION_list)
                    ||apiUrl.contains(StaticField.URL_SECT_FIGHT_QUESTION)) {
                request = request.newBuilder()
                        .cacheControl(noCacheControl)
                        .build();
            } else {
                if (!networkOnLine) {
                    // have no network
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                } else {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_NETWORK)
                            .build();
                }
            }

            Response response = chain.proceed(request);

            if (networkOnLine) {
                int maxAge = 0; // 有网络时 设置缓存超时时间0个小时
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
//                maxStale = cacheOnlyGetMethod ? 0 : maxStale;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }

            return response;
        }
    }

    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.d("LoggingInterceptor", String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.d("LoggingInterceptor", String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }


    public void refreshJPushTag() {
        if (!TextUtils.isEmpty(getUserId())) {
            Set<String> tag = new HashSet<>(1);
            tag.add(getUserId());
            JPushInterface.setTags(this, tag, null);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //  QTPlayerAgent.getInstance().stop();

        DianyunSdk.finiSdk();
        AccountHelper.fini();
        NetworkHelper.fini();
        PreferenceManager.fini();
    }
    public static CartoonApp getInstance() {
        return sInstance;
    }



    private void initPlatfomInfo() {
        //各个平台的配置，建议放在全局Application或者程序入口

//        PlatformConfig.setWeixin("wxff6cc82699a50796", "4cba8e7cb4162da4fad08a1039713ba3");
       PlatformConfig.setWeixin("wxff6cc82699a50796", "17a743ed0fed1db18d2bb8396333b3ae");
        PlatformConfig.setQQZone("1105682174", "BDo2ZeLSBjtksojb");
        //PlatformConfig.setQQZone("1105500910", "IbMgY66ikKRCDh2o");
       // PlatformConfig.setSinaWeibo("2359285492", "57b000212455ac516ee01fb734195d38","");
    }

    public boolean isLogin(Context context) {
        if (TextUtils.isEmpty(getUserId())) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return false;
        }
        return true;
    }

    public String getUserId() {
        if (getUserInfo() != null&&getUserInfo().getId()!=null) {
            return getUserInfo().getId();
        }
        return "";
    }

    public String getToken() {
        if (getUserInfo() != null&&getUserInfo().getToken()!=null) {
            return getUserInfo().getToken();
        }
        return "";
    }

    public UserInfo getUserInfo() {
        if (userInfo == null) {
            userInfo = (UserInfo) PreferenceUtil.getObject(sp, Keys.PREFERENCE_USER_INFO, null);
        }
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        PreferenceUtil.putObject(sp, Keys.PREFERENCE_USER_INFO, userInfo);
    }


    public UserInfo getUserLastInfo() {
        if (lastUserInfo == null) {
            lastUserInfo = (UserInfo) PreferenceUtil.getObject(sp, Keys.PREFERENCE_USERLAST_INFO, null);
        }
        return lastUserInfo;
    }

    public void setUserLastInfo(UserInfo userInfo) {
        this.lastUserInfo = userInfo;
        PreferenceUtil.putObject(sp, Keys.PREFERENCE_USERLAST_INFO, userInfo);
    }


    public void logout() {
        setUserInfo(null);
        setUserLastInfo(null);
    }

    public SharedPreferences getPreferences() {
        return sp;
    }



    /**
     * 获取设备唯一标识
     */

    public String getDeviceId() {
        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String device_token = tm.getDeviceId();
            return device_token;
        } catch (Exception e) {
            return "00000000";
        }
    }


    //扩展65535，线上删除
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
