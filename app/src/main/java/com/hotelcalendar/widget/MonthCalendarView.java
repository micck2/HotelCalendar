package com.hotelcalendar.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hotelcalendar.R;
import com.hotelcalendar.bean.CalendarBO;
import com.hotelcalendar.util.CalendarUtils;

import java.util.List;

/**
 * 自定义月份view
 *
 * @author lilin on 2018/1/18 8:50
 */

public class MonthCalendarView extends ViewGroup {
    /**
     * 列数
     */
    private int mColumn = 7;
    /**
     * 当前年月
     */
    private int mYear, mMonth;
    /**
     * 已选中开始结束的位置
     */
    private int mStartSelectedPos, mEndSelectedPos = -1;
    /**
     * 开始结束日期
     */
    private CalendarBO mStartCalender,mEndCalender;
    /**
     * 日期单选与否
     */
    private boolean mSingle = false;

    /**
     * 点击接口回调
     */
    private OnDateSelectListener mOnDateSelectListener;
    public void setOnDatePickUpListener(OnDateSelectListener onDateSelectListener) {
        mOnDateSelectListener = onDateSelectListener;
    }

    public interface OnDateSelectListener {
        void onDateSelect(CalendarBO calendarBO);
    }

    public MonthCalendarView(Context context) {
        super(context);
    }

