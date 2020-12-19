package com.epayeats.epayeatsdeliverypartner.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import com.epayeats.epayeatsdeliverypartner.Map.Sample_MapsActivity;
import com.epayeats.epayeatsdeliverypartner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Sample_Activity extends AppCompatActivity
{
    CardView cardview1, cardview2, cardview3, cardview4, cardview5;

    ImageView sample_menu_image;
    TextView sample_menu_name;
    TextView sample_menu_price;
    TextView sample_menu_qty;
    TextView sample_menu_total;

    TextView sample_rest_name;
    TextView sample_rest_place;
    TextView sample_rest_phone;

    TextView sample_cname;
    TextView sample_house;
    TextView sample_area;
    TextView sample_pincode;
    TextView sample_status;

    TextView sample_phone;
    ImageView sample_phone_btn;
    TextView sample_alt_phone;
    ImageView sample_alt_phone_btn;
    Button sample_complete_order;

    String orderID;
    String menuID;
    String menuName;
    String menuImage;

    String mainCatagoryID;
    String mainCatagoryName;
    String subCatagoryID;
    String subCatagoryName;

    String localAdminID;

    String offerPrice;
    String sellingPrice;
    String actualPrice;

    String orderDate;
    String orderTime;
    String qty;
    String totalPrice;

    String house;
    String area;
    String city;
    String pincode;
    String cName;
    String cPhone;
    String cAltPhone;

    String userID;
    String userLocation;
    String user_lat;
    String user_long;

    String status;
    int total = 0;

    String restID;
    String restName;

    Button sample_simple_map_btn;
    Button sample_detailed_map_btn;

    Button sample_cancel_order;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);

        cardview1 = findViewById(R.id.cardview1);
        cardview2 = findViewById(R.id.cardview2);
        cardview3 = findViewById(R.id.cardview3);
        cardview4 = findViewById(R.id.cardview4);
        cardview5 = findViewById(R.id.cardview5);

        sample_menu_image = findViewById(R.id.sample_menu_image);
        sample_menu_name = findViewById(R.id.sample_menu_name);
        sample_menu_price = findViewById(R.id.sample_menu_price);
        sample_menu_qty = findViewById(R.id.sample_menu_qty);
        sample_menu_total = findViewById(R.id.sample_menu_total);

        sample_rest_name = findViewById(R.id.sample_rest_name);
        sample_rest_place = findViewById(R.id.sample_rest_place);
        sample_rest_phone = findViewById(R.id.sample_rest_phone);

        sample_cname = findViewById(R.id.sample_cname);
        sample_house = findViewById(R.id.sample_house);
        sample_area = findViewById(R.id.sample_area);
        sample_pincode = findViewById(R.id.sample_pincode);
        sample_status = findViewById(R.id.sample_status);

        sample_phone = findViewById(R.id.sample_phone);
        sample_phone_btn = findViewById(R.id.sample_phone_btn);
        sample_alt_phone = findViewById(R.id.sample_alt_phone);
        sample_alt_phone_btn = findViewById(R.id.sample_alt_phone_btn);
        sample_complete_order = findViewById(R.id.sample_complete_order);

        sample_simple_map_btn = findViewById(R.id.sample_simple_map_btn);
        sample_detailed_map_btn = findViewById(R.id.sample_detailed_map_btn);

        sample_cancel_order = findViewById(R.id.sample_cancel_order);

        orderID = getIntent().getExtras().getString("orderID");
        menuID = getIntent().getExtras().getString("menuID");
        menuName = getIntent().getExtras().getString("menuName");
        menuImage = getIntent().getExtras().getString("menuImage");

        mainCatagoryID = getIntent().getExtras().getString("mainCatagoryID");
        mainCatagoryName = getIntent().getExtras().getString("mainCatagoryName");
        subCatagoryID = getIntent().getExtras().getString("subCatagoryID");
        subCatagoryName = getIntent().getExtras().getString("subCatagoryName");

        localAdminID = getIntent().getExtras().getString("localAdminID");

        offerPrice = getIntent().getExtras().getString("offerPrice");
        sellingPrice = getIntent().getExtras().getString("sellingPrice");
        actualPrice = getIntent().getExtras().getString("actualPrice");

        orderDate = getIntent().getExtras().getString("orderDate");
        orderTime = getIntent().getExtras().getString("orderTime");
        qty = getIntent().getExtras().getString("qty");
        totalPrice = getIntent().getExtras().getString("totalPrice");

        house = getIntent().getExtras().getString("house");
        area = getIntent().getExtras().getString("area");
        city = getIntent().getExtras().getString("city");
        pincode = getIntent().getExtras().getString("pincode");
        cName = getIntent().getExtras().getString("cName");
        cPhone = getIntent().getExtras().getString("cPhone");
        cAltPhone = getIntent().getExtras().getString("cAltPhone");

        userID = getIntent().getExtras().getString("userID");
        userLocation = getIntent().getExtras().getString("userLocation");
        user_lat = getIntent().getExtras().getString("user_lat");
        user_long = getIntent().getExtras().getString("user_long");

        status = getIntent().getExtras().getString("status");

        restID = getIntent().getExtras().getString("restID");
        restName = getIntent().getExtras().getString("restName");

        Picasso.get().load(menuImage).into(sample_menu_image);
        sample_menu_name.setText(menuName);
        sample_menu_price.setText(offerPrice);
        sample_menu_qty.setText(qty);
        sample_house.setText(house);
        sample_area.setText(area);
        sample_pincode.setText(pincode);
        sample_cname.setText(cName);
        sample_phone.setText(cPhone);
        sample_alt_phone.setText(cAltPhone);

        sample_rest_name.setText(restName);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("restaurants").child(restID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                sample_rest_phone.setText(snapshot.child("resPhone").getValue().toString());
                sample_rest_place.setText(snapshot.child("resLocation").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Sample_Activity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if (status.equals("0")) {
            sample_status.setText("Pending, Not yet Delivered");
        } else if (status.equals("1")) {
            sample_status.setText("Order is Picked up by the Delivery Agent");
        } else if (status.equals("2"))
        {
            sample_status.setText("Delivered");
            sample_complete_order.setEnabled(false);
            sample_phone_btn.setEnabled(false);
            sample_alt_phone_btn.setEnabled(false);
        } else {
            sample_status.setText("Cancelled");
            sample_complete_order.setEnabled(false);
            sample_phone_btn.setEnabled(false);
            sample_alt_phone_btn.setEnabled(false);
        }

        int qty = 0;
        int price = 0;
        qty = qty + Integer.parseInt(sample_menu_qty.getText().toString());
        price = price + Integer.parseInt(sample_menu_price.getText().toString());
        int temp = qty * price;
        total = total + temp;
        sample_menu_total.setText(total + "");

        sample_complete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });

        sample_phone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Permissions.check(Sample_Activity.this, Manifest.permission.CALL_PHONE, null, new PermissionHandler() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onGranted()
                    {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        if (cPhone.isEmpty()) {
                            Toast.makeText(Sample_Activity.this, "Unable To make Call", Toast.LENGTH_SHORT).show();
                        } else {
                            intent.setData(Uri.parse("tel:" + cPhone));
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions)
                    {
                        Toast.makeText(Sample_Activity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        super.onDenied(context, deniedPermissions);
                    }

                    @Override
                    public boolean onBlocked(Context context, ArrayList<String> blockedList)
                    {
                        Toast.makeText( Sample_Activity.this, "Permission blocked", Toast.LENGTH_SHORT).show();
                        return super.onBlocked(context, blockedList);
                    }
                });
            }
        });

        sample_alt_phone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Permissions.check(Sample_Activity.this, Manifest.permission.CALL_PHONE, null, new PermissionHandler() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onGranted()
                    {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        if (cAltPhone.isEmpty()) {
                            Toast.makeText(Sample_Activity.this, "Unable To make Call", Toast.LENGTH_SHORT).show();
                        } else {
                            intent.setData(Uri.parse("tel:" + cAltPhone));
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions)
                    {
                        Toast.makeText(Sample_Activity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        super.onDenied(context, deniedPermissions);
                    }

                    @Override
                    public boolean onBlocked(Context context, ArrayList<String> blockedList)
                    {
                        Toast.makeText( Sample_Activity.this, "Permission blocked", Toast.LENGTH_SHORT).show();
                        return super.onBlocked(context, blockedList);
                    }
                });
            }
        });

        sample_simple_map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Sample_Activity.this, GetLocation_Activity.class);
                intent.putExtra("user_lat", user_lat);
                intent.putExtra("user_long", user_long);
                intent.putExtra("userLocation", userLocation);
                startActivity(intent);
            }
        });

        sample_detailed_map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Sample_Activity.this, GetLocationMainActivity.class);
                intent.putExtra("user_lat", user_lat);
                intent.putExtra("user_long", user_long);
                intent.putExtra("userLocation", userLocation);
                startActivity(intent);
            }
        });

        sample_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                cancelOrder();
            }
        });

    }

    private void cancelOrder()
    {
        SweetAlertDialog dialog1 = new SweetAlertDialog(Objects.requireNonNull(Sample_Activity.this), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Cancel your order!")
                .setConfirmText("Yes !")
                .setCancelText("No, cancel please")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog)
                    {
                        sweetAlertDialog.setTitleText("Cancelled")
                                .setContentText("Order has been completed")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .showCancelButton(false)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);


                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order_data").child(orderID);
                        reference.child("orderStatus").setValue("3");

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(Sample_Activity.this)
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                                .setContentTitle("Your Order")
                                .setContentText("Your order has been cancelled")
                                .setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_ALL);

                        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        builder.setSound(path);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            String channelId = "Your_Channel_ID";
                            NotificationChannel channel = new NotificationChannel(channelId, "Chanel Human redable Text", NotificationManager.IMPORTANCE_DEFAULT);
                            notificationManager.createNotificationChannel(channel);
                            builder.setChannelId(channelId);
                        }
                        notificationManager.notify(1, builder.build());

                        Toast.makeText(Sample_Activity.this, "Status Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Sample_Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        dialog1.show();

    }

    private void updateStatus()
    {
        SweetAlertDialog dialog1 = new SweetAlertDialog(Objects.requireNonNull(Sample_Activity.this), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Complete your order!")
                .setConfirmText("Yes !")
                .setCancelText("No, cancel please")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog)
                    {
                        sweetAlertDialog.setTitleText("Delivered")
                                .setContentText("Order has been completed")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .showCancelButton(false)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        String time = new SimpleDateFormat("hh-mm-a", Locale.getDefault()).format(new Date());

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order_data").child(orderID);
                        reference.child("orderStatus").setValue("2");
                        reference.child("deliveryDate").setValue(date);
                        reference.child("deliveryTime").setValue(time);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(Sample_Activity.this)
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                                .setContentTitle("Your Order")
                                .setContentText("Your order has been completed")
                                .setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_ALL);

                        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        builder.setSound(path);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            String channelId = "Your_Channel_ID";
                            NotificationChannel channel = new NotificationChannel(channelId, "Chanel Human redable Text", NotificationManager.IMPORTANCE_DEFAULT);
                            notificationManager.createNotificationChannel(channel);
                            builder.setChannelId(channelId);
                        }
                        notificationManager.notify(1, builder.build());

                        Toast.makeText(Sample_Activity.this, "Status Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Sample_Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        dialog1.show();
    }

}
