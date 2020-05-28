package com.example.birdepremvar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SonDepremler extends AppCompatActivity {

    public String BASE_URL="https://192.168.2.91:45000/";
    Retrofit retrofit;
    ArrayList<String> lokasyon;
    ArrayList<String> siddet;
    ArrayList<String> zaman;
    RecyclerView recyclerView;
    Recsondepremleradapter recsondepremleradapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_son_depremler);
        lokasyon=new ArrayList<>();
        siddet=new ArrayList<>();
        zaman=new ArrayList<>();
        recyclerView=findViewById(R.id.RecSonDepremler);


        Gson gson=new GsonBuilder().setLenient().create();
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        loadDepremData();

    }
    private void loadDepremData(){
        final DepremAPI depremAPI=retrofit.create(DepremAPI.class);
        Call<JsonObject> call =depremAPI.getData();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    System.out.println("veri geldi");


                    for(int i=0;i<10;i++){
                        String lok=response.body().getAsJsonArray("result").get(i).getAsJsonObject().get("lokasyon").toString();
                        lok=lok.replace("\"","");
                        String time=response.body().getAsJsonArray("result").get(i).getAsJsonObject().get("date").toString();
                        time=time.replace("\"","");

                        lokasyon.add(lok);
                        siddet.add(response.body().getAsJsonArray("result").get(i).getAsJsonObject().get("mag").toString());
                        zaman.add(time);
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recsondepremleradapter=new Recsondepremleradapter(lokasyon,siddet,zaman);
                    recyclerView.setAdapter(recsondepremleradapter);

                    for(int i=0;i<10;i++){

                        System.out.println(lokasyon.get(i)+":"+siddet.get(i));

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Hata"+t);
                Toast.makeText(getApplicationContext(),"Depremler Getirilemedi!!!\nBağlantınızı kontrol edip\nTekrar Deneyiniz!!!",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void profilim(View view){
        System.out.println("Profilim!");
        Intent intent=new Intent(getApplicationContext(), KullaniciProfili.class);
        startActivity(intent);
    }

    public void Kisilerim(View view){
        System.out.println("Kişilerim!");
        Intent intent=new Intent(getApplicationContext(), Kisilerim.class);
        startActivity(intent);
    }
}
