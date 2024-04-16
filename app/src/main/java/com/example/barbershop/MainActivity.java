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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button btn_logout;
    TextView textView;
    FirebaseUser user;
    FirebaseFirestore fStore;
    String userName;
//    CalendarView picked_cal;
//    Button btn_pick;
    private String date;
    LinearLayout view_barber1,view_barber2,view_barber3,view_barber4,view_barber5,view_barber6;
    FrameLayout frameLayout;
    LinearLayout my_appointment_btn;
    LinearLayout cancel_order_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        btn_logout = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        frameLayout = findViewById(R.id.fragment_frame);
        my_appointment_btn = findViewById(R.id.my_appointment);
        cancel_order_btn = findViewById(R.id.cancel_order);

        view_barber1 = findViewById(R.id.barber1);
        view_barber2 = findViewById(R.id.barber2);
        view_barber3 = findViewById(R.id.barber3);
        view_barber4 = findViewById(R.id.barber4);
        view_barber5 = findViewById(R.id.barber5);
        view_barber6 = findViewById(R.id.barber6);

//        picked_cal = findViewById(R.id.picked_cal);
//        btn_pick = findViewById(R.id.btn_pick);

        if(user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
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
                            textView.setText("Hello " + userName);
                        }
                        else {
                            Log.d("Document", "No data");
                        }
                    }
                }
            });
        }

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF9800"));
        }

//        replaceFragment(new Calender_picker());

        view_barber1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barberFunction("barber1");
            }
        });

        view_barber2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barberFunction("barber2");
            }
        });

        view_barber3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barberFunction("barber3");
            }
        });

        view_barber4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barberFunction("barber4");
            }
        });

        view_barber5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barberFunction("barber5");
            }
        });

        view_barber6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barberFunction("barber6");
            }
        });

        my_appointment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), user_appointmentView.class);
                startActivity(intent);
                finish();
            }
        });

         cancel_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), user_cancelOrder.class);
                startActivity(intent);
                finish();
            }
        });





//        picked_cal = findViewById(R.id.picked_cal);
//        btn_pick = findViewById(R.id.btn_pick);

//        Calendar minCalendar = Calendar.getInstance();
//        minCalendar.setTimeInMillis(System.currentTimeMillis());
//        Calendar maxCalendar = Calendar.getInstance();
//        maxCalendar.setTimeInMillis(System.currentTimeMillis());
//        maxCalendar.add(Calendar.WEEK_OF_YEAR, 2);
//        picked_cal.setMinDate(minCalendar.getTimeInMillis());
//        picked_cal.setMaxDate(maxCalendar.getTimeInMillis());
//
//
//        picked_cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//                // Set the value of the "date" variable to the selected date
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year, month, dayOfMonth);
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
//                date = simpleDateFormat.format(calendar.getTime());
//
//                Calendar selectedDay = Calendar.getInstance();
//                selectedDay.set(year, month, dayOfMonth);
//
//                if (selectedDay.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
//                    selectedDay.add(Calendar.DATE, 2);
//                    picked_cal.setDate(selectedDay.getTimeInMillis());
//                }
//                if (selectedDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
//                    selectedDay.add(Calendar.DATE, 1);
//                    picked_cal.setDate(selectedDay.getTimeInMillis());
//                }
//            }
//        });
//
//        btn_pick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("date", date);
//                Intent intent = new Intent(getApplicationContext(), Appoint_time.class);
//                startActivity(intent);
//                finish();
//            }
//        });



    }

    private void replaceFragment (Fragment fragment, String barberID){

        Bundle bundle = new Bundle();
        bundle.putString("barberID", barberID);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame,fragment);
        fragmentTransaction.commit();
    }

    public void barberFunction (String barberID){
        frameLayout.setVisibility(View.VISIBLE);
        replaceFragment(new Calender_picker(),barberID);
    }
}