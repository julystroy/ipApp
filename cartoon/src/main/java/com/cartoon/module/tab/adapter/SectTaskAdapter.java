package com.cartoon.module.tab.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cartoon.CartoonApp;
import com.cartoon.data.SectTask;
import com.cartoon.data.UserInfo;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.question.QuestionActivity;
import com.cartton.library.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-12-12.
 */
public class SectTaskAdapter extends BaseAdapter{

    private Context        context;
    private List<SectTask> taskName;
    private List<SectTask> taskName1 =new ArrayList<>();

    private boolean success = false;
    private int[] drawable={R.mipmap.icon_task_sign, R.mipmap.icon_task_comment,R.mipmap.icon_task_buy,R.mipmap.icon_task_question};
    public SectTaskAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setData(List<SectTask> taskName){
        if (taskName1!=null)taskName1.clear();
        if (taskName.size() > 4) {
            for (int i = 0; i < 4; i++) {
                taskName1.add(taskName.get(i));
            }
             success = true;
        }else taskName1.addAll(taskName);
        this.taskName = taskName1;
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

         final ComViewHolder comHolder;
        final SectTask item = taskName.get(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.sect_task_item, null);
                    comHolder = new ComViewHolder();
                    comHolder.taskTitle = (TextView) convertView.findViewById(R.id.tv_task_item_title);
                    comHolder.taskState = (ImageView) convertView.findViewById(R.id.iv_task_state);
                    comHolder.userPoint = (TextView) convertView.findViewById(R.id.user_point);
                    comHolder.jingyan = (TextView) convertView.findViewById(R.id.jingyan);
                    comHolder.userContribution = (TextView) convertView.findViewById(R.id.user_stone);
                    comHolder.item2 = (TextView) convertView.findViewById(R.id.item_name2);
                    comHolder.taskCount = (TextView) convertView.findViewById(R.id.task_count);
                    comHolder.tvDesc = (TextView) convertView.findViewById(R.id.text_desc);
                    comHolder.llIterm = (LinearLayout) convertView.findViewById(R.id.ll_item);
                    comHolder.blackView = (View) convertView.findViewById(R.id.black_view);
                    comHolder.lvSectTask = (ListView) convertView.findViewById(R.id.lv_sect_task);

                    convertView.setTag(comHolder);
                } else {
                    comHolder = (ComViewHolder) convertView.getTag();
                }

        if (position < drawable.length)
            comHolder.taskState.setImageResource(drawable[position]);
            if (item.getIsFinished() == 1) {
                comHolder.taskCount.setSelected(true);
                comHolder.taskCount.setText("已完成");
            } else {
                comHolder.taskCount.setSelected(false);
                comHolder.taskCount.setText("未完成");
            }

            if (position == 0) {
                comHolder.userPoint.setVisibility(View.GONE);
                comHolder.jingyan.setVisibility(View.GONE);
                if (item.getIsFinished() == 1) {
                    comHolder.taskCount.setSelected(true);
                    comHolder.taskCount.setText("已完成");
                } else {
                    comHolder.taskCount.setSelected(false);
                    comHolder.taskCount.setText("签到");

                    comHolder.taskCount.setOnClickListener(new View.OnClickListener() {//宗门签到
                        @Override
                        public void onClick(View v) {
                            String sectionId="";
                            UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
                            if (userLastInfo!=null&&userLastInfo.getSectionId()!=null) {
                                sectionId = userLastInfo.getSectionId();
                            }
                            BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SIGN_IN)
                                    .addParams("sectionId", sectionId)
                                    .addParams("action_type", "21")
                                    .build().execute(new BaseCallBack() {
                                @Override
                                public void onLoadFail() {
                                    ToastUtils.showShort(context,"签到失败");
                                }

                                @Override
                                public void onContentNull() {

                                }

                                @Override
                                public void onLoadSuccess(Object response) {
                                    ToastUtils.showShort(context,"签到成功");
                                    comHolder.taskCount.setSelected(true);
                                    comHolder.taskCount.setText("已完成");
                                }

                                @Override
                                public Object parseNetworkResponse(String response) throws Exception {
                                    return response;
                                }
                            });
                        }
                    });
                }
            }

            if (position == 3) {
                comHolder.blackView.setVisibility(View.GONE);
                comHolder.taskTitle.setText("完成每日所有答题");
                comHolder.item2.setVisibility(View.VISIBLE);
                if (item.getIsFinished()==0){
                    comHolder.taskCount.setText("去完成");
                    comHolder.taskCount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, QuestionActivity.class));
                        }
                    });
                }else{
                    comHolder.llIterm.setEnabled(true);
                    if (success)
                        comHolder.item2.setSelected(true);
                    else
                        comHolder.item2.setSelected(false);
                }
            } else {
                comHolder.item2.setVisibility(View.GONE);
                comHolder.blackView.setVisibility(View.VISIBLE);
                comHolder.taskTitle.setText(item.getTaskName());
            }


            if (position == 1 && item.getTaskList()!=null&&item.getTaskList().size()!=0) {//// FIXME: 17-12-25
                comHolder.lvSectTask.setVisibility(View.VISIBLE);
                TaskAdpter adpter = new TaskAdpter(item.getTaskList());
                comHolder.lvSectTask.setAdapter(adpter);
                if (adpter != null) {
                    int totalHeight = 0;
                    for (int i = 0; i < comHolder.lvSectTask.getCount(); i++) {
                        View listItem = adpter.getView(i, null, comHolder.lvSectTask);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }
                    ViewGroup.LayoutParams params = comHolder.lvSectTask.getLayoutParams();
                    params.height = totalHeight + (comHolder.lvSectTask.getDividerHeight() * (adpter.getCount() - 1));
                    comHolder.lvSectTask.setLayoutParams(params);

                }
            }else{
                comHolder.lvSectTask.setVisibility(View.GONE);
            }

            comHolder.userPoint.setText("+"+item.getExp()+"");
            comHolder.userContribution.setText("+"+item.getContributions()+"");


        return convertView;

    }


    class ComViewHolder {
        TextView     taskTitle;
        TextView     userPoint;
        TextView     jingyan;
        TextView     tvDesc;
        TextView     taskCount;
        TextView     userContribution;
        TextView     item2;
        ImageView    taskState;
        View         blackView;
        ListView   lvSectTask;
        LinearLayout llIterm;


    }

    public class TaskAdpter extends BaseAdapter{
        private  List<SectTask.TaskList> list;


        public TaskAdpter(List<SectTask.TaskList> taskList) {
            this.list =taskList;
        }

        @Override
        public int getCount() {
            return list!=null?list.size():0;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.sect_task_item2, null);

                View line1 = convertView.findViewById(R.id.line1);
                View line2 = convertView.findViewById(R.id.line2);
                TextView itemName = (TextView) convertView.findViewById(R.id.item_name);

                SectTask.TaskList task = list.get(position);
                itemName.setText(task.getAction_name());
                if (position==0)
                    line1.setVisibility(View.GONE);
                else if (position == list.size() - 1) {

                    line2.setVisibility(View.GONE);
                } else {
                    line1.setVisibility(View.VISIBLE);
                    line2.setVisibility(View.VISIBLE);
                }

                if (task.getIsFinished()==1)
                    itemName.setSelected(true);
                else
                    itemName.setSelected(false);

            }
            return convertView;
        }
    }

}
