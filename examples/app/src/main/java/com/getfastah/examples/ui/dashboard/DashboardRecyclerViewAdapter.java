package com.getfastah.examples.ui.dashboard;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getfastah.examples.database.MeasureSampleEntity;
import com.getfastah.networkkit.ConnectionQuality;
import com.getfastah.networkkit.testapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<DashboardRecyclerViewAdapter.ViewHolder> {

    private List<MeasureSampleEntity> mMeasureSampleEntityList = new ArrayList<>();
    private static SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");

    public void updateList(List<MeasureSampleEntity> newList) {
        mMeasureSampleEntityList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_measure_sample_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MeasureSampleEntity viewModel = mMeasureSampleEntityList.get(position);
        holder.bind(viewModel);
    }

    @Override
    public int getItemCount() {
        return mMeasureSampleEntityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivIcon = itemView.findViewById(R.id.iv_image);
        private TextView tvNetworkState = itemView.findViewById(R.id.tv_network_state);
        private TextView tvNetworkLatency = itemView.findViewById(R.id.tv_network_latency);
        private TextView tvTimestamp = itemView.findViewById(R.id.tv_timestamp);

        private int greenColor = ContextCompat.getColor(itemView.getContext(), R.color.green);
        private int redColor = ContextCompat.getColor(itemView.getContext(), R.color.red);
        private int orangeColor = ContextCompat.getColor(itemView.getContext(), R.color.orange);
        private int yellowColor = ContextCompat.getColor(itemView.getContext(), R.color.yellow);
        private int grayColor = ContextCompat.getColor(itemView.getContext(), R.color.gray);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(MeasureSampleEntity vm) {
            tvNetworkState.setText(vm.getNetworkState());
            tvNetworkLatency.setText(vm.getLatency() + " ms");
            tvTimestamp.setText(mDateFormat.format(new Date(vm.getTimestamp())));
            ConnectionQuality quality = ConnectionQuality.valueOf(vm.getNetworkState());
            ivIcon.setImageResource(R.drawable.ic_baseline_wifi_24);
            switch (quality) {
                case EXCELLENT:
                    ImageViewCompat.setImageTintList(ivIcon, ColorStateList.valueOf(greenColor));
                    break;
                case GOOD:
                    ImageViewCompat.setImageTintList(ivIcon, ColorStateList.valueOf(yellowColor));
                    break;
                case MODERATE:
                    ImageViewCompat.setImageTintList(ivIcon, ColorStateList.valueOf(orangeColor));
                    break;
                case POOR:
                    ImageViewCompat.setImageTintList(ivIcon, ColorStateList.valueOf(redColor));
                    break;
                case OFFLINE:
                    ImageViewCompat.setImageTintList(ivIcon, ColorStateList.valueOf(grayColor));
                    ivIcon.setImageResource(R.drawable.ic_baseline_wifi_off_24);
                    break;
                case UNKNOWN:
                    ImageViewCompat.setImageTintList(ivIcon, ColorStateList.valueOf(grayColor));
                    ivIcon.setImageResource(R.drawable.ic_baseline_perm_scan_wifi_24);
            }
        }
    }
}
