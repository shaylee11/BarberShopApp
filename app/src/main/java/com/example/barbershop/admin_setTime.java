package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class admin_setTime extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth auth;
    FirebaseUser user;
    TextInputEditText editText_startTime, editText_endTime;
    Button btn_submitTime;
    TextView returnMenu_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_set_time);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        editText_startTime = findViewById(R.id.startTime);
        editText_endTime = findViewById(R.id.endTime);
        btn_submitTime = findViewById(R.id.btn_submit);
        returnMenu_text = findViewById(R.id.returnMenu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF9800"));
        }

        btn_submitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start_time,end_time;
                start_time = String.valueOf(editText_startTime.getText());
                end_time = String.valueOf(editText_endTime.getText());

                DocumentReference documentReference = fStore.collection("workDayTime").document("dayTime");
                Map<String,Object> userMap = new HashMap<>();
                userMap.put("startHour", start_time);
                userMap.put("endHour", end_time);
                documentReference.set(userMap);

                Intent intent = new Intent(getApplicationContext(), admin_timeBooking.class);
                startActivity(intent);
                finish();
            }
        });

        returnMenu_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), adminMainScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }
}