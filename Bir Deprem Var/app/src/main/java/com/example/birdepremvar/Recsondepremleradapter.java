package com.example.birdepremvar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recsondepremleradapter extends RecyclerView.Adapter<Recsondepremleradapter.holder> {
    ArrayList<String> lokasyon;
    ArrayList<String> siddet;
    ArrayList<String> zaman;

    public Recsondepremleradapter(ArrayList<String> lokasyon, ArrayList<String> siddet, ArrayList<String> zaman) {
        this.lokasyon = lokasyon;
        this.siddet = siddet;
        this.zaman = zaman;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recsondepremler,parent,false);
        return new holder(view);
    }

    @Override
    public int getItemCount() {
        return lokasyon.size();
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        holder.bind(position);

    }

    public  class holder extends RecyclerView.ViewHolder {
        TextView txtlokasyon;
        TextView txtsiddet;
        TextView txtzaman;

        public holder(@NonNull View itemView) {
            super(itemView);

        }

        public void bind(int position){
            txtlokasyon=itemView.findViewById(R.id.txtkonum);
            txtsiddet=itemView.findViewById(R.id.txtsiddet);
            txtzaman=itemView.findViewById(R.id.txtzaman);
            txtlokasyon.setText(lokasyon.get(position));
            txtsiddet.setText(siddet.get(position));
            txtzaman.setText(zaman.get(position));
        }

    }

}
