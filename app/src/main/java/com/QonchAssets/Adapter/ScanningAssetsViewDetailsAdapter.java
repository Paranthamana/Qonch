package com.QonchAssets.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.QonchAssets.ApiResponse.AssetBySectionResponse;
import com.QonchAssets.Common.Constants;
import com.QonchAssets.Model.AssetByFloorModel;
import com.QonchAssets.RealmDB.ScanTagIDAndTimeModel;
import com.QonchAssets.event.AlertClickListener;
import com.zebra.qonchAssets.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ScanningAssetsViewDetailsAdapter extends RecyclerView.Adapter<ScanningAssetsViewDetailsAdapter.Holder>
        implements AlertClickListener {

    Context mContext;
    RealmResults<ScanTagIDAndTimeModel> scanTagIDAndTimeModel;
    RealmResults<AssetByFloorModel> assetByFloorModelsDB;
    List<AssetBySectionResponse.Asset> floorList;
    String statusText;
    String str;
    Realm realm;

    public ScanningAssetsViewDetailsAdapter(Context context,
                                            RealmResults<ScanTagIDAndTimeModel> scanTagIDAndTimeModel,
                                            List<AssetBySectionResponse.Asset> floorList,
                                            RealmResults<AssetByFloorModel> assetByFloorModelsDB) {
        this.mContext = context;
        this.scanTagIDAndTimeModel = scanTagIDAndTimeModel;
        this.floorList = floorList;
        this.assetByFloorModelsDB = assetByFloorModelsDB;
    }

    @NonNull
    @Override
    public ScanningAssetsViewDetailsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View previousList = layoutInflater.inflate(R.layout.adapter_scan_assets_viewdetails, parent,
                false);
        return new Holder(previousList);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ScanningAssetsViewDetailsAdapter.Holder holder,
                                 @SuppressLint("RecyclerView") int position) {

        if (scanTagIDAndTimeModel != null) {
            try {
                Date d = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =
                        new SimpleDateFormat("hh:mm a");
                String currentDateTimeString = sdf.format(d);
                statusText = "UnScanned";

                holder.tvAssertViewTagIdValue.setText(assetByFloorModelsDB.get(position).getTagId());
                holder.tvAssertViewStatus.setText(statusText);
                holder.tvAuditViewTimeValue.setText(currentDateTimeString);
                //holder.tvTypedDescription.setText(str);

                System.out.println("assetByFloorModelsDB--" + assetByFloorModelsDB.size());
                System.out.println("assetByFloorModelsDB--" + assetByFloorModelsDB.get(position).getTagId());
                System.out.println("assetByFloorModelsDB--" + assetByFloorModelsDB.get(position).getQrCode());
                AssetByFloorModel assetByFloorModel = new AssetByFloorModel();

                if (floorList.get(position).getTagId().equals(assetByFloorModelsDB.get(position).getTagId())) {
                    for (int i = 0; i < scanTagIDAndTimeModel.size(); i++) {
                        AssetByFloorModel assetByFloorModel1 = new AssetByFloorModel();
                        if (assetByFloorModelsDB.get(position).getTagId().equals(scanTagIDAndTimeModel.get(i).getQrCodeId())) {
                            holder.tvAssertViewBarQrValue.setText(scanTagIDAndTimeModel.get(i).getAssertTagID());
                            holder.tvAuditViewTimeValue.setText(scanTagIDAndTimeModel.get(i).getTime());
                            holder.tvAssertViewStatus.setText(R.string.Scanned);
                            holder.tvAssertViewStatus.setTextColor(mContext.getResources().getColor(R.color.alertGreen));
                            statusText = "Scanned";
                            assetByFloorModel1.setStatus("Scanned");
                            assetByFloorModel1.setQrCode(scanTagIDAndTimeModel.get(i).getAssertTagID());
                        } else {
                            System.out.println("unScanned");
                        }
                        Constants.ScannedTotal = scanTagIDAndTimeModel.size();
                    }
                } else {
                    holder.tvAssertViewStatus.setText(R.string.UnScanned);
                }

                holder.tvAssertViewStatus.setOnClickListener(v -> {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.alert_status_description);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
                    Button btnAlertCancel = dialog.findViewById(R.id.btnAlertCancel);
                    Button btnAlertSubmit = dialog.findViewById(R.id.btnAlertSubmit);
                    EditText edtDescription = dialog.findViewById(R.id.edtDescription);
                    TextView tvScannedStatusValue = dialog.findViewById(R.id.tvScannedStatusValue);

                    tvScannedStatusValue.setText(holder.tvAssertViewStatus.getText().toString());

                    btnAlertCancel.setOnClickListener(v1 -> dialog.dismiss());
                    btnAlertSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            str = edtDescription.getText().toString();
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("str", str);
                            editor.apply();
                            dialog.dismiss();
                            holder.tvTypedDescription.setText(str);

                            try {
                                realm = Realm.getDefaultInstance();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        /*int i = 0;
                                        while (i <= scanTagIDAndTimeModel.size()) {
                                            scanTagIDAndTimeModel.get(i).setDescription
                                                    (holder.tvTypedDescription.getText().toString());
                                            break;
                                            //i++;
                                        }*/
                                        for (int index3 = 0; index3 < scanTagIDAndTimeModel.size(); index3++) {
                                            scanTagIDAndTimeModel.get(index3).setDescription
                                                    (holder.tvTypedDescription.getText().toString());
                                        }

                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    edtDescription.setText(holder.tvTypedDescription.getText().toString());

                    WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                    wmlp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
                    wmlp.x = 1;   //x position
                    wmlp.y = 100;   //y position
                    dialog.show();
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return assetByFloorModelsDB.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public void callback(View view, String result) {

    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView tvAssertViewTagIdValue, tvAssertViewBarQrValue,
                tvAuditViewTimeValue, tvAssertViewStatus, tvTypedDescription;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvAssertViewTagIdValue = itemView.findViewById(R.id.tvAssertViewTagIdValue);
            tvAssertViewBarQrValue = itemView.findViewById(R.id.tvAssertViewBarQrValue);
            tvAuditViewTimeValue = itemView.findViewById(R.id.tvAuditViewTimeValue);
            tvAssertViewStatus = itemView.findViewById(R.id.tvAssertViewStatus);
            tvTypedDescription = itemView.findViewById(R.id.tvTypedDescription);
        }
    }
}
