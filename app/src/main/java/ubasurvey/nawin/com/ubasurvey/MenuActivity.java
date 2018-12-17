package ubasurvey.nawin.com.ubasurvey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {
    // Creating Progress dialog.
    ProgressDialog progressDialog;
    ImageView insert,update,select,delete;
    ChoiceApplication globalObject;
     CollapsingToolbarLayout collapsingToolbarLayout;
    SharedPreferences prefs;
    static final String KEY="ubaid";


    String HttpSelectUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubagetformone.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsingtoolbar);

        progressDialog = new ProgressDialog(MenuActivity.this);
        prefs = getSharedPreferences("lastrecord",MODE_PRIVATE);
        globalObject=(ChoiceApplication)getApplicationContext();
        collapsingToolbarLayout.setTitle("UBA Survey :  "+globalObject.getVolunteerID()+"  Logged in");
        Log.d("Share",prefs.getString(KEY,"no value saved"));
        insert=(ImageView)findViewById(R.id.insertimg);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalObject.setMenu(0);
                startActivity( new Intent(MenuActivity.this, BasicinfoActivity.class));

            }
        });

       /* update=(ImageView)findViewById(R.id.updateimg);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  prefs.getInt(KEY1,1);
                globalObject.setMenu(1);

                globalObject.setJsonString("");


               if((globalObject.getUbaid()==null))
                {
                    globalObject.setUbaid(prefs.getString(KEY,""));


                }
                if(globalObject.getUbaid().compareTo("")!=0) {
                   Log.d("global",globalObject.getJsonString());

                    selectDatafromDB(globalObject.getUbaid());
                    //if(globalObject.getJsonString().compareTo("")!=0)

                }

            }
        });*/
        //select=(ImageView)findViewById(R.id.selectimg);
        update=(ImageView)findViewById(R.id.updateimg);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalObject.setMenu(1);
                startActivity( new Intent(MenuActivity.this, SelectRecordActivity.class));

            }
        });

    }

    void  selectDatafromDB(final String ubaidlocal)
    {
        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
        progressDialog.show();
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpSelectUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        //code to globar var
                        //setValuetoForm(ServerResponse);
                        globalObject.setJsonString(ServerResponse);


                     /*   Toast toast = Toast.makeText(getApplicationContext(),
                                "Menu "+globalObject.getJsonString(),
                                Toast.LENGTH_LONG);

                        toast.show();*/
                        startActivity(new Intent(MenuActivity.this, FormsMenuActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        globalObject.setJsonString("");

                        // Showing error message if something goes wrong.
                        Toast.makeText(MenuActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                        //finish();;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("ubaid", ubaidlocal);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(MenuActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
}