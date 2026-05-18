package com.example.api_php;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ListView listProduk;
    Button btntambah, btnlogout;

    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;

    String URL = "http://10.0.2.2/raka_api/tampil.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listProduk = findViewById(R.id.listProduk);
        btntambah = findViewById(R.id.btntambah);
        btnlogout = findViewById(R.id.btnlogout);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                list
        );

        listProduk.setAdapter(adapter);

        btntambah.setOnClickListener(view -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    TambahActivity.class
            );

            startActivity(intent);
        });

        btnlogout.setOnClickListener(view -> {

            getSharedPreferences("login", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(
                    HomeActivity.this,
                    MainActivity.class
            );

            startActivity(intent);
            finish();
        });

        loadData();
    }

    private void loadData() {

        StringRequest request = new StringRequest(
                Request.Method.GET,
                URL,

                response -> {

                    Log.d("DATA", response);

                    try {

                        list.clear();

                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject obj = array.getJSONObject(i);

                            String nama = obj.getString("nama_produk");
                            String harga = obj.getString("harga");
                            String stok = obj.getString("stok");

                            list.add(
                                    nama +
                                            " | Rp." + harga +
                                            " | Stok : " + stok
                            );
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {

                        Toast.makeText(HomeActivity.this,
                                "JSON ERROR : " + e.getMessage(),
                                Toast.LENGTH_LONG).show();

                        Log.d("JSON_ERROR", e.toString());
                    }

                },

                error -> {

                    Toast.makeText(HomeActivity.this,
                            "ERROR : " + error.toString(),
                            Toast.LENGTH_LONG).show();

                    Log.d("VOLLEY_ERROR", error.toString());
                }

        );

        Volley.newRequestQueue(this).add(request);
    }
}