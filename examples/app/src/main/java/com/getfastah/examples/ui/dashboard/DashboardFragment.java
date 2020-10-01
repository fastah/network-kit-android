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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getfastah.examples.ExampleApplication;
import com.getfastah.examples.database.FastahDatabase;
import com.getfastah.examples.database.MeasureSampleEntity;
import com.getfastah.networkkit.MeasureSample;
import com.getfastah.networkkit.testapp.R;

import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FastahDatabase mFastahDatabase;
    private RecyclerView mRecyclerView;
    private DashboardRecyclerViewAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mFastahDatabase = ((ExampleApplication) getContext().getApplicationContext()).getFastahDatabase();

        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dashboardViewModel = new ViewModelProvider(this,
                new DashboardViewModel.Factory(getActivity().getApplication(), mFastahDatabase))
                .get(DashboardViewModel.class);

        mAdapter = new DashboardRecyclerViewAdapter();
        mRecyclerView = view.findViewById(R.id.measure_sample_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        dashboardViewModel.getMeasureSampleHistory().observe(getViewLifecycleOwner(), new Observer<List<MeasureSampleEntity>>() {
            @Override
            public void onChanged(List<MeasureSampleEntity> measureSampleEntities) {
                mAdapter.updateList(measureSampleEntities);
            }
        });
    }
}