package com.avinash.digitalmarketing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class signuptab extends Fragment {

    View mview;
    EditText txt1,txt2,txt3,txt4,txt5,txt6,txt7;
    String username,password,email,re_password,address,mobile,fullname;
    Button b1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_signuptab, container, false);

        txt1 = (EditText) mview.findViewById(R.id.uemail);
        txt2 = (EditText) mview.findViewById(R.id.uname);
        txt3 = (EditText) mview.findViewById(R.id.upass);
        txt4 = (EditText) mview.findViewById(R.id.upass1);
        txt5 = (EditText) mview.findViewById(R.id.ufullname);
        txt6 = (EditText) mview.findViewById(R.id.phn_no);
        txt7 = (EditText) mview.findViewById(R.id.address);

        b1 = (Button) mview.findViewById(R.id.register);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(final View v){
                if(attemptlogin()){
                    new authentication(v).execute();
                }
            }
        });

        return mview;
    }

    public boolean attemptlogin(){
        boolean flag=true;

        // Reset errors.
        txt1.setError(null);
        txt2.setError(null);
        txt3.setError(null);
        txt4.setError(null);
        txt5.setError(null);
        txt6.setError(null);
        txt7.setError(null);

        email = txt1.getText().toString();
        username = txt2.getText().toString();
        password = txt3.getText().toString();
        re_password = txt4.getText().toString();
        fullname = txt5.getText().toString();
        mobile = txt6.getText().toString();
        address = txt7.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            txt2.setError("This field is required");
            focusView = txt2;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            txt1.setError("This field is required");
            focusView = txt1;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            txt3.setError("This field is required");
            focusView = txt3;
            cancel = true;
        }
        if (TextUtils.isEmpty(re_password)) {
            txt4.setError("This field is required");
            focusView = txt4;
            cancel = true;
        }
        if (TextUtils.isEmpty(fullname)) {
            txt5.setError("This field is required");
            focusView = txt5;
            cancel = true;
        }
        if (TextUtils.isEmpty(mobile)) {
            txt6.setError("This field is required");
            focusView = txt6;
            cancel = true;
        }

        if (TextUtils.isEmpty(address)) {
            txt7.setError("This field is required");
            focusView = txt7;
            cancel = true;
        }

        if(!password.equals(re_password)){
            txt4.setError("Passwords do not match");
            focusView = txt4;
            cancel = true;
        }

        if(mobile.length() != 10){
            txt6.setError("mobile number must be 10 digits");
            focusView = txt6;
            cancel = true;
        }
        if(!email.contains("@")){
            txt1.setError("invalid email id");
            focusView = txt1;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            flag=false;
            focusView.requestFocus();
        }

        return flag;
    }

    private class authentication extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        View v;

        private authentication(View _v){
            v = _v;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Registering...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url+"/customer/register/",method="POST";
            try {

                jsoninput.put("username" , username);
                jsoninput.put("password", password);
                jsoninput.put("email", email);
                jsoninput.put("mobile", mobile);
                jsoninput.put("cust_name", fullname);
                jsoninput.put("address",address);

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
                    pDialog.dismiss();
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        String msg = jobj.getString("msg");
                        Snackbar.make(v,msg,Snackbar.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().startActivity(intent);
                    } else {
                        String msg = jobj.getString("msg");
                        Snackbar.make(v,msg,Snackbar.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }

    }

}
