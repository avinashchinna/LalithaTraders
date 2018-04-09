package com.avinash.digitalmarketing;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    TextView txt1, txt2, txt3, txt4, txt5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt1 = findViewById(R.id.username);
        txt2 = findViewById(R.id.fullname);
        txt3 = findViewById(R.id.email);
        txt4 = findViewById(R.id.mobile);
        txt5 = findViewById(R.id.address);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_profile, menu);
        return true;
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

            urlconnection myconnection = new urlconnection();
            output = myconnection.urlreturn(url_input,jsoninput,method,"a");
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    if(status){
                        txt1.setText(jobj.getString("username"));
                        txt2.setText(jobj.getString("name"));
                        txt3.setText(jobj.getString("email"));
                        txt4.setText(jobj.getString("mobile"));
                        txt5.setText(jobj.getString("address"));
                    }else{
                        Toast.makeText(getApplicationContext(),"connection failure",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.logout:
                new AlertDialog.Builder(Profile.this)
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
                return true;

            case R.id.edit_profile:
                intent = new Intent(getApplicationContext(), EditProfile.class);
                intent.putExtra("username", txt1.getText());
                intent.putExtra("fullname", txt2.getText());
                intent.putExtra("email", txt3.getText());
                intent.putExtra("mobile", txt4.getText());
                intent.putExtra("address", txt5.getText());
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
}
