package com.viru.retrofittutorial.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.viru.retrofittutorial.LeftNavFragments.AddFragment;
import com.viru.retrofittutorial.LeftNavFragments.DeleteFragment;
import com.viru.retrofittutorial.LeftNavFragments.HomeFragment;
import com.viru.retrofittutorial.LeftNavFragments.UpdateDateFragment;
import com.viru.retrofittutorial.LeftNavFragments.UpdateTimeFragment;
import com.viru.retrofittutorial.LeftNavFragments.UpdateVenueFragment;
import com.viru.retrofittutorial.ModelResponse.DeleteResponse;
import com.viru.retrofittutorial.NavFragments.DashboardFragment;
import com.viru.retrofittutorial.NavFragments.ProfileFragment;
import com.viru.retrofittutorial.NavFragments.UsersFragment;
import com.viru.retrofittutorial.R;
import com.viru.retrofittutorial.RetrofitClient;
import com.viru.retrofittutorial.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    SharedPrefManager sharedPrefManager;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         bottomNavigationView = findViewById(R.id.bottomnav);
         bottomNavigationView.setOnNavigationItemSelectedListener(this);
         loadFragment(new DashboardFragment());

         sharedPrefManager = new SharedPrefManager(this);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav=(NavigationView)findViewById(R.id.navmenu);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
        nav.setCheckedItem(R.id.menu_home);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            Fragment fragment;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.menu_home :
                        fragment =new HomeFragment();
                        break;
                    case R.id.menu_add_meeting :
                        fragment =new AddFragment();
                        break;
                    case R.id.menu_Update_meeting_date:
                        fragment =new UpdateDateFragment();
                        break;
                    case R.id.menu_Update_meeting_time:
                        fragment=new UpdateTimeFragment();
                        break;
                    case R.id.menu_Update_meeting_venue:
                        fragment = new UpdateVenueFragment();
                        break;
                    case R.id.menu_Update_delete_meeting:
                        fragment=new DeleteFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch(menuItem.getItemId()){
            case R.id.dashboard:
                //if menuItem dashboard is clicked then put the object of the Dashboard fragment into fragment
                fragment = new DashboardFragment();
                break;
            case R.id.users:
                //if menuItem users is clicked then put the object of the Dashboard fragment into fragment
                fragment = new UsersFragment();
                break;
            case R.id.profile:
                //if menuItem profile is clicked then put the object of the Dashboard fragment into fragment
                fragment = new ProfileFragment();
                break;
        }
//        check if the fragment variable fragment is not null i.e.
//        the ueer has clicked one of the three items then call load fragment() and pass the selected option fragment.

        if (fragment != null){
            loadFragment(fragment);
        }
        return true;
    }
    // out of the above three fragment one of them is get passed in the below loadFragment() method and lpads the fragment                                                                                                                                                                                                                                                                                                                   will be in the fragment variable which is paased into into the load
    void loadFragment(Fragment fragment){

        // To attach fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    //To define the qctionbar title
    public void setactionbartitle(String title){
        getSupportActionBar().setTitle(title);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //to attach logout menu created in menu directory
        getMenuInflater().inflate(R.menu.logoutmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                 logoutUser();
                 break;
            case R.id.deleteAccount:
                deleteAccount();
                 break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAccount() {
        Call<DeleteResponse>  call= RetrofitClient
                              .getInstance()
                              .getApi()
                              .deleteUser(sharedPrefManager.getUser().getId());

        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                DeleteResponse deleteResponse = response.body();
                if (response.isSuccessful()){
                    if (deleteResponse.getError().equals("200")){
                        //when the user is deleted then we have to logout the user from the application
                        logoutUser();
                        //Toast.makeText(HomeActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                        Toast.makeText(HomeActivity.this, deleteResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(HomeActivity.this, "delete failed ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        //calling logout method residing in shared preference Manager
        // with the help of shared preference object
        sharedPrefManager.logout();
        /* now we have to clear all the tasks in switching from login activity to
         dashboard activity etc or between fragments.*/
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show();
    }
}