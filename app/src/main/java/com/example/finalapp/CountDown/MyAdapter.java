package com.example.finalapp.CountDown;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalapp.R;

import java.util.Random;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] mDataset;
    int i = 1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        //
        int red[] = new int[]{0,30,60,90,120,150,180,210,240,255};
        int green[] = new int[]{0,30,60,90,120,150,180,210,240,255};
        int blue[] = new int[]{0,30,60,90,120,150,180,210,240,255};

        Random rn = new Random();
        int randRed;
        int randGreen;
        int randBlue;
        do{
            randRed = rn.nextInt(8); //rand int from 0 to 9
            randGreen = rn.nextInt(8); //rand int from 0 to 9
            randBlue = rn.nextInt(8); //rand int from 0 to 9
        }
        while (Math.abs(randRed - randGreen) > 30 || Math.abs(randGreen - randBlue) > 30 || Math.abs(randRed - randBlue) > 30);


        System.out.println("randRed, randGreen, randBlue: " + randRed + " " + randGreen + " " + randBlue);
        v.setBackgroundColor(Color.rgb(red[randRed],green[randGreen],blue[randBlue]));

//        if (i % 4 == 0)
//           v.setBackgroundColor(Color.rgb(205, 255, 253));
//        else if (i % 4 == 1)
//            v.setBackgroundColor(Color.rgb(252, 255, 205));
//        else if (i % 4 == 2)
//            v.setBackgroundColor(Color.rgb(255, 229, 229));
//        else
//            v.setBackgroundColor(Color.rgb(255, 175, 175));
        i++;
        v.setTextColor(Color.rgb(81, 76, 84));
//        ...
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset[position]);

    }

//    public void setColor(MyViewHolder holder, int color) {
//        holder.textView.setBackgroundColor(color);
//    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}