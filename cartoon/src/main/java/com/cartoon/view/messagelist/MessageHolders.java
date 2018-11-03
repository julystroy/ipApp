package com.cartoon.view.messagelist;


import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cartoon.data.chat.SectChat;
import com.cartoon.data.message.Message;
import com.cartoon.utils.DateFormatter;
import com.cartoon.utils.UserDB;
import com.cartoon.view.messagelist.model.MessageContentType;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.xuanjiezhimen.R;

/*
 * Created by troy379 on 31.03.17.
 */
@SuppressWarnings("WeakerAccess")
public class MessageHolders {

    private static final short VIEW_TYPE_DATE_HEADER = 130;
    private static final short VIEW_TYPE_TEXT_MESSAGE = 131;
   // private static final short VIEW_TYPE_IMAGE_MESSAGE = 132;

    private Class<? extends ViewHolder<Date>> dateHeaderHolder;
    private int                               dateHeaderLayout;

    private HolderConfig<Message> incomingTextConfig;
    private HolderConfig<Message> outcomingTextConfig;
//    private HolderConfig<MessageContentType.Image> incomingImageConfig;
//    private HolderConfig<MessageContentType.Image> outcomingImageConfig;

    private List<ContentTypeConfig> customContentTypes = new ArrayList<>();
    private ContentChecker contentChecker;

    public MessageHolders() {
        this.dateHeaderHolder = DefaultDateHeaderViewHolder.class;
        this.dateHeaderLayout = R.layout.item_date_header;

        this.incomingTextConfig = new HolderConfig<>(DefaultIncomingTextMessageViewHolder.class, R.layout.item_incoming_text_message);
        this.outcomingTextConfig = new HolderConfig<>(DefaultOutcomingTextMessageViewHolder.class, R.layout.item_outcoming_text_message);
//        this.incomingImageConfig = new HolderConfig<>(DefaultIncomingImageMessageViewHolder.class, R.layout.item_incoming_image_message);
//        this.outcomingImageConfig = new HolderConfig<>(DefaultOutcomingImageMessageViewHolder.class, R.layout.item_outcoming_image_message);
    }

    /**
     * Sets both of custom view holder class and layout resource for incoming text message.
     *
     * @param holder holder class.
     * @param layout layout resource.
     * @return {@link MessageHolders} for subsequent configuration.
     */
    public MessageHolders setIncomingTextConfig(
            @NonNull Class<? extends BaseMessageViewHolder<? extends Message>> holder,
            @LayoutRes int layout) {
        this.incomingTextConfig.holder = holder;
        this.incomingTextConfig.layout = layout;
        return this;
    }

    /**
     * Sets custom view holder class for incoming text message.
     *
     * @param holder holder class.
     * @return {@link MessageHolders} for subsequent configuration.
     */
    public MessageHolders setIncomingTextHolder(
            @NonNull Class<? extends BaseMessageViewHolder<? extends Message>> holder) {
        this.incomingTextConfig.holder = holder;
        return this;
    }

    /**
     * Sets custom layout resource for incoming text message.
     *
     * @param layout layout resource.
     * @return {@link MessageHolders} for subsequent configuration.
     */
    public MessageHolders setIncomingTextLayout(@LayoutRes int layout) {
        this.incomingTextConfig.layout = layout;
        return this;
    }

    /**
     * Sets both of custom view holder class and layout resource for outcoming text message.
     *
     * @param holder holder class.
     * @param layout layout resource.
     * @return {@link MessageHolders} for subsequent configuration.
     */
    public MessageHolders setOutcomingTextConfig(
            @NonNull Class<? extends BaseMessageViewHolder<? extends Message>> holder,
            @LayoutRes int layout) {
        this.outcomingTextConfig.holder = holder;
        this.outcomingTextConfig.layout = layout;
        return this;
    }

    /**
     * Sets custom view holder class for outcoming text message.
     *
     * @param holder holder class.
     * @return {@link MessageHolders} for subsequent configuration.
     */
    public MessageHolders setOutcomingTextHolder(
            @NonNull Class<? extends BaseMessageViewHolder<? extends Message>> holder) {
        this.outcomingTextConfig.holder = holder;
        return this;
    }

