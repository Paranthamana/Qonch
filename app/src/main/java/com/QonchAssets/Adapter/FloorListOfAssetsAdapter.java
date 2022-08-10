package com.QonchAssets.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.QonchAssets.ApiResponse.AssetsByFloorResponse;
import com.QonchAssets.Common.CustomProgressDialog;
import com.zebra.qonchAssets.R;

import java.util.List;

public class FloorListOfAssetsAdapter extends RecyclerView.Adapter<FloorListOfAssetsAdapter.Holder> {

    Context mContext;
    List<AssetsByFloorResponse.Asset> floorList;


    public FloorListOfAssetsAdapter(Context context, List<AssetsByFloorResponse.Asset> floorList) {
        this.floorList = floorList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public FloorListOfAssetsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View previousList = layoutInflater.inflate(R.layout.adapter_floor_list_of_assets, parent,
                false);
        return new Holder(previousList);
    }

    @Override
    public void onBindViewHolder(@NonNull FloorListOfAssetsAdapter.Holder holder, int position) {
        CustomProgressDialog.getInstance().dismiss();
        holder.tvFloorAssertTagIdValue.setText(floorList.get(position).getTagId());
        holder.tvFloorDescriptionValue.setText(floorList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return floorList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView tvFloorAssertTagIdValue;
        TextView tvFloorDescriptionValue;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvFloorDescriptionValue = itemView.findViewById(R.id.tvFloorDescriptionValue);
            tvFloorAssertTagIdValue = itemView.findViewById(R.id.tvFloorAssertTagIdValue);

        }
    }
}
