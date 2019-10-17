package com.example.nightpatrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nightpatrol.api.model.Shift;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {
    private List<Shift> shiftdata;

    // RecyclerView recyclerView;
    public ShiftAdapter(List<Shift> shiftdata) {
        this.shiftdata = shiftdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_shifts, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Shift myListData = shiftdata.get(position);
        Date date = new java.util.Date((long) myListData.getStartTime());
// the format of your date
        SimpleDateFormat day = new java.text.SimpleDateFormat("EEEE d MMM");
        SimpleDateFormat time = new java.text.SimpleDateFormat("HH:mm");
// give a timezone reference for formatting (see comment at the bottom)
        time.setTimeZone(java.util.TimeZone.getTimeZone("GMT+11"));
        String formattedDate = day.format(date);

        holder.dateView.setText(formattedDate);
        holder.vanView.setText(myListData.getVehicle());
        holder.timeView.setText(time.format(date));
        holder.teamIDView.setText(myListData.getId());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    @Override
    public int getItemCount() {
        return shiftdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateView;
        public TextView vanView;
        public TextView timeView;
        public TextView teamIDView;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);

            this.dateView = itemView.findViewById(R.id.dateText);
            this.vanView = itemView.findViewById(R.id.vanText);
            this.timeView = itemView.findViewById(R.id.timeText);
            this.teamIDView = itemView.findViewById(R.id.teamIDText);

            linearLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
