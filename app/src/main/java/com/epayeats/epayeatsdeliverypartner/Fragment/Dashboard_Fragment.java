package com.epayeats.epayeatsdeliverypartner.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.epayeats.epayeatsdeliverypartner.Activity.Sample_Activity;
import com.epayeats.epayeatsdeliverypartner.Adapter.OrderDataAdapter;
import com.epayeats.epayeatsdeliverypartner.Model.orderModel;
import com.epayeats.epayeatsdeliverypartner.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Dashboard_Fragment extends Fragment implements OrderDataAdapter.OnitemClickListener
{
    SwitchMaterial switchStatus;
    SharedPreferences sharedPreferences;
    String a1;

    RecyclerView myOrderListView;
    DatabaseReference databaseReference;
    List<orderModel> mOrderModel;
    OrderDataAdapter mOrderDataAdapter;
    public ProgressDialog progressDialog;
    String status1 = "1";

    public Dashboard_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_, container, false);

        myOrderListView = view.findViewById(R.id.myOrderListView);
        databaseReference = FirebaseDatabase.getInstance().getReference("order_data");

        sharedPreferences = getContext().getSharedPreferences("data", 0);
        a1 = sharedPreferences.getString("userid", "");

        switchStatus= view.findViewById(R.id.switchStatus);
        switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                changeStatus(isChecked);
            }
        });

        if(switchStatus.isChecked())
        {
            myOrderListView.setVisibility(View.VISIBLE);
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");

        loadStatus();

        loadOrders();

//        dashboard_report_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent( getContext(), deliveryBoyReportdetails_Activity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }

    private void loadStatus()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("delivery_boy").child(a1);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String sta = snapshot.child("onlineorOffline").getValue().toString();
                String on = "online";
                if(on.equals(sta))
                {
                    switchStatus.setChecked(true);
                    switchStatus.setText("Online");
                }
                else
                {
                    switchStatus.setChecked(false);
                    switchStatus.setText("Offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOrders()
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
        mOrderDataAdapter = new OrderDataAdapter(getContext(), mOrderModel);
        myOrderListView.setAdapter(mOrderDataAdapter);
        mOrderDataAdapter.setOnClickListener(Dashboard_Fragment.this);
    }

    private void changeStatus(boolean isChecked)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("delivery_boy").child(a1);

        if(isChecked)
        {
            switchStatus.setText("Online");
            myOrderListView.setVisibility(View.VISIBLE);
            ref.child("onlineorOffline").setValue("online");
        }
        else
        {
            switchStatus.setText("Offline");
            myOrderListView.setVisibility(View.GONE);
            ref.child("onlineorOffline").setValue("offline");
        }
    }


    @Override
    public void onItemClick(int position)
    {
        Intent intent = new Intent(getContext(), Sample_Activity.class);
        intent.putExtra("orderID", mOrderModel.get(position).getOrderID());
        intent.putExtra("menuID", mOrderModel.get(position).getMenuID());
        intent.putExtra("menuName", mOrderModel.get(position).getMenuName());
        intent.putExtra("menuImage", mOrderModel.get(position).getMenuImage());

        intent.putExtra("mainCatagoryID", mOrderModel.get(position).getMainCatagoryID());
        intent.putExtra("mainCatagoryName", mOrderModel.get(position).getMainCatagoryName());
        intent.putExtra("subCatagoryID", mOrderModel.get(position).getSubCatagoryID());
        intent.putExtra("subCatagoryName", mOrderModel.get(position).getSubCatagoryName());

        intent.putExtra("localAdminID", mOrderModel.get(position).getLocalAdminID());

        intent.putExtra("offerPrice", mOrderModel.get(position).getOfferPrice());
        intent.putExtra("sellingPrice", mOrderModel.get(position).getSellingPrice());
        intent.putExtra("actualPrice", mOrderModel.get(position).getActualPrice());


        intent.putExtra("orderDate", mOrderModel.get(position).getOrderDate());
        intent.putExtra("orderTime", mOrderModel.get(position).getOrderTime());

        intent.putExtra("qty", mOrderModel.get(position).getQty());
        intent.putExtra("totalPrice", mOrderModel.get(position).getTotalPrice());

        intent.putExtra("house", mOrderModel.get(position).getHouse());
        intent.putExtra("area", mOrderModel.get(position).getArea());
        intent.putExtra("city", mOrderModel.get(position).getCity());
        intent.putExtra("pincode", mOrderModel.get(position).getPincode());
        intent.putExtra("cName", mOrderModel.get(position).getcName());
        intent.putExtra("cPhone", mOrderModel.get(position).getcPhone());
        intent.putExtra("cAltPhone", mOrderModel.get(position).getcAltPhone());

        intent.putExtra("userID", mOrderModel.get(position).getUserID());
        intent.putExtra("userLocation", mOrderModel.get(position).getUserLocation());
        intent.putExtra("user_lat", mOrderModel.get(position).getUserLatitude());
        intent.putExtra("user_long", mOrderModel.get(position).getUserLongitude());

        intent.putExtra("restID", mOrderModel.get(position).getRestID());
        intent.putExtra("restName", mOrderModel.get(position).getRestName());

        intent.putExtra("status", mOrderModel.get(position).getOrderStatus());
        startActivity(intent);
    }
}