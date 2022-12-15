package com.example.app3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Sign_up extends AppCompatActivity {

    private final static String TAG = "Sign_up";
    private FirebaseAuth mAuth;
    private String userEmail;
    private Intent moveToLogin;
    public boolean is_password_confirm = false;
    private final long finishtimed=1500;
    private long presstime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mAuth = FirebaseAuth.getInstance();

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText passwordCheck = findViewById(R.id.Password_check);

        moveToLogin = new Intent(this, Login.class);

        Button Sign_up_Button = findViewById(R.id.Sign_up_button);

        Sign_up_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = email.getText().toString().trim();

                if (password.getText().toString().equals(passwordCheck.getText().toString()))
                    is_password_confirm = true;
                else
                    is_password_confirm = false;

                if(is_password_confirm){
                    if(email.length() == 0 || password.length() == 0){
                        Toast.makeText(getApplicationContext(), "비밀번호와 이메일 입력", Toast.LENGTH_SHORT).show();
                    } else createAccount(email.getText().toString().trim(), password.getText().toString().trim());
                }
                else{
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        long tempTime=System.currentTimeMillis();
        long intervalTime=tempTime-presstime;

        if(0<=intervalTime&&finishtimed>=intervalTime){
            finish();
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        }
        else{
            presstime=tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 가시려면 한번 더 눌러주세요", Toast.LENGTH_SHORT).show();
        }

    }
    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "SignUpSuccess");
                            Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", userEmail);
                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                            database.collection("Users").document(mAuth.getCurrentUser().getUid()).set(hashMap);


                            startActivity(moveToLogin);
                            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                            finish();
                        }
                        else{
                            if(password.length() < 6){
                                Log.w(TAG, "PasswordError", task.getException());
                                Toast.makeText(getApplicationContext(), "비밀번호를 n자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.w(TAG, "EmailAccessError", task.getException());
                                Toast.makeText(getApplicationContext(), "이미 가입 된 이메일입니다", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                    }
                });

    }


}
