package com.cartoon.module.tab.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cartoon.CartoonApp;
import com.cartoon.data.MineSectData;
import com.cartoon.data.SectAgree;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.utils.ImageLoaderUtils;
import com.cartoon.utils.Utils;
import com.cartoon.view.DialogToast;
import com.cartton.library.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-10-16.
 */
public class SectEventListAdpter extends RecyclerView.Adapter<SectEventListAdpter.ViewHolder> {

    private Context            context;
    private List<MineSectData> Sectlist;

    public SectEventListAdpter(Context context) {
        this.context = context;
    }

    public void setData(List<MineSectData> list){
        this.Sectlist = list;
        notifyDataSetChanged();
    }


    public void addAll(List<MineSectData> list) {
        int size = this.Sectlist.size();
        this.Sectlist.addAll(list);
        notifyItemRangeInserted(size + 1, list.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sect2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTvPupilLevel.setVisibility(View.GONE);
        holder.mIvBang.setVisibility(View.GONE);
        holder.mIvLeader.setVisibility(View.GONE);
        holder.mTvSectLevel.setVisibility(View.GONE);
        holder.mTvSectCheif.setVisibility(View.GONE);
        holder.mTvSectL.setVisibility(View.GONE);
        holder.mTvSectPupil.setVisibility(View.GONE);
        holder.mView.setVisibility(View.GONE);
        final MineSectData sectList = Sectlist.get(position);
        holder.mTvSectName.setText(sectList.getNickname());

        ImageLoaderUtils.displayRound(context,holder.mIvIcon, Utils.addBaseUrlShare(sectList.getAvatar()));
        if (sectList.getUser_status() == 1) {//申请加入宗门
            holder.mTvOk.setVisibility(View.VISIBLE);
            holder.mTvOk.setText("同意");
            holder.mTvSectN.setText("申请加入宗门");
        }else{
            holder.mTvOk.setVisibility(View.GONE);
            holder.mTvSectN.setText(sectList.getMsg());
        }
        holder.mTvSelfLevel.setText(sectList.getLvName());

        holder.mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SECTAGREE)
                        .addParams( "uid", CartoonApp.getInstance().getUserId())
                        .addParams("token" ,CartoonApp.getInstance().getToken())
                        .addParams("sectionId",Sectlist.get(position).getSect_id()+"")
                        .addParams("applier",Sectlist.get(position).getUser_id()+"")
                        .build().execute(new BaseCallBack<String>() {
                    @Override
                    public void onLoadFail() {
                        ToastUtils.showShort(context,"暂无法执行");
                    }

                    @Override
                    public void onContentNull() {

                    }

                    @Override
                    public void onLoadSuccess(String response) {
                        DialogToast.createToastConfig().ToastShow(context,"已同意",4);
                        holder.mTvOk.setSelected(true);
                        holder.mTvOk.setText("已同意");
                        EventBus.getDefault().post(new SectAgree());

                    }

                    @Override
                    public String parseNetworkResponse(String response) throws Exception {
                        return response;
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return Sectlist!=null?Sectlist.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView      mIvIcon;
        @BindView(R.id.iv_bang)
        ImageView           mIvBang;
        @BindView(R.id.tv_sect_name)
        TextView       mTvSectName;
        @BindView(R.id.tv_sect_level)
        TextView            mTvSectLevel;
        @BindView(R.id.tv_self_level)
        TextView            mTvSelfLevel;
        @BindView(R.id.tv_pupil_level)
        TextView            mTvPupilLevel;
        @BindView(R.id.tv_sect_n)
        TextView            mTvSectN;
        @BindView(R.id.tv_sect_cheif)
        TextView            mTvSectCheif;
        @BindView(R.id.tv_sect_l)
        TextView            mTvSectL;
        @BindView(R.id.tv_sect_pupil)
        TextView            mTvSectPupil;
        @BindView(R.id.ll_build_sect)
        LinearLayout   mLlBuildSect;
        @BindView(R.id.tv_sect_message)
        TextView            mTvSectMessage;
        @BindView(R.id.tv_ok)
        TextView            mTvOk;
        @BindView(R.id.iv_leader)
        ImageView           mIvLeader;
        @BindView(R.id.rl_item)
        RelativeLayout mRlItem;
        @BindView(R.id.vv)
        View mView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
