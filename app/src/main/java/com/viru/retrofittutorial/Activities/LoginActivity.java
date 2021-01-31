package com.viru.retrofittutorial.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.viru.retrofittutorial.ModelResponse.LoginResponse;
import com.viru.retrofittutorial.ModelResponse.RegisterResponse;
import com.viru.retrofittutorial.R;
import com.viru.retrofittutorial.RetrofitClient;
import com.viru.retrofittutorial.SharedPrefManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email, password;
    Button login;
    TextView registerlink;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // hide status bar
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   */

        email = findViewById(R.id.etemail);
        password = findViewById(R.id.etpassword);
        login = findViewById(R.id.btn_login);
        registerlink = findViewById(R.id.registerlink);

        registerlink.setOnClickListener(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        //login.setOnClickListener(this);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
               userLogin();
                break;
            case R.id.registerlink:
                switchOnRegister();
                break;
        }
    }

    private void userLogin() {
        //get the values of EditText i.e. values entered by the user
        String useremail = email.getText().toString().trim();
        String userpassword = password.getText().toString().trim();

        // validations
        if (useremail.isEmpty()){
            email.requestFocus();
            email.setError("Please enter your email");
            return;
        }
        // check for the correct pattern of the email
        if  (!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
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
        // Create object of Call
        Call<LoginResponse>call= RetrofitClient.getInstance().getApi().login(useremail, userpassword);

        //call enqueue method
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                if (response.isSuccessful()){
                    if (loginResponse.getError().equals("200") && loginResponse.getMessage().equalsIgnoreCase("login success")){
                        /*call saveUser() method of class Shared pref Manager and pass user
                         to save the user info when the login is */
                        sharedPrefManager.saveUser(loginResponse.getUser());
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }else if (loginResponse.getError().equals("200") && loginResponse.getMessage().equalsIgnoreCase("Wrong credentials")){
                   //     Toast.makeText(LoginActivity.this, loginResponse.getError(), Toast.LENGTH_LONG).show();
                        Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                    }else if (loginResponse.getError().equals("400") && loginResponse.getMessage().equalsIgnoreCase("user does not exist")){
                        Toast.makeText(LoginActivity.this, "user does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
              }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void switchOnRegister() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sharedPrefManager.isLoggedIn()){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            //Toast.makeText(LoginActivity.this, "Login failed ", Toast.LENGTH_SHORT).show();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //Toast.makeText(LoginActivity.this, "Login failed ", Toast.LENGTH_SHORT).show();
        }
    }
}