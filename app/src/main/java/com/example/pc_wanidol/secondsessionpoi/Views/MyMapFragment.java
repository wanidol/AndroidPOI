package com.example.pc_wanidol.secondsessionpoi.Views;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc_wanidol.secondsessionpoi.Models.PlaceInterest;
import com.example.pc_wanidol.secondsessionpoi.R;
import com.example.pc_wanidol.secondsessionpoi.Tools.NeighborAsycncTask;
import com.example.pc_wanidol.secondsessionpoi.Tools.PlaceInterestDatabaseHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoogleApiClient mGoogleApiClient = null;
    private Location mLastLocation;
    private GoogleMap mGoogleMap;

    private GoogleMap googleCurrentMap;

    private Boolean mRequestingLocationUpdates = false;
    private  LocationRequest mLocationRequest = null; //for setting value in Location Provider
    private  float mRadius = 0;


    // for permissions
    private static final int REQUEST_LOCATION_PERMISSION = 2;


    // for dealing with asynctask
    private NeighborAsycncTask mCloseCharacterAsycncTask = null;
    private BroadcastReceiver neighborBR = null;

    public MyMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyMapFragment newInstance(String param1, String param2) {
        MyMapFragment fragment = new MyMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (mGoogleApiClient == null) {
            //Connecting with Google API Client in google service
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this) //set status callback for google API Client
                    .addOnConnectionFailedListener(this) //Set Listener when failed to connect to Google API
                    .addApi(LocationServices.API)// What API u need to connect
                    .build(); //Create instance

        }


        neighborBR = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String path = intent.getStringExtra(getResources().getString(R.string.key_neighbor_path));
//                Toast.makeText(getActivity(), path, Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_map, container, false);
        SupportMapFragment mapFragment  = (SupportMapFragment)   getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(this);



        PlaceInterestDatabaseHandler dbh = new PlaceInterestDatabaseHandler(getContext());
        ArrayList<PlaceInterest> poi = dbh.getPOI();

        LatLngBounds.Builder llb = LatLngBounds.builder();
        for (PlaceInterest p : poi) {
            String lat2 = String.valueOf(p.getLatitude());
            String lng2 = String.valueOf(p.getLongitude());
            LatLng latLngPoi = new LatLng(p.getLatitude(), p.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLngPoi).title(p.toString()).alpha(0.5f).snippet("Lat Lng : "+lat2 + " : "+lng2));
            llb.include(latLngPoi);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(llb.build(), 40));
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) { //Method of ConnectionCallBack working when success to connect to API ..call Location Service here

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();

        } else {
            // permission has been granted, continue as usual
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                updatePoiUI(mLastLocation);

            }

            if (mRequestingLocationUpdates) {
                startLocationUpdates();
            } else {
                stopLocationUpdates();
            }
        }

    }


    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(
                getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION_PERMISSION);
    }



    @Override
    public void onConnectionSuspended(int i) { //ConnectionCallBack working when when cancel to connect API

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { //From method OnConnectFailedListener Can't connect to API

    }

    @Override
    public void onLocationChanged(Location location) { //from Method Location listener Read current location of device by send data by class Location
        Location mCurrentLocation = location;
        if (mCurrentLocation != null) {
//            String lats = "" + mCurrentLocation.getLatitude();
//            String longs = "" + mCurrentLocation.getLongitude();
//            Toast.makeText(getActivity(), lats + " " + longs, Toast.LENGTH_LONG).show();
            updatePoiUI(location);
        }


    }

    @Override
    public boolean onMarkerClick(Marker marker) {

       return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect(); //connect to Google API everytime when u back to this fragment

    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect(); //Disconnect safe battery

    }



    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
        getContext().unregisterReceiver(neighborBR);
    }

    protected void stopLocationUpdates() { //Stop working
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }


    @Override
    public void onResume() {
        super.onResume();
        setLocationParmeters();
        if (mGoogleApiClient.isConnected()){
            if (mRequestingLocationUpdates) {
                startLocationUpdates();
            } else {
                stopLocationUpdates();
            }
        }
        getContext().registerReceiver(neighborBR, new IntentFilter(getResources().getString(R.string.key_neighbor_intent)));
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();

        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates( //Call Location Service
                    mGoogleApiClient, mLocationRequest, this);
        }
    }






    private void setLocationParmeters(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());//Gets a SharedPreferences instance that points to the default file that is used by the preference framework in the given context.
            mRequestingLocationUpdates = sharedPref.getBoolean(getResources().getString(R.string.key_location_switch), false);
        mRadius = Float.parseFloat(sharedPref.getString(getResources().getString(R.string.key_search_radius),"0"));

        if (mRequestingLocationUpdates) {
            int interval = Integer.parseInt(sharedPref.getString(getResources().getString(R.string.key_search_delay), "10"));

            mLocationRequest = new LocationRequest(); //Location Povider is ready so calling Location Service
            mLocationRequest.setInterval(1000 * interval); //set time for read position millisecond
            mLocationRequest.setFastestInterval(1000 * interval / 2); //read fastest that it can
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //read data high priority affect to battery
        }



    }




    private void updatePoiUI(Location location){

        mGoogleMap.clear();
        PlaceInterestDatabaseHandler dbh = new PlaceInterestDatabaseHandler(getContext());

        Location mCurrentLocation = location;
        if (mCurrentLocation != null) {

            // User Current Location
            //Lat Lng Description
            String lat = String.valueOf(location.getLatitude());
            String lng = String.valueOf(location.getLongitude());

            Log.d("msg", "Loc : "+lat + ":"+lng);
            LatLng latLngPoi = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            MarkerOptions mo = new MarkerOptions().position(latLngPoi).title("Current Location").snippet("Lat Lng : "+lat + " : "+lng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));;
            mGoogleMap.addMarker(mo);


        }
        ArrayList<PlaceInterest> poi = dbh.getPOI();
        LatLngBounds.Builder llb = LatLngBounds.builder();

        for (PlaceInterest p : poi) {
            String lat2 = String.valueOf(location.getLatitude());
            String lng2 = String.valueOf(location.getLongitude());
            LatLng latLngPoi = new LatLng(p.getLatitude(), p.getLongitude());
            MarkerOptions mo = new MarkerOptions().position(latLngPoi).title(p.toString()).snippet("Lat Lng : "+lat2 + " : "+lng2);
            Location poiLocation = new Location("");
            poiLocation.setLatitude(p.getLatitude());
            poiLocation.setLongitude(p.getLongitude());

            float dis = location.distanceTo(poiLocation);
            if ( dis > mRadius){
                mo.alpha(0.5f);
            }
            mGoogleMap.addMarker(mo);
            llb.include(latLngPoi);
        }
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(llb.build(),100));
        new NeighborAsycncTask(this.getContext().getApplicationContext(),location).execute(poi.toArray());
    }

}

