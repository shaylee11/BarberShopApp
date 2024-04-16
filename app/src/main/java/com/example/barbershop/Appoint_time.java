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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Appoint_time extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView textView,barberName,date_details_textView;
    String userName;
    Button btn_bookNow,btn_logout;
    AutoCompleteTextView userPickedHour;
    String bookingStartTime;
    LinearLayout userAppointement,userCancel_order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_time);

        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("selected_date");
        String selectedBarber =  intent.getStringExtra("select_barber");
        Log.d("select barber", selectedBarber);
        Log.d("select date", selectedDate);



        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        textView = findViewById(R.id.user_details);
        barberName = findViewById(R.id.barber_details);
        btn_bookNow = findViewById(R.id.bookNow);
        date_details_textView = findViewById(R.id.date_details);
        userPickedHour = findViewById(R.id.hour_choose);
        user = auth.getCurrentUser();
        btn_logout = findViewById(R.id.logout);
        userAppointement = findViewById(R.id.my_appointement);
        userCancel_order = findViewById(R.id.cancel_order);
        String bookingEndTime ="10:00";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF9800"));
        }

        if(user == null) {
            Log.d("callout", "User was null");
        } else {
            String userID = auth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userID);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            Log.d("Document", doc.getData().toString());
                            userName   =  doc.getData().get("fName").toString();
                            textView.setText("Client name:   " + userName);
                        }
                        else {
                            Log.d("Document", "No data");
                        }
                    }
                }
            });

            DocumentReference documentReferenceBarber = fStore.collection("barbers").document(selectedBarber);
            documentReferenceBarber.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot docBarber = task.getResult();
                        if (docBarber.exists()) {
                            Log.d("Document", docBarber.getData().toString());
                            String barberName_db =  docBarber.getData().get("name").toString();
                            barberName.setText("Barber name:   " + barberName_db);
                            date_details_textView.setText("Appointment date:   " + selectedDate);
                        }
                        else {
                            Log.d("Document", "No data");
                        }
                    }
                }
            });

            DocumentReference documentReferenceTime = fStore.collection("workDayTime").document("dayTime");
            documentReferenceTime.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot docTime = task.getResult();
                        if (docTime.exists()) {
                            Log.d("Document", docTime.getData().toString());
                            String startTime = docTime.getData().get("startHour").toString();
                            String endTime = docTime.getData().get("endHour").toString();
                            List<String> times = getAvailableHours(startTime, endTime);

                            final List<Map<String, Object>> appointments = new ArrayList<>();
                            fStore.collection("appointments")
                                    .whereEqualTo("barberUID", selectedBarber).whereEqualTo("orderDate", selectedDate)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    appointments.add(document.getData());
                                                }

                                                int length = appointments.size();
                                                Log.d("Appointments length: ", Integer.toString(length));
                                                if (length != 0) {
                                                    for (Map<String, Object> appointment : appointments) {
                                                        Object orderTimes = appointment.get("startTime");
                                                        Log.d("Appointment data: ", orderTimes.toString());
                                                        times.remove(orderTimes.toString());
                                                    }
                                                }
                                            } else {
                                                Log.d("data test:::::", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });

                            Log.d("selected time", times.get(2));
                            AutoCompleteTextView hourChoose = findViewById(R.id.hour_choose);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Appoint_time.this, android.R.layout.simple_list_item_1, times);
                            hourChoose.setAdapter(adapter);
                        }
                        else {
                            Log.d("Document", "No data");
                        }
                    }
                }
            });

            btn_bookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference documentReferenceWrite = fStore.collection("appointments").document();
                    Map<String,Object> userMap = new HashMap<>();
                    userMap.put("startTime", bookingStartTime);
                    userMap.put("endTime", addTime(bookingStartTime,20));
                    userMap.put("userName", userName);
                    userMap.put("userUID", userID);
                    userMap.put("barberUID", selectedBarber);
                    userMap.put("orderDate",selectedDate);
                    documentReferenceWrite.set(userMap);

                    Intent intent = new Intent(getApplicationContext(), orderBookedPage.class);
                    startActivity(intent);
                    finish();
                }
            });

            userPickedHour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    bookingStartTime = (String) parent.getItemAtPosition(position);
                    btn_bookNow.setEnabled(true);
                    btn_bookNow.setBackgroundColor(Color.parseColor("#FF9800"));
                    // Do something with selectedHour
                    Log.d("userTimePick", bookingStartTime);
                }
            });

            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            });

            userAppointement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), user_appointmentView.class);
                    startActivity(intent);
                    finish();
                }
            });

            userCancel_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), user_cancelOrder.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }



    public static List<String> getAvailableHours(String startHour, String endHour) {
        int startHourInt = Integer.parseInt(startHour.split(":")[0]);
        int startMinuteInt = Integer.parseInt(startHour.split(":")[1]);
        int endHourInt = Integer.parseInt(endHour.split(":")[0]);
        int endMinuteInt = Integer.parseInt(endHour.split(":")[1]);

        List<String> availableHours = new ArrayList<>();

        for (int hour = startHourInt; hour <= endHourInt; hour++) {
            for (int minute = 0; minute < 60; minute += 20) {
                if (hour == endHourInt && minute >= endMinuteInt) {
                    break;
                }
                if (hour == startHourInt && minute < startMinuteInt) {
                    continue;
                }
                availableHours.add(String.format("%02d:%02d", hour, minute));
            }
        }

        return availableHours;
    }

    public static String addTime(String time, int minutes) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return sdf.format(cal.getTime());
    }
}