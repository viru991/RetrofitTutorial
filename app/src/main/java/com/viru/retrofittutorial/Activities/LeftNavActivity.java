package com.viru.retrofittutorial.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.viru.retrofittutorial.LeftNavFragments.AddFragment;
import com.viru.retrofittutorial.LeftNavFragments.DeleteFragment;
import com.viru.retrofittutorial.LeftNavFragments.HomeFragment;
import com.viru.retrofittutorial.LeftNavFragments.UpdateDateFragment;
import com.viru.retrofittutorial.LeftNavFragments.UpdateTimeFragment;
import com.viru.retrofittutorial.LeftNavFragments.UpdateVenueFragment;
import com.viru.retrofittutorial.R;

public class LeftNavActivity extends AppCompatActivity {
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_nav);

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

    //To define the qctionbar title
    public void setactionbartitle(String title){
        getSupportActionBar().setTitle(title);

    }
}
