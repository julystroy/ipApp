package com.cartoon.module.tab.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.AnswersBean;
import com.cartoon.data.NovelQuestion;
import com.cartoon.data.SelectAnswer;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartton.library.utils.ToastUtils;

import java.util.List;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-2-14.
 * 投票
 */
public class NovelSelectAdapter extends BaseAdapter{


    private Context context;
    private List<AnswersBean> mList;
    private String questionId;
    private String rightAnswerId;//正确答案
    private String answeredId;//已选答案
    private int isDisable;//是否可点击   后台默认给的1

    private String[] arr = {"A. ","B. ","C. ","D. ","E. ","F. ","G. ","H. ","I. ","J. ","K. "};

    public NovelSelectAdapter(Context context, NovelQuestion answersBean) {
        this.context = context;
        this.mList = answersBean.getAnswers();
        questionId = answersBean.getQuestionId();
        rightAnswerId = answersBean.getRightAnswerId();
        answeredId      = answersBean.getAnsweredId();
        isDisable        = answersBean.getIsDisable();
    }


    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyHolder holder;
        final AnswersBean select = mList.get(position);
        if (convertView == null) {
            holder = new MyHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_select_list, parent, false);
            holder.ivRight = (ImageView) convertView.findViewById(R.id.iv_right);
            holder.tvSelect = (TextView) convertView.findViewById(R.id.tv_select_item);
            holder.tvSort = (TextView) convertView.findViewById(R.id.tv_sort);
            holder.llSelct = (LinearLayout) convertView.findViewById(R.id.ll_select);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }

        if (position<=arr.length)
        holder.tvSort.setText(arr[position]);

           holder.tvSelect.setText(select.getOptionContent());

        if (isDisable == 0) {
            if (rightAnswerId == null) {
                holder.ivRight.setVisibility(View.GONE);
                holder.tvSelect.setTextColor(Color.parseColor("#bebebe"));
                holder.tvSort.setTextColor(Color.parseColor("#bebebe"));
                holder.llSelct.setBackgroundResource(R.drawable.fang_background_gray);
            }
           else if (rightAnswerId != null && rightAnswerId.equals(select.getAnswerId())) {//正确答案
                holder.tvSort.setTextColor(Color.parseColor("#4da231"));
                holder.tvSelect.setTextColor(Color.parseColor("#4da231"));
                holder.llSelct.setBackgroundResource(R.drawable.fang_background_green);
                holder.ivRight.setVisibility(View.VISIBLE);
            }
            else if (answeredId != null && answeredId.equals(select.getAnswerId())&&rightAnswerId == null) {//用户选择答案
                holder.ivRight.setVisibility(View.GONE);
                holder.tvSelect.setTextColor(Color.parseColor("#ffa000"));
                holder.tvSort.setTextColor(Color.parseColor("#ffa000"));
                holder.llSelct.setBackgroundResource(R.drawable.fang_background_yello);
            } else {//置灰状态
                holder.ivRight.setVisibility(View.GONE);
                holder.tvSelect.setTextColor(Color.parseColor("#FF000000"));
                holder.tvSort.setTextColor(Color.parseColor("#FF000000"));
                holder.llSelct.setBackgroundResource(R.drawable.fang_background_gray);
            }
        } else {//可选状态

            if (rightAnswerId != null && rightAnswerId.equals(select.getAnswerId())) {//正确答案
                holder.tvSelect.setTextColor(Color.parseColor("#4da231"));
                holder.tvSort.setTextColor(Color.parseColor("#4da231"));
                holder.llSelct.setBackgroundResource(R.drawable.fang_background_green);
                holder.ivRight.setVisibility(View.VISIBLE);
            } else if (answeredId != null && answeredId.equals(select.getAnswerId())&&rightAnswerId == null) {//用户选择答案
                holder.ivRight.setVisibility(View.GONE);
                holder.tvSelect.setTextColor(Color.parseColor("#ffa000"));
                holder.tvSort.setTextColor(Color.parseColor("#ffa000"));
                holder.llSelct.setBackgroundResource(R.drawable.fang_background_yello);
            } else {
                holder.ivRight.setVisibility(View.GONE);
                holder.tvSelect.setTextColor(Color.parseColor("#FA151414"));
                holder.tvSort.setTextColor(Color.parseColor("#FA151414"));
                holder.llSelct.setBackgroundResource(R.drawable.fang_background_gray);
                if (isDisable != 0 && answeredId==null) {
                    holder.llSelct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (CartoonApp.getInstance().isLogin(context)) {
                                BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SELECT_ANSWER)
                                        .addParams("answerId",select.getAnswerId())
                                        .addParams("userId", CartoonApp.getInstance().getUserId())
                                        .addParams("questionId",questionId)
                                        .build().execute(new BaseCallBack<SelectAnswer>() {
                                    @Override
                                    public void onLoadFail() {
                                    }

                                    @Override
                                    public void onContentNull() {
                                    }

                                    @Override
                                    public void onLoadSuccess(SelectAnswer response) {
                                        if (response != null && response.getIsDisable() == 0&&response.getAnswerId()==null) {
                                            isDisable =0;
                                            ToastUtils.showShort(context,"活动已结束");
                                        } else {
                                            holder.llSelct.setEnabled(true);
                                            holder.llSelct.setClickable(false);
                                            answeredId=response.getAnswerId();
                                            holder.tvSelect.setTextColor(Color.parseColor("#ffa000"));
                                            holder.tvSort.setTextColor(Color.parseColor("#ffa000"));
                                            holder.llSelct.setBackgroundResource(R.drawable.fang_background_yello);
                                        }
                                        notifyDataSetChanged();
                                    }
                                    @Override
                                    public SelectAnswer parseNetworkResponse(String response) throws Exception {
                                        return JSON.parseObject(response,SelectAnswer.class);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }

        return convertView;
    }
    class MyHolder{
        private ImageView    ivRight;
        private TextView     tvSelect;
        private TextView     tvSort;
        private LinearLayout llSelct;
    }
}
