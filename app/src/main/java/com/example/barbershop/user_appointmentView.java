package com.example.barbershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class user_appointmentView extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth auth;
    FirebaseUser user;

    LinearLayout linFrame;
    TextView noOrder_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointment_view);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        String userID = auth.getCurrentUser().getUid();

        linFrame = findViewById(R.id.layout_userApp);
        noOrder_text = findViewById(R.id.noOrder_view);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(R.id.layout_userApp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF9800"));
        }

//        int numberOfFrameLayouts = 10;
//        for (int i = 0; i < numberOfFrameLayouts; i++) {
//            FrameLayout frameLayout = new FrameLayout(this);
//            frameLayout.setId(View.generateViewId());
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
//            params.setMargins(0, 0, 0, margin);
//            frameLayout.setLayoutParams(params);
//            linearLayout.addView(frameLayout);
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(frameLayout.getId(), new user_booking());
//            fragmentTransaction.commit();
//        }
//        setContentView(linearLayout);

        final List<Map<String, Object>> appointments = new ArrayList<>();
        fStore.collection("appointments")
                .whereEqualTo("userUID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("data test:::::", document.getId() + " => " + document.getData());
                                appointments.add(document.getData());
                            }

                            int length = appointments.size();
                            Log.d("Appointments length: ", Integer.toString(length));

                            if (length == 0) {
                                linFrame.setVisibility(View.GONE);
                                noOrder_text.setVisibility(View.VISIBLE);
                            } else {
                                for (Map<String, Object> appointment : appointments) {
//                                Object someValue = appointment.get("someKey");
                                    // Do something with the value
//                                Log.d("Appointment data: ", appointment.toString());
                                    Object barberUID = appointment.get("barberUID");
                                    Object appoint_time = appointment.get("startTime");
                                    Object appoint_date = appointment.get("orderDate");
                                    Log.d("Appointment data: ", barberUID.toString());


                                    FrameLayout frameLayout = new FrameLayout(user_appointmentView.this);
                                    frameLayout.setId(View.generateViewId());
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    );
                                    int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
                                    params.setMargins(0, 0, 0, margin);
                                    frameLayout.setLayoutParams(params);
                                    linearLayout.addView(frameLayout);

                                    Fragment fragment_userBooking = new user_booking();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("barberID", barberUID.toString());
                                    bundle.putString("appoint_time", appoint_time.toString());
                                    bundle.putString("appoint_date", appoint_date.toString());

                                    fragment_userBooking.setArguments(bundle);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(frameLayout.getId(), fragment_userBooking);
                                    fragmentTransaction.commit();
                                }
                                setContentView(linearLayout);
                            }
                        } else {
                            Log.d("data test:::::", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


    }

    //FragmentManager fragmentManager = getSupportFragmentManager();
    //        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    //        fragmentTransaction.replace(R.id.fragment_frame,fragment);
    //        fragmentTransaction.commit();
