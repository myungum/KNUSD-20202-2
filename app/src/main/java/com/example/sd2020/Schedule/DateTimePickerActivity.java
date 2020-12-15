package com.example.sd2020.Schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sd2020.R;

public class DateTimePickerActivity extends AppCompatActivity {
    private final int NULL = -1;
    int year = NULL, month = NULL, day = NULL;
    int stHour = NULL, stMinute = NULL;
    int edHour = NULL, edMinute = NULL;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_p1_datetime_picker);

        // 날짜 선택 Plain Text
        final TextView plainTextDate = (TextView)findViewById(R.id.plainTextDate);
        plainTextDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            year = i;
                            month = i1;
                            day = i2;
                            plainTextDate.setText(i + "년 " + i1 + "월 " + i2 + "일 ");
                        }
                    };
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(DateTimePickerActivity.this, callback, year, month, day);
                    datePickerDialog.show();
                }
                return false;
            }
        });

        // 시작 시간 선택 Plain Text
        final TextView plainTextStartTime = (TextView)findViewById(R.id.plainTextStartTime);
        plainTextStartTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            stHour = i;
                            stMinute = i1;
                            plainTextStartTime.setText(i + "시 " + i1 + "분");
                        }
                    };
                    final Calendar c = Calendar.getInstance();
                    int hour = (0 <= stHour && stHour < 24) ? stHour : c.get(Calendar.HOUR);
                    int minute = (0 <= stMinute && stMinute < 60) ? stMinute : c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(DateTimePickerActivity.this, callback, hour, minute, true);
                    timePickerDialog.show();
                }
                return false;
            }
        });

        // 종료 시간 선택 Plain Text
        final TextView plainTextEndTime = (TextView)findViewById(R.id.plainTextEndTime);
        plainTextEndTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            edHour = i;
                            edMinute = i1;
                            plainTextEndTime.setText(i + "시 " + i1 + "분");
                        }
                    };
                    final Calendar c = Calendar.getInstance();
                    int hour = (0 <= edHour && edHour < 24) ? edHour : c.get(Calendar.HOUR);
                    int minute = (0 <= edMinute && edMinute < 60) ? edMinute : c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(DateTimePickerActivity.this, callback, hour, minute, true);
                    timePickerDialog.show();
                }
                return false;
            }
        });

        // 취소 버튼
        Button buttonCancel = (Button)findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra("result", "cancel");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // 추가 버튼
        final TextView plainTextName = (TextView)findViewById(R.id.plainTextName);
        Button buttonAccept = (Button)findViewById(R.id.buttonAccept);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (plainTextName.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (year == NULL || month == NULL || day == NULL || plainTextDate.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (stHour == NULL || stMinute == NULL || plainTextStartTime.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "시작 시간을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edHour == NULL || edMinute == NULL || plainTextEndTime.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "종료 시간을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                int stTime = 60 * stHour + stMinute;
                int edTime = 60 * edHour + edMinute;
                if (stTime == edTime){
                    Toast.makeText(getApplicationContext(), "오류 : 시작 시간이 종료 시간과 같을 수 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (stTime > edTime) {
                    Toast.makeText(getApplicationContext(), "오류 : 시작 시간이 종료 시간보다 늦을 수 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                Work work = new Work(Work.WORK_TYPE.ONESHOT, plainTextName.getText().toString(), year, month, day, stHour, stMinute, edHour, edMinute);
                Intent intent = getIntent();
                intent.putExtra("result", "accept");
                intent.putExtra("work", work);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}