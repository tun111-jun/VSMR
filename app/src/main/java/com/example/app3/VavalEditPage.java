package com.example.app3;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import io.feeeei.circleseekbar.CircleSeekBar;

public class VavalEditPage extends AppCompatActivity {

    private String Uid;
    private ImageView faceImage;
    private TextView faceText;
    private ImageView faceButton;
    private FirebaseAuth mAuth;

    private CircleSeekBar seekBar_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaval_edit_page);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        for(UserInfo profile : user.getProviderData()){

            Uid=profile.getUid();

            Log.d("TOKEN",Uid);

            //String UId=Uid;
        }

        Uid=Uid.split("\\.")[0];
        System.out.println("Intent_KEYWORD: "+Uid);

        faceImage = findViewById(R.id.faceImageView);
        faceText = findViewById(R.id.textview);
        faceButton=findViewById(R.id.imageView3);
        setListeners();
        faceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Connect_server.class);
                System.out.println("type:"+seekBar_temp.getCurProcess()+" "+seekBar_temp.toString()+" "+seekBar_temp);
                intent.putExtra("Date", Uid+","+String.valueOf(seekBar_temp.getCurProcess()));
                startActivity(intent);

                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }
        });
    }

    public void setListeners() {
        CircleSeekBar circleSeekBar = findViewById(R.id.seek_bar);
        circleSeekBar.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(CircleSeekBar seekBar, int progress) {
                // Seek Bar 값이 변경되었을 때 동작할 코드
                if (seekBar.getCurProcess() < 12.5) {
                    seekBar_temp=seekBar;
                    faceImage.setImageResource(R.drawable.sad);
                    faceText.setText("SAD");
                } else if (seekBar.getCurProcess() < 25) {
                    seekBar_temp=seekBar;
                    faceImage.setImageResource(R.drawable.excitement);
                    faceText.setText("EXCITEMENT");
                } else if (seekBar.getCurProcess() < 37.5) {
                    seekBar_temp=seekBar;
                    faceImage.setImageResource(R.drawable.angry);
                    faceText.setText("ANGRY");
                } else if (seekBar.getCurProcess() < 50) {
                    seekBar_temp=seekBar;
                    faceImage.setImageResource(R.drawable.amuse);
                    faceText.setText("AMUSE");
                } else if (seekBar.getCurProcess() < 62.5) {
                    seekBar_temp=seekBar;
                    faceImage.setImageResource(R.drawable.awe);
                    faceText.setText("AWESOME");
                } else if (seekBar.getCurProcess() < 75) {
                    seekBar_temp=seekBar;
                    faceImage.setImageResource(R.drawable.contentment);
                    faceText.setText("CONTENTMENT");
                } else if (seekBar.getCurProcess() < 87.5) {
                    seekBar_temp=seekBar;
                    faceImage.setImageResource(R.drawable.disgust);
                    faceText.setText("DISGUST");
                }
                else {
                    seekBar_temp=seekBar;
                    faceImage.setImageResource(R.drawable.fear);
                    faceText.setText("FEAR");
                }
            }


            public void onStartTrackingTouch(CircleSeekBar seekBar) {
                // Seek Bar를 터치하여 값을 변경하기 시작했을 때 동작할 코드
            }


            public void onStopTrackingTouch(CircleSeekBar seekBar) {
                // Seek Bar를 터치하여 값을 변경하는 것을 멈췄을 때 동작할 코드
            }

        });

    }

}
