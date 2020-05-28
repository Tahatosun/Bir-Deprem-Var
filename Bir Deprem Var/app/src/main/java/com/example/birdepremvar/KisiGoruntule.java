package com.example.birdepremvar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.birdepremvar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

public class KisiGoruntule extends AppCompatActivity {

    String aktifKullanici;
    String secilenkisi;
    String secilenKisiAdiSoyad;
    String secilenKisiTel;
    EditText edtadsoyad;
    EditText edteposta;
    EditText edttel;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisi_goruntule);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        edtadsoyad=findViewById(R.id.txtKisiGoruntuleAdSoyad);
        edteposta=findViewById(R.id.txtKisiGoruntuleEposta);
        edttel=findViewById(R.id.txtKisiGoruntuleTelefon);
        aktifKullanici=firebaseUser.getEmail();

        Intent intent=getIntent();
        secilenkisi=intent.getStringExtra("secilenkisieposta");
        kisigetir();



    }

    public void KisiGuncelle(View view){




    }

    public void KisiSil(View view){

        DocumentReference kisiguncelle=firebaseFirestore.collection("Kisiler").document("Kisiler");
        kisiguncelle=kisiguncelle.collection(aktifKullanici).document(secilenkisi);
        kisiguncelle.delete();

    }


    public void kisigetir(){

       CollectionReference collectionReference=firebaseFirestore.collection("Kisiler");
       collectionReference.document("Kisiler").collection(aktifKullanici).document(secilenkisi).addSnapshotListener(new EventListener<DocumentSnapshot>() {
           @Override
           public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

               if(e!=null){
                   Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
               }

               if(documentSnapshot==null){
                   System.out.println("queryDocumentSnapshots null !!!");
               }else{

                        secilenKisiAdiSoyad=documentSnapshot.get("adisoyadi").toString();
                        secilenKisiTel=documentSnapshot.get("telefon").toString();

                        edtadsoyad.setText(secilenKisiAdiSoyad);
                        edteposta.setText(secilenkisi);
                        edttel.setText(secilenKisiTel);

                        System.out.println("Kisigörüntüle:"+secilenKisiAdiSoyad+secilenkisi+secilenKisiTel);

                   }
               }
       });


    }


}
