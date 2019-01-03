package ubasurvey.nawin.com.ubasurvey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.HashMap;
import java.util.Map;

public class ChangePswActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    static final String KEY1 = "password";
    // Creating EditText.
    EditText OldPassword, NewPassword ;

TextView  usernameTextView;
    // Creating button;
    Button InsertButton;

    // Creating Volley RequestQueue.
    RequestQueue requestQueue;

    // Create string variable to hold the EditText Value.
    String OldPasswordHolder, NewPasswordHolder, EmailHolder ;

    // Creating Progress dialog.
    ProgressDialog progressDialog;
    String userName;

    // Storing server url into String variable.
    String HttpUrl;// = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/changepsw.php";
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_psw);
        prefs = getSharedPreferences("username", MODE_PRIVATE);
        editor = prefs.edit();
        HttpUrl=getString(R.string.url)+"changepsw.php";
        relativeLayout=(RelativeLayout)findViewById(R.id.pswrelativelayout);
        // Assigning ID's to EditText.
        OldPassword = (EditText) findViewById(R.id.oldpsw);
        NewPassword = (EditText) findViewById(R.id.newpsw);
        usernameTextView = (TextView) findViewById(R.id.username);
        Bundle bundle = getIntent().getExtras();
        userName=bundle.getString("value1");
        usernameTextView.setText("User Nmae : "+userName);

        // Assigning ID's to Button.
        InsertButton = (Button) findViewById(R.id.btn_change);


        // Creating Volley newRequestQueue .
        requestQueue = Volley.newRequestQueue(ChangePswActivity.this);

        progressDialog = new ProgressDialog(ChangePswActivity.this);

        // Adding click listener to button.
        InsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(OldPassword.getText()) )
                {

                    YoYo.with(Techniques.Bounce)
                            .duration(700)
                            .playOn(findViewById(R.id.oldpsw));   
                }
                if(TextUtils.isEmpty(NewPassword.getText()) )
                {

                    YoYo.with(Techniques.Bounce)
                            .duration(700)
                            .playOn(findViewById(R.id.newpsw));
                }

                // Showing progress dialog at user registration time.
                progressDialog.setMessage("Please Wait, We are validating your password on Server");
                progressDialog.show();

                // Calling method to get value from EditText.
                GetValueFromEditText();

                // Creating string request with post method.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String ServerResponse) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();
                              if(ServerResponse.compareTo("1")==0)
                              {
                                  editor.putString(KEY1, NewPasswordHolder);
                                  //---saves the values---
                                  editor.commit();
                              }
                                // Showing response message coming from server.
                               // Toast.makeText(ChangePswActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                                Intent data = new Intent();
                                data.setData(Uri.parse(ServerResponse));
                                setResult(RESULT_OK, data);
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                // Showing error message if something goes wrong.
                                Snackbar snackbar = Snackbar
                                        .make(relativeLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                                        .setAction("RETRY", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                            }
                                        });

                                // Changing message text color
                                snackbar.setActionTextColor(Color.RED);

                                // Changing action button text color
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);

                                snackbar.show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {

                        // Creating Map String Params.
                        Map<String, String> params = new HashMap<String, String>();

                        // Adding All values to Params.
                        params.put("old_psw", OldPasswordHolder);
                        params.put("new_psw", NewPasswordHolder);
                        params.put("username", userName);

                        return params;
                    }

                };

                // Creating RequestQueue.
                RequestQueue requestQueue = Volley.newRequestQueue(ChangePswActivity.this);

                // Adding the StringRequest object into requestQueue.
                requestQueue.add(stringRequest);

            }
        });

    }

    // Creating method to get value from EditText.
    public void GetValueFromEditText(){

        OldPasswordHolder = OldPassword.getText().toString().trim();
        NewPasswordHolder = NewPassword.getText().toString().trim();
        //EmailHolder = usernameTextView.getText().toString().trim();

    }

}