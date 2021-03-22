package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.androidproject.Notifications.NotifToken;
import com.example.androidproject.Users.User;
import com.example.androidproject.databinding.ActivityMainBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.example.androidproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity  {

    OnSwipeTouchListener onSwipeTouchListener;
    static NavHostFragment navHostFragment;
    DatabaseReference reference;
    FirebaseUser fBU;
    ArrayList<Fragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragments=new ArrayList<>();

        //navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.myNavHostFragment);
        //DataBindingUtil binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflator, R.layout.fragment_title, container, false)
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //NavController navController = Navigation.findNavController(this, R.id.myNavHostFragment);
        //onSwipeTouchListener = new OnSwipeTouchListener(this, findViewById(R.id.myNavHostFragment));

        CircleImageView cIV = binding.profileImage;
        TextView username=binding.usernameDisplay;
        Toolbar toolbar=binding.toolBar;
        AppBarLayout layout=binding.appBarLayout;

        Fade fade = new Fade();
        View decor=getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.nav_host_fragment), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(enterTransition());
            getWindow().setSharedElementExitTransition(returnTransition());
            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }



        //generate key-hash for the developer facebook
        //make sure to change the packageName
        //key-hash will be in logcat
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.androidproject",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }



        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
            layout.setElevation(0);
        }

        fBU=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(fBU.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());

                if(user.getImageURL().equals("default")) {
                    cIV.setImageResource(R.mipmap.ic_launcher_round);
                }
                else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(cIV);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        BottomNavigationView navView = binding.navView;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavigationUI.setupWithNavController(navView, navHostFragment.getNavController());

        }

        /*
        BottomNavigationView.OnNavigationItemSelectedListener navListener=
                new BottomNavigationView.OnNavigationItemSelectedListener(){

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment=null;

                        switch(item.getItemId()){

                            case R.id.home:
                                selectedFragment=new TitleFragment();
                                break;

                            case R.id.messages:
                                selectedFragment=new MessagesFragment();
                                break;

                            case R.id.notifications:
                                selectedFragment=new NotificationFragment();
                                break;

                            case R.id.maps:
                                selectedFragment=new MessagesFragment();
                                break;

                            case R.id.userProfile:
                                selectedFragment=new userProfileSettingsFragment();
                                break;

                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_container, selectedFragment).commit();
                        return true;
                    }
                };

         */
        //binding.navigationView.setOnNavigationItemSelectedListener(navListener);

        /*
        binding.messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navHostFragment.getNavController().navigate(R.id.action_titleFragment2_to_messagesFragment);
            }
        });

        binding.mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navHostFragment.getNavController().navigate(R.id.);
            }
        });

        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              //navHostFragment.getNavController().navigate();
          }

      });



        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               navController.navigate(R.id.action_titleFragment2_to_userProfile);
            }
        });
         */


        updateToken(FirebaseInstanceId.getInstance().getToken());

        binding.getRoot();
        //setContentView(R.layout.activity_main);
    }

    public static class OnSwipeTouchListener implements View.OnTouchListener {


        private final GestureDetector gestureDetector;
        Context context;

        OnSwipeTouchListener(Context ctx, View mainView) {
            gestureDetector = new GestureDetector(ctx, new GestureListener(mainView));
            mainView.setOnTouchListener(this);
            context = ctx;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }


        public class GestureListener extends
                GestureDetector.SimpleOnGestureListener {


            private View view;
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            public GestureListener(View mainView) {
                this.view=mainView;
            }


            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight(view);
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }
        void onSwipeRight(View view) {
            navigatToMessages(view);
            Toast.makeText(context, "Swiped Right", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeRight();
        }

        private void navigatToMessages(View view) {

            //Navigation.findNavController(view).navigate(R.id.action_titleFragment2_to_messagesFragment);

            /*
            NavDirections action =
                    SpecifyAmountFragmentDirections
                            .actionSpecifyAmountFragmentToConfirmationFragment();
            Navigation.findNavController(view).navigate(action);

             */
        }

        void onSwipeLeft() {

            Toast.makeText(context, "Swiped Left", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeLeft();
        }
        void onSwipeTop() {
            Toast.makeText(context, "Swiped Up", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeTop();
        }
        void onSwipeBottom() {
            Toast.makeText(context, "Swiped Down", Toast.LENGTH_SHORT).show();
            this.onSwipe.swipeBottom();
        }
        interface onSwipeListener {
            void swipeRight();
            void swipeTop();
            void swipeBottom();
            void swipeLeft();
        }
        onSwipeListener onSwipe;
        }

        private void configStatus(String status){

            reference=FirebaseDatabase.getInstance().getReference("Users").child(fBU.getUid());

            SharedPreferences sp=getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("Current_USERID", fBU.getUid());
            editor.apply();

            HashMap<String, Object> hM=new HashMap<>();
            hM.put("status", status);

            reference.updateChildren(hM);

        }

     public void updateToken(String token){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Tokens");
        NotifToken mToken=new NotifToken(token);
        ref.child(fBU.getUid()).setValue(mToken);
     }

    @Override
    protected void onResume() {
        super.onResume();
        configStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        configStatus("offline");
    }

    private Transition enterTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setDuration(200);

        return bounds;
    }

    private Transition returnTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setInterpolator(new DecelerateInterpolator());
        bounds.setDuration(200);

        return bounds;
    }
}
