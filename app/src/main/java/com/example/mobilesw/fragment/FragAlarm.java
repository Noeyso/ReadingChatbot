package com.example.mobilesw.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mobilesw.AlarmReceiver;
import com.example.mobilesw.DeviceBootReceiver;
import com.example.mobilesw.R;
import com.google.android.material.chip.Chip;
import com.google.common.primitives.Booleans;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragAlarm extends Fragment {
    private static final String ARG_NO = "ARG_NO";

    private Button btn_calender,btn_close_alarm;
    private Button btn_set_alarm,btn_delete_alarm,btn_set_date;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private RelativeLayout layout_alarm,layout_date_picker;
    private LinearLayout layout_text;
    private TextView alarm_text;
    private AlarmManager alarmManager;

    private Chip mon,tue,wed,thu,fri,sat,sun;
    private int hour,minute;
    private float year,month,day;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db;
    DocumentReference docRef;


    public static FragAlarm getInstance(int no) {
        FragAlarm fragment = new FragAlarm();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NO, no);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FragAlarm() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_alarm, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout_alarm = view.findViewById(R.id.layout_alarm);
        layout_date_picker = view.findViewById(R.id.layout_date_picker);
        btn_calender = view.findViewById(R.id.btn_calender);
        timePicker = view.findViewById(R.id.alarm_timePicker);
        datePicker = view.findViewById(R.id.alarm_datePicker);
        btn_set_alarm =view.findViewById(R.id.btn_set_alarm);
        btn_delete_alarm = view.findViewById(R.id.btn_delete_alarm);
        btn_set_date = view.findViewById(R.id.btn_set_date);
        btn_close_alarm = view.findViewById(R.id.btn_close_alarm);
        layout_text = view.findViewById(R.id.layout_text);
        alarm_text = view.findViewById(R.id.alarm_text);
        mon = view.findViewById(R.id.cb_mon);
        tue = view.findViewById(R.id.cb_tue);
        wed = view.findViewById(R.id.cb_wed);
        thu = view.findViewById(R.id.cb_thu);
        fri = view.findViewById(R.id.cb_fri);
        sat = view.findViewById(R.id.cb_sat);
        sun = view.findViewById(R.id.cb_sun);


        btn_calender.setOnClickListener(clickListener);
        btn_set_alarm.setOnClickListener(clickListener);
        btn_delete_alarm.setOnClickListener(clickListener);
        btn_set_date.setOnClickListener(clickListener);
        btn_close_alarm.setOnClickListener(clickListener);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("alarm",Context.MODE_PRIVATE);
        String date_text=sharedPreferences.getString("date_text","");
        if(date_text!=null){
            alarm_text.setText(date_text);
            layout_text.setVisibility(layout_text.VISIBLE);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_calender:
                    layout_date_picker.setVisibility(layout_date_picker.VISIBLE);
                    break;
                case R.id.btn_set_date:
                    layout_date_picker.setVisibility(layout_date_picker.GONE);
                    year = datePicker.getYear();
                    month = datePicker.getMonth();
                    day = datePicker.getDayOfMonth();
                    System.out.println("선택한 년도  : "+year);
                    System.out.println("선택한 월  : "+month);
                    System.out.println("선택한 일  : "+day);
                    break;
                case R.id.btn_set_alarm:
                    //boolean[] week = {false,sun.isChecked(),mon.isChecked(),tue.isChecked(),wed.isChecked(),thu.isChecked(),fri.isChecked(),sat.isChecked()};
                    //setAlarm();
                    AlarmTask alarmTask = new AlarmTask();
                    alarmTask.execute();
                    break;
                case R.id.btn_delete_alarm:
                    unregistAlarm();
                    break;
                case R.id.btn_close_alarm:
                    layout_alarm.setVisibility(layout_alarm.GONE);
                    break;
            }
        }
    };
    private void setAlarm(){

        boolean[] week = {false,sun.isChecked(),mon.isChecked(),tue.isChecked(),wed.isChecked(),thu.isChecked(),fri.isChecked(),sat.isChecked()};


//        db = FirebaseFirestore.getInstance();
//        docRef = db.collection("users").document(user.getUid());
//        docRef.update("mybook", FieldValue.arrayUnion());

        if(Build.VERSION.SDK_INT>=23){
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
            System.out.println("시간 : "+hour+" 분 : "+minute);
        }else{
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
            System.out.println("시간 : "+hour+" 분 : "+minute);
        }

        Calendar calendar = Calendar.getInstance();
        if(year!=0){
            calendar.set(Calendar.YEAR,(int)year);
            calendar.set(Calendar.MONTH,(int)month);
            calendar.set(Calendar.DAY_OF_MONTH,(int)day);
        }
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        Date today = new Date();
        long intervalDay = 24*60*60*1000;//24시간
        long selectTime = calendar.getTimeInMillis();
        long currentTime = System.currentTimeMillis();

        //만약 설정한 시간이 현재 시간보다 작다면 알람이 부정확하게 울리기 때문에 다음날 울리게 설정
        if(currentTime>selectTime){
            selectTime+=intervalDay;
        }

        System.out.println("등록 버튼을 누른 시간 : "+today+" 설정한 시간 : "+calendar.getTime());

        System.out.println("calendar.getTimeInMillis() : "+calendar.getTimeInMillis());

        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE,1);
        }

        Date currentDateTime = calendar.getTime();
        String date_text="";
        if(Booleans.contains(week,true)) {
            for (int i = 0; i < week.length; i++) {
                if (week[i] == true) {
                    switch (i) {
                        case 1:
                            date_text += "일요일,";
                            break;
                        case 2:
                            date_text += "월요일,";
                            break;
                        case 3:
                            date_text += "화요일,";
                            break;
                        case 4:
                            date_text += "수요일,";
                            break;
                        case 5:
                            date_text += "목요일,";
                            break;
                        case 6:
                            date_text += "금요일,";
                            break;
                        case 7:
                            date_text += "토요일,";
                            break;
                    }
                }
            }
            date_text += new SimpleDateFormat("a hh시 mm분", Locale.getDefault()).format(currentDateTime);
        }else{
            date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분", Locale.getDefault()).format(currentDateTime);
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("alarm",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("date_text",date_text);
        editor.commit();
        alarm_text.setText(date_text);
        layout_text.setVisibility(layout_text.VISIBLE);
        Toast.makeText(getContext(),"알림이 설정되었습니다.",Toast.LENGTH_SHORT).show();



        diaryNotification(calendar);
    }

    void diaryNotification(Calendar calendar){

        boolean[] week = {false,sun.isChecked(),mon.isChecked(),tue.isChecked(),wed.isChecked(),thu.isChecked(),fri.isChecked(),sat.isChecked()};
        boolean[] week_copy=new boolean[8];

        for(int i=0;i<week.length;i++){
            week_copy[i]=week[i];
            System.out.println("week_copy["+i+"] :"+week_copy[i]);
        }

        Boolean dailyNotify = true;

        PackageManager pm = getContext().getPackageManager();
        ComponentName receiver = new ComponentName(getContext(), DeviceBootReceiver.class);

        Intent alarmIntent = new Intent(getContext(), AlarmReceiver.class);
        alarmIntent.putExtra("weekday",week_copy);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,alarmIntent,0);
        alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);

        if(dailyNotify){
            if(alarmManager!=null){
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                }
            }
            //부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        }
    }

    void unregistAlarm(){
        alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,alarmIntent,0);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(getContext(),"알림이 해재되었습니다.",Toast.LENGTH_SHORT).show();
        layout_text.setVisibility(layout_text.GONE);
    }

    private class AlarmTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setAlarm();
                }
            });
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            boolean[] week = {false,sun.isChecked(),mon.isChecked(),tue.isChecked(),wed.isChecked(),thu.isChecked(),fri.isChecked(),sat.isChecked()};
            for(int i=0;i<week.length;i++){
                if(week[i]==true){
                    switch (i) {
                        case 1:
                            sun.setChecked(false);
                            break;
                        case 2:
                            mon.setChecked(false);
                            break;
                        case 3:
                            tue.setChecked(false);
                            break;
                        case 4:
                            wed.setChecked(false);
                            break;
                        case 5:
                            thu.setChecked(false);
                            break;
                        case 6:
                            fri.setChecked(false);
                            break;
                        case 7:
                            sat.setChecked(false);
                            break;
                    }
                }
            }
        }
    }
}


