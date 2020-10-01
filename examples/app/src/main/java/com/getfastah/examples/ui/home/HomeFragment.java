package com.getfastah.examples.ui.home;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.getfastah.examples.ExampleApplication;
import com.getfastah.examples.MainActivity;
import com.getfastah.examples.database.FastahDatabase;
import com.getfastah.networkkit.ConnectionQuality;
import com.getfastah.networkkit.MeasureSample;
import com.getfastah.networkkit.testapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    private GoogleMap googleMap;
    private com.google.android.gms.maps.SupportMapFragment mapFragment;
    private Marker youAreHereMarker;
    private View rootView;
    private HalfGauge mHalfGauge;
    private TextView tvConnectionState;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FastahDatabase mFastahDatabase = ((ExampleApplication) getContext().getApplicationContext()).getFastahDatabase();

        homeViewModel =
                new ViewModelProvider(this, new HomeViewModel.Factory(getActivity().getApplication(),
                        mFastahDatabase)).get(HomeViewModel.class);

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

//        MainActivity.applyTopInsets(rootView, textView);
        homeViewModel.getLocationData().observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {

                if (googleMap != null) {
                    final LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                    if (youAreHereMarker != null) {
                        youAreHereMarker.remove();
                    }
                    youAreHereMarker = googleMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title("Your are here!").visible(true));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
                    youAreHereMarker.showInfoWindow();
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHalfGauge = view.findViewById(R.id.halfGauge);
        tvConnectionState = view.findViewById(R.id.tv_network_state);

        Range poorRange = new Range();
        poorRange.setColor(ContextCompat.getColor(getContext(), R.color.red));
        poorRange.setFrom(0);
        poorRange.setTo(2);

        Range moderateRange = new Range();
        moderateRange.setColor(ContextCompat.getColor(getContext(), R.color.orange));
        moderateRange.setFrom(2);
        moderateRange.setTo(4);

        Range goodRange = new Range();
        goodRange.setColor(ContextCompat.getColor(getContext(), R.color.yellow));
        goodRange.setFrom(4);
        goodRange.setTo(6);

        Range excellentRange = new Range();
        excellentRange.setColor(ContextCompat.getColor(getContext(), R.color.green));
        excellentRange.setFrom(6);
        excellentRange.setTo(8);

        mHalfGauge.addRange(poorRange);
        mHalfGauge.addRange(moderateRange);
        mHalfGauge.addRange(goodRange);
        mHalfGauge.addRange(excellentRange);
        mHalfGauge.setMinValue(0);
        mHalfGauge.setMaxValue(8);

        homeViewModel.getNetworkLatencyData().observe(getViewLifecycleOwner(), new Observer<MeasureSample>() {
            @Override
            public void onChanged(@Nullable MeasureSample s) {
                homeViewModel.persistMeasurement(s);
                mHalfGauge.setValue(resolveNetworkStatusValue(s.networkState));
                tvConnectionState.setText(s.networkState.toString());
            }
        });

    }

    private int resolveNetworkStatusValue(ConnectionQuality connectionQuality) {
        switch (connectionQuality) {
            case EXCELLENT:
                return 7;
            case GOOD:
                return 5;
            case MODERATE:
                return 3;
            case POOR:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public void onMapReady(GoogleMap gm) {
        googleMap = gm;
        googleMap.setPadding(0, convertDpToPixel(96), 0, convertDpToPixel(64));
        googleMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
    }

    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(px);
    }
}