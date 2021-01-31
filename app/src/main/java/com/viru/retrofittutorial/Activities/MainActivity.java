package com.viru.retrofittutorial.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.viru.retrofittutorial.ModelResponse.RegisterResponse;
import com.viru.retrofittutorial.R;
import com.viru.retrofittutorial.RetrofitClient;

import java.sql.Struct;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //declaring the xml objects
    TextView loginlink;
    EditText name, email, password;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // type casting the all the XML widgets used in the acivity_main.xml fle

        name = findViewById(R.id.etname);
        email = findViewById(R.id.etemail);
        password = findViewById(R.id.etpassword);
        register= findViewById(R.id.btn_register);
        loginlink = findViewById(R.id.loginlink);

        loginlink.setOnClickListener(this);
        register.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                registerUser();
                //Toast.makeText(this, "register", Toast.LENGTH_SHORT).show();
                 break;
            case R.id.loginlink:
                 switchOnLogin();
                 break;
        }
    }
    private void registerUser() {
        //get the values EditText i.e. values entered by the user
         String username = name.getText().toString();
         String useremail = email.getText().toString();
         String userpassword = password.getText().toString();

         // validations
        if (username.isEmpty()){
            name.requestFocus();
            name.setError("Please enter your username");
            return;
        }
        if (useremail.isEmpty()){
            email.requestFocus();
            email.setError("Please enter your email");
            return;
        }
        // check for the correct pattern of the email
        if (!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
            email.requestFocus();
            email.setError("Please enter correct email");
            return;
        }
        if (userpassword.isEmpty()){
            password.requestFocus();
            password.setError("Please enter the password");
            return;
        }
        // check for the length of the password
        if (userpassword.length()<8){
            password.requestFocus();
            password.setError("Password must be of atleast 8 characters");
            return;
        }
        // Retrofit request to the server
        Call<RegisterResponse> call = RetrofitClient.getInstance().getApi().register(username,useremail,userpassword);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(retrofit2.Call<RegisterResponse> call, Response<RegisterResponse> response) {
                // if there is a response from the server. it may be correct or incorrect response then
                //we check for the correct response.
                // response will of RegisterResponse type, so take in RegisterResponse type variable

                RegisterResponse registerResponse = response.body();
                if (response.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    // note that register response has complete response as received in POSTMAN i.e.
                    // error and message
                    Toast.makeText(MainActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, registerResponse.getError(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<RegisterResponse> call, Throwable t) {
               // if there is no response from the server
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void switchOnLogin() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);

    }
}