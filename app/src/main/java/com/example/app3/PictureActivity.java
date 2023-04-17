package com.example.app3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PictureActivity extends AppCompatActivity {

    Uri uri;
    ImageView imageView;
    Button button_send;
    Button back;
    private String keyword="<p>";
    private final long finishtimed=1500;
    private FirebaseStorage storage;
    private long presstime=0;
    Intent serv;
    //Intent serv = new Intent(this, Connect_server.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Intent intent = getIntent();
        String temp_ky=intent.getStringExtra("Uid");
        temp_ky=temp_ky.split("@")[0];
        System.out.println("Intent_KEYWORD: "+temp_ky);
        keyword = keyword+temp_ky;
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        imageView = findViewById(R.id.imageUpload);
        button_send=findViewById(R.id.Start);
        storage=FirebaseStorage.getInstance();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setType("image/*");
                serv = new Intent(view.getContext(), Connect_server.class);
                startActivityResult.launch(intent);
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }
        });

        back = findViewById(R.id.backPicture);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), SelectPage.class);
//                startActivity(intent);

                Intent intent = new Intent(view.getContext(), Youtube.class);
                startActivity(intent);
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                //finish();
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


    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK && result.getData() != null){
                        System.out.println("OnACtivityResult2");
                        uri = result.getData().getData();
                        StorageReference storageRef=storage.getReference();

                        StorageReference riversRef=storageRef.child(keyword);
                        UploadTask uploadTask=riversRef.putFile(uri);

                        try{
                            System.out.println("OnACtivityResult1");
                            InputStream in=getContentResolver().openInputStream(result.getData().getData());
                            Bitmap img=BitmapFactory.decodeStream(in);
                            in.close();
                            imageView.setImageBitmap(img);
                            System.out.println("OnACtivityResult3");
//                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                            System.out.println("Path : "+uri.getPath());
//
//                            imageView.setImageBitmap(bitmap);
//                            ByteArrayOutputStream stream=new ByteArrayOutputStream();
//
//                            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
//                            byte[] bytearray=stream.toByteArray();
//                            //String byteaaray= Base64.encodeToString(bytearray,Base64.NO_WRAP);
//                            //Bitmap compressedBitmap = BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);
//
                            if(button_send.getVisibility() == View.INVISIBLE){
                                button_send.setVisibility(View.VISIBLE);
                            }
                            button_send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    System.out.println("KEYWORD : "+keyword);
                                    serv.putExtra("Date", keyword);

                                    startActivity(serv);
                                    overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                                    //finish();
                                }
                            });
//                            serv.putExtra("Date", keyword);
//
//                            startActivity(serv);
//
//                            System.out.println("BYTEARRAY: "+bytearray);

                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}