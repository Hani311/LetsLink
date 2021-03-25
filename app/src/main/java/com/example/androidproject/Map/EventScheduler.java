package com.example.androidproject.Map;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.androidproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.TimerTask;

import static com.facebook.appevents.AppEventsLogger.getUserID;

public class EventScheduler extends TimerTask {

    private final GoogleMap gMap;

    public EventScheduler(GoogleMap gMap) {
        this.gMap = gMap;
    }

    @Override
    public void run() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");
        DatabaseReference MemberCountRef = FirebaseDatabase.getInstance().getReference("Joined Member");
        DatabaseReference joinedMemberRef = FirebaseDatabase.getInstance().getReference("Joined Users");
        DatabaseReference groupChatRef = FirebaseDatabase.getInstance().getReference("Groups");
                    reference.removeValue();
                    MemberCountRef.removeValue();
                    joinedMemberRef.removeValue();
                    groupChatRef.removeValue();
                     gMap.clear();
                  // MapsFragment m = new MapsFragment();
                 //  m.spawnNearbyEventsOnMap(gMap);


    }
}
