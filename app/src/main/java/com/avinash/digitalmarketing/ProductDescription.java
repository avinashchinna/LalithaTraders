package com.avinash.digitalmarketing;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDescription extends AppCompatActivity {

    String product_name;
    int bill_cost=0;
    JSONArray orders = new JSONArray(),order_ids = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            product_name = extras.getString("product");
            System.out.println("product_name" + product_name);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(product_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new showDescription().execute();
        new showitems().execute();

        Button submit_order = (Button) findViewById(R.id.submit_order);
        submit_order.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                bill_cost=0;
                TableLayout tl = (TableLayout) findViewById(R.id.itemlist);
                boolean atleast_one_item_order=false;
                orders = new JSONArray();

                for (int i=0;i<tl.getChildCount();i++){
                    TableRow row = (TableRow)tl.getChildAt(i);
                    TextView item_id = (TextView)row.getChildAt(0);
                    TextView item_name = (TextView)row.getChildAt(1);
                    TextView item_cost = (TextView)row.getChildAt(2);
                    EditText item_quantity = (EditText) row.getChildAt(3);

                    try{
                        if(!item_quantity.getText().toString().equals("")){
                            if(Integer.parseInt(item_quantity.getText().toString())>0){

                                bill_cost+=Integer.parseInt(item_quantity.getText().toString())
                                        *Integer.parseInt(item_cost.getText().toString());

                                atleast_one_item_order = true;
                                JSONObject temp = new JSONObject();
                                temp.put("id",item_id.getText().toString());
                                temp.put("quantity",Integer.parseInt(item_quantity.getText().toString()));
                                orders.put(temp);
                            }
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                if(atleast_one_item_order){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDescription.this);
                    builder.setMessage("Your bill is Rs "+bill_cost+"\n"+
                    "Shall we confirm the order?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            new placeorder().execute();
                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).create();
                    builder.show();
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Atleast one item should have a quantity of greater or equal to 1", Toast.LENGTH_SHORT).show();
                };

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_options, menu);
        return true;
    }

    private class showDescription extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url +"/customer/product_description/",method="POST";
            try {

                jsoninput.put("product" , product_name);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            urlconnection url_connect = new urlconnection();
            output = url_connect.urlreturn(url_input,jsoninput,method,"a");
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        TextView prod_name = findViewById(R.id.product_name);
                        prod_name.setText(jobj.getString("product_name"));
                        TextView brand_name = findViewById(R.id.brand_name);
                        brand_name.setText(jobj.getString("brand_name"));
                        TextView offers = findViewById(R.id.offers);
                        offers.setText(jobj.getString("offers"));
                        TextView description = findViewById(R.id.description);
                        description.setText(jobj.getString("description"));
                        TextView additional_info = findViewById(R.id.additional_info);
                        additional_info.setText(jobj.getString("additional_info"));

                        TextView types = findViewById(R.id.types_of_brand);
                        String types_of_brand = "";
                        JSONArray jarray = jobj.getJSONArray("items");
                        for(int i=0; i<jarray.length(); i++){
                            final JSONObject json_data = jarray.getJSONObject(i);
                            types_of_brand += String.valueOf(i+1) + ") " +
                                    json_data.getString("name") + "\n";
                        }
                        types.setText(types_of_brand);

                    } else {
                        String msg = jobj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }

    }

    private class showitems extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url +"/customer/fetchitems/",method="POST";

            try {
                jsoninput.put("product" , product_name);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            urlconnection myconnection = new urlconnection();
            output = myconnection.urlreturn(url_input,jsoninput,method,"b");
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        JSONArray jarray = jobj.getJSONArray("items");
                        TableLayout tl = (TableLayout) findViewById(R.id.itemlist);
                        tl.removeAllViews();

                        for(int i=0; i<jarray.length(); i++){
                            final JSONObject json_data = jarray.getJSONObject(i);
                            TableRow row = new TableRow(getApplicationContext());
                            TableRow.LayoutParams lp1 = new TableRow.LayoutParams
                                    (TableRow.LayoutParams.MATCH_PARENT,
                                            TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                            row.setLayoutParams(lp1);
                            row.setPadding(0, 0, 0, 2);

                            TextView item_name = new TextView(getApplicationContext());
                            TextView item_id = new TextView(getApplicationContext());
                            TextView item_cost = new TextView(getApplicationContext());
                            EditText quantity = new EditText(getApplicationContext());
                            quantity.setGravity(Gravity.CENTER);

                            item_id.setText(Integer.toString(json_data.getInt("id")));
                            item_name.setText(json_data.getString("name"));
                            item_cost.setText(Integer.toString(json_data.getInt("cost")));
                            quantity.setHint("No. of Pieces");
                            quantity.setInputType(InputType.TYPE_CLASS_NUMBER);

                            String color = "#f50057";

                            item_id.setVisibility(View.GONE);
                            item_name.setTextSize(18);
                            item_cost.setTextSize(18);
                            item_name.setTextColor(Color.parseColor(color));
                            item_cost.setTextColor(Color.parseColor(color));

                            row.addView(item_id);
                            row.addView(item_name);
                            row.addView(item_cost);
                            row.addView(quantity);

                            tl.addView(row);

                        }

                    } else {
                        String msg = jobj.getString("msg");
                        System.out.println(msg);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }
    }

    private class placeorder extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url + "/customer/placeorder/",method="POST";

            try {
                jsoninput.put("items",orders);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            urlconnection myconnection = new urlconnection();
            output = myconnection.urlreturn(url_input,jsoninput,method,"b");
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        Toast.makeText(getApplicationContext(),"Your order has been placed successfully", Toast.LENGTH_LONG).show();
                        JSONArray jarray = jobj.getJSONArray("order_ids");

                        for(int i=0; i<jarray.length(); i++){
                            order_ids.put(jarray.getInt(i));
                        }

                        Intent intent = new Intent(getApplicationContext(),Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {
                        String msg = jobj.getString("msg");
                        System.out.println(msg);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.logout:
                new AlertDialog.Builder(ProductDescription.this)
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
