package com.example.dell.sdl;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class RestDetailsActivity extends AppCompatActivity {

    RatingBar rb;
    Button open_map,next;
    Button go_back,go_next;
    boolean flag;
    Double lat,longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_details);
        flag=false;
        rb = (RatingBar) findViewById(R.id.ratingBar);
        flag=true;
        setRating();
       // rb.setRating(4.0f);
        final Bundle bundle=getIntent().getExtras();
        TextView name=(TextView)findViewById(R.id.res_name);
        TextView add=(TextView)findViewById(R.id.res_add);
        TextView cui=(TextView)findViewById(R.id.res_cui);
        ImageView img=(ImageView)findViewById(R.id.res_img);
        String temp1=bundle.getString("res_name");
        temp1=temp1.toLowerCase();
        temp1=temp1.replace(" ","_");
        Resources r=getResources();
        int rid=r.getIdentifier(temp1,"drawable",getPackageName());
        img.setImageResource(rid);
        name.setText(bundle.getString("res_name"));
        add.setText(bundle.getString("res_add"));
        cui.setText(bundle.getString("res_cui"));
        String check=bundle.getString("Activity");
        next=(Button)findViewById(R.id.button_next);
        if(check==null)
        {
            next.setVisibility(View.INVISIBLE);
        }
        open_map = (Button) findViewById(R.id.button_map);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RestDetailsActivity.this,Tables.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        open_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MapsActivity obj = new MapsActivity();
                //obj.setResDetails(18.5209416,73.8389852,"Vaishali");
                Intent i = new Intent(RestDetailsActivity.this,MapsActivity.class);
                Bundle b = getIntent().getExtras();
                b.putDouble("my_lat",lat);
                b.putDouble("my_lon",longi);
                //b.putString("r_name",);

                i.putExtras(b);

                startActivity(i);

            }
        });
    }
    void setRating()
    {
        String res_name=getIntent().getExtras().getString("res_name");
        if(flag)
        {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Restaurants").child(res_name);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Object>> t=new GenericTypeIndicator<Map<String, Object>>() {};
                Map<String,Object> map=dataSnapshot.getValue(t);
                rb.setRating(Float.parseFloat(map.get("Rating").toString()));
                lat= (Double) map.get("Latitude");
                longi= (Double) map.get("Longitude");
                //rb.setRating();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
}
