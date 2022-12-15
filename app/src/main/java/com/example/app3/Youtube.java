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
    ImageView imageView1;
    private final long finishtimed=1500;
    private long presstime=0;

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

        imageView1=findViewById(R.id.imageView1);


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
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                n=0;
                playVideo(n);

            }

        });

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listitem){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)

            {

                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.WHITE);

                return view;

            }
        };
        //
        ListView listview1=findViewById(R.id.listView1);
        listview1.setAdapter(adapter);
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             // 콜백매개변수는 순서대로 어댑터뷰, 해당 아이템의 뷰, 클릭한 순번, 항목의 아이디
                                             @Override
                                             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                 //플레이리스트의 임의의 음악을 선택하였을 때 재생될 수 있게 작성
                                                 Toast.makeText(getApplicationContext(),listitem.get(i).toString() + " 재생.",Toast.LENGTH_SHORT).show();
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
            player.pause();
        }
        System.out.println("Video_ID: "+videoId);
        //썸네일 갖고오는 라이브러리 각 음악마다 가져올 수 있는 방법 생각해봐야함
        Glide.with(this).load("https://img.youtube.com/vi/"+videoId[num]+"/"+"default.jpg").into(imageView1);
        //player.loadVideos(videoId);

        //음악재생
        player.cueVideo(videoId[num]);

    }

}



