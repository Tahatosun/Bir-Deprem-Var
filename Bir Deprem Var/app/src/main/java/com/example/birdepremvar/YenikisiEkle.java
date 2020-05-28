package com.example.birdepremvar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.birdepremvar.R;
import com.example.birdepremvar.Kisilerim;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class YenikisiEkle extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    EditText kisieposta;
    EditText kisiad;
    EditText kisitel;
    String aktifKullanici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yenikisi_ekle);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        aktifKullanici=firebaseUser.getEmail();


        kisiad=findViewById(R.id.txtYeniKisiEkleAdSoyad);
        kisieposta=findViewById(R.id.txtYeniKisiEkleEposta);
        kisitel=findViewById(R.id.txtYeniKisiEkleTelefon);


    }

    public void YeniKisiEkle(View view){


        HashMap<String, Object> postdata = new HashMap<>();
        postdata.put("adisoyadi",kisiad.getText().toString());
        postdata.put("eposta", kisieposta.getText().toString());
        postdata.put("telefon", kisitel.getText().toString());


        firebaseFirestore.collection("Kisiler").document("Kisiler").collection(aktifKullanici).document(kisieposta.getText().toString()).set(postdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Kişi Başarılı Bir Şekilde Eklendi",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"HATA!!!\nkişi Eklenemdi Tekrar Deneyin\n"+e.toString(),Toast.LENGTH_LONG).show();
            }
        });


        System.out.println("Yeni Kisi eklendi, Yeni Kişi Ekle Activitty finis edildi Ve Kişilerim Activitye Geçildi");
        Intent intent =new Intent(getApplicationContext(), Kisilerim.class);
        startActivity(intent);
        finish();
    }

}
