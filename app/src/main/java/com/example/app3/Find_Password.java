package com.example.app3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class Find_Password extends AppCompatActivity {

    private final static String TAG = "FindPassword";

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            return true;

        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpassword);
        EditText getEmail = findViewById(R.id.SendEmail);
        Button SendEmail_Button = findViewById(R.id.SendButton);
        Intent moveToLogin = new Intent(this, Login.class);

        SendEmail_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = getEmail.getText().toString();
                if (emailAddress == null)
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    Log.d(TAG, "emailAddress: " + emailAddress);
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "이메일을 보냈습니다.", Toast.LENGTH_LONG).show();
                                        finish();
                                        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(), "이메일 보내기 실패", Toast.LENGTH_LONG).show();
                                    }
                                }

                            });
                }
            }

        });
    }
}
