package com.example.dell.sdl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

public class Tables extends AppCompatActivity {
    int tableId;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tables);
        bundle=getIntent().getExtras();

        //getTables(10,2,6);
        setTables(bundle.getString("res_name"));
        Button button=(Button)findViewById(R.id.buttonBook);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view= LayoutInflater.from(Tables.this).inflate(R.layout.dialog,null);
                TextView table=(TextView)view.findViewById(R.id.table);
                table.setText(Integer.toString(tableId));
                String rest=bundle.getString("res_name");
                String cuis=bundle.getString("res_cui");
                Log.i(rest,cuis);
                TextView res=(TextView)view.findViewById(R.id.res);
                TextView cui=(TextView)view.findViewById(R.id.cui);
                res.setText(rest);
                cui.setText(cuis);
                final EditText table_no= (EditText) view.findViewById(R.id.editText);
                final AlertDialog.Builder builder=new AlertDialog.Builder(Tables.this);
                LayoutInflater in=(LayoutInflater)Tables.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                builder.setView(view).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Tables.this,FoodSelection.class);
                        bundle.putString("table_no",table_no.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(true);
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
    }
    public void setTables(String res)
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference resRef=database.getReference("Restaurants").child(res);
        resRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Object>> t=new GenericTypeIndicator<Map<String, Object>>() {};
                Map<String,Object> map=dataSnapshot.getValue(t);
                Long tables= (Long) map.get("No of Tables");
                String tempStr= (String) map.get("Tables Booked");
                String[] arr=tempStr.split(",");
                Vector<Integer> booked=new Vector<Integer>();
                for(String temp:arr)
                {
                    booked.add(Integer.parseInt(temp));
                }
                getTables(tables.intValue(),booked);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @SuppressWarnings("ResourceType")
    public void getTables(int no, Vector<Integer> booked)
    {
        int flag=0;
        TableLayout layout=(TableLayout)findViewById(R.id.table);
        ArrayList<TableRow> rows=new ArrayList<>();
        TableRow row=new TableRow(Tables.this);
        for(int i=1;i<=no;i++)
        {
            if(flag==0)
            {
                ImageButton button=new ImageButton(Tables.this);
                button.setImageResource(R.drawable.table);
                button.setBackgroundResource(R.drawable.table_button);
                button.setId(i);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tableId=v.getId();
                        Toast.makeText(getApplicationContext(),Integer.toString(v.getId()),Toast.LENGTH_SHORT).show();
                    }
                });
                row.addView(button);
                flag=1;
            }
           else if(i%3==1&&flag==1)
           {
               rows.add(row);
               row=new TableRow(Tables.this);
               ImageButton button=new ImageButton(Tables.this);
               button.setImageResource(R.drawable.table);
               button.setBackgroundResource(R.drawable.table_button);
               button.setId(i);
              // button.setBackground(R.dr);
               button.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       tableId=v.getId();
                       Toast.makeText(getApplicationContext(),Integer.toString(v.getId()),Toast.LENGTH_SHORT).show();
                   }
               });
               row.addView(button);
           }
           else{
               ImageButton button=new ImageButton(Tables.this);
               button.setImageResource(R.drawable.table);
                button.setBackgroundResource(R.drawable.table_button);
               button.setId(i);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tableId=v.getId();
                        Toast.makeText(getApplicationContext(),Integer.toString(v.getId()),Toast.LENGTH_SHORT).show();
                    }
                });
               row.addView(button);
           }
        }
        for(TableRow rowTemp:rows)
        {
            layout.addView(rowTemp);
        }
        layout.addView(row);
        for(int t:booked)
        {
            ImageButton temp=(ImageButton)layout.findViewById(t);
            temp.setBackgroundResource(R.drawable.table_reserved);
            temp.setEnabled(false);
            temp.setClickable(false);
        }
        //if(no%3!=0)

        layout.setStretchAllColumns(true);
    }
}
