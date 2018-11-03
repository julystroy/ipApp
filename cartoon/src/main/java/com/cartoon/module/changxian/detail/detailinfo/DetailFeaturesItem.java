package com.cartoon.module.changxian.detail.detailinfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.cartoon.data.AppFeature;
import com.cartoon.volley.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

import cn.com.xuanjiezhimen.R;

/**
 * Created by shidu on 17/1/4.
 */

public class DetailFeaturesItem extends LinearLayout {

    private GridView gvIcon;
    private AppFeaturesAdapter mAdapter;

    public DetailFeaturesItem(Context context) {
        super(context);

        init();
    }

    public DetailFeaturesItem(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.layout_detail_features, this);
        gvIcon = (GridView) findViewById(R.id.gridView);
        mAdapter = new AppFeaturesAdapter(getContext());
        gvIcon.setAdapter(mAdapter);
    }

    public void setData(List<AppFeature> datas) {
        mAdapter.setData(datas);
    }

    /**
     * @author 叶盛武
     * @version V1.0
     * @date 2013-3-22 下午3:08:08
     * @description 4个banner
     */
    private static final class AppFeaturesAdapter extends BaseAdapter {

        private Context context;
        private List<AppFeature> mitems;
        private final LayoutInflater mInflater;

        public AppFeaturesAdapter(Context context) {
            this.context = context;

            mInflater = LayoutInflater.from(context);
            mitems = new ArrayList<AppFeature>();
        }

        @Override
        public int getCount() {
            return mitems == null ? 0 : mitems.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public AppFeature getItem(int position) {
            return mitems.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppFeature item = mitems.get(position);
            String iconUrl = item.icon;
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_detail_features_grid, null);

                viewHolder = new ViewHolder();
                viewHolder.image = (NetworkImageView) convertView
                        .findViewById(R.id.iv_detail_feature);
                viewHolder.text = (TextView) convertView
                        .findViewById(R.id.tv_detail_feature_name);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.text.setText(item.key);
            viewHolder.image.setDefaultImageResId(R.drawable.feature_language);
            viewHolder.image.setImageUrl(iconUrl, VolleySingleton.getInstance(context).getImageLoader());

            return convertView;
        }

        public void setData(List<AppFeature> datas) {
            if (datas != null && datas.size() > 0) {
                mitems.addAll(datas);

                notifyDataSetChanged();
            }

        }

        private static final class ViewHolder {
            NetworkImageView image;
            TextView text;
        }
    }

}
