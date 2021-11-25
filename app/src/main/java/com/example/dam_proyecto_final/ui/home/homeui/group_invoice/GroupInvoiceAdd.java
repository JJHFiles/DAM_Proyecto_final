package com.example.dam_proyecto_final.ui.home.homeui.group_invoice;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.dam_proyecto_final.R;
import com.example.dam_proyecto_final.model.GroupModel;
import com.example.dam_proyecto_final.model.InvoiceModel;
import com.example.dam_proyecto_final.web_api.WebApiRequest;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GroupInvoiceAdd extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    // Request code for selecting a PDF document.
    private static final int PICK_PDF_FILE = 2;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 400;

    private TextInputEditText
            tietInvoiceNum,
            tietProvider,
            tietDate,
            tietStartPeriod,
            tietEndPeriod,
            tietConsumption,
            tietAmount;
    TextView tvFileName;
    String pdfFile;
    ImageButton ibFileIcon;
    LinearLayout llFilePicker;

    private String typeSelection;
    private AutoCompleteTextView actvInvoiceType;
    private Button btNew;

    private GroupModel groupModel;
    private String userEmail;
    private String userPass;
    private WebApiRequest webApiRequest;

    private final Calendar dateEmition = Calendar.getInstance();
    private final Calendar dateStart = Calendar.getInstance();
    private final Calendar dateEnd = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invoice_add);
        setTitle("Insertar nueva factura");

        webApiRequest = new WebApiRequest(getApplicationContext());

        tietInvoiceNum = findViewById(R.id.tiet_GIFStarPeriod);
        tietInvoiceNum.addTextChangedListener(this);
        tietProvider = findViewById(R.id.tiet_provider);
        tietProvider.addTextChangedListener(this);
        tietDate = findViewById(R.id.tiet_date);
        tietDate.addTextChangedListener(this);
        tietDate.setOnClickListener(this);
        tietStartPeriod = findViewById(R.id.tiet_startPeriod);
        tietStartPeriod.addTextChangedListener(this);
        tietStartPeriod.setOnClickListener(this);
        tietEndPeriod = findViewById(R.id.tiet_endPeriod);
        tietEndPeriod.addTextChangedListener(this);
        tietEndPeriod.setOnClickListener(this);
        tietConsumption = findViewById(R.id.tiet_Consumption);
        tietConsumption.addTextChangedListener(this);
        tietAmount = findViewById(R.id.tiet_Amount);
        tietAmount.addTextChangedListener(this);
        actvInvoiceType = findViewById(R.id.actv_invoiceType);
        actvInvoiceType.addTextChangedListener(this);
        btNew = findViewById(R.id.bt_New);
        btNew.setOnClickListener(this);
        llFilePicker = findViewById(R.id.ll_invoiceadd_filepicker);
        llFilePicker.setOnClickListener(this);
        tvFileName = findViewById(R.id.tv_invoiceadd_filename);
        ibFileIcon = findViewById(R.id.ib_invoiceadd_fileicon);


        Bundle param = this.getIntent().getExtras();
        if (param != null) {
            groupModel = (GroupModel) param.getSerializable("groupModel");
            userEmail = param.getString("userEmail", "vacio");
            userPass = param.getString("userPass", "vacio");
            Uri uri = (Uri) param.getParcelable("uri");
            if (uri != null) {
                pdfFile = getStringPdf(uri);
                tvFileName.setText(R.string.groupinvoiceadd_defaultfilename);
                ibFileIcon.setVisibility(View.INVISIBLE);
                llFilePicker.setClickable(false);
            }
        }

        checkStoragePermissions();

        loadInvoiceType();

    }

    public void loadInvoiceType() {
        //Asignamos lista a DropDowns
        String[] type = {"Electricidad", "Agua", "Gas", "Telefonía"};
        ArrayAdapter typeAdapter = new ArrayAdapter(this, R.layout.activity_group_add_dropdown_item, type);
        actvInvoiceType.setAdapter(typeAdapter);
        actvInvoiceType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                typeSelection = parent.getItemAtPosition(position).toString();
                btNew.setEnabled(checkFields());
            }
        });
    }

    private void addCalendar(Calendar calendar, TextInputEditText tiet) {
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.select_date)
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                calendar.setTimeInMillis((Long) selection);

                DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
                String formatted = format.format(calendar.getTime());
                tiet.setText(formatted);
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), String.valueOf(R.string.select_date));
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_New) {
            if (dateStart.before(dateEnd)) {
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                InvoiceModel invoiceModel = new InvoiceModel(
                        tietInvoiceNum.getText().toString(),
                        tietProvider.getText().toString(),
                        typeSelection,
                        formater.format(dateEmition.getTime()),
                        formater.format(dateStart.getTime()),
                        formater.format(dateEnd.getTime()),
                        Double.parseDouble(tietConsumption.getText().toString()),
                        Double.parseDouble(tietAmount.getText().toString()),
                        "0",
                        groupModel.getId()
                );
                if (pdfFile != null) {
                    invoiceModel.setFile(pdfFile);
                }
                insertInvoice(invoiceModel);
                btNew.setClickable(false);
            } else {
                tietStartPeriod.setText("");
                tietEndPeriod.setText("");
                Toast.makeText(this, getResources().getText(R.string.warning_badperioddate),
                        Toast.LENGTH_LONG).show();
                btNew.setEnabled(false);
            }
        } else if (v.getId() == R.id.tiet_date) {
            addCalendar(dateEmition, (TextInputEditText) v);
        } else if (v.getId() == R.id.tiet_startPeriod) {
            addCalendar(dateStart, (TextInputEditText) v);
        } else if (v.getId() == R.id.tiet_endPeriod) {
            addCalendar(dateEnd, (TextInputEditText) v);
        } else if (v.getId() == R.id.ll_invoiceadd_filepicker) {
            openFile();
        }
    }

    private boolean checkFields() {
        if (tietInvoiceNum.getText().toString().equals("")) {
            return false;
        }
        if (tietProvider.getText().toString().equals("")) {
            return false;
        }
        if (typeSelection == null || typeSelection.equals("")) {
            return false;
        }
        if (tietDate.getText().toString().equals("")) {
            return false;
        }
        if (tietStartPeriod.getText().toString().equals("")) {
            return false;
        }
        if (tietEndPeriod.getText().toString().equals("")) {
            return false;
        }
        if (tietConsumption.getText().toString().equals("")) {
            return false;
        }
        if (tietAmount.getText().toString().equals("")) {
            return false;
        }

        return true;
    }


    private void openFile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");

        startActivityForResult(intent, PICK_PDF_FILE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_FILE && resultCode == Activity.RESULT_OK) {
            Uri pdfPath = data.getData();
            pdfFile = getStringPdf(pdfPath);
            if (pdfPath.getScheme().equals("content")) {
                Cursor cursor = getContentResolver().query(pdfPath, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        tvFileName.setText(cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)));
                    }
                } finally {
                    cursor.close();
                }
                ibFileIcon.setVisibility(View.INVISIBLE);
                llFilePicker.setClickable(false);
            }
        } else {
            Toast.makeText(this, "Error al seleccioanr el fichero", Toast.LENGTH_LONG).show();
        }
    }

    public String getStringPdf(Uri filepath) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream = getContentResolver().openInputStream(filepath);

            byte[] buffer = new byte[1024];
            byteArrayOutputStream = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] pdfByteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
    }


    public void insertInvoice(InvoiceModel im) {

        webApiRequest.insertInvoice(userEmail, userPass, im, new WebApiRequest.WebApiRequestJsonObjectListener() {
            @Override
            public void onSuccess(int id, String message) {
                if (id > 0) {
                    Log.d("DEBUGME", "insertInvoice onSucess: " + id + " " + message);

                    Toast.makeText(getApplicationContext(), "Factura insertada con éxito: " + id, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), GroupInvoiceTab.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("groupModel", groupModel);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userPass", userPass);
                    startActivity(intent);
                } else if (id < 0) {
                    Log.d("DEBUGME", "insertInvoice onSucess: " + id + " " + message);
                    Toast.makeText(getApplicationContext(), "Error al insertar factura. Codigo de error: " + id, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(int id, String message) {
                Log.d("DEBUGME", "insertInvoice onerror: " + id + " " + message);
                Toast.makeText(getApplicationContext(), "Error al insertar factura. Codigo de error: " + id, Toast.LENGTH_LONG).show();
                btNew.setClickable(false);
            }
        });
    }


    // Métodos para habilitar el botón con el form completo
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (checkFields()) {
            btNew.setEnabled(true);
        } else {
            btNew.setEnabled(false);
        }
    }


    private void checkStoragePermissions() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Se necesitan de almacenamiento para poder almacenar la factura")
                        .setTitle("Permisos denegados")
                        .setNeutralButton("Lo he entendido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                                onBackPressed();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // request permission
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    onBackPressed();
                }
            }

        }
    }
}