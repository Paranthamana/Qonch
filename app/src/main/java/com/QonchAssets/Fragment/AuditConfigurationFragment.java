package com.QonchAssets.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.QonchAssets.Common.Constants;
import com.QonchAssets.Common.SessionManager;
import com.QonchAssets.event.AuditConfigurationEvent;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.zebra.qonchAssets.R;
import com.zebra.qonchAssets.databinding.FragmentAuditConfigurationBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class AuditConfigurationFragment extends Fragment {

    FragmentAuditConfigurationBinding binding;

    public AuditConfigurationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAuditConfigurationBinding.inflate(inflater);
        View view = binding.getRoot();

        EventBus.getDefault().register(this);

        RadioButton rbBarcodeScan = view.findViewById(R.id.rbBarcodeScan);
        RadioButton rbQRCodeScan = view.findViewById(R.id.rbQRCodeScan);
        RadioButton rbRFIDScan = view.findViewById(R.id.rbRFIDScan);
        RadioGroup rgScanning = view.findViewById(R.id.rgScanning);
        /*binding.rbBarcodeScan.setChecked(true);
        binding.rbBarcodeScan.isChecked();*/
        binding.rgScanning.check(rbBarcodeScan.getId());

        try {
            rbBarcodeScan.setChecked(SessionManager.getInstance().Update("BarCodeChecked"));
            rbQRCodeScan.setChecked(SessionManager.getInstance().Update("QRCode"));
            rbRFIDScan.setChecked(SessionManager.getInstance().Update("RFIDChecked"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        rbBarcodeScan.setOnCheckedChangeListener((buttonView, BarCodeChecked) ->
                SessionManager.getInstance().SavedIntoSession("BarCodeChecked", BarCodeChecked));

        rbQRCodeScan.setOnCheckedChangeListener((compoundButton, QRCodeChecked) ->
                SessionManager.getInstance().SavedIntoSession("QRCode", QRCodeChecked));
        rbRFIDScan.setOnCheckedChangeListener((compoundButton, RFIDChecked) ->
                SessionManager.getInstance().SavedIntoSession("RFIDChecked", RFIDChecked));

        rgScanning.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbBarcodeScan) {
                FancyToast.makeText(getActivity(), Constants.BarCodeScanningSelected,
                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                rbBarcodeScan.setChecked(true);
            } else if (checkedId == R.id.rbQRCodeScan) {
                FancyToast.makeText(getActivity(), Constants.ORCodeScanningSelected,
                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
            } else if (checkedId == R.id.rbRFIDScan) {
                FancyToast.makeText(getActivity(), Constants.RFIDScanningSelected,
                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
            }
        });

        return view;
    }

    @Subscribe
    public void onEvent(AuditConfigurationEvent event) {
        System.out.println("e" + event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}