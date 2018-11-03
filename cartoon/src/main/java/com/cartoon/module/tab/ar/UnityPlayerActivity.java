package com.cartoon.module.tab.ar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cartoon.data.ArShow;
import com.cartoon.utils.UnityUtils;
import com.cartoon.view.DefineToast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.unity3d.player.UnityPlayer;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UnityPlayerActivity extends Activity implements UnityUtils.UnityCallBack
{
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    private   ProgressBar dialog;

    // Setup activity layout
    @Override protected void onCreate (Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        dialog = new ProgressBar(this);
       // WindowManager.LayoutParams attributes = getWindow().getAttributes();
        LinearLayout.LayoutParams lp1=new LinearLayout.LayoutParams(50,50);
      //  attributes.gravity= Gravity.CENTER;
        lp1.gravity = Gravity.CENTER;
       // getWindow().setAttributes(attributes);
        dialog.setLayoutParams(lp1);
        dialog.setVisibility(View.GONE);

        mUnityPlayer = new UnityPlayer(this);
        setContentView(mUnityPlayer);
        mUnityPlayer.addView(dialog);

        mUnityPlayer.requestFocus();
        UnityUtils.getInstance().setUnityCallBack(this);

        //模拟识别图资源下载
        File datasetDat=new File(getExternalFilesDir("DataSets")+"/frxxz.dat");
        if (!datasetDat.exists())Assets2Sd(this,"demo_dataset/frxxz.dat",datasetDat.getAbsolutePath());

        File datasetXml=new File(getExternalFilesDir("DataSets")+"/frxxz.xml");
        if (!datasetXml.exists())Assets2Sd(this,"demo_dataset/frxxz.xml",datasetXml.getAbsolutePath());

    }



    public void Assets2Sd(Context context, String fileAssetPath, String fileSdPath)
    {

        InputStream myInput;
        OutputStream myOutput = null;
        try {
            myOutput = new FileOutputStream(fileSdPath);
            myInput = context.getAssets().open(fileAssetPath);
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while(length > 0)
            {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }

            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void Assets3Sd(final Context context, final String fileAssetPath, final String fileSdPath)
    {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(fileAssetPath).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(fileSdPath);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    // System.out.println("@@下载成功");

                } catch (IOException e) {
                    // System.out.println("@@下载失败");
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {

                    }
                }
            }

        });

    }




    /**
     * 接收Unity回调接口
     *
     * @param json
     */
    public void UnityCallBack(String json) {
        UnityUtils.getInstance().from(json);
    }


    /**
     * Unity加载完成回调
     *
     * unity启动完成之后会在/sdCard/Android/data/包名/files/下创建DataSets和AssetsBundles目录
     * DataSets目录是用来放识别图资源的
     * AssetsBundles目录是用来放模型资源的
     */
    @Override
    public void startComplete() {
        EventBus.getDefault().post(new ArShow());
        //加载资源模型识别图，参数为json,示例如下：dataSetName、url、id这三个字段是必须的，可以自主添加自定义字段，比如添加模型名字modeName。json字符串类型是数组，所以可以一次传多个识别图
        //dataSetName是识别图资源的名字，url最好填写模型的下载地址，方便在模型下载回调里进行下载，id字段内容随意
        String json="[{'dataSetName':'frxxz','url':'','id':1,'modelName':'frxxz_hanli'}]";
       // UnityUtils.getInstance().loadDataSet(json);
        UnityPlayer.UnitySendMessage("InteractObject","LoadDataSet",json);

    }

    /**
     * 返回按键点击回调
     */
    @Override
    public void clickedBackKey() {
        finish();
    }


    /**
     * 资源识别图加载完成回调
     */
    @Override
    public void loadDataSetsCompleted() {

    }

    /**
     * 下载模型资源回调
     * 一旦扫描的识别图本地没有模型，将会进入这个回调
     * @param info 参数是调用loadDataSet的时候传入的json字符串的子数据，会对应扫描的识别图传进对应的子数据
     */
    @Override
    public void downAssetBundle(String info) {
        try {

            JSONObject jsonObject=new JSONObject(info);
            String datasetName=jsonObject.getString("dataSetName");
            String modelName=jsonObject.getString("modelName");
            File assetsBundles = new File(getExternalFilesDir("AssetsBundles") + "/"+datasetName+"/"+modelName+".ass");
            if (assetsBundles!=null)
            assetsBundles.delete();

            //调用加载识别图的方法之后会在AssetsBundles目录下创建相对应的模型文件夹,文件夹的名字是根据dataSetName来命名的，把对应的模型资源下载到对应的文件夹里。
            //比如我这个识别图资源的dataSetName是MiTu_Sea，那么会在AssetsBundles目录下创建一个MiTu_Sea的文件夹来放这个识别图对应的模型。
            //模拟模型下载
           // Assets2Sd(this,"demo_ass/frxxz_hanli.ass",getExternalFilesDir("AssetsBundles")+"/"+datasetName+"/"+modelName+".ass");
            Assets3Sd(this,"http://video.mopian.tv/frxxz_yinyue.ass",getExternalFilesDir("AssetsBundles")+"/frxxz/frxxz_yinyue.ass");
            Assets3Sd(this,"http://video.mopian.tv/frxxz_hanli.ass",getExternalFilesDir("AssetsBundles")+"/"+datasetName+"/frxxz_hanli.ass");
            //  Assets2Sd(this,"demo_ass/frxxz_hanli.ass",getExternalFilesDir("AssetsBundles")+"/"+datasetName+"/frxxz_hanli.ass");
            //  Assets2Sd(this,"demo_ass/frxxz_nangongwan.ass",getExternalFilesDir("AssetsBundles")+"/"+datasetName+"/frxxz_nangongwan.ass");
            Assets3Sd(this,"http://video.mopian.tv/frxxz_nangongwan.ass",getExternalFilesDir("AssetsBundles")+"/"+datasetName+"/frxxz_nangongwan.ass");
            Toast.makeText(this,"模型下载中,请重新扫描图片",Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*
    * 截屏回调*/
    @Override
    public void takePhoteBacks(final String ImagePath) {

        //Toast.makeText(UnityPlayerActivity.this,ImagePath,Toast.LENGTH_SHORT).show();

        DefineToast.createToastConfig().ToastShow(UnityPlayerActivity.this,ImagePath);
     //  ApiUtils.share(UnityPlayerActivity.this,"",ImagePath,"图片","");


        ShareBoardConfig config = new ShareBoardConfig();
        config.setTitleVisibility(false);
        config.setCancelButtonText("取消");
        config.setShareboardBackgroundColor(Color.parseColor("#ffffff"));
        config.setIndicatorVisibility(false);
        config.setStatusBarHeight(20);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR,10);
        new ShareAction(UnityPlayerActivity.this).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        UMImage image = new UMImage(UnityPlayerActivity.this, BitmapFactory.decodeFile(ImagePath));
                        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
                        image.compressFormat = Bitmap.CompressFormat.PNG;
                        new ShareAction(UnityPlayerActivity.this).withText("凡人_AR瞬间").withMedia(image )
                                .setPlatform(share_media)
                                .setCallback(shareListener)
                                .share();

                    }
                }).open(config);

    }



    @Override
    public void audioBacks(String audioPath) {
        Toast.makeText(UnityPlayerActivity.this,audioPath,Toast.LENGTH_SHORT).show();
      //  System.out.println("ddddd  "+audioPath);
    }


    @Override protected void onNewIntent(Intent intent)
    {
        setIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.quit();
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }


    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            dialog.setVisibility(View.VISIBLE);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(UnityPlayerActivity.this,"成功了",Toast.LENGTH_LONG).show();
            dialog.setVisibility(View.GONE);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            dialog.setVisibility(View.GONE);
            Toast.makeText(UnityPlayerActivity.this,"失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
           // SocializeUtils.safeCloseDialog(dialog);
            dialog.setVisibility(View.GONE);
            Toast.makeText(UnityPlayerActivity.this,"取消了",Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }


}
