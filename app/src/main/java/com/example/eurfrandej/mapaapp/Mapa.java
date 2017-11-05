package com.example.eurfrandej.mapaapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {


    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private static final int LOCATION_REQUEST_CODE = 1;

    //Variables

    private GoogleMap mMap;
    private Marker marcador;

    double latitud = 0.0;
    double longitud = 0.0;


    double longitudGPS, latitudGPS;
    double longitudNetwork, latitudNetwork;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        setTitle("Mapa de olores");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            mi_ubicacion();
            mMap.setMyLocationEnabled(true);



        } else {
            // Solicitar permiso
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }





        mMap.setOnMapClickListener( new GoogleMap.OnMapClickListener() {

            @Override

            public void onMapClick(LatLng Nueva_Ubicación)
            {

                mMap.addMarker(new MarkerOptions()
                        .position(Nueva_Ubicación)
                        .title("Esta es la marca nueva")
                        .snippet(" Hola gente ")


                );

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Nueva_Ubicación, 17) );

                //                Formulario x = new Formulario();
                //                 x.setContentView(R.layout.form);


                Intent i = new Intent(getApplicationContext(), Formulario.class );

                float Lat = (float) Nueva_Ubicación.latitude;
                float Long = (float) Nueva_Ubicación.longitude;

               i.putExtra("Latitud", Lat);
                i.putExtra("Longitud", Long );

                startActivity(i);

                //setContentView(R.layout.activity_formulario);

//
//               Intent i = new Intent(getApplicationContext(), formulario.class );
//                startActivity(i);



            }
        });




    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);

            } else {
                // Permission was denied. Display an error message.
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }
        }

    }



    private void actualizar_ubicacion (Location ubicacion)
    {

        if(ubicacion != null)
        {

            latitud= ubicacion.getLatitude();
            longitud= ubicacion.getLongitude();

            LatLng MiUbicacion = new LatLng(latitud, longitud);

            mMap.clear();

            String direccion = "";

            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(latitud, longitud, 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);

                    direccion = address.getAddressLine(0);

                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }


            if(direccion!="") {
                mMap.addMarker(new MarkerOptions()
                        .position(MiUbicacion)
                        .title("Estoy aqui")
                        .snippet("Esta es la direccion: " + direccion)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))

                );

            }
            else{
                mMap.addMarker(new MarkerOptions()
                        .position(MiUbicacion)
                        .title("Estoy aqui")
                        .snippet("Actualmente en un monte")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))

                );
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MiUbicacion, 17) );

        }
    }



    LocationListener revisar_ubicacion = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            //Recibo el location de este metodo
            actualizar_ubicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

            Intent i= new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);

        }
    };


    private void mi_ubicacion(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        //Creo la variable para administrar la ubicacion y los servicios

        LocationManager Administrar_ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Pido la ultima ubicación conocida por gps
        Location ubicacion = Administrar_ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        actualizar_ubicacion(ubicacion);


        // recibe como parámetros el tiempo de actualizacion, la distancia en metros y el metodo
        // LocationListener que se creo antes

        //Administrar_ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, revisar_ubicacion);

        Administrar_ubicacion.requestLocationUpdates("gps", 15000, 0, revisar_ubicacion);



    }


}
