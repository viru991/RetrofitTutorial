package com.viru.retrofittutorial;

import android.content.Context;
import android.content.SharedPreferences;

import com.viru.retrofittutorial.ModelResponse.User;

public class SharedPrefManager {
    //in this class we will define methods for saving, fetching and deleting the
    // user details
    private static final String SHARED_PREF_NAME="thecodingshef";
    private SharedPreferences sp;
    Context context;
    private SharedPreferences.Editor editor;

    // constructor
    public SharedPrefManager(Context context) {
        this.context = context;
    }
    //method to save all the user data used in login the app into share preferences
    public void saveUser(User user){
        sp=context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
        editor.putInt("id", user.getId());
        editor.putString("username",user.getUsername());
        editor.putString("email",user.getEmail());
        editor.putBoolean("logged",true);
        editor.apply();
    }
    // method to get the value of the Logged variable from shared preferences
    public boolean isLoggedIn(){
         sp=context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

         /* get the value of the sharedpref variable Logged and see whether it is true
            if it is true then user is Logged in else user is not logged in.
            so if Logged variable contains any value then it will be true elase
            it will return default value false  */
         return sp.getBoolean("logged", false);
     }

     public User getUser(){
        // get user details from the shared preferences
         sp=context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
         return new User (sp.getInt("id", -1), sp.getString("username", null),
         sp.getString("email", null));
     }

     // method to remove all user data from the shared preferences

     public void logout(){
         sp=context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
         editor=sp.edit();
         editor.clear();
         editor.apply();
    }
}