    public MonthCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取整个布局的宽
        int parentWidth = MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(widthMeasureSpec,
               MeasureSpec.EXACTLY));
        //将宽度平均分成七份，每个item的宽高都等于它
        int itemWidth = parentWidth / mColumn;
        int itemHeight = itemWidth;

        int parentHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(MeasureSpec.makeMeasureSpec(itemWidth,MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(itemHeight,MeasureSpec.EXACTLY));
            //计算控件所需的高度
            if (i % mColumn == 0) {
                parentHeight += childView.getMeasuredHeight();
            }
        }
        setMeasuredDimension(parentWidth,parentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            View itemView = getChildAt(i);
            int columnCount = i % mColumn;
            int rowCount = i / mColumn;
            int itemWidth = itemView.getMeasuredWidth();
            int itemHeight = itemView.getMeasuredHeight();

            left = columnCount * itemWidth;
            top = rowCount * itemHeight;
            right = left + itemWidth;
            bottom = top + itemHeight;
            itemView.layout(left, top, right, bottom);
        }
    }

    /**
     * 供外部调用的日期构建方法
     * @param year
     * @param month
     */
    public void setMonth(int year, int month) {
        mYear = year;
        mMonth = month;
        removeAllViews();
        invalidateMonth();
    }

    /**
     * 展示上一个月
     */
    public void moveToPreMonth() {
        mMonth -= 1;
        invalidateMonth();
    }

    /**
     * 展示下一个月
     */
    public void moveToNextMonth() {
        mMonth += 1;
        invalidateMonth();
    }

    /**
     * 获取当前显示的年月
     * @return
     */
    public String getCurrentYearAndMonth() {
        return CalendarUtils.formatYearAndMonth(mYear, mMonth);
    }

    private void invalidateMonth() {
        List<CalendarBO> calendarList =  CalendarUtils.initDaysListOfMonth(mYear, mMonth);
        for (int i = 0, size = calendarList.size(); i < size; i++) {
            final CalendarBO calendarBO = calendarList.get(i);
            final View itemView = initDateView(calendarBO, i);
            addViewInLayout(itemView, i, itemView.getLayoutParams(), true);
        }
        //requestLayout();
    }

    /**
     * 日期item填充
     * @param calendarBO
     * @return
     */
    private View initDateView(final CalendarBO calendarBO,final int pos) {
        final View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_date_view,null);
        final TextView tv_date = itemView.findViewById(R.id.tv_date);
        final TextView tv_type = itemView.findViewById(R.id.tv_type);
        if (calendarBO.isCurrentMonth) {
            tv_date.setText(String.valueOf(calendarBO.day));
            tv_type.setText("");
            if (CalendarUtils.isToday(calendarBO)) {
                //tv_date.setBackgroundResource(R.drawable.shape_today_bg);
                tv_date.setText(R.string.calender_today);
                tv_date.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
            }
        }

        itemView.setOnClickListener(new OnClickListener() {
            @SuppressLint("CutPasteId")
            @Override
            public void onClick(View view) {
                if (!mSingle) {//双选
                    if (mStartCalender != null && mEndCalender != null) {
                        mStartCalender = null;
                        mStartSelectedPos = -1;
                        mEndCalender = null;
                        mEndSelectedPos = -1;
                        TextView tv_date;
                        for (int i = 0; i < getChildCount(); i++) {
                            getChildAt(i).setSelected(false);
                            ((TextView)getChildAt(i).findViewById(R.id.tv_type)).setText("");
                            tv_date = getChildAt(i).findViewById(R.id.tv_date);
                            if (!getContext().getString(R.string.calender_today).equals(tv_date.getText().toString())) {
                                tv_date.setTextColor(Color.parseColor("#000000"));
                            } else {
                                tv_date.setTextColor(Color.parseColor("#ff0000"));
                            }
                        }
                    }
                    if (mStartCalender == null) {
                        mStartCalender = calendarBO;
                        mStartSelectedPos = pos;
                        tv_date.setTextColor(Color.parseColor("#ffffff"));
                        tv_type.setText(R.string.calender_start);
                        itemView.setSelected(true);
                    } else {
                        if (mStartCalender.equals(calendarBO)) {
                            return;
                        }
                        if (!calendarBO.compare(mStartCalender)) {
                            /*Toast.makeText(getContext(),"结束时间必须大于开始时间",
                                    Toast.LENGTH_SHORT).show();*/
                            getChildAt(mStartSelectedPos).setSelected(false);
                            ((TextView)getChildAt(mStartSelectedPos).findViewById(R.id.tv_type)).setText("");
                            TextView tv_date_start = getChildAt(mStartSelectedPos).findViewById(R.id.tv_date);
                            if (!getContext().getString(R.string.calender_today).equals(tv_date_start.getText().toString())) {
                                tv_date_start.setTextColor(Color.parseColor("#000000"));
                            } else {
                                tv_date_start.setTextColor(Color.parseColor("#ff0000"));
                            }
                            mStartCalender = calendarBO;
                            mStartSelectedPos = pos;
                            tv_date.setTextColor(Color.parseColor("#ffffff"));
                            tv_type.setText(R.string.calender_start);
                            itemView.setSelected(true);
                            return;
                        }
                        mEndCalender = calendarBO;
                        mEndSelectedPos = pos;
                        tv_type.setText(R.string.calender_end);
                        TextView tv_date_selected;
                        for (int i = mStartSelectedPos; i <= mEndSelectedPos; i++) {
                            //itemView.setSelected(true);
                            getChildAt(i).setSelected(true);
                            tv_date_selected = getChildAt(i).findViewById(R.id.tv_date);
                            tv_date_selected.setTextColor(Color.parseColor("#ffffff"));
                        }
                    }
                    Log.e("pos===",mStartSelectedPos+"/"+mEndSelectedPos);
                } else {//单选
                    if (mStartSelectedPos == pos) {
                        return;
                    }
                    if (mStartSelectedPos != -1) {
                        getChildAt(mStartSelectedPos).setSelected(false);
                        ((TextView)getChildAt(mStartSelectedPos).findViewById(R.id.tv_type)).setText("");
                    }
                    itemView.setSelected(true);
                    tv_type.setText(R.string.calender_start);

                    if (mOnDateSelectListener != null) {
                        mOnDateSelectListener.onDateSelect(calendarBO);
                    }
                    mStartSelectedPos = pos;
                }
            }
        });

        return itemView;
    }
}
