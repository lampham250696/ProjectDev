package com.example.helloworld.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld.Entity.Video;
import com.example.helloworld.Interface.VideoClick;
import com.example.helloworld.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SuggestVideoAdapter extends RecyclerView.Adapter<SuggestVideoAdapter.Viewholder> {
    List<Video> videoList;
    Context context;
    VideoClick videoClick;
    Video playingVideo;

    public SuggestVideoAdapter(List<Video> videoList, Context context, VideoClick videoClick, Video playingVideo) {
        this.videoList = videoList;
        this.context = context;
        this.videoClick = videoClick;
        this.playingVideo = playingVideo;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_item_view, parent, false);
        SuggestVideoAdapter.Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        final Video video = videoList.get(position);
        if (position == 0) {
            holder.layout_item_video_view.setVisibility(View.GONE);
            holder.iv_top_img.setVisibility(View.GONE);
            holder.tv_top_title.setText(playingVideo.getTitle());
            holder.tv_top_artis.setText(checkNull(playingVideo.getArtis_name()));
            holder.tv_top_date.setText(formatDate(playingVideo.getDate_public()));
        }else{
            holder.layout_top_video.setVisibility(View.GONE);
            holder.tv_item_artis.setText(checkNull(video.getArtis_name()));
            holder.tv_item_date.setText(formatDate(video.getDate_public()));
            holder.tv_item_title.setText(video.getTitle());
            Picasso.with(context).load(video.getAvt_url()).into(holder.iv_item_img);
            holder.layout_item_video_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoClick.onClick(video);
                }
            });
        }
    }

    private String checkNull(String name){
        return name.equals("null") ? "" : name;
    }

    private String formatDate(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date output = null;
        try {
            output= simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("dd/MM/yyyy").format(output);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView tv_item_title, tv_top_title;
        TextView tv_item_artis, tv_top_artis;
        TextView tv_item_date, tv_top_date;
        LinearLayout layout_item_video_view;
        ImageView iv_item_img, iv_top_img;
        LinearLayout layout_top_video;
        CardView card_item_view;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tv_item_artis = itemView.findViewById(R.id.tv_item_artis);
            tv_top_artis = itemView.findViewById(R.id.tv_top_artis);
            tv_item_title = itemView.findViewById(R.id.tv_item_title);
            tv_top_title = itemView.findViewById(R.id.tv_top_title);
            tv_item_date = itemView.findViewById(R.id.tv_item_date);
            tv_top_date = itemView.findViewById(R.id.tv_top_date);
            iv_item_img = itemView.findViewById(R.id.iv_item_img);
            iv_top_img = itemView.findViewById(R.id.iv_top_img);
            layout_item_video_view = itemView.findViewById(R.id.layout_item_video_view);
            layout_top_video = itemView.findViewById(R.id.layout_top_video);
            card_item_view = itemView.findViewById(R.id.card_item_view);
        }
    }
}