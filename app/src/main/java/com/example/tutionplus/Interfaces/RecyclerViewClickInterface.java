package com.example.tutionplus.Interfaces;

import android.widget.TextView;

import androidx.cardview.widget.CardView;

public interface RecyclerViewClickInterface {
    void onItemClick(int position, CardView cardView, TextView textView);
}
