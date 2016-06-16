package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.androidquery.AQuery;
import com.lsm.barrister.R;
import com.lsm.barrister.app.AppConfig;
import com.lsm.barrister.data.entity.User;
import com.lsm.barrister.ui.fragment.AvaterCenterFragment;
import com.lsm.barrister.ui.fragment.HomeFragment;
import com.lsm.barrister.ui.fragment.LearningCenterFragment;
import com.lsm.barrister.ui.fragment.MyOrdersFragment;
import com.lsm.barrister.ui.widget.BottomNavigationItem;
import com.lsm.barrister.ui.widget.BottomNavigationView;

import java.util.HashMap;


/**
 * 主页，4个模块，HOME，我的订单，学习中心，个人中心
 */
public class MainActivity extends BaseActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    AQuery aq;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    BottomNavigationView bottomNavigationView;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = AppConfig.getUser(this);

        setupBottomNav();

        setupViewPager();

        aq = new AQuery(this);


//        ECHelper.getInstance().createSubUser(this,"test123456");
    }

    private void setupViewPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.onBottomNavigationItemClick(position);

                switch (position) {
                    case 0:
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupBottomNav() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        if (bottomNavigationView != null){
            bottomNavigationView.isWithText(true);
            bottomNavigationView.isColoredBackground(false);
        }

        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                (getString(R.string.tab_home), getResources().getColor(R.color.firstColor), R.drawable.func_main_home_selector);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                (getString(R.string.tab_myorders), getResources().getColor(R.color.secondColor), R.drawable.func_main_myorders_selector);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                (getString(R.string.tab_learning_center), getResources().getColor(R.color.thirdColor), R.drawable.func_main_learning_selector);
        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem
                (getString(R.string.tab_avatar_center), getResources().getColor(R.color.fourthColor),R.drawable.func_main_avatar_selector);


        bottomNavigationView.addTab(bottomNavigationItem);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.addTab(bottomNavigationItem2);
        bottomNavigationView.addTab(bottomNavigationItem3);

        bottomNavigationView.setOnBottomNavigationItemClickListener(new BottomNavigationView.OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {

                mViewPager.setCurrentItem(index,false);

                switch (index) {
                    case 0:
                        HomeFragment homeFragment = (HomeFragment) fragmentHashMap.get(0);
                        homeFragment.refresh();
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    HashMap<Integer,Fragment> fragmentHashMap = new HashMap<>();

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if(position==0){
                fragment = HomeFragment.newInstance();
            }else if(position==1){
                fragment = MyOrdersFragment.newInstance();
            }else if(position==2){
                fragment = LearningCenterFragment.newInstance();
            }else{
                fragment = AvaterCenterFragment.newInstance(user);
            }

            fragmentHashMap.put(position,fragment);

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

    }
}