    /**
     * Sets custom layout resource for outcoming text message.
     *
     * @param layout layout resource.
     * @return {@link MessageHolders} for subsequent configuration.
     */
    public MessageHolders setOutcomingTextLayout(@LayoutRes int layout) {
        this.outcomingTextConfig.layout = layout;
        return this;
    }



    /**
     * Sets both of custom view holder class and layout resource for date header.
     *
     * @param holder holder class.
     * @param layout layout resource.
     * @return {@link MessageHolders} for subsequent configuration.
     */
    public MessageHolders setDateHeaderConfig(
            @NonNull Class<? extends ViewHolder<Date>> holder,
            @LayoutRes int layout) {
        this.dateHeaderHolder = holder;
        this.dateHeaderLayout = layout;
        return this;
    }

    /**
     * Sets custom view holder class for date header.
     *
     * @param holder holder class.
     * @return {@link MessageHolders} for subsequent configuration.
     */
    public MessageHolders setDateHeaderHolder(@NonNull Class<? extends ViewHolder<Date>> holder) {
        this.dateHeaderHolder = holder;
        return this;
    }

    /**
     * Sets custom layout reource for date header.
     *
     * @param layout layout resource.
     * @return {@link MessageHolders} for subsequent configuration.
     */
    public MessageHolders setDateHeaderLayout(@LayoutRes int layout) {
        this.dateHeaderLayout = layout;
        return this;
    }

    /**
     * Registers custom content type (e.g. multimedia, events etc.)
     *
     * @param type            unique id for content type
     * @param holder          holder class for incoming and outcoming messages
     * @param incomingLayout  layout resource for incoming message
     * @param outcomingLayout layout resource for outcoming message
     * @param contentChecker  {@link ContentChecker} for registered type
     * @return {@link MessageHolders} for subsequent configuration.
     */
    public <TYPE extends MessageContentType>
    MessageHolders registerContentType(
            byte type, @NonNull Class<? extends BaseMessageViewHolder<TYPE>> holder,
            @LayoutRes int incomingLayout,
            @LayoutRes int outcomingLayout,
            @NonNull ContentChecker contentChecker) {

        return registerContentType(type,
                holder, incomingLayout,
                holder, outcomingLayout,
                contentChecker);
    }

    /**
     * Registers custom content type (e.g. multimedia, events etc.)
     *
     * @param type            unique id for content type
     * @param incomingHolder  holder class for incoming message
     * @param outcomingHolder holder class for outcoming message
     * @param incomingLayout  layout resource for incoming message
     * @param outcomingLayout layout resource for outcoming message
     * @param contentChecker  {@link ContentChecker} for registered type
     * @return {@link MessageHolders} for subsequent configuration.
     */
    public <TYPE extends MessageContentType>
    MessageHolders registerContentType(
            byte type,
            @NonNull Class<? extends BaseMessageViewHolder<TYPE>> incomingHolder, @LayoutRes int incomingLayout,
            @NonNull Class<? extends BaseMessageViewHolder<TYPE>> outcomingHolder, @LayoutRes int outcomingLayout,
            @NonNull ContentChecker contentChecker) {

        if (type == 0)
            throw new IllegalArgumentException("content type must be greater or less than '0'!");

        customContentTypes.add(
                new ContentTypeConfig<>(type,
                        new HolderConfig<>(incomingHolder, incomingLayout),
                        new HolderConfig<>(outcomingHolder, outcomingLayout)));
        this.contentChecker = contentChecker;
        return this;
    }

    /*
    * INTERFACES
    * */

    /**
     * The interface, which contains logic for checking the availability of content.
     */
    public interface ContentChecker<MESSAGE extends Message> {

        /**
         * Checks the availability of content.
         *
         * @param message current message in list.
         * @param type    content type, for which content availability is determined.
         * @return weather the message has content for the current message.
         */
        boolean hasContentFor(MESSAGE message, byte type);
    }

