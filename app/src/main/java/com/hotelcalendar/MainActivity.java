package com.hotelcalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hotelcalendar.util.CalendarUtils;
import com.hotelcalendar.widget.MonthCalendarView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.month_view)
    MonthCalendarView month_view;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        int[] nowDay = CalendarUtils.getNowDayFromSystem();
        month_view.setMonth(nowDay[0], nowDay[1]);
        tv_title.setText(month_view.getCurrentYearAndMonth());
    }
}
