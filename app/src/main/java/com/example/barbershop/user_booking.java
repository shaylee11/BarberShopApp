package com.example.barbershop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link user_booking#newInstance} factory method to
 * create an instance of this fragment.
 */
public class user_booking extends Fragment {

    View view;
    TextView barberName_text,orderDate_text,orderTime_text;
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
    private String userAppoint_time;
    private String appoint_date;

    public user_booking() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment user_booking.
     */
    // TODO: Rename and change types and number of parameters
    public static user_booking newInstance(String param1, String param2) {
        user_booking fragment = new user_booking();
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
            userAppoint_time = getArguments().getString("appoint_time");
            appoint_date = getArguments().getString("appoint_date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_booking, container, false);

        barberName_text = view.findViewById(R.id.barber_name);
        orderDate_text = view.findViewById(R.id.order_date);
        orderTime_text = view.findViewById(R.id.order_time);
        barberName_text.setText(barberID);
        orderDate_text.setText(appoint_date);
        orderTime_text.setText(userAppoint_time); 

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        DocumentReference documentReferenceBarber = fStore.collection("barbers").document(barberID);
        documentReferenceBarber.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot docBarber = task.getResult();
                    if (docBarber.exists()) {
                        String barberName_db =  docBarber.getData().get("name").toString();
                        barberName_text.setText(barberName_db);
                        orderTime_text.setText(userAppoint_time);
                        try {
                            orderDate_text.setText(turnDaysFirst(appoint_date));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        Log.d("Document", "No data");
                    }


                }
            }
        });

        return view;
    }

    public String turnDaysFirst(String userDate) throws ParseException {
        SimpleDateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = originalFormat.parse(userDate);
        String formattedDate = targetFormat.format(date);

        return formattedDate;

    }

}