package ubasurvey.nawin.com.ubasurvey;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.support.v7.widget.SwitchCompat;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private int request_Code = 1;
    ChoiceApplication globalVar;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    static final String KEY = "user";
    static final String KEY1 = "password";
    TextView location;
    // Creating EditText.
    EditText usernameEdittext, passwordEdittext, Email;
    // Creating button;
    Button InsertButton;

    SwitchCompat mode;
    // Creating Volley RequestQueue.
    RequestQueue requestQueue;
    // Create string variable to hold the EditText Value.
    String userNameValue, passwordNameValue, EmailHolder;

    // Creating Progress dialog.
    ProgressDialog progressDialog;

    // Storing server url into String variable.
    String HttpUrl;// = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/checklogin.php";
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("username", MODE_PRIVATE);
        editor = prefs.edit();

        HttpUrl = getString(R.string.url) + "checklogin.php";
        relativeLayout = (RelativeLayout) findViewById(R.id.loginrelativelayout);
        globalVar = (ChoiceApplication) getApplicationContext();
        globalVar.setMode(false);
        // Assigning ID's to EditText.
        usernameEdittext = (EditText) findViewById(R.id.input_email);
        passwordEdittext = (EditText) findViewById(R.id.input_password);
        usernameEdittext.setText(prefs.getString("user", ""));
        mode = findViewById(R.id.mode);
        // Assigning ID's to Button.
        InsertButton = (Button) findViewById(R.id.btn_login);
        location = (TextView) findViewById(R.id.link_signup);
        location.setMovementMethod(LinkMovementMethod.getInstance());
        Spannable spans = (Spannable) location.getText();
        ClickableSpan clickSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                //put whatever you like here, below is an example

                Intent i = null;
                if (!TextUtils.isEmpty(usernameEdittext.getText())) {
                    i = new Intent(LoginActivity.this, ChangePswActivity.class);
                    i.putExtra("value1", usernameEdittext.getText().toString());
                    startActivityForResult(i, request_Code);
                } else if (usernameEdittext.getText().length() == 0) {
                    usernameEdittext.setError("Cannot be empty");
                }

            }
        };
        spans.setSpan(clickSpan, 0, spans.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    globalVar.setMode(true);
                } else {
                    globalVar.setMode(false);
                }
            }
        });

        // Creating Volley newRequestQueue .
        requestQueue = Volley.newRequestQueue(LoginActivity.this);

        progressDialog = new ProgressDialog(LoginActivity.this);

        // Adding click listener to button.
        InsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(usernameEdittext.getText())) {
                    YoYo.with(Techniques.Bounce)
                            .duration(700)
                            .playOn(findViewById(R.id.input_email));
                    return;
                }


                // Calling method to get value from EditText.
                GetValueFromEditText();

                if (globalVar.getMode())
                    checkOfflineLogin();
                else {
                    // Showing progress dialog at user registration time.
                    progressDialog.setMessage("Please Wait, We are Authenticating on Server");
                    progressDialog.show();
                    checkOnlineLogin();
                }


            }

            void checkOfflineLogin() 
            {
                SharedPreferences prefs = getSharedPreferences("username", MODE_PRIVATE);
                String offlinepassword = prefs.getString(KEY1, null);
                if (offlinepassword != null) {
                    if(passwordNameValue.compareTo(offlinepassword)==0)
                    {
                        globalVar.setVolunteerID(userNameValue);

                        startActivity(new Intent(LoginActivity.this, MenuActivity.class), ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                        finish();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Login Failure", Toast.LENGTH_LONG).show();

                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "you should have Login atleast once online", Toast.LENGTH_LONG).show();
                }

            }

            void checkOnlineLogin() {
                // Creating string request with post method.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String ServerResponse) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();
/*                                Toast toast = Toast.makeText(getApplicationContext(),
                                        ServerResponse,
                                        Toast.LENGTH_LONG);

                                toast.show();*/

                                // Showing response message coming from server.
                                if (ServerResponse.compareTo("failed") == 0) {
                                    Toast.makeText(LoginActivity.this, "Login Failure", Toast.LENGTH_LONG).show();
                                } else {
                                    saveUsername(userNameValue,passwordNameValue);
                                    sendNotification(ServerResponse);
                                    globalVar.setVolunteerID(userNameValue);

                                    startActivity(new Intent(LoginActivity.this, MenuActivity.class), ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                                    finish();
                                }

                                //if(ServerResponse.compareTo("1")==0)
                                //
                                // else


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
                        params.put("first_name", userNameValue);
                        params.put("last_name", passwordNameValue);
                        // params.put("email", EmailHolder);

                        return params;
                    }

                };

                // Creating RequestQueue.
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

                // Adding the StringRequest object into requestQueue.
                requestQueue.add(stringRequest);

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_Code) {
            if (resultCode == RESULT_OK) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // Setting Alert Dialog Title
                alertDialogBuilder.setTitle("Password Change");
                // Icon Of Alert Dialog
                alertDialogBuilder.setIcon(R.drawable.uba);
                // Setting Alert Dialog Message
                alertDialogBuilder.setMessage("Are you sure,You want to exit");
                // alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                    }
                });


                if (data.getData().toString().compareTo("1") == 0) {
                    alertDialogBuilder.setMessage("Password changed Succesfully ");
                } else
                    alertDialogBuilder.setMessage("Unsuccesfull check username /oldpassword");

                Toast.makeText(LoginActivity.this, data.getData().toString(), Toast.LENGTH_LONG).show();

                // Setting Alert Dialog Message


                AlertDialog alertDialog = alertDialogBuilder.create();
                //Setting the title manually
                alertDialog.show();

            }
        }
    }

    // Creating method to get value from EditText.
    public void GetValueFromEditText() {

        userNameValue = usernameEdittext.getText().toString().trim();
        passwordNameValue = passwordEdittext.getText().toString().trim();
        // EmailHolder = Email.getText().toString().trim();

    }

    void saveUsername(String user,String password) {

        //---save the values in the EditText view to preferences---
        editor.putString(KEY, user);
        editor.putString(KEY1, password);
        //---saves the values---
        editor.commit();
    }


    void sendNotification(String ServerResponse) {
        String title = "";
        String message = "";
        try {

            JSONObject jobj = new JSONObject(ServerResponse);
            /*        */
            title = jobj.getString("title");
            message = jobj.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (title.length() != 0) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(LoginActivity.this, "111")
                            .setSmallIcon(R.drawable.uba)
                            .setContentTitle(title)
                            .setContentText(message).setChannelId("111").
                            setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(message));


            // Gets an instance of the NotificationManager service//

            NotificationManager notificationManager =

                    (NotificationManager) getSystemService(LoginActivity.this.NOTIFICATION_SERVICE);
            String channelId = "111";
            String channelDescription = "Default Channel";
// Since android Oreo notification channel is needed.
//Check if notification channel exists and if not create one
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
                if (notificationChannel == null) {
                    int importance = NotificationManager.IMPORTANCE_HIGH; //Set the importance level
                    notificationChannel = new NotificationChannel(channelId, channelDescription, importance);
                    notificationChannel.setLightColor(Color.GREEN); //Set if it is necesssary
                    notificationChannel.enableVibration(true); //Set if it is necesssary
                    notificationManager.createNotificationChannel(notificationChannel);
                }
                notificationManager.createNotificationChannel(notificationChannel);
            }


            // When you issue multiple notifications about the same type of event,
            // it’s best practice for your app to try to update an existing notification
            // with this new information, rather than immediately creating a new notification.
            // If you want to update this notification at a later date, you need to assign it an ID.
            // You can then use this ID whenever you issue a subsequent notification.
            // If the previous notification is still visible, the system will update this existing notification,
            // rather than create a new one. In this example, the notification’s ID is 001//

            notificationManager.notify(111, mBuilder.build());
        }


    }
   /* public void backup() {
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File outputFile = new File(sdcard,
                    "uba.db");

            if (!outputFile.exists())
                outputFile.createNewFile();

            File data = Environment.getDataDirectory();
            File inputFile = new File(data, "data/ubasurvey.nawin.com.ubasurvey/databases/srmpricelist.db");
            InputStream input = new FileInputStream(inputFile);
            OutputStream output = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            input.close();
        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}