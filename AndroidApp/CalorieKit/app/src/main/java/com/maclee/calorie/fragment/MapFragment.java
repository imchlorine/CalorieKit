package com.maclee.calorie.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.SphericalUtil;
import com.maclee.calorie.MainActivity;
import com.maclee.calorie.R;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.maclee.calorie.api.HttpUrlHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;


public class MapFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    //public static LatLng HOME;//= new LatLng(-37.815868, 144.945175);
    private LocationRequest locationRequest;
    Double lat;
    Double lng;
    String url;
    LatLng center;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);


        StringBuilder sb = null;
        try {
            sb = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?");
            JSONObject userinfo = ((MainActivity) getActivity()).infoObj;
            String address = userinfo.getString("postcode") + "+" + userinfo.getString("address");
            sb.append("&address=" + address);
            sb.append("&key=" + getResources().getString(R.string.google_maps_key));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Object dataTransfer[] = new Object[1];
        dataTransfer[0] = sb.toString();
        new GetLatLong().execute(dataTransfer);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
//        googleApiClient = new GoogleApiClient.Builder(this.getContext())
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//        googleApiClient.connect();

//        if (center!=null){
//            MarkerOptions mHome = new MarkerOptions()
//                    .position(center)
//                    .title("Home");
//            mMap.addMarker(mHome);
//            mMap.addCircle(new CircleOptions()
//                    .center(center)
//                    .radius(5000)
//                    .strokeColor(getResources().getColor(R.color.colorBackground)));
//            mMap.moveCamera(getZoomForDistance(center, 5000));
//        }
    }

    private CameraUpdate getZoomForDistance(LatLng originalPosition, double distance) {
        LatLng rightBottom = SphericalUtil.computeOffset(originalPosition, distance, 135);
        LatLng leftTop = SphericalUtil.computeOffset(originalPosition, distance, -45);
        LatLngBounds sBounds = new LatLngBounds(new LatLng(rightBottom.latitude, leftTop.longitude), new LatLng(leftTop.latitude, rightBottom.longitude));
        return CameraUpdateFactory.newLatLngBounds(sBounds, 0);

    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        locationRequest = new LocationRequest().create();
//        locationRequest.setInterval(1000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }

    public class NearByParks extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... objects) {
            mMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            String result = new HttpUrlHelper().getHttpUrlConnection(url);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject parentObject = new JSONObject(s);
                JSONArray resultArray = parentObject.getJSONArray("results");
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject jsonObject = resultArray.getJSONObject(i);
                    JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");
                    String latitude = locationObj.getString("lat");
                    String longitude = locationObj.getString("lng");
                    String parkName = jsonObject.getString("name");
                    String vicinity = jsonObject.getString("vicinity");
                    String icon = jsonObject.getString("icon");
                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng)
                            .title(parkName);
                    mMap.addMarker(markerOptions).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    public class GetLatLong extends AsyncTask<Object, String, String> {

        @Override
        protected String doInBackground(Object... objects) {
            String result = new HttpUrlHelper().getHttpUrlConnection((String) objects[0]);
            return result;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject location = null;
            try {
                location = new JSONObject(s)
                        .getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");

                if (location != null) {
                    lat = Double.parseDouble(location.getString("lat"));
                    lng = Double.parseDouble(location.getString("lng"));
                } else {
                    lat = -37.815868;
                    lng = 144.945175;
                }
                 center = new LatLng(lat, lng);
                MarkerOptions mHome = new MarkerOptions()
                        .position(center)
                        .title("Home");
                mMap.addMarker(mHome);
//                mMap.addCircle(new CircleOptions()
//                        .center(center)
//                        .radius(5000)
//                        .strokeColor(getResources().getColor(R.color.colorBackground)));
                mMap.moveCamera(getZoomForDistance(center, 5000));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb.append("&location=" + lat + "," + lng);
            sb.append("&radius=" + 5000);
            sb.append("&types=" + "park");
            sb.append("&key=" + getResources().getString(R.string.google_maps_key));
            String url = sb.toString();
            Object dataTransfer[] = new Object[2];
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;
            //SupportMapFragment mapFragment = (SupportMapFragment) MapFragment.this.getChildFragmentManager().findFragmentById(R.id.map);
           // mapFragment.getMapAsync(MapFragment.this);
            new NearByParks().execute(dataTransfer);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) MapFragment.this.getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(MapFragment.this);
        }
    }

    public void reload(){
        // Reload current fragment
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("StepsFragment");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }


}
