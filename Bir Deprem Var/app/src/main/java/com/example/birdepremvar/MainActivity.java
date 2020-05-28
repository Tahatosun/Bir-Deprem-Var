package com.example.birdepremvar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText txtKullaniciadi;
    EditText txtsifre;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!checkPermission(Manifest.permission.SEND_SMS) || !checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
       /*if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION,}, 44);
        }*/

        txtKullaniciadi=findViewById(R.id.txtgirisKullaniciAdi);
        txtsifre=findViewById(R.id.txtgirissifre);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            Intent intent=new Intent(getApplicationContext(), SonDepremler.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"Hoş Geldiniz",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if((grantResults.length>0) && (grantResults[0]+ grantResults[1])==PackageManager.PERMISSION_GRANTED){

            }
            else {
                if (!checkPermission(Manifest.permission.SEND_SMS) || !checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }
        }
    }

    public void GirisYap(View view){
        System.out.println("Giriş Yap!");
        String eposta=txtKullaniciadi.getText().toString();
        String sifre=txtsifre.getText().toString();

        if(eposta.matches("")||sifre.matches("")){
            Toast.makeText(getApplicationContext(),"Kullanıcı Adı ve Şifreyi Kontrol Edin!!",Toast.LENGTH_LONG).show();
        }else {
            firebaseAuth.signInWithEmailAndPassword(eposta,sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent=new Intent(getApplicationContext(),SonDepremler.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Kullanıcı Adı Ve Şifreyi Kontrol Edin!!",Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    public void KayitOl(View view){
        System.out.println("Kayıt Ol!");
        Intent intent=new Intent(getApplicationContext(), KayitOl.class);
        startActivity(intent);
    }
    private boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);

    }
}

