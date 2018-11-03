package com.cartoon.module.bangai;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.Constants;
import com.cartoon.bean.Bangai;
import com.cartoon.data.CartoonCommentChild;
import com.cartoon.data.Keys;
import com.cartoon.data.NewBase;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.CommentData;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.ApiQuestListener;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.module.mymoment.OthersMomentActivity;
import com.cartoon.module.tab.bookfriendmoment.GridImageAdapter;
import com.cartoon.module.tab.bookfriendmoment.PreviewPhotoActivity;
import com.cartoon.utils.ApiUtils;
import com.cartoon.utils.CatchImage;
import com.cartoon.utils.ImageLoaderUtils;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.view.dialog.DeleteDialog;
import com.cartoon.view.ExpandGridView;
import com.cartoon.view.dialog.ReportDialog;
import com.cartoon.view.SelectableImageView;
import com.cartton.library.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * 评论适配器
 * <p/>
 * Created by David on 16/6/27.
 */
public class BangaiDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEAD = 0x101;
    private static final int TYPE_ITEM = 0x102;

    private int sort_type = BangaiDetailActivity.TYPE_LATEST;
    private View.OnClickListener onClickSubViewListener;
    /**
     * 二级评论触发
     */
    public interface OnItemCommentListener {
        public void onItemCommentClick(CommentData comment, int position);

        public void onItemClick(CommentData comment, int position);

        public void onItemCommentClick(String comment_id, CartoonCommentChild child, int position);

    }

    public interface OnItemCommentTypeChange {
        public void OnItemCommentTypeChange(int type);
    }

    private Context                 context;
    private Bangai                  expound;
    private String                  totalNum;
    private List<CommentData>    mList;
    private List<NewBase>           bList;
    private OnItemCommentListener   listener;
    private OnItemCommentTypeChange changeListener;
    private View.OnClickListener    onClickReadListener;

    private Activity mActivity;

    public BangaiDetailAdapter(Context context, Activity activity) {
        this.context = context;
        this.mActivity = activity;
    }

    public void setOnClickReadListener(View.OnClickListener onClickReadListener) {
        this.onClickReadListener = onClickReadListener;
    }
    public void setExpound(Bangai expound) {
        this.expound = expound;
        notifyDataSetChanged();
    }

    public void setData(List<CommentData> mList, String total) {
        this.mList = mList;
        this.totalNum = total;
        notifyDataSetChanged();
    }

    public void addAll(List<CommentData> list,String total) {
        if (mList != null) {

            int size  = this.mList.size();
            this.mList.addAll(list);
            notifyItemRangeInserted(size + 1, list.size());
        } else {
            this.mList = list;
            this.totalNum = total;
            notifyDataSetChanged();
        }

    }

    public void setListener(OnItemCommentListener listener) {
        this.listener = listener;
    }

    public void setChangeListener(OnItemCommentTypeChange changeListener) {
        this.changeListener = changeListener;
    }
    public CommentData getItem(int position) {
        return mList.get(position-1);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_bangai_head, parent, false);
            return new HeadHolder(view);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_cartoon_comment_first, parent, false);

        return new CMViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
      //  bList = new ArrayList<>();
       // loadContent();
        if (viewHolder instanceof HeadHolder) {
            final HeadHolder holder = (HeadHolder) viewHolder;
            if (expound == null)
                return;
            holder.tvDesc.setText("共" + (TextUtils.isEmpty(totalNum) ? "0" : totalNum) + "条评论");

            holder.tvTitle.setText(expound.getTitle());

            holder.webView.loadData(expound.getContent(), "text/html; charset=UTF-8", null);
            holder.webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    WebView.HitTestResult hr = ((WebView) v).getHitTestResult();
                    if (hr != null && event != null) {

                        if (event.getAction() == MotionEvent.ACTION_UP && hr.getType() == 5) {//IMAGE_TYPE
                            Log.i("onTouch", "getExtra = " + hr.getExtra() + "\t Type=" + hr.getType() + "\t event=" + event.getAction());
                            ArrayList<String> images = CatchImage.getImages(expound.getContent());

                            if (images != null && images.size() > 0) {
                                Intent intent = new Intent(context, PreviewPhotoActivity.class);
                                intent.putExtra(PreviewPhotoActivity.PHOTO_URLS, images.toArray(new String[0]));
                                intent.putExtra(PreviewPhotoActivity.POSITION, getPosition(images, hr.getExtra()));
                                context.startActivity(intent);
                            }

                        }
                    }

                    return false;
                }
            });


            holder.tvTime.setText(expound.getCreate_time());

            if (sort_type == BangaiDetailActivity.TYPE_HOT) {
                holder.tvHot.setSelected(true);
                holder.tvNew.setSelected(false);
            } else {
                holder.tvHot.setSelected(false);
                holder.tvNew.setSelected(true);
            }


            holder.tvHot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sort_type = BangaiDetailActivity.TYPE_HOT;
                    holder.tvHot.setSelected(true);
                    holder.tvNew.setSelected(false);
                    if (changeListener != null) {
                        changeListener.OnItemCommentTypeChange(BangaiDetailActivity.TYPE_HOT);
                    }
                }
            });
            holder.tvNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sort_type = BangaiDetailActivity.TYPE_LATEST;

                    holder.tvHot.setSelected(false);
                    holder.tvNew.setSelected(true);
                    if (changeListener != null) {
                        changeListener.OnItemCommentTypeChange(BangaiDetailActivity.TYPE_LATEST);
                    }
                }
            });

            if (mList == null || mList.size() == 0) {
                holder.llEmpty.setVisibility(View.VISIBLE);
            }else
                holder.llEmpty.setVisibility(View.GONE);

            return;
        }
        final int pos = position - 1;
        final CMViewHolder holder = (CMViewHolder) viewHolder;
        final CommentData comment = mList.get(pos);

        holder.ivCover.setTag(R.id.position_book_friend_moment, position);
        holder.ivCover.setTapListener(new SelectableImageView.ITapListener() {
            @Override
            public void onTaped() {
                onClickSubViewListener.onClick(holder.ivCover);
            }
        });

        holder.tvName.setText(comment.getNickname());
        NicknameColorHelper.setNicknameColor(holder.tvName, comment.getUid());
        holder.tvBuild.setText(Integer.parseInt(totalNum)-pos+"楼");
        holder.tvTime.setText(comment.getCreate_time());
        if (comment.getContent() != null && !comment.getContent().isEmpty()) {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(comment.getContent());
        } else {
            holder.tvContent.setVisibility(View.GONE);
        }
        if (comment.getComment_num()==0)
            holder.tvComment.setText("回复");
        else
            holder.tvComment.setText(comment.getComment_num()+"条回复");
        holder.tvFocus.setText(comment.getApprove_num());


        if (!TextUtils.isEmpty(comment.getHonorName())) {
            if (!TextUtils.equals(comment.getHonorName(),"认证")){

                holder.tvSpecial.setText(comment.getHonorName());
                holder.tvSpecial.setBackgroundResource(R.drawable.transparent_background);
            }
            else{
                holder.tvSpecial.setText("");
                holder.tvSpecial.setBackgroundResource(R.mipmap.official);
            }
        } else {
            holder.tvSpecial.setVisibility(View.GONE);
        }


        holder.tvBonus.setText(comment.getLvName());

        if (comment.getIs_approve() == 1) {
            holder.tvFocus.setSelected(true);
            holder.tvFocus.setTextColor(Color.parseColor("#ef5f11"));
        } else {
            holder.tvFocus.setSelected(false);
            holder.tvFocus.setTextColor(Color.parseColor("#5b5c5e"));
        }


        holder.tvDelete.setVisibility(TextUtils.equals(comment.getUid()+"",
                CartoonApp.getInstance().getUserId()) ? View.VISIBLE : View.GONE);

        ImageLoaderUtils.displayRound(context,holder.ivIcon,comment.getAvatar());

        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserAlreadyLogin()) {
                    Intent intent = new Intent(context, OthersMomentActivity.class);
                    intent.putExtra(Keys.TARGET_UID, comment.getUid() + "");
                    intent.putExtra(Keys.TARGET_OTHERS, comment.getNickname());
                    context.startActivity(intent);
                }
                //// FIXME: 16-11-30  代入参数
                else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }
        });
        holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CartoonApp.getInstance().isLogin(context)) {
                    return;
                }

                if (listener != null) {
                    listener.onItemCommentClick(comment, pos);
                }
            }
        });


        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(comment, pos);
                }
            }
        });


        if (TextUtils.equals(comment.getUid(), CartoonApp.getInstance().getUserId())) {
            holder.tvDelete.setVisibility(View.VISIBLE);
            holder.ivDelete.setVisibility(View.GONE);
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CartoonApp.getInstance().isLogin(context)) {
                        return;
                    }
                    DeleteDialog deleteDialog = new DeleteDialog(mActivity,"删除");
                    deleteDialog.setOnButtonClickListener(new DeleteDialog.buttonClick() {
                        @Override
                        public void onButtonClickListener() {
                            deleteComment(comment.getId(), holder, pos);
                        }
                    });
                    deleteDialog.show();

                }
            });
        } else {
            holder.tvDelete.setVisibility(View.GONE);
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CartoonApp.getInstance().isLogin(context)) {
                        return;
                    }
                    DeleteDialog deleteDialog = new DeleteDialog(mActivity,"举报");
                    deleteDialog.setOnButtonClickListener(new DeleteDialog.buttonClick() {
                        @Override
                        public void onButtonClickListener() {
                            ReportDialog dialog1 = new ReportDialog(context,comment.getUid(),comment.getId(),"1");// FIXME: 17-3-30 对应id是否对
                            dialog1.show();
                        }
                    });
                    deleteDialog.show();
                }
            });
        }

        holder.tvFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiUtils.approveTarget(comment.getId(), Constants.APPROVE_COMMENT, new ApiQuestListener() {
                    @Override
                    public void onLoadFail() {

                    }

                    @Override
                    public void onLoadSuccess(String response) {
                        comment.setIs_approve(1);
                        int num = Integer.parseInt(comment.getApprove_num());
                        comment.setApprove_num(String.valueOf(num + 1));
                        notifyItemChanged(position);

                    }
                });
            }
        });

        if (comment.getPhoto() != null && comment.getPhoto().size() > 0) {
            if (comment.getPhoto().size() > 1) {
                // show gridView
                holder.gridView.setVisibility(View.VISIBLE);
                holder.ivCover.setVisibility(View.GONE);
                holder.gridView.setAdapter(new GridImageAdapter(new WeakReference<>(holder.gridView.getContext()), comment.getPhoto(), onClickSubViewListener, position));
            } else {
                // show single imageView
                holder.gridView.setVisibility(View.GONE);
                holder.ivCover.setVisibility(View.VISIBLE);
                Glide.with(holder.ivCover.getContext()).load(comment.getPhoto().get(0)).centerCrop().placeholder(R.drawable.default_photo).into(holder.ivCover);
            }
        } else {
            // show single imageView
            holder.gridView.setVisibility(View.GONE);
            holder.ivCover.setVisibility(View.GONE);
        }
    }
    public void setOnClickSubViewListener(View.OnClickListener onClickSubViewListener) {
        this.onClickSubViewListener = onClickSubViewListener;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        }
        return TYPE_ITEM;
    }

    private boolean isUserAlreadyLogin() {
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        return userInfo != null;
    }

    @Override
    public int getItemCount() {
        //return mList == null ? 1 : (mList.size() > 4 ? 4 : mList.size()+1);
        return mList == null ? 1 : mList.size() + 1;
    }


    /**
     * 删除评论
     * <p/>
     * //is_two.用户删除评论的类型  一级评论为针对于漫画听书..    二级评论为针对于一级评论的回复评论
     * //根据参数不同操作的表也不同 ,
     * is_two =1 :删除一级评论 id参数为一级评论的唯一id值
     * is_two=2 :删除二级评论  id参数为二级评论的唯一id值
     */
    private void deleteComment(String comment_id, final CMViewHolder holder, final int position) {
        ((BaseActivity) context).showLoadingDialog();
        BuilderInstance.getInstance()
                .getGetBuilderInstance(StaticField.URL_DEL_COMMENT)
                .addParams("id", comment_id)
                .addParams("is_two", "1")
                .build().execute(new BaseCallBack() {
            @Override
            public void onLoadFail() {
                ((BaseActivity) context).hideLoadingDialog();
                ToastUtils.showShort(context, "...fail..");
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(Object response) {
                ((BaseActivity) context).hideLoadingDialog();
                ToastUtils.showShort(context, "删除成功");
                notifyItemRangeChanged(position, mList.size());
                if (mList.size() == 1) {
                    mList.remove(0);//防止数组越界
                } else {

                    mList.remove(position);
                }
                notifyItemRemoved(position + 1);
            }

            @Override
            public Object parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, Object.class);
            }
        });
    }

    class CMViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView           ivIcon;
        @BindView(R.id.tv_name)
        TextView            tvName;
        @BindView(R.id.tv_time)
        TextView            tvTime;
        @BindView(R.id.tv_focus)
        TextView            tvFocus;
        @BindView(R.id.tv_comment)
        TextView            tvComment;
        @BindView(R.id.tv_content)
        TextView            tvContent;
        @BindView(R.id.gridView)
        ExpandGridView      gridView;
        @BindView(R.id.iv_cover)
        SelectableImageView ivCover;
        @BindView(R.id.iv_delete)
        ImageView           ivDelete;
        @BindView(R.id.tv_delete)
        TextView            tvDelete;
        @BindView(R.id.ll_item)
        LinearLayout        llItem;
        @BindView(R.id.tv_bonus)
        TextView            tvBonus;
        @BindView(R.id.tv_special_name)
        TextView            tvSpecial;
        @BindView(R.id.tv_build)
        TextView            tvBuild;


        public CMViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeadHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_desc)
        TextView       tvDesc;
        @BindView(R.id.tv_hot)
        TextView       tvHot;
        @BindView(R.id.tv_new)
        TextView       tvNew;
        @BindView(R.id.rl_summary)
        RelativeLayout rlSummary;
        @BindView(R.id.tv_title)
        TextView       tvTitle;
        @BindView(R.id.tv_time)
        TextView       tvTime;
        //        @BindView(R.id.tv_content)
        //        HtmlTextView tvContent;
        @BindView(R.id.icon_cover)
        ImageView      iconCover;
        @BindView(R.id.webView)
        WebView        webView;
        @BindView(R.id.tv_preface)
        TextView       tvPreface;
        @BindView(R.id.ll_empty)
        TextView llEmpty;
        /*@BindView(R.id.next_bangai)
        Button mNextBangai;
        @BindView(R.id.cata_log)
        Button mCataLog;
        @BindView(R.id.pre_bangai)
        Button mPreBangai;*/
        public HeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ChildAdapter extends BaseAdapter {

        private List<CartoonCommentChild> list;

        public ChildAdapter(List<CartoonCommentChild> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : (list.size() > 3 ? 3 : list.size());
        }

        @Override
        public CartoonCommentChild getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder holder;
            if (convertView == null) {
                holder = new MyHolder();
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.item_child_list, parent, false);
                holder.textView = (TextView) convertView.findViewById(R.id.tv_comment);
                convertView.setTag(holder);
            } else {
                holder = (MyHolder) convertView.getTag();
            }
            CartoonCommentChild item = list.get(position);


            String content = item.getTo_content();
            if (!TextUtils.isEmpty(content) && content.length() > 0 && content.charAt(0) == '@' && content.contains(":")) {

                String name = content.substring(1, content.indexOf(":"));
                String real_content = content.substring(content.indexOf(":") + 1, content.length());


                holder.textView.setText(Html.fromHtml(
                        "<font color=#3e649b>" + item.getNickname() + "@</font>" +
                                "<font color=#3e649b>" + name + ":</font>" +
                                real_content));
            } else {

                holder.textView.setText(Html.fromHtml(
                        "<font color=#3e649b>" + item.getNickname() + ":</font>" + item.getTo_content()));
            }

            return convertView;
        }
    }

    class MyHolder {
        private TextView textView;
    }

    private int getPosition(ArrayList<String> list, String item) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(item)) {
                    return i;
                }
            }
        }

        return 0;
    }


}
