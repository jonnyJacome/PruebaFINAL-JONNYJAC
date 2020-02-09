package com.example.pruebafinal_jonnyjac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import web.Asynchtask;
import web.WebService;



public class MainActivity extends AppCompatActivity implements Asynchtask, AdapterView.OnItemClickListener, OnMapReadyCallback {
        GoogleMap nMap;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Map<String, String> datos = new HashMap<String, String>();
            WebService ws= new WebService("http://www.geognos.com/api/en/countries/info/all.json",
                    datos, this, this);
            ws.execute("");
            ListView lstOpciones = (ListView) findViewById(R.id.listPaises);
            lstOpciones.setOnItemClickListener(this);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            getPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            ejecutarWS(bundle.getString("codISO"));
        }




    private void ejecutarWS(String dato){
        Map<String, String> datos = new HashMap<String, String>();
        WebService ws= new WebService("http://www.geognos.com/api/en/countries/info/"+dato+".json", datos, PaisInfo.this, PaisInfo.this  );
        ws.execute();
    }
    private TextView tvpais;
    private TextView tvcapital;
    private TextView tvcodigo;
    private ImageView ivbandera;


    private String codigoISO;
    private double latituf, longitud;
    public LatLng posicionMap;
    private String oeste;
    private String este;
    private String norte;
    private String sur;

    @Override
    public void processFinish(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        JSONObject jResults = jsonObject.getJSONObject("Results");
        tvpais.setText(jResults.getString("Name"));
        JSONObject jCapital = jResults.getJSONObject("Capital");
        tvcapital.setText(jCapital.getString("Name"));
        JSONObject jGeoRectangle = jResults.getJSONObject("GeoRectangle");
        oeste = jGeoRectangle.getString("West");
        este = jGeoRectangle.getString("East");
        norte = jGeoRectangle.getString("North");
        sur = jGeoRectangle.getString("South");
        JSONArray jGeoPt = jResults.getJSONArray("GeoPt");
        latituf = jGeoPt.getDouble(0);
        longitud = jGeoPt.getDouble(1);
        JSONObject jCountryCodes = jResults.getJSONObject("CountryCodes");
        codigoISO = jCountryCodes.getString("iso2");
        tvcodigo.setText(codigoISO);
        //Cargar imagen desde una URL
        Glide.with(this).load("http://www.geognos.com/api/en/countries/flag/"+codigoISO+".png").into(ivbandera);
        llamada();
    }



    GoogleMap mapa;
    public void llamada(){
        PolylineOptions rec = new PolylineOptions();
        rec.add(new LatLng(Double.parseDouble(norte),Double.parseDouble(oeste)));

        Marker marker =  mapa.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(norte),Double.parseDouble(oeste)))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("norte"));
        marker.showInfoWindow();
        rec.add(new LatLng(Double.parseDouble(norte),Double.parseDouble(este)));

        Marker marker2 =  mapa.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(norte),Double.parseDouble(este)))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("norte,este"));
        marker2.showInfoWindow();
        rec.add(new LatLng(Double.parseDouble(sur),Double.parseDouble(este)));
        Marker marker3 =  mapa.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(sur),Double.parseDouble(este)))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("sur,este"));
        marker3.showInfoWindow();
        rec.add(new LatLng(Double.parseDouble(sur),Double.parseDouble(oeste)));



        Marker marker4 =  mapa.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(sur),Double.parseDouble(oeste)))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("sur,oeste"));
        marker4.showInfoWindow();
        rec.add(new LatLng(Double.parseDouble(norte),Double.parseDouble(oeste)));



        rec.width(8);

        //Definimos el color de la Polilíneas
        rec.color(Color.CYAN);
        mapa.addPolyline(rec);
    }


        public void btnSatelite(View view){
            mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mapa.getUiSettings().setZoomControlsEnabled(true);
            llamada();
        }
        public void getPermission(String permission){

            if (Build.VERSION.SDK_INT >= 23) {
                if (!(checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED))
                    ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
            }
        }
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode==1){
                Toast.makeText(this.getApplicationContext(),"OK", Toast.LENGTH_LONG).show();
            }
        }
    private void ejecutarWS(String dato){
        Map<String, String> datos = new HashMap<String, String>();
        WebService ws= new WebService("http://www.geognos.com/api/en/countries/info/"+dato+".json", datos, PaisInfo.this, PaisInfo.this  );
        ws.execute();
    }
    TextView hola;
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long lg) {
            hola = (TextView) findViewById(R.id.txtTitulo);


            Map<String, String> datos = new HashMap<String, String>();
            WebService ws= new WebService("http://www.geognos.com/api/en/countries/info/"+hola+".json", datos, PaisInfo.this, PaisInfo.this  );
            ws.execute();
llamada();
            //   String MY_URL = ((Paises) adapterView.getItemAtPosition(i)).getImg();
            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MY_URL)));


//para descargar

            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(((Paises)adapterView.getItemAtPosition(i)).getImg()));
            request.setDescription("PDF Paper");
            request.setTitle("Pdf Artcilee");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "filedownload.pdf");
            DownloadManager manager = (DownloadManager) this.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
            try {
                manager.enqueue(request);        }
            catch (Exception e) {
                Toast.makeText(this.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }

        }


        @Override

        public void onMapReady(GoogleMap map) {
            mapa = map;
        }
    }















/**
 PolygonOptions rectangulo = new PolygonOptions()

 .add(new LatLng(-1.012670, -79.467096),

 new LatLng(-1.013336, -79.471773),

 new LatLng(-1.014006, -79.471795),

 new LatLng(-1.014360, -79.467257));



 rectangulo.strokeWidth(8);

 rectangulo.strokeColor(Color.RED);

 mMap.addPolygon(rectangulo);
 */




/**
 * Manipulates the map once available.
 * This callback is triggered when the map is ready to be used.
 * This is where we can add markers or lines, add listeners or move the camera. In this case,
 * we just add a marker near Sydney, Australia.
 * If Google Play services is not installed on the device, the user will be prompted to install
 * it inside the SupportMapFragment. This method will only be triggered once the user has
 * installed Google Play services and returned to the app.
 */


