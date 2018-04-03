package com.avinash.digitalmarketing;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONObject;

public class Home extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    PagerOrder adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Brands"));
        tabLayout.addTab(tabLayout.newTab().setText("Current ORDERS"));
        tabLayout.addTab(tabLayout.newTab().setText("Past ORDERS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.pager);

        adapter = new PagerOrder(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.logout:
                new AlertDialog.Builder(Home.this)
                        .setTitle("Alert")
                        .setMessage("Are you sure , you want to logout ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new logout().execute();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;

            case R.id.contact_details:
                Intent intent = new Intent(getApplicationContext(), Contact.class);
                startActivity(intent);

            case R.id.edit_profile:
                intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();
        tabLayout.removeOnTabSelectedListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private class logout extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            String output,url_input = LoginActivity.url + "/customer/logout/",method="GET";

            urlconnection myconnection = new urlconnection();
            output = myconnection.urlreturn(url_input,null,method,"logout");
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(getApplication(),"Logged out Successfully",Toast.LENGTH_SHORT).show();
                    } else {

                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
