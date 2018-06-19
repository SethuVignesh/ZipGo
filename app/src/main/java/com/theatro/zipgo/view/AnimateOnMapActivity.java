package com.theatro.zipgo.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.theatro.zipgo.POJO.PlaceList;
import com.theatro.zipgo.R;
import com.theatro.zipgo.view.mapanimation.MapAnimator;
import com.theatro.zipgo.model.PrefUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnimateOnMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = AnimateOnMapActivity.class.getSimpleName();
    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    private Button btnDraw;
    ArrayList<PlaceList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_on_map);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Animate on Map");
        getSupportActionBar().setIcon(R.mipmap.image003);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        // Initializing
        markerPoints = new ArrayList<LatLng>();
        arrayList = PrefUtils.getPlaceList(AnimateOnMapActivity.this);


        for (PlaceList placeList : arrayList) {
            markerPoints.add(placeList.getLatLng());
        }

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        // Getting Map for the SupportMapFragment
        fm.getMapAsync(this);


    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for (int i = 1; i < markerPoints.size() - 1; i++) {
            LatLng point = (LatLng) markerPoints.get(i);
            if (i == 1)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception ", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Enable MyLocation Button in the Map

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        map = googleMap;
        if (map == null) return;


        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
//                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        map.setMyLocationEnabled(true);


        MarkerOptions options = new MarkerOptions();
        int i = 0;
        for (LatLng point : markerPoints) {
            options.position(point);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            options.title(arrayList.get(i).getName()).visible(true);
            map.addMarker(options).showInfoWindow();
            i++;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : markerPoints) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);

        map.moveCamera(cu);

        if (markerPoints.size() >= 2) {
            LatLng origin = markerPoints.get(0);
            LatLng dest = markerPoints.get(markerPoints.size() - 1);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startAnim(List<LatLng> route) {
        if (route == null && route.size() < 3) {
            for (LatLng latLng : markerPoints) {
                route.add(latLng);
            }
        }
        if (map != null) {
            MapAnimator.getInstance().animateRoute(map, route);
        } else {
            Toast.makeText(getApplicationContext(), "Map not ready", Toast.LENGTH_LONG).show();
        }
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, PolylineOptions> {
        ArrayList<LatLng> route;

        // Parsing the data in non-ui thread
        @Override
        protected PolylineOptions doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                //  routes = parser.parse(jObject);
                route = parser.parseRoute(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            ArrayList<LatLng> points = null;
//            PolylineOptions lineOptions = null;
//
//            // Traversing through all the routes
//            for (int i = 0; i < routes.size(); i++) {
//                points = new ArrayList<LatLng>();
//                lineOptions = new PolylineOptions();
//
//                // Fetching i-th route
//                List<HashMap<String, String>> path = routes.get(i);
//
//                // Fetching all the points in i-th route
//                for (int j = 0; j < path.size(); j++) {
//                    HashMap<String, String> point = path.get(j);
//
//                    double lat = Double.parseDouble(point.get("lat"));
//                    double lng = Double.parseDouble(point.get("lng"));
//                    LatLng position = new LatLng(lat, lng);
//
//                    points.add(position);
//                }
//
//                // Adding all the points in the route to LineOptions
//                lineOptions.addAll(points);
//                lineOptions.width(2);
//                lineOptions.color(Color.RED);
//            }


            return null;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(PolylineOptions result) {


            // Drawing polyline in the Google Map for the i-th route
//                   map.addPolyline(result);
            startAnim(route);
        }
    }


}