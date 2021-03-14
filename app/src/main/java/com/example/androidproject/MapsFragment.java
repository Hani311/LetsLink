package com.example.androidproject;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsFragment extends Fragment implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    private static MapsFragment INSTANCE = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    String cityName;
    ViewGroup view;
    GoogleMap gMap;
    SearchView search = null;
    static boolean iftrue = true;
    private Button create;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_maps, null);
        search = view.findViewById(R.id.searchLocation);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        createEvent(); //The plus button at the top-right corner of the map method



    }

    public void JoinAndLeaveEvents(View dialogView, DataSnapshot snapshot) {
        Button join = dialogView.findViewById(R.id.join);
        EditText nameOfEvenet = dialogView.findViewById(R.id.nameOFEvent);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Events even = new Events(spinner.getSelectedItem().toString(), latLngg.longitude, latLngg.latitude, editText.getText().toString(), "8");

                Query query = FirebaseDatabase.getInstance().getReference("Events");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot post : snapshot.getChildren()) {
                            if (post.child("eventName").getValue().equals(nameOfEvenet.getText().toString())) {
                                Toast.makeText(getActivity(), "The event name is already in use, please use another!", Toast.LENGTH_LONG).show();
                            } else {

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        spawnNearbyEventsOnMap(googleMap);
        searchLocation();
        markerClicked();
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                try {

                    addNameToMarkerOnMap(latLng.latitude, latLng.longitude);
                } catch (IOException e) {
                    System.out.println("NO AREA FOUND !");
                }
                markerOptions.position(latLng).title(cityName);
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                gMap.addMarker(markerOptions);
                gMap.clear();
                spawnNearbyEventsOnMap(googleMap);


            }
        });


        getClientCurrentLocation(googleMap);


        //Bitmap bitmapIcon = BitmapFactory.decodeResource(getResources(), R.drawable.sport);
        //gMap.addMarker(new MarkerOptions().position(sydney).title("You").snippet("Snippet ...").icon(BitmapDescriptorFactory.fromBitmap(customizeImageToBitMap(R.drawable.sport))));

        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    public void markerClicked(){
        Log.e("mm", "marker: "+gMap);
                Query query = FirebaseDatabase.getInstance().getReference("Events").orderByChild("eventType");
                gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        boolean m = true;


                        String title = marker.getTitle();
                       // Log.e("Title", "onMarkerClick: "+title);


                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Events event = dataSnapshot.getValue(Events.class);

                                    if (event.getEventName().toLowerCase().equals(title.toLowerCase())) {
                                        final AlertDialog.Builder eventPopup = new AlertDialog.Builder(getActivity()); //Creates dialog
                                        final LayoutInflater inflater = LayoutInflater.from(getActivity());
                                        final View dialogView = inflater.inflate(R.layout.view_eventmarker, null);
                                        final AlertDialog dialog = eventPopup.create();
                                        // eventPopup.setNegativeButton("Cancel", null);
                                        eventPopup.setView(dialogView);
                                        eventPopup.setTitle("Event");
                                        eventPopup.show();
                                        deleteEventOnlyForUser(dialogView,event);


                                        TextView capacity = dialogView.findViewById(R.id.capacity);
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Joined Member").child(dataSnapshot.getKey()).child("joined");

                                        ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int number = snapshot.getValue(Integer.class);

                                                capacity.setText(number + "/" + event.getCapacity());
                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        TextView desc = dialogView.findViewById(R.id.description);
                                        desc.setText(event.getDescription());
                                        Button btnJoin = dialogView.findViewById(R.id.join);


                                        btnJoin.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                boolean value = false;

                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Joined Member").child(dataSnapshot.getKey()).child("joined");
                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        final int[] number = {snapshot.getValue(Integer.class)};
                                                        if (number[0] < Integer.parseInt(event.getCapacity())) {
                                                            // number++;
                                                            // ref.setValue(number);
                                                            DatabaseReference userJoind = FirebaseDatabase.getInstance().getReference("Joined Users").child(dataSnapshot.getKey());
                                                            userJoind.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                    boolean newUser = true;

                                                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                                                        if (snapshot1.getValue().equals(getUserID())) {
                                                                            newUser = false;
                                                                        }

                                                                    }

                                                                    if (newUser) {
                                                                        number[0]++;
                                                                        ref.setValue(number[0]);
                                                                        userJoind.child(getUserID()).setValue(getUserID());
                                                                    }
                                                                    Log.e("values", snapshot.toString());
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });

                                                            Log.e("TAGGGGG", number[0] + "");
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            }
                                        });

                                        Button btnLeave = dialogView.findViewById(R.id.leave);
                                        btnLeave.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Joined Member").child(dataSnapshot.getKey()).child("joined");
                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        final int[] number = {snapshot.getValue(Integer.class)};
                                                        DatabaseReference userJoind = FirebaseDatabase.getInstance().getReference("Joined Users").child(dataSnapshot.getKey());
                                                        userJoind.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                boolean newUser = true;

                                                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                                                    if (snapshot1.getValue().equals(getUserID())) {

                                                                        if (number[0] > 0) {
                                                                            number[0]--;
                                                                            ref.setValue(number[0]);
                                                                            userJoind.child(getUserID()).removeValue();
                                                                        }
                                                                    }

                                                                }


                                                                Log.e("values", snapshot.toString());

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });


                                                        Log.e("TAGGGGG", number[0] + "");

                                                    }


                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            }
                                        });

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        return true;
                    }



        });

    }

    public void deleteEventOnlyForUser(View dialogView,Events events){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events").child(events.getEventID());
        Button button = dialogView.findViewById(R.id.deleteEvent);

        if (getUserID().equals(events.getEventAdminID())) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reference.removeValue();
                    spawnNearbyEventsOnMap(gMap);
                }
            });
        }
    }


    public void getClientCurrentLocation(GoogleMap map) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (requireActivity().getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                Log.e("mad", "getClientCurrentLocation: " + location);

                                    // map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                      //    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                          //                                       int[] grantResults)

                                        return;
                                    }
                                    map.setMyLocationEnabled(true);
                                    Log.e("location", "onSuccess: Here!");

                            }
                        });


                    }
                }




        }



    public void addNameToMarkerOnMap(double lati, double longi) throws IOException {
        Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(lati, longi, 1);
        if (addresses.size() > 0) {
            cityName = addresses.get(0).getLocality();
        } else {
            System.out.println("No area found!");
        }
    }

    public void searchLocation() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = search.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert addressList != null;
                    Address address = addressList.get(0);
                    System.out.println(address);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    gMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    gMap.clear();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void spawnNearbyEventsOnMap(GoogleMap googleMap) {
        //Load from firebase data
        //once loaded, create if statements to check what the event is
        //Spawn the event as markers with description (only near current location, not the whole world)
        Query query = FirebaseDatabase.getInstance().getReference("Events");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot post : snapshot.getChildren()) {

                    double lats = Double.parseDouble(String.valueOf(post.child("latitude").getValue()));
                    double lon = Double.parseDouble(String.valueOf(post.child("longitude").getValue()));
                    String type = String.valueOf(post.child("eventType").getValue());
                    System.out.println(lats + "     " + lon);
                    int event = 0;
                    if (type.equals("sport")) {
                        event = R.drawable.sport;
                    } else if (type.equals("party")) { // add more icons later
                        event = R.drawable.party;
                    }

                    gMap = googleMap;
                    LatLng latLng = new LatLng(lats, lon);
                    gMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(String.valueOf(post.child("eventName").getValue()))
                            .icon(BitmapDescriptorFactory.fromBitmap(customizeImageToBitMap(event))));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    public Bitmap customizeImageToBitMap(int resourcePath) { //Example input: "R.id.sport"
        int height = 50; //Default
        int width = 50; //Default
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(resourcePath); //Change
        Bitmap b = bitmapdraw.getBitmap();

        return Bitmap.createScaledBitmap(b, width, height, false);
    }

    public void createEvent() {
        Boolean e = false;
        ImageView createEvent = getActivity().findViewById(R.id.createEventButton);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder eventPopup = new AlertDialog.Builder(getActivity()); //Creates dialog
                final LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View dialogView = inflater.inflate(R.layout.newnewtest, null); //Inflate the actual newnewtest.xml file
                final AlertDialog dialog;
                eventPopup.setView(dialogView);
                eventPopup.setTitle("Create Event");
                dialog = eventPopup.create();
                dialog.show();

                create = dialogView.findViewById(R.id.createEventB);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText tex = dialogView.findViewById(R.id.addressToLatLong);
                        String t = tex.getText().toString();
                        LatLng latLngg = null;
                        latLngg = getLocationFromAddress(getActivity(), t);
                        EditText editText = dialogView.findViewById(R.id.CreateDescription);
                        editText.getText().toString();

                        Spinner spinner = dialogView.findViewById(R.id.eventTypeSpinner);
                        spinner.getSelectedItem().toString();
                        EditText nameOfEvenet = dialogView.findViewById(R.id.nameOFEvent);

                        Spinner spinnerCapacity = dialogView.findViewById(R.id.eventCapacitySpinner);

                        Log.e("Lkljadlkfj", latLngg + "");
                        if (latLngg != null) {

                            DatabaseReference refere = FirebaseDatabase.getInstance().getReference("Events");
                            LatLng finalLatLngg = latLngg;

                            refere.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    boolean exists = false;
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        Events e = snapshot1.getValue(Events.class);
                                        if (e.getEventName().equals(nameOfEvenet.getText().toString())) {
                                            exists = true;
                                        }
                                    }
                                    if (exists != true) {
                                        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Events");
                                        String keys = myref.push().getKey();
                                        Events even = new Events(spinner.getSelectedItem().toString(), nameOfEvenet.getText().toString(), finalLatLngg.longitude, finalLatLngg.latitude, editText.getText().toString(), spinnerCapacity.getSelectedItem().toString(), getUserID(), keys);
                                        myref.child(keys).setValue(even);
                                        DatabaseReference myreference = FirebaseDatabase.getInstance().getReference("Joined Member");
                                        myreference.child(keys).child("joined").setValue(0);


                                        System.out.println(even);
                                    } else {
                                        Toast.makeText(getActivity(), "Already exists", Toast.LENGTH_SHORT).show();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        } else {
                            System.out.println("Did not work");
                        }
                    }
                });


            }
        });


    }

    public LatLng getLocationFromAddress(Context context, String strAddress) { //Based on the user's createvent address, this will produce latitude and longitude for the provided address.

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
            System.out.printf("it worked but the address is wrong");
        }

        return p1;
    }

    public String getUserID(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return  Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
