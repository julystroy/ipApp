package com.cartoon.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cartoon.data.Cartoon;
import com.cartoon.data.EventChangeCollect;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * 漫画选集适配器
 * <p/>
 * Created by David on 16/6/27.
 */
public class CartoonCollectAdapter extends RecyclerView.Adapter<CartoonCollectAdapter.ViewHolder> {

    private Context context;
    private List<Cartoon> mList;
    private CommonPopDialog.OnHideListener mOnHideListener;

    public CartoonCollectAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Cartoon> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setOnHideListener(CommonPopDialog.OnHideListener mOnHideListener) {
        this.mOnHideListener = mOnHideListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cartoon_down, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Cartoon cartoon = mList.get(position);
        holder.tvCollect.setText(cartoon.getCatalog());

        holder.tvCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnHideListener != null) {
                    mOnHideListener.hide();
                }

                EventBus.getDefault().post(new EventChangeCollect(mList.get(position).getId(), mList.get(position).getTitle()));
//                Intent intent = new Intent(context, ReaderActivity.class);
//                intent.putExtra(Keys.CARTOON_ID, mList.get(position).getId());
//                intent.putExtra(Keys.CARTOON_TITLE, mList.get(position).getTitle());
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_collect)
        TextView     tvCollect;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
