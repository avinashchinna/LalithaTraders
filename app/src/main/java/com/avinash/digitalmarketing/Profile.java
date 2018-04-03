package com.avinash.digitalmarketing;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume(){
        super.onResume();
        new get_profile().execute();
    }

    private class get_profile extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url +"/customer/getprofile/",method="GET";
//            try {
//
//                jsoninput.put("brand" , brand_name);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            urlconnection myconnection = new urlconnection();
            output = myconnection.urlreturn(url_input,jsoninput,method,"a");
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    JSONObject jobj = new JSONObject(strings);
                    System.out.println(jobj);
                } catch (JSONException e) {

                }
            }
        }
    }
}
