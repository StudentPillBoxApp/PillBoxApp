package com.example.pillboxesapp.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pillboxesapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PillsFragment extends Fragment {
    private View view;
    private HomeActivity homeActivity;
    private FloatingActionButton addMedicineButton;
    private Button editMedicineButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pills, container, false);
        if (container != null && container.getContext() instanceof HomeActivity) {
            homeActivity = (HomeActivity) container.getContext();
        }
        assignWidgets();
        setUpButtonListeners();
        if (homeActivity != null) {
            homeActivity.getCurrentPillBox();
        }
        return view;
    }

    private void assignWidgets() {
        addMedicineButton = view.findViewById(R.id.floatingActionButton);
        editMedicineButton = view.findViewById(R.id.pillBoxEditButton);
    }


    private void setUpButtonListeners() {
        addMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeActivity != null) {
                    homeActivity.openNewMedicineActivity();
                }
            }
        });
        editMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeActivity != null) {
                    homeActivity.openNewMedicineActivity();
                }
            }
        });
    }

}
