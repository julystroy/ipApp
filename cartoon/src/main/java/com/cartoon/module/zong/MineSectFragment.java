package com.cartoon.module.zong;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.Keys;
import com.cartoon.data.SectAgree;
import com.cartoon.data.SectUi;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.MineSectRes;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.tab.adapter.MineSectListAdpter;
import com.cartoon.utils.CollectionUtils;
import com.cartoon.view.CustomItemDecoration;
import com.cartoon.view.DialogToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by cc on 17-9-25.
 */
public class MineSectFragment extends BaseFragment implements MineSectListAdpter.ButtonClick, EndLessListener {
    private static final String TAG ="MineSectFragment" ;

    @BindView(R.id.recycleview)
    EndLessRecyclerView mRecycleview;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private MineSectListAdpter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fg_mine_sect;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        adapter = new MineSectListAdpter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.addItemDecoration(new CustomItemDecoration((int) getResources().getDimension(R.dimen.dp3), 1));
        mRecycleview.setNestedScrollingEnabled(false);
        mRecycleview.setAdapter(adapter);
        adapter.setOnClickSubViewListener(this);
        mRecycleview.setEndLessListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadSectInformation(START_PAGE);
    }


    private boolean refresh =false;
    private int count ;

    private void loadSectInformation(final int page) {
        String sectionId="";
        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userLastInfo!=null&&userLastInfo.getSectionId()!=null) {
            sectionId = userLastInfo.getSectionId();
        }
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SECTMINELIST)
                .addParams("sectionId",sectionId )
                .addParams(Keys.PAGE_NUM, String.valueOf(page))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .build().execute(new BaseCallBack<MineSectRes>() {
            @Override
            public void onLoadFail() {
                if (mProgressBar!=null)
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onContentNull() {
            }

            @Override
            public void onLoadSuccess(MineSectRes response) {
                setupLoadMoreList(response);
                if (mProgressBar!=null)
                    mProgressBar.setVisibility(View.GONE);

                if (response != null) {
                    count =response.getTotalPage();
                    if (page == START_PAGE) {
                        adapter.setData(response);
                    } else {
                        adapter.addData(response);
                    }
                }
                mRecycleview.completeLoadMore();
            }

            @Override
            public MineSectRes parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, MineSectRes.class);
            }
        });
    }

    private void setupLoadMoreList(MineSectRes response) {
        if (CollectionUtils.isEmpty(response.getList())
                || response.getList().size() < PAGE_SIZE) {
            mRecycleview.setEndLessListener(null);
           // mRecycleview.completeLoadMore();
        } else {
            mRecycleview.setEndLessListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        mProgressBar.setVisibility(View.VISIBLE);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(SectAgree d) {
        loadSectInformation(START_PAGE);
    }



    @Override
    public void buttonClick(View view, final boolean isBoss) {
        new MaterialDialog.Builder(getContext()).title(isBoss ? "确定解散宗门？" : "确定退出宗门")
                .content(isBoss ? "解散后此宗门的相关信息将被删除，且宗门内成员24小时内不能加入新宗门，请慎重考虑！" : "退出后你将在24小时内不能加入宗门")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        String sectionId="";
                        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
                        if (userLastInfo!=null&&userLastInfo.getSectionId()!=null) {
                            sectionId = userLastInfo.getSectionId();
                        }
                        //退出或者解散
                        BuilderInstance.getInstance().getPostBuilderInstance(isBoss ? StaticField.URL_SECT_SECTADISSOLVEW : StaticField.URL_SECT_SECTEXIT)
                                .addParams("sectionId", sectionId)
                                .build().execute(new BaseCallBack<String>() {
                            @Override
                            public void onLoadFail() {

                            }

                            @Override
                            public void onContentNull() {

                            }

                            @Override
                            public void onLoadSuccess(String response) {
                                DialogToast.createToastConfig().ToastShow(getContext(), response.toString(), 4);
                                EventBus.getDefault().post(new SectUi());
                            }

                            @Override
                            public String parseNetworkResponse(String response) throws Exception {
                                return response;
                            }
                        });
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                }).show();
    }

    int page =1;
    @Override
    public void onLoadMoreData(int i) {
        if (i<=count) {
            loadSectInformation(i);
        }else
        loadSectInformation(page++);
    }
}
