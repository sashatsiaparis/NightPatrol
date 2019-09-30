package com.example.nightpatrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {
    private ShiftData[] shiftdata;

    // RecyclerView recyclerView;
    public ShiftAdapter(ShiftData[] shiftdata) {
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
        final ShiftData myListData = shiftdata[position];
        holder.dateView.setText(shiftdata[position].getDate());
        holder.vanView.setText(shiftdata[position].getVan());
        holder.timeView.setText(shiftdata[position].getTime());
        holder.teamIDView.setText(shiftdata[position].getTeamID());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Shift: "+myListData.getDate(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return shiftdata.length;
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
