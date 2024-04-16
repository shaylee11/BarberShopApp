package com.example.barbershop;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Calender_picker#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calender_picker extends Fragment {

    View view;
    CalendarView picked_cal;
    Button btn_pick;
    private String date;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    FirebaseUser user;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String barberID;


    public Calender_picker() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calender_picker.
     */
    // TODO: Rename and change types and number of parameters
    public static Calender_picker newInstance(String param1, String param2) {
        Calender_picker fragment = new Calender_picker();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            barberID = getArguments().getString("barberID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_calender_picker, container, false);

        Log.d("barberId number!", barberID);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        picked_cal = view.findViewById(R.id.picked_cal);
        btn_pick = view.findViewById(R.id.btn_pick);

        Calendar minCalendar = Calendar.getInstance();
        minCalendar.setTimeInMillis(System.currentTimeMillis());
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.setTimeInMillis(System.currentTimeMillis());
        maxCalendar.add(Calendar.WEEK_OF_YEAR, 2);
        picked_cal.setMinDate(minCalendar.getTimeInMillis());
        picked_cal.setMaxDate(maxCalendar.getTimeInMillis());


        picked_cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Set the value of the "date" variable to the selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                date = simpleDateFormat.format(calendar.getTime());

                Calendar selectedDay = Calendar.getInstance();
                selectedDay.set(year, month, dayOfMonth);

                if (selectedDay.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    selectedDay.add(Calendar.DATE, 2);
                    picked_cal.setDate(selectedDay.getTimeInMillis());
                }
                if (selectedDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    selectedDay.add(Calendar.DATE, 1);
                    picked_cal.setDate(selectedDay.getTimeInMillis());
                }
            }
        });

        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("date", date);
                Intent intent = new Intent(getActivity(), Appoint_time.class);
                intent.putExtra("selected_date", date);
                intent.putExtra("select_barber", barberID);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return view;
    }
}