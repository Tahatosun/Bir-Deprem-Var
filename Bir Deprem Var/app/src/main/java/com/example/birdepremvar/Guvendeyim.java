package com.example.birdepremvar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Guvendeyim extends AppCompatActivity {

    Button btnGuvendeyim;
    SharedPreferences sharedPreferences;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String aktifKullanici = firebaseUser.getEmail();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guvendeyim);

        sharedPreferences = getSharedPreferences("com.example.birdepremvar",MODE_PRIVATE);
        btnGuvendeyim=findViewById(R.id.btnGuvendeyim);


        btnGuvendeyim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KisiSil();
               sharedPreferences.edit().putBoolean("isGuvende",true).commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });

    }
    public void KisiSil(){
        Log.i("xxx","KisiSil() çalıştı");
        DocumentReference kisiSil=firebaseFirestore.collection("Deprem").document(aktifKullanici);
        kisiSil.delete();

    }


}
