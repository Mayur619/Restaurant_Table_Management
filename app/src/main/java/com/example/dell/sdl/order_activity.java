package com.example.dell.sdl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class order_activity extends AppCompatActivity {
    String con,temp1,name,tables;
    Bundle bundle;
    boolean updated=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_activity);
        bundle = getIntent().getExtras();
        String activity = bundle.getString("Activity");
        Button button = (Button) findViewById(R.id.button4);
        final TextView res_name, res_add, user_name, menu, table, user_con, date_time, cost;
        res_name = (TextView) findViewById(R.id.res_name);
        res_add = (TextView) findViewById(R.id.res_add);
        user_name = (TextView) findViewById(R.id.user_name);
        user_con = (TextView) findViewById(R.id.user_con);
        cost = (TextView) findViewById(R.id.cost);
        menu = (TextView) findViewById(R.id.Menu);
        table = (TextView) findViewById(R.id.table);
        date_time = (TextView) findViewById(R.id.date_time);
        if (activity.equals("FoodSelection")) {
            res_name.setText(bundle.getString("res_name"));
            res_add.setText(bundle.getString("res_add"));
            cost.setText(String.valueOf(bundle.getLong("cost")));
            ArrayList<String> temp = bundle.getStringArrayList("menu");
            temp1 = temp.toString();
            temp1 = temp1.substring(1, temp1.length() - 1);
            menu.setText(temp1);
            String dateTime = bundle.getString("date") + "  " + bundle.getString("time");
            Log.i("Date and time:", dateTime);
            date_time.setText(dateTime);
            table.setText(bundle.getString("table_no"));
            String uid = null;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Name, email address, and profile photo Url
                name = user.getDisplayName();
                user_name.setText(name);
                uid = user.getUid();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users");
                myRef.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        con = dataSnapshot.child("contact no").getValue(String.class);
                        user_con.setText(con);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference table_ref = database.getReference("Restaurants");
                    final DatabaseReference up = table_ref.child(bundle.getString("res_name")).child("Tables Booked");
                    table_ref.child(bundle.getString("res_name")).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (updated == false) {
                                tables = dataSnapshot.child("Tables Booked").getValue(String.class);
                                tables = tables + "," + bundle.getString("table_no");
                                up.setValue(tables);
                                updated = true;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference myRef = database.getReference("Orders");
                    StringBuilder builder = new StringBuilder();
                    String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                    for (int i = 0; i < 5; i++) {
                        int character = (int) (Math.random() * ALPHA_NUMERIC.length());
                        builder.append(ALPHA_NUMERIC.charAt(character));
                    }
                    DatabaseReference ref = myRef.child(builder.toString());
                    ref.child("res_name").setValue(bundle.getString("res_name"));
                    ref.child("user_name").setValue(name);
                    ref.child("res_add").setValue(bundle.getString("res_add"));
                    ref.child("date_time").setValue(bundle.getString("date_time"));
                    ref.child("cost").setValue(String.valueOf(bundle.getLong("cost")));
                    ref.child("table").setValue(bundle.getString("table_no"));
                    ref.child("menu").setValue(temp1);
                    ref.child("user_con").setValue(con);
                    Intent in = new Intent(order_activity.this, Final.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("order",builder.toString());
                    in.putExtras(bundle);
                    startActivity(in);
                }
            });

        }
        else
        {
            button.setVisibility(View.INVISIBLE);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table_ref = database.getReference("Orders");
            String id=bundle.getString("order");
            table_ref.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        res_name.setText(dataSnapshot.child("res_name").getValue(String.class));
                        res_add.setText(dataSnapshot.child("res_add").getValue(String.class));
                        user_name.setText(dataSnapshot.child("user_name").getValue(String.class));
                        user_con.setText(dataSnapshot.child("user_con").getValue(String.class));
                        table.setText(dataSnapshot.child("table").getValue(String.class));
                        date_time.setText(dataSnapshot.child("date_time").getValue(String.class));
                        menu.setText(dataSnapshot.child("menu").getValue(String.class));
                        cost.setText(dataSnapshot.child("cost").getValue(String.class));
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
