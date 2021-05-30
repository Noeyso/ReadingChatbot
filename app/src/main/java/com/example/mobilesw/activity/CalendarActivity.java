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
    public Button cha_Btn,del_Btn,save_Btn,mov_Btn;
    public TextView BookTitle,Review;
    public EditText contextEditText;
    private FirebaseFirestore db;
    final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String userId = null; // 현재 user id

    Map<String, String> calendarMap = new HashMap<>();
    MaterialCalendarView materialCalendarView;
    ArrayList<String> result = new ArrayList<>();
    ArrayList<String> selectedDay = new ArrayList<>(); //확정된 날짜
    SimpleDateFormat transDate = new  SimpleDateFormat("yyyy-MM-dd hh:mm:ss", java.util.Locale.getDefault());//String을 Date 형식으로 변경
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

        BookTitle=findViewById(R.id.BookTitle);
        save_Btn=findViewById(R.id.save_Btn);
        del_Btn=findViewById(R.id.del_Btn);
        cha_Btn=findViewById(R.id.cha_Btn);
        Review=findViewById(R.id.Review);
        contextEditText=findViewById(R.id.contextEditText);

        Review.setMovementMethod(new ScrollingMovementMethod());

        db = FirebaseFirestore.getInstance();
        Intent intent=getIntent();
        userId = user.getUid();

        mov_Btn = (Button)findViewById(R.id.mov_Btn);
        mov_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);

                startActivity(intent);

            }

        });

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) { //달력 날짜가 선택되면
                BookTitle.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                Review.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);

                int Year = date.getYear();
                int Month = date.getMonth()+1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                final String shot_Day = Year + "-" + Month + "-" + Day;

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();

                BookTitle.setText(String.format("%d / %d / %d",Year,Month,Day)); // 날짜를 보여주는 텍스트에 해당 날짜를 넣음
                contextEditText.setText("");
                checkDay(shot_Day);

                save_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { //저장 버튼 클릭
                        str=contextEditText.getText().toString(); // EditText 내용을 str에 저장

                        calendarMap.put(shot_Day, str);

                        Review.setText(str); // TextView에 str 출력
                        save_Btn.setVisibility(View.INVISIBLE); // 저장 버튼 Invisible
                        cha_Btn.setVisibility(View.VISIBLE); // 수정 버튼 Visible
                        del_Btn.setVisibility(View.VISIBLE); // 삭제 버튼 Visible
                        contextEditText.setVisibility(View.INVISIBLE);
                        Review.setVisibility(View.VISIBLE);

                        result.add(shot_Day);   // result에 메모가 저장된 날짜 추가
                        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor()); // 데코 표시
                    }
                });
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
            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
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

            contextEditText.setVisibility(View.INVISIBLE);
            Review.setVisibility(View.VISIBLE);
            Review.setText(str);

            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);

            cha_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // 수정 버튼 클릭
                    contextEditText.setVisibility(View.VISIBLE);
                    Review.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str); // editText에 textView에 저장된 내용 출력

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    Review.setText(contextEditText.getText());
                }

            });
            del_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // 삭제 버튼 클릭
                    Review.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);

                    calendarMap.remove(shot_Day);

                    result.remove(shot_Day);
                    if(selectedDay.contains(shot_Day)){
                        selectedDay.clear();
                    }
                    removeDeco();
                }
            });
            if(Review.getText()==null){
                Review.setVisibility(View.INVISIBLE);
                BookTitle.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

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

