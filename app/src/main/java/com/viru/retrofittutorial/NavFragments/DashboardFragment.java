package com.viru.retrofittutorial.NavFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viru.retrofittutorial.R;
import com.viru.retrofittutorial.SharedPrefManager;

public class DashboardFragment extends Fragment {
    TextView etname, etemail;
    SharedPrefManager sharedPrefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        etname = view.findViewById(R.id.etname);
        etemail = view.findViewById(R.id.etemail);
        // we passed getActvity as context due to fragment
        // if it would have been activity then we may have to pass getApplicationContext

        sharedPrefManager= new SharedPrefManager(getActivity());
        String username ="Hey! " + sharedPrefManager.getUser().getUsername();
        etname.setText(username);
        etemail.setText(sharedPrefManager.getUser().getEmail());
        return view;
    }
}