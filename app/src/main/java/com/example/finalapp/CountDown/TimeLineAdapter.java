package com.example.finalapp.CountDown;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalapp.R;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TimeLineModel> timeLineModelList;
    private Context context;

    TimeLineAdapter(Context context, List<TimeLineModel> timeLineModelList) {
        this.timeLineModelList = timeLineModelList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewFuture = LayoutInflater.from(parent.getContext()).inflate(R.layout.countdown_layout_future, parent, false);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_horizontal, parent, false);
//        viewFuture.setBackgroundColor(Color.rgb(252, 255, 205));
//        viewFuture.findViewById(R.id.card).setBackgroundColor(Color.rgb(205, 255, 253));
        GradientDrawable gd = new GradientDrawable();
        if (viewType == 1)
            gd.setColor(Color.rgb(205, 255, 253));
        else
            gd.setColor(Color.rgb(252, 255, 205));
        gd.setCornerRadius(15);
        viewFuture.findViewById(R.id.card).setBackground(gd);
        return new ViewHolder(viewFuture, viewType);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ((ViewHolder) holder).textView.setText(timeLineModelList.get(position).getName());
        ((ViewHolder) holder).textViewDescription.setText(timeLineModelList.get(position).getDescription());
        ((ViewHolder)holder).textViewTime.setText(timeLineModelList.get(position).getDate());
//        ((ViewHolder)holder).textViewAddress.setText(timeLineModelList.get(position).getAddress());

        if (timeLineModelList.get(position).getStatus().equals("inactive"))
//            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(ic_remove_circle_outline_black_24dp));
//            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(R.drawable.ic_baseline_remove_circle_outline_24));
            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(R.drawable.ic_marker_inactive));
        else
//            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(ic_check_circle_black_24dp));
//            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(R.drawable.ic_baseline_check_circle_24));
            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(R.drawable.ic_marker_active));
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return timeLineModelList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TimelineView timelineView;
        TextView textView, textViewDescription, textViewTime, textViewAddress;
        MaterialCardView cardColor;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            timelineView = itemView.findViewById(R.id.row_timeline_layout_time_marker);
            textView = itemView.findViewById(R.id.row_timeline_layout_text_view_name);
            textViewDescription = itemView.findViewById(R.id.row_timeline_layout_text_view_description);
            textViewTime = itemView.findViewById(R.id.row_timeline_layout_text_view_time);
            textViewAddress = itemView.findViewById(R.id.address);
            timelineView.initLine(viewType);
//            cardColor = itemView.findViewById(R.id.card);
//            cardColor.setBackgroundColor(Color.parseColor("#FF9800"));
        }
   }
}
