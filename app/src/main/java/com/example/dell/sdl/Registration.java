package com.example.dell.sdl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText name,phone,username,address,password,confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        username = (EditText)findViewById(R.id.email_id);
        address = (EditText) findViewById(R.id.address);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        Button confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(TextUtils.isEmpty(username.getText().toString()))
                   Toast.makeText(Registration.this,"You cannot leave username blank",Toast.LENGTH_LONG);

                SignUp();
            }
        });
    }
    void SignUp()
    {
        final String user=username.getText().toString().trim();
        String pass=password.toString().trim();
        if(!(TextUtils.isEmpty(user)&&(TextUtils.isEmpty(pass))))
        {
        mAuth.createUserWithEmailAndPassword(user,pass)
                .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            FirebaseUser curr_user=mAuth.getCurrentUser();
                            UserProfileChangeRequest req=new UserProfileChangeRequest.Builder().setDisplayName(name.getText().toString()).build();
                            curr_user.updateProfile(req);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Users");
                            DatabaseReference ref =myRef.child(curr_user.getUid());
                            ref.child("name").setValue(name.getText().toString());
                            ref.child("email-id").setValue(user);
                            ref.child("contact no").setValue(phone.getText().toString());
                            ref.child("address").setValue(address.getText().toString());
                            Toast.makeText(Registration.this,"Registered",Toast.LENGTH_LONG).show();
                            //Intent intent=new Intent(Registration.this,Tables.class);
                            Intent intent=new Intent(Registration.this,Login.class);
                            startActivity(intent);

                        }
                        else
                            Toast.makeText(Registration.this,"User already registered",Toast.LENGTH_LONG).show();

                        // ...
                    }
                });
    }
}
}
