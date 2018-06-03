package com.example.root.alergystats;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.root.alergystats.networking.UnsafeOkHttpClient;
import com.example.root.alergystats.rest.PollenGetter;
import com.example.root.alergystats.rest.models.PollenData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static String ALCO_POLLEN_DATASET = "5f0743fd-24e9-413a-b35d-f57d2854b640";
    public static String ALCO_POLLEN_RESOURCE = "93f1fbfc-5e83-472e-8618-7c47197b1bf2";
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter sa;
    private PollenGetter restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new GsonBuilder()
            .setDateFormat("yyyyMMdd HH:mm:ss")
            .create();

        Retrofit r = new Retrofit.Builder().baseUrl("https://datos.alcobendas.org")
                                           .addConverterFactory(GsonConverterFactory.create(gson))
                                           .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                                           .build();

        restClient = r.create(PollenGetter.class);

        sa = new MySimpleAdapter(
            this,
            data,
            R.layout.row,
            new String[]{"type", "concentration", "date"},
            new int[]{R.id.pollen_type, R.id.current_concentration, R.id.date}
        );
        ((ListView) findViewById(R.id.list)).setAdapter(sa);
    }

    public void getPollen(View v) {

        Call<List<PollenData>> call = restClient.getPollenData(
            MainActivity.ALCO_POLLEN_DATASET,
            MainActivity.ALCO_POLLEN_RESOURCE,
            "libro1-completo-del-2018_datos-abiertos.json");

        call.enqueue(
            new Callback<List<PollenData>>() {
                @Override
                public void onResponse(Call<List<PollenData>> call, Response<List<PollenData>> response) {
                    Toast.makeText(MainActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                    HashMap<String, PollenData> auxMap = new HashMap<String, PollenData>();
                    data.clear();
                    for(PollenData pl : response.body()){
                        if (! auxMap.containsKey(pl.getType()) || auxMap.get(pl.getType()).getDate().compareTo(pl.getDate()) < 0 ){
                            auxMap.put(pl.getType(), pl);
                        }
                    }
                    data.clear();
                    for(PollenData pl:auxMap.values()){
                        data.add(pl.toHashMap());
                    }
                    sa.notifyDataSetChanged();

                }


                @Override
                public void onFailure(Call<List<PollenData>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        );

        sa.notifyDataSetChanged();

    }

    private class MySimpleAdapter extends SimpleAdapter {
        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            int concentration = Integer.parseInt(((HashMap<String, String>) getItem(position)).get("concentration"));
            int col = Color.WHITE;
            if ( concentration > 161){
                col = Color.RED;
            } else if (concentration > 81){
                col = Color.YELLOW;
            } else if (concentration > 51){
                col = Color.GREEN;
            }
            row.setBackgroundColor(col);
            return row;
        }
    }
}