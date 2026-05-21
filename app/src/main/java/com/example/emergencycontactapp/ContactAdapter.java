package com.example.emergencycontactapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;
    private OnContactClickListener listener;

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
        void onDeleteClick(Contact contact, int position);
        void onEditClick(Contact contact, int position);
    }

    public ContactAdapter(List<Contact> contactList, OnContactClickListener listener) {
        this.contactList = contactList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        // Add an emoji to make it look like the other buttons
        holder.tvName.setText("🏠 " + contact.getName());
        holder.tvPhone.setText(contact.getPhone());
        
        holder.layoutContactInfo.setOnClickListener(v -> listener.onContactClick(contact));
        holder.btnEdit.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                listener.onEditClick(contact, currentPosition);
            }
        });
        holder.btnDelete.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                listener.onDeleteClick(contact, currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone;
        ImageButton btnEdit, btnDelete;
        LinearLayout layoutContactInfo;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvContactName);
            tvPhone = itemView.findViewById(R.id.tvContactPhone);
            btnEdit = itemView.findViewById(R.id.btnEditContact);
            btnDelete = itemView.findViewById(R.id.btnDeleteContact);
            layoutContactInfo = itemView.findViewById(R.id.layoutContactInfo);
        }
    }
}
