package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.example.androidproject.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.NavigationUI;

//import com.example.androidproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity  {

    OnSwipeTouchListener onSwipeTouchListener;
    static NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.myNavHostFragment);
        //DataBindingUtil binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflator, R.layout.fragment_title, container, false)
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //NavController navController = Navigation.findNavController(this, R.id.myNavHostFragment);
        //onSwipeTouchListener = new OnSwipeTouchListener(this, findViewById(R.id.myNavHostFragment));

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


    }
