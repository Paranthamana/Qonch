package com.QonchAssets.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.QonchAssets.Activity.ScanAuditingActivity;
import com.QonchAssets.ApiResponse.SectionApiResponse;
import com.QonchAssets.Common.CommonFunctions;
import com.QonchAssets.Common.CustomProgressDialog;
import com.zebra.qonchAssets.R;

import java.util.List;

public class ListOfAssetsSectionAdapter extends RecyclerView.Adapter<ListOfAssetsSectionAdapter.Holder> {

    Context mContext;
    List<SectionApiResponse> sectionResponse;

    public ListOfAssetsSectionAdapter(Context context, List<SectionApiResponse> sectionResponse) {
        this.sectionResponse = sectionResponse;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ListOfAssetsSectionAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View previousList = layoutInflater.inflate(R.layout.adapter_list_of_assets_section, parent,
                false);
        return new Holder(previousList);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfAssetsSectionAdapter.Holder holder,
                                 @SuppressLint("RecyclerView") int position) {
        CustomProgressDialog.getInstance().dismiss();
        holder.tvSectionAssertTagIdValue.setText(sectionResponse.get(position).getName());

        holder.llAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Selected_SectionID",sectionResponse.get(position).getId().toString());
                CommonFunctions.getInstance().newIntent(mContext, ScanAuditingActivity.class,
                        bundle,false);
            }
        });
        CustomProgressDialog.getInstance().dismiss();
    }

    @Override
    public int getItemCount() {
        return sectionResponse.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView tvSectionAssertTagIdValue;
        LinearLayout llAssets;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvSectionAssertTagIdValue = itemView.findViewById(R.id.tvSectionAssertTagIdValue);
            llAssets = itemView.findViewById(R.id.llAssets);
        }
    }
}
