package com.example.app3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.databinding.DataBindingUtil;


public class SelectPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectpage);



        ImageButton interiorbtn = (ImageButton) findViewById(R.id.interiorImage);
        ImageButton profilebtn = (ImageButton) findViewById(R.id.profileImage);

        Button gobtn = (Button) findViewById(R.id.next);

        Button hidden1 = (Button) findViewById(R.id.hidden1);
        Button in_hidden1 = (Button) findViewById(R.id.in_hidden1);

        Button hidden2 = (Button) findViewById(R.id.hidden2);
        Button in_hidden2 = (Button) findViewById(R.id.in_hidden2);

        gobtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }
        });

        in_hidden1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("확인","클릭");


                Intent intent = new Intent(getApplicationContext(), PictureActivity.class);
                startActivity(intent);
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }
        });

        in_hidden2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Log.d("확인","클릭");

//                hidden2.setBackgroundColor(context.getResources().getColor(R.color.fill));
//                in_hidden2.setBackgroundColor(context.getResources().getColor(R.color.out));
                Intent intent = new Intent(getApplicationContext(), PictureActivity.class);
                startActivity(intent);
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

}