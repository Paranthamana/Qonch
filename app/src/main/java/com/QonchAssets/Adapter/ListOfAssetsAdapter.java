package com.QonchAssets.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.QonchAssets.Activity.AssetsViewDetailsActivity;
import com.QonchAssets.ApiResponse.ListofAssetsResponse;
import com.QonchAssets.Common.CommonFunctions;
import com.zebra.qonchAssets.R;

import java.util.List;

public class ListOfAssetsAdapter extends RecyclerView.Adapter<ListOfAssetsAdapter.Holder> {

    List<ListofAssetsResponse.Asset> listOfAssets;
    Context mContext;

    public ListOfAssetsAdapter(Context context, List<ListofAssetsResponse.Asset> listOfAssets) {
        this.mContext = context;
        this.listOfAssets = listOfAssets;
    }

    @NonNull
    @Override
    public ListOfAssetsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View previousList = layoutInflater.inflate(R.layout.adapter_list_of_assets, parent,
                false);
        return new Holder(previousList);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListOfAssetsAdapter.Holder holder, @SuppressLint("RecyclerView") int position) {
        byte[] imageAsBytes = Base64.decode(listOfAssets.get(position)
                .getThumbnail().getBytes(), Base64.DEFAULT);

        if (listOfAssets.get(position).getThumbnail().equals("") ||
                listOfAssets.get(position).getThumbnail() == null) {
            holder.ivAssets.setImageResource(R.drawable.ic_no_image);
        } else {
            holder.ivAssets.setImageBitmap(
                    BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }



        holder.tvAssertTagIdValue.setText(" " + listOfAssets.get(position).getTagId());
        holder.tvBrandValue.setText(" " + listOfAssets.get(position).getBrand());
        holder.tvCostValue.setText(" " + listOfAssets.get(position).getCost().toString());
        holder.tvDescriptionValue.setText(" " + listOfAssets.get(position).getDescription());
        holder.tvPurchasedFromValue.setText(" " + listOfAssets.get(position).getPurchasedFrom());
        holder.tvCreatedByValue.setText(" " + listOfAssets.get(position).getCreatedUser());

        holder.cardView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("AssetsId", listOfAssets.get(position).getId().toString());
            CommonFunctions.getInstance().newIntent(mContext,
                    AssetsViewDetailsActivity.class, bundle, false);
        });
    }

    @Override
    public int getItemCount() {
        return listOfAssets.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView tvAssertTagIdValue, tvBrandValue, tvCostValue,
                tvDescriptionValue, tvPurchasedFromValue, tvCreatedByValue;

        ImageView ivAssets;
        CardView cardView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvAssertTagIdValue = itemView.findViewById(R.id.tvAssertTagIdValue);
            tvBrandValue = itemView.findViewById(R.id.tvBrandValue);
            tvCostValue = itemView.findViewById(R.id.tvCostValue);
            tvDescriptionValue = itemView.findViewById(R.id.tvDescriptionValue);
            tvPurchasedFromValue = itemView.findViewById(R.id.tvPurchasedFromValue);
            tvCreatedByValue = itemView.findViewById(R.id.tvCreatedByValue);
            ivAssets = itemView.findViewById(R.id.ivAssets);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
