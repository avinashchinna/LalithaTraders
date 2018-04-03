package com.avinash.digitalmarketing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class logintab extends Fragment {

    View mview;
    EditText txt1,txt2;
    String username,password;
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
        mview = inflater.inflate(R.layout.fragment_logintab, container, false);

        txt1 = (EditText) mview.findViewById(R.id.username);
        txt2 = (EditText) mview.findViewById(R.id.password);
        b1 = (Button) mview.findViewById(R.id.login);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(attemptlogin()){
                    new authentication().execute();
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

        username = txt1.getText().toString();
        password = txt2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            txt2.setError("This field is required");
            focusView = txt2;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            txt1.setError("This is field cannot be empty");
            focusView = txt1;
            cancel = true;
        }
        if (cancel) {
            flag=false;
            focusView.requestFocus();
        }

        return flag;
    }

    private class authentication extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url +"/customer/login/",method="POST";
            try {

                jsoninput.put("username" , username);
                jsoninput.put("password", password);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            urlconnection url_connect = new urlconnection();
            output = url_connect.urlreturn(url_input,jsoninput,method,"login");
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
                        Intent intent = new Intent(getActivity(), Home.class);
                        intent.putExtra("username", username);
                        getActivity().startActivity(intent);
                    } else {
                        String msg = jobj.getString("msg");
                        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }

    }

}
