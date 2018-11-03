package com.cartoon.module.question;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;

import com.cartoon.module.listener.CardAdapter;
import com.cartoon.utils.countdown.CircularProgressBar;
import com.cartoon.utils.countdown.CountdownView;
import com.cartoon.view.MyViewPage;

import cn.com.xuanjiezhimen.R;


public class ShadowTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {

    private MyViewPage  mViewPager;
    private CardAdapter mAdapter;
    private float       mLastOffset;
    private boolean     mScalingEnabled =true;

    public ShadowTransformer(MyViewPage viewPager, CardAdapter adapter) {
        mViewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        mAdapter = adapter;

    }

    @Override
    public void transformPage(View page, float position) {

    }

    @Override
    public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels) {
        int realCurrentPosition;
        int nextPosition;
        float baseElevation = mAdapter.getBaseElevation();
        float realOffset;
        boolean goingLeft = mLastOffset > positionOffset;

        // If we're going backwards, onPageScrolled receives the last position
        // instead of the current one
        if (goingLeft) {
            realCurrentPosition = position + 1;
            nextPosition = position;
            realOffset = 1 - positionOffset;
        } else {
            nextPosition = position + 1;
            realCurrentPosition = position;
            realOffset = positionOffset;
        }

        // Avoid crash on overscroll
        if (nextPosition > mAdapter.getCount() - 1
                || realCurrentPosition > mAdapter.getCount() - 1) {
            return;
        }



        CardView currentCard = mAdapter.getCardViewAt(realCurrentPosition);

        // This might be null if a fragment is being used
        // and the views weren't created yet
        if (currentCard != null) {
            if (mScalingEnabled) {
                currentCard.setScaleX((float) (1 + 0.1 * (1 - realOffset)));
                currentCard.setScaleY((float) (1 + 0.1 * (1 - realOffset)));
            }
           /* currentCard.setCardElevation((baseElevation + baseElevation
                    * (CardAdapter.MAX_ELEVATION_FACTOR - 1) * (1 - realOffset)));*/
            //currentCard.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }

        CardView nextCard = mAdapter.getCardViewAt(nextPosition);

        // We might be scrolling fast enough so that the next (or previous) card
        // was already destroyed or a fragment might not have been created yet
        if (nextCard != null) {
            if (mScalingEnabled) {
                nextCard.setScaleX((float) (1 + 0.1 * (realOffset)));
                nextCard.setScaleY((float) (1 + 0.1 * (realOffset)));
            }
           /* nextCard.setCardElevation((baseElevation + baseElevation
                    * (CardAdapter.MAX_ELEVATION_FACTOR - 1) * (realOffset)));*///cardview 阴影
           // currentCard.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));//cardview  背景颜色
        }

        mLastOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {
        //mViewPager.setPagingEnabled(true);
        CardView cardViewAt = (CardView) mAdapter.getCardViewAt(position);
        cardViewAt.setCardBackgroundColor(Color.TRANSPARENT);
        //cardViewAt.setCardBackgroundColor(R.color.color_white);
        CountdownView countdownview = (CountdownView) cardViewAt.findViewById(R.id.slowtimer);
        CircularProgressBar circularProgressBar = (CircularProgressBar) cardViewAt.findViewById(R.id.circularProgressbar);
        countdownview.start((long) 60 * 1000);
        circularProgressBar.startCir((long) 60 * 1000);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
