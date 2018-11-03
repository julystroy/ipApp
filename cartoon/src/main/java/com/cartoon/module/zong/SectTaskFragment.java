package com.cartoon.module.zong;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.SectTask;
import com.cartoon.data.UserInfo;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.tab.adapter.SectTaskAdapter;
import com.cartoon.view.MyListview;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-9-25.
 */
public class SectTaskFragment extends BaseFragment {
    @BindView(R.id.lv_sect_task)
    MyListview   mLvSectTask;
    @BindView(R.id.ll_task_desc)
    LinearLayout mLlTaskDesc;

    private SectTaskAdapter adapter;

    private String[] desc = {"1.答对4到6道题 经验值+30 宗门贡献+10"
            ,"2.答对6到8道题 经验值+50 宗门贡献+30"
            ,"3.答对8到9道题 经验值+80 宗门贡献+50",
            "4.答对10道题 经验值+100 宗门贡献+80",};
    @Override
    protected int getLayoutId() {
        return R.layout.fg_sect_task;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        adapter = new SectTaskAdapter(getContext());
        mLvSectTask.setDivider(null);
        mLvSectTask.setAdapter(adapter);

        for (int i = 0; i < desc.length; i++) {
            View subView = buildItemView( desc, i);
            mLlTaskDesc.addView(subView);
        }
        loadTask();
    }
    @NonNull
    private View buildItemView( String[] imgId1, int i) {
        View subView = LayoutInflater.from(getContext()).inflate(R.layout.item_task_desc, null);
        TextView textView = (TextView) subView.findViewById(R.id.text_desc);
        textView.setText(imgId1[i]);
        return subView;
    }

    private void loadTask() {
        String sectionId="";
        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userLastInfo!=null&&userLastInfo.getSectionId()!=null) {
            sectionId = userLastInfo.getSectionId();
        }
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SECTTASK)
                .addParams("sectionId",sectionId)
       .build().execute(new BaseCallBack<List<SectTask>>() {
            @Override
            public void onLoadFail() {
            }
            @Override
            public void onContentNull() {
            }
            @Override
            public void onLoadSuccess(List<SectTask> response) {
                adapter.setData(response);
            }
            @Override
            public List<SectTask> parseNetworkResponse(String response) throws Exception {
                return JSON.parseArray(response, SectTask.class);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
