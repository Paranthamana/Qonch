package com.QonchAssets.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.QonchAssets.Model.NavigationDataModel;
import com.QonchAssets.event.NavigationItemClickEvent;
import com.zebra.qonchAssets.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class NavigationMenuAdapter extends BaseAdapter {
    private final List<NavigationDataModel> navigationDataModels;
    private Context context;

    public NavigationMenuAdapter(Context context, List<NavigationDataModel> navigationDataModels) {
        this.context = context;
        this.navigationDataModels = navigationDataModels;
    }


    @Override
    public int getCount() {
        return navigationDataModels.size();
    }

    @Override
    public Object getItem(int position) {
        return navigationDataModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_navigation_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final NavigationDataModel navigationDataModel = navigationDataModels.get(position);
        holder.tvNavigationItemName.setText(navigationDataModel.getName());
        holder.ivIcon.setImageResource(navigationDataModel.getIcons());

        if (navigationDataModel.getHeader() != null) {
            holder.tvHeader.setText(navigationDataModel.getHeader());
            holder.tvHeader.setVisibility(View.VISIBLE);
        } else {
            holder.tvHeader.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(v -> {
            int id = navigationDataModel.getId();
            EventBus.getDefault().post(new NavigationItemClickEvent(id));
        });

        return convertView;
    }


    static class ViewHolder {
        ImageView ivIcon;
        TextView tvNavigationItemName;
        TextView tvHeader;

        ViewHolder(View view) {
            ivIcon = view.findViewById(R.id.iv_icon);
            tvNavigationItemName = view.findViewById(R.id.tvNavigationItemName);
            tvHeader = view.findViewById(R.id.tvHeader);

        }
    }
}