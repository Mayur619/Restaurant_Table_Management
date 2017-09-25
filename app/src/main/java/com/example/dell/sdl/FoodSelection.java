package com.example.dell.sdl;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FoodSelection extends AppCompatActivity {
    ArrayList<String> selected_food = null;
    private Spinner categories;
    private Spinner menu;
    int removeItem=0;
    boolean isRemove=false,fetched=false,t=false;
    ArrayAdapter food_adapter=null;
    String[] cats;
    String Cuisine;
    DataSnapshot data;
    Button reserve;
    Long cost;
    Map<String,Object> item_cost;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bundle=getIntent().getExtras();
        t=true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_selection);
        String res = bundle.getString("res_name");
        FetchCuisine(res);
        cost= Long.valueOf(0);
        item_cost=new HashMap<>();
        reserve=(Button)findViewById(R.id.reserve);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodSelection.this,order_activity.class);
                bundle.putLong("cost",cost);
                bundle.putString("Activity","FoodSelection");
                bundle.putStringArrayList("menu",selected_food);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        LinearLayout layout=(LinearLayout)findViewById(R.id.food_back);
        layout.setBackground(getDrawable(R.drawable.hotel1));
        categories = (Spinner) findViewById(R.id.cat);
        menu = (Spinner) findViewById(R.id.menu);
        selected_food = new ArrayList<>();
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView t=((TextView) parent.getChildAt(0));
                t.setTypeface(Typeface.create("cursive",Typeface.NORMAL));
                t.setTextColor(Color.parseColor("#41FCFF"));
                if(fetched)
                    FetchMenu(categories.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                FetchMenu(categories.getSelectedItem().toString());
            }
        });
        menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        Button add = (Button) findViewById(R.id.add);
        Button remove = (Button) findViewById(R.id.remove);
        final Button show = (Button) findViewById(R.id.show);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_food.add(menu.getSelectedItem().toString());
                if(food_adapter==null)
                    food_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, selected_food);
                food_adapter.notifyDataSetChanged();
                cost+=(Long) item_cost.get(menu.getSelectedItem().toString());
                Toast.makeText(FoodSelection.this,Long.toString(cost),Toast.LENGTH_LONG).show();
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_food();
            }
        });
    }
    void show_dialog() {
        if (selected_food.size() == 0) {
            LayoutInflater inflator=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout=inflator.inflate(R.layout.list_empty,null);
            Toast toast=new Toast(this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.FILL_HORIZONTAL,0,0);
            toast.setView(layout);
            toast.show();
        } else {
            final Dialog dialog = new Dialog(FoodSelection.this);
            dialog.setContentView(R.layout.food_list);
            dialog.setCancelable(true);
            ListView list = (ListView) dialog.findViewById(R.id.selected_food);
            list.setAdapter(food_adapter);
            list.setDividerHeight(2);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView t=((TextView) parent.getChildAt(0));
                    t.setTypeface(Typeface.create("cursive",Typeface.NORMAL));
                    t.setTextColor(Color.parseColor("#41FCFF"));
                    if(isRemove)
                        removeItem=position;
                    dialog.dismiss();
                }
            });
            dialog.show();

        }
    }
    void remove_food()
    {
        if (selected_food.size() == 0) {
            LayoutInflater inflator=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout=inflator.inflate(R.layout.list_empty,null);
            Toast toast=new Toast(this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.FILL_HORIZONTAL,0,0);
            toast.setView(layout);
            toast.show();
        } else {
            final Dialog dialog = new Dialog(FoodSelection.this);
            dialog.setContentView(R.layout.food_list);
            dialog.setCancelable(true);
            final ListView list = (ListView) dialog.findViewById(R.id.selected_food);
            list.setAdapter(food_adapter);
            list.setDividerHeight(2);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cost-=(Long)item_cost.get(selected_food.get(position));
                    selected_food.remove(position);
                    food_adapter.notifyDataSetChanged();

                    Toast.makeText(FoodSelection.this,Long.toString(cost),Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
    synchronized void FetchCuisine(String res) {
        if (t) {

            Log.i("data", res);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference resRef = database.getReference("Restaurants").child(res);
            resRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    data = dataSnapshot;
                    GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {
                    };
                    Map<String, Object> map = dataSnapshot.getValue(t);
                    Cuisine = (String) map.get("Cuisine");
                    cats = Cuisine.split(",");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, cats);
                    adapter.setDropDownViewResource(R.layout.spinner_row);
                    categories.setAdapter(adapter);
                    Log.i("Cuisine:", Cuisine);
                    fetched = true;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    synchronized void FetchMenu(String cuisine)
    {
        final FirebaseDatabase  database = FirebaseDatabase.getInstance();
        DatabaseReference resRef=database.getReference("Cuisine").child(cuisine);
        resRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> menuItems=new ArrayList<String>();
                Iterable<DataSnapshot> itb=dataSnapshot.getChildren();
                Iterator<DataSnapshot> it=itb.iterator();
                List<holder> list=new ArrayList<>();
                while(it.hasNext())
                {
                    DataSnapshot data=it.next();
                    item_cost.put(data.getKey(),data.getValue());
                    menuItems.add(data.getKey());
                    /*Holder.setName(data.getKey());
                    Holder.setCost((Long) data.getValue());
                    Log.i(Holder.getName(), String.valueOf(Holder.getCost()));
                    list.add(Holder);
                    Custom menu_adapter=new Custom(FoodSelection.this,list);
                    menu.setAdapter(menu_adapter);*/
                }

                ArrayAdapter<String> menu_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,menuItems);
                menu_adapter.setDropDownViewResource(R.layout.spinner_row);
                menu.setAdapter(menu_adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
