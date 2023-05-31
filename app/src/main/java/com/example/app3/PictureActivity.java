package com.example.app3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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
        temp_ky=temp_ky.split("\\.")[0];
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

                        ProgressDialog progressDialog = new ProgressDialog(PictureActivity.this);
                        progressDialog.setMessage("Uploading...");
                        progressDialog.setCancelable(false); // 터치를 막기 위해 취소 불가능 설정
                        progressDialog.show(); // ProgressDialog 표시
                        uri = result.getData().getData();
                        StorageReference storageRef=storage.getReference();

                        StorageReference riversRef=storageRef.child(keyword);
                        UploadTask uploadTask=riversRef.putFile(uri);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // 업로드가 완료되었을 때 실행되는 코드
                                        Log.d("Upload", "사진이 성공적으로 업로드되었습니다.");
                                        progressDialog.dismiss();
                                        try{

                                            InputStream in=getContentResolver().openInputStream(result.getData().getData());
                                            Bitmap img=BitmapFactory.decodeStream(in);
                                            in.close();
                                            imageView.setImageBitmap(img);

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


                                        }catch (FileNotFoundException e){
                                            e.printStackTrace();
                                        }catch (IOException e){
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        // 업로드 진행 상황을 표시하는 부분
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                        // 진행 상황을 프로그레스 바에 업데이트
                                        progressDialog.setProgress((int) progress);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // 업로드 실패 시 실행되는 코드
                                        Log.e("Upload", "사진 업로드에 실패했습니다.", e);

                                        progressDialog.dismiss();
                                    }
                                });

                    }
                }
            }
    );
}