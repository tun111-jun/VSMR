package com.example.app3;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nullable;

public class Connect_server extends AppCompatActivity {
    Button send_button;
    EditText send_editText;
    TextView send_textView;
    TextView read_textView;
    private Socket client;
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;
    private static String SERVER_IP = "210.102.178.119";
    private static String CONNECT_MSG = "connect";
    private static String STOP_MSG = "stop";

    private static int BUF_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connectserver);
        send_button = findViewById(R.id.send_button);
        send_editText = findViewById(R.id.send_editText);
        send_textView = findViewById(R.id.send_textView);
        read_textView = findViewById(R.id.read_textView);


        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connect connect = new Connect();
                connect.execute(CONNECT_MSG);
            }
        });
    }

    private class Connect extends AsyncTask<  String , String,Void > {
        private byte[] output_message;
        private String input_message;
        private String img_path;
        Intent intent = getIntent();
        byte[] image_byte = getIntent().getByteArrayExtra("Date");

        @Override
        protected Void doInBackground(@Nullable String... strings) {
            try {
                client = new Socket(SERVER_IP, 12125);
                dataOutput = new DataOutputStream(client.getOutputStream());
                dataInput = new DataInputStream(client.getInputStream());
                StringBuilder sb = new StringBuilder();

                for(byte b : image_byte)
                    sb.append(String.format("%02X", b&0xff));

                System.out.println("BYTEARRAY : "+ new BigInteger(image_byte).toString(16));
                System.out.println("BYTEARRAY11 : "+ image_byte.length);
                Log.d("BYTE", new BigInteger(image_byte).toString(16));
                Log.d("BYTE", Integer.toString(image_byte.length));

            } catch (UnknownHostException e) {
                String str = e.getMessage().toString();
                Log.w("discnt", str + " 1");
            } catch (IOException e) {
                String str = e.getMessage().toString();
                Log.w("discnt", str + " 2");
            }

            while (true){
                try {
                    dataOutput.writeUTF(Integer.toString(image_byte.length));
                    dataOutput.flush();

                    dataOutput.write(image_byte);
                    dataOutput.flush();
                    img_path=readString(dataInput);
                    client.close();

                    if (!input_message.equals(STOP_MSG)){
                        publishProgress(input_message);
                    }
                    else{
                        break;
                    }
                    Thread.sleep(2);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public String readString(DataInputStream dis)throws IOException{
            int length=dataInput.readInt();
            byte[] data=new byte[length];
            dataInput.readFully(data,0,length);
            String text=new String(data, StandardCharsets.UTF_8);
            return text;
        }


    }
}
