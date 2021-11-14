package com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.dam_proyecto_final.R;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InvoiceOCRAddActivity extends AppCompatActivity {

    ImageView imgvScan;
    int REQUEST_CODE = 99;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123; // permisions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_ocradd);

        imgvScan = findViewById(R.id.imgv_aioa_scan);

        ActivityResultLauncher<Intent> intentScanForResult;

      /*  intentScanForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
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
                });*/


        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Se necesitan permisos de camara para poder hacer fotográfías de la factura")
                        .setTitle("Permisos denegados")
                        .setNeutralButton("Lo he entendido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                                onBackPressed();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Se necesitan de almacenamiento para poder almacenar la factura")
                        .setTitle("Permisos denegados")
                        .setNeutralButton("Lo he entendido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                                onBackPressed();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // request permission
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    onBackPressed();
                } else {
                    // Tenemos permisos
                    int preference = ScanConstants.OPEN_CAMERA;
                    Intent intent = new Intent(this, ScanActivity.class);
//                    intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }

        } else {
            // Tenemos permisos
            int preference = ScanConstants.OPEN_CAMERA;
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
            startActivityForResult(intent, REQUEST_CODE);
        }




//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                imgvScan.setImageBitmap(bitmap);


                // Load JPG file into bitmap
//                File f = new File(getFilesDir().getPath() + "prueba.pdf");
//                Bitmap bitmap = BitmapFactory.decodeFile(f.absolutePath)

                // Create a PdfDocument with a page of the same size as the image
                PdfDocument document = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
                PdfDocument.Page page = document.startPage(pageInfo);

                // Draw the bitmap onto the page
                Canvas canvas = page.getCanvas();
                canvas.drawBitmap(bitmap, 0f, 0f, null);
                document.finishPage(page);

                // Write the PDF file to a file
                String directoryPath = getFilesDir().getPath() + "/prueba.pdf";
                document.writeTo(new FileOutputStream(directoryPath));
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}