package com.example.finalapp.CountDown;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
        CardView card;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            timelineView = itemView.findViewById(R.id.row_timeline_layout_time_marker);
            textView = itemView.findViewById(R.id.row_timeline_layout_text_view_name);
            textViewDescription = itemView.findViewById(R.id.event);
            textViewTime = itemView.findViewById(R.id.date);
            textViewAddress = itemView.findViewById(R.id.address);
            timelineView.initLine(viewType);
            textDays = itemView.findViewById(R.id.days);
            card = (CardView) itemView.findViewById(R.id.card);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(itemView.getContext(), "Clicked Card...", Toast.LENGTH_LONG).show();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    CountdownEditEventFrag fragment = new CountdownEditEventFrag();
                    Bundle bundle = new Bundle();

                    String eventName = String.valueOf(textViewDescription.getText());
                    String eventDate = String.valueOf(textViewTime.getText());
                    String end = eventName.substring(eventName.length()-4, eventName.length()-1);
                    if (end.equalsIgnoreCase("een")){
                        eventName = eventName.substring(0,eventName.length()-10);
                    }
                    else{
                        eventName = eventName.substring(0,eventName.length()-4);
                    }

                    bundle.putString("event", eventName);
                    bundle.putString("date", eventDate);
                    fragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, fragment).addToBackStack(null).commit();
                }
            });
        }

    }
}