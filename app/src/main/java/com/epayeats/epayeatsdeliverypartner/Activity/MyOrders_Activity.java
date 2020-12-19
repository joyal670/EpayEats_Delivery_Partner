package com.epayeats.epayeatsdeliverypartner.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsdeliverypartner.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyOrders_Activity extends AppCompatActivity
{
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

    String status;

    ImageView current_image;
    TextView current_name, current_price, current_qty, current_house, current_area, current_pincode, current_cname, current_cnumber, current_altcnumber, current_status, myOrder_TotalPrice;
    Button completebtn;
    ImageButton callbtn, callaltbtn;

    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_);

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

        status = getIntent().getExtras().getString("status");

        current_image = findViewById(R.id.myOrder_image);
        current_name = findViewById(R.id.myOrder_name);
        current_price = findViewById(R.id.myOrder_price);
        current_qty = findViewById(R.id.myOrder_qty);
        current_house = findViewById(R.id.myOrder_house);
        current_area = findViewById(R.id.myOrder_area);
        current_pincode = findViewById(R.id.myOrder_pincode);
        current_cname = findViewById(R.id.myOrder_cname);
        current_cnumber = findViewById(R.id.myOrder_cnumber);
        current_altcnumber = findViewById(R.id.myOrder_caltnumber);
        current_status = findViewById(R.id.myOrder_status);
        completebtn = findViewById(R.id.myOrder_button);
        callbtn = findViewById(R.id.myOrder_callbtn);
        callaltbtn = findViewById(R.id.myOrder_callaltbtn);
        myOrder_TotalPrice = findViewById(R.id.myOrder_TotalPrice);

        Picasso.get().load(menuImage).into(current_image);
        current_name.setText(menuName);
        current_price.setText(offerPrice);
        current_qty.setText(qty);
        current_house.setText(house);
        current_area.setText(area);
        current_pincode.setText(pincode);
        current_cname.setText(cName);
        current_cnumber.setText(cPhone);
        current_altcnumber.setText(cAltPhone);

        if (status.equals("0")) {
            current_status.setText("Pending, Not yet Delivered");
        } else if (status.equals("1")) {
            current_status.setText("Order is Picked up by the Delivery Agent");
        } else if (status.equals("2")) {
            current_status.setText("Delivered");
            completebtn.setEnabled(false);
            callbtn.setEnabled(false);
            callaltbtn.setEnabled(false);
        } else {
            current_status.setText("Cancelled");
            completebtn.setEnabled(false);
            callbtn.setEnabled(false);
            callaltbtn.setEnabled(false);
        }

        int qty = 0;
        int price = 0;
        qty = qty + Integer.parseInt(current_qty.getText().toString());
        price = price + Integer.parseInt(current_price.getText().toString());
        int temp = qty * price;
        total = total + temp;
        myOrder_TotalPrice.setText(total + "");

        completebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Permissions.check(MyOrders_Activity.this, Manifest.permission.CALL_PHONE, null, new PermissionHandler() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onGranted()
                    {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        if (cPhone.isEmpty()) {
                            Toast.makeText(MyOrders_Activity.this, "Unable To make Call", Toast.LENGTH_SHORT).show();
                        } else {
                            intent.setData(Uri.parse("tel:" + cPhone));
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions)
                    {
                        Toast.makeText(MyOrders_Activity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        super.onDenied(context, deniedPermissions);
                    }

                    @Override
                    public boolean onBlocked(Context context, ArrayList<String> blockedList)
                    {
                        Toast.makeText( MyOrders_Activity.this, "Permission blocked", Toast.LENGTH_SHORT).show();
                        return super.onBlocked(context, blockedList);
                    }
                });
            }
        });

        callaltbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Permissions.check(MyOrders_Activity.this, Manifest.permission.CALL_PHONE, null, new PermissionHandler() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onGranted()
                    {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        if (cAltPhone.isEmpty()) {
                            Toast.makeText(MyOrders_Activity.this, "Unable To make Call", Toast.LENGTH_SHORT).show();
                        } else {
                            intent.setData(Uri.parse("tel:" + cAltPhone));
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions)
                    {
                        Toast.makeText(MyOrders_Activity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        super.onDenied(context, deniedPermissions);
                    }

                    @Override
                    public boolean onBlocked(Context context, ArrayList<String> blockedList)
                    {
                        Toast.makeText( MyOrders_Activity.this, "Permission blocked", Toast.LENGTH_SHORT).show();
                        return super.onBlocked(context, blockedList);
                    }
                });
            }
        });

    }

    private void updateStatus()
    {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh-mm-a", Locale.getDefault()).format(new Date());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("order_data").child(orderID);
        reference.child("orderStatus").setValue("2");
        reference.child("deliveryDate").setValue(date);
        reference.child("deliveryTime").setValue(time);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
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

        Toast.makeText(MyOrders_Activity.this, "Status Updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MyOrders_Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}