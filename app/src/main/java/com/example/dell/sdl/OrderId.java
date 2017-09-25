package com.example.dell.sdl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderId extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_id);
        final Bundle bundle=getIntent().getExtras();
        final EditText text=(EditText)findViewById(R.id.editText2);
        Button button=(Button)findViewById(R.id.button);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Orders");
        if(bundle.getString("action").equals("details"))
        {
            button.setText("Get Details");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child(text.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null)
                            {
                                bundle.putString("order",text.getText().toString());
                                Intent in=new Intent(OrderId.this,order_activity.class);
                                in.putExtras(bundle);
                                startActivity(in);
                            }
                            Toast.makeText(OrderId.this,"Please enter valid order id",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });
        }
        else
        {

            button.setText("Cancel Reservation");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child(text.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null)
                            {
                                bundle.putString("order",text.getText().toString());
                                Intent in=new Intent(OrderId.this,Cancel.class);
                                in.putExtras(bundle);
                                startActivity(in);
                            }
                            else
                            {
                                Toast.makeText(OrderId.this,"Please enter valid order id",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }
}
