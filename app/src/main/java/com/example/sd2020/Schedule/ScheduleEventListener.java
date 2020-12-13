package com.example.sd2020.Schedule;

import java.util.HashMap;
import java.util.List;

public interface ScheduleEventListener {
    void onScheduleChanged(HashMap<String, List<Work>> works);
}