    /*
    * PRIVATE METHODS
    * */

    protected ViewHolder getHolder(ViewGroup parent, int viewType, MessagesListStyle messagesListStyle) {
        switch (viewType) {
            case VIEW_TYPE_DATE_HEADER:
                return getHolder(parent, dateHeaderLayout, dateHeaderHolder, messagesListStyle);
            case VIEW_TYPE_TEXT_MESSAGE:
                return getHolder(parent, incomingTextConfig, messagesListStyle);
            case -VIEW_TYPE_TEXT_MESSAGE:
                return getHolder(parent, outcomingTextConfig, messagesListStyle);
//            case VIEW_TYPE_IMAGE_MESSAGE:
//                return getHolder(parent, incomingImageConfig, messagesListStyle);
//            case -VIEW_TYPE_IMAGE_MESSAGE:
//                return getHolder(parent, outcomingImageConfig, messagesListStyle);
            default:
                for (ContentTypeConfig typeConfig : customContentTypes) {
                    if (Math.abs(typeConfig.type) == Math.abs(viewType)) {
                        if (viewType > 0)
                            return getHolder(parent, typeConfig.incomingConfig, messagesListStyle);
                        else
                            return getHolder(parent, typeConfig.outcomingConfig, messagesListStyle);
                    }
                }
        }
        throw new IllegalStateException("Wrong message view type. Please, report this issue on GitHub with full stacktrace in description.");
    }

