package com.cartoon.module.changxian.detail.comment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cartoon.account.AccountHelper;
import com.cartoon.data.Keys;
import com.cartoon.data.LikeResponse;
import com.cartoon.http.StaticField;
import com.cartoon.module.changxian.base.BaseFragment;
import com.cartoon.uploadlog.UploadLog;
import com.cartoon.view.MaterialRatingBar;
import com.cartoon.view.RatingBar;
import com.cartoon.volley.GsonRequest;
import com.cartoon.volley.VolleySingleton;
import com.cartton.library.utils.DebugLog;
import com.umeng.socialize.utils.SocializeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.xuanjiezhimen.R;

public class EditCommentFragment extends BaseFragment {
    private String mAppName;
    private long mId;
    private boolean mHasRating;
    private int mEvaluateNum = 0;
    private String mEvaluateText = "";

    private MaterialRatingBar mSelfRating;
    private View mRatingView;
    private EditText mCommentET;
    private MenuItem  mSubmitItem;

    public void setHasRating(boolean hasRating) {
        if (!hasRating) {
            ((LinearLayout)findViewById(R.id.ll_rootView)).removeView(mRatingView);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        mId = intent.getLongExtra(Keys.EXTRA_APP_ID, -1);
        mAppName = intent.getStringExtra(Keys.EXTRA_APP_NAME);
        mHasRating = intent.getBooleanExtra(Keys.EXTRA_HAS_RATING, false);
        setTitle(mAppName);
        DebugLog.d("app name:" + mAppName + ", id: " + mId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_edit_comment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
        mRatingView = findViewById(R.id.ll_ratingView);
        mSelfRating = (MaterialRatingBar)findViewById(R.id.mb_self_rating);
        mCommentET = (EditText)findViewById(R.id.et_comment);
        setHasRating(mHasRating);
        mSelfRating.setMax(5);
        mSelfRating.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                if (!mSubmitItem.isEnabled()) {
                    mSubmitItem.setEnabled(true);
                }
                DebugLog.d("set value change");
                setRatingStarNum((int)rating);
            }
        });

        mCommentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mEvaluateText = mCommentET.getText().toString();
                if (!mHasRating) {
                    if (!mSubmitItem.isEnabled()){
                        if (mEvaluateText.length() > 0) mSubmitItem.setEnabled(true);
                    }else {
                        if (mEvaluateText.length() == 0) mSubmitItem.setEnabled(false);
                    }
                }
            }
        });
    }

    private void setRatingStarNum(int startNum) {
        DebugLog.d("startNum: " + startNum);
        if (startNum == 0){
            return;
        }
        int[] TEXT_ID = {R.string.evaluate_1, R.string.evaluate_2, R.string.evaluate_3, R.string.evaluate_4, R.string.evaluate_5};
        int[] IMAGE_ID = {R.drawable.star_focus_edit1, R.drawable.star_focus_edit2,
                R.drawable.star_focus_edit3, R.drawable.star_focus_edit4,
                R.drawable.star_focus_edit5};
        TextView ratingText = (TextView)findViewById(R.id.tv_title);
        ratingText.setTextColor(ColorStateList.valueOf(RatingBar.COLORS[mSelfRating.getNumStars() - startNum]));

        ratingText.setText(getResources().getString(TEXT_ID[startNum - 1]));
        mSelfRating.setProgressImageId(getContext(), IMAGE_ID[startNum - 1]);
        mEvaluateNum = startNum;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, 0, 0, "提交").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mSubmitItem = menu.findItem(0);
        mSubmitItem.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                if (mHasRating){
                    if (mEvaluateNum > 0)
                        doRating();
                } else {
                    if (mEvaluateText.length() > 0) doRating();
                }
                break;
            default:
                DebugLog.d("onOptionsItemSelected click item: " + item.getItemId());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doRating() {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        SocializeUtils.safeShowDialog(dialog);
        String url = String.format("%s/api/apps/evaluate", StaticField.BASE_CXURL);
        JSONObject params = new JSONObject();
        try {
            params.put("appId", mId);
            if (mHasRating) {
                if (mEvaluateText.length() > 0){
                    params.put("rating", mEvaluateNum*2);
                    params.put("content", mEvaluateText);
                }else {
                    params.put("rating", mEvaluateNum*2);
                }
            }else {
                params.put("content", mEvaluateText);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugLog.d("do rating url = " + url + " parm = " + params);
        GsonRequest<LikeResponse> request = new GsonRequest<LikeResponse>(Request.Method.POST, url, params.toString(), LikeResponse.class, new Response.Listener<LikeResponse>() {
            @Override
            public void onResponse(LikeResponse response) {
                long commentId = response.data.id;
                DebugLog.d("rating success. id: " + commentId);
                endNetwork(true, commentId, dialog);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.d("rating failed. e = " + error.toString());
                endNetwork(false, -1, dialog);
            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void endNetwork(boolean isSuccess, long commentId, ProgressDialog dialog) {
        SocializeUtils.safeCloseDialog(dialog);
        if (isSuccess) {
            Toast.makeText(getContext(), R.string.upload_evaluate_success, Toast.LENGTH_SHORT).show();
            Intent it = new Intent(Keys.SEND_DETAIL_UPDATE_EVALUATE_BROADCAST);
            if (mHasRating) {
                //评分完成，拉取一次未评分数，确定是否显示红点
                AccountHelper.getLatestUserInfo(getContext());

                it.putExtra(Keys.EXTRA_RATING, mEvaluateNum * 2);
                UploadLog.uploadPageActionCompleteLog(UploadLog.RATING_BUTTON, mId, getActivity());
            }
            if (mEvaluateText.length() > 0) {
                it.putExtra(Keys.EXTRA_COMMENT_CONTENT, mEvaluateText);
                UploadLog.uploadPageActionCompleteLog(UploadLog.COMMENT_BUTTON, mId, getActivity());
            }
            if (commentId != -1) {
                it.putExtra(Keys.EXTRA_COMMENT_ID, commentId);
            }
                it.putExtra(Keys.EXTRA_APP_ID, mId);
                getContext().sendBroadcast(it);
        }else {
            Toast.makeText(getContext(), R.string.upload_evaluate_fail, Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent();
        getActivity().setResult(2, intent);
        getActivity().finish();
    }
}