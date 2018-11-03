package com.cartoon.module.newmodules;

/**
 * 通用选集适配器
 * Created by debuggerx on 16-11-21.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cartoon.data.Keys;
import com.cartoon.data.NewBase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

public class NewBaseCollectAdapter extends RecyclerView.Adapter<NewBaseCollectAdapter.ViewHolder> {

    private Context                        context;
    private List<NewBase>                  mList;
    private NewBasePopDialog.OnHideListener mOnHideListener;
    private int modulesType;

    public NewBaseCollectAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<NewBase> mList , int modulesType) {
        this.mList = mList;
        this.modulesType = modulesType;
        notifyDataSetChanged();
    }

    public void setOnHideListener(NewBasePopDialog.OnHideListener mOnHideListener) {
        this.mOnHideListener = mOnHideListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cartoon_down, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NewBase newBase = mList.get(position);
        holder.tvCollect.setText(newBase.getCatalog());

        holder.tvCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnHideListener != null) {
                    mOnHideListener.hide();
                }

                //EventBus.getDefault().post(new EventChangeCollect(mList.get(position).getId(), mList.get(position).getTitle()));
/*                                Intent intent = new Intent(context, NewBaseActivity.class);
                                intent.putExtra(Keys.CARTOON_ID, mList.get(position).getId());
                                intent.putExtra(Keys.CARTOON_TITLE, mList.get(position).getTitle());
                                context.startActivity(intent);*/

/*
                Intent intent = new Intent(this, NewBaseActivity.class);
                intent.putExtra(Keys.COMMENT_ID, mList.get(position).getId());
                intent.putExtra(Keys.SOURCE, fragmentName[NewBaseStaticField.getApiNumByType(modulesType)]);
                intent.putExtra(Keys.SHOW_KEYBOARD, true);

                context.startActivity(intent);*/



                Intent intent = new Intent(context, NewBaseActivity.class);
                intent.putExtra(Keys.TARGET_ID, String.valueOf(mList.get(position).getId()));
                intent.putExtra(Keys.TARGET_OBJECT, mList.get(position));
                intent.putExtra(Keys.COMMENT_TYPE, modulesType);


                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_collect)
        TextView tvCollect;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