    @SuppressWarnings("unchecked")
    protected void bind(final ViewHolder holder, final Object item, boolean isSelected,
                        final ImageLoader imageLoader,
                        final View.OnClickListener onMessageClickListener,
                        final View.OnLongClickListener onMessageLongClickListener,
                        final DateFormatter.Formatter dateHeadersFormatter,
                        final SparseArray<MessagesListAdapter.OnMessageViewClickListener> clickListenersArray) {

        if (item instanceof Message) {
   //         ((MessageHolders.BaseMessageViewHolder) holder).isSelected = isSelected;
            ((MessageHolders.BaseMessageViewHolder) holder).imageLoader = imageLoader;
            holder.itemView.setOnLongClickListener(onMessageLongClickListener);
            holder.itemView.setOnClickListener(onMessageClickListener);

            for (int i = 0; i < clickListenersArray.size(); i++) {
                final int key = clickListenersArray.keyAt(i);
                final View view = holder.itemView.findViewById(key);
                if (view != null) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickListenersArray.get(key).onMessageViewClick(view, (Message) item);
                        }
                    });
                }
            }
        } else if (item instanceof Date) {
            ((MessageHolders.DefaultDateHeaderViewHolder) holder).dateHeadersFormatter = dateHeadersFormatter;
        }

        holder.onBind(item);
    }


    protected int getViewType(Object item, String senderId) {
        boolean isOutcoming = false;
        int viewType;

        if (item instanceof Message) {
            Message message = (Message) item;
            isOutcoming = message.getUid().trim().contentEquals(senderId);
            viewType = getContentViewType(message);

        } else viewType = VIEW_TYPE_DATE_HEADER;

        return isOutcoming ? viewType * -1 : viewType;
    }

    private ViewHolder getHolder(ViewGroup parent, HolderConfig holderConfig, MessagesListStyle style) {
        return getHolder(parent, holderConfig.layout, holderConfig.holder, style);
    }

    private <HOLDER extends ViewHolder>
    ViewHolder getHolder(ViewGroup parent, @LayoutRes int layout, Class<HOLDER> holderClass, MessagesListStyle style) {

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        try {
            Constructor<HOLDER> constructor = holderClass.getDeclaredConstructor(View.class);
            constructor.setAccessible(true);
            HOLDER holder = constructor.newInstance(v);
            if (holder instanceof DefaultMessageViewHolder && style != null) {
                ((DefaultMessageViewHolder) holder).applyStyle(style);
            }
            return holder;
        } catch (Exception e) {
            return null;
           // throw new UnsupportedOperationException("Somehow we couldn't create the ViewHolder for message. Please, report this issue on GitHub with full stacktrace in description.", e);
        }
    }

    @SuppressWarnings("unchecked")
    private short getContentViewType(Message message) {
//        if (message instanceof MessageContentType.Image
//                && ((MessageContentType.Image) message).getImageUrl() != null) {
//            return VIEW_TYPE_IMAGE_MESSAGE;
//        }

        // other default types will be here


            for (int i = 0; i < customContentTypes.size(); i++) {
                ContentTypeConfig config = customContentTypes.get(i);
                if (contentChecker == null) {
                    throw new IllegalArgumentException("ContentChecker cannot be null when using custom content types!");
                }
                boolean hasContent = contentChecker.hasContentFor(message, config.type);
                if (hasContent) return config.type;
            }


        return VIEW_TYPE_TEXT_MESSAGE;
    }

    /*
    * HOLDERS
    * */

    /**
     * The base class for view holders for incoming and outcoming message.
     * You can extend it to create your own holder in conjuction with custom layout or even using default layout.
     */
    public static abstract class BaseMessageViewHolder<MESSAGE extends Message> extends ViewHolder<MESSAGE> {

     //   boolean isSelected;

        /**
         * Callback for implementing images loading in message list
         */
        protected ImageLoader imageLoader;

        public BaseMessageViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * Returns whether is item selected
         *
         * @return weather is item selected.
         */
//       public boolean isSelected() {
//            return isSelected;
//        }

        /**
         * Returns weather is selection mode enabled
         *
         * @return weather is selection mode enabled.
         */
        public boolean isSelectionModeEnabled() {
            return MessagesListAdapter.isSelectionModeEnabled;
        }

        /**
         * Getter for {@link #imageLoader}
         *
         * @return image loader interface.
         */
        public ImageLoader getImageLoader() {
            return imageLoader;
        }

        protected void configureLinksBehavior(final TextView text) {
            text.setLinksClickable(false);
            text.setMovementMethod(new LinkMovementMethod() {
                @Override
                public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
                    boolean result = false;
                    if (!MessagesListAdapter.isSelectionModeEnabled) {
                        result = super.onTouchEvent(widget, buffer, event);
                    }
                    itemView.onTouchEvent(event);
                    return result;
                }
            });
        }

    }

    /**
     * Default view holder implementation for incoming text message
     */
    public static class IncomingTextMessageViewHolder<MESSAGE extends Message>
            extends BaseIncomingMessageViewHolder<MESSAGE> {

        protected ViewGroup bubble;
        protected TextView  text;
        protected TextView  control;
        protected TextView  name     ;
        protected TextView  userLevel;
        protected TextView  sectJob  ;

        public IncomingTextMessageViewHolder(View itemView) {
            super(itemView);
            bubble    = (ViewGroup) itemView.findViewById(R.id.bubble);
            text      = (TextView) itemView.findViewById(R.id.messageText);
            control   = (TextView) itemView.findViewById(R.id.message_control);
            name      = (TextView) itemView.findViewById(R.id.name);
            userLevel = (TextView) itemView.findViewById(R.id.user_level);
            sectJob   = (TextView) itemView.findViewById(R.id.sect_job);
        }

        @Override
        public void onBind(MESSAGE message) {
            super.onBind(message);
            if (message == null)return;
            if (message.getText().contains("###")) {
                bubble.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
                control.setVisibility(View.VISIBLE);
                name.setVisibility(View.GONE);
                userLevel.setVisibility(View.GONE);
                sectJob.setVisibility(View.GONE);
                control.setText(message.getText().replace("###",""));
            } else {
                control.setVisibility(View.GONE);
                bubble.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                userLevel.setVisibility(View.VISIBLE);
                sectJob.setVisibility(View.VISIBLE);
//                if (bubble != null) {
//                    bubble.setSelected(isSelected());
//                }

                if (text != null) {
                    text.setText(message.getText());
                }
                String uid = message.getUid();
                if (uid != null) {
                    SectChat sectChat = UserDB.getInstance().querySect(uid);
                    if (sectChat != null) {
                        if (name!=null){
                            name.setText(sectChat.getNickname()==null?"未知":sectChat.getNickname());
                        }
                        if (userLevel!=null){
                            userLevel.setText(sectChat.getLv_name()==null?"未知":sectChat.getLv_name());
                        }
                        if (sectJob!=null){
                            sectJob.setText(sectChat.getRank_name()==null?"未知":sectChat.getRank_name());
                        }
                    }
                }
            }
        }

        @Override
        public void applyStyle(MessagesListStyle style) {
            super.applyStyle(style);
            if (bubble != null) {
                bubble.setPadding(style.getIncomingDefaultBubblePaddingLeft(),
                        style.getIncomingDefaultBubblePaddingTop(),
                        style.getIncomingDefaultBubblePaddingRight(),
                        style.getIncomingDefaultBubblePaddingBottom());
                ViewCompat.setBackground(bubble, style.getIncomingBubbleDrawable());
            }

            if (text != null) {
                text.setTextColor(style.getIncomingTextColor());
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getIncomingTextSize());
                text.setTypeface(text.getTypeface(), style.getIncomingTextStyle());
                text.setAutoLinkMask(style.getTextAutoLinkMask());
                text.setLinkTextColor(style.getIncomingTextLinkColor());
                configureLinksBehavior(text);
            }
        }
    }

    /**
     * Default view holder implementation for outcoming text message
     */
    public static class OutcomingTextMessageViewHolder<MESSAGE extends Message>
            extends BaseOutcomingMessageViewHolder<MESSAGE> {

        protected ViewGroup bubble;
        protected TextView  text;
        protected TextView  name     ;
        protected TextView  userLevel;
        protected TextView  sectJob  ;
        protected TextView  control  ;

        public OutcomingTextMessageViewHolder(View itemView) {
            super(itemView);
            bubble = (ViewGroup) itemView.findViewById(R.id.bubble);
            text = (TextView) itemView.findViewById(R.id.messageText);
            name      = (TextView) itemView.findViewById(R.id.name);
            userLevel = (TextView) itemView.findViewById(R.id.user_level);
            sectJob   = (TextView) itemView.findViewById(R.id.sect_job);
            control   = (TextView) itemView.findViewById(R.id.message_control);
        }

        @Override
        public void onBind(MESSAGE message) {
            super.onBind(message);
            //            if (bubble != null) {
            //                bubble.setSelected(isSelected());
            //            }
            if (message == null)return;
            if (message.getText().contains("###")) {
                bubble.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
                control.setVisibility(View.VISIBLE);
                name.setVisibility(View.GONE);
                userLevel.setVisibility(View.GONE);
                sectJob.setVisibility(View.GONE);
                control.setText(message.getText());
            } else {
                control.setVisibility(View.GONE);
                bubble.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                userLevel.setVisibility(View.VISIBLE);
                sectJob.setVisibility(View.VISIBLE);
                if (text != null) {
                    text.setText(message.getText());
                }

                String uid = message.getUid();
                if (uid != null) {
                    SectChat sectChat = UserDB.getInstance().querySect(uid);
                    if (sectChat != null) {
                        if (name != null) {
                            name.setText(sectChat.getNickname() == null ? "未知" : sectChat.getNickname());
                        }
                        if (userLevel != null) {
                            userLevel.setText(sectChat.getLv_name() == null ? "未知" : sectChat.getLv_name());
                        }
                        if (sectJob != null) {
                            sectJob.setText(sectChat.getRank_name() == null ? "未知" : sectChat.getRank_name());
                        }
                    }
                }
            }
        }

        @Override
        public final void applyStyle(MessagesListStyle style) {
            super.applyStyle(style);
            if (bubble != null) {
                bubble.setPadding(style.getOutcomingDefaultBubblePaddingLeft(),
                        style.getOutcomingDefaultBubblePaddingTop(),
                        style.getOutcomingDefaultBubblePaddingRight(),
                        style.getOutcomingDefaultBubblePaddingBottom());
                ViewCompat.setBackground(bubble, style.getOutcomingBubbleDrawable());
            }

            if (text != null) {
                text.setTextColor(style.getOutcomingTextColor());
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getOutcomingTextSize());
                text.setTypeface(text.getTypeface(), style.getOutcomingTextStyle());
                text.setAutoLinkMask(style.getTextAutoLinkMask());
                text.setLinkTextColor(style.getOutcomingTextLinkColor());
                configureLinksBehavior(text);
            }
        }
    }

    /**
     * Default view holder implementation for date header
     */
    public static class DefaultDateHeaderViewHolder extends ViewHolder<Date>
            implements DefaultMessageViewHolder {

        protected TextView                text;
        protected String                  dateFormat;
        protected DateFormatter.Formatter dateHeadersFormatter;

        public DefaultDateHeaderViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.messageText);
        }

        @Override
        public void onBind(Date date) {
            if (text != null) {
                if (DateFormatter.isToday(date)) {//日期大头
                    text.setVisibility(View.GONE);
                } else {
                    text.setVisibility(View.VISIBLE);
                    String formattedDate = null;
                    if (dateHeadersFormatter != null) formattedDate = dateHeadersFormatter.format(date);
                    text.setText(formattedDate == null ? DateFormatter.format(date, dateFormat) : formattedDate);
                }
            }
        }

        @Override
        public void applyStyle(MessagesListStyle style) {
            if (text != null) {
                text.setTextColor(style.getDateHeaderTextColor());
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getDateHeaderTextSize());
                text.setTypeface(text.getTypeface(), style.getDateHeaderTextStyle());
                text.setPadding(style.getDateHeaderPadding(), style.getDateHeaderPadding(),
                        style.getDateHeaderPadding(), style.getDateHeaderPadding());
            }
            dateFormat = style.getDateHeaderFormat();
            dateFormat = dateFormat == null ? DateFormatter.Template.STRING_DAY_MONTH_YEAR.get() : dateFormat;
        }
    }

    /**
     * Base view holder for incoming message
     */
    private static long lastTime =0;
    public abstract static class BaseIncomingMessageViewHolder<MESSAGE extends Message>
            extends BaseMessageViewHolder<MESSAGE> implements DefaultMessageViewHolder {

        protected TextView  time;
        protected ImageView userAvatar;


        public BaseIncomingMessageViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.messageTime);
            userAvatar = (ImageView) itemView.findViewById(R.id.messageUserAvatar);
        }

        @Override
        public void onBind(MESSAGE message) {
            if (message == null)return;
            if (message.getText().contains("###")) {
                userAvatar.setVisibility(View.GONE);
            } else {
                userAvatar.setVisibility(View.VISIBLE);

                if (userAvatar != null) {
                    SectChat sectChat = UserDB.getInstance().querySect(message.getUid());
                    if (sectChat == null)return;
                    boolean isAvatarExists =imageLoader != null&&sectChat!=null&&sectChat.getAvatar()!=null
                       /* && message.getUser().getAvatar() != null
                        && !message.getUser().getAvatar().isEmpty()*/;// FIXME: 17-12-6 头像

                    userAvatar.setVisibility(View.VISIBLE);
                    if (isAvatarExists) {
                        imageLoader.loadImage(userAvatar, sectChat.getAvatar());
                    }else{
                        userAvatar.setImageResource(R.mipmap.ic_launcher);
                    }
                }
            }
            if (time != null) {
                long time = message.getCreatedAt().getTime();
                if (lastTime==0||(time-lastTime) > 300000) {//显示时间间隔
                    this.time.setVisibility(View.VISIBLE);
                    this.time.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
                } else {
                    this.time.setVisibility(View.GONE);
                }

                lastTime=time;
            }
        }

        @Override
        public void applyStyle(MessagesListStyle style) {
            if (time != null) {
                time.setTextColor(style.getIncomingTimeTextColor());
                time.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getIncomingTimeTextSize());
                time.setTypeface(time.getTypeface(), style.getIncomingTimeTextStyle());
            }

            if (userAvatar != null) {
                userAvatar.getLayoutParams().width = style.getIncomingAvatarWidth();
                userAvatar.getLayoutParams().height = style.getIncomingAvatarHeight();
            }

        }
    }

    /**
     * Base view holder for outcoming message
     */
    public abstract static class BaseOutcomingMessageViewHolder<MESSAGE extends Message>
            extends BaseMessageViewHolder<MESSAGE> implements DefaultMessageViewHolder {

        protected TextView  time;
        protected ImageView userAvatar;
        public BaseOutcomingMessageViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.messageTime);
            userAvatar = (ImageView) itemView.findViewById(R.id.messageUserAvatar);
        }

        @Override
        public void onBind(MESSAGE message) {
            if (message == null) return;
            if (message.getText().contains("###")) {
                userAvatar.setVisibility(View.GONE);
            } else {
                userAvatar.setVisibility(View.VISIBLE);
                if (userAvatar != null) {
                    SectChat sectChat = UserDB.getInstance().querySect(message.getUid());
                    if (sectChat == null)return;
                    boolean isAvatarExists = imageLoader != null && sectChat != null && sectChat.getAvatar() != null
                       /* && message.getUser().getAvatar() != null
                        && !message.getUser().getAvatar().isEmpty()*/;// FIXME: 17-12-6 头像

                    userAvatar.setVisibility(View.VISIBLE);
                    if (isAvatarExists) {
                        imageLoader.loadImage(userAvatar, sectChat.getAvatar());
                    } else {
                        userAvatar.setImageResource(R.mipmap.ic_launcher);
                    }
                }
            }
            if (time != null) {
                long time = message.getCreatedAt().getTime();
                if (lastTime == 0 || (time - lastTime) > 300000) {
                    this.time.setVisibility(View.VISIBLE);
                    this.time.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
                } else {
                    this.time.setVisibility(View.GONE);
                }

                lastTime = time;
            }
        }

        @Override
        public void applyStyle(MessagesListStyle style) {
            if (time != null) {
                time.setTextColor(style.getOutcomingTimeTextColor());
                time.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getOutcomingTimeTextSize());
                time.setTypeface(time.getTypeface(), style.getOutcomingTimeTextStyle());
            }
            if (userAvatar != null) {
                userAvatar.getLayoutParams().width = style.getIncomingAvatarWidth();
                userAvatar.getLayoutParams().height = style.getIncomingAvatarHeight();
            }
        }
    }

    /*
    * DEFAULTS
    * */

    interface DefaultMessageViewHolder {
        void applyStyle(MessagesListStyle style);
    }

    private static class DefaultIncomingTextMessageViewHolder
            extends IncomingTextMessageViewHolder<Message> {

        public DefaultIncomingTextMessageViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class DefaultOutcomingTextMessageViewHolder
            extends OutcomingTextMessageViewHolder<Message> {

        public DefaultOutcomingTextMessageViewHolder(View itemView) {
            super(itemView);
        }
    }


    private static class ContentTypeConfig<TYPE extends MessageContentType> {

        private byte type;
        private HolderConfig<TYPE> incomingConfig;
        private HolderConfig<TYPE> outcomingConfig;

        private ContentTypeConfig(
                byte type, HolderConfig<TYPE> incomingConfig, HolderConfig<TYPE> outcomingConfig) {

            this.type = type;
            this.incomingConfig = incomingConfig;
            this.outcomingConfig = outcomingConfig;
        }
    }

    private class HolderConfig<T extends Message> {

        protected Class<? extends BaseMessageViewHolder<? extends T>> holder;
        protected int                                                 layout;

        HolderConfig(Class<? extends BaseMessageViewHolder<? extends T>> holder, int layout) {
            this.holder = holder;
            this.layout = layout;
        }
    }
}
