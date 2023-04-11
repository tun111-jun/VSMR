package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class VavalEditPage extends AppCompatActivity implements View.OnClickListener {

    private ImageView emotionClick;
    private int clickX, clickY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaval_edit_page);

        emotionClick = findViewById(R.id.emotionClick);
        emotionClick.setOnClickListener(this);
    }

  public void onClick(View view){
      clickX = (int) view.getX(); // 클릭된 뷰의 x 좌표
      clickY = (int) view.getY(); // 클릭된 뷰의 y 좌표
  }
}