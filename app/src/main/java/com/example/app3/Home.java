package com.example.app3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    private final long finishtimed=1500;
    private long presstime=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Button Sign_up_Button = findViewById(R.id.sign_up);
        Button Login_Button = findViewById(R.id.Login);

        Intent moveToLogin = new Intent(this, Login.class);
        Intent moveToSU = new Intent(this, Sign_up.class);


        //Login_Button.setOnClickListener(v->startActivity(moveToLogin));

        Login_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("11");
                startActivity(moveToLogin);
                System.out.println("22");
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }
        });

        Sign_up_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(moveToSU);
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }
        });



    }
    @Override
    public void onBackPressed() {
        long tempTime=System.currentTimeMillis();
        long intervalTime=tempTime-presstime;

        if(0<=intervalTime&&finishtimed>=intervalTime){
            finish();
        }
        else{
            presstime=tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르시면 종료합니다", Toast.LENGTH_SHORT).show();
        }

    }
}
