package com.avinash.digitalmarketing;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class PastOrders extends Fragment {

    View mview;
    JSONArray jarray1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_past_orders, container, false);
        return mview;
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        new past_orders().execute();
    }

    private class past_orders extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            String output,url_input = LoginActivity.url + "/customer/completed_orders/",method="GET";

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

                        jarray1 = jobj.getJSONArray("completed_orders");

                        LinearLayout tl1 = mview.findViewById(R.id.completed_transactions);

                        String color = "#2e7d32";

                        tl1.removeAllViews();

                        if(jarray1.length() > 0){
                            View v1 = new View(getActivity());
                            v1.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    10
                            ));
                            v1.setBackgroundColor(Color.parseColor("#B3B3B3"));
                            tl1.addView(v1);
                        }else {
                            TextView tv = new TextView(getActivity());
                            tv.setText("No orders yet");
                            tv.setTextSize(18);
                            tv.setTextColor(Color.parseColor(color));
                            tv.setPadding(40,40,0,10);
                            tl1.addView(tv);

                        }

                        for(int i=0; i<jarray1.length(); i++){

                            final JSONObject json_data = jarray1.getJSONObject(i);

                            TextView item_name = new TextView(getActivity());
                            TextView prod_name = new TextView(getActivity());
                            TextView brand = new TextView(getActivity());
                            TextView order_status = new TextView(getActivity());
                            TextView order_cost = new TextView(getActivity());
                            TextView quantity = new TextView(getActivity());

                            brand.setPadding(40, 40, 0, 4);
                            prod_name.setPadding(40, 0, 0, 4);
                            item_name.setPadding(40, 0, 0, 4);
                            quantity.setPadding(40, 0, 0, 4);
                            order_cost.setPadding(40, 0, 0, 4);
                            order_status.setPadding(40, 0, 0, 40);


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

                            prod_name.setTextSize(18);
                            order_status.setTextSize(18);
                            item_name.setTextSize(18);
                            order_cost.setTextSize(18);
                            quantity.setTextSize(18);
                            brand.setTextSize(18);

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


                            tl1.addView(brand);
                            tl1.addView(prod_name);
                            tl1.addView(item_name);
                            tl1.addView(quantity);
                            tl1.addView(order_cost);
                            tl1.addView(order_status);

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

}
