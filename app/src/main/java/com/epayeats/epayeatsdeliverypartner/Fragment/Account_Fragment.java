package com.epayeats.epayeatsdeliverypartner.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsdeliverypartner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Account_Fragment extends Fragment
{
    TextView account_name, account_email, account_id, account_phone, account_licence, account_vechileno;
    TextView account_local_admin, account_deliveryCharge;
    ImageView account_photo;

    SharedPreferences sharedPreferences;
    String a1;
    public ProgressDialog progressDialog;
    DatabaseReference reference;

    public Account_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_, container, false);

        sharedPreferences = getContext().getSharedPreferences("data", 0);
        a1 = sharedPreferences.getString("userid", "");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        account_name = view.findViewById(R.id.account_name);
        account_email = view.findViewById(R.id.account_email);
        account_id = view.findViewById(R.id.account_id);
        account_phone = view.findViewById(R.id.account_phone);
        account_licence = view.findViewById(R.id.account_licence);
        account_vechileno = view.findViewById(R.id.account_vechileno);
        account_local_admin = view.findViewById(R.id.account_local_admin);
        account_deliveryCharge = view.findViewById(R.id.account_deliveryCharge);
        account_photo = view.findViewById(R.id.account_photo);

        progressDialog.show();
        reference = FirebaseDatabase.getInstance().getReference("delivery_boy").child(a1);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                progressDialog.dismiss();
                account_name.setText(snapshot.child("deliveryBoyName").getValue().toString());
                account_email.setText(snapshot.child("deliveryBoyEmail").getValue().toString());
                account_id.setText(snapshot.child("deliveryBoyID").getValue().toString());
                account_phone.setText(snapshot.child("deliveyBoyMobileNo").getValue().toString());
                account_licence.setText(snapshot.child("deliveyBoyLicence").getValue().toString());
                account_vechileno.setText(snapshot.child("deliveyBoyVechileNo").getValue().toString());
                account_local_admin.setText(snapshot.child("deliveryBoyLocalAdminName").getValue().toString());
                account_deliveryCharge.setText(snapshot.child("deliveryBoyDeliveryCharge").getValue().toString());

                Picasso.get().load(snapshot.child("photo").getValue().toString()).into(account_photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}