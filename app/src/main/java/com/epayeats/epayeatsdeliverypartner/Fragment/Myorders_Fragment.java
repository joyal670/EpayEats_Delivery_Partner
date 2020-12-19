package com.epayeats.epayeatsdeliverypartner.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.epayeats.epayeatsdeliverypartner.Adapter.DelivredOrderAdapter;
import com.epayeats.epayeatsdeliverypartner.Adapter.OrderDataAdapter;
import com.epayeats.epayeatsdeliverypartner.Model.orderModel;
import com.epayeats.epayeatsdeliverypartner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Myorders_Fragment extends Fragment implements DelivredOrderAdapter.OnitemClickListener
{
    RecyclerView deliveredOrdrs_Recyclerview;
    SharedPreferences sharedPreferences;
    String a1;
    DatabaseReference databaseReference;
    List<orderModel> mOrderModel;
    DelivredOrderAdapter mOrderDataAdapter;
    public ProgressDialog progressDialog;
    String status1 = "2";

    public Myorders_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myorders_, container, false);

        deliveredOrdrs_Recyclerview = view.findViewById(R.id.deliveredOrdrs_Recyclerview);
        databaseReference = FirebaseDatabase.getInstance().getReference("order_data");

        sharedPreferences = getContext().getSharedPreferences("data", 0);
        a1 = sharedPreferences.getString("userid", "");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");

        loadData();

        return view;
    }

    private void loadData()
    {
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                progressDialog.dismiss();
                mOrderModel.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if (a1.equals(dataSnapshot1.child("deliveryBodID").getValue().toString()))
                    {
                        if(status1.equals(dataSnapshot1.child("orderStatus").getValue().toString()))
                        {
                            orderModel upload = dataSnapshot1.getValue(orderModel.class);
                            mOrderModel.add(upload);
                        }

                    }
                }
                mOrderDataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mOrderModel = new ArrayList<>();
        mOrderDataAdapter = new DelivredOrderAdapter(getContext(), mOrderModel);
        deliveredOrdrs_Recyclerview.setAdapter(mOrderDataAdapter);
        mOrderDataAdapter.setOnClickListener(Myorders_Fragment.this);
    }

    @Override
    public void onItemClick(int position)
    {
    }
}