package com.getfastah.examples.ui.home;

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
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.getfastah.examples.MainActivity;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = rootView.findViewById(R.id.text_home);
        MainActivity.applyTopInsets(rootView, textView);
        homeViewModel.getLocationData().observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                textView.setText(location.getAccuracy() + "/" + location.getLatitude() + "/" +location.getLongitude());
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
        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        return rootView;
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

    public static int convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(px);
    }
}