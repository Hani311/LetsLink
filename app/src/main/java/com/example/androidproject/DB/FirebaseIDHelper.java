package com.example.androidproject.DB;

import com.example.androidproject.Notifications.NotifToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseIDHelper extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        FirebaseUser fBU= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken=FirebaseInstanceId.getInstance().getToken();
        if(fBU!=null){
            updateToken(refreshToken);
        }
    }

    private void updateToken(String refreshToken) {
        FirebaseUser fBU= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        NotifToken token= new NotifToken(refreshToken);
        reference.child(fBU.getUid()).setValue(token);

    }
}
