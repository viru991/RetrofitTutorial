package com.viru.retrofittutorial.ModelResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchUserResponse {
    //see what responce we are getting

    /*
    {
    "users": [
        {
            "id": "2",
            "username": "Virendra",
            "email": "viru991@gmail.com"
        },
        {
            "id": "16",
            "username": "Rachit Pant",
            "email": "rachit@gmail.com"
        },
    ],
    "error": "200"
}   */

    @SerializedName("users")
    List<User> userList;
    String error;

    //Create constructor using Alt +Insert
    public FetchUserResponse(List<User> userList, String error) {
        this.userList = userList;
        this.error = error;
    }

    //Create getters and setters using Alt +Insert
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
