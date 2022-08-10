package com.QonchAssets.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.QonchAssets.Activity.ScanAuditingActivity;
import com.QonchAssets.Model.AuditScreenModel;
import com.zebra.qonchAssets.R;

import java.util.ArrayList;

public class AuditListAdapter extends RecyclerView.Adapter<AuditListAdapter.holder> {

    Context mContext;
    ArrayList<AuditScreenModel> listData;
    TextView textView;

    public AuditListAdapter(ScanAuditingActivity activity,
                            ArrayList<AuditScreenModel> listData, TextView textView) {
        this.mContext = activity;
        this.listData = listData;
        this.textView = textView;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View previousList = layoutInflater.inflate(R.layout.adapter_audit_list, parent,
                false);
        return new holder(previousList);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        try {
            holder.tvAssertNameValue.setText(listData.get(position).getAssetQRBarCode());
            holder.tvAuditTimeValue.setText(listData.get(position).getAuditTime());
            holder.tvAssertTagIDValue.append(listData.get(position).getAssertTagId());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class holder extends RecyclerView.ViewHolder {

        TextView tvAssertNameValue;
        TextView tvAssertTagIDValue;
        TextView tvAuditTimeValue;
        LinearLayout rlAdapterData;

        public holder(@NonNull View itemView) {
            super(itemView);

            tvAssertNameValue = itemView.findViewById(R.id.tvAssertNameValue);
            tvAssertTagIDValue = itemView.findViewById(R.id.tvAssertTagIDValue);
            tvAuditTimeValue = itemView.findViewById(R.id.tvAuditTimeValue);
            rlAdapterData = itemView.findViewById(R.id.rlAdapterData);
        }
    }
}
