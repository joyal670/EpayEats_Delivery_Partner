package com.epayeats.epayeatsdeliverypartner.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsdeliverypartner.Fragment.Account_Fragment;
import com.epayeats.epayeatsdeliverypartner.Fragment.Dashboard_Fragment;
import com.epayeats.epayeatsdeliverypartner.Fragment.Logout_Fragment;
import com.epayeats.epayeatsdeliverypartner.Fragment.Myorders_Fragment;
import com.epayeats.epayeatsdeliverypartner.Fragment.Report_Fragment;
import com.epayeats.epayeatsdeliverypartner.R;
import com.epayeats.epayeatsdeliverypartner.Service.MyService;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToogle;
    String currentFragment = "other";
    String isBlocked = "Blocked";
    SharedPreferences sharedPreferences;
    String a1;
    String a2;
    TextView headerEmail;

    String status1 = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerToogle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerToogle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
        mDrawerLayout.addDrawerListener(mDrawerToogle);
        mDrawerToogle.syncState();

        sharedPreferences = getSharedPreferences("data", 0);
        a1 = sharedPreferences.getString("userid", "");

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerEmail = navigationView.getHeaderView(0).findViewById(R.id.header_email);
        a2 = sharedPreferences.getString("useremail", "");
        headerEmail.setText(a2);


        Dashboard_Fragment fragment = new Dashboard_Fragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment,"Dashboard");
        fragmentTransaction.commit();

        checkStatus();

        DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("order_data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if(a1.equals(dataSnapshot.child("deliveryBodID").getValue().toString()))
                    {
                        if(status1.equals(dataSnapshot.child("orderStatus").getValue().toString()))
                        {
                            startService(new Intent(MainActivity.this, MyService.class));
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void checkStatus()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("delivery_boy").child(a1);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String status = "UnBlocked";
                if(status.equals(snapshot.child("isBlocked").getValue().toString()))
                {

                }
                else
                {
                    SweetAlertDialog dialog1 = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Your account is Blocked")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog)
                                {
                                    FirebaseAuth.getInstance().signOut();
                                    sharedPreferences = getSharedPreferences("data",0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                                    startActivity(intent);

                                }
                            });
                    dialog1.setCancelable(false);
                    dialog1.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.dashboard)
        {
            Dashboard_Fragment fragment = new Dashboard_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment,"Dashboard");
            fragmentTransaction.commit();
        }
        else if (id == R.id.myorders)
        {
            currentFragment = "other";
            Myorders_Fragment fragment = new Myorders_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment,"My Orders");
            fragmentTransaction.commit();
        }
        else if (id == R.id.accout)
        {
            currentFragment = "other";
            Account_Fragment fragment = new Account_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment,"Account");
            fragmentTransaction.commit();
        }
        else if (id == R.id.report)
        {
            currentFragment = "other";
            Report_Fragment fragment = new Report_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment,"Report");
            fragmentTransaction.commit();
        }
        else if (id == R.id.logout)
        {
            currentFragment = "other";
            Logout_Fragment fragment = new Logout_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment,"Logout");
            fragmentTransaction.commit();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (currentFragment != "home")
            {
                Dashboard_Fragment fragment = new Dashboard_Fragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment,"Dashboard");
                fragmentTransaction.commit();
            } else {
                super.onBackPressed();
            }
        }
    }
}