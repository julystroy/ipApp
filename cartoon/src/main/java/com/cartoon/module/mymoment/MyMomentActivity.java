package com.cartoon.module.mymoment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cartoon.data.Keys;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.tab.bookfriendmoment.BookFragment;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;

/**
 * Created by jinbangzhu on 7/23/16.
 */

public class MyMomentActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.bt_left)
    ImageButton mBtLeft;
    @BindView(R.id.tv_title)
    TextView    mTvTitle;
    @BindView(R.id.bt_right)
    ImageButton mBtRight;

    @Override
    protected int getContentViewId() {
        return R.layout.my_moment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBtLeft.setImageResource(R.mipmap.icon_back_black);
        mBtRight.setVisibility(View.GONE);
        mTvTitle.setText("动态");
        mBtLeft.setOnClickListener(this);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Keys.MOMENT_MYSELF_ONLY, true);
        BookFragment bookFragment = new BookFragment();
        bookFragment.setArguments(bundle);
        addFragment(bookFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
