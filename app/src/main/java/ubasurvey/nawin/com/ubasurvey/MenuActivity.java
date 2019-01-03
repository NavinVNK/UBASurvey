package ubasurvey.nawin.com.ubasurvey;


import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Intent;

import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import android.support.v4.app.DialogFragment;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
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

public class MenuActivity extends AppCompatActivity {
    // Creating Progress dialog.
    ProgressDialog progressDialog;
    ImageView insert,update,select,delete;
    ChoiceApplication globalObject;
     CollapsingToolbarLayout collapsingToolbarLayout;
    DatabaseHelper ubadb;
    private CoordinatorLayout coordinatorLayout;



    String HttpSelectUrl;// = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubagetformone.php";
    static String HttpupdateUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ubadb= DatabaseHelper.getInstance(this);
        //backup();
       // Log.d("Db",ubadb.getAllData());
        HttpSelectUrl=getString(R.string.url)+"ubagetformone.php";
        HttpupdateUrl = getString(R.string.url)+"ubaupdatemessage.php";
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsingtoolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarid);

        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .menucoordinatelayout);

        progressDialog = new ProgressDialog(MenuActivity.this);

        globalObject=(ChoiceApplication)getApplicationContext();
        collapsingToolbarLayout.setTitle("UBA Survey :  "+globalObject.getVolunteerID()+"  Logged in");
        //Log.d("Share",prefs.getString(KEY,"no value saved"));
        insert=(ImageView)findViewById(R.id.insertimg);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalObject.setMenu(0);

                startActivity( new Intent(MenuActivity.this, BasicinfoActivity.class),ActivityOptions.makeSceneTransitionAnimation(MenuActivity.this).toBundle());

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
                startActivity( new Intent(MenuActivity.this, SelectRecordActivity.class),ActivityOptions.makeSceneTransitionAnimation(MenuActivity.this).toBundle());

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
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
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
                        finish();;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(globalObject.getVolunteerID().compareTo("admin")==0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_admin, menu);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_message:

                DialogFragment dialogFragment = new MessageFragment();

                dialogFragment.show(getSupportFragmentManager(),"dialog");
                break;
            case R.id.action_upload:

                uploadOfflineRecords();
                break;

            default:
                break;
        }

        return true;
    }
    public void uploadOfflineRecords()
    {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Upload offline",
                Toast.LENGTH_LONG);

        toast.show();

    }

    public static class MessageFragment extends DialogFragment {
        ProgressDialog progressDialog;
        //String //http://navinsjavatutorial.000webhostapp.com/ucbsurvey/


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            final EditText msgtitle;
            final EditText msgsubject;
            final Button message,clear;
            final Dialog dialog;

            progressDialog = new ProgressDialog(getActivity());
          final  View view = getActivity().getLayoutInflater().inflate(
                    R.layout.fragment_message, null);


            msgsubject=view.findViewById(R.id.msgsubject);
            message=view.findViewById(R.id.msg_btn);
            clear=view.findViewById(R.id.clear_btn);


            msgtitle=view.findViewById(R.id.msgtitle);

            dialog= new AlertDialog.Builder(getActivity())
                    .setView(view)
                    .setTitle("Message Board")
                    .setIcon(R.drawable.uba)


                  /*  .setPositiveButton(
                            "Send",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(msgtitle.length()==0)
                                    {
*//*                                        YoYo.with(Techniques.BounceInUp)
                                                .duration(700)
                                                .playOn(view.findViewById(R.id.msgtitle));*//*

                                    }
                                    else
                                    {
                                        insertToDB(msgtitle.getText().toString(),msgsubject.getText().toString());
                                        dialog.dismiss();
                                    }
                                }
                            })
                    .setNegativeButton(
                            "Clear",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    msgtitle.setText("");
                                    msgsubject.setText("");
                                    insertToDB(msgtitle.getText().toString(),msgsubject.getText().toString());
                                    dialog.cancel();
                                }
                            })*/
                    .create();
            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(msgtitle.length()==0)
                    {
                                       YoYo.with(Techniques.BounceInUp)
                                                .duration(700)
                                                .playOn(view.findViewById(R.id.msgtitle));

                    }
                    else
                    {
                        insertToDB(msgtitle.getText().toString(),msgsubject.getText().toString());
                        dialog.dismiss();

                    }
                }
            });
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msgtitle.setText("");
                    msgsubject.setText("");
                    insertToDB(msgtitle.getText().toString(),msgsubject.getText().toString());
                    dialog.dismiss();
                }
            });
/*            dialog.requestWindowFeature(Window.FEATURE_RIGHT_ICON);
            dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.uba);*/
            return dialog;
        }

        void  insertToDB(String s1,String s2)
        {
            final String title=s1;
            final String message=s2;
            // Showing progress dialog at user registration time.
            progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
            progressDialog.show();
            // Creating string request with post method.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpupdateUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String ServerResponse) {

                            // Hiding the progress dialog after all task complete.
                            progressDialog.dismiss();


                            if(ServerResponse.compareTo("0")==0) {


                            }
                            else {


                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                            // Hiding the progress dialog after all task complete.
                            progressDialog.dismiss();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {

                    // Creating Map String Params.
                    Map<String, String> params = new HashMap<String, String>();

                    // Adding All values to Params.					goingto

                    params.put("title",title);
                    params.put("message",message );


                    return params;
                }

            };

            // Creating RequestQueue.
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            // Adding the StringRequest object into requestQueue.
            requestQueue.add(stringRequest);

        }
    }


}
