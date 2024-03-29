package com.example.app3;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Connect_server extends AppCompatActivity {
    Button send_button;
    EditText send_editText;
    TextView send_textView;
    TextView read_textView;

    AppCompatDialog dialog;
    //ProgressDialog dialog;
    private Socket client;
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;
    private static String SERVER_IP = "210.102.178.119";
    private static String CONNECT_MSG;
    private static String STOP_MSG = "stop";

    private static int BUF_SIZE = 100;

    ImageView img_loading_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connectserver);


        //new
        dialog = new AppCompatDialog(Connect_server.this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.connectserver);
        dialog.show();

        img_loading_frame = dialog.findViewById(R.id.progressImage);
        AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(() -> frameAnimation.start());



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

                output_message = CONNECT_MSG;
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
                    InputStreamReader inputStreamReader = new InputStreamReader(dataInput, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    line = reader.readLine();

                    System.out.println("line! : " + line);
                    if (line != null) {
                        dialog.dismiss();
                        publishProgress(input_message);
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