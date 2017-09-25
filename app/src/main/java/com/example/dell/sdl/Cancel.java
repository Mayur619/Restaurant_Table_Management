package com.example.dell.sdl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Cancel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_ref = database.getReference("Orders");
        Bundle bundle=getIntent().getExtras();
        String id=bundle.getString("order");
        table_ref.child(id).setValue(null);
    }
}
