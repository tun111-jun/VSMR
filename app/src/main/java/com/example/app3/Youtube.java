package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Reader;


import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.common.base.CharMatcher;

import java.io.BufferedReader;
import java.io.Reader;

import org.json.JSONException;
import org.json.JSONTokener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Youtube extends YouTubeBaseActivity {
    YouTubePlayerView playerView;
    YouTubePlayer player;
    LinearLayout ll;
    //ImageButton temp=null;
    int temp_vid=-1;
    int flag=0;
    private ListView listview=null;
    //ImageButton imgbutton;
    //TextView textView;
    ImageView imageView1;
    private final long finishtimed=1500;
    ArrayAdapter<String> adapter;
    private long presstime=0;
    ArrayList<String> listitem_temp;

    ListView listview1;
    int n=0;
    AlertDialog.Builder oDialog;
    //API key AIzaSyDvycuGNf8DbrWxzRWkwQFsyREzkOju4TI
    private static String API_KEY = "AIzaSyDvycuGNf8DbrWxzRWkwQFsyREzkOju4TI";


    private static String Music_list="";
    private static String[] videoId =new String[]{};
    private static String[] temp_list =new String[]{};
    private static String[] id_list =new String[]{};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Music_list = intent.getStringExtra("List");
        temp_list=Music_list.split("   ");
        Music_list=temp_list[0];
        System.out.println("Music_list"+Music_list+"\n");
        System.out.println("temp_list"+temp_list[1]+"\n");



        String charsToRemove = "[]\"\'";
        Music_list = CharMatcher.anyOf(charsToRemove).removeFrom(Music_list);
        Music_list=Music_list.replaceAll(" ","");
        videoId = Music_list.split(",");


        initPlayer();
        oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);


        ArrayList<String> listitem=new ArrayList<String>();
        listitem_temp=new ArrayList<String>();

        temp_list[temp_list.length-1] = CharMatcher.anyOf(charsToRemove).removeFrom(temp_list[temp_list.length-1]);
        id_list=temp_list[temp_list.length-1].split(",");

        int num=id_list.length;
        //음악 리스트를 list에 추가
        for (int i = 0; i< num; i++) {

            listitem.add(id_list[i]);
            listitem_temp.add(id_list[i]);

        }




        //전체재생 버튼, 해당 버튼을 눌러도 되고 바로 리스트에 있는 임의의 음악을 눌러도 됨
        Button button = findViewById(R.id.playAll);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                n=0;
                if(temp_vid!=-1){
                    listitem_temp.set(temp_vid,id_list[temp_vid]);
                    adapter.notifyDataSetChanged();
                }

                temp_vid=n;
                listitem_temp.set(n,id_list[n]+" - Playing...");
                adapter.notifyDataSetChanged();
                playVideo(n);

            }

        });

        //플레이리스트 Back 버튼
        Button button3 = findViewById(R.id.Back);
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                long tempTime=System.currentTimeMillis();
                long intervalTime=tempTime-presstime;

                if(0<=intervalTime&&finishtimed>=intervalTime){
                    finish();
                    overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                }
                else{
                    presstime=tempTime;
                    Toast.makeText(getApplicationContext(), "한번 더 누르시면 홈화면으로 이동합니다", Toast.LENGTH_SHORT).show();
                }

            }

        });

        ////어댑터, 리스트뷰를 띄우고 그 위에 playlistlayout이 입혀져서 인지 리스트뷰 자체가 덮어져서 클릭이 안됨
        adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listitem_temp){
            private ArrayList<String> items=listitem;

            @Override
            public View getView(int position, View convertView, ViewGroup parent)

            {

                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.playlist, null);
                }

                // ImageView 인스턴스



                 //리스트뷰의 아이템에 이미지를 변경한다.(썸네일)
                System.out.println("where : "+position);
                ImageView imageView = (ImageView)v.findViewById(R.id.imageView);

                    for (int i = 0; i < num; i++) {
                        if (listitem_temp.get(i).equals(listitem.get(position))){
                            System.out.println("TO : "+i);
                            Glide.with(getApplicationContext()).load("https://img.youtube.com/vi/" + videoId[i] + "/" + "default.jpg").into(imageView);
                            imageView.setClipToOutline(true);
                        }
                    }




                //썸네일 클릭 시
                imageView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //playVideo(i);
                        n=position;
                        //player.cueVideo(items.get(position));
                        if(temp_vid!=-1){
                            listitem_temp.set(temp_vid,id_list[temp_vid]);
                            adapter.notifyDataSetChanged();
                        }
                        temp_vid=position;
                        listitem_temp.set(n,id_list[n]+" - Playing...");
                        adapter.notifyDataSetChanged();
                        player.loadVideo(videoId[position]);

                    }
                });


                //노래제목, 아티스트
                TextView textView = (TextView)v.findViewById(R.id.textView);

                textView.setText(listitem_temp.get(position));
                final String text = listitem_temp.get(position);



                //노래제목, 아티스트 클릭 시
                textView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {

                        n=position;
                        System.out.println("Here::"+position);

                        if(temp_vid!=-1){
                            listitem_temp.set(temp_vid,id_list[temp_vid]);
                            adapter.notifyDataSetChanged();
                        }
                        temp_vid=position;
                        listitem_temp.set(n,id_list[n]+" - Playing...");
                        adapter.notifyDataSetChanged();
                        player.loadVideo(videoId[position]);
                    }

                });



                return v;


            }
        };

        listview1=findViewById(R.id.listView1);
        listview1.setAdapter(adapter);

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             // 콜백매개변수는 순서대로 어댑터뷰, 해당 아이템의 뷰, 클릭한 순번, 항목의 아이디
                                             @Override
                                             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                 //플레이리스트의 임의의 음악을 선택하였을 때 재생될 수 있게 작성
                                                 //Toast.makeText(getApplicationContext(),listitem.get(i).toString() + " 재생.",Toast.LENGTH_SHORT).show();
                                                 n=i;
                                                 if(temp_vid!=-1){
                                                     listitem_temp.set(temp_vid,id_list[temp_vid]);
                                                     adapter.notifyDataSetChanged();
                                                 }
                                                 temp_vid=i;
                                                 listitem_temp.set(n,id_list[n]+" - Playing...");
                                                 adapter.notifyDataSetChanged();
                                                 System.out.println("Adapter: "+adapterView.getItemAtPosition(n).toString());

                                                 playVideo(n);

                                                 //adapter.notifyDataSetChanged();
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
            Toast.makeText(getApplicationContext(), "한번 더 누르시면 홈화면으로 이동합니다", Toast.LENGTH_SHORT).show();
        }

    }




    //https://www.youtube.com/channel/UC-OcDPFxfY9Hhdf5P9zq7-A
    public void initPlayer(){
        //재생화면
        playerView = findViewById(R.id.playerView);

        /* YouTubePlayerView 초기화 하기 */
        playerView.initialize(API_KEY, new YouTubePlayer.OnInitializedListener(){
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b){
                player = youTubePlayer;
                player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onPlaying() {
                        //아마 나갔다 들어왔을 때 3번째 노래에 멈춰있고 재생 누르면 첫 곡에 playing 되는 문제가 여기일듯
                        if(temp_vid!=-1){
                            listitem_temp.set(temp_vid,id_list[temp_vid]);
                            adapter.notifyDataSetChanged();
                        }
                        System.out.println("onPlaying");
                        listitem_temp.set(n,id_list[n]+" - Playing...");
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onPaused() {
//                        if(temp_vid!=-1){
//                            listitem_temp.set(temp_vid,videoId[temp_vid]);
//                            adapter.notifyDataSetChanged();
//                        }
                        System.out.println("onPaused");
                        listitem_temp.set(n,id_list[n]+" - Paused");
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onStopped() {
                        System.out.println("onStopped");
                    }

                    @Override
                    public void onBuffering(boolean b) {
                        System.out.println("onBuffering");
                    }

                    @Override
                    public void onSeekTo(int i) {

                    }
                });


                player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {

                        System.out.println("onLoading");

                    }


                    @Override
                    public void onLoaded(String id) {

                        System.out.println("onLoaded");
//                        ImageButton imgbutton1 = (ImageButton)findViewById(R.id.button);
//
//                        imgbutton1.setVisibility(View.INVISIBLE);
                        player.play();  // 동영상이 로딩되었으면 재생하기



                    }

                    @Override
                    public void onAdStarted() {
                        System.out.println("onAdStarted");
                    }

                    @Override
                    public void onVideoStarted() {
                        System.out.println("OvVideoStarted");
                    }

                    @Override
                    public void onVideoEnded() {

                        if(n>=videoId.length-1){

                            listitem_temp.set(n,id_list[n]+" - Paused");
                            adapter.notifyDataSetChanged();

                            //리스트의 음악이 모두 재생되면 pause or 처음부터 재생.
                            oDialog.setMessage("playback is over. Would you like to play from the beginning?")
                                    .setTitle("Tune Your Area")
                                    .setPositiveButton("Stop", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            player.pause();
                                            Log.i("Dialog", "Stop");
                                            Toast.makeText(getApplicationContext(), "Thanks", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .setNeutralButton("Play", new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            System.out.println("Here31::"+n);
                                            System.out.println("Video_ID: "+videoId.length);
                                            listitem_temp.set(n,id_list[n]);
                                            adapter.notifyDataSetChanged();
                                            n=0;
                                            temp_vid=n;
                                            System.out.println("Onclick : "+n+temp_vid);
                                            listitem_temp.set(n,id_list[n]+" - Playing...");
                                            adapter.notifyDataSetChanged();
                                            System.out.println("Onclick2 : "+videoId[0]+listitem_temp.get(0));
                                            System.out.println("Onclick3 : "+n+temp_vid);
                                            playVideo(n);
                                            //player.loadVideo(videoId[num]);
                                        }
                                    })
                                    .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.


                                    .show();


                        }
                        else{
                            //재생 중인 음악이 종료되면 다음 음악으로 넘어감
                            System.out.println("Here1::"+n);
                            System.out.println("Video_ID: "+videoId.length);
                            listitem_temp.set(n,id_list[n]);
                            adapter.notifyDataSetChanged();
                            n=n+1;
//                        if(temp!=null)
//                            temp.setVisibility(View.VISIBLE);
                            temp_vid=n;
                            listitem_temp.set(n,id_list[n]+" - Playing...");
                            adapter.notifyDataSetChanged();
                            playVideo(n);
                        }


                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {}
                });
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult){}
        });
    }

    public void playVideo(int num){
        if(player != null){
            if(player.isPlaying())
                System.out.println("isPlaying");
                player.pause();
        }



//                        if(temp!=null)
//                            temp.setVisibility(View.VISIBLE);
        System.out.println("Video_ID: "+videoId.length);

            player.loadVideo(videoId[num]);






        //Glide.with(this).load("https://img.youtube.com/vi/"+videoId[num]+"/"+"default.jpg").into(imageView1);
        //player.loadVideos(videoId);

        //음악재생
        //player.cueVideo(videoId[num]);

        //loadVideo로 하니까 광고가 안뜸


    }

}



