package com.example.tutionplus.ProjectAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutionplus.R;
import com.example.tutionplus.Interfaces.RecyclerViewClickInterface;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private String[] subjectList;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public RecyclerAdapter(String[] subjectList, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.subjectList = subjectList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_content_subject, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.subject_name.setText(subjectList[position]);
    }

    @Override
    public int getItemCount() {
        return subjectList.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView subject_name;
        CardView card_subject_list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subject_name = (TextView) itemView.findViewById(R.id.subject_name);
            card_subject_list = (CardView) itemView.findViewById(R.id.card_subject_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition(), card_subject_list, subject_name);
                }
            });
        }
    }
}
