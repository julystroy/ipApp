package com.cartoon.module.mytask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.bean.TaskBean;
import com.cartoon.data.BuyTaskList;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.view.dialog.BuyTaskDialog;
import com.cartton.library.utils.ToastUtils;

import java.util.Collections;
import java.util.List;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-12-12.
 */
public class MyTaskAdapter extends BaseAdapter{

    private Context  context;
    private List<TaskBean>  taskName;

    private int[] drawable={R.mipmap.icon_task_sign,R.mipmap.icon_task_share,R.mipmap.icon_task_thumb,R.mipmap.icon_task_reply,
            R.mipmap.icon_task_comment,R.mipmap.icon_task_upgrade,R.mipmap.icon_task_question};

    public MyTaskAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setData(List<TaskBean> taskName){
        this.taskName = taskName;
        Collections.sort(taskName);//排序
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return taskName==null?0:taskName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

         ComViewHolder comHolder;
        final TaskBean item = taskName.get(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.mytask_list_item_com, null);
                    comHolder = new ComViewHolder();
                    comHolder.taskTitle = (TextView) convertView.findViewById(R.id.tv_task_item_title);
                    comHolder.taskJingyan = (TextView) convertView.findViewById(R.id.jingyan);
                    comHolder.taskState = (ImageView) convertView.findViewById(R.id.iv_task_state);
                    comHolder.userPoint = (TextView) convertView.findViewById(R.id.user_point);
                    comHolder.userStone = (TextView) convertView.findViewById(R.id.user_stone);

                    comHolder.taskCount = (TextView) convertView.findViewById(R.id.task_count);
                    comHolder.taskStone = (TextView) convertView.findViewById(R.id.stone);
                    comHolder.taskInfo = (LinearLayout) convertView.findViewById(R.id.use_task_info);
                    comHolder.llIterm = (LinearLayout) convertView.findViewById(R.id.ll_item);
                    comHolder.view = (View) convertView.findViewById(R.id.view);
                    comHolder.line = (View) convertView.findViewById(R.id.line);
                  //  comHolder.llStringIterm = (LinearLayout) convertView.findViewById(R.id.ll_string_task);
                    convertView.setTag(comHolder);
                } else {
                    comHolder = (ComViewHolder) convertView.getTag();
                }


                 if (position == 0) {
                    comHolder.view.setVisibility(View.VISIBLE);
                    comHolder.line.setVisibility(View.GONE);
                }else{
                    comHolder.view.setVisibility(View.GONE);
                    comHolder.line.setVisibility(View.VISIBLE);
                }

      //  comHolder.taskTitle.setText(item.getActionName());
        if (item.getActionType() == 3 && item.getLv_type()<3) {
                comHolder.taskTitle.setText(item.getActionName()+"("+item.getCount()+"/"+item.getLimit()+")");
                comHolder.taskCount.setText("剩余1");
        }else {
            comHolder.taskTitle.setText(item.getActionName());
        }
        comHolder.userStone.setText("+"+item.getStone()+"");
        comHolder.taskState.setImageResource(drawable[position]);
        if (position == 5) {
            comHolder.userPoint.setVisibility(View.GONE);
            comHolder.taskJingyan.setVisibility(View.GONE);
            comHolder.taskCount.setVisibility(View.INVISIBLE);
        } else {
            comHolder.userPoint.setVisibility(View.VISIBLE);
            comHolder.taskJingyan.setVisibility(View.VISIBLE);
            comHolder.taskCount.setVisibility(View.VISIBLE);

                comHolder.taskCount.setText(item.getRemain()+"");
                comHolder.userPoint.setText("+"+item.getBonus()+"");
                if (item.isFinishToday()) {
                    comHolder.taskCount.setSelected(true);
                    comHolder.taskCount.setText("已完成");

                } else {
                    comHolder.taskCount.setSelected(false);
                    if (item.getRemain() != 0) {
                        comHolder.taskCount.setText("剩余" + item.getRemain());
                        comHolder.taskCount.setClickable(false);
                    } else {
                        comHolder.taskCount.setText("购买" );
                        comHolder.taskCount.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //请求购买任务
                                BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_BUY_TASK_list)
                                        .addParams("action_type", item.getActionType()+"")
                                        .build().execute(new BaseCallBack<BuyTaskList>() {
                                    @Override
                                    public void onLoadFail() {

                                        ToastUtils.showShort(context,"链接失败");
                                    }

                                    @Override
                                    public void onContentNull() {
                                    }

                                    @Override
                                    public void onLoadSuccess(BuyTaskList response) {
                                        BuyTaskDialog dialog = new BuyTaskDialog(context,response.getBuy_stone(),response.getCanBuyNum(),item.getActionType());
                                        dialog.setCanceledOnTouchOutside(true);
                                        dialog.show();

                                    }

                                    @Override
                                    public BuyTaskList parseNetworkResponse(String response) throws Exception {
                                        return JSON.parseObject(response, BuyTaskList.class);
                                    }
                                });

                            }
                        });
                    }
                    comHolder.taskInfo.setVisibility(View.VISIBLE);
                }
        }

        return convertView;

    }


    class ComViewHolder {
        TextView taskTitle;
        TextView taskJingyan;
        TextView userPoint;
        TextView userStone;
        TextView taskCount;
        TextView taskStone;
        ImageView taskState;
        LinearLayout taskInfo;
        LinearLayout llIterm;
     //   LinearLayout llStringIterm;
        View view;
        View line;

    }

}
