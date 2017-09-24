package com.example.dell.sdl;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


public class Filter extends AppCompatActivity implements View.OnClickListener{
    EditText date;
    EditText time;
    Button confirm;
    Button btn_date;
    boolean flag;
    Button btn_time,show;
    int day,month,year,hour,minute;
    ProgressDialog progress;
    Spinner spin_location,spin_cuisine,spin_capacity,spin_Res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        spin_location = (Spinner) findViewById(R.id.spinner1);
        spin_cuisine = (Spinner) findViewById(R.id.spinner2);
        //spin_capacity = (Spinner) findViewById(R.id.spinner3);
        spin_Res=(Spinner)findViewById(R.id.spinner4);
        progress=new ProgressDialog(Filter.this);
        final Bundle temp=getIntent().getExtras();
        progress.setMessage("Downloading required resources \n Please wait.....");
        show=(Button)findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();;
                //String test=temp.getString("Activity");
                if(temp==null)
                {
                    bundle.putString("Activity","AppLauncher");
                }
                Intent intent=new Intent(Filter.this,RestDetailsActivity.class);
                bundle.putString("res_name",spin_Res.getSelectedItem().toString());
                bundle.putString("res_add",spin_location.getSelectedItem().toString());
                bundle.putString("date",date.getText().toString());
                bundle.putString("time",time.getText().toString());
                bundle.putString("res_cui",spin_cuisine.getSelectedItem().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_date = (Button) findViewById(R.id.button2);
        btn_time = (Button) findViewById(R.id.button3);
        confirm = (Button) findViewById(R.id.show);
        date = (EditText) findViewById(R.id.date);
        time = (EditText) findViewById(R.id.time);
        flag=false;
        setData();

        btn_date.setOnClickListener(this);
        btn_time.setOnClickListener(this);

    }

    public void onClick(View v) {
        if(v==btn_date){
            final Calendar c= Calendar.getInstance();
            day=c.get(Calendar.DAY_OF_MONTH);
            month=c.get(Calendar.MONTH);
            year=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    date.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                }
            },year,month,day);
            datePickerDialog.show();
        }
        if (v==btn_time){
            final Calendar c= Calendar.getInstance();
            hour=c.get(Calendar.HOUR_OF_DAY);
            minute=c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    time.setText(hourOfDay+":"+minute);
                }
            },hour,minute,false);
            timePickerDialog.show();
        }
    }
    void setData() {
        progress.show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Restaurants");

        Query query = myRef.orderByChild("Location");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> locs = new TreeSet<String>();
                Iterable<DataSnapshot> itb = dataSnapshot.getChildren();
                Iterator<DataSnapshot> it = itb.iterator();
                while (it.hasNext()) {
                    locs.add((String) it.next().child("Location").getValue());
                }
                ArrayAdapter adapter = new ArrayAdapter(Filter.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList(locs));
                //TextView t=(TextView)findViewById(R.id.item);
                //t.setTextColor(Color.parseColor("#41FCFF"));
                adapter.setDropDownViewResource(R.layout.spinner_row);
                spin_location.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        spin_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView t=((TextView) parent.getChildAt(0));
                t.setTypeface(Typeface.create("cursive",Typeface.NORMAL));
                t.setTextColor(Color.parseColor("#41FCFF"));
                String cur_loc = spin_location.getSelectedItem().toString();
                Query query1 = myRef.orderByChild("Location").equalTo(cur_loc);
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> itb = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> it = itb.iterator();
                        Set<String> cui = new TreeSet<String>();
                        while (it.hasNext()) {
                            DataSnapshot data = it.next();
                            String temp = (String) data.child("Cuisine").getValue();
                            String[] arr = temp.split(",");
                            for (String ar : arr)
                                cui.add(ar);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(Filter.this, android.R.layout.simple_list_item_1, new ArrayList(cui));
                        adapter.setDropDownViewResource(R.layout.spinner_row);
                        spin_cuisine.setMinimumWidth(50);
                        spin_cuisine.setAdapter(adapter);
                        spin_Res.setEnabled(true);
                        flag=true;
                        if(flag)
                        {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                             final DatabaseReference myRef = database.getReference("Restaurants");
                             final String cur_loc=spin_location.getSelectedItem().toString();
                            spin_cuisine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    final String cur_cui = spin_cuisine.getSelectedItem().toString();
                                    TextView t=((TextView) parent.getChildAt(0));
                                    t.setTypeface(Typeface.create("cursive",Typeface.NORMAL));
                                    t.setTextColor(Color.parseColor("#41FCFF"));
                                    Query query = myRef.orderByChild("Location").equalTo(cur_loc);
                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Set<String> res = new TreeSet<String>();
                                            Iterable<DataSnapshot> itb = dataSnapshot.getChildren();
                                            Iterator<DataSnapshot> it = itb.iterator();
                                            while (it.hasNext()) {
                                                DataSnapshot data = it.next();
                                                if (data.child("Cuisine").getValue(String.class).contains(cur_cui)) {
                                                    res.add(data.getKey());
                                                }
                                            }
                                            ArrayAdapter adapter = new ArrayAdapter(Filter.this, android.R.layout.simple_list_item_1, new ArrayList(res));
                                            adapter.setDropDownViewResource(R.layout.spinner_row);
                                            spin_Res.setAdapter(adapter);
                                            spin_Res.setMinimumWidth(30);
                                            spin_Res.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    TextView t=((TextView) parent.getChildAt(0));
                                                    t.setTypeface(Typeface.create("cursive",Typeface.NORMAL));
                                                    t.setTextColor(Color.parseColor("#41FCFF"));
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                            if(progress.isShowing())
                                                progress.dismiss();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
