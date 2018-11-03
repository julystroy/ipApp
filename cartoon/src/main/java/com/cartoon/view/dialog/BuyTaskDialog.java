package com.cartoon.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.EventRefresh;
import com.cartoon.data.UserInfo;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartton.library.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-10-25.
 */
 public class BuyTaskDialog extends Dialog implements View.OnClickListener {
    private  int type;
    private int buy_stone;
    private  int      canBuyNum;
    /***
     构造方法
     @param context

     */
    private       TextView buyCount;
    private       TextView btRight;
    private       TextView tvNumber;
    private       TextView btLeft;
    private       TextView stoneCount;

      private int count =1;
    public BuyTaskDialog(Context context) {
        super(context, R.style.DialogStyleDesc);

        //修改显示位置 本质就是修改 WindowManager.LayoutParams让内容水平居中 底部对齐
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        //android:gravity=bottom|center_horizonal
        attributes.gravity= Gravity.CENTER|Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(attributes);
    }

    public BuyTaskDialog(Context context, int buy_stone, int canBuyNum,int type) {
        super(context);
        this.buy_stone=buy_stone;
        this.canBuyNum=canBuyNum;
        this.type=type;

    }

    /*** 方法
     @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_buytask_dialog);
       // LoadData();

        findView();
        initData();

    }

    private void findView() {
         btLeft= (TextView) findViewById(R.id.bt_left);
         btRight= (TextView) findViewById(R.id.bt_right);
         tvNumber= (TextView) findViewById(R.id.tv_number);
         buyCount= (TextView) findViewById(R.id.buy_count);
         stoneCount= (TextView) findViewById(R.id.stone_count);
    }
    //下载数据
    private void initData() {
        tvNumber.setText(String.valueOf(count));
        buyCount.setText("今天可以购买"+canBuyNum+"次");
        btLeft.setOnClickListener(this);
        btRight.setOnClickListener(this);
        ChangeNumColor("今天可以购买"+canBuyNum+"次");
        if (canBuyNum == 0) {
            stoneCount.setText("不可购买");
            stoneCount.setSelected(true);
            return;
        } else {
            stoneCount.setText(buy_stone+"灵石");
            stoneCount.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                if (count > 1) {
                    tvNumber.setText(String.valueOf(--count));
                    canBuyNum = canBuyNum + count+1;
                    buyCount.setText("今天可以购买"+canBuyNum+"次");
                    ChangeNumColor("今天可以购买"+canBuyNum+"次");
                    stoneCount.setText(buy_stone*count+"灵石");
                }
                break;
            case R.id.bt_right:
                if (count < canBuyNum) {
                    tvNumber.setText(String.valueOf(++count));
                    canBuyNum = canBuyNum -count ;
                    buyCount.setText("今天可以购买"+canBuyNum+"次");
                    ChangeNumColor("今天可以购买"+canBuyNum+"次");
                    stoneCount.setText(buy_stone*count+"灵石");
                }
                break;
            case R.id.stone_count:
                final String trim = tvNumber.getText().toString().trim();
                final int BonuesNum = Integer.parseInt(CartoonApp.getInstance().getUserLastInfo().getBonus_stone());
                if ((buy_stone*count) > BonuesNum) {
                    buyCount.setText("坚持完成任务获取灵石");
                    stoneCount.setText("灵石不足");
                    return;
                }
                BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_BUY_TASK)
                        .addParams("action_type", String.valueOf(type))
                        .addParams("buyNum",trim)
                        .build().execute(new BaseCallBack<String>() {

                    @Override
                    public void onLoadFail() {
                        ToastUtils.showShort(getContext(),"购买失败");
                    }

                    @Override
                    public void onContentNull() {

                    }

                    @Override
                    public void onLoadSuccess(String response) {
                        ToastUtils.showShort(getContext(),"购买成功");
                        //保存的灵石记录为最新
                        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
                        int i = BonuesNum - Integer.parseInt(trim);
                        userLastInfo.setBonus_stone(String.valueOf(i));
                        CartoonApp.getInstance().setUserLastInfo(userLastInfo);
                       EventBus.getDefault().post(new EventRefresh());
                        //关闭dialog
                        dismiss();
                    }

                    @Override
                    public String parseNetworkResponse(String response) throws Exception {
                        return JSON.parseObject(response, String.class);
                    }
                });
                break;
        }


    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void ChangeNumColor(String trim) {
        if (trim != null) {
            SpannableStringBuilder builder = new SpannableStringBuilder(trim);
            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
            ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
//文字加粗
            StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
            builder.setSpan(redSpan, 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(bss, 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            buyCount.setText(builder);
        }
    }
}
