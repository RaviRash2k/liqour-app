package com.example.testingmad.adminFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.testingmad.MainActivity;
import com.example.testingmad.R;
import com.example.testingmad.adminHome.AdminHome;

public class AccountFragment extends Fragment {

    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        logout = rootView.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("CurrentUser", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        return rootView;
    }
}