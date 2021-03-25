package com.example.androidproject.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1001;
    private CircleImageView civ;
    private TextView profileChangeBtn;
    private View view;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    private String myUri="";
    private StorageTask upLoadTask;
    private StorageReference storageProfilePicsRef;
    private Button saveBtn, logoutBtn, btnChangePasActivity;
    private FirebaseUser User;
    private EditText editTextChangeUserName, editTextStatus;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("imageURL");
        civ= findViewById(R.id.profile_image);
        saveBtn=findViewById(R.id.btnSave);
        profileChangeBtn= findViewById(R.id.changePic);
        btnChangePasActivity=findViewById(R.id.btnResetActivity);
        editTextStatus=findViewById(R.id.editTextStatus);
        editTextChangeUserName= findViewById(R.id.editTextTextPersonName);


        saveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextChangeUserName.getText().toString();
                String status = editTextStatus.getText().toString();
                if( name.equals("")) {

                } else{

                    databaseReference.child(mAuth.getCurrentUser().getUid()).child("Users").setValue(name);
                    databaseReference.child(mAuth.getCurrentUser().getUid()).child("searchname").setValue(name);
                    Toast.makeText(UserProfile.this, "UserName have been changed", Toast.LENGTH_SHORT).show();
                }
                if( status.equals("")) {

                } else{

                    databaseReference.child(mAuth.getCurrentUser().getUid()).child("bio").setValue(status);
                    Toast.makeText(UserProfile.this, "Status have been changed", Toast.LENGTH_SHORT).show();
                }

                uploadProfileImage();


            }
        });
        profileChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  chooseImageFromGallery()
                Intent openGalleryIntent =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });
        btnChangePasActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent= new Intent(UserProfile.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });


        getUserinfo();
    }

    public void getUserinfo(){
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&  snapshot.getChildrenCount() > 0){
                    if(snapshot.hasChild("imageURL")){
                        String image = snapshot.child("imageURL").getValue().toString();
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
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl= task.getResult();
                        myUri = downloadUrl.toString();
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("imageURL", myUri);
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode  == 1000){
            if(resultCode== Activity.RESULT_OK){
                imageUri = data.getData();
                civ.setImageURI(imageUri);
            }
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