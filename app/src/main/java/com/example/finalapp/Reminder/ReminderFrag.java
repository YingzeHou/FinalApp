package com.example.finalapp.Reminder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.example.finalapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderFrag extends Fragment {
    private ListView lv;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] myDataset;
    private MyAdapter.MyViewHolder[] viewHolders;
    public static ArrayList<Todo> todos = new ArrayList<>();
    private RecyclerView timeLineRecyclerView;

    List<TimeLineModel> timeLineModelList;
    TimeLineModel[] timeLineModel;
    Context context;
    LinearLayoutManager linearLayoutManager;

    Calendar c;

    private NotificationManagerCompat notificationManager;
    public static final String CHANNEL_1_ID = "alarmChannel";

    public ReminderFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        notificationManager = NotificationManagerCompat.from(this.getContext());

        Context context = this.getContext().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("todos", Context.MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        todos = dbHelper.readTodos();
        lv = view.findViewById(R.id.notesListView);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getContext());
        myDataset = new String[todos.size()];

        viewHolders = new MyAdapter.MyViewHolder[todos.size()];

        for (int i = 0; i < todos.size(); i++) {
//            myDataset[i] = String.format("%d. %s", i + 1, todos.get(i).getContent());
            myDataset[i] = todos.get(i).getContent();
        }

        lv.setAdapter(new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, myDataset) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                if (position % 4 == 0)
                    v.setBackgroundColor(Color.rgb(205, 255, 253));
                else if (position % 4 == 1)
                    v.setBackgroundColor(Color.rgb(252, 255, 205));
                else if (position % 4 == 2)
                    v.setBackgroundColor(Color.rgb(255, 229, 229));
                else
                    v.setBackgroundColor(Color.rgb(255, 175, 175));
                return v;
            }
        });

        SearchView searchView = view.findViewById(R.id.searchBar);
        searchView.setBackgroundColor(Color.rgb(163,201,192));
        searchView.setQueryHint("Search Todo");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ((ArrayAdapter)lv.getAdapter()).getFilter().filter(s);
                return false;
            }
        });

        Context finalContext = context;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchView.clearFocus();
                Bundle bundle = new Bundle();
                for (int j = 0; j < myDataset.length; j++) {
                    if (myDataset[j].compareTo((String) lv.getAdapter().getItem(i)) == 0) {
                        bundle.putInt("todoId", j);
                        break;
                    }
                }
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                Fragment fragment = new EditTodoFrag();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
            }
        });

        timeLineModelList = new ArrayList<>();
        int size = todos.size();
        timeLineModel = new TimeLineModel[size];
        context = this.getContext();
        linearLayoutManager = new LinearLayoutManager(this.getContext());

        c = Calendar.getInstance();
        for (int i = 0; i < size; i++) {
            timeLineModel[i] = new TimeLineModel();
            Todo todo = todos.get(i);
            timeLineModel[i].setName(String.format("Todo%d", i + 1));
            String curDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
            String curTime = String.format("%02d:%02d", c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
            String todoDateAndTime = todo.getDate() + todo.getTime();
            if (todoDateAndTime.compareTo(curDate + curTime) < 0) {
                timeLineModel[i].setStatus("active");
            }
            else
                timeLineModel[i].setStatus("inactive");
            if (todo.getDate().compareTo(curDate) == 0)
                sendOnChannel1(view);
            timeLineModel[i].setDescription(todo.getContent());
            timeLineModel[i].setDate(todo.getDate());
            timeLineModel[i].setTime(todo.getTime());
            timeLineModel[i].setAddress("CS407\n@CS Building 1240");
            timeLineModelList.add(timeLineModel[i]);
        }
        timeLineRecyclerView = (RecyclerView) view.findViewById(R.id.timeLineView);
        timeLineRecyclerView.setLayoutManager(linearLayoutManager);
        timeLineRecyclerView.setAdapter(new TimeLineAdapter(context, timeLineModelList));


        ImageButton addToDoButton = view.findViewById(R.id.addTodoButton);
        addToDoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                Fragment fragment = new AddToDoFrag();
                fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
            }
        });

        return view;
    }

    public void sendOnChannel1(View v) {
        String title = "Made My Day";
        String message = "You have some todos today!";
        Intent activityIntent = new Intent(this.getContext(), ReminderFrag.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this.getContext(),
                0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this.getContext(), NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this.getContext(),
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this.getContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_hourglass_bottom_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
//                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                .build();

        notificationManager.notify(1, notification);
    }

}