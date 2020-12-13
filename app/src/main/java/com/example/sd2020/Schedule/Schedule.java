package com.example.sd2020.Schedule;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.jlurena.revolvingweekview.WeekView;
import me.jlurena.revolvingweekview.WeekViewEvent;

public class Schedule {
    private String groupId;
    private HashMap<String, List<Work>> works;
    ScheduleEventListener scheduleEventListener;
    private transient DatabaseReference reference;

    public Schedule(String groupId, final ScheduleEventListener scheduleEventListener) {
        this.groupId = groupId;
        this.works = new HashMap<String, List<Work>>();
        this.scheduleEventListener = scheduleEventListener;

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.reference = db.getReference("schedules").child(groupId);
        reference.child("data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String base64string = dataSnapshot.getValue(String.class);
                if (base64string == null || base64string.length() == 0)
                    return;
                byte[] serializedMember = Base64.getDecoder().decode(base64string);
                try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember)) {
                    try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                        Object objectMember = ois.readObject();
                        works = (HashMap<String, List<Work>>) objectMember;
                        scheduleEventListener.onScheduleChanged(works);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    Log.i("ValueEventListener", e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ValueEventListener", "Failed to read value.", error.toException());
            }
        });
    }

    public void addWork(Work.WORK_TYPE workType, String name, int year, int month, int day, int stHour, int stMinute, int edHour, int edMinute) throws IOException {
        Work work = new Work(workType, name, year, month, day, stHour, stMinute, edHour, edMinute);
        addWork(work);
    }
    public void addWork(Work work) throws IOException {
        HashMap<String, List<Work>> worksCopy = (HashMap<String, List<Work>>)this.works.clone();
        // 해당하는 날짜가 없으면 추가
        if (!worksCopy.containsKey(work.getDate())) {
            worksCopy.put(work.getDate(), new ArrayList<Work>());
        }
        // work 추가
        worksCopy.get(work.getDate()).add(work);
        uploadSchedule(worksCopy);
    }


    public void removeWork(String id) throws IOException {
        HashMap<String, List<Work>> worksCopy = (HashMap<String, List<Work>>)this.works.clone();
        for (List<Work> worksCopyDay : worksCopy.values()) {
            for (int i = 0; i < worksCopyDay.size(); i++) {
                Work work = worksCopyDay.get(i);
                if (work.isSame(id)) {
                    worksCopyDay.remove(work);
                    uploadSchedule(worksCopy);
                    return;
                }
            }
        }

        Log.w("Schedule.removeWork()", "the work is not found");
    }

    public void uploadSchedule(HashMap<String, List<Work>> works) throws IOException {
        byte[] serializedMember;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(works);
                serializedMember = baos.toByteArray();
            }
        }

        // 바이트 배열로 생성된 직렬화 데이터를 base64로 변환
        String base64string = Base64.getEncoder().encodeToString(serializedMember);
        reference.child("data").setValue(base64string);
    }

    public String getUserId() {return groupId;}
}
