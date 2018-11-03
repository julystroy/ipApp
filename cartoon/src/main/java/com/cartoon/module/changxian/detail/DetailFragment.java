package com.cartoon.module.changxian.detail;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cartoon.data.AppFeature;
import com.cartoon.data.DetailPage;
import com.cartoon.data.DetailPageResponse;
import com.cartoon.data.FeedbackReason;
import com.cartoon.data.FeedbackReasonRespones;
import com.cartoon.data.Keys;
import com.cartoon.data.PackageInfoRecord;
import com.cartoon.data.QuickInfo;
import com.cartoon.data.game.DownloadGame;
import com.cartoon.data.response.EmptyResponse;
import com.cartoon.http.StaticField;
import com.cartoon.module.changxian.base.BaseFragment;
import com.cartoon.module.changxian.detail.account.TabAccountFragment;
import com.cartoon.module.changxian.detail.comment.EditCommentActivity;
import com.cartoon.module.changxian.detail.comment.TabCommentFragment;
import com.cartoon.module.changxian.detail.detailinfo.TabDetailInfoFragment;
import com.cartoon.sdk.PlaySdk;
import com.cartoon.uploadlog.UploadLog;
import com.cartoon.utils.UIHelper;
import com.cartoon.utils.UserDB;
import com.cartoon.utils.Util;
import com.cartoon.utils.Utils;
import com.cartoon.view.DownloadButton;
import com.cartoon.view.EmptyView;
import com.cartoon.view.SlidingTabLayout;
import com.cartoon.view.dialog.FeedbackDialog;
import com.cartoon.volley.GsonRequest;
import com.cartoon.volley.VolleySingleton;
import com.cartton.library.utils.DebugLog;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.utils.SocializeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import cn.com.xuanjiezhimen.R;
import cn.idianyun.streaming.data.AppInfoResponse;
import cn.idianyun.streaming.data.LaunchInfo;

public class DetailFragment extends BaseFragment implements ObservableScrollViewCallbacks {
//    private static final Logger LOG = LoggerFactory.getLogger(DetailFragment.class);

    private static final int TAB_DETAIL = 0;
    private static final int TAB_ACCOUNT = 1;
    private static final int TAB_COMMENT = 2;

    private static final String REQUEST_TAG = "REQUEST_TAG";

    private static final int SLIDING_STATE_TOP = 0;
    private static final int SLIDING_STATE_MIDDLE = 1;
    private static final int SLIDING_STATE_BOTTOM = 2;

    private long mId;
    private String mAppName;
    private String mPosterURL;
    private String mIntro;

    private boolean mIsFavoMoved = false;

    private int mPlayType;
    private QuickInfo mQuickInfo;
    private DetailPage mDetailPage;

    private View                         mContentView;
    private Toolbar                      mToolbarView;
    private TouchInterceptionFrameLayout mInterceptionLayout;
    private ImageView                    mGoBack;
    private TextView                     mHeaderTitle;
    private DetailCategoryItem           mCategoryItem;
    private ViewPager                    mViewPager;
    private SlidingTabLayout             mSlidingTabLayout;
    private NavigationAdapter            mPagerAdapter;

    ProgressDialog dialog;
    private LinearLayout mLayoutPlay;
    private Button mTryPlayButton;
    private Button mDownloadPlayButton;
    private DownloadButton mDownloadButton;
    private Button mCommentButton;

    private int mSlidingState;
    private int mIntersectionHeight;
    private int mHeaderBarHeight;
    private int mSlidingSlop;
    private float mScrollYOnDownMotion;
    private float mInitialY;
    private float mMovedDistanceY;
    private boolean mMoved;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        mId = intent.getLongExtra(Keys.EXTRA_APP_ID, -1);
        mAppName = intent.getStringExtra(Keys.EXTRA_APP_NAME);

