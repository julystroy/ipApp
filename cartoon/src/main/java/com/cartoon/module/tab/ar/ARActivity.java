package com.cartoon.module.tab.ar;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cartoon.data.ArShow;
import com.cartoon.utils.UnityUtils;
import com.cartoon.view.dialog.DescDialog;
import com.cartton.library.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-10-25.
 */
public class ARActivity extends UnityPlayerActivity implements View.OnClickListener {
    private LinearLayout imageView;
    private ImageView ivBack;
    private ImageView ivRecode;
    private ImageView ivMore;
    private ImageView ivSave;
    private RelativeLayout rlItem;
    private TextView tvAudio;
    private TextView tvTakePhoto;
    private TextView tvCountDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this,mPermissionList,123);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        EventBus.getDefault().register(this);
        //获取显示Unity视图的父控件
        LinearLayout mParent=(LinearLayout)findViewById(R.id.UnityView);
         imageView  = (LinearLayout) findViewById(R.id.iv_load);
         ivBack      = (ImageView) findViewById(R.id.iv_back);
         ivRecode    = (ImageView) findViewById(R.id.iv_recode);
         ivMore      = (ImageView) findViewById(R.id.iv_more);
         ivSave      = (ImageView) findViewById(R.id.iv_save);
        tvAudio      = (TextView) findViewById(R.id.iv_audio);
        tvTakePhoto  = (TextView) findViewById(R.id.tv_takephoto);
        tvCountDown  = (TextView) findViewById(R.id.count_down);
        rlItem       = (RelativeLayout) findViewById(R.id.rl_item);

        //获取Unity视图
        View mView=mUnityPlayer.getView();
        //将Unity视图添加到Android视图中
        mParent.addView(mView);

//        ivBack.setOnClickListener(this);
//                ivRecode.setOnClickListener(this);
//        ivMore.setOnClickListener(this);
//        ivSave.setOnClickListener(this);
//                tvAudio.setOnClickListener(this);
//        tvTakePhoto.setOnClickListener(this);

    }


    @Subscribe
    public void onEvent(ArShow a){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.GONE);
             //   rlItem.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        finish();
    }
    private boolean audioing = true;
    private int count =1;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (!audioing) {
                    if (count==2)
                        showDialog();
                    else
                    this.onDestroy();
                } else {
                    if (count == 1) {
                        tvAudio.setVisibility(View.VISIBLE);
                        tvTakePhoto.setVisibility(View.VISIBLE);
                        tvCountDown.setVisibility(View.GONE);
                        ivRecode.setVisibility(View.GONE);
                        ivMore.setVisibility(View.VISIBLE);
                        ivMore.setClickable(true);
                        audioing = false;
                    } else {
                        showDialog();
                    }
                }
                break;
            case R.id.iv_recode:
                if (count <= 2) {
                    if (count == 1) {
                        mHandler.sendEmptyMessage(1);
                    } else {
                       // mHandler.sendEmptyMessage(0);
                        mHandler.removeMessages(1);
                    }
                    count++;
                    ivRecode.setSelected(audioing);
                    UnityUtils.getInstance().StartScreenCap(audioing);
                    audioing = !audioing;
                    if (count >2)
                        ivSave.setVisibility(View.VISIBLE);
                }else{
                    ToastUtils.showShort(ARActivity.this,"请先对刚录制的视频进行处理");
                }
                break;
            case R.id.iv_more:
                DescDialog descDialog = new DescDialog(ARActivity.this,1);
                descDialog.setCanceledOnTouchOutside(true);
                descDialog.show();
                break;
            case R.id.iv_audio:
                audioing =true;
                tvAudio.setVisibility(View.GONE);
                tvTakePhoto.setVisibility(View.GONE);
                ivMore.setVisibility(View.INVISIBLE);
                ivMore.setClickable(false);
                tvCountDown.setVisibility(View.VISIBLE);
                ivRecode.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_takephoto:
                UnityUtils.getInstance().ScreenShot();
                break;
            case R.id.iv_save:
                ToastUtils.showShort(ARActivity.this,"保存成功");
                tvAudio.setVisibility(View.VISIBLE);
                tvTakePhoto.setVisibility(View.VISIBLE);
                tvCountDown.setVisibility(View.GONE);
                ivRecode.setVisibility(View.GONE);
                ivMore.setVisibility(View.VISIBLE);
                ivMore.setClickable(true);
                ivSave.setVisibility(View.GONE);
                audioing = false;
                count=1;
                timeing =0;
                isstop = false;
                tvCountDown.setText("00:00");
                break;
        }
    }

    private void  showDialog(){
        new MaterialDialog.Builder(this).title(R.string.notice)
                .content("确定要删除录像吗?")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        // onBackPressed();
                        File del_file = new File(getExternalFilesDir("ScreenCaps")+"");
                        deleteDirectory(del_file);
                        tvAudio.setVisibility(View.VISIBLE);
                        tvTakePhoto.setVisibility(View.VISIBLE);
                        tvCountDown.setVisibility(View.GONE);
                        ivRecode.setVisibility(View.GONE);
                        ivMore.setVisibility(View.VISIBLE);
                        ivMore.setClickable(true);
                        ivSave.setVisibility(View.GONE);
                        count =1;
                        audioing = false;
                        timeing =0;
                        isstop = false;
                        tvCountDown.setText("00:00");
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                }).show();
    }
    //删除文件夹
    private static void deleteDirectory(File file) {
        if (file.isFile()) {// 表示该文件不是文件夹
            file.delete();
        } else {
            // 首先得到当前的路径
            String[] childFilePaths = file.list();
            for (String childFilePath : childFilePaths) {
                File childFile = new File(file.getAbsolutePath() + "/" + childFilePath);
                deleteDirectory(childFile);
            }
            file.delete();
        }
    }


    //秒表
    private long timeing;
    private boolean isstop =false;
    private Handler mHandler = new Handler() {
        /* 
         * edit by yuanjingchao 2014-08-04 19:10 
         */
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub  
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 添加更新ui的代码  
                    if (!isstop) {
                        updateView();
                        mHandler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
                case 0:
                    break;
            }
        }

    };

    private void updateView() {
        String m ="00";
        String s ="00";
        timeing += 1;
        int minute = (int) (timeing / 60)%60;
        int second = (int) (timeing % 60);
        if (minute < 10)
            m=("0" + minute);
        else
            m=("" + minute);
        if (second < 10)
            s=("0" + second);
        else
            s=("" + second);

        tvCountDown.setText(m+":"+s);
    }

}
