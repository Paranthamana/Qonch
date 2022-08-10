package com.QonchAssets.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.QonchAssets.Activity.AssetBySectionActivity;
import com.QonchAssets.Activity.AssetsByFloorActivity;
import com.QonchAssets.ApiResponse.FloorMapResponse;
import com.QonchAssets.Common.CommonFunctions;
import com.QonchAssets.Common.CustomProgressDialog;
import com.zebra.qonchAssets.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ListOfAssetsLocationAdapter extends RecyclerView.Adapter<ListOfAssetsLocationAdapter.Holder> {

    private static final long DISPLAY_LENGTH = 2000;
    Context mContext;
    List<FloorMapResponse> floorMapResponse;
    byte[] buildImageResponse;
    String urlImage = "blob:http://13.233.196.242/d4b57150-c851-4a3f-80de-52d46a699411";

    public ListOfAssetsLocationAdapter(Context context, List<FloorMapResponse> floorMapResponse,
                                       byte[] buildImageResponse) {
        this.buildImageResponse = buildImageResponse;
        this.mContext = context;
        this.floorMapResponse = floorMapResponse;
    }

    @NonNull
    @Override
    public ListOfAssetsLocationAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View previousList = layoutInflater.inflate(R.layout.adapter_list_of_assets_location, parent,
                false);
        return new Holder(previousList);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfAssetsLocationAdapter.Holder holder,
                                 @SuppressLint("RecyclerView") int position) {

        holder.tvFloorNameValue.setText(floorMapResponse.get(position).getName());

        holder.tvViewAssets.setOnClickListener(v -> {
            if (!CustomProgressDialog.getInstance().isShowing()) {
                CustomProgressDialog.getInstance().show(mContext);
            }
            new Handler().postDelayed(() -> {
                Bundle bundle = new Bundle();
                bundle.putString("position", floorMapResponse.get(position).getId().toString());
                CommonFunctions.getInstance().newIntent(mContext, AssetsByFloorActivity.class,
                        bundle, false);
            }, DISPLAY_LENGTH);
        });

        holder.tvViewSection.setOnClickListener(v -> {
            if (!CustomProgressDialog.getInstance().isShowing()) {
                CustomProgressDialog.getInstance().show(mContext);
            }
            new Handler().postDelayed(() -> {
                Bundle bundle = new Bundle();
                bundle.putString("position", floorMapResponse.get(position).getId().toString());
                CommonFunctions.getInstance().newIntent(mContext, AssetBySectionActivity.class,
                        bundle, false);
            }, DISPLAY_LENGTH);
        });

        //holder.ivLocationAssetsImage.setImageBitmap(getBitmapFromBytes(buildImageResponse));

    }

    @Override
    public int getItemCount() {
        return floorMapResponse.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView tvFloorNameValue;
        TextView tvViewAssets;
        TextView tvViewSection;
        ImageView ivLocationAssetsImage;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvFloorNameValue = itemView.findViewById(R.id.tvFloorNameValue);
            tvViewAssets = itemView.findViewById(R.id.tvViewAssets);
            ivLocationAssetsImage = itemView.findViewById(R.id.ivLocationAssetsImage);
            tvViewSection = itemView.findViewById(R.id.tvViewSection);
        }
    }

    public static Bitmap getBitmapFromBytes(byte[] bytes) {
        if (bytes != null) {
            return BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
        }
        return null;
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            return stream.toByteArray();
        }
        return null;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static Bitmap convertBLOB2Bitmap(byte[] blob) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap tmp = BitmapFactory.decodeByteArray(blob, 0, blob.length, options);
        return tmp;
    }

    public static Bitmap convertStringToBitmap(String string) {
        byte[] byteArray1;
        byteArray1 = Base64.decode(string, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray1, 0,
                byteArray1.length);
        return bmp;
    }

    Bitmap Base64ToBitmap(String myImageData) {
        byte[] imageAsBytes = Base64.decode(myImageData.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static Bitmap getBitmap(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math
                .abs(options.outWidth - 100);
        if (options.outHeight * options.outWidth * 2 >= 16384) {
            double sampleSize = scaleByHeight
                    ? options.outHeight / 100
                    : options.outWidth / 100;
            options.inSampleSize =
                    (int) Math.pow(2d, Math.floor(
                            Math.log(sampleSize) / Math.log(2d)));
        }
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        Bitmap output = BitmapFactory.decodeFile(filePath, options);
        return output;
    }
}
