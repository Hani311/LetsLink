package com.example.androidproject.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidproject.R;
import com.example.androidproject.Users.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGroupActivity extends AppCompatActivity {

    private static final int STORAGE_REQUEST_CODE=100;
    private static final int CAMERA_REQUEST_CODE=200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE=400;

    private ActionBar actionBar;
    private FirebaseAuth auth;
    private Button createBtn;
    private CircleImageView groupIcon;
    private EditText groupNameInput, groupDescrInput;
    private Uri iconUri=null;
    private String[] camerapermissions;
    private String[] storagepermissions;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        auth=FirebaseAuth.getInstance();
        actionBar=getSupportActionBar();
        actionBar.setTitle("Creating group");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        camerapermissions=new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagepermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        groupIcon=findViewById(R.id.civ_group_create);
        createBtn=findViewById(R.id.create_group_btn);
        groupNameInput=findViewById(R.id.group_name_input);
        groupDescrInput=findViewById(R.id.group_descr_input);

        groupIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGroupIcon();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                beginCreation();
            }
        });

        auth=FirebaseAuth.getInstance();
        checkForUser();
        //sendBtn=findViewById(R.id.);

    }

    private void beginCreation() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating group");
        String groupTitle=groupNameInput.getText().toString().trim();
        String groupDescription=groupDescrInput.getText().toString().trim();

        if(TextUtils.isEmpty(groupTitle)){
            Toast.makeText(this, "Please enter a group title.....",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        String timestamp= String.valueOf(System.currentTimeMillis());

        if(iconUri==null){
            createGroup(timestamp, groupTitle, groupDescription, "default");
        }
        else{

            createGroup(timestamp, groupTitle, groupDescription, String.valueOf(iconUri));
        }
    }

    private void createGroup(String timestamp, String title, String description, String groupIcon){

        HashMap<String, String> hM=new HashMap<>();

        hM.put("groupID", ""+timestamp);
        hM.put("title",  ""+title);
        hM.put("description", ""+ description);
        hM.put("groupIcon", ""+ groupIcon);
        hM.put("timeCreated", ""+ timestamp);
        hM.put("groupAdmin", ""+auth.getCurrentUser().getUid());


        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Groups");
        reference.child(timestamp).setValue(hM)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        HashMap<String, String> hashMap=new HashMap<>();
                        hashMap.put("userID", auth.getCurrentUser().getUid());
                        hashMap.put("role", "admin");
                        hashMap.put("timestamp", timestamp);

                        DatabaseReference participantsReff=FirebaseDatabase.getInstance().getReference("Groups");
                        participantsReff.child(timestamp).child("Participants").child(auth.getCurrentUser().getUid())
                        .setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(CreateGroupActivity.this, "Group Created", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(CreateGroupActivity.this, "Group failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CreateGroupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void selectGroupIcon() {

        String [] options={"Camera", "Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Choose the group icon")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){

                            if(!checkCameraPermissions()){
                                requestCamerPermissions();
                            }
                            else{
                                selectionFromCamera();
                            }
                        }
                        else{

                            if(!checkStoragePermissions()){
                                requestStoragePermissions();
                            }
                            else{
                                selectionFromGallery();
                            }
                        }
                    }
                }).show(); 
    }

    private void selectionFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermissions(){
        boolean result= ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private boolean checkCameraPermissions(){
        boolean result =ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean secondResult=ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result&secondResult;
    }

    private void requestStoragePermissions(){
        ActivityCompat.requestPermissions(this, storagepermissions, STORAGE_REQUEST_CODE);
    }

    private void requestCamerPermissions(){
        ActivityCompat.requestPermissions(this, camerapermissions, CAMERA_REQUEST_CODE);
    }

    private void selectionFromCamera(){
        ContentValues cv=new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Group Icon");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Group Icon Description");
        iconUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iconUri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private void checkForUser() {
        FirebaseUser user = auth.getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("User");
        
        if(user!=null){
            actionBar.setSubtitle("username");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{

                if(grantResults.length>0) {
                    boolean cameraAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    boolean storagAccepted = (grantResults[1] == PackageManager.PERMISSION_GRANTED);

                    if (cameraAccepted & storagAccepted) {
                        selectionFromCamera();
                    } else {
                        Toast.makeText(this, "Camera & Storage permissions not granted", Toast.LENGTH_SHORT).show();
                    }

                }
            }
            break;

            case STORAGE_REQUEST_CODE:{

                if(grantResults.length>0) {
                    boolean storagAccepted = (grantResults[1] == PackageManager.PERMISSION_GRANTED);

                    if (storagAccepted) {
                        selectionFromCamera();
                    } else {
                        Toast.makeText(this, "Storage permissions not granted", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode==RESULT_OK){
            if(requestCode==IMAGE_PICK_GALLERY_CODE){
                iconUri=data.getData();
                groupIcon.setImageURI(iconUri);
            }
            else if(requestCode==IMAGE_PICK_CAMERA_CODE){
                iconUri=data.getData();
                groupIcon.setImageURI(iconUri);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


}