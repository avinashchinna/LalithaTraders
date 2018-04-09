package com.avinash.digitalmarketing;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Products extends AppCompatActivity implements View.OnClickListener{

    String brand_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            brand_name = extras.getString("brand");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(brand_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new showproducts().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_options, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ProductDescription.class);
        intent.putExtra("product", view.getTag().toString());
        startActivity(intent);
    }

    private class showproducts extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url +"/customer/fetchproducts/",method="POST";
            try {

                jsoninput.put("brand" , brand_name);

            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                    if (status) {

                        JSONArray jarray = jobj.getJSONArray("products");
                        TableLayout tl = (TableLayout) findViewById(R.id.products);
                        tl.removeAllViews();

                        View v1 = new View(getApplicationContext());
                        v1.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                5
                        ));
                        v1.setBackgroundColor(Color.parseColor("#B3B3B3"));
                        tl.addView(v1);

                        for(int i=0; i<jarray.length(); i++) {
                            final JSONObject json_data = jarray.getJSONObject(i);
                            TableRow row = new TableRow(getApplicationContext());
                            TableRow.LayoutParams lp = new TableRow.
                                    LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT);
                            row.setLayoutParams(lp);
                            row.setBackgroundColor(Color.TRANSPARENT);
                            row.setGravity(Gravity.CENTER_HORIZONTAL);

                            Button b1 = new Button(getApplicationContext());
                            b1.setText(json_data.getString("name"));
                            b1.setBackgroundColor(Color.parseColor("#1A237E"));
                            b1.setTextColor(Color.parseColor("#FFFFFF"));
                            b1.setTag(json_data.getString("name"));
                            b1.setOnClickListener(Products.this);
                            row.addView(b1);
                            tl.addView(row);
                            View v = new View(getApplicationContext());
                            v.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    5
                            ));
                            v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                            tl.addView(v);
                        }

                    } else {
                        String msg = jobj.getString("msg");
                        System.out.println(msg);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //null error
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.logout:
                new AlertDialog.Builder(Products.this)
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

            case android.R.id.home:
                super.onBackPressed();
                return true;

            case R.id.edit_profile:
                intent = new Intent(getApplicationContext(), Profile.class);
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
