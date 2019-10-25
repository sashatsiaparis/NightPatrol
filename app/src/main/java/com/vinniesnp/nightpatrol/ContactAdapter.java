package com.vinniesnp.nightpatrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vinniesnp.nightpatrol.api.model.ShiftUsers;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<ShiftUsers> shiftusers;

    public ContactAdapter(List<ShiftUsers> shiftusers) {
        this.shiftusers = shiftusers;
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_contacts, parent, false);

        ContactAdapter.ViewHolder viewHolder = new ContactAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ViewHolder holder, final int position) {

        final ShiftUsers shiftuser = shiftusers.get(position);

        holder.nameView.setText(shiftuser.getFullName());
        holder.phoneView.setText(shiftuser.getPhone());
        holder.emailView.setText(shiftuser.getEmail());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    @Override
    public int getItemCount() {
        return shiftusers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView phoneView;
        public TextView emailView;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            this.nameView = itemView.findViewById(R.id.nameText);
            this.phoneView = itemView.findViewById(R.id.phoneText);
            this.emailView = itemView.findViewById(R.id.emailText);

            linearLayout = itemView.findViewById(R.id.contactLayout);
        }
    }
}

