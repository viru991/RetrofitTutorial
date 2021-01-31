package com.viru.retrofittutorial.LeftNavFragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.viru.retrofittutorial.Activities.HomeActivity;
import com.viru.retrofittutorial.Activities.LeftNavActivity;
import com.viru.retrofittutorial.MyRetrofitClient;
import com.viru.retrofittutorial.R;
import com.viru.retrofittutorial.ResponsePOJO;
import com.viru.retrofittutorial.TestingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    EditText et_meetingparticulars;
    Spinner spinner_uic, spinner_venue;
    TextView tv_time, tv_date;
    Button btn_save,btn_agenda,btn_note;
    ArrayList<String> venuelist, oiclist;
    Calendar myCalendar;
    String oicstring, venuestring;
    private int REQ_PDF = 21;
    private int REQ_PDF1 = 22;
    private String encodedPDF, encodedPDF1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add, container, false);
        // Method to enable runtime permission.
        RequestRunTimePermission();
        btn_agenda = v.findViewById(R.id.btn_agenda);
        tv_date = v.findViewById(R.id.tv_date);
        btn_note = v.findViewById(R.id.btn_note);
        tv_time = v.findViewById(R.id.tv_time);
        spinner_uic = v.findViewById(R.id.spinner_uic);
        spinner_venue = v.findViewById(R.id.spinner_venue);
        btn_save = v.findViewById(R.id.btn_save);
        et_meetingparticulars = v.findViewById(R.id.et_meetingparticular);
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
        venuelist = new ArrayList<>();
        oiclist = new ArrayList<>();
        /*load the venue and uic from server side*/
        loaduic();
        loadoic();
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        spinner_venue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), venuelist.get(i), Toast.LENGTH_SHORT).show();
                venuestring = venuelist.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_uic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), oiclist.get(i), Toast.LENGTH_SHORT).show();
                oicstring = oiclist.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_agenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("application/pdf");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, REQ_PDF);
            }
        });

        btn_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("application/pdf");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, REQ_PDF1);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDocument();
            }
        });

        return v;
    }

    private void RequestRunTimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Toast.makeText(getActivity(), "READ_EXTERNAL_STORAGE permission Access Dialog",
                    Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }


    private void uploadDocument() {

        Call<ResponsePOJO> call = MyRetrofitClient.getInstance().getAPI().uploadDocument(tv_date.getText().toString(),
                encodedPDF,encodedPDF1, et_meetingparticulars.getText().toString(), tv_time.getText().toString(),venuestring, oicstring);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, retrofit2.Response<ResponsePOJO> response) {
                Toast.makeText(getActivity(), response.body().getRemarks(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tv_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void loadoic() {
        StringRequest sr = new StringRequest(1, "http://searchkero.com/UserApi/fetch_oic.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("oics");
                    for (int i = 0 ; i < ja.length(); i++)
                    {
                        JSONObject job = ja.getJSONObject(i);
                        oiclist.add(job.getString("oic_name"));
                    }
                    ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,oiclist);
                    spinner_uic.setAdapter(aa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(sr);

    }

    private void loaduic() {
        StringRequest sr = new StringRequest(1, "http://searchkero.com/UserApi/fetch_venue.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("venues");
                    for (int i = 0 ; i < ja.length(); i++)
                    {
                        JSONObject job = ja.getJSONObject(i);
                        venuelist.add(job.getString("venue_name"));
                    }
                    ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,venuelist);
                    spinner_venue.setAdapter(aa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(sr);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((HomeActivity)getActivity()).setactionbartitle("Add Meeting");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_PDF && resultCode == RESULT_OK && data != null) {

            Uri path = data.getData();


            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(path);
                byte[] pdfInBytes = new byte[inputStream.available()];
                inputStream.read(pdfInBytes);
                encodedPDF = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);

                Toast.makeText(getActivity(), "Document Selected", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (requestCode == REQ_PDF1 && resultCode == RESULT_OK && data != null) {

            Uri path1 = data.getData();


            try {
                InputStream inputStream1 = getActivity().getContentResolver().openInputStream(path1);
                byte[] pdfInBytes1 = new byte[inputStream1.available()];
                inputStream1.read(pdfInBytes1);
                encodedPDF1 = Base64.encodeToString(pdfInBytes1, Base64.DEFAULT);

                Toast.makeText(getActivity(), "Document Selected", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}