package ubasurvey.nawin.com.ubasurvey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

public class RespondentProfileActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    ChoiceApplication globalVar;
    Button respondentprofile_btn_submit_handler;
    EditText  respondentNameHandler,respondentAgeHandler,relationshipNameHandler,respondentmobileNoHandler,respondentIdNumberTextHandler;
    Spinner respondent_genderSpinnerHandler,respondentidTypeSpinnerHandler;

    String ubaid="",respondentNameValue="",respondentAgeValue="",relationshipNameValue="",respondentmobileNoValue="",respondentIdtypeValue="",
            respondent_genderSpinnerValue="",respondentIdnumberValue="";
    String HttpInsertUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateformthree.php";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respondent_profile);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .respondentcoordinatorLayout);
        globalVar=(ChoiceApplication)getApplicationContext();
        ubaid=globalVar.getUbaid();

        progressDialog = new ProgressDialog(RespondentProfileActivity.this);
        respondent_genderSpinnerHandler=(Spinner)findViewById(R.id.spinner_respondent_gender);
        respondentidTypeSpinnerHandler=(Spinner)findViewById(R.id.identityCardTypeSpinner);
        respondentNameHandler = (EditText)findViewById(R.id.respondentName);
        relationshipNameHandler = (EditText)findViewById(R.id.relationshipName);
        respondentmobileNoHandler = (EditText)findViewById(R.id.respondentmobileNo);
        respondentIdNumberTextHandler=(EditText)findViewById(R.id.identityCardNumberText);
        respondentAgeHandler=(EditText)findViewById(R.id.respondentage);
        respondentprofile_btn_submit_handler = (Button)findViewById(R.id.respondent_btn_submit);

        if(globalVar.getMenu()>0) {
            /*//selectDatafromDB(globalVar.getUbaid());
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Basic info"+globalVar.getJsonString(),
                    Toast.LENGTH_LONG);

            toast.show();*/
            setValuetoForm(globalVar.getJsonString());
            respondentprofile_btn_submit_handler.setText("Update");
        }
        respondentprofile_btn_submit_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( getValueFromForm()) {

                    insertToDB(HttpInsertUrl);
                }
                else
                {
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .playOn(findViewById(R.id.respondent_btn_submit));
                }


            }
        });
        }

    // Creating method to get value from EditText.
    public boolean getValueFromForm(){

        respondent_genderSpinnerValue = respondent_genderSpinnerHandler.getSelectedItem().toString();
        respondentNameValue = String.valueOf(respondentNameHandler.getText());
            relationshipNameValue = String.valueOf(relationshipNameHandler.getText());
            respondentmobileNoValue = String.valueOf(respondentmobileNoHandler.getText());
        respondentAgeValue=String.valueOf(respondentAgeHandler.getText());
        respondentIdtypeValue=respondentidTypeSpinnerHandler.getSelectedItem().toString();
        respondentIdnumberValue=String.valueOf(respondentIdNumberTextHandler.getText());

        if(respondentNameValue.length()==0){
            respondentNameHandler.setError("Cannot be empty");
        }
        if(respondentmobileNoValue.length()==0){
            respondentmobileNoHandler.setError("Cannot be empty");
        }
        if(relationshipNameValue.length()==0){
            relationshipNameHandler.setError("Cannot be empty");
        }

        if(respondent_genderSpinnerValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)respondent_genderSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }

         if(respondent_genderSpinnerValue.compareTo("Select Value")==0||respondentNameValue.compareTo("")==0||relationshipNameValue.compareTo("")==0
                 || respondentmobileNoValue.compareTo("")==0)//||respondent_genderSpinnerValue.compareTo("Select Value")==0||respondentIdtypeValue.compareTo("Select Value")==0
            return false;
        else
            return  true;

    }

    void  insertToDB(String HttpUrl)
    {
        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
        progressDialog.show();
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

/*                        Toast toast = Toast.makeText(getApplicationContext(),
                                ServerResponse,
                                Toast.LENGTH_LONG);

                        toast.show();*/
                        if(ServerResponse.compareTo("0")==0) {


                        }
                        else
                        {

                            if(globalVar.getMenu()==0)
                            {
                                Intent i = new Intent(RespondentProfileActivity.this, FamilyInfoActivity.class);

                                // Starts TargetActivity
                                startActivity(i);
                            }

                            else
                            {
                                globalVar.setJsonString(ServerResponse);

                            }
                        }
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
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("ubaid", ubaid);
                params.put("respondentname",respondentNameValue);
                params.put("respondentgender", respondent_genderSpinnerValue);
                params.put("respondentage", respondentAgeValue);
                params.put("respondentrelationship", relationshipNameValue);
                params.put("respondentcontactnumber", respondentmobileNoValue);
                params.put("respondentidcardtype", respondentIdtypeValue);
                params.put("respondentidcardnumber", respondentIdnumberValue);



                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(RespondentProfileActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    public void setValuetoForm(String jsonString){



        try {
            JSONObject jobj = new JSONObject(jsonString);
            respondentNameValue=jobj.getString("respondentname");
            respondent_genderSpinnerValue = jobj.getString("respondentgender");

            respondentAgeValue = jobj.getString("respondentage");
            relationshipNameValue =jobj.getString("respondentrelationship");
            respondentmobileNoValue = jobj.getString("respondentcontactnumber");
            respondentIdtypeValue = jobj.getString("respondentidcardtype");
            respondentIdnumberValue =jobj.getString("respondentidcardnumber");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(respondent_genderSpinnerValue.compareTo("null")==0)
            respondent_genderSpinnerValue="Select Value";
        else
         respondent_genderSpinnerHandler.setSelection(setSpinnerPos(respondent_genderSpinnerHandler,respondent_genderSpinnerValue));
        if(respondentIdtypeValue.compareTo("null")==0)
            respondentIdtypeValue="Select Value";
        else
        respondentidTypeSpinnerHandler.setSelection(setSpinnerPos(respondentidTypeSpinnerHandler,respondentIdtypeValue));
        if(respondentNameValue.compareTo("null")==0)
            respondentNameHandler.setText("");
        else
        respondentNameHandler.setText(respondentNameValue);
        if(respondentAgeValue.compareTo("null")==0)
            respondentAgeHandler.setText("");
        else
        respondentAgeHandler.setText(respondentAgeValue);
        if(relationshipNameValue.compareTo("null")==0)
            relationshipNameHandler.setText("");
        else
        relationshipNameHandler.setText(relationshipNameValue);
        if(respondentmobileNoValue.compareTo("null")==0)
            respondentmobileNoHandler.setText("");
        else
            respondentmobileNoHandler.setText(respondentmobileNoValue);
        if(respondentIdnumberValue.compareTo("null")==0)
            respondentIdNumberTextHandler.setText("");
        else
                respondentIdNumberTextHandler.setText(respondentIdnumberValue);

    }
    int  setSpinnerPos(Spinner spinner,String value)
    {


        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        return myAdap.getPosition(value);

        //set the default according to value
        // spinner.setSelection(spinnerPosition);
    }

}
