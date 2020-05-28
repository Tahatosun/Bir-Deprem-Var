package com.example.birdepremvar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class KayitOl extends AppCompatActivity {

    EditText txtkullaniciAdi;
    EditText txtePosta;
    EditText txttelefon;
    Spinner spnSehir;
    EditText txtsifreFirst;
    EditText txtsifreSecond;


    String kullaniciadi;
    String eposta;
    String tel;
    String secilisehir;
    String sifre1;
    String sifre2;



    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        txtkullaniciAdi=findViewById(R.id.txtKayitOlAdSoyad);
        txtePosta=findViewById(R.id.txtKayitOlEposta);
        txttelefon=findViewById(R.id.txtKayitOltelefon);
        spnSehir=findViewById(R.id.spnKayitOlsehir);
        txtsifreFirst=findViewById(R.id.txtKayitOlParolaFirst);
        txtsifreSecond=findViewById(R.id.txtKayitOlParolaSecond);
        ArrayAdapter adapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.sehirler,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSehir.setAdapter(adapter);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();



    }
    public void KullaniciOlustur(View view){
        System.out.println("Kullanici Oluştur");

        kullaniciadi =txtkullaniciAdi.getText().toString();
        eposta=txtePosta.getText().toString();
        tel=txttelefon.getText().toString();
        secilisehir=spnSehir.getSelectedItem().toString();
        sifre1=txtsifreFirst.getText().toString();
        sifre2=txtsifreSecond.getText().toString();


        if(kullaniciadi.matches("") || eposta.matches("")||tel.matches("")||sifre1.matches("")||sifre2.matches("")){
            Toast.makeText(getApplicationContext(),"Girilen Bilgileri Kontroledin!!",Toast.LENGTH_LONG).show();
        }else {
            if(sifre1.matches(sifre2)&&sifre1.length()>5) {
                    //Bilgiler Doğru
                firebaseAuth.createUserWithEmailAndPassword(eposta,sifre1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getApplicationContext(),"Kullanici Oluşturuldu!!!",Toast.LENGTH_LONG).show();
                            veritabanıislemi();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Kullanici Oluşturulamadı!!!",Toast.LENGTH_LONG).show();
                        System.out.println(e.getMessage());
                    }
                });
            }else {
                Toast.makeText(getApplicationContext(),"Şifreleri Kontroledin!!",Toast.LENGTH_LONG).show();
            }
        }


    }


    public void SonDepremlereGec(){
        Intent intent =new Intent(getApplicationContext(), SonDepremler.class);
        startActivity(intent);
    }




    public void veritabanıislemi() {

        //veritabanı işlemleri
        HashMap<String, Object> postdata = new HashMap<>();
        postdata.put("adisoyadi",kullaniciadi);
        postdata.put("eposta", eposta);
        postdata.put("telefon", tel);
        postdata.put("sehir", secilisehir);

        firebaseFirestore.collection("kullanicilar").document(eposta).set(postdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("vt+");
                Toast.makeText(getApplicationContext(), "Bilgiler Tabanına Kaydedildi", Toast.LENGTH_LONG).show();
                SonDepremlereGec();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("vt-");
                Toast.makeText(getApplicationContext(), "Bilgiler Tabanına Kaydedilemedi", Toast.LENGTH_LONG).show();
            }
        });


        SonDepremlereGec();
    }

}





