package com.avinash.digitalmarketing;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CurrentOrders extends Fragment {

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
        mview = inflater.inflate(R.layout.fragment_current_orders, container, false);

        return mview;
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        new ShowTransactions().execute();
    }

    private class ShowTransactions extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            String output,url_input = LoginActivity.url + "/customer/ongoing_orders/",method="GET";

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
                        String color = "#f50057";

                        LinearLayout tl = mview.findViewById(R.id.transactions);

                        tl.removeAllViews();

                        if(jarray1.length() > 0){
                            View v1 = new View(getActivity());
                            v1.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    10
                            ));
                            v1.setBackgroundColor(Color.parseColor("#B3B3B3"));
                            tl.addView(v1);
                        }else {
                            TextView tv = new TextView(getActivity());
                            tv.setText("No orders yet");
                            tv.setTextSize(18);
                            tv.setTextColor(Color.parseColor(color));
                            tv.setPadding(40,40,0,10);
                            tl.addView(tv);

                        }


                        for(int i=0; i<jarray1.length(); i++){

                            boolean  cb;
                            final JSONObject json_data = jarray1.getJSONObject(i);
                            cb = json_data.getBoolean("showcb");

                            TextView item_name = new TextView(getActivity());
                            TextView prod_name = new TextView(getActivity());
                            TextView brand = new TextView(getActivity());
                            TextView order_status = new TextView(getActivity());
                            TextView order_cost = new TextView(getActivity());
                            TextView quantity = new TextView(getActivity());
                            Button cancel_order = new Button(getActivity());

                            brand.setPadding(40, 40, 0, 4);
                            prod_name.setPadding(40, 0, 0, 4);
                            item_name.setPadding(40, 0, 0, 4);
                            quantity.setPadding(40, 0, 0, 4);
                            order_cost.setPadding(40, 0, 0, 4);
                            order_status.setPadding(40, 0, 0, 4);

                            String boldText = "ITEM NAME : ";
                            String normalText = json_data.getString("item_name");
                            SpannableString str = new SpannableString(boldText + normalText);
                            str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length()
                                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            item_name.setText(str);

                            boldText = "PRODUCT NAME : ";
                            normalText = json_data.getString("product");
                            str = new SpannableString(boldText + normalText);
                            str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length()
                                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            prod_name.setText(str);

                            boldText = "BRAND NAME : ";
                            normalText = json_data.getString("brand");
                            str = new SpannableString(boldText + normalText);
                            str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length()
                                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            brand.setText(str);

                            boldText = "STATUS OF ORDER : ";
                            normalText = json_data.getString("order_status");
                            str = new SpannableString(boldText + normalText);
                            str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length()
                                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            order_status.setText(str);

                            boldText = "QUANTITY : ";
                            normalText = json_data.getString("quantity");
                            str = new SpannableString(boldText + normalText);
                            str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length()
                                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            quantity.setText(str);

                            boldText = "COST : ";
                            normalText = json_data.getString("order_cost");
                            str = new SpannableString(boldText + normalText);
                            str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length()
                                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            order_cost.setText(str);

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

                            if(!(cb)){
                                order_status.setPadding(40, 0, 0, 40);
                            }

                            tl.addView(brand);
                            tl.addView(prod_name);
                            tl.addView(item_name);
                            tl.addView(quantity);
                            tl.addView(order_cost);
                            tl.addView(order_status);
                            if(cb){
                                tl.addView(cancel_order);
                            }
                            tl.addView(v);

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
