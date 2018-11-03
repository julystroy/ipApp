package com.cartoon.module.zong;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.data.AppPoint;
import com.cartoon.data.EventPlay;
import com.cartoon.data.UserInfo;
import com.cartoon.data.Users;
import com.cartoon.data.chat.SectChat;
import com.cartoon.data.message.Message;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.utils.DateFormatter;
import com.cartoon.utils.JPushUtils;
import com.cartoon.utils.NetworkUtils;
import com.cartoon.utils.ThreadPoolManager;
import com.cartoon.utils.UserDB;
import com.cartoon.utils.WebsocketUtil;
import com.cartoon.view.dialog.SectJobDialog;
import com.cartoon.view.messagelist.ImageLoader;
import com.cartoon.view.messagelist.MessageInput;
import com.cartoon.view.messagelist.MessagesList;
import com.cartoon.view.messagelist.MessagesListAdapter;
import com.cartton.library.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by cc on 17-12-6.
 */
public class SectChatActivity extends BaseActivity implements MessagesListAdapter.OnLoadMoreListener, MessageInput.InputListener, DateFormatter.Formatter {
    @BindView(R.id.bt_left)
    ImageButton    mBtLeft;
    @BindView(R.id.tv_title)
    TextView       mTvTitle;
    @BindView(R.id.tv_sendcontrol)
    TextView       mTvControl;
    @BindView(R.id.bt_right)
    ImageButton    mBtRight;
    @BindView(R.id.messagesList)
    MessagesList   mMessagesList;
    @BindView(R.id.input)
    MessageInput   mInput;
    protected final String senderId = CartoonApp.getInstance().getUserId();
    protected ImageLoader                  imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;

