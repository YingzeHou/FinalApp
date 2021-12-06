package com.example.finalapp.CountDown;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalapp.R;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TimeLineModel> timeLineModelList;
    private Context context;
    private String[] mDataset;

    TimeLineAdapter(Context context, List<TimeLineModel> timeLineModelList) {
        this.timeLineModelList = timeLineModelList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewFuture = LayoutInflater.from(parent.getContext()).inflate(R.layout.countdown_layout_future, parent, false);

        GradientDrawable gd = new GradientDrawable();
        GradientDrawable gd2 = new GradientDrawable();
        gd.setCornerRadius(100);
        gd2.setCornerRadius(50);
        if (viewType == 0) {    //future event
            gd.setColor(Color.parseColor("#87CEFA"));   //blue
            gd2.setColor(Color.parseColor("#72005EFF"));
        } else if (viewType == 1) { //past event
            gd.setColor(Color.parseColor("#FFB952"));   //orange
            gd2.setColor(Color.parseColor("#FF9800"));
        } else {
            System.out.println("viewType exception, please check value of viewType");
            //something is wrong nothing should happen here
        }
        viewFuture.findViewById(R.id.card).setBackground(gd);
        viewFuture.findViewById(R.id.smallCard).setBackground(gd2);
        return new ViewHolder(viewFuture, viewType);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).textViewDescription.setText(timeLineModelList.get(position).getDescription());
        ((ViewHolder) holder).textViewTime.setText(timeLineModelList.get(position).getDate());
        ((ViewHolder) holder).textDays.setText(timeLineModelList.get(position).getDiff() + " Days");
    }

    @Override
    public int getItemViewType(int position) {
        if (timeLineModelList.get(position).getPast().equalsIgnoreCase( "false")){  //future event
            return 0;
        }
        else if (timeLineModelList.get(position).getPast().equalsIgnoreCase("true")){
            return 1;
        }
        else {  //shouldn't happen
            System.out.println("something is wrongggggggg");
            return TimelineView.getTimeLineViewType(position, getItemCount());
        }
    }

    @Override
    public int getItemCount() {
        return timeLineModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TimelineView timelineView;
        TextView textView, textViewDescription, textViewTime, textViewAddress, textDays;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            timelineView = itemView.findViewById(R.id.row_timeline_layout_time_marker);
            textView = itemView.findViewById(R.id.row_timeline_layout_text_view_name);
            textViewDescription = itemView.findViewById(R.id.event);
            textViewTime = itemView.findViewById(R.id.date);
            textViewAddress = itemView.findViewById(R.id.address);
            timelineView.initLine(viewType);
            textDays = itemView.findViewById(R.id.days);
        }
    }
}