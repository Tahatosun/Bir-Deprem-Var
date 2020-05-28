package com.example.birdepremvar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class Kisilerim extends AppCompatActivity {


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String aktifKullanici;
    ArrayList<String> kisiAd;
    ArrayList<String> kisieposta;
    ArrayList<String> kisitel;
    RecyclerView recyclerViewKisilerim;
    KisilerimFeedRecyclerAdaptere kisilerimFeedRecyclerAdaptere;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisilerim);
        recyclerViewKisilerim=findViewById(R.id.reckisilerim);

        kisiAd=new ArrayList<>();
        kisieposta=new ArrayList<>();
        kisitel=new ArrayList<>();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        aktifKullanici=firebaseUser.getEmail();


        kisiCek();



    }

    public void kisiCek(){


        CollectionReference collectionReference=firebaseFirestore.collection("Kisiler").document("Kisiler").collection(aktifKullanici);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                if(queryDocumentSnapshots==null){
                    System.out.println("queryDocumentSnapshots null !!!");
                }else{
                    for(DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> data=snapshot.getData();
                        kisiAd.add(data.get("adisoyadi").toString());
                        kisieposta.add(data.get("eposta").toString());
                        kisitel.add(data.get("telefon").toString());
                        kisilerimFeedRecyclerAdaptere.notifyDataSetChanged();

                    }
                }

            }
        });


            kisilerigoruntule();

    }

    public void YeniKisi(View view){

        System.out.println("Yeni Kişi Ekleme Ekranına Geçildi");
        Intent intent =new Intent(getApplicationContext(), YenikisiEkle.class);
        startActivity(intent);
        finish();
    }

    public void kisilerigoruntule(){
        System.out.println("Kisiler Görüntüleniyor");
        RecyclerView recyclerViewkisilerim=findViewById(R.id.reckisilerim);
        recyclerViewkisilerim.setLayoutManager(new LinearLayoutManager(this));
        kisilerimFeedRecyclerAdaptere =new KisilerimFeedRecyclerAdaptere(kisiAd,kisitel,kisieposta);
        recyclerViewkisilerim.setAdapter(kisilerimFeedRecyclerAdaptere);
    }
}

