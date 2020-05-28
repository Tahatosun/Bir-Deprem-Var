package com.example.birdepremvar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class KullaniciProfili extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    TextView txtadsoyad;
    TextView txteposta;
    TextView txttelefon;
    Spinner spnSehir;
    String secilisehir;



    String aktifKullanici;
    String adıSoyadı;
    String ePosta;
    String telefon;
    String sehir;

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_profili);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        firebaseFirestore=FirebaseFirestore.getInstance();
        aktifKullanici=firebaseUser.getEmail().toString();



        txtadsoyad=findViewById(R.id.txtprofiladisoyadi);
        txteposta=findViewById(R.id.txtprofileposta);
        txttelefon=findViewById(R.id.txtprofiltelefon);
        spnSehir=findViewById(R.id.spnprofilsehir);


        adapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.sehirler,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSehir.setAdapter(adapter);

        BilgiGetir();
    }

    public void sehirdegistir(View view){

        secilisehir=spnSehir.getSelectedItem().toString();

        DocumentReference sehirdegistir=firebaseFirestore.collection("kullanicilar").document(aktifKullanici);

        sehirdegistir.update("sehir",secilisehir).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Şehir Güncellendi Yeni Şehir\n"+secilisehir,Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"!!!HAta!!!\nŞehir Güncellenemedi\n",Toast.LENGTH_LONG).show();
            }
        });


    }
    public void sifreguncelle(View view){

       firebaseAuth.sendPasswordResetEmail(aktifKullanici).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               Toast.makeText(getApplicationContext(),"Şifre Sıfırlama Epostanız Gönderildi",Toast.LENGTH_LONG).show();
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(getApplicationContext(),"Hata!!!\nİşleminiz Gerçekleştirilemedi Tekrar Deneyin. ",Toast.LENGTH_LONG).show();
           }
       });

    }
    public void cikis(View view){
        firebaseAuth.signOut();
        Intent  intent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }
    public void BilgiGetir(){


        CollectionReference collectionReference=firebaseFirestore.collection("kullanicilar");
        collectionReference.whereEqualTo("eposta",aktifKullanici).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        System.out.println(data);
                        adıSoyadı=data.get("adisoyadi").toString();
                        ePosta=data.get("eposta").toString();
                        sehir=data.get("sehir").toString();
                        telefon=data.get("telefon").toString();
                        System.out.println(adıSoyadı+ePosta+telefon+sehir);

                        txtadsoyad.setText(adıSoyadı);
                        txteposta.setText(ePosta);
                        txttelefon.setText(telefon);

                        spnSehir.setSelection(adapter.getPosition(sehir));



                    }
                }
            }
        });




    }


}
