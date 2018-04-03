package com.avinash.digitalmarketing;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AllordersTab extends Fragment {

    View mview;
    JSONArray jarray1,jarray2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_allorders_tab, container, false);
        new ShowTransactions().execute();
        return mview;
    }

    private class ShowTransactions extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            String output,url_input = LoginActivity.url + "/customer/allorders/",method="GET";

            urlconnection myconnection = new urlconnection();
            output = myconnection.urlreturn(url_input,null,method,"a");
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    JSONObject jobj = new JSONObject(strings);
                    System.out.println(jobj);
                    boolean status = jobj.getBoolean("status");
                    if (status) {

                        jarray1 = jobj.getJSONArray("ongoing_orders");
                        jarray2 = jobj.getJSONArray("completed_orders");

                        TableLayout tl = (TableLayout) mview.findViewById(R.id.transactions);
                        TableLayout tl1 = (TableLayout) mview.findViewById(R.id.completed_transactions);

                        tl1.removeAllViews();
                        tl.removeAllViews();

                        if(jarray1.length() > 0){
                            View v1 = new View(getActivity());
                            v1.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    10
                            ));
                            v1.setBackgroundColor(Color.parseColor("#B3B3B3"));
                            tl.addView(v1);
                        }

                        if(jarray2.length() > 0){
                            View v2 = new View(getActivity());
                            v2.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    10
                            ));
                            v2.setBackgroundColor(Color.parseColor("#B3B3B3"));
                            tl1.addView(v2);
                        }

                        for(int i=0; i<jarray1.length(); i++){

                            boolean  cb;
                            final JSONObject json_data = jarray1.getJSONObject(i);
                            cb = json_data.getBoolean("showcb");
                            TableRow row1 = new TableRow(getActivity());
                            TableRow row2 = new TableRow(getActivity());
                            TableRow row3 = new TableRow(getActivity());
                            TableRow row4 = new TableRow(getActivity());
                            TableRow row5 = new TableRow(getActivity());
                            TableRow row6 = new TableRow(getActivity());
                            TableRow row7 = new TableRow(getActivity());
                            TableRow.LayoutParams lp = new TableRow.LayoutParams
                                    (TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
                            row1.setLayoutParams(lp);
                            row2.setLayoutParams(lp);
                            row3.setLayoutParams(lp);
                            row4.setLayoutParams(lp);
                            row5.setLayoutParams(lp);
                            row6.setLayoutParams(lp);
                            row7.setLayoutParams(lp);
                            row7.setGravity(Gravity.CENTER_HORIZONTAL);

                            row1.setPadding(40, 40, 0, 4);
                            row2.setPadding(40, 0, 0, 4);
                            row3.setPadding(40, 0, 0, 4);
                            row4.setPadding(40, 0, 0, 4);
                            row5.setPadding(40, 0, 0, 4);
                            row6.setPadding(40, 0, 0, 4);
                            row7.setPadding(40, 0, 40, 40);

                            TextView item_name = new TextView(getActivity());
                            TextView prod_name = new TextView(getActivity());
                            TextView brand = new TextView(getActivity());
                            TextView order_status = new TextView(getActivity());
                            TextView order_cost = new TextView(getActivity());
                            TextView quantity = new TextView(getActivity());
                            Button cancel_order = new Button(getActivity());

                            item_name.setText("ITEM NAME : "+json_data.getString("item_name"));
                            prod_name.setText("PRODUCT NAME : "+json_data.getString("product"));
                            brand.setText("BRAND NAME : "+json_data.getString("brand"));
                            order_status.setText("STATUS OF ORDER : "+json_data.getString("order_status"));
                            quantity.setText("QUANTITY : "+Integer.toString(json_data.getInt("quantity")));
                            order_cost.setText("COST : "+json_data.getString("order_cost"));

                            cancel_order.setText("Cancel Order");

                            prod_name.setTextSize(18);
                            order_status.setTextSize(18);
                            item_name.setTextSize(18);
                            order_cost.setTextSize(18);
                            quantity.setTextSize(18);
                            brand.setTextSize(18);

                            final int order_id = json_data.getInt("order_id");

                            cancel_order.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Alert")
                                            .setMessage("Are you sure , you want to cancel this order ?")
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    new cancelorder(order_id).execute();
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            });
                            String color = "#f50057";
                            brand.setTextColor(Color.parseColor(color));
                            prod_name.setTextColor(Color.parseColor(color));
                            order_cost.setTextColor(Color.parseColor(color));
                            item_name.setTextColor(Color.parseColor(color));
                            order_status.setTextColor(Color.parseColor(color));
                            quantity.setTextColor(Color.parseColor(color));

                            View v = new View(getActivity());
                            v.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    10
                            ));
                            v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                            row1.addView(item_name);
                            row2.addView(prod_name);
                            row3.addView(brand);
                            row4.addView(quantity);
                            row5.addView(order_cost);
                            row6.addView(order_status);

                            if(cb){
                                row7.addView(cancel_order);
                            }
                            if(!(cb)){
                                row6.setPadding(40, 0, 0, 40);
                            }

                            tl.addView(row1);
                            tl.addView(row2);
                            tl.addView(row3);
                            tl.addView(row4);
                            tl.addView(row5);
                            tl.addView(row6);
                            if(cb){
                                tl.addView(row7);
                            }
                            tl.addView(v);

                        }

                        for(int i=0; i<jarray2.length(); i++){

                            final JSONObject json_data1 = jarray2.getJSONObject(i);
                            TableRow row1 = new TableRow(getActivity());
                            TableRow row2 = new TableRow(getActivity());
                            TableRow row3 = new TableRow(getActivity());
                            TableRow row4 = new TableRow(getActivity());
                            TableRow row5 = new TableRow(getActivity());
                            TableRow row6 = new TableRow(getActivity());
                            TableRow.LayoutParams lp = new TableRow.LayoutParams
                                    (TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
                            row1.setLayoutParams(lp);
                            row2.setLayoutParams(lp);
                            row3.setLayoutParams(lp);
                            row4.setLayoutParams(lp);
                            row5.setLayoutParams(lp);
                            row6.setLayoutParams(lp);

                            row1.setPadding(40, 40, 0, 4);
                            row2.setPadding(40, 0, 0, 4);
                            row3.setPadding(40, 0, 0, 4);
                            row4.setPadding(40, 0, 0, 4);
                            row5.setPadding(40, 0, 0, 4);
                            row6.setPadding(40, 0, 0, 40);

                            TextView item_name = new TextView(getActivity());
                            TextView prod_name = new TextView(getActivity());
                            TextView brand = new TextView(getActivity());
                            TextView order_status = new TextView(getActivity());
                            TextView order_cost = new TextView(getActivity());
                            TextView quantity = new TextView(getActivity());


                            item_name.setText("ITEM NAME : "+json_data1.getString("item_name"));
                            prod_name.setText("PRODUCT NAME : "+json_data1.getString("product"));
                            brand.setText("BRAND NAME : "+json_data1.getString("brand"));
                            order_status.setText("STATUS OF ORDER : "+json_data1.getString("order_status"));
                            quantity.setText("QUANTITY : "+Integer.toString(json_data1.getInt("quantity")));
                            order_cost.setText("COST : "+json_data1.getString("order_cost"));

                            prod_name.setTextSize(18);
                            order_status.setTextSize(18);
                            item_name.setTextSize(18);
                            order_cost.setTextSize(18);
                            quantity.setTextSize(18);
                            brand.setTextSize(18);

                            String color = "#2e7d32";
                            brand.setTextColor(Color.parseColor(color));
                            prod_name.setTextColor(Color.parseColor(color));
                            order_cost.setTextColor(Color.parseColor(color));
                            item_name.setTextColor(Color.parseColor(color));
                            order_status.setTextColor(Color.parseColor(color));
                            quantity.setTextColor(Color.parseColor(color));

                            View v = new View(getActivity());
                            v.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    10
                            ));
                            v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                            row1.addView(item_name);
                            row2.addView(prod_name);
                            row3.addView(brand);
                            row4.addView(quantity);
                            row5.addView(order_cost);
                            row6.addView(order_status);

                            tl1.addView(row1);
                            tl1.addView(row2);
                            tl1.addView(row3);
                            tl1.addView(row4);
                            tl1.addView(row5);
                            tl1.addView(row6);

                            tl1.addView(v);

                        }

                    } else {
                        String msg = jobj.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }
    }

    private class cancelorder extends AsyncTask<String, String, String> {

        int order_id;
        public cancelorder(int a){
            order_id = a;
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url + "/customer/cancelorder/",method="POST";

            try {
                jsoninput.put("order_id" , order_id);
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
                    System.out.println("status reviews "+status);
                    if (status) {
                        Toast.makeText(getActivity(),"Your order has been cancelled successfully", Toast.LENGTH_LONG).show();
                        new ShowTransactions().execute();

                    } else {
                        String msg = jobj.getString("msg");
                        System.out.println(msg);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }
    }

}
