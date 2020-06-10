package com.example.nfcwrite;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etNombre, etPassword;
    private Button btnAceptar;
    private String URL_BASE="http://192.168.1.12:8000/";
    private String userCode="";
    String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnAceptar = this.findViewById(R.id.btnAceptar);

        etNombre=findViewById(R.id.etNombre);
        etPassword=findViewById(R.id.etPassword);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    userLogin();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void userLogin() throws JSONException
    {
        username = etNombre.getText().toString();
        password = etPassword.getText().toString();
        String url=URL_BASE+"user/login";
        final JSONObject objDatos = new JSONObject();
        objDatos.put("usuario", username);
        objDatos.put("password",password);
        JsonObjectRequest arrayPeticion;

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            etNombre.setError("Introduce el usuario");
            etNombre.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Introduce la contraseña");
            etPassword.requestFocus();
            return;
        }


        //TODO BIEN
        arrayPeticion = new JsonObjectRequest(Request.Method.POST, url, objDatos, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Si no hay error en el response
                try {
                    if(response != null){
                        Toast.makeText(getApplicationContext(), "Sesión iniciada", Toast.LENGTH_SHORT).show();
                        //TODO Guardar codigo recibido en userCode
                        userCode=response.getString("codigonfc");
                        initNfc();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error al iniciar", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("joseba", ""+ response);
                    objDatos.put("usuario", etNombre.getText());
                    objDatos.put("password", etPassword.getText());
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("joseba",""+error.getMessage());
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user", username);
                params.put("password", password);
                return params;
            }
        };

        UsuarioSingleton.getInstance(getApplicationContext()).addToRequestQueue(arrayPeticion);
    }
    private void initNfc(){
        //TODO añadir variables login
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("user_code",this.userCode);
        this.startActivity(intent);
    }

}
