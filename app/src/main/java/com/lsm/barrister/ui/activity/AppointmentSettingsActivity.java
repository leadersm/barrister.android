package com.lsm.barrister.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.data.entity.AppointmentSetting;
import com.lsm.barrister.data.io.Action;
import com.lsm.barrister.data.io.IO;
import com.lsm.barrister.data.io.app.GetMyAppointmentSettingsReq;
import com.lsm.barrister.data.io.app.SetAppointmentSettingsReq;
import com.lsm.barrister.ui.UIHelper;
import com.lsm.barrister.utils.DLog;
import com.lsm.barrister.utils.DateFormatUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 接单设置，接受预约设置
 * 1.按天
 * 2.按小时，每半小时一个分段
 */
public class AppointmentSettingsActivity extends BaseActivity {


    private static final String TAG = AppointmentSettingsActivity.class.getSimpleName();
    SetAppointmentSettingsReq mSetReq;
    GetMyAppointmentSettingsReq mGetReq;
    ViewPager viewPager;
    DatePagerAdapter mAdapter;

    List<AppointmentSetting> data = new ArrayList<>();

    String [] hourStrs;
    SmartTabLayout viewPagerTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_settings);
        setupToolbar();

        hourStrs = getResources().getStringArray(R.array.hours);

        mAdapter = new DatePagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(mAdapter);

        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCommit();
            }
        });

        loadMySettings();

    }

    /**
     * 加载我的预约设置
     */
    private void loadMySettings() {

        if(mGetReq==null){

            String today = DateFormatUtils.format(new Date(),"yyyy-MM-dd");
            mGetReq = new GetMyAppointmentSettingsReq(this,today);

        }

        mGetReq.execute(new Action.Callback<IO.GetAppointmentSettingsResult>() {

            @Override
            public void progress() {

            }

            @Override
            public void onError(int errorCode, String msg) {
                UIHelper.showToast(getApplicationContext(),msg);
            }

            String defaultSettings = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";

            /**
             * 向前补全日期
             * @param in
             */
            private void addForeDate(Date in){

                Date todayDate = DateFormatUtils.parse(DateFormatUtils.format(new Date(System.currentTimeMillis()),"yyyy-MM-dd"),"yyyy-MM-dd");

                if (in.after(todayDate)){

                    Date fore = new Date(in.getTime() - 24 * 3600 * 1000);

                    //默认不可接单
                    String dateStr = DateFormatUtils.format(fore, "yyyy-MM-dd");

                    DLog.d(TAG, "向前自动补全日期:" + dateStr);

                    AppointmentSetting tempSettings = new AppointmentSetting();
                    tempSettings.setDate(dateStr);
                    tempSettings.setSettings(defaultSettings);
                    data.add(0,tempSettings);

                    addForeDate(fore);
                }
            }

            /**
             * 向后补全日期
             * @param size
             * @param lastDate
             */
            private void addAfterSettings(int size, Date lastDate) {
                for (int i = 0; i < 7 - size; i++) {
                    //默认不可接单
                    Date date = new Date(lastDate.getTime() + (i + 1) * 24 * 3600 * 1000);
                    String dateStr = DateFormatUtils.format(date, "yyyy-MM-dd");
                    DLog.d(TAG, "自动补全日期:" + dateStr);
                    AppointmentSetting tempSettings = new AppointmentSetting();
                    tempSettings.setDate(dateStr);
                    tempSettings.setSettings(defaultSettings);
                    data.add(tempSettings);
                }
            }

            @Override
            public void onCompleted(IO.GetAppointmentSettingsResult result) {

                String today = DateFormatUtils.format(new Date(), "yyyy-MM-dd");

                List<AppointmentSetting> mySettings = result.appointmentSettings;

                if(mySettings == null){
                    mySettings = new ArrayList<>();
                }

                data.clear();
                data.addAll(mySettings);

                int size = data.size();

                if (size <= 7) {

                    String firstDay = data.isEmpty() ? today : data.get(0).getDate();
                    Date firstDate = DateFormatUtils.parse(firstDay, "yyyy-MM-dd");
//                  Date todayDate = DateFormatUtils.parse(today, "yyyy-MM-dd");

                    addForeDate(firstDate);

                    size = data.size();

                    String lastDay = data.isEmpty() ? today : data.get(data.size()-1).getDate();
                    Date lastDate = DateFormatUtils.parse(lastDay, "yyyy-MM-dd");

                    addAfterSettings(size, lastDate);
                }

                for (int i = 0; i < data.size(); i++) {

                    AppointmentSetting item = data.get(i);
                    String[] flags = item.getSettings().split(",");

                    List<AppointmentSetting.HourItem> hours = new ArrayList<>();

                    for (int j = 0; j < flags.length ; j++) {
                        AppointmentSetting.HourItem hour = new AppointmentSetting.HourItem();
                        hour.setEnable(!flags[j].equals("0"));//是否可预约  非0
                        hour.setChangeAble(!flags[j].equals("2"));//是否可改变预约状态 非2
                        hour.setHour(hourStrs[12+j]);
                        hour.setIndex(j);
                        hour.setDate(item.getDate());
                        hours.add(hour);
                    }

                    item.setHours(hours);

                }

                mAdapter.notifyDataSetChanged();
                viewPagerTab.setViewPager(viewPager);
            }
        });

    }

    /**
     * 提交请求
     */
    private void doCommit() {

        if(mSetReq != null && mSetReq.isLoading()){
            UIHelper.showToast(getApplicationContext(),"保存中，请稍候...");
            return;
        }

        StringBuffer sb = null;

        JSONArray jsonArray = new JSONArray();

        try {

            JSONObject obj = null;

            for(AppointmentSetting temp:data){

                obj = new JSONObject();

                sb = new StringBuffer();

                for(AppointmentSetting.HourItem hour:temp.getHours()){
                    sb.append(hour.isEnable() ? "1," : "0,");
                }

                temp.setSettings(sb.toString());

                obj.put("date",temp.getDate());
                obj.put("value",temp.getSettings());

                jsonArray.put(obj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSetReq = new SetAppointmentSettingsReq(this,jsonArray.toString());
        mSetReq.execute(new Action.Callback<Boolean>() {

            @Override
            public void progress() {
                progressDialog.setMessage("请稍候...");
                progressDialog.show();
            }

            @Override
            public void onError(int errorCode, String msg) {
                progressDialog.dismiss();
                UIHelper.showToast(getApplicationContext(), "设置失败：" + msg);
            }

            @Override
            public void onCompleted(Boolean aBoolean) {
                progressDialog.dismiss();

                if(aBoolean){
                    UIHelper.showToast(getApplicationContext(),getString(R.string.tip_settings_success));
                }
            }
        });


    }

    /**
     * @author lsm
     * @date 2015-4-6
     */
    public class DatePagerAdapter extends FragmentStatePagerAdapter {

        public DatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return data.get(position).getDate();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = DateFragment.newInstance(data.get(position));

            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_bespeak);

    }

    public static class DateFragment extends Fragment {

        public DateFragment() {
        }

        public static DateFragment newInstance(AppointmentSetting date) {
            DateFragment fragment = new DateFragment();
            Bundle args = new Bundle();
            args.putSerializable("daySettings", date);
            fragment.setArguments(args);
            return fragment;
        }

        AppointmentSetting daySettings;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            daySettings = (AppointmentSetting) getArguments().getSerializable("daySettings");
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_date, container, false);
            init(view);
            return view;
        }

        GridView mGridView;
        HalfHourAdapter mHourAdapter;
        CheckBox cb;
        private void init(View view) {

            cb = (CheckBox) view.findViewById(R.id.cb_day);
            cb.setChecked(!daySettings.getSettings().contains("0"));
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doSetDaySettings();
                }
            });

            mGridView = (GridView) view.findViewById(R.id.gridView);
            mHourAdapter = new HalfHourAdapter(getActivity(),R.layout.item_half_hour, daySettings.getHours());
            mGridView.setAdapter(mHourAdapter);
        }

        private void doSetDaySettings() {

            boolean dayEnable = cb.isChecked();

            daySettings.getHours();

            for(AppointmentSetting.HourItem temp : daySettings.getHours()){

                temp.setEnable(dayEnable);

            }

            mHourAdapter.notifyDataSetChanged();

        }

        class HalfHourAdapter extends ArrayAdapter {

            public HalfHourAdapter(Context context, int resource, List objects) {
                super(context, resource, objects);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                final AQuery holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate( R.layout.item_half_hour,parent,false);
                    holder = new AQuery(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (AQuery) convertView.getTag();
                }

                final AppointmentSetting.HourItem hour = (AppointmentSetting.HourItem) getItem(position);

                holder.id(R.id.cb_item_half_hour).checked(hour.isEnable()).enabled(hour.isChangeAble() && !isItemOutOfDate(hour.getDate(),hour.getHour())).text(hour.getHour()).clicked(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hour.setEnable(holder.id(R.id.cb_item_half_hour).isChecked());

                    }
                });

                return convertView;
            }
        }
    }


    private static boolean isItemOutOfDate(String date,String hour){

        String sHour = hour.split("~")[1];

        if(sHour.equals("00:00")){
            return false;
        }

        String inStr = date + " " + sHour;
        Date inDate = DateFormatUtils.parse(inStr, "yyyy-MM-dd HH:mm");

        Date nowDate = new Date(System.currentTimeMillis());

        if(inDate.before(nowDate)){
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mSetReq!=null && mSetReq.isLoading()){
            mSetReq.cancel();
            mSetReq = null;
        }

        if(mGetReq !=null && mGetReq.isLoading()){
            mGetReq = null;
        }

    }
}
