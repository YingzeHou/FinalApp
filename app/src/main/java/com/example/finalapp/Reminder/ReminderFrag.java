package com.example.finalapp.Reminder;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.Calendar.dao.Event;
import com.example.finalapp.R;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderFrag extends Fragment {

    public static com.example.finalapp.utils.DBHelper dbHelper2;

    private ListView lv;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> myDataset;
    private ArrayList<Event> eventList;
    private MyAdapter.MyViewHolder[] viewHolders;
    public static ArrayList<Todo> todos = new ArrayList<>();
    private RecyclerView timeLineRecyclerView;
    private ArrayAdapter arrayAdapter;
    private SearchView searchView;

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

        lv = view.findViewById(R.id.notesListView);
        eventList = dbHelper2.readEvents();

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String[] days = new String[7];
        for (int i = 0; i < 7; i++)
        {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        dbHelper.deleteEventsNotExist(eventList, days);

        for (int i = 0; i < eventList.size(); i++) {
            Event cur = eventList.get(i);
            if (!dbHelper.findEventInTodoDatabase(cur, days))
                dbHelper.saveTodos(cur.getEventName(), String.valueOf(days[cur.getWeekDay() - 1]), cur.getStartTime(), cur.getLocation(), "true", String.valueOf(todos.size() + i + 1));
        }
        todos = dbHelper.readTodos();

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getContext());
//        myDataset = new String[todos.size()];
        myDataset = new ArrayList<>();

        viewHolders = new MyAdapter.MyViewHolder[todos.size()];

        ArrayList<String> repeatedEventName = new ArrayList<>();
        for (int i = 0; i < todos.size(); i++) {
//            myDataset[i] = String.format("%d. %s", i + 1, todos.get(i).getContent());
//            myDataset[i] = todos.get(i).getContent();
            if (todos.get(i).getIsEvent().compareTo("true") == 0) {
                if (!repeatedEventName.contains(todos.get(i).getContent())) {
                    myDataset.add(todos.get(i).getContent());
                    repeatedEventName.add(todos.get(i).getContent());
                }
                continue;
            }
            myDataset.add(todos.get(i).getContent());
        }

        arrayAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, myDataset) {
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
        };

        lv.setAdapter(arrayAdapter);
        registerForContextMenu(lv);

//        lv.setAdapter(new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, myDataset) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                TextView v = (TextView) super.getView(position, convertView, parent);
//                if (position % 4 == 0)
//                    v.setBackgroundColor(Color.rgb(205, 255, 253));
//                else if (position % 4 == 1)
//                    v.setBackgroundColor(Color.rgb(252, 255, 205));
//                else if (position % 4 == 2)
//                    v.setBackgroundColor(Color.rgb(255, 229, 229));
//                else
//                    v.setBackgroundColor(Color.rgb(255, 175, 175));
//                return v;
//            }
//        });

        searchView = view.findViewById(R.id.searchBar);
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

//        Context finalContext = context;
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                searchView.clearFocus();
//                Bundle bundle = new Bundle();
//                for (int j = 0; j < myDataset.length; j++) {
//                    if (myDataset[j].compareTo((String) lv.getAdapter().getItem(i)) == 0) {
//                        bundle.putInt("todoId", j);
//                        break;
//                    }
//                }
//                FragmentManager fragmentManager = getParentFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
//                Fragment fragment = new EditTodoFrag();
//                fragment.setArguments(bundle);
//                fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
//            }
//        });
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                registerForContextMenu((TextView) lv.getChildAt(i));
//                return false;
//            }
//        });

        timeLineModelList = new ArrayList<>();
        int size = todos.size();
        timeLineModel = new TimeLineModel[size];
        context = this.getContext();
        linearLayoutManager = new LinearLayoutManager(this.getContext());

        c = Calendar.getInstance();
        for (int i = 0; i < size; i++) {
            timeLineModel[i] = new TimeLineModel();
            Todo todo = todos.get(i);
//            timeLineModel[i].setName(String.format("Todo%d", i + 1));
            String curDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
            String curTime = String.format("%02d:%02d", c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
            String todoDateAndTime = todo.getDate() + todo.getTime();
            if (todoDateAndTime.compareTo(curDate + curTime) < 0) {
                timeLineModel[i].setStatus("active");
            }
            else
                timeLineModel[i].setStatus("inactive");
//            if (todo.getDate().compareTo(curDate) == 0)
//                sendOnChannel1(view);
            timeLineModel[i].setName(todo.getIsEvent());
            timeLineModel[i].setDescription(todo.getContent());
            timeLineModel[i].setDate(todo.getDate());
            timeLineModel[i].setTime(todo.getTime());
            if (todo.getLocation().contains(","))
                timeLineModel[i].setAddress("@" + todo.getLocation().split(",")[0]);
            else
                timeLineModel[i].setAddress("@" + todo.getLocation());
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

    ListView selectedView = null;
    String selectedText = null;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        selectedView = (ListView) v;
        // you can set menu header with title icon etc
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedText = (String) lv.getItemAtPosition(acmi.position);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.cal_popup_menu, menu);
        for (int i = 0; i < todos.size(); i++) {
            if (selectedText.compareTo(todos.get(i).getContent()) == 0 && todos.get(i).getIsEvent().compareTo("true") == 0) {
                menu.removeItem(R.id.delEvent);
                menu.getItem(0).setTitle("View");
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.editEvent) {
            Bundle bundle = new Bundle();
            for (int i = 0; i < todos.size(); i++) {
                if (selectedText.compareTo(todos.get(i).getContent()) == 0) {
                    bundle.putInt("todoId", i);
                    break;
                }
            }
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
            Fragment fragment = new EditTodoFrag();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
            return true;
        }
        if(item.getItemId()==R.id.delEvent){
            Context context = this.getContext().getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("todos", Context.MODE_PRIVATE,null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
            AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(getContext());
            deleteConfirmation.setTitle("Do you want to delete this todo?")
                    .setMessage("Delete Confirmation")
                    .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < todos.size(); i++) {
                                if (selectedText.compareTo(todos.get(i).getContent()) == 0) {
                                    dbHelper.deleteTodo(todos.get(i).getId());
                                    refresh();
                                    break;
                                }
                            }
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            deleteConfirmation.create().show();
        }
        if(item.getItemId()==R.id.navEvent) {
            Context context = this.getContext().getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("todos", Context.MODE_PRIVATE,null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
            for (int i = 0; i < todos.size(); i++) {
                if (selectedText.compareTo(todos.get(i).getContent()) == 0) {
                    String location = todos.get(i).getLocation();
                    if(TextUtils.isEmpty(location) || location.compareTo("no location info") == 0){
                        Toast.makeText(getContext(),"Sorry! You haven't choose a location for this todo!", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    String lat = location.split(",")[1];
                    String lon = location.split(",")[2];
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon+"&mode=w");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }

        }
        return true;
    }

    public void refresh() {
        FragmentManager fragmentManager = getParentFragmentManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fragmentManager.beginTransaction().detach(this).commitNow();
            fragmentManager.beginTransaction().attach(this).commitNow();
        } else {
            fragmentManager.beginTransaction().detach(this).attach(this).commit();
        }
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.onActionViewCollapsed();
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