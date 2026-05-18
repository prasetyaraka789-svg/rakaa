package com.example.api_php;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText regusername, regpassword;
    Button btndaftar;

    String URL = "http://10.0.2.2/raka_api/regis.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        regusername = findViewById(R.id.regusername);
        regpassword = findViewById(R.id.regpassword);
        btndaftar = findViewById(R.id.btndaftar);

        btndaftar.setOnClickListener(view -> {

            String username = regusername.getText().toString().trim();
            String password = regpassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest request = new StringRequest(Request.Method.POST, URL,
                    response -> {

                        Toast.makeText(RegisterActivity.this,
                                response,
                                Toast.LENGTH_LONG).show();

                    },
                    error -> {

                        Toast.makeText(RegisterActivity.this,
                                error.toString(),
                                Toast.LENGTH_LONG).show();

                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(request);
        });
    }
}