package com.example.app3;




import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
    private ListView listview=null;

    //TextView textView;
    ImageView imageView1;
    private final long finishtimed=1500;
    private long presstime=0;
    int flag=0;
    ListView listview1;
    int n=0;
    //API key AIzaSyDvycuGNf8DbrWxzRWkwQFsyREzkOju4TI
    private static String API_KEY = "AIzaSyDvycuGNf8DbrWxzRWkwQFsyREzkOju4TI";
    //음악 리스트, 각각 videoId로 구성 차후에 모델에서 넘어오는 url에서 동적으로 저장하여 사용.
    private static String[] videoId = new String[]{"p1hrdozsPcY","wJZwd8ZyD8Q","0vvCe4EHtus","H0NFTIhxOpc","zaIsVnmwdqg","U5TTMeIadME","ygTZZpVkmKg","iIWoYaJRryw","jU0V9AhFniI"};
    //private static ArrayList<String> videoId = new ArrayList<>(Arrays.asList("p1hrdozsPcY", "wJZwd8ZyD8Q"));
    //"p1hrdozsPcY", "wJZwd8ZyD8Q"


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPlayer();



        ArrayList<String> listitem=new ArrayList<String>();

        //imageView1=findViewById(R.id.imageView1);


        //Glide.with(this).load("https://img.youtube.com/vi/zaIsVnmwdqg/default.jpg").into(imageView1);
        //Glide.with(this).load("https://img.youtube.com/vi/"+videoId[i]+"/"+"default.jpg").into(imageView1);
        //String url1 ="https://img.youtube.com/vi/"+ id+ "/" + "default.jpg"

        int num = videoId.length;
        //음악 리스트를 list에 추가
        for (int i = 0; i< num; i++) {
            //Glide.with(this).load("https://img.youtube.com/vi/"+videoId[num]+"/"+"default.jpg").into(imageView1);
            listitem.add(videoId[i]);


        }
        //전체재생 버튼, 해당 버튼을 눌러도 되고 바로 리스트에 있는 임의의 음악을 눌러도 됨
        Button button = findViewById(R.id.playAll);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                n=0;
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
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listitem){
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
                ImageView imageView = (ImageView)v.findViewById(R.id.imageView);

                // 리스트뷰의 아이템에 이미지를 변경한다.(썸네일)
                for (int i = 0; i< num; i++) {
                    if(videoId[i].equals(items.get(position)))
                        Glide.with(getApplicationContext()).load("https://img.youtube.com/vi/"+videoId[i]+"/"+"default.jpg").into(imageView);
                        imageView.setClipToOutline(true);



                }
                //썸네일 클릭 시
                imageView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //playVideo(i);
                        n=position;
                        //player.cueVideo(items.get(position));
                        player.loadVideo(items.get(position));
                        //Toast.makeText(this, "text", Toast.LENGTH_SHORT).show();
                    }
                });


                //노래제목, 아티스트
                TextView textView = (TextView)v.findViewById(R.id.textView);
                textView.setText(items.get(position));
                final String text = items.get(position);
                //LinearLayout border=(LinearLayout)findViewById(R.id.border);

                //노래제목, 아티스트 클릭 시
                textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        //playVideo(i);
                        n=position;
                        System.out.println("Here::"+listview1.getItemAtPosition(position));
                        //border.setBackgroundResource(R.drawable.roundborder2);
                        //player.cueVideo(items.get(position));
                        player.loadVideo(items.get(position));
                        //playVideo(n);


                        //Toast.makeText(this, "text", Toast.LENGTH_SHORT).show();
                    }
                });

                ///////재생버튼 -> 생성하게 되면 재생버튼 클릭 시에만 노래 재생가능
//                ImageButton button = (ImageButton)v.findViewById(R.id.button);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //playVideo(i);
//                        n=position;
//                        //player.cueVideo(items.get(position));
//                        player.loadVideo(items.get(position));
//
//                        //Toast.makeText(this, "text", Toast.LENGTH_SHORT).show();
//                    }
//                });

                return v;
//                View view = super.getView(position, convertView, parent);
//
//                TextView tv = (TextView) view.findViewById(android.R.id.text1);
//
//                tv.setTextColor(Color.WHITE);

                //return view;

            }
        };
        //
        //ListView listview1=findViewById(R.id.listView1);
        listview1=findViewById(R.id.listView1);
        listview1.setAdapter(adapter);

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             // 콜백매개변수는 순서대로 어댑터뷰, 해당 아이템의 뷰, 클릭한 순번, 항목의 아이디
                                             @Override
                                             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                 //플레이리스트의 임의의 음악을 선택하였을 때 재생될 수 있게 작성
                                                 //Toast.makeText(getApplicationContext(),listitem.get(i).toString() + " 재생.",Toast.LENGTH_SHORT).show();
                                                 n=i;

                                                 playVideo(i);

                                                 adapter.notifyDataSetChanged();
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

                player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {}

                    @Override
                    public void onLoaded(String id) {

                        System.out.println("ID: "+id);
                        player.play();  // 동영상이 로딩되었으면 재생하기



                    }

                    @Override
                    public void onAdStarted() {}

                    @Override
                    public void onVideoStarted() {}

                    @Override
                    public void onVideoEnded() {

                        //재생 중인 음악이 종료되면 다음 음악으로 넘어감
                        n=n+1;
                        playVideo(n);

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
                player.pause();
        }
        if(num>videoId.length){
            //리스트의 음악이 모두 재생되면 pause, 다시 재생되게 하거나 shuffle 기능 등 추가 할 예정
            //노래가 다 끝나면 앱 자체가 종료 돼버리는데 문제가 아직 뭔지는 모르겠음
            num=0;
            n=0;
        }
        System.out.println("Video_ID: "+videoId);

        //Glide.with(this).load("https://img.youtube.com/vi/"+videoId[num]+"/"+"default.jpg").into(imageView1);
        //player.loadVideos(videoId);

        //음악재생
        //player.cueVideo(videoId[num]);

        //loadVideo로 하니까 광고가 안뜸
        player.loadVideo(videoId[num]);

    }

}



