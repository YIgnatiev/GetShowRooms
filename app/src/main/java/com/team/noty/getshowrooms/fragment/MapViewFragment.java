package com.team.noty.getshowrooms.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team.noty.getshowrooms.R;
import com.team.noty.getshowrooms.api.GetTerSetter;
import com.team.noty.getshowrooms.api.getHttpGet;
import com.team.noty.getshowrooms.customTextView.TextViewPlus;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by copch on 07.02.2017.
 */

public class MapViewFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    MapView mMapView;
    private GoogleMap googleMap;

    getHttpGet request = new getHttpGet();
    ArrayList<GetTerSetter> setMarkersShowRooms = new ArrayList<>();
    String httpShowMarkers = "http://getshowroom.ru/api/showrooms";
    CameraPosition cameraPosition;
    LatLng latLng = null;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }



// Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createMap();


        return rootView;
    }
    public void createMap() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(request.getHttpGet(httpShowMarkers));

                    Gson gson = new Gson();
                    setMarkersShowRooms = gson.fromJson(jsonArray.toString(), new TypeToken<List<GetTerSetter>>() {
                    }.getType());

                    if (setMarkersShowRooms != null) {
                        LatLng latLngShowRoom;
                        String title = "";
                        for (int i = 0; i < setMarkersShowRooms.size(); i++) {

                            // Add a marker in Sydney and move the camera
                            latLngShowRoom = new LatLng(Double.parseDouble(setMarkersShowRooms.get(i).getLat()), Double.parseDouble(setMarkersShowRooms.get(i).getLon()));
                            mMap.addMarker(new MarkerOptions().title(String.valueOf(i)).position(latLngShowRoom).icon(BitmapDescriptorFactory.fromResource(R.drawable.pointmap)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // For zooming automatically to the location of the marker

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    // Use default InfoWindow frame
                    @Override
                    public View getInfoWindow(Marker args) {
                        Log.d("MyLog", " id " + setMarkersShowRooms.get(Integer.parseInt(args.getTitle())).getName());

                        // Getting view from the layout file info_window_layout
                        View v = getActivity().getLayoutInflater().inflate(R.layout.layout_for_window_info, null);
                        TextViewPlus nameShowRoom = (TextViewPlus) v.findViewById(R.id.nameShowRoom);
                        TextViewPlus review = (TextViewPlus) v.findViewById(R.id.review);
                        RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
                        RatingBar ratingBarruble = (RatingBar) v.findViewById(R.id.ruble);

                        nameShowRoom.setText(setMarkersShowRooms.get(Integer.parseInt(args.getTitle())).getName());
                        review.setText(setMarkersShowRooms.get(Integer.parseInt(args.getTitle())).getReviews() + " отзывов");
                        ratingBar.setRating(Float.parseFloat(setMarkersShowRooms.get(Integer.parseInt(args.getTitle())).getRating()));
                        ratingBarruble.setRating((float) setPrice(setMarkersShowRooms.get(Integer.parseInt(args.getTitle())).getPrice()));

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            public void onInfoWindowClick(Marker marker) {

                                replaceFragment(new DetailInfoFragment(), setMarkersShowRooms.get(Integer.parseInt(marker.getTitle())).getId());
                            }
                        });

                        // Returning the view containing InfoWindow contents
                        return v;
                    }

                    // Defines the contents of the InfoWindow
                    @Override
                    public View getInfoContents(Marker args) {
                        return null;

                    }
                });
            }
        });
    }

    private void replaceFragment(Fragment fragment, String string) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("id", string);
        fragment.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.replace(R.id.main_frame, fragment);
        transaction.commit();
    }

    public double setPrice(String s) {
        double rating = .0;
        switch (s) {
            case "до 5000р":
                rating = 1.00;
                break;
            case "от 5000р до 14000р":
                rating = 2.00;
                break;
            case "от 15000р до 25000р":
                rating = 3.00;
                break;
            case "от 25000р":
                rating = 4.00;
                break;

        }
        return rating;
    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("MyLog", "location " + mLastLocation.getLatitude() + " "+ mLastLocation.getLongitude());
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            bundle = getArguments();
            if (bundle != null) {
                latLng = new LatLng(Double.parseDouble(bundle.getString("lat")), Double.parseDouble(bundle.getString("lon")));
                Log.d("MyLog", "id show room " + latLng);
                cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
            } else {
                cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
            }
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
