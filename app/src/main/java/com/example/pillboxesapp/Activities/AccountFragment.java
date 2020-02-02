package com.example.pillboxesapp.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pillboxesapp.R;

public class AccountFragment extends Fragment {
    private View view;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        if (container != null && container.getContext() instanceof HomeActivity) {
            homeActivity = (HomeActivity) container.getContext();
        }
        createButtonListeners();
        return view;
    }

    private void createButtonListeners() {
        Button logout = view.findViewById(R.id.account_btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeActivity != null) {
                    homeActivity.logoutProcess();
                }
            }
        });
    }
}
