package com.example.dam_proyecto_final.ui.home.homeui.group_invoice.group_invoice_tabui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.pdf.PdfDocument;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.ui.home.homeui.group_invoice.GroupInvoiceAdd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InvoiceOCRAddActivity extends AppCompatActivity {

    ImageView imgvScan;
    int REQUEST_CODE = 99;
    int TAKE_PHOTO = 546;

    private final int REQUEST_CODE_ASK_PERMISSIONS = 123; // permisions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_ocradd);

        imgvScan = findViewById(R.id.imgv_aioa_scan);


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
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    onBackPressed();
                } else {
                    takePicture();
                }
            }
        } else {
            takePicture();
/*           Intent ScanLibrary
            // Tenemos permisos
            int preference = ScanConstants.OPEN_CAMERA;
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
            startActivityForResult(intent, REQUEST_CODE);*/
        }
    }

    private void takePicture() {
    /*                  Intent ScanLibrary
                        // Tenemos permisos
                        int preference = ScanConstants.OPEN_CAMERA;
                        Intent intent = new Intent(this, ScanActivity.class);
                        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                        startActivityForResult(intent, REQUEST_CODE);*/

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            String photoFile;
            File image = new File(getExternalFilesDir(null), "tempimage");
            // Continue only if the File was successfully created
            Uri photoURI = FileProvider.getUriForFile(this,
                    this.getApplicationContext().getPackageName() +
                            ".provider", image);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, TAKE_PHOTO);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            File imagereduce = new File(getExternalFilesDir(null), "imagereduce.png");
            File image = new File(getExternalFilesDir(null), "tempimage");
            Bitmap bitmap = null;
            try {

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = false;
//                bmOptions.inSampleSize = scaleFactor;
//                bmOptions.inPurgeable = true;
                bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
//                getContentResolver().delete(uri, null, null);
                imgvScan.setImageBitmap(bitmap);

                Bitmap reduce = getResizedBitmap(bitmap, 600);
                FileOutputStream out = new FileOutputStream(imagereduce);
                reduce.compress(Bitmap.CompressFormat.PNG, 100, out);
                Bitmap bMapRotate = null;
                if (reduce.getWidth() > reduce.getHeight()) {

                    Matrix mat = new Matrix();
                    mat.postRotate(90);
                    bMapRotate = Bitmap.createBitmap(reduce, 0, 0, reduce.getWidth(), reduce.getHeight(), mat, true);
                } else {
                    bMapRotate = reduce;
                }


                // Create a PdfDocument with a page of the same size as the image
                PdfDocument document = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bMapRotate.getWidth(), bMapRotate.getHeight(), 1).create();
                PdfDocument.Page page = document.startPage(pageInfo);

                // Draw the bitmap onto the page
                Canvas canvas = page.getCanvas();
                canvas.drawBitmap(bMapRotate, 0f, 0f, null);
                document.finishPage(page);

//                 Write the PDF file to a file/
                File fileout = new File(getExternalFilesDir(null), "tempupload.pdf");
                String directoryPath = fileout.getPath();

                document.writeTo(new FileOutputStream(directoryPath));
                document.close();


                Bundle param = this.getIntent().getExtras();
                if (param != null) {
                    GroupModel groupModel = (GroupModel) param.getSerializable("groupModel");
                    String userEmail = param.getString("userEmail", "vacio");
                    String userPass = param.getString("userPass", "vacio");

                    Uri uri = Uri.fromFile(fileout);
                    Intent intent = new Intent(this, GroupInvoiceAdd.class);
                    intent.putExtra("groupModel", groupModel);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userPass", userPass);
                    intent.putExtra("uri", uri);
                    finish();
                    startActivity(intent);
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(this, getResources().getText(R.string.error_saving_image), Toast.LENGTH_LONG).show();
                onBackPressed();
            } catch (IOException e) {
                Toast.makeText(this, getResources().getText(R.string.error_saving_image), Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }


//        if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
//            File fileout = new File(getExternalFilesDir(null), "tempupload.pdf");
//            Uri uri = Uri.fromFile(fileout);
//            Bitmap bitmap = null;
//            try {
//                bitmap = (Bitmap) data.getExtras().get("data");
////                getContentResolver().delete(uri, null, null);
//                imgvScan.setImageBitmap(bitmap);
//
//                // Create a PdfDocument with a page of the same size as the image
//                PdfDocument document = new PdfDocument();
//                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
//                PdfDocument.Page page = document.startPage(pageInfo);
//
//                // Draw the bitmap onto the page
//                Canvas canvas = page.getCanvas();
//                canvas.drawBitmap(bitmap, 0f, 0f, null);
//                document.finishPage(page);
//
//                // Write the PDF file to a file
////                fileout = new File(getExternalFilesDir(null), "tempupload");
//                String directoryPath = fileout.getPath();
//
//                document.writeTo(new FileOutputStream(directoryPath));
//                document.close();
//
//
//                Bundle param = this.getIntent().getExtras();
//                if (param != null) {
//                    GroupModel groupModel = (GroupModel) param.getSerializable("groupModel");
//                    String userEmail = param.getString("userEmail", "vacio");
//                    String userPass = param.getString("userPass", "vacio");
//
//                    uri = Uri.fromFile(fileout);
//                    Intent intent = new Intent(this, GroupInvoiceAdd.class);
//                    intent.putExtra("groupModel", groupModel);
//                    intent.putExtra("userEmail", userEmail);
//                    intent.putExtra("userPass", userPass);
//                    intent.putExtra("uri", uri);
//                    finish();
//                    startActivity(intent);
//                }
//            } catch (FileNotFoundException e) {
//                Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_LONG).show();
//                onBackPressed();
//            } catch (IOException e) {
//                Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_LONG).show();
//                onBackPressed();
//            }
//        }

/*       Request code for ScanLibrary
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                imgvScan.setImageBitmap(bitmap);

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
        }*/
    }

    //    private void getPic(File file) {
//        // Get the dimensions of the View
//        int targetW = imageView.getWidth();
//        int targetH = imageView.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        return bitmap;
//    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


}