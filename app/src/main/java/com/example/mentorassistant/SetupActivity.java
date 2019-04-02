package com.example.mentorassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {

    private TextInputLayout Username, Phone, City, Istitution;
    private Button Save;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference profileUserRef;
    private StorageReference UserProfileImageRef;
    //private Uri ImageUri;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);

        Username = findViewById(R.id.et_username);
        Phone = findViewById(R.id.et_phoneNumber);
        City = findViewById(R.id.city);
        Istitution = findViewById(R.id.institution);
        Save = findViewById(R.id.btn_save);
        loadingBar = new ProgressDialog(this);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAccountSetupInformation();
            }
        });

    }

    private void SaveAccountSetupInformation() {
        String username = Username.getEditText().getText().toString();
        String phone = Phone.getEditText().getText().toString();
        String city = City.getEditText().getText().toString();
        String institution = Istitution.getEditText().getText().toString();


        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Пожалуйста, напишите ваше полное имя ...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Пожалуйста, напишите свой номер телефона...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Пожалуйста, напишите свой город", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(institution)) {
            Toast.makeText(this, "Пожалуйста, укажите своё учебное учреждение ", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Сохранение информации");
            loadingBar.setMessage("Пожалуйста, подождите, пока мы создаем вашу новую учетную запись ...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("phone", phone);
            userMap.put("city", city);
            userMap.put("gender", institution);


            profileUserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        SendUserToMainActivity();
                        Toast.makeText(SetupActivity.this, "Ваша учетная запись успешно создана.", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Произошла ошибка: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }


    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
