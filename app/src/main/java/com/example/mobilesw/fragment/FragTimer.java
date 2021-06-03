package com.example.mobilesw.fragment;

import android.animation.ObjectAnimator;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.mobilesw.R;

public class FragTimer extends Fragment {

    private static final String ARG_NO = "ARG_NO";

    private NumberPicker pick_minute,pick_second;
    private Button btn_start_timer,btn_cancel_timer,btn_close_timer; //타이머 시작,종료 버튼,창 닫기 버튼

    private LinearLayout layout_timer;
    private LinearLayout layout_set_time; //세팅 화면
    private RelativeLayout layout_show_time; //타이머 화면

    private CountDownTimer countDownTimer;

    private boolean timerRunning; //타이머 상태
    private boolean firstState;

    private TextView timeText;

    private ProgressBar progress;

    private long time = 0;
    private long tempTime=0;

    public static FragTimer fragTimer;


    public static FragTimer getInstance(int no) {

        FragTimer fragment = new FragTimer();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NO, no);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FragTimer() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_timer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pick_minute = view.findViewById(R.id.pick_minute);
        pick_second = view.findViewById(R.id.pick_second);
        btn_start_timer = view.findViewById(R.id.btn_start_timer);
        btn_cancel_timer = view.findViewById(R.id.btn_cancel_timer);
        btn_close_timer = view.findViewById(R.id.btn_close_timer);
        layout_timer = view.findViewById(R.id.layout_timer);
        layout_set_time = view.findViewById(R.id.layout_set_time);
        layout_show_time = view.findViewById(R.id.layout_show_time);
        timeText = view.findViewById(R.id.timeText);
        progress = view.findViewById(R.id.progress);

        pick_minute.setMinValue(0);
        pick_minute.setMaxValue(60);
        pick_minute.setValue(5);
        pick_second.setMinValue(0);
        pick_second.setMaxValue(59);
        pick_minute.setFormatter(nf);
        pick_second.setFormatter(nf);

        btn_start_timer.setOnClickListener(clickListener);
        btn_cancel_timer.setOnClickListener(clickListener);
        btn_close_timer.setOnClickListener(clickListener);
    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_start_timer:
                    firstState=true;

                    layout_set_time.setVisibility(layout_set_time.GONE);
                    layout_show_time.setVisibility(layout_show_time.VISIBLE);

                    startTimer();
                    break;
                case R.id.btn_cancel_timer:
                    layout_set_time.setVisibility(layout_set_time.VISIBLE);
                    layout_show_time.setVisibility(layout_show_time.GONE);

                    stopTimer();
                    break;
                case R.id.btn_close_timer:
                    layout_timer.setVisibility(layout_timer.GONE);
                    stopTimer();
                    break;
            }
        }
    };
    NumberPicker.Formatter nf = new NumberPicker.Formatter() {
        @Override
        public String format(int value) {
            return String.format("%02d",value);
        }
    };
    private void startTimer(){
        //처음이면 설정 타이머값을 사용한다.
        if(firstState){
            String minute = String.valueOf(pick_minute.getValue());
            String second = String.valueOf(pick_second.getValue());
            time = (Long.parseLong(minute)*60000)+(Long.parseLong(second)*1000)+1000;
            time-=1000;
        }else{
            time = tempTime;
            time-=1000;
        }
        System.out.println("time = "+time+"time = "+((int)(time)/10000));
        progress.setMax((int)(time));
        progress.setProgress((int)(time));

        countDownTimer = new CountDownTimer(time,1) {
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("시간 : "+millisUntilFinished);
                tempTime = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                System.out.println("종료되었습니다!");
                timeText.setText("00:00");
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone ringtone = RingtoneManager.getRingtone(getContext(), uri);
                ringtone.play();
            }
        }.start();
        timerRunning=true;
        firstState=false;
    }

    private void stopTimer(){
        countDownTimer.cancel();
        timerRunning=false;
    }

    private void updateTimer(){
        int minutes = (int)tempTime%3600000/60000;
        int seconds = ((int)tempTime%3600000%60000/1000);

        System.out.println("minute : "+minutes+"seconds"+seconds);

        String timeLeftText="";
        if(minutes<10)timeLeftText+="0";
        timeLeftText+=""+minutes+":";
        if(seconds<10) timeLeftText+="0";
        timeLeftText+=seconds;

        progress.setProgress((int)(tempTime));
        timeText.setText(timeLeftText);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
