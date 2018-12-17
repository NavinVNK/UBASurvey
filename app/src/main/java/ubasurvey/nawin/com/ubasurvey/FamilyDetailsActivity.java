package ubasurvey.nawin.com.ubasurvey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class FamilyDetailsActivity extends AppCompatActivity {
    // Creating Progress dialog.
    ProgressDialog progressDialog;
    ChoiceApplication globalVar;
    Button familydetails_btn_submit_handler;
    TextView headNameText;
    EditText  nameEdit_handler,ageEditHandler,majorHealthEdit_Handler;
    Spinner genderSpinnerHandler, maritialSpinnerHandler,educationSpinnerHandler,schoolSpinnerHandler,ssPensinSpinner,occupationSpinnerHandler;
    Switch aadharSwitchHandler,compSwitchHandler,bankACSwitchHandler,mnrgeaSwitchhandler,shGroupSwitchhandler;
    String ubaid,ubaindid,nameValue,ageValue,genderValue,maritialValue,educationValue,schoolValue,aadharValue,bankACValue,compLiteratureValue,
    ssPensionValue,majorHealthValue,mnregaValue,SHGroupvalue,occupationValue;
    // Storing server url into String variable.
    String HttpInsertUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubainsertfamilydetails.php";
    String HttpupdateUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdatefamilydetail.php";
    private boolean update;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_details);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .familycoordinatorLayout);
        Bundle bundle = getIntent().getExtras();
        progressDialog = new ProgressDialog(FamilyDetailsActivity.this);
        globalVar=(ChoiceApplication)getApplicationContext();
        ubaid=globalVar.getUbaid();
        update=false;


        nameEdit_handler = findViewById(R.id.familymembers_name_text);
/*        if(globalVar.getFamilyMemCount()==2)
            nameEdit_handler.setEnabled(false);
        else
            nameEdit_handler.setEnabled(true)*/;

        ageEditHandler = (EditText)findViewById(R.id.family_age_text);
        majorHealthEdit_Handler=(EditText)findViewById(R.id.healthproblems_text);

        genderSpinnerHandler =(Spinner)findViewById(R.id.familygender);
        maritialSpinnerHandler = (Spinner)findViewById(R.id.maritalstatusspinner);
        educationSpinnerHandler = (Spinner)findViewById(R.id.educationlevelspinner);
        schoolSpinnerHandler= (Spinner) findViewById(R.id.schoolspinner);
        occupationSpinnerHandler=(Spinner)findViewById(R.id.occupationcode);
        ssPensinSpinner=(Spinner)findViewById(R.id.socialsecuritypensionspinner);


        aadharSwitchHandler= (Switch)findViewById(R.id.aadhaarcard_switch);
        compSwitchHandler=(Switch)findViewById(R.id.computerliterate_switch);
        shGroupSwitchhandler =(Switch) findViewById(R.id.selfhelpgroups_switch);

        bankACSwitchHandler=(Switch)findViewById(R.id.bankaccount_switch);
        mnrgeaSwitchhandler=(Switch)findViewById(R.id.mnregajobcard_switch);

        familydetails_btn_submit_handler = findViewById(R.id.familydetails_btn_submit);
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
           update=true;


           // int pos=extras.getInt("position");//get from bundle
            String familyjsonValue=extras.getString("familyrecord");
            //setValuetoForm(globalVar.getFamilyjsonString(), pos);
            setValuetoForm(familyjsonValue);
            familydetails_btn_submit_handler.setText("Update");
        }
