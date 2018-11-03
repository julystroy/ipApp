package com.cartoon.module.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cartoon.CartoonApp;
import com.cartoon.CartoonService;
import com.cartoon.data.AppAD;
import com.cartoon.data.UserInfo;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.MainActivity;
import com.cartoon.utils.BonusHttp;
import com.cartton.library.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.xuanjiezhimen.R;
import cn.jpush.android.api.InstrumentedActivity;
import okhttp3.Call;

/**
 * Created by jinbangzhu on 7/24/16.
 */

public class SplashActivity extends InstrumentedActivity {
    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @BindView(R.id.btn_skip)
    Button btnSkip;

    private static final int DELAY = 3000;
    private boolean isShowAd, isSkip;
    private AppAD adResponse;
    //每个页面都有一个广告位ID列表
    List<Integer> mAdIds = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatService.start(this);

        Intent service = new Intent(getBaseContext(), CartoonService.class);
        service.setAction(CartoonService.ACTION_JPUSH);
        startService(service);
        //避免每次启动都走欢迎界面
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }
        setContentView(R.layout.splash_layout);
        ButterKnife.bind(this);

        //点云sdk
      /*  DianyunSdk.getInstance().registerPlayableListener(mOnCheckPlayableListener);
        //假设该页面只有一个广告位，ID为1
        mAdIds.add(1);
        //检查对应的试玩广告是否可玩，此方法会访问网络，通过注册的回调OnCheckPlayableListener返回
        DianyunSdk.getInstance().checkPlayable(mAdIds);
       // WebDesignUtils.getDesignConfig();

      //  WebDesignUtils.getLastedDesign();

        if (DianyunSdk.getInstance().isPlayable(0)) {
            //展示广告


        }*/




        delayStart();



        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_APP_AD)
                .build().execute(new BaseCallBack<AppAD>() {

            public void onLoadFail() {
                ToastUtils.hideToast();
            }
            @Override
            public void onContentNull() {

            }
            @Override
            public void onError(Call call, Exception e, int id) {
//                super.onError(call, e, id);
                ToastUtils.hideToast();

            }

            @Override
            public void onLoadSuccess(AppAD response) {
                if (isDestroyed())
                    return;
                adResponse = response;
                Glide.with(SplashActivity.this)
                        .load(response.getAd_pric())
                        .crossFade()
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                if (ivSplash == null)
                                    return false;
                                isShowAd = true;
                                btnSkip.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(ivAd);

                if (isUserAlreadyLogin()) {
                //成功登录app   赠送积分
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //增加積分
                            BonusHttp.SendUser2Point(CartoonApp.getInstance().getUserId(),5);
                    }
                }).start();
                }
            }



            @Override
            public AppAD parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, AppAD.class);
            }
        });

  //dianyunsdk


    }

  /*  private void launch(int id) {
        try {
            DianyunSdk.getInstance().launch(SplashActivity.this, new CustomSdkListener(this), id, true);
        } catch (UnsupportedOperationException e) {
            Toast.makeText(SplashActivity.this, "该设备不支持试玩", Toast.LENGTH_SHORT).show();
        }
    }*/

   /* private  class CustomSdkListener implements DYSdkListener {
        private WeakReference<Context> weakContext;

        public CustomSdkListener(Context context) {
            this.weakContext = new WeakReference<Context>(context);
        }

        @Override
        public void onStart() {
            Log.d("splashActivity", "onStart");
        }

        @Override
        public void onStop() {
            long duration = DianyunSdk.getInstance().getPlayDuration();
            Log.d("splashActivity", "onStop. playDuration = " + duration);
            startup();
        }

        @Override
        public boolean onDownloadClick(String json) {
            Log.d("splashActivity", "onDownloadClick. json = " + json);

            Toast.makeText(weakContext.get(), "开始下载", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
*/



    private boolean isUserAlreadyLogin() {
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        return userInfo != null;
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 页面埋点，需要使用Activity的引用，以便代码能够统计到具体页面名
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 页面结束埋点，需要使用Activity的引用，以便代码能够统计到具体页面名
        StatService.onPause(this);
    }

    @OnClick(R.id.btn_skip)
    public void onClickSkip() {
        isSkip = true;
        startup();
    }

    @OnClick(R.id.iv_ad)
    public void onClickAd() {
        if (null != adResponse && !TextUtils.isEmpty(adResponse.getUrl())) {
            isSkip = true;
            WebViewActivity.invoke(this, adResponse.getUrl(), adResponse.getDescription(), true);
            finish();
        } else {
            if (null != adResponse&&!TextUtils.isEmpty(adResponse.getAd_pric())) {
                Random random = new Random();
                int i = random.nextInt(4);

                if (i == 0) {
                    i=1;
                }
               // launch(i);
            }
        }

    }

    private void delayStart() {
        ivSplash.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSkip) return;
                if (isShowAd) {
                    isShowAd = false;
                    delayStart();
                    return;
                }
//                if (CartoonApp.getInstance().getUserInfo() == null) {
//                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                } else {

                startup();
            }
        }, DELAY);
    }

    public void startup() {
        final Bundle bundle = getIntent().getExtras();
        Log.d("SplashActivity", "bundle=" + bundle);
        if (null != bundle)
            startActivity(new Intent(SplashActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).putExtras(bundle));
        else
            startActivity(new Intent(SplashActivity.this, MainActivity.class));


//                Intent service = new Intent(getBaseContext(), CartoonService.class);
//                service.setAction(CartoonService.ACTION_JPUSH);
//                startService(service);
//                }

        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

     //   DianyunSdk.getInstance().unregisterPlayableListener(mOnCheckPlayableListener);
    }


 /*   private OnCheckPlayableListener mOnCheckPlayableListener = new OnCheckPlayableListener() {
        @Override
        public void onCheckPlayable(List<Integer> adIds, List<Boolean> playableList) { //playableList与adIds一一对应
            for (int i = 0; i < adIds.size(); i++) {
                int adId = adIds.get(i);
                boolean playable = playableList.get(i);
                Log.d("MainActivity", "adId = " + adId + ", playable = " + playable);

                if (playable) {
                    //展示相应的广告
                }
            }
        }
    };*/
}
