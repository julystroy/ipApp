package com.cartoon.module.question;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cartoon.data.CardItem;
import com.cartoon.module.listener.CardAdapter;
import com.cartoon.utils.EasySharePreference;
import com.cartoon.utils.StringUtils;
import com.cartoon.utils.countdown.CircularProgressBar;
import com.cartoon.utils.countdown.CountdownView;
import com.cartoon.view.dialog.DayAskDialog;
import com.cartton.library.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.xuanjiezhimen.R;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private  Context       context;
    private List<CardView> mViews;
    private List<CardItem> mData;
    private float          mBaseElevation;

    private  long lastQuestionTime;
    private  String nowSystemTime;
    private nextQuestionListener onClickNext;
    private DayAskDialog dialog;
    private boolean last =false;
    public CardPagerAdapter(Context context) {
        this.context=context;
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        nowSystemTime = StringUtils.getNowSystemTime(System.currentTimeMillis());
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
        notifyDataSetChanged();
    }



    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData==null?0:mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final   CardItem item =mData.get(position);
        lastQuestionTime = EasySharePreference.getLastQuestionTime(context);
        final boolean sameDay = isSameDay(lastQuestionTime);//答题时判断是否是同一天
        int positionNum = EasySharePreference.getPositionNum(context);
        boolean haveAnswered = EasySharePreference.getHaveAnswered(context);
        long time = System.currentTimeMillis() - lastQuestionTime;

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.adapter, container, false);
        final ViewHolder   holder = new ViewHolder(view);
       //已答题目禁选
        if (lastQuestionTime!=0&&item.getPosition() == positionNum && (haveAnswered||time>60*1000)) {
            holder.rl1.setEnabled(false);
            holder.rl2.setEnabled(false);
            holder.rl3.setEnabled(false);
            holder.rl4.setEnabled(false);
            holder.checkBox1.setTextColor(Color.parseColor("#FFC9C5C5"));
            holder.checkBox2.setTextColor(Color.parseColor("#FFC9C5C5"));
            holder.checkBox3.setTextColor(Color.parseColor("#FFC9C5C5"));
            holder.checkBox4.setTextColor(Color.parseColor("#FFC9C5C5"));
            if (holder.timer != null) {
                holder.timer.stop();
                holder.crProgressbar.stopCir();
            }
        } else {
            //倒计时
            if (position == 0) {
                if (time < 60 * 1000) {
                    long l = 60 * 1000 - time;
                    holder.timer.start(l);
                    holder.crProgressbar.startCir(l);
                } else {
                    //第一次进来为60s
                    holder.timer.start(60*1000);
                    holder.crProgressbar.startCir(60*1000);
                }
            }
        }

        holder.checkBox1.setTag(position);
        holder.checkBox2.setTag(position);
        holder.checkBox3.setTag(position);
        holder.checkBox4.setTag(position);
        holder.checkBox1.setText("  A  "+item.getOption1());
        holder.checkBox2.setText("  B  "+item.getOption2());
        holder.checkBox3.setText("  C  "+item.getOption3());
        holder.checkBox4.setText("  D  "+item.getOption4());
        holder.contentTextView.setText("("+item.getPosition()+"/10)"+item.getQuestion());

        if (item.getPosition()==10) {
            holder.nextQuestion.setText("查看今日战况");
        }else{
            holder.nextQuestion.setText("下一题");
        }
        holder.nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (item.getPosition()+1 > 5) {
                    dialog = new DayAskDialog(context);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setOnClickListener(new DayAskDialog.OnDialogClickListener() {
                        @Override
                        public void onDilogClick(TextView view) {
                            if (onClickNext != null)
                                onClickNext.clickNext(item.getId(),item.getPosition(), dialog);
                        }
                    });
                    dialog.show();
                } else {
                    if (onClickNext !=null)
                        onClickNext.clickNext(item.getId(),item.getPosition());
                }*/


                if (onClickNext !=null)
                    onClickNext.clickNext(item.getId(),item.getPosition());
            }
        });
        holder.rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sameDay) {
                    holder.imag1.setVisibility(View.VISIBLE);
                    holder.rl2.setEnabled(false);
                    holder.rl3.setEnabled(false);
                    holder.rl4.setEnabled(false);
                    holder.rl1.setEnabled(false);
                    holder.timer.stop();//关闭倒计时
                    holder.crProgressbar.stopCir();
                    if (onClickNext!=null)
                        onClickNext.clickItem(item.getId(),"1",item.getPosition(),holder.imag1,holder.rl1);

                }else
                    ToastUtils.showShort(context,"题目已过期，请退出重新进入答题");
            }
        });
        holder.rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sameDay) {
                    holder.imag2.setVisibility(View.VISIBLE);
                    holder.rl2.setEnabled(false);
                    holder.rl3.setEnabled(false);
                    holder.rl4.setEnabled(false);
                    holder.rl1.setEnabled(false);
                    holder.timer.stop();//关闭倒计时
                    holder.crProgressbar.stopCir();
                    if (onClickNext!=null)
                        onClickNext.clickItem(item.getId(),"2",item.getPosition(),holder.imag2,holder.rl2);

                }else
                    ToastUtils.showShort(context,"题目已过期，请退出重新进入答题");
            }
        });
        holder.rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sameDay) {
                    holder.imag3.setVisibility(View.VISIBLE);
                    holder.rl2.setEnabled(false);
                    holder.rl3.setEnabled(false);
                    holder.rl4.setEnabled(false);
                    holder.rl1.setEnabled(false);
                    holder.timer.stop();//关闭倒计时
                    holder.crProgressbar.stopCir();
                    if (onClickNext!=null)
                        onClickNext.clickItem(item.getId(),"3",item.getPosition(),holder.imag3,holder.rl3);

                }else
                    ToastUtils.showShort(context,"题目已过期，请退出重新进入答题");
            }
        });
        holder.rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sameDay) {
                    holder.imag4.setVisibility(View.VISIBLE);
                    holder.rl2.setEnabled(false);
                    holder.rl3.setEnabled(false);
                    holder.rl4.setEnabled(false);
                    holder.rl1.setEnabled(false);
                    holder.timer.stop();//关闭倒计时
                    holder.crProgressbar.stopCir();
                    if (onClickNext!=null)
                        onClickNext.clickItem(item.getId(),"4",item.getPosition(),holder.imag4,holder.rl4);
                }else
                    ToastUtils.showShort(context,"题目已过期，请退出重新进入答题");
            }
        });

        //保存当前题目的id
        EasySharePreference.setLastQestionId(context,item.getId());


        //倒计时结束时与后台的交互
        holder.timer.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                holder.crProgressbar.stopCir();
                holder.crProgressbar.setProgress(0);
                holder.rl2.setEnabled(false);
                holder.rl3.setEnabled(false);
                holder.rl4.setEnabled(false);
                holder.rl1.setEnabled(false);
                holder.checkBox1.setTextColor(Color.parseColor("#FFC9C5C5"));
                holder.checkBox2.setTextColor(Color.parseColor("#FFC9C5C5"));
                holder.checkBox3.setTextColor(Color.parseColor("#FFC9C5C5"));
                holder.checkBox4.setTextColor(Color.parseColor("#FFC9C5C5"));
               // SendToBG.getAnswer(item.getId(),item.getPosition());
                EasySharePreference.setHaveAnswered(context,true);
                if (onClickNext!=null)
                    onClickNext.clickItem(item.getId(),"0",item.getPosition(),holder.imag4,holder.rl4);

            }
        });


        container.addView(view);

        //bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }



        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        cardView.setCardBackgroundColor(Color.TRANSPARENT);
        mViews.set(position, cardView);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        /*if (mData.size() > 0) {
            mData.clear();
            mData=null;
        }*/
        container.removeView((View) object);
        mViews.set(position, null);
    }

