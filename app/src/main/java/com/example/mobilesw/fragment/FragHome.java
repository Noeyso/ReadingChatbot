package com.example.mobilesw.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.mobilesw.R;
import com.example.mobilesw.adapter.ChatAdapter;
import com.example.mobilesw.info.ChatItem;
import com.example.mobilesw.info.MemberInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragHome extends Fragment {
    FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference chatRef;
    private String userId;

    private Button msgBtn;
    private Button startBtn;
    private Button menuBtn;
    private EditText et;
    private ListView listView;
    private BottomNavigationView chatNavi;

    private ArrayList<ChatItem> messageItems=new ArrayList<>();
    private ChatAdapter adapter;
    private SharedPreferences sp;

    public FragHome() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_home, container, false);

        msgBtn = (Button) view.findViewById(R.id.msgBtn);
        startBtn = (Button) view.findViewById(R.id.startBtn);
        menuBtn = view.findViewById(R.id.chatmenuBtn);
        et=view.findViewById(R.id.et);
        listView=view.findViewById(R.id.listView);
        listView.setVisibility(listView.INVISIBLE);
        chatNavi = view.findViewById(R.id.chatMenuNavi);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        firebaseDatabase= FirebaseDatabase.getInstance();
        chatRef= (DatabaseReference) firebaseDatabase.getReference("chat").child(userId);
        Query chatQuery = chatRef.orderByChild("timestamp");


        //RealtimeDB에서 채팅 메세지들 실시간 읽어오기..
        //'chat'노드에 저장되어 있는 데이터들을 읽어오기
        //chatRef에 데이터가 변경되는 것을 듣는 리스너 추가
        chatQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //새로 추가된 데이터(값 : MessageItem객체) 가져오기
                ChatItem messageItem= dataSnapshot.getValue(ChatItem.class);

                Calendar cal = Calendar.getInstance();
                Calendar temp = Calendar.getInstance();

                if(messageItems.size() == 0){
                    cal.setTimeInMillis(messageItem.getTimestamp());
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    ChatItem date = new ChatItem();
                    int nyear = cal.get(Calendar.YEAR);
                    date.setId("date");
                    date.setTime(nyear + "년 " + (month + 1) + "월 " + day + "일");
                    messageItems.add(date);

                    //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                    messageItems.add(messageItem);

                }else{
                    System.out.println(messageItems);
                    if(messageItems.get(messageItems.size() - 1).getTimestamp() == null){
                        //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                        messageItems.add(messageItem);
                        adapter.notifyDataSetChanged();
                        listView.setSelection(messageItems.size()-1);

                    }else {

                        cal.setTimeInMillis(messageItems.get(messageItems.size() - 1).getTimestamp());
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        temp.setTimeInMillis(messageItem.getTimestamp());
                        int nyear = temp.get(Calendar.YEAR);
                        int nmonth = temp.get(Calendar.MONTH);
                        int nday = temp.get(Calendar.DAY_OF_MONTH);

                        // 마지막 메세지보다 날짜가 지난 경우
                        if ((year < nyear) || (month < nmonth) || (month == nmonth && day < nday)) {
                            ChatItem date = new ChatItem();
                            date.setId("date");
                            date.setTime(nyear + "년 " + (nmonth + 1) + "월 " + nday + "일");
                            messageItems.add(date);
                        }
                        //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                        messageItems.add(messageItem);
                        adapter.notifyDataSetChanged();
                        listView.setSelection(messageItems.size() - 1);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        adapter=new ChatAdapter(messageItems,getLayoutInflater());
        listView.setAdapter(adapter);
        listView.setVisibility(listView.VISIBLE);


        startBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) { clickStart(v); }
        });

        msgBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) { clickSend(v); }
        });

        // + 버튼 클릭시 채팅 메뉴 Toggle
        menuBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(chatNavi.getVisibility() == View.GONE){
                    chatNavi.setVisibility(View.VISIBLE);
                }else{
                    chatNavi.setVisibility(View.GONE);
                }
            }
        });

        // 채팅 메뉴 Navigation 클릭 시 행동
        chatNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.chat_timer:
                        sendMsg("지금부터 읽은 시간을 측정할게 시~작!","bot");
                        chatNavi.setVisibility(View.GONE);
                        //타이머 뷰 visible
                        //timer_layout.setVisibility(view.VISIBLE);
                        FragmentManager childFragment = getChildFragmentManager();
                        childFragment.beginTransaction().add(R.id.chat_layout,FragTimer.getInstance(0))
                                .addToBackStack(null).commit();
                        return true;
                    case R.id.chat_alarm:
                        sendMsg("알람을 설정할 시간을 입력해줘!","bot");
                        chatNavi.setVisibility(View.GONE);
                        return true;
                    case R.id.chat_question:;
                        sendMsg("책을 읽고 느낀점을 말해줘~","bot");
                        chatNavi.setVisibility(View.GONE);
                        return true;

                    case R.id.chat_report:
                        sendMsg("독후감 작성할 내용을 입력해줘!","bot");
                        chatNavi.setVisibility(View.GONE);
                        return true;

                }
                return false;
            }
        });


        return view;
    }

    // 시작 버튼 누른 후: 메뉴가 보이고 채팅봇의 초기 메세지가 나옴
    public void clickStart(View view) {
        System.out.println("시작");
        // 시작 버튼을 누르면 챗봇의 메세지와 메뉴 탭이 나타남
        startBtn.setVisibility(View.GONE);
        chatNavi.setVisibility(View.VISIBLE);
        sendMsg("안녕! 오늘도 재밌는 책 읽어보자", "bot");

    }


    // 전송 버튼 누른 후: 입력한 내용 DB에 저장, 화면 표시
    public void clickSend(View view) {
        // EditText의 문자열 DB에 전송
        String message= et.getText().toString();
        sendMsg(message, "user");

        //EditText에 있는 글씨 지우기
        et.setText("");

        //소프트키패드 안보이도록
        InputMethodManager imm=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);

    }


    public void sendMsg(String message, String type) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        ChatItem messageItem;
        //메세지 작성 시간 문자열로
        Calendar calendar= Calendar.getInstance();
        String time = timeFormat.format(calendar.getTime());
        Date now = new Date();
        Long timestamp = now.getTime();

        if(type.equals("bot")){
            //DB에 저장할 값들(닉네임, 메세지, 시간)
            messageItem= new ChatItem("bot", message, time, timestamp);
        }else{
            messageItem= new ChatItem("user", message, time, timestamp);
        }

        //DB에 메세지 등록
        chatRef.push().setValue(messageItem);

    }

}
