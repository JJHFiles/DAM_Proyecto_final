package com.example.dam_proyecto_final.home.homeui.group_invoice.group_invoice_tabui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.dam_proyecto_final.R;
import com.scanlibrary.ScanConstants;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InvoiceOCRAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_ocradd);

        ImageView imgvScan = findViewById(R.id.imgv_aioa_scan);

        ActivityResultLauncher<Intent> intentScanForResult;

        intentScanForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Uri uri = result.getData().getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                            getApplicationContext().getContentResolver().delete(uri, null, null);
                            imgvScan.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}