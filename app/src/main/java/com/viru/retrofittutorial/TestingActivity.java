package com.viru.retrofittutorial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestingActivity extends AppCompatActivity {

    private Button btnSelect,btnSelect1, btnUpload;
    private TextView textView,textView1,tv_date;
    private int REQ_PDF = 21;
    private int REQ_PDF1 = 22;
    private String encodedPDF, encodedPDF1;

    // Pdf upload request code.
    public int PDF_REQ_CODE = 1;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        // Method to enable runtime permission.
        RequestRunTimePermission();
        btnSelect1 = findViewById(R.id.btnSelect1);
        textView1 = findViewById(R.id.textView1);
        tv_date = findViewById(R.id.tv_dates);
        textView = findViewById(R.id.textView);
        btnSelect = findViewById(R.id.btnSelect);
        btnUpload = findViewById(R.id.btnUpload);

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TestingActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("application/pdf");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, REQ_PDF);

            }
        });

        btnSelect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("application/pdf");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, REQ_PDF1);

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDocument();
            }
        });


    }

    private void uploadDocument() {

        Call<ResponsePOJO> call = MyRetrofitClient.getInstance().getAPI().uploadDocument(tv_date.getText().toString(),
                encodedPDF,encodedPDF1,encodedPDF, encodedPDF, encodedPDF, encodedPDF);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                Toast.makeText(TestingActivity.this, response.body().getRemarks(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(TestingActivity.this, "Network Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }


    // Requesting run time permission method starts from here.
    public void RequestRunTimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(TestingActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Toast.makeText(TestingActivity.this, "READ_EXTERNAL_STORAGE permission Access Dialog",
                    Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(TestingActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_PDF && resultCode == RESULT_OK && data != null) {

            Uri path = data.getData();


            try {
                InputStream inputStream = TestingActivity.this.getContentResolver().openInputStream(path);
                byte[] pdfInBytes = new byte[inputStream.available()];
                inputStream.read(pdfInBytes);
                encodedPDF = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);

                textView.setText("Document Selected");
                btnSelect.setText("Select Document");

                Toast.makeText(this, "Document Selected", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (requestCode == REQ_PDF1 && resultCode == RESULT_OK && data != null) {

            Uri path1 = data.getData();


            try {
                InputStream inputStream1 = TestingActivity.this.getContentResolver().openInputStream(path1);
                byte[] pdfInBytes1 = new byte[inputStream1.available()];
                inputStream1.read(pdfInBytes1);
                encodedPDF1 = Base64.encodeToString(pdfInBytes1, Base64.DEFAULT);

                textView1.setText("Document Selected");
                btnSelect1.setText("Select Document");

                Toast.makeText(this, "Document Selected", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tv_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Toast.makeText(TestingActivity.this, "Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


}