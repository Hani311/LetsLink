package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.UTFDataFormatException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.CalendarContract.CalendarCache.URI;

public class UserProfile extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1001;
    private CircleImageView civ;
    private CircleImageView profileChangeBtn;
    private View view;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    private String myUri="";
    private StorageTask upLoadTask;
    private StorageReference storageProfilePicsRef;
    private Button saveBtn, logoutBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
         databaseReference= FirebaseDatabase.getInstance().getReference().child("User");
         storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");
        civ= findViewById(R.id.profile_image);
        saveBtn=findViewById(R.id.btnSave);
         profileChangeBtn= findViewById(R.id.profile_image);
        saveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { uploadProfileImage();
            }
        });
        profileChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

          CropImage.activity().setAspectRatio(1,1).start(UserProfile.this);
            }
        });

    getUserinfo();
    }

    private void getUserinfo(){
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&  snapshot.getChildrenCount() > 0){
                    if(snapshot.hasChild("image")){
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(civ);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadProfileImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait, while we are setting your data");
        progressDialog.show();
        if(imageUri !=null){
            final StorageReference fileRef= storageProfilePicsRef
                    .child(mAuth.getCurrentUser().getUid()+".jpg");
            upLoadTask =fileRef.putFile(imageUri);

            upLoadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if( task.isSuccessful())
                    {
                        Uri downloadUrl= task.getResult();
                        myUri = downloadUrl.toString();
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", myUri);
                        databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
                        progressDialog.dismiss();
                    }
                }
            });

        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this,"Image not selected",Toast.LENGTH_SHORT).show();
        }

    }


    public void addUserProfileFromGallery(){

        civ = (CircleImageView) view.findViewById(R.id.profileImage);


        civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted, ask for permission
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }else {

                        chooseImageFromGallery();

                    }
            }
        });
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE: {
                if (grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageFromGallery();
                }else{
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }*/





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode  == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE && resultCode==  RESULT_OK && data != null){
            //CropImage.activity().setAspectRatio(1,1).start(UserProfile.this);

            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri= result.getUri();
            civ.setImageURI(imageUri);
        }
        else {
            Toast.makeText(this,"Error, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    public void editUserName(){
        TextView current = view.findViewById(R.id.editTextTextPersonName);
        String text = (String) current.getText();
        Button saveButton = view.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(current.getText() == text)){
                    System.out.println("Hello");
                }
            }
        });
    }

    public void backToLoginPage(){
        Button button = view.findViewById(R.id.logout);
        Intent intent = new Intent(); //add (this, nameOfClass.class)
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

    }
}