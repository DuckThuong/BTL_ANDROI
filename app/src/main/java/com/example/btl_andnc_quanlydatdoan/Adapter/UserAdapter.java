package com.example.btl_andnc_quanlydatdoan.Adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_andnc_quanlydatdoan.Domain.User;
import com.example.btl_andnc_quanlydatdoan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public interface OnUserClickListener {
        void onUserClick(User user);
        void onDeleteClick(String userId);
    }

    private OnUserClickListener listener;
    private List<User> userList;

    public UserAdapter(List<User> userList, OnUserClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder,
                                 int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private Button btnXoa;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            btnXoa = itemView.findViewById(R.id.btnXoa);
            txtName = itemView.findViewById(R.id.txtName);
        }

        public void bind(User user) {
            txtName.setText(user.getName());

            btnXoa.setOnClickListener(v -> {
                listener.onDeleteClick(user.getId());
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserClick(user);
                }
            });
        }
    }
}