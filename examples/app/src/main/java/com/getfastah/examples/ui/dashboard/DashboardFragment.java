package com.getfastah.examples.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.getfastah.examples.ExampleApplication;
import com.getfastah.examples.database.FastahDatabase;
import com.getfastah.networkkit.MeasureSample;
import com.getfastah.networkkit.testapp.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FastahDatabase mFastahDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mFastahDatabase = ((ExampleApplication) getContext().getApplicationContext()).getFastahDatabase();

        dashboardViewModel =
                ViewModelProviders.of(this, new DashboardViewModel.Factory(getActivity().getApplication(), mFastahDatabase)).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);

        dashboardViewModel.getNetworkLatencyData().observe(getViewLifecycleOwner(), new Observer<MeasureSample>() {
            @Override
            public void onChanged(@Nullable MeasureSample s) {
                dashboardViewModel.persistMeasurement(s);
                textView.setText(s.toString());
            }
        });
        return root;
    }
}