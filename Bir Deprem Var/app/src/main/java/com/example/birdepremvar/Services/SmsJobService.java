package com.example.birdepremvar.Services;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmsJobService extends JobService {
    private SmsSend smsSend;
    JobParameters jobParameters;


    @Override
    public boolean onStartJob(JobParameters params) {
        jobParameters = params;
        Log.i("xxx", "OnStartJob() çalıştı");
        smsSend = new SmsSend();
        smsSend.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("xxx", "OnStopJob() çalıştı");

        return false;
    }

    private class SmsSend extends AsyncTask<Void, Void, Void> {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.birdepremvar", MODE_PRIVATE);

        String lat, lng;
        LocationManager locationManager;
        LocationListener locationListener;
        ArrayList<String> kisitel;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String aktifKullanici = firebaseUser.getEmail();
        String adıSoyadı, ePosta;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        @SuppressLint("MissingPermission")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Location();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AcilDurumKisileriniGetir();
            KullanıcıBilgileriniGetir();
            Log.i("xxx", "60 sn bekleniyor");
            SystemClock.sleep(60000);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("xxx", "Süre Doldu");

            if (!sharedPreferences.getBoolean("isGuvende", true)) {
                for (int i = 0; i < kisitel.size(); i++) {
                    Log.i("xxx", kisitel.get(i) + "numaralı telefona mesaj gönderildi");
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+90" + kisitel.get(i), null, "Güvende olmayabilirim Konumum: https://www.google.com/maps/place/" + lat + "," + lng, null, null);
                    GuvendeOlmayanlaraEkle(ePosta, adıSoyadı, lat, lng, false);
                }
            }
            locationManager.removeUpdates(locationListener);
            jobFinished(jobParameters, false);

        }

        public void Location() {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    System.out.println("Locat : " + location.toString());
                    lat = "" + location.getLatitude();
                    lng = "" + location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
        }


        public void AcilDurumKisileriniGetir() {

            Log.i("xxx", "AcilDurumKisileriniGetir() çalıştı");
            kisitel = new ArrayList<>();

            CollectionReference collectionReference = firebaseFirestore.collection("Kisiler").document("Kisiler").collection(aktifKullanici);
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                    if (queryDocumentSnapshots == null) {
                        System.out.println("queryDocumentSnapshots null !!!");
                    } else {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> data = snapshot.getData();
                            kisitel.add(data.get("telefon").toString());
                        }
                    }
                }
            });

        }

        public void GuvendeOlmayanlaraEkle(String eposta, String adiSoyadi, String lat, String lng, Boolean guven) {
            Log.i("xxx", " GuvendeOlmayanlaraEkle() çalıştı");
            HashMap<String, Object> kisi = new HashMap<>();
            kisi.put("eposta", eposta);
            kisi.put("lat", lat);
            kisi.put("lng", lng);
            kisi.put("adiSoyadi", adiSoyadi);
            kisi.put("guven", guven);


            firebaseFirestore.collection("Deprem").document(eposta).set(kisi).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    System.out.println("vt+");
                    Toast.makeText(getApplicationContext(), "Bilgiler Tabanına Kaydedildi", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("vt-");
                    Toast.makeText(getApplicationContext(), "Bilgiler Tabanına Kaydedilemedi", Toast.LENGTH_LONG).show();
                }
            });


        }

        public void KullanıcıBilgileriniGetir() {
            Log.i("xxx", "KullanıcıBilgileriniGetir() çalıştı");
            CollectionReference collectionReference = firebaseFirestore.collection("kullanicilar");
            collectionReference.whereEqualTo("eposta", aktifKullanici).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }

                    if (queryDocumentSnapshots == null) {
                        System.out.println("queryDocumentSnapshots null !!!");
                    } else {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> data = snapshot.getData();
                            System.out.println(data);
                            adıSoyadı = data.get("adisoyadi").toString();
                            ePosta = data.get("eposta").toString();
                            System.out.println(adıSoyadı + ePosta);

                        }
                    }
                }
            });


        }
    }
}
