package com.example.nfcwrite;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class UsuarioSingleton
{
    private static UsuarioSingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;
    // Request es cada post, get, put, delete
    public UsuarioSingleton(Context context)
    {
        this.context = context;
        requestQueue = getRequestQueue();
    }
    public static synchronized UsuarioSingleton
        // Método estático y sincronizado
        getInstance(Context context){
        // Comprobar si se a creado alguna instancia
            if (singleton == null){
                // En el caso de que no haya, la creamos
                singleton = new UsuarioSingleton(context);
            }
            return singleton;
    }
    // Mirar si hay una cola de peticiones
    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public void addToRequestQueue(Request req){
        getRequestQueue().add(req);
    }
}