//

        familydetails_btn_submit_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( getValueFrom()) {
                    if(update)
                        insertToDB(HttpupdateUrl);
                     else
                         insertToDB(HttpInsertUrl);
                }
                else
                {
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .playOn(findViewById(R.id.familydetails_btn_submit));
                }


            }
        });
    }

    private boolean getValueFrom() {

        nameValue= String.valueOf(nameEdit_handler.getText());
        ageValue = String.valueOf(ageEditHandler.getText());
        genderValue = genderSpinnerHandler.getSelectedItem().toString();
        maritialValue=maritialSpinnerHandler.getSelectedItem().toString();
        educationValue = educationSpinnerHandler.getSelectedItem().toString();
        schoolValue = schoolSpinnerHandler.getSelectedItem().toString();
        if(bankACSwitchHandler.isChecked())
              bankACValue = bankACSwitchHandler.getTextOn().toString();
        else
            bankACValue = bankACSwitchHandler.getTextOff().toString();
        if(compSwitchHandler.isChecked())
             compLiteratureValue = compSwitchHandler.getTextOn().toString();
        else
            compLiteratureValue = compSwitchHandler.getTextOff().toString();
        if(aadharSwitchHandler.isChecked())
            aadharValue=aadharSwitchHandler.getTextOn().toString();
        else
            aadharValue=aadharSwitchHandler.getTextOff().toString();
        ssPensionValue = ssPensinSpinner.getSelectedItem().toString();
        if(shGroupSwitchhandler.isChecked())
            SHGroupvalue = shGroupSwitchhandler.getTextOn().toString();
        else
            SHGroupvalue = shGroupSwitchhandler.getTextOff().toString();
        if(mnrgeaSwitchhandler.isChecked())
            mnregaValue=mnrgeaSwitchhandler.getTextOn().toString();
        else
            mnregaValue=mnrgeaSwitchhandler.getTextOff().toString();

        majorHealthValue=String.valueOf(majorHealthEdit_Handler.getText());
        occupationValue =occupationSpinnerHandler.getSelectedItem().toString();
        if(nameValue.length()==0)
            nameEdit_handler.setError("Cannot be empty");
        if(genderValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)genderSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }

       if(nameValue.compareTo("")==0||genderValue.compareTo("Select Value")==0)
             return false;
       /* if(bankACValue.compareTo("Select Value")==0||genderValue.compareTo("Select Value")==0||maritialValue.compareTo("Select Value")==0||
                schoolValue.compareTo("Select Value")==0||aadharValue.compareTo("Select Value")==0||ageValue.compareTo("")==0
                ||povertyStatusValue.compareTo("Select Value")==0||educationValue.compareTo("Select Value")==0
                ||compLiteratureValue.compareTo("Select Value")==0||biogasValue.compareTo("Select Value")==0)
            return false;*/
        else
            return true;

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


                        if(ServerResponse.compareTo("0")==0) {


                        }
                        else
                        {

                            if(!update)
                            {
                                globalVar.setIncrement();
                                Intent data = new Intent();
                                data.putExtra(globalVar.getFamilyMemCount().toString(),"value");
                                setResult(RESULT_OK, data);

                            }

                            else
                            {
/*                                Toast toast = Toast.makeText(getApplicationContext(),
                                        ServerResponse,
                                        Toast.LENGTH_LONG);
                                toast.show();*/
                                    Intent data = new Intent();
                                    //data.setData(Uri.parse("hello"));
                                data.putExtra(ServerResponse,"value");
                                    setResult(RESULT_OK, data);


                            }
                        }
                        if(globalVar.getMenu()==0)
                        {
                            // Intent i = new Intent(RespondentProfileActivity.this, MigrationStatusActivity.class);

                            // Starts TargetActivity
                            //  startActivity(i);

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

                // Adding All values to Params.					goingto								
                params.put("ubaid", ubaid);
                if(!update)
                    ubaindid=globalVar.getFamilyMemCount().toString();
                params.put("ubaindid",ubaindid);
                params.put("name", nameValue);
                params.put("age", ageValue);
                params.put("gender",genderValue );
                params.put("marritalstatus", maritialValue);
                params.put("education", educationValue);
                params.put("school", schoolValue);
                params.put("aadharcard",aadharValue);
                params.put("bankac", bankACValue);
                params.put("compliterate", compLiteratureValue);
                params.put("pension", ssPensionValue);
                params.put("healthprob", majorHealthValue);
                params.put("mnrega", mnregaValue);
                params.put("selfhelpgroup", SHGroupvalue);
                params.put("occupation", occupationValue);


                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(FamilyDetailsActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
    void  selectDatafromDB(final String ubaidlocal)
    {
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
                        //code to globar var
                        globalVar.setJsonString(ServerResponse);
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
        RequestQueue requestQueue = Volley.newRequestQueue(FamilyDetailsActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    public void setValuetoForm(String jsonString){//,int pos

        try {
        //JSONArray jsonarray = new JSONArray(jsonString);
        //JSONObject jobj = jsonarray.getJSONObject(pos);
            JSONObject jobj = new JSONObject(jsonString);
/*            Toast toast = Toast.makeText(getApplicationContext(),
                    "Family "+jobj.toString(),
                    Toast.LENGTH_LONG);

            toast.show();*/
            ubaid=jobj.getString("ubaid");
            ubaindid =jobj.getString("ubaindid").toString();

            if(ubaindid.compareTo("1")==0) {

                nameEdit_handler.setEnabled(false);
                genderSpinnerHandler.setEnabled(false);
                nameEdit_handler.setError("HouseHead name will not be editable ");
            }
            else {
                nameEdit_handler.setEnabled(true);
                genderSpinnerHandler.setEnabled(true);
            }
            nameValue=jobj.getString("name");
            if(nameValue.compareTo("no")==0)
                nameValue = "";
            ageValue = jobj.getString("age");
            if(ageValue .compareTo("0")==0)
                ageValue="";
            genderValue = jobj.getString("gender");
            if(genderValue.compareTo("no")==0)
                genderValue= "Select Value";
            maritialValue = jobj.getString("marritalstatus");
            if(maritialValue.compareTo("no")==0)
                maritialValue = "Select Value";
            educationValue = jobj.getString("education");
            if(educationValue.compareTo("no")==0)
                educationValue = "Select Value";
            schoolValue =jobj.getString("school");
            if(schoolValue.compareTo("no")==0)
                schoolValue= "Select Value";
            aadharValue = jobj.getString("aadharcard");
            if(aadharValue.compareTo("YES")==0)
                aadharSwitchHandler.setChecked(true);
            else
                aadharSwitchHandler.setChecked(false);
            bankACValue = jobj.getString("bankac");
            if(bankACValue.compareTo("YES")==0)
                bankACSwitchHandler.setChecked(true);
            else
                bankACSwitchHandler.setChecked(false);
            compLiteratureValue = jobj.getString("compliterate");
            if(compLiteratureValue.compareTo("YES")==0)
               compSwitchHandler.setChecked(true);
            else
                compSwitchHandler.setChecked(false);

            mnregaValue=jobj.getString("mnrega");
            if(mnregaValue.compareTo("YES")==0)
                mnrgeaSwitchhandler.setChecked(true);
            else
                mnrgeaSwitchhandler.setChecked(false);
            SHGroupvalue=jobj.getString("selfhelpgroup");
            if(SHGroupvalue.compareTo("YES")==0)
                shGroupSwitchhandler.setChecked(true);
            else
                shGroupSwitchhandler.setChecked(false);
            majorHealthValue = jobj.getString("healthprob");
            if(majorHealthValue.compareTo("no")==0)
                majorHealthValue = "";
            ssPensionValue=jobj.getString("pension");
            if(ssPensionValue.compareTo("no")==0)
                ssPensionValue = "Select Value";

            occupationValue = jobj.getString("occupation");
            if(occupationValue.compareTo("no")==0)
                occupationValue= "Select Value";





        } catch (JSONException e) {
            e.printStackTrace();
        }

        genderSpinnerHandler.setSelection(setSpinnerPos(genderSpinnerHandler,genderValue));
        maritialSpinnerHandler.setSelection(setSpinnerPos(maritialSpinnerHandler,maritialValue));
        educationSpinnerHandler.setSelection(setSpinnerPos(educationSpinnerHandler,educationValue));
        schoolSpinnerHandler.setSelection(setSpinnerPos(schoolSpinnerHandler,schoolValue));
        ssPensinSpinner.setSelection(setSpinnerPos(ssPensinSpinner,ssPensionValue));
        occupationSpinnerHandler.setSelection(setSpinnerPos(occupationSpinnerHandler,occupationValue));

        nameEdit_handler.setText(nameValue);
        ageEditHandler.setText(ageValue);
        majorHealthEdit_Handler.setText(majorHealthValue);

    }
    int  setSpinnerPos(Spinner spinner,String value)
    {


        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        return myAdap.getPosition(value);

        //set the default according to value
        // spinner.setSelection(spinnerPosition);
    }


}