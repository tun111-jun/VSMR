package com.example.app3;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import io.feeeei.circleseekbar.CircleSeekBar;

public class VavalEditPage extends AppCompatActivity {


    private ImageView faceImage;
    private TextView faceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaval_edit_page);

        faceImage = findViewById(R.id.faceImageView);
        faceText = findViewById(R.id.textview);

        setListeners();
    }

    public void setListeners() {
        CircleSeekBar circleSeekBar = findViewById(R.id.seek_bar);
        circleSeekBar.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(CircleSeekBar seekBar, int progress) {
                // Seek Bar 값이 변경되었을 때 동작할 코드
                if (seekBar.getCurProcess() < 12.5) {
                    faceImage.setImageResource(R.drawable.sad);
                    faceText.setText("SAD");
                } else if (seekBar.getCurProcess() < 25) {
                    faceImage.setImageResource(R.drawable.excitement);
                    faceText.setText("EXCITEMENT");
                } else if (seekBar.getCurProcess() < 37.5) {
                    faceImage.setImageResource(R.drawable.angry);
                    faceText.setText("ANGRY");
                } else if (seekBar.getCurProcess() < 50) {
                    faceImage.setImageResource(R.drawable.amuse);
                    faceText.setText("AMUSE");
                } else if (seekBar.getCurProcess() < 62.5) {
                    faceImage.setImageResource(R.drawable.awe);
                    faceText.setText("AWESOME");
                } else if (seekBar.getCurProcess() < 75) {
                    faceImage.setImageResource(R.drawable.contentment);
                    faceText.setText("CONTENTMENT");
                } else if (seekBar.getCurProcess() < 87.5) {
                    faceImage.setImageResource(R.drawable.disgust);
                    faceText.setText("DISGUST");
                }
                else {
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
