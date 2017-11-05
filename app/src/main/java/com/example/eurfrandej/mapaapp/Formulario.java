package com.example.eurfrandej.mapaapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Formulario extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Tipo de Olor");


        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Atención");
        dialogo1.setMessage("¿ Desea agregar este marcador ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                try {
                    aceptar();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelar();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                dialogo1.show();


                Snackbar.make(view, "Enviando...", Snackbar.LENGTH_LONG)
                        .setAction( "Sent" , null).show();

            }
        });




    }


    public void aceptar() throws IOException {





        Bundle datos =  this.getIntent().getExtras();

        if(datos != null)
        {

            float Lat = datos.getFloat("Latitud");
            float Long = datos.getFloat("Longitud");




            ServidorHTTP nuevo = new  ServidorHTTP();

//            ServidorHTTP.ObtenerWebService marca = new ServidorHTTP.ObtenerWebService();


            RadioGroup botones = (RadioGroup)findViewById(R.id.Olor);

            int checked = botones.getCheckedRadioButtonId();

            Toast t=Toast.makeText(this," Esto es checked: "+checked, Toast.LENGTH_SHORT);

            RadioButton olor;
            String Olor = "";

            switch(checked)
                {
                    case R.id.radioButton1:
                        Olor= "Gas";
                        break;

                    case R.id.radioButton2:
                        Olor= "Basura";
                        break;


                    case R.id.radioButton3:
                        Olor= "Gasolina";
                        break;


                    case R.id.radioButton4:
                        Olor = "Aguas%20Residuales";
                        break;


                    default:
                        Olor = "Aguas%20Residuales";

                                            }



            EditText ip = (EditText)findViewById(R.id.IP);

            String lat = String.valueOf(Lat);
            String longi = String.valueOf(Long);

            String IP =ip.getText().toString();

//            Olor = olor.getText().toString();



            String www="http://192.168.1.107:5000/?";

//            www= www+"Latitud=7";
//
//            www= www+"&";
//
//            www= www+"Longitud=6";
//
//            www= www+"&";
//
//            www= www+"Olor=uj";


            nuevo.enviar_recibir(www, lat, longi, Olor);

            t.makeText(this," Gracias por agregar este marcador."+Lat+" "+Long+" "+IP+" "+Olor, Toast.LENGTH_SHORT);
//
//
//            if (resp)
//            {

//                t.show();
//            }
//            else
//            {
//                Toast t=Toast.makeText(this," No pude acceder a tu localizacion ", Toast.LENGTH_SHORT);
//                t.show();
//            }


            //Enviar(IP, lat, longi, Olor);


            Intent i = new Intent(getApplicationContext(), Mapa.class );
            startActivity(i);
        }



//            Toast t=Toast.makeText(this," No pude acceder a tu localizacion ", Toast.LENGTH_SHORT);


            Intent i = new Intent(getApplicationContext(), Mapa.class );
            startActivity(i);





    }

    public void cancelar() {
        finish();
    }


//
//public void Enviar(String IP, String lat, String longi, String Olor) throws IOException {
//
//
//        String www="http://192.168.1.107:5000/?";
//
//        www= www+"Latitud="+lat;
//
//        www= www+"&";
//
//        www= www+"Longitud="+longi;
//
//        www= www+"&";
//
//        www= www+"Olor="+Olor;
//
//
//
//
////        conexion.setRequestProperty("User-Agent", "Mozilla/5.0" +
////                " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
//
//            try {
////
//
//                URL url = new URL(www);
//
//                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
////                int resp = conexion.getResponseCode();
////
////
////                if (resp == HttpURLConnection.HTTP_OK){
//                    Toast t=Toast.makeText(this," Gracias por agregar este marcador."+" "+www, Toast.LENGTH_SHORT);
//                    t.show();
////                }
////
////                else {
////                    Toast t=Toast.makeText(this," No mande nada "+" "+www, Toast.LENGTH_SHORT);
////                    t.show();
////                }
//
//                conexion.disconnect();
//                }catch (Exception e){}
//            }







}
