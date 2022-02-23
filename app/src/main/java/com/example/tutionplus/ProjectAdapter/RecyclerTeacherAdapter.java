package com.example.tutionplus.ProjectAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutionplus.ProjectClass.Teacher;
import com.example.tutionplus.ProjectClass.User;
import com.example.tutionplus.R;

import java.util.ArrayList;

public class RecyclerTeacherAdapter extends RecyclerView.Adapter<RecyclerTeacherAdapter.ViewHolder> {

    private ArrayList<Teacher> teacherList;

    public RecyclerTeacherAdapter(ArrayList<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_content_teacher_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.teacher_name.setText(teacherList.get(position).getUserName());
        holder.teacher_location.setText(teacherList.get(position).getLocation());
        holder.teacher_status.setText(teacherList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView teacher_name;
        TextView teacher_location;
        TextView teacher_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            teacher_name = (TextView) itemView.findViewById(R.id.teacher_name);
            teacher_location = (TextView) itemView.findViewById(R.id.teacher_location);
            teacher_status = (TextView) itemView.findViewById(R.id.teacher_status);

        }
    }
}
