package com.cartoon.module.game;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.account.AccountHelper;
import com.cartoon.data.response.UserInfoResponse;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.changxian.download.DownloadActivity;
import com.cartoon.module.game.favor.GameFavorActivity;
import com.cartoon.module.game.history.GameHistoryPlayActivity;
import com.cartton.library.utils.DebugLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;
import okhttp3.Call;
import okhttp3.Response;

import static com.cartoon.module.game.history.GameHistoryPlayActivity.EDIT_RESULT;

/**
 * Created by wuchuchu on 2018/3/1.
 */

public class MineGameActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.bt_left)
    ImageButton mBtLeft;
    @BindView(R.id.tv_title)
    TextView    mTvTitle;
    @BindView(R.id.ll_container)
    LinearLayout mLlContainer;

    @Override
    protected int getContentViewId() {
        return R.layout.fg_mine_game_list;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.mipmap.icon_mine_game_history:
                startActivityForResult(new Intent(this, GameHistoryPlayActivity.class), 1);
//                startActivity(new Intent(this, MineGameHistoryActivity.class));
                break;
            case R.mipmap.icon_mine_game_favor:
                startActivity(new Intent(this, GameFavorActivity.class));
                break;
            case R.mipmap.icon_mine_game_download:
                startActivity(new Intent(this, DownloadActivity.class));
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DebugLog.d("request code:" + requestCode + ", result code:" + resultCode);
        if (requestCode == 1 && resultCode == EDIT_RESULT) {
            View subView = null;
            for (int i = 0; i < mLlContainer.getChildCount(); i ++) {
                View view = mLlContainer.getChildAt(i);
                if (Integer.valueOf(view.getTag().toString()) == R.mipmap.icon_mine_game_history) {
                    subView = view;
                    break;
                }
            }
            if (subView != null) {
                TextView textView = (TextView) subView.findViewById(R.id.tv_extra);
                if (textView != null) {
                    setNoRatingCount(textView);
                }
            }
        }
    }

    private void setupView() {

        mTvTitle.setText(R.string.my_game);
        mBtLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int[] titleArr2;
        int[] imgId2;

        titleArr2 = new int[]{
                R.string.my_game_history,
                R.string.my_game_favor,
                R.string.my_game_download};


        imgId2 = new int[]{
                R.mipmap.icon_mine_game_history,
                R.mipmap.icon_mine_game_favor,
                R.mipmap.icon_mine_game_download};


        for (int i = 0; i < imgId2.length; i++) {
            DebugLog.d("item index:" + i + ", title:" + titleArr2[i]);
            View subView = buildItemView(titleArr2[i], imgId2, i);
            subView.setTag(imgId2[i]);
            mLlContainer.addView(subView);
        }
    }

    private View buildItemView(int resid, int[] imgId1, int i) {
        View subView = LayoutInflater.from(getBaseContext()).inflate(R.layout.part_mine_item, null);

        RelativeLayout item = (RelativeLayout) subView.findViewById(R.id.rl_item);
        item.setId(imgId1[i]);
        item.setOnClickListener(this);
        ImageView icon = (ImageView) subView.findViewById(R.id.iv_icon);
        ImageView arrow = (ImageView) subView.findViewById(R.id.iv_arrow);
        TextView textView = (TextView) subView.findViewById(R.id.tv_text);
        TextView textExtra = (TextView) subView.findViewById(R.id.tv_extra);
        View line = subView.findViewById(R.id.line);
        arrow.setVisibility(View.GONE);
        icon.setImageResource(imgId1[i]);
        textView.setText(resid);
        if (i == imgId1.length - 1) {
            line.setVisibility(View.GONE);
        }

        if (imgId1[i] == R.mipmap.icon_mine_game_history) {
            setNoRatingCount(textExtra);
        }
        return subView;
    }

    private static final String COOKIE_KEY = "Cookie";
    private void setNoRatingCount(final TextView view) {
        GetBuilder get = OkHttpUtils.get();

        String cookie = AccountHelper.getCookie();
        if (!TextUtils.isEmpty(cookie)) {
            get.addHeader(COOKIE_KEY, cookie);
        }

        get.url(StaticField.URL_CHANGXIAN_USER_CONFIG)
                .build().execute(new Callback<UserInfoResponse>() {
            @Override
            public UserInfoResponse parseNetworkResponse(Response response, int id) throws Exception {
                String data = response.body().string().trim();
                DebugLog.d("response:" + data);
                return JSON.parseObject(data, UserInfoResponse.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(UserInfoResponse response, int id) {
                if (response != null) {
                    DebugLog.d("response:" + response + ", data:" + response.data);
                    int noRatingCount = response.data.noRatingCount;

                    if (noRatingCount > 0) {
                        view.setVisibility(View.VISIBLE);
                        view.setText(noRatingCount + getString(R.string.my_game_history_rate));
                    }
                }
            }
        });
    }
}
