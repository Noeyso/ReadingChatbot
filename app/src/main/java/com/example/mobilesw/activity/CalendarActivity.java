package com.example.mobilesw.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilesw.R;
import com.example.mobilesw.activity.RecordActivity;
import com.example.mobilesw.decorators.EventDecorator;
import com.example.mobilesw.decorators.OneDayDecorator;
import com.example.mobilesw.decorators.SaturdayDecorator;
import com.example.mobilesw.decorators.SundayDecorator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class CalendarActivity extends AppCompatActivity {
    private View view;

    public String fname=null;
    public String str=null;
    private FirebaseFirestore db;
    final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String userId = null; // 현재 user id
    Intent intent = getIntent();
    private Button add_record;

    Map<String, String> calendarMap = new HashMap<>();
    MaterialCalendarView materialCalendarView;
    ArrayList<String> result = new ArrayList<>();
    ArrayList<String> selectedDay = new ArrayList<>(); //확정된 날짜
    SimpleDateFormat transDate = new  SimpleDateFormat("yyyy-MM-dd hh:mm:ss", java.util.Locale.getDefault());
    SimpleDateFormat transString = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_calendar);
        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator(CalendarActivity.this));


        db = FirebaseFirestore.getInstance();
        Intent intent=getIntent();
        userId = user.getUid();

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) { //달력 날짜가 선택되면
                add_record.setVisibility(View.VISIBLE);

                int Year = date.getYear();
                int Month = date.getMonth()+1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                final String shot_Day = Year + "-" + Month + "-" + Day;

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();

                checkDay(shot_Day);
            }
        });
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        ArrayList<String> Time_Result;
        ApiSimulator(ArrayList<String> Time_Result) {
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < Time_Result.size(); i++) {
                String[] time = Time_Result.get(i).split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                calendar.set(year, month - 1, dayy);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }
            return dates;
        }
        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }
            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, CalendarActivity.this, 1));
        }
    }

    public void  checkDay(String shot_Day){
        try{
            str=new String(calendarMap.get(shot_Day)); //

            add_record.setVisibility(View.INVISIBLE);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeDeco(){
        materialCalendarView.removeDecorators();    // 모든 데코 지우기
        materialCalendarView.addDecorators( //모든 데코 표시
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator(CalendarActivity.this));
        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor()); // 데코 표시
    }
}