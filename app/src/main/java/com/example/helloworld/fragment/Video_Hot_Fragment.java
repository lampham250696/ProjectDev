package com.example.helloworld.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld.Activity.PlayActivity;
import com.example.helloworld.Adapter.VideoAdapter;
import com.example.helloworld.Entity.Video;
import com.example.helloworld.Interface.VideoClick;
import com.example.helloworld.R;
import com.example.helloworld.Web_API.CallAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Video_Hot_Fragment extends Fragment {

    RecyclerView recyclerView;
    List<Video> videoList;
    Video top_video;
    String json;
    TextView tv_loading_hotvideo, tv_top_title, tv_top_artis, tv_top_date;
    String videoUrl;
    ProgressBar pb_video_hot;
    LinearLayout layout_top_video;
    ImageView iv_top_img;


    public Video_Hot_Fragment(String url) {
        this.videoUrl = url;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot_video, container, false);
        pb_video_hot = view.findViewById(R.id.pb_video_hot);
        recyclerView = view.findViewById(R.id.rv_video);
        tv_loading_hotvideo = view.findViewById(R.id.tv_loading_hotvideo);
        tv_top_title = view.findViewById(R.id.tv_top_title);
        tv_top_artis = view.findViewById(R.id.tv_top_artis);
        tv_top_date = view.findViewById(R.id.tv_top_date);
        layout_top_video = view.findViewById(R.id.layout_top_video);
        iv_top_img = view.findViewById(R.id.iv_top_img);

        videoList = new ArrayList<>();
        new VideoHTTP(videoUrl).execute();

        return view;
    }

    public static Video_Hot_Fragment newInstance(String videoUrl) {
        return new Video_Hot_Fragment(videoUrl);
    }


    private List<Video> getListVideo(String json){
        List<Video> currentList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);
            int count = 0;
            while (jsonArray.getJSONObject(count) != null){
                JSONObject jsonObject = jsonArray.getJSONObject(count);

                String title = jsonObject.getString("title");
                String artis = jsonObject.getString("artist_name");
                String date = jsonObject.getString("date_published");
                String avt_url = jsonObject.getString("avatar");
                String mp4_url = jsonObject.getString("file_mp4");

                Video video = new Video(title, date, artis, avt_url, mp4_url);
                currentList.add(video);
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return currentList;

    }

    private void setTopVideo(final Video video){
        tv_top_title.setText(video.getTitle());
        tv_top_artis.setText(video.getArtis_name());
        tv_top_date.setText(video.getDate_public());
        Picasso.with(getContext()).load(video.getAvt_url()).into(iv_top_img);
        layout_top_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PlayActivity.class);
                intent.putExtra("video", video);
                startActivity(intent);
            }
        });
    }


    class VideoHTTP extends AsyncTask<String, Void, String> {
        String url;
        public VideoHTTP(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            tv_loading_hotvideo.setText(getString(R.string.loading));
            pb_video_hot.setIndeterminate(true);
            pb_video_hot.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
             json = new CallAPI().getJsonFromWeb(url);
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(json != null){
                tv_loading_hotvideo.setText("");
                pb_video_hot.setVisibility(View.INVISIBLE);
                videoList = getListVideo(json);
                setTopVideo(videoList.get(0));
                videoList.remove(0);
                VideoAdapter videoAdapter = new VideoAdapter(videoList, getContext(), new VideoClick() {
                    @Override
                    public void onClick(Video video) {
                        Intent intent = new Intent(getContext(), PlayActivity.class);
                        intent.putExtra("video", video);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(videoAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            }else{
                pb_video_hot.setVisibility(View.INVISIBLE);
                tv_loading_hotvideo.setText(getString(R.string.disconnect));
            }
        }

    }
}
