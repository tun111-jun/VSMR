package com.example.app3;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import io.feeeei.circleseekbar.CircleSeekBar;

public class VavalEditPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaval_edit_page);
    }

    public void setListeners() {
        CircleSeekBar circleSeekBar = findViewById(R.id.seek_bar);
        circleSeekBar.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(CircleSeekBar seekBar, int progress) {
                // Seek Bar 값이 변경되었을 때 동작할 코드
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
