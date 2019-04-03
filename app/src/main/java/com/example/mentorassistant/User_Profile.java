package com.example.mentorassistant;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Profile extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "User_Profile";

    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    TextView email;
    TextView name;
    TextView phone;
    TextView city;
    TextView institution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

        email = findViewById(R.id.email);
        name = findViewById(R.id.username);
        phone = findViewById(R.id.phoneNumber);
        institution = findViewById(R.id.institution);
        city = findViewById(R.id.city);


        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String myUserName = dataSnapshot.child("username").getValue().toString();
                    String myEmail = dataSnapshot.child("email").getValue().toString();
                    String myPhone = dataSnapshot.child("phone").getValue().toString();
                    String myInstitution = dataSnapshot.child("institution").getValue().toString();
                    String myCity = dataSnapshot.child("city").getValue().toString();


                    name.setText("Пользователь :  " + myUserName);
                    email.setText("Пошта : " + myEmail);
                    phone.setText("Телефон : " + myPhone);
                    city.setText("Город : " + myCity);
                    institution.setText("Учреждение : " + myInstitution);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {

    }


}
