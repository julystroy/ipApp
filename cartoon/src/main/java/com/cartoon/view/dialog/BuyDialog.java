package com.cartoon.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.data.BuyItem;
import com.cartoon.data.RefreshActivity;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.utils.Utils;
import com.cartoon.view.DefineToast;
import com.cartton.library.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-10-25.
 */
 public class BuyDialog extends Dialog implements View.OnClickListener {
    private  Context context;
    /***
     构造方法
     @param context

     */

    private TextView MPay;
    private TextView MarketsDesc;
    private TextView MarketsCount;
    private ImageView ivCard;

    public BuyDialog(Context context,String item_id, String stone_count, String image_url, String desc) {
        super(context, R.style.DialogStyleDesc);
        this.item_id = item_id;
        this.context = context;
        this.stone_count = stone_count;
        this.image_url = image_url;
        this.desc = desc;
        //修改显示位置 本质就是修改 WindowManager.LayoutParams让内容水平居中 底部对齐
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        //android:gravity=bottom|center_horizonal
        attributes.gravity= Gravity.CENTER|Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(attributes);
    }
    /*** 方法
     @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_markets_dialog);
       // LoadData();
        initData();

    }
//下载数据


    private void initData() {
         MPay= (TextView) findViewById(R.id.tv_pay_stone_count);
         MarketsDesc= (TextView) findViewById(R.id.tv_markets_desc);
         MarketsCount= (TextView) findViewById(R.id.tv_markets_count);
         ivCard= (ImageView) findViewById(R.id.iv_experience_card);
        MarketsDesc.setText(desc);
        MarketsCount.setText("灵石"+stone_count);
//        if (TextUtils.equals(item_id, "3") || TextUtils.equals(item_id, "4")) {
//            MarketsDesc.setTextColor(Color.parseColor("#ffffff"));
//            MarketsCount.setTextColor(Color.parseColor("#ffffff"));
//        } else {
//            MarketsDesc.setTextColor(Color.parseColor("#000000"));
//            MarketsCount.setTextColor(Color.parseColor("#000000"));
//        }

        MPay.setOnClickListener(this);
        Glide.with(getContext()).load(Utils.addImageDomain(image_url))
                .placeholder(R.drawable.background_full_gray)
                .error(R.drawable.background_full_gray)
                .into(ivCard);
    }

    @Override
    public void onClick(View v) {

            BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_MARKETS_BUYITEM)
                    .addParams("uid", CartoonApp.getInstance().getUserId())
                    .addParams("item_id", item_id)
                    .build().execute(new BaseCallBack<BuyItem>() {

                @Override
                public void onLoadFail() {
                    ToastUtils.showShort(getContext(), "购买失败");
                }

                @Override
                public void onContentNull() {

                }

                @Override
                public void onLoadSuccess(BuyItem response) {
                    DefineToast.createToastConfig().ToastShow(getContext(), response.getMsg());
                    // ToastUtils.showShort(getContext(),response.getMsg());
                    EventBus.getDefault().post(new RefreshActivity(true));
                    //关闭dialog
                    dismiss();
                }

                @Override
                public BuyItem parseNetworkResponse(String response) throws Exception {
                    return JSON.parseObject(response, BuyItem.class);
                }
            });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private String item_id;
    private String image_url;
    private String stone_count;
    private String desc;

}
