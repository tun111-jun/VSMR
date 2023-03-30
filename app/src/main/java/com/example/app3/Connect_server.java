package com.example.app3;



import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

public class Connect_server extends AppCompatActivity {
    Button send_button;
    EditText send_editText;
    TextView send_textView;
    TextView read_textView;
    ProgressDialog dialog;
    private Socket client;
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;
    private static String SERVER_IP = "210.102.178.119";
    private static String CONNECT_MSG;
    private static String STOP_MSG = "stop";

    private static int BUF_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connectserver);



        dialog = new ProgressDialog(
                Connect_server.this);
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        dialog.setMessage("Creating a playlist...");

        // show dialog
        dialog.show();

        ImageView piano = (ImageView) findViewById(R.id.gif_image);
        DrawableImageViewTarget gifImage = new DrawableImageViewTarget(piano);
        Glide.with(this).load(R.drawable.piano).into(gifImage);

        Intent intent = getIntent();
        CONNECT_MSG = intent.getStringExtra("Date");
        System.out.println("String" + CONNECT_MSG);
        Connect connect = new Connect();
        connect.execute(CONNECT_MSG);

    }

    private class Connect extends AsyncTask<String, String, Void> {
        private String output_message;
        private String input_message;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                client = new Socket(SERVER_IP, 12125);
                dataOutput = new DataOutputStream(client.getOutputStream());
                dataInput = new DataInputStream(client.getInputStream());
                output_message = strings[0];
                dataOutput.writeUTF(output_message);

            } catch (UnknownHostException e) {
                String str = e.getMessage().toString();
                Log.w("discnt", str + " 1");
            } catch (IOException e) {
                String str = e.getMessage().toString();
                Log.w("discnt", str + " 2");
            }


            try {

                String line = "";
                while (true) {
                    line = dataInput.readUTF();
                    System.out.println("line! : " + line);
                    if (line != null) {
                        dialog.dismiss();
                        publishProgress(input_message);
                        System.out.println("InPut_MeSSAGE! : " + input_message);
                        Intent intent = new Intent(getApplicationContext(), Youtube.class);
                        intent.putExtra("List", line);
                        startActivity(intent);
                        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                    } else {

                    }
                    Thread.sleep(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... params) {
            send_textView.setText(""); // Clear the chat box
            send_textView.append("보낸 메세지: " + output_message);

        }
    }
}