package com.example.dell.sdl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Final extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        TextView order=(TextView)findViewById(R.id.textView16);
        Button button=(Button)findViewById(R.id.button6);
        Bundle bundle=getIntent().getExtras();
        order.setText(order.getText().toString()+bundle.getString("order"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Final.this,AppLauncher.class);
                startActivity(intent);
            }
        });
    }
}
