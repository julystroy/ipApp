package com.cartoon.module.mytask;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.bean.TaskBeanRes;
import com.cartoon.data.EventRefresh;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-11-29.
 */
public class MyTaskActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.bt_left)
    ImageButton  mBtLeft;
    @BindView(R.id.tv_title)
    TextView     mTvTitle;
    @BindView(R.id.lv_mytask)
    ListView     mLvMytask;


    private MyTaskAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.ac_cartoon_task;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setUpView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null && adapter.getCount() > 0) {
            adapter.notifyDataSetChanged();
        }
    }


    private void setUpView() {
        mBtLeft.setOnClickListener(this);
        mTvTitle.setText(R.string.my_task);
        adapter = new MyTaskAdapter(MyTaskActivity.this);
        mLvMytask.setDivider(null);
        mLvMytask.setAdapter(adapter);
        onLoadData();

    }

    private void onLoadData() {
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_USER_TASK)
                .build().execute(new BaseCallBack<TaskBeanRes>() {
            @Override
            public void onLoadFail() {
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(TaskBeanRes response) {

                adapter.setData(response.getTasks());
                //显示经验加倍
//                if (response.getTypes() != null && response.getTypes().size() > 0) {
//                    StringBuilder string = new StringBuilder();
//                    for (int i = 0; i < response.getTypes().size(); i++) {
//                        string.append(response.getTypes().get(i));
//                        if (i != response.getTypes().size() - 1)
//                            string.append("/");
//                    }
//                    mBonusTitle.setVisibility(View.VISIBLE);
//                    mBonusTitle.setText(string);
//                }

            }

            @Override
            public TaskBeanRes parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, TaskBeanRes.class);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
        }
    }

    @Subscribe
    public void onEvent(EventRefresh f) {

        onLoadData();

    }

}
