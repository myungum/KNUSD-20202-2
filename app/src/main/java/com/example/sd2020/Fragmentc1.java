package com.example.sd2020;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sd2020.Schedule.Schedule;
import com.example.sd2020.Schedule.ScheduleEventListener;
import com.example.sd2020.Schedule.Work;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import me.jlurena.revolvingweekview.WeekView;
import me.jlurena.revolvingweekview.WeekViewEvent;

import static android.app.Activity.RESULT_OK;

public class Fragmentc1 extends Fragment{
    Schedule schedule;
    private final int ACTIVITY_CODE = 1;
    private String groupId;
    public Fragmentc1(String groupId) {
        this.groupId = groupId;
    }

    // DateTimePickerActivity 로부터 결과를 받기 위함
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            if (requestCode == ACTIVITY_CODE) {
                String extra = intent.getStringExtra("result");
                if (extra != null && extra.contentEquals("accept")) {
                    Work work = (Work)intent.getSerializableExtra("work");
                    try {
                        schedule.addWork(work);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "오류", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_fragment_c1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get a reference for the week view in the layout.
        final WeekView mWeekView = (WeekView)getView().findViewById(R.id.revolving_weekview);
        this.schedule = new Schedule(groupId, new ScheduleEventListener() {
            @Override
            public void onScheduleChanged(final HashMap<String, List<Work>> works) {
                // fragment가 죽지 않는 상황 예외처리
                try {
                    mWeekView.setWeekViewLoader(new WeekView.WeekViewLoader() {
                        @Override
                        public List<? extends WeekViewEvent> onWeekViewLoad() {
                            List<WeekViewEvent> events = new ArrayList<>();

                            Calendar c = Calendar.getInstance();
                            for (int i = 0; i < 7; i++) {
                                String date = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                                int week = (c.get(Calendar.DAY_OF_WEEK) - 1 + 6) % 7 + 1; // 일=1 에서 월=1로 변경
                                if (works.containsKey(date)) {
                                    for (Work work : works.get(date)) {
                                        WeekViewEvent event = work.getWeekViewEvent(week);
                                        events.add(event);
                                    }
                                    c.add(Calendar.DATE, 1);
                                }
                            }
                            return events;
                        }
                    });
                    mWeekView.notifyDatasetChanged();
                    Toast.makeText(getActivity(), "스케줄이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Log.e("fragmentc1", e.getMessage());
                }
            }
        });

        // 환자는 삭제 권한 없음
        // mWeekView.setEventLongPressListener(new WeekView.EventLongPressListener();

        mWeekView.setWeekViewLoader(new WeekView.WeekViewLoader() {
            @Override
            public List<? extends WeekViewEvent> onWeekViewLoad() {
                return new ArrayList<WeekViewEvent>();
            }
        });
    }
}