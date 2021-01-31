package com.viru.retrofittutorial.NavFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.viru.retrofittutorial.ModelResponse.LoginResponse;
import com.viru.retrofittutorial.ModelResponse.UpdatePassResponse;
import com.viru.retrofittutorial.R;
import com.viru.retrofittutorial.RetrofitClient;
import com.viru.retrofittutorial.SharedPrefManager;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    EditText etusername, etemail, etcurrentpassword, etnewpassword;
    Button btn_updateUserAccount, btn_UpdatePassword;
    
    int userid;
    String userEmailId;

    SharedPrefManager sharedPrefManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //for update Account
        etusername = view.findViewById(R.id.etusername);
        etemail = view.findViewById(R.id. etemail);
        btn_updateUserAccount =view.findViewById(R.id.btn_UpdateAccount);
        
        //for update password
        etcurrentpassword=view.findViewById(R.id.etcurrentpassword);
        etnewpassword = view.findViewById(R.id. etnewpassword);
        btn_UpdatePassword = view.findViewById(R.id.btn_UpdatePassword);
        
        //to get the id from shared pref. for updating account
        sharedPrefManager = new SharedPrefManager(getActivity());
        userid= sharedPrefManager.getUser().getId();
        // to get the email from shared pref. for updating the password
        userEmailId = sharedPrefManager.getUser().getEmail();

        btn_updateUserAccount.setOnClickListener(this);
        btn_UpdatePassword.setOnClickListener(this);
        
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_UpdateAccount:
                updateUserAccount();
               break;
            case R.id.btn_UpdatePassword:
                updatePassword();
                break;
        }
    }

// method to update the password
    private void updatePassword() {

        String currentpassword = etcurrentpassword.getText().toString();
        String newpassowrd = etnewpassword.getText().toString();
        if (currentpassword.isEmpty()){
            etcurrentpassword.setError("Plese enter current password");
            etcurrentpassword.requestFocus();
            return;
        }
        if (currentpassword.length()<8 ){
            etcurrentpassword.setError("Wrong current password");
            etcurrentpassword.requestFocus();
            return;
        }
        if (newpassowrd.isEmpty()){
            etnewpassword.setError("Please enter new password email");
            etnewpassword.requestFocus();
            return;
        }
        if (newpassowrd.length() < 8 ){
            etnewpassword.setError("Enter 8 digit new password");
            etnewpassword.requestFocus();
            return;
        }
        // implement retrofit for update password
        Call<UpdatePassResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .updatepassword(userEmailId, currentpassword, newpassowrd);
        call.enqueue(new Callback<UpdatePassResponse>() {
            @Override
            public void onResponse(Call<UpdatePassResponse> call, Response<UpdatePassResponse> response) {
                 UpdatePassResponse passResponse = response.body();
                 if (response.isSuccessful()){
                     if (passResponse.getError().equals("200")){
                         Toast.makeText(getActivity(), passResponse.getMessage(), Toast.LENGTH_SHORT).show();
                         // clear the form
                         etcurrentpassword.setText("");
                         etnewpassword.setText("");
                     }
                 }else{
                     Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                 }
            }

            @Override
            public void onFailure(Call<UpdatePassResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

// method to update the user account
    private void updateUserAccount() {
        String username = etusername.getText().toString();
        String email = etemail.getText().toString();

        if (username.isEmpty()){
            etusername.setError("Plese enter user name");
            etusername.requestFocus();
            return;
        }
        if (email.isEmpty()){
            etemail.setError("Please enter email");
            etemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etemail.requestFocus();
            etemail.setError("please enter correct email");
            return;
        }
        //implement retroft2 for update user account

        Call<LoginResponse> call = RetrofitClient
                       .getInstance()
                       .getApi()
                       .updateuseraccount(userid, username, email);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
            LoginResponse updateresponse= response.body();
            if (response.isSuccessful()){
                if (updateresponse.getError().equals("200")){
                    sharedPrefManager.saveUser(updateresponse.getUser());
                    Toast.makeText(getActivity(), updateresponse.getMessage(), Toast.LENGTH_SHORT).show();
                   // Clear the form
                    etusername.setText("");
                    etemail.setText("");
                }
                else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}