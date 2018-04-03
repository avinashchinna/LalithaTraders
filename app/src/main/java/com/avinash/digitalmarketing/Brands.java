package com.avinash.digitalmarketing;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Brands extends Fragment implements View.OnClickListener {

    View mview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_brands, container, false);

        new showbrands().execute();

        return mview;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), Products.class);
        intent.putExtra("brand", view.getTag().toString());
        getActivity().startActivity(intent);
    }

    private class showbrands extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url +"/customer/fetchbrands/",method="GET";

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

                        JSONArray jarray = jobj.getJSONArray("brands");
                        TableLayout tl = (TableLayout) mview.findViewById(R.id.brands);
                        tl.removeAllViews();

                        View v1 = new View(getContext());
                        v1.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                5
                        ));
                        v1.setBackgroundColor(Color.parseColor("#B3B3B3"));
                        tl.addView(v1);

                        for(int i=0; i<jarray.length(); i++) {
                            final JSONObject json_data = jarray.getJSONObject(i);
                            TableRow row = new TableRow(getActivity());
                            TableRow.LayoutParams lp = new TableRow.
                                    LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT);
                            row.setLayoutParams(lp);
                            row.setBackgroundColor(Color.TRANSPARENT);
                            row.setGravity(Gravity.CENTER_HORIZONTAL);

                            Button b1 = new Button(getActivity());
                            b1.setText(json_data.getString("name"));
                            b1.setBackgroundColor(Color.parseColor("#1A237E"));
                            b1.setTextColor(Color.parseColor("#FFFFFF"));
                            b1.setTag(json_data.getString("name"));
                            b1.setOnClickListener(Brands.this);
                            row.addView(b1);
                            tl.addView(row);
                            View v = new View(getActivity());
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
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //null error
                }
            }

        }
    }


}
