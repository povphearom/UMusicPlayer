package com.phearom.um.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phearom.um.R;
import com.phearom.um.config.Config;
import com.phearom.um.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ActivitySplashScreenBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (null != firebaseAuth.getCurrentUser()) {
                    FirebaseDatabase.getInstance().getReference("Server").child("IP").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String IP = dataSnapshot.getValue(String.class);
                            Config.AUTH.SERVER_IP = IP;
                            launch();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            snake();
                        }
                    });
                } else {
                    snake();
                }
            }
        };
        startAuth();
    }

    private void launch() {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        startActivity(intent);
        finish();
    }

    private void startAuth() {
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    snake();
                }
            }
        });
    }

    private void snake() {
        Snackbar snack = Snackbar.make(mBinding.getRoot(), "No Authenticated found", Snackbar.LENGTH_LONG);
        snack.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAuth();
            }
        });
        snack.show();
    }

    @Override
    protected void onStart() {
        if (null != mAuth)
            mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (null != mAuthListener)
            mAuth.removeAuthStateListener(mAuthListener);
        super.onStop();
    }
}
