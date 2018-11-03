package com.cartoon.view;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cartoon.CartoonApp;
import com.cartoon.module.compose.ComposeActivity;
import com.cartoon.module.compose.NoteActivity;
import com.cartoon.module.login.LoginActivity;

import cn.com.xuanjiezhimen.R;


public class SendPopWindow extends PopupWindow implements View.OnClickListener {


    private Context context;
    private View content;
    private View viewById2;

    @SuppressLint("InflateParams")
    public SendPopWindow(final Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         content = inflater.inflate(R.layout.popupwindow_send, null);


       // setBackgroundAlpha(0.5f);//设置屏幕透明度
        // 设置SelectPicPopupWindow的View
        this.setContentView(content);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
       // this.setBackgroundDrawable(ContextCompat.getDrawable(context,android.R.color.transparent));
        // 刷新状态
        this.update();

//        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//            // 在dismiss中恢复透明度
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
//                lp.alpha = 1f;
//                ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                ((Activity) context).getWindow().setAttributes(lp);
//            }
//        });




        //this.setBackgroundDrawable(new BitmapDrawable());
        // 实例化一个ColorDrawable颜色为半透明
       // ColorDrawable dw = new ColorDrawable(000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
      // this.setBackgroundDrawable(dw);


        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

        TextView sendState = (TextView) content.findViewById(R.id.send_state);
        TextView sendNote = (TextView) content.findViewById(R.id.send_note);
        View viewById = content.findViewById(R.id.view);
         viewById2 = content.findViewById(R.id.view2);

        sendNote.setOnClickListener(this);
        sendState.setOnClickListener(this);
        viewById.setOnClickListener(this);
        viewById2.setOnClickListener(this);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow   - DensityUtil.dip2px(context,75)
            //this.showAsDropDown(content, 0, (int) (parent.getHeight()+((Activity) context).getWindow().getAttributes().verticalMargin));
           /* if (Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT)
                viewById2.setVisibility(View.GONE);
            else
                viewById2.setVisibility(View.VISIBLE);
*/
                this.showAsDropDown(parent, 0,0);

        } else {
            this.dismiss();
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
       // int i = ((Activity) context).getWindow().getWindowManager().getDefaultDisplay().getHeight() - 48;
        ((Activity) context).getWindow().setAttributes(lp);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_state:
                if (CartoonApp.getInstance().getToken()!=null)
                    context.startActivity(new Intent(context, ComposeActivity.class));
                else
                    context.startActivity(new Intent(context, LoginActivity.class));
                SendPopWindow.this.dismiss();
                break;
            case R.id.send_note:
                if (CartoonApp.getInstance().getToken()!=null)
                    context.startActivity(new Intent(context, NoteActivity.class));
                else
                    context.startActivity(new Intent(context, LoginActivity.class));
                SendPopWindow.this.dismiss();
                break;
            case R.id.view:
            case R.id.view2:
                dismiss();
                break;
        }
    }
}
