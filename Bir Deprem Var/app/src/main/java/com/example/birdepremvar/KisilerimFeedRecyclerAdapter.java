package com.example.birdepremvar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class KisilerimFeedRecyclerAdaptere extends RecyclerView.Adapter<KisilerimFeedRecyclerAdaptere.KisiHolder> {

    private ArrayList<String> kisiad;
    private  ArrayList<String> kisitel;
    private ArrayList<String> kisieposta;


    public KisilerimFeedRecyclerAdaptere(ArrayList<String> kisiad, ArrayList<String> kisitel,ArrayList<String>kisieposta) {
        this.kisiad = kisiad;
        this.kisitel = kisitel;
        this.kisieposta=kisieposta;
    }

    @NonNull
    @Override
    public KisilerimFeedRecyclerAdaptere.KisiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.reckisilerim_card,parent,false);
        return  new KisiHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final KisilerimFeedRecyclerAdaptere.KisiHolder holder, final int position) {

        holder.kisiAd.setText(kisiad.get(position));
        holder.kisiTel.setText(kisitel.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context=holder.itemView.getContext();
                Intent intent=new Intent(context, KisiGoruntule.class);
                intent.putExtra("secilenkisieposta",kisieposta.get(position));
                System.out.println("Seçilen Kişi:"+kisieposta.get(position));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return kisiad.size();
    }

    class KisiHolder extends RecyclerView.ViewHolder {

        TextView kisiAd;
        TextView kisiTel;

        public KisiHolder(@NonNull View itemView) {
            super(itemView);
            kisiAd = itemView.findViewById(R.id.recardkisiad);
            kisiTel = itemView.findViewById(R.id.reccardkisitel);


        }


    }
}
