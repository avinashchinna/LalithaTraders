package com.avinash.digitalmarketing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfile extends AppCompatActivity {

    TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8;
    String username, fullname, email, mobile, address, prev_password, new_pass, new_pass1;
    Button b1;
    boolean password_change = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("username");
            fullname = extras.getString("fullname");
            email = extras.getString("email");
            mobile = extras.getString("mobile");
            address = extras.getString("address");
        }

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt1 = findViewById(R.id.username);
        txt2 = findViewById(R.id.fullname);
        txt3 = findViewById(R.id.email);
        txt4 = findViewById(R.id.mobile);
        txt5 = findViewById(R.id.address);
        txt6 = findViewById(R.id.prevpass);
        txt7 = findViewById(R.id.newpass);
        txt8 = findViewById(R.id.newpass1);

        String boldText = "Username: ";
        String normalText = username;
        SpannableString str = new SpannableString(boldText + normalText);
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt1.setTextSize(18);
        txt1.setText(str);

        txt2.setText(fullname);
        txt3.setText(email);
        txt4.setText(mobile);
        txt5.setText(address);

        b1 = findViewById(R.id.save_profile);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(final View v){
                if(saveprofile()){
                    new edit_profile(v).execute();
                }
            }
        });
    }

    public boolean saveprofile(){
        boolean flag=true;

        // Reset errors.
        txt2.setError(null);
        txt3.setError(null);
        txt4.setError(null);
        txt5.setError(null);
        txt6.setError(null);
        txt7.setError(null);
        txt8.setError(null);

        fullname = txt2.getText().toString();
        email = txt3.getText().toString();
        mobile = txt4.getText().toString();
        address = txt5.getText().toString();
        prev_password = txt6.getText().toString();
        new_pass = txt7.getText().toString();
        new_pass1 = txt8.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(fullname)) {
            txt2.setError("This field is required");
            focusView = txt2;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            txt3.setError("This field is required");
            focusView = txt3;
            cancel = true;
        }

        if (TextUtils.isEmpty(mobile)) {
            txt4.setError("This field is required");
            focusView = txt4;
            cancel = true;
        }

        if (TextUtils.isEmpty(address)) {
            txt5.setError("This field is required");
            focusView = txt5;
            cancel = true;
        }

        if (!TextUtils.isEmpty(prev_password)) {
            if (TextUtils.isEmpty(new_pass)) {
                txt7.setError("This field is required");
                focusView = txt7;
                cancel = true;
            }
            if (TextUtils.isEmpty(new_pass1)) {
                txt8.setError("This field is required");
                focusView = txt8;
                cancel = true;
            }
        }else{
            password_change = false;
        }

        if(!new_pass1.equals(new_pass)){
            txt8.setError("Passwords do not match");
            focusView = txt8;
            cancel = true;
        }

        if (!TextUtils.isEmpty(new_pass)) {
            if (TextUtils.isEmpty(prev_password)) {
                txt6.setError("This field is required");
                focusView = txt6;
                cancel = true;
            }
        }

        if(mobile.length() != 10){
            txt6.setError("mobile number must be 10 digits");
            focusView = txt4;
            cancel = true;
        }
        if(!email.contains("@")){
            txt3.setError("invalid email id");
            focusView = txt3;
            cancel = true;
        }

        if(!TextUtils.isEmpty(new_pass) && !TextUtils.isEmpty(new_pass1)
                && !TextUtils.isEmpty(prev_password)){
            password_change = true;
        }

        if(TextUtils.isEmpty(new_pass) && TextUtils.isEmpty(new_pass1)
                && TextUtils.isEmpty(prev_password)){
            password_change = false;
        }

        if (cancel) {

            flag=false;
            focusView.requestFocus();
        }

        return flag;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case android.R.id.home:
                super.onBackPressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private class edit_profile extends AsyncTask<String, String, String> {

        View v;

        private edit_profile(View _v){
            v = _v;
        }

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(EditProfile.this);
            pDialog.setMessage("Saving Details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url+"/customer/edit_profile/",method="POST";
            try {

                jsoninput.put("username" , username);
                jsoninput.put("prev_password", prev_password);
                jsoninput.put("password", new_pass);
                jsoninput.put("email", email);
                jsoninput.put("mobile", mobile);
                jsoninput.put("cust_name", fullname);
                jsoninput.put("address",address);
                jsoninput.put("password_change", password_change);

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
                        String msg = jobj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        if(!password_change){
                            Intent intent = new Intent(getApplicationContext(), Profile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

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
