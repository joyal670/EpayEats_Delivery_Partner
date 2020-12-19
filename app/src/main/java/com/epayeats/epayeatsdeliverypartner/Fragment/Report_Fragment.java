package com.epayeats.epayeatsdeliverypartner.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsdeliverypartner.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Report_Fragment extends Fragment
{
    TextView selectedDate_From_admn, selectedDate_To_admn;
    Button search_generate_coadmin_report;

    public ProgressDialog progressDialog;

    TextView coadmin_report_totaldeliverd, coadmin_report_km, coadmin_report_charge;

    String fromDate;
    String toDate;

    SharedPreferences sharedPreferences;
    String myid;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date fromD = new Date();
    Date toD = new Date();

    int report_totaldeliverd = 0;

    int delivCharge;
    int delivtotal = 0;
    String status2 = "2";

    Button deliveryboy_report_from_date;

    public Report_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_, container, false);

        sharedPreferences = getContext().getSharedPreferences("data", 0);
        myid = sharedPreferences.getString("userid", "");


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");


        selectedDate_From_admn = view.findViewById(R.id.selectedDate_From_deliveryboy);
        selectedDate_To_admn = view.findViewById(R.id.selectedDate_To_deliveryboy);
        search_generate_coadmin_report = view.findViewById(R.id.search_generate_deliveryboy_report);
        deliveryboy_report_from_date = view.findViewById(R.id.deliveryboy_report_from_date);

        coadmin_report_charge = view.findViewById(R.id.deliveryboy_report_delivery_charge);
        coadmin_report_totaldeliverd = view.findViewById(R.id.deliveryboy_report_totaldeliverd);
        coadmin_report_km = view.findViewById(R.id.deliveryboy_report_km);

        search_generate_coadmin_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a1 = selectedDate_From_admn.getText().toString();
                String a2 = selectedDate_To_admn.getText().toString();
                if (a1.isEmpty() || a2.isEmpty()) {
                    Toast.makeText(getContext(), "Select Date", Toast.LENGTH_SHORT).show();
                } else {
                    generateReport(a1, a2);
                }
            }
        });

        CalendarConstraints.Builder conBuilder = new CalendarConstraints.Builder();
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Date");
        builder.setCalendarConstraints(conBuilder.build());
        final MaterialDatePicker materialDatePicker = builder.build();
        deliveryboy_report_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                materialDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection)
            {
                Pair selectedDates = (Pair) materialDatePicker.getSelection();

                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));

                Date startDate = rangeDate.first;
                Date endDate = rangeDate.second;

                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate_From_admn.setText(simpleFormat.format(startDate));
                selectedDate_To_admn.setText(simpleFormat.format(endDate));
            }
        });

        return view;
    }

    private void generateReport(String a1, String a2)
    {
        String tem1 = a1;
        String tem2 = a2;


        try {
            fromD = dateFormat.parse(a1);
            toD = dateFormat.parse(a2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        report_totaldeliverd = 0;
        coadmin_report_totaldeliverd.setText("0");
        coadmin_report_charge.setText("0");

        delivtotal = 0;
        delivCharge = 0;
        coadmin_report_km.setText("0");


        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("order_data");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                progressDialog.dismiss();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    if (myid.equals(snapshot1.child("deliveryBodID").getValue().toString()))
                    {
                        String d1 = snapshot1.child("orderDate").getValue().toString();

                        try {
                            Date cd = dateFormat.parse(d1);

                            if (fromD.before(cd) && cd.before(toD))
                            {
                                if (status2.equals(snapshot1.child("orderStatus").getValue().toString()))
                                {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("delivery_boy").child(myid);
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot)
                                        {
                                            String charge = snapshot.child("deliveryBoyDeliveryCharge").getValue().toString();


                                            coadmin_report_charge.setText(charge);

                                            delivCharge = Integer.parseInt(charge);
                                            int cg = delivCharge;

                                            delivtotal = delivtotal + cg;

                                            coadmin_report_km.setText(delivtotal + "");


                                            report_totaldeliverd = report_totaldeliverd + 1;
                                            coadmin_report_totaldeliverd.setText(report_totaldeliverd + "");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}