    private int count =1;
    private Long id =Long.valueOf(0);
    @Override
    protected int getContentViewId() {
        return R.layout.activity_sectchat;
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

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Glide.with(SectChatActivity.this).load(url).into(imageView);
            }
        };

       initView();

        messagesAdapter  = new MessagesListAdapter<>(SectChatActivity.this,senderId, imageLoader);
        messagesAdapter.setLoadMoreListener(this);
        messagesAdapter.setDateHeadersFormatter(this);
        messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
                new MessagesListAdapter.OnMessageViewClickListener<Message>() {
                    @Override
                    public void onMessageViewClick(View view, final Message message) {
                        String uid = message.getUid();
                        SectChat sectChat = UserDB.getInstance().querySect(CartoonApp.getInstance().getUserId());
                        SectChat sectChat2 = UserDB.getInstance().querySect(uid);
                        if (sectChat != null) {
                            if (sectChat.getRank_name().equals("掌门") || sectChat.getRank_name().equals("长老")) {//权限
                                if (sectChat2!=null&&!sectChat2.getRank_name().equals("掌门")&&!uid.equals(CartoonApp.getInstance().getUserId())) {
                                    BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SECTPOINTLIST)
                                            .addParams("myId", CartoonApp.getInstance().getUserId())
                                            .addParams("token",CartoonApp.getInstance().getToken())
                                            .addParams("memberId",message.getUid()+"")
                                            .addParams("sectionId",sectChat2.getSect_id())
                                            .build().execute(new BaseCallBack<List<AppPoint>>() {
                                        @Override
                                        public void onLoadFail() {
                                        }
                                        @Override
                                        public void onContentNull() {
                                        }
                                        @Override
                                        public void onLoadSuccess(List<AppPoint> response) {
                                            SectJobDialog dialog = new SectJobDialog(SectChatActivity.this,message.getUid(),response);
                                            dialog.setCanceledOnTouchOutside(true);
                                            dialog.setOnSureClickListener(new SectJobDialog.onSureClickListener() {
                                                @Override
                                                public void onSureClickListener(int rankid, String name) {
                                                    UserDB.getInstance().updataRankName(message.getUid(),name);
                                                    //                                        Toast.makeText(SectChatActivity.this,
                                                    //                                                "操作成功",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            dialog.show();
                                        }

                                        @Override
                                        public List<AppPoint> parseNetworkResponse(String response) throws Exception {
                                            return JSON.parseArray(response,AppPoint.class);
                                        }
                                    });

                                }
                            }
                        }


                    }
                });
       // this.mMessagesList.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));;
        this.mMessagesList.setAdapter(messagesAdapter);
        mInput.setInputListener(this);


        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                final SectChat sectChat = UserDB.getInstance().querySect(CartoonApp.getInstance().getUserId());

                //第一次进来时从数据库拿未读数据

                List<Message> messages1 = UserDB.getInstance().queryLast();
                List<Message> messages = null;
                if (messages != null) {
                    messages.clear();
                }
                if (messages1 != null && messages1.size() != 0) {
                    Message message = messages1.get(0);
                    if (message != null)
                        id = message.getId();


                    if (id > 100)
                        messages = UserDB.getInstance().queryDb((int) (id - 100));
                    else
                        messages = UserDB.getInstance().queryDb(1);

                        for (int i = 0; i < messages.size(); i++) {
                            messagesAdapter.addToStart(messages.get(i), true);
                        }



                }

                if (sectChat != null) {
                    mTvControl.setVisibility(sectChat.getUser_status().equals("0") ? View.GONE : View.VISIBLE);
                    mInput.setVisibility(sectChat.getUser_status().equals("0") ? View.VISIBLE : View.GONE);
                } else {
                    mTvControl.setVisibility(View.VISIBLE);
                    mTvControl.setText("已不在宗门内");
                    mInput.setVisibility(View.GONE);

                }
            }
        });





        /*else{
            if (sectChat!=null)
            WebSocketUtils.getInstance().sendMessage("###"+sectChat.getNickname()+"加入聊天", CartoonApp.getInstance().getUserId());
        }*/
        ChatClients.getCount(true);//通知不计数



    }

    private void initView() {
        mTvTitle.setText("宗门聊天");
        mBtLeft.setImageResource(R.mipmap.icon_back_black);
        mBtRight.setVisibility(View.INVISIBLE);
        mBtLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        if (totalItemsCount>100) {
            loadMessages();
            count++;
        }
    }

    protected void loadMessages() {
        new Handler().postDelayed(new Runnable() { //imitation of internet connection
            @Override
            public void run() {
                //  ArrayList<Message> messages = MessagesFixtures.getMessages(lastLoadedDate);
                //  lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
                List<Message> messages;
                if (id>count*100)
                    messages = UserDB.getInstance().queryDb((int) (id - count * 100));
                else
                    messages =  UserDB.getInstance().queryDb(0);
                for (int i = 0; i < messages.size(); i++) {

                    messagesAdapter.addToEnd(messages, false);
                }
            }
        }, 1000);

    }

    @Override
    public boolean onSubmit(CharSequence input) {
        if (NetworkUtils.isNetworkOnline(SectChatActivity.this)) {
            if (!input.toString().trim().isEmpty()&&input.length()<2000) {

                    Message message = new Message();
                    message.setUid(CartoonApp.getInstance().getUserId());
                    message.setText(input.toString());
                    message.setCreatedAt(new Date(System.currentTimeMillis()));
                   // messagesAdapter.addToStart(message, true);
                WebsocketUtil.getInstance().sendMessage(input.toString(), CartoonApp.getInstance().getUserId());
                    return true;
            } else {
                    ToastUtils.showShort(SectChatActivity.this, "内容为空或者内容过长");
                    return false;
                }

        } else {
            ToastUtils.showShort(SectChatActivity.this,"请检查网络链接");
            return false;
        }
    }

    @Subscribe
    public void onEvent(Users u){
        if (u.message!=null)
            messagesAdapter.addToStart(u.message, true);
    }

    @Subscribe
    public void onEvent(EventPlay u){
        boolean who = u.who;
        mTvControl.setVisibility(View.VISIBLE);
        mInput.setVisibility(View.GONE);
        if (who) {
            mTvControl.setText("禁言中 请联系贵门掌门或者长老");
        }else
            mTvControl.setText("已被逐出师门");

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatClients.getCount(false);//通知不计数
        if (messagesAdapter !=null)
        messagesAdapter.delete();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return getString(R.string.date_header_today);
        } else if (DateFormatter.isYesterday(date)) {
            return getString(R.string.date_header_yesterday);
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        UserInfo userInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userInfo != null&&userInfo.getSectionId()!=null) {
            setAlias("chat"+userInfo.getSectionId());
        } else {
            setAlias("android");
        }

    }

    /**
     * 设置别名
     ***/
    private void setAlias(String alias) {

        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(getBaseContext(), R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!JPushUtils.isValidTagAndAlias(alias)) {
            Toast.makeText(getBaseContext(), R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));

    }


    private static final int MSG_SET_ALIAS = 1001;



    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                  //  Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                default:
                //    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                   // Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                  //  Log.i(TAG, logs);
                    if (JPushUtils.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000);
                    } else {
                    //    Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                  //  Log.e(TAG, logs);
            }

        }

    };
}
