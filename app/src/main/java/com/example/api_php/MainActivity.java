package com.example.api_php;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editname, editpassword;
    Button btnlogin, btnregis;

    String URL = "http://10.0.2.2/raka_api/kon.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editname = findViewById(R.id.editname);
        editpassword = findViewById(R.id.editpassword);

        btnlogin = findViewById(R.id.btnlogin);
        btnregis = findViewById(R.id.btnregis);

        boolean isLogin = getSharedPreferences("login", MODE_PRIVATE)
                .getBoolean("isLogin", false);

        if (isLogin) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }

        btnregis.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });

        btnlogin.setOnClickListener(view -> {

            String username = editname.getText().toString().trim();
            String password = editpassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {

                Toast.makeText(MainActivity.this,
                        "Isi username dan password",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    URL,

                    response -> {

                        Log.d("SERVER", response);

                        try {

                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            String message = obj.getString("message");

                            if (status.equals("success")) {

                                getSharedPreferences("login", MODE_PRIVATE)
                                        .edit()
                                        .putBoolean("isLogin", true)
                                        .apply();

                                Toast.makeText(MainActivity.this,
                                        message,
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(
                                        MainActivity.this,
                                        HomeActivity.class
                                );

                                startActivity(intent);
                                finish();

                            } else {

                                Toast.makeText(MainActivity.this,
                                        message,
                                        Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {

                            Toast.makeText(MainActivity.this,
                                    "JSON ERROR : " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();

                            Log.d("JSON_ERROR", e.toString());
                        }

                    },

                    error -> {

                        Toast.makeText(MainActivity.this,
                                "ERROR : " + error.toString(),
                                Toast.LENGTH_LONG).show();

                        Log.d("VOLLEY_ERROR", error.toString());
                    }

            ) {

                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<>();

                    params.put("username", username);
                    params.put("password", password);

                    return params;
                }
            };

            Volley.newRequestQueue(MainActivity.this).add(request);
        });
    }
}