        if (savedInstanceState == null) {
            mSlidingState = SLIDING_STATE_MIDDLE;
        }
        mIntersectionHeight = Utils.dp2px(getContext(), 0);
        mHeaderBarHeight = Utils.dp2px(getContext(), 48);
        mSlidingSlop = Utils.dp2px(getContext(), 32);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();

        IntentFilter updateEvaluateFilter = new IntentFilter(Keys.SEND_DETAIL_UPDATE_EVALUATE_BROADCAST);
        getContext().registerReceiver(mUpdateEvaluateReceiver, updateEvaluateFilter);
        IntentFilter deleteCommentFilter = new IntentFilter(Keys.SEND_DETAIL_DELETE_COMMENT_BROADCAST);
        getContext().registerReceiver(mDeleteCommentReceiver, deleteCommentFilter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showEmptyView(EmptyView.STATE_INIT);
        loadData();
    }

    @Override
    protected void loadData() {
        loadDataInner(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mDetailPage != null) {
            mDownloadButton.update(); //更新下载按钮状态
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SocializeUtils.safeCloseDialog(dialog);
    }

    @Override
    public void onDestroyView() {
        mViewPager.removeOnPageChangeListener(mOnPageChangeListener);
        getContext().unregisterReceiver(mUpdateEvaluateReceiver);
        getContext().unregisterReceiver(mDeleteCommentReceiver);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (mIsFavoMoved) {
            Intent intent = new Intent(Keys.SEND_DO_FAVOR_BROADCAST);
            intent.putExtra(Keys.EXTRA_APP_ID, mId);
            getContext().sendBroadcast(intent);
        }
        super.onDestroy();

        //页面退出取消请求
        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(REQUEST_TAG);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    protected View getContentView() {
        return mContentView;
    }

    private void initViews() {
        mContentView = findViewById(R.id.layout_content);
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);

        mHeaderTitle = (TextView) findViewById(R.id.header_title);
        mGoBack = (ImageView) findViewById(R.id.go_back);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCategoryItem = (DetailCategoryItem) findViewById(R.id.item_category);
        mLayoutPlay = (LinearLayout) findViewById(R.id.layout_play);
        mTryPlayButton = (Button) findViewById(R.id.btn_try);
        mDownloadPlayButton = (Button) findViewById(R.id.btn_download_play);
        mDownloadButton = (DownloadButton) findViewById(R.id.btn_download);
        mCommentButton = (Button) findViewById(R.id.btn_comment);
        setupActionListener();

        mInterceptionLayout = (TouchInterceptionFrameLayout) findViewById(R.id.scroll_wrapper);
        mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);
        ScrollUtils.addOnGlobalLayoutListener(mInterceptionLayout, new Runnable() {
            @Override
            public void run() {
                changeSlidingState(mSlidingState, false);
            }
        });

        mPagerAdapter = new NavigationAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount() - 1);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mPagerAdapter.setListener(new TabCommentFragment.CommentListener() {
            @Override
            public void updateCommentCount() {
                updateCommentText();
            }
        });

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1, android.R.id.text2, R.drawable.comment_count_gray_bg);
        mSlidingTabLayout.setSelectedIndicatorThickness(Utils.dp2px(getContext(), 2));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.subtitle_text_green));
        mSlidingTabLayout.setDividerColors(getResources().getColor(R.color.transparent));
    }

    private void setupActionListener() {
        ButtonClickListener listener = new ButtonClickListener();
        mTryPlayButton.setOnClickListener(listener);
        mDownloadPlayButton.setOnClickListener(listener);
        mCommentButton.setOnClickListener(listener);
        findViewById(R.id.action_favo).setOnClickListener(listener);
        findViewById(R.id.action_doubt).setOnClickListener(listener);
        findViewById(R.id.action_share).setOnClickListener(listener);
    }

    public void selectCommentTab() {
        mViewPager.setCurrentItem(TAB_COMMENT, true);
    }

    protected void loadDataInner(final boolean refresh) {
        String url = String.format("%s/api/apps/detail?id=%d", StaticField.BASE_CXURL, mId);
        DebugLog.d("loadDataInner. url = " + url);

        GsonRequest<DetailPageResponse> gsonRequest = new GsonRequest<>(url, DetailPageResponse.class, new Response.Listener<DetailPageResponse>() {
            @Override
            public void onResponse(DetailPageResponse response) {
                mDetailPage = response.data;

                DebugLog.d("response.data = " + mDetailPage.user.favorite);

                if (refresh) {
                    mPagerAdapter.mDetailInfoFragment.setData(mDetailPage);
                    mPagerAdapter.mCommentFragment.setData(mDetailPage);
                } else {
                    mPlayType = mDetailPage.playType;
                    mQuickInfo = mDetailPage.quickInfo;

                    mCategoryItem.setData(mDetailPage);
                    mPagerAdapter.setData(mDetailPage);

                    mSlidingTabLayout.setViewPager(mViewPager);
                    if (mPagerAdapter.getCount() > 2) {
                        mSlidingTabLayout.setRedSpot(1);
                    }
                    updateCommentText();
                    mIntro = mDetailPage.intro;
                    mPosterURL = String.format("%s%s", StaticField.BASE_CXURL, mDetailPage.logo);

                    setFavorBtn(mDetailPage.user.favorite);
                    setupPlayButton(mDetailPage);

                    PackageInfoRecord packageInfo = PackageInfoRecord.find(mDetailPage.appId);
                    if (packageInfo == null) {
                        if (mPlayType == 2) {
                            packageInfo = new PackageInfoRecord();
                            packageInfo.appId = mDetailPage.appId;
                            packageInfo.packageName = mQuickInfo.packageName;
                            packageInfo.saveRecord();

                            setupDownloadButton(mQuickInfo.packageName);
                            showContentView();
                        } else {
                            loadAppInfo();
                        }
                    } else {
                        setupDownloadButton(packageInfo.packageName);
                        showContentView();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.d("onErrorResponse. error = " + error);

                showEmptyView(EmptyView.STATE_FAIL); // 加载失败,显示空页面
            }
        });

        gsonRequest.setTag(REQUEST_TAG);
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(gsonRequest);
    }

    private void updateCommentText() {
        if (mPagerAdapter.getCount() > 2) {
            mSlidingTabLayout.setCountInPos(2, mDetailPage.commentCount);
        } else {
            mSlidingTabLayout.setCountInPos(1, mDetailPage.commentCount);
        }
    }

    private BroadcastReceiver mUpdateEvaluateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long appId = intent.getLongExtra(Keys.EXTRA_APP_ID, -1);
            long commentId = intent.getLongExtra(Keys.EXTRA_COMMENT_ID, -1);
            String commentText = intent.getStringExtra(Keys.EXTRA_COMMENT_CONTENT);
            int rating = intent.getIntExtra(Keys.EXTRA_RATING, -1);
            if (appId == mId) {
                //TODO:如果用服务器刷新的数据，则无需更新commentText
//                if (commentText != null) {
//                    if (commentText.length() > 0 && commentId != -1) {
//                        mDetailPage.commentCount++;
//                        updateCommentText();
//                    }
//                }
                if (rating > 0) {
                    mDetailPage.user.rating = rating;
                    mDetailPage.ratingDetail.addRating(rating);
                    mCategoryItem.setRating(mDetailPage.ratingDetail);
                    mPagerAdapter.mCommentFragment.refreshRating();
                }
            }
        }
    };

    private BroadcastReceiver mDeleteCommentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long appId = intent.getLongExtra(Keys.EXTRA_APP_ID, -1);
            long commentId = intent.getLongExtra(Keys.EXTRA_COMMENT_ID, -1);
            if (appId != mDetailPage.id)
                return;
            if (commentId != -1) {
                mDetailPage.commentCount--;
                updateCommentText();
            }
        }
    };

    private void loadAppInfo() {
        String url = String.format(Locale.US, "%s/api/apps/item/%d", StaticField.BASE_CXAPPURL, mDetailPage.appId);
        DebugLog.d("loadAppInfo. url = " + url);

        GsonRequest<AppInfoResponse> gsonRequest = new GsonRequest<>(url, AppInfoResponse.class, new Response.Listener<AppInfoResponse>() {
            @Override
            public void onResponse(AppInfoResponse response) {
                DebugLog.d("loadAppInfo. response = " + response);

                PackageInfoRecord packageInfo = PackageInfoRecord.get(mDetailPage.appId);
                packageInfo.packageName = response.data.packageName;
                packageInfo.saveRecord();

                setupDownloadButton(response.data.packageName);
                showContentView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.d("onErrorResponse. error = " + error);
                showEmptyView(EmptyView.STATE_FAIL); // 加载失败,显示空页面
            }
        });
        gsonRequest.setTag(REQUEST_TAG);
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(gsonRequest);
    }

    private void setupPlayButton(DetailPage detailPage) {
        switch (detailPage.playType) {
            case DetailPage.PLAY_TYPE_NONE:
                mLayoutPlay.setVisibility(View.GONE);
                break;
            case DetailPage.PLAY_TYPE_BOTH:
                mLayoutPlay.setVisibility(View.VISIBLE);
                mTryPlayButton.setVisibility(View.VISIBLE);
                mDownloadPlayButton.setVisibility(View.VISIBLE);
                break;
            case DetailPage.PLAY_TYPE_PLAY:
                mLayoutPlay.setVisibility(View.VISIBLE);
                mTryPlayButton.setVisibility(View.VISIBLE);
                mDownloadPlayButton.setVisibility(View.GONE);
                break;
            case DetailPage.PLAY_TYPE_INSTALL:
                mLayoutPlay.setVisibility(View.VISIBLE);
                mTryPlayButton.setVisibility(View.GONE);
                mDownloadPlayButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setupDownloadButton(String packageName) {
        mDownloadButton.setStyle(DownloadButton.STYLE_PROGRESS);
        mDownloadButton.setAppId(mId);
        DownloadGame record = UserDB.getInstance().getGameDownload(mDetailPage.androidDownloadUrl);
        if (record != null) {
            record.setPackageName(packageName);
            record.setCxId(mDetailPage.id);
            record.setAppName(mDetailPage.name);
            record.setIconUrl(mDetailPage.logo);
            record.setSize(mDetailPage.size);

            mDownloadButton.setDownloadInfo(record);
        }

        if (TextUtils.isEmpty(mDetailPage.androidDownloadUrl)) {
            mDownloadButton.setBackgroundResource(R.drawable.btn_bg_gray);
            mDownloadButton.setTextColor(0xffffffff);
            mDownloadButton.setEnabled(false);
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == TAB_DETAIL) {
                mLayoutPlay.setVisibility(View.VISIBLE);
                mCommentButton.setVisibility(View.GONE);
            } else if (position == mPagerAdapter.getCount() - 1) {
                mLayoutPlay.setVisibility(View.GONE);
                mCommentButton.setVisibility(View.VISIBLE);
            } else {
                mLayoutPlay.setVisibility(View.GONE);
                mCommentButton.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_favo:
                    doFavor();
                    break;
                case R.id.action_doubt:
                    getFeedbackReasons();
                    break;
                case R.id.action_share:
                    doShare();
                    break;
                case R.id.btn_try:
                case R.id.btn_download_play:
                    LaunchInfo launchInfo = mDetailPage.changeToLaunchInfo();
                    PlaySdk.getInstance().launch(getContext(), (int) mDetailPage.id, launchInfo, new PlaySdk.OnDownloadListener() {
                        @Override
                        public void onDownload(String url, String pkgName) {
                            mDownloadButton.addDListener();
                            DownloadGame record = mDownloadButton.getDownloadRecord();
                            if (record != null) {
                                UserDB.getInstance().saveGameDownload(record);
                            }
                        }
                    });
                    getActivity().setResult(2);
                    break;
//                case R.id.btn_download_play:
//                    LaunchingActivity.launch(getContext(), mId, mAppName, mQuickInfo);
//                    break;
                case R.id.btn_comment:
                    EditCommentActivity.launch(getActivity(), mAppName, mId, false);
                    break;
            }
        }
    }

    private void getFeedbackReasons() {
        String url = String.format("%s/api/feedbacks/reasons", StaticField.BASE_CXURL);

        dialog = new ProgressDialog(getActivity());
        dialog.show();
        GsonRequest<FeedbackReasonRespones> request = new GsonRequest<FeedbackReasonRespones>(url, FeedbackReasonRespones.class, new Response.Listener<FeedbackReasonRespones>() {
            @Override
            public void onResponse(FeedbackReasonRespones response) {
                showFeedbackDialog(response.data.items);
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UIHelper.showToast(getActivity(), "获取反馈信息失败，请稍后重试", Toast.LENGTH_SHORT);
                dialog.dismiss();
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void showFeedbackDialog(List<FeedbackReason> reasons) {
        new FeedbackDialog.Builder(getContext())
                .setOnClickCancelListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DebugLog.d("click feedback cancel");
                    }
                })
                .setOnClickSubmitListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FeedbackDialog tem = (FeedbackDialog) dialog;
                        DebugLog.d("click feedback submit = " + tem.suggestion);
                        String url = String.format("%s/api/feedbacks", StaticField.BASE_CXURL);
                        JSONObject params = new JSONObject();
                        String reasons = "";
                        for (int i = 0; i < tem.mReasons.size(); i++) {
                            if (i != tem.mReasons.size() - 1) {
                                reasons += (String.valueOf(tem.mReasons.get(i)) + ",");
                            } else {
                                reasons += (String.valueOf(tem.mReasons.get(i)));
                            }
                        }
                        try {
                            params.put("appId", mId);
                            params.put("contact", tem.QQNum);
                            params.put("suggestion", tem.suggestion);
                            params.put("model", Util.getOSModel());
                            params.put("os", Util.getOSVersion() + "||" + Util.getOSFingerprint());
                            params.put("reasons", reasons);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        DebugLog.d("do feedback url = " + url + ", parm = " + params);
                        GsonRequest<EmptyResponse> request = new GsonRequest<EmptyResponse>(Request.Method.POST, url, params.toString(), EmptyResponse.class, new Response.Listener<EmptyResponse>() {
                            @Override
                            public void onResponse(EmptyResponse response) {
                                UIHelper.showToast(getActivity(), getResources().getString(R.string.report_success), Toast.LENGTH_SHORT);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                UIHelper.showToast(getActivity(), getResources().getString(R.string.report_fail), Toast.LENGTH_SHORT);
                            }
                        });
                        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
                    }
                })
                .create(reasons)
                .show();
    }

    private void doFavor() {
        final boolean isFavor = (boolean) findViewById(R.id.action_favo).getTag();
        String url = String.format("%s/api/favorites/%s", StaticField.BASE_CXURL, isFavor ? "remove" : "add");
        JSONObject params = new JSONObject();
        try {
            params.put("appId", mId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GsonRequest<EmptyResponse> request = new GsonRequest<EmptyResponse>(Request.Method.POST, url, params.toString(), EmptyResponse.class, new Response.Listener<EmptyResponse>() {
            @Override
            public void onResponse(EmptyResponse response) {
                setFavorBtn(!isFavor);
                String msg = isFavor ? getResources().getString(R.string.delete_favor_success) : getResources().getString(R.string.do_favor_success);
                UIHelper.showToast(getActivity(), msg, Toast.LENGTH_SHORT);
                mIsFavoMoved = isFavor;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = isFavor ? getResources().getString(R.string.delete_favor_fail) : getResources().getString(R.string.do_favor_fail);
                UIHelper.showToast(getActivity(), msg, Toast.LENGTH_SHORT);
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);

        if (!isFavor) {
            UploadLog.uploadPageActionClickLog(UploadLog.FAVOR_BUTTON, mId, getActivity());
        }
    }

    private void setFavorBtn(boolean isFavor) {
        findViewById(R.id.action_favo).setTag(isFavor);
        findViewById(R.id.action_favo).setBackgroundResource(isFavor ? R.drawable.icon_favor_checked : R.drawable.icon_favo);
    }

    public void doShare() {
        String url = String.format("%s%d", StaticField.SHARE_URL, mId);
        UMWeb web = new UMWeb(url);
        web.setTitle(String.format(getResources().getString(R.string.share_title), mAppName));//标题
        web.setDescription(mIntro);//描述
        UMImage thumb = new UMImage(getActivity(), mPosterURL);
        web.setThumb(thumb);
        ShareBoardConfig config = new ShareBoardConfig();
        config.setIndicatorVisibility(false);

        dialog = new ProgressDialog(getActivity());
        new ShareAction(getActivity())
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setCallback(shareListener)
                .open(config);
        UploadLog.uploadPageActionClickLog(UploadLog.SHARE_BUTTON, mId, getActivity());
    }

    private UMShareListener shareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA platform) {
            DebugLog.d("start share");
            //SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            DebugLog.d("get result");
            Toast.makeText(getActivity(), getResources().getString(R.string.share_success), Toast.LENGTH_LONG).show();
            //SocializeUtils.safeCloseDialog(dialog);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            DebugLog.d("get error");
            //SocializeUtils.safeCloseDialog(dialog);

            String[] splits = t.getLocalizedMessage().split(getResources().getString(R.string.error_info));
            splits = splits[1].split(" ");
            String msg = getResources().getString(R.string.share_error) + splits[0];
            UIHelper.showToast(getActivity(), msg, Toast.LENGTH_SHORT);
            DebugLog.d("share fail, because: " + t.getLocalizedMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            DebugLog.d("cancel share");
            //SocializeUtils.safeCloseDialog(dialog);
        }
    };

    private boolean canDownload(List<AppFeature> features) {
        boolean result = false;
        if (features != null && features.size() > 0) {
            for (AppFeature item : features) {
                if (item.key.contains("以及更高")) {
                    int index = item.key.indexOf("以及更高");
                    String version = item.key.substring(0, index);
                    if (Util.getOSVersion().compareToIgnoreCase(version) >= 0) {
                        result = true;
                    }
                    break;
                }
            }
        } else {
            result = true;
        }
        return result;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
//        LOG.error("onScrollChanged. scrollY = {}, dragging = {}", scrollY, dragging);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mViewPager.getCurrentItem());
    }

    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
            if (diffX == 0 && diffY == 0) {
                return false;
            }
            if ((ViewHelper.getTranslationY(mInterceptionLayout) == mHeaderBarHeight
                    || ViewHelper.getTranslationY(mInterceptionLayout) == getDetailHeaderHeight()) && Math.abs(diffX) > Math.abs(diffY)) {
                return false;
            }

            final Scrollable scrollable = (Scrollable) getCurrentFragment().getView().findViewById(R.id.scroll);
            boolean res = moving && ((int) ViewHelper.getY(mInterceptionLayout) > mHeaderBarHeight
                    || Math.max(scrollable.getCurrentScrollY(), 0) - diffY < 0);
            DebugLog.d("shouldInterceptTouchEvent. moving = " + moving + ", res = " + res);
            return res;
        }

        @Override
        public void onDownMotionEvent(MotionEvent ev) {
            DebugLog.d("onDownMotionEvent. x = " + (int) ev.getX() + ", y = " + (int) ev.getY());
            final Scrollable scrollable = (Scrollable) getCurrentFragment().getView().findViewById(R.id.scroll);
            mScrollYOnDownMotion = scrollable.getCurrentScrollY();
            mInitialY = ViewHelper.getTranslationY(mInterceptionLayout);
        }

        @Override
        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
            DebugLog.d("onMoveMotionEvent. diffX = " + diffX + ", diffY = " + diffY);
            DebugLog.d("onMoveMotionEvent. getTranslationY = " + ViewHelper.getTranslationY(mInterceptionLayout) + ", mScrollYOnDownMotion = " + mScrollYOnDownMotion);
            mMoved = true;
            float translationY = ViewHelper.getTranslationY(mInterceptionLayout) + diffY;
            if (translationY < -mIntersectionHeight) {
                translationY = -mIntersectionHeight;
            } else if (UIHelper.getScreenHeight(getContext()) - mHeaderBarHeight < translationY) {
                translationY = UIHelper.getScreenHeight(getContext()) - mHeaderBarHeight;
            }

            DebugLog.d("onMoveMotionEvent. translationY = " + translationY);
            if (translationY >= mHeaderBarHeight && translationY <= getDetailHeaderHeight()) {
                slideTo(translationY, true);
            }

            mMovedDistanceY = ViewHelper.getTranslationY(mInterceptionLayout) - mInitialY;
        }

        @Override
        public void onUpOrCancelMotionEvent(MotionEvent ev) {
            DebugLog.d("onUpOrCancelMotionEvent. x = " + (int) ev.getX() + ", y = " + (int) ev.getY());
            if (!mMoved) {
                // Invoke slide animation only on header view
                Rect outRect = new Rect();
                mToolbarView.getHitRect(outRect);
                if (outRect.contains((int) ev.getX(), (int) ev.getY())) {
                    //slideOnClick();
                }
            } else {
                stickToAnchors();
            }
            mMoved = false;
        }
    };

    private void slideTo(float translationY, final boolean animated) {
        ViewHelper.setTranslationY(mInterceptionLayout, translationY);

        if (translationY < 0) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
            lp.height = (int) - translationY + UIHelper.getScreenHeight(getContext());
            mInterceptionLayout.requestLayout();
        }

        ViewHelper.setTranslationY(mCategoryItem, translationY * 0.6f);
        float alpha = (translationY - mHeaderBarHeight) / getResources().getDimension(R.dimen.detail_header_height);
        if (alpha > 1) {
            alpha = 1;
        }
        if (alpha < 0) {
            alpha = 0;
        }
        mCategoryItem.setAlpha(alpha);
        if (mHeaderTitle != null) {
            mHeaderTitle.setText(mAppName);
            mHeaderTitle.setAlpha(1 - alpha);
        }
    }

    private void stickToAnchors() {
        // Slide to some points automatically
        if (0 < mMovedDistanceY) {
            // Sliding down
            if (mSlidingSlop < mMovedDistanceY) {
                // Sliding down to an anchor
                changeSlidingState(SLIDING_STATE_MIDDLE, true);
            } else {
                // Sliding up(back) to an anchor
                if (getDetailHeaderHeight() < ViewHelper.getTranslationY(mInterceptionLayout)) {
                    changeSlidingState(SLIDING_STATE_MIDDLE, true);
                } else {
                    changeSlidingState(SLIDING_STATE_TOP, true);
                }
            }
        } else if (mMovedDistanceY < 0) {
            // Sliding up
            if (mMovedDistanceY < -mSlidingSlop) {
                // Sliding up to an anchor
                if (getDetailHeaderHeight() < ViewHelper.getTranslationY(mInterceptionLayout)) {
                    changeSlidingState(SLIDING_STATE_MIDDLE, true);
                } else {
                    changeSlidingState(SLIDING_STATE_TOP, true);
                }
            } else {
                // Sliding down(back) to an anchor
                if (getDetailHeaderHeight() < ViewHelper.getTranslationY(mInterceptionLayout)) {
                    changeSlidingState(SLIDING_STATE_BOTTOM, true);
                } else {
                    changeSlidingState(SLIDING_STATE_MIDDLE, true);
                }
            }
        }
    }

    private void changeSlidingState(final int slidingState, boolean animated) {
        DebugLog.d("changeSlidingState. sldingState = " + slidingState);
        mSlidingState = slidingState;
        float translationY = 0;
        switch (slidingState) {
            case SLIDING_STATE_TOP:
                translationY = mToolbarView.getHeight();
                break;
            case SLIDING_STATE_MIDDLE:
                translationY = getDetailHeaderHeight();
                break;
            case SLIDING_STATE_BOTTOM:
                translationY = getAnchorYBottom();
                break;
        }
        if (animated) {
            slideWithAnimation(translationY);
        } else {
            slideTo(translationY, false);
        }
    }

    private void slideOnClick() {
        float translationY = ViewHelper.getTranslationY(mInterceptionLayout);
        if (translationY == getAnchorYBottom()) {
            changeSlidingState(SLIDING_STATE_MIDDLE, true);
        } else if (translationY == getDetailHeaderHeight()) {
            changeSlidingState(SLIDING_STATE_BOTTOM, true);
        }
    }

    private void slideWithAnimation(float toY) {
        DebugLog.d("slideWithAnimation . toY = " + toY);
        float layoutTranslationY = ViewHelper.getTranslationY(mInterceptionLayout);
        if (layoutTranslationY != toY) {
            ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mInterceptionLayout), toY).setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    slideTo((float) animation.getAnimatedValue(), true);
                }
            });
            animator.start();
        }
    }

    private float getAnchorYBottom() {
        return UIHelper.getScreenHeight(getContext()) - mHeaderBarHeight;
    }

    private float getDetailHeaderHeight() {
        return getResources().getDimension(R.dimen.detail_header_height) + mHeaderBarHeight;
    }

    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private String[] mTitles;
        private DetailPage mDetailPage;
        private TabCommentFragment.CommentListener listener;

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        public TabCommentFragment mCommentFragment;
        public TabDetailInfoFragment mDetailInfoFragment;

        public void setListener(TabCommentFragment.CommentListener listener) {
            this.listener = listener;
        }

        public void setData(DetailPage detailPage) {
            mDetailPage = detailPage;
            if (detailPage.accounts != null && detailPage.accounts.size() > 0) {
                mTitles = new String[]{"详情", "精品账号", "评论"};
            } else {
                mTitles = new String[]{"详情", "评论"};
            }
            notifyDataSetChanged();
        }

        @Override
        protected Fragment createItem(int position) {
            DebugLog.d("createItem. pos = " + position);

            // Initialize fragments.
            // Please be sure to pass scroll position to each fragments using setArguments.
            Fragment f = null;
            switch (position) {
                case 0:
                    TabDetailInfoFragment detailInfoFragment = new TabDetailInfoFragment();
                    detailInfoFragment.setData(mDetailPage);
                    f = detailInfoFragment;
                    mDetailInfoFragment = detailInfoFragment;
                    break;
                case 1:
                    if (getCount() == 2) {
                        TabCommentFragment commentFragment = new TabCommentFragment();
                        commentFragment.setData(mDetailPage);
                        f = commentFragment;
                        mCommentFragment = commentFragment;
                        if (listener != null) {
                            mCommentFragment.setCommentListener(listener);
                        }
                    } else {
                        TabAccountFragment accountFragment = new TabAccountFragment();
                        accountFragment.setData(mDetailPage);
                        f = accountFragment;
                    }
                    break;
                case 2:
                    TabCommentFragment commentFragment = new TabCommentFragment();
                    commentFragment.setData(mDetailPage);
                    f = commentFragment;
                    mCommentFragment = commentFragment;
                    if (listener != null) {
                        mCommentFragment.setCommentListener(listener);
                    }
                    break;
                default:
                    break;
            }
            return f;
        }

        @Override
        public int getCount() {
            return mTitles != null ? mTitles.length : 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles != null ? mTitles[position] : "";
        }
    }
}
