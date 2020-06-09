package com.example.nfcread;

import android.app.DialogFragment;
import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NFCReadFragment extends DialogFragment {

    public static final String TAG = NFCReadFragment.class.getSimpleName();
    private String URL_BASE="";
    public static Context miContexto;

    public static NFCReadFragment newInstance() {

        return new NFCReadFragment();
    }
    private TextView mTvMessage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_read,container,false);
        initViews(view);

        miContexto = inflater.getContext();

        return view;
    }

    private void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void onNfcDetected(Ndef ndef){

        readFromNFC(ndef);
    }

    private void readFromNFC(Ndef ndef) {

        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            Log.d(TAG, "readFromNFC: "+message);
            mTvMessage.setText(message);
            ndef.close();

        } catch (IOException | FormatException e) {
            e.printStackTrace();

        }
    }

    private void peticionAcceso() throws JSONException {

        String url = URL_BASE;
        String userCode="";
        String doorCode="";
        final JSONObject objDatos = new JSONObject();
        objDatos.put("user_code", userCode);
        objDatos.put("door_code", doorCode);
        JsonObjectRequest arrayPeticion;

        arrayPeticion = new JsonObjectRequest(Request.Method.PUT, url, objDatos, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Si no hay error en el response
                try {
                    if(response != null){

                        if(response.getBoolean("acceso")){
                            Toast.makeText(miContexto.getApplicationContext(), "Acceso permitido", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(miContexto.getApplicationContext(), "Acceso denegado", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("joseba","No entra");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_code", userCode);
                params.put("door_code", doorCode);
                return params;
            }
        };


    }
}
