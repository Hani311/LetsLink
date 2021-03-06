package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.MenuInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Double.parseDouble

class PopInChatUserActivity : AppCompatActivity() {

    lateinit var dM : DisplayMetrics
    lateinit var popUpUser:String
    lateinit var reference:DatabaseReference
    lateinit var popUpCiv:CircleImageView
    lateinit var popUpUsername:TextView
    lateinit var backImage:ImageView
    lateinit var optionsImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_in_chat_user)

        popUpCiv=findViewById(R.id.friends_profile_image_popup)
        popUpUsername=findViewById(R.id.friends_username_popup)
        backImage=findViewById(R.id.popup_back)
        optionsImage=findViewById(R.id.popup_options)




        optionsImage.setOnClickListener {
            showPopup(it)
        }

        val intent=intent
        popUpUser= intent.getStringExtra("userid")!!

        reference = FirebaseDatabase.getInstance().getReference("Users").child(popUpUser)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)!!
                popUpUsername.setText(user.username)
                if (user.imageURL == "default") {
                    popUpCiv.setImageResource(R.mipmap.ic_launcher_round)
                } else {
                    Glide.with(applicationContext).load(user.imageURL).into(popUpCiv)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        dM= DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dM)

        val width=dM.widthPixels
        val height=dM.heightPixels

        window.setLayout((width).toInt(), (height*.5).toInt())

        var params:WindowManager.LayoutParams =window.attributes
        params.gravity=Gravity.TOP
        params.x=0
        params.y=+20

        window.attributes=params


        backImage.setOnClickListener {
            onBackPressed()
        }
    }


    fun showPopup(v : View){
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.inchat_options_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.block_options-> {

                }
                R.id.mute_notifs-> {

                }
            }
            true
        }
        popup.show()
    }

}