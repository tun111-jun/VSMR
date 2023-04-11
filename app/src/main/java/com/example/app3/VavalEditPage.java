package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class VavalEditPage extends AppCompatActivity {

    private ImageView emotionClick;
    private int clickX, clickY;
    private FrameLayout flContainer;
    int image_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaval_edit_page);

//        flContainer = findViewById(R.id.flContainer);
//        flContainer.setOnClickListener(this);

        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        Button backButton = findViewById(R.id.filteringBack);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float clickX = v.getX()/2 + v.getWidth()/2;
                float clickY = v.getY()/2 + v.getHeight()/2;

                ImageView imageView = new ImageView(VavalEditPage.this);
                imageView.setImageResource(R.drawable.party);
                imageView.setX((clickX - imageView.getWidth())/2);
                imageView.setY((clickY - imageView.getHeight())/2);

                relativeLayout.addView(imageView);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToList = new Intent(getApplicationContext(), Youtube.class);
                startActivity(backToList);
            }
        });

    }


//  public void onClick(View view){
//      clickX = (int) view.getX(); // 클릭된 뷰의 x 좌표
//      clickY = (int) view.getY(); // 클릭된 뷰의 y 좌표
//      ImageView imageView = new ImageView(this);
//      imageView.setImageResource(R.drawable.party);
//      imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//      int size = getResources().getDimensionPixelSize(R.dimen.image_size);
//      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);
//      layoutParams.leftMargin = clickX - size/2;
//      layoutParams.topMargin = clickY - size/2;
//      imageView.setLayoutParams(layoutParams);
//
//      flContainer.addView(imageView);
//
//  }

}