public interface nextQuestionListener{
    void clickNext(int id ,int position);
  //  void clickNext(int id ,int position, DayAskDialog dialog);
    void clickItem(int id, String s, int position, ImageView imag1,RelativeLayout rlayout);
}
public void setOnclickNext(nextQuestionListener onclickNext){
    this.onClickNext = onclickNext;
}

    class ViewHolder{
        private TextView       checkBox1;
        private TextView       checkBox2;
        private TextView       checkBox3;
        private TextView       checkBox4;
        private TextView       nextQuestion;
        private TextView       contentTextView;
        private CountdownView  timer;
        private ImageView      imag1;
        private ImageView      imag2;
        private ImageView      imag3;
        private ImageView      imag4;
        private RelativeLayout rl1;
        private RelativeLayout rl2;
        private RelativeLayout rl3;
        private RelativeLayout rl4;
        private CardView    flCard;
        private CircularProgressBar    crProgressbar;

        public ViewHolder(View view){
            contentTextView = (TextView) view.findViewById(R.id.contentTextView);
            checkBox1 = (TextView) view.findViewById(R.id.checkBox1);
            checkBox2 = (TextView) view.findViewById(R.id.checkBox2);
            checkBox3 = (TextView) view.findViewById(R.id.checkBox3);
            checkBox4 = (TextView) view.findViewById(R.id.checkBox4);
            nextQuestion = (TextView) view.findViewById(R.id.next_question);
            timer = (CountdownView) view.findViewById(R.id.slowtimer);
            imag1 = (ImageView) view.findViewById(R.id.iv1);
            rl1 = (RelativeLayout) view.findViewById(R.id.rl1);
            rl2 = (RelativeLayout) view.findViewById(R.id.rl2);
            rl3 = (RelativeLayout) view.findViewById(R.id.rl3);
            rl4 = (RelativeLayout) view.findViewById(R.id.rl4);
            imag2 = (ImageView) view.findViewById(R.id.iv2);
            imag3 = (ImageView) view.findViewById(R.id.iv3);
            imag4 = (ImageView) view.findViewById(R.id.iv4);
            flCard = (CardView) view.findViewById(R.id.cardView);
            crProgressbar = (CircularProgressBar) view.findViewById(R.id.circularProgressbar);
        }
    }

    //判断是否是同一天
    private boolean isSameDay(long tiome) {
        String nowSystemTime1 = StringUtils.getNowSystemTime(tiome);
        return TextUtils.equals(nowSystemTime1, "1970/1") || TextUtils.equals(nowSystemTime, nowSystemTime1);
    }
}