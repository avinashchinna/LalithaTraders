package com.avinash.digitalmarketing;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.net.CookieManager;

public class LoginActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    static final String url = "https://lalithatraders.herokuapp.com";
//    static final String url = "http://10.42.0.1:8000";
    //This is our tablayout
    private TabLayout tabLayout1;

    //This is our viewPager
    private ViewPager viewPager1;
    static final String COOKIES_HEADER = "Set-Cookie";
    static CookieManager msCookieManager = new CookieManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        //Initializing the tablayout
        tabLayout1 = (TabLayout) findViewById(R.id.tabLayout1);

        //Adding the tabs using addTab() method
        tabLayout1.addTab(tabLayout1.newTab().setText("LOGIN"));
        tabLayout1.addTab(tabLayout1.newTab().setText("SIGN UP"));
        tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager1 = (ViewPager) findViewById(R.id.pager1);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout1.getTabCount());

        //Adding adapter to pager
        viewPager1.setAdapter(adapter);
        viewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout1));

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager1.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPause(){
        super.onPause();
        tabLayout1.removeOnTabSelectedListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        //Adding onTabSelectedListener to swipe views
        tabLayout1.addOnTabSelectedListener(this);
    }
}
