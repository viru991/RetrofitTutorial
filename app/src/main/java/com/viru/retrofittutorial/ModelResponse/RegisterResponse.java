package com.viru.retrofittutorial.ModelResponse;

public class RegisterResponse {
    //below variable names must be same as received in response
    // else we have to use @SerializedName("message")
    // String msg;
    String error;
    String message;

    public RegisterResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
