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
        if (viewType == 1) {
            //System.out.println("viewType isssssssssssssssssssssssss" + viewType);
            gd.setColor(Color.parseColor("#87CEFA"));   //blue
            gd2.setColor(Color.parseColor("#72005EFF"));
        }
        else {       //PROBLEM: viewType is wrong
            //System.out.println("viewType issssssssssssssssssssssssssssss" + viewType);
            gd.setColor(Color.parseColor("#FFB952"));   //orange
            gd2.setColor(Color.parseColor("#FF9800"));
        }
//        else{
//            System.out.println("viewType isssssssssssssssssssssssss " + viewType);
//        }
        viewFuture.findViewById(R.id.card).setBackground(gd);
        viewFuture.findViewById(R.id.smallCard).setBackground(gd2);
        return new ViewHolder(viewFuture, viewType);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ((ViewHolder) holder).textView.setText(timeLineModelList.get(position).getName());
        ((ViewHolder)holder).textViewDescription.setText(timeLineModelList.get(position).getDescription());
        ((ViewHolder)holder).textViewTime.setText(timeLineModelList.get(position).getDate());
        ((ViewHolder)holder).textDays.setText(timeLineModelList.get(position).getDiff() + " Days");
//        ((ViewHolder)holder).textViewAddress.setText(timeLineModelList.get(position).getAddress());

        if (timeLineModelList.get(position).getStatus().equals("inactive"))
//            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(ic_remove_circle_outline_black_24dp));
//            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(R.drawable.ic_baseline_remove_circle_outline_24));
            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(R.drawable.ic_marker_inactive));
        else
//            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(ic_check_circle_black_24dp));
//            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(R.drawable.ic_baseline_check_circle_24));
            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(R.drawable.ic_marker_active));

        //holder.textView.setText(mDataset[position]);
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return timeLineModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TimelineView timelineView;
        TextView textView, textViewDescription, textViewTime, textViewAddress, textDays;
        MaterialCardView cardColor;

        //long diff = getDiff();

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            timelineView = itemView.findViewById(R.id.row_timeline_layout_time_marker);
            textView = itemView.findViewById(R.id.row_timeline_layout_text_view_name);
            textViewDescription = itemView.findViewById(R.id.event);
            textViewTime = itemView.findViewById(R.id.date);
            textViewAddress = itemView.findViewById(R.id.address);
            timelineView.initLine(viewType);
            textDays = itemView.findViewById(R.id.days);
            //textDays.setText(String.valueOf(diff) + " Days");
        }
   }

    private long getDiff() {
        TimeLineModel timeLineModel = new TimeLineModel();
        String date = timeLineModel.getDate();
        String curDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

        if (curDate.compareTo(date) < 0){   //future events
            timeLineModel.setPast("false");
            return(getDaysBetweenDates(curDate,date));
        }
        else {          //past events
            timeLineModel.setPast("true");
            return(getDaysBetweenDates(date, curDate));
        }
    }

    public static final String DATE_FORMAT = "MM/dd/yyyy";

    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }
}
