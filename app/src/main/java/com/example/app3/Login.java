package com.example.app3;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class Login extends AppCompatActivity {

    private final static String TAG = "Login";
    private FirebaseAuth mAuth;
    private String Uid;
    boolean isLogin = false;
    Intent moveToYoutube;
    Intent moveToSelect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        EditText enterEmail = findViewById(R.id.Login_email);
        EditText enterPassword = findViewById(R.id.Login_password);

        Button Login_Button = findViewById(R.id.Login_button);
        Button Find_Password_Button = findViewById(R.id.Find_password);

        moveToYoutube = new Intent(this, Youtube.class);
        moveToSelect = new Intent(this, PictureActivity.class);
        Intent moveToFindPassword = new Intent(this, Find_Password.class);

        mAuth = FirebaseAuth.getInstance();

        Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enterEmail.length() == 0) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(enterPassword.length() < 6) {
                    Toast.makeText(getApplicationContext(), "비밀번호는 n자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();
                } else signIn(enterEmail.getText().toString().trim(), enterPassword.getText().toString().trim());
            }
        });

        Find_Password_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(moveToFindPassword);
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }
        });


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            return true;

        }
        return false;
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "SignInSuccess");
                            FirebaseUser user = mAuth.getCurrentUser();
                            isLogin = true;
                            for(UserInfo profile : user.getProviderData()){

                                Uid=profile.getUid();

                                Log.d("TOKEN",Uid);

                            }
                            //이후에 임의의 엑티비티에서 넘겨진 해당 Uid를 활용

                            moveToSelect.putExtra("Uid", Uid);
                            startActivity(moveToSelect);
                            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                            finish();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "로그인에 실패했습니다", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

    }

}