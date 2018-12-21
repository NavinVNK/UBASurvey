package ubasurvey.nawin.com.ubasurvey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class HouseholdActivity extends AppCompatActivity {
    // Creating Progress dialog.
    ProgressDialog progressDialog;
    ChoiceApplication globalVar;
    Button household_btn_submit_handler;
    TextView householdID_Handler,household_headNameValue_handler;
    EditText  annualIncome_Handler;
    Spinner typeofhouseSpinnerHandler,categorySpinnerHandler,poverty_statusSpinnerHandler,ownhouseSpinnerHandler
            ,toilet_column1SpinnerHandler,drainage_SystemSpinnerHandler,waste_CollectionSpinnerHandler,compostSpinnerHandler,biogasSpinnerHandler;

    String ubaid,householdID,household_headNameValue,categoryValue,povertyStatusValue,ownHouseValue,typeHouseValue,toiletValue;
    String drainageValue,wastageValue,compostValue,biogasValue,annualIncomeValue;

    // Storing server url into String variable.
    String HttpInsertUrl ;//= "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateformtwo.php";
   // String HttpSelectUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubagetformone.php";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_household);
        HttpInsertUrl=getString(R.string.url)+"ubaupdateformtwo.php";
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .householdcoordinatorLayout);
        Bundle bundle = getIntent().getExtras();
        progressDialog = new ProgressDialog(HouseholdActivity.this);
        globalVar=(ChoiceApplication)getApplicationContext();
        ubaid=globalVar.getUbaid();

        householdID_Handler = (TextView)findViewById(R.id.householdID);

        household_headNameValue_handler = (TextView)findViewById(R.id.household_HeadName);
        annualIncome_Handler = findViewById(R.id.annualIncome);
       
        categorySpinnerHandler = (Spinner)findViewById(R.id.spinner_category);
        poverty_statusSpinnerHandler = (Spinner)findViewById(R.id.spinner_poverty_status);
        ownhouseSpinnerHandler = (Spinner)findViewById(R.id.spinner_ownhouse);
        toilet_column1SpinnerHandler= (Spinner) findViewById(R.id.spinner_toilet_column1);
        drainage_SystemSpinnerHandler=(Spinner)findViewById(R.id.spinner_drainage_column1);

        waste_CollectionSpinnerHandler= (Spinner)findViewById(R.id.spinner_DoorStep_column1);
        compostSpinnerHandler=(Spinner)findViewById(R.id.spinner_compost);

        biogasSpinnerHandler = findViewById(R.id.spinner_biogas_column1);
       
        typeofhouseSpinnerHandler=findViewById(R.id.typeofhouseSpinner);
        //genderSpinnerHandler=findViewById(R.id.spinner_Gender);

        household_btn_submit_handler = findViewById(R.id.household_btn_submit);
        if(globalVar.getMenu()>0) {
            //selectDatafromDB(globalVar.getUbaid());
            /*Toast toast = Toast.makeText(getApplicationContext(),
                    "Basic info"+globalVar.getJsonString(),
                    Toast.LENGTH_LONG);

            toast.show();*/
            setValuetoForm(globalVar.getJsonString());
            household_btn_submit_handler.setText("Update");
        }
        else {
            householdID_Handler.setText(bundle.getString("houseid"));
            household_headNameValue_handler.setText(bundle.getString("househeadname"));
        }

        household_btn_submit_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( getValueFrom()) {

                        insertToDB(HttpInsertUrl);
                }
                else
                {
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .playOn(findViewById(R.id.household_btn_submit));
                }


            }
        });
    }

    private boolean getValueFrom() {


       // householdID = String.valueOf(householdID_Handler.getText());
        household_headNameValue = String.valueOf(household_headNameValue_handler.getText());

       // genderValue = genderSpinnerHandler.getSelectedItem().toString();
        categoryValue=categorySpinnerHandler.getSelectedItem().toString();
        povertyStatusValue = poverty_statusSpinnerHandler.getSelectedItem().toString();
        ownHouseValue = ownhouseSpinnerHandler.getSelectedItem().toString();
        typeHouseValue = typeofhouseSpinnerHandler.getSelectedItem().toString();
        toiletValue = toilet_column1SpinnerHandler.getSelectedItem().toString();
        drainageValue =drainage_SystemSpinnerHandler.getSelectedItem().toString();
        wastageValue = waste_CollectionSpinnerHandler.getSelectedItem().toString();
        compostValue = compostSpinnerHandler.getSelectedItem().toString();;
        biogasValue = biogasSpinnerHandler.getSelectedItem().toString();
        annualIncomeValue = String.valueOf(annualIncome_Handler.getText());

        if(annualIncomeValue.length()==0){
            annualIncome_Handler.setError("Cannot be empty");
        }


/*        if(genderValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)genderSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }*/
        if(categoryValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)categorySpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(typeHouseValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)typeofhouseSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(toiletValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)toilet_column1SpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(wastageValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)waste_CollectionSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(drainageValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)drainage_SystemSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(povertyStatusValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)poverty_statusSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(ownHouseValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)ownhouseSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(compostValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)compostSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(biogasValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)biogasSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }   

        if(typeHouseValue.compareTo("Select Value")==0||categoryValue.compareTo("Select Value")==0||
                toiletValue.compareTo("Select Value")==0||wastageValue.compareTo("Select Value")==0||drainageValue.compareTo("Select Value")==0||annualIncomeValue.compareTo("")==0
                ||household_headNameValue.compareTo("")==0||povertyStatusValue.compareTo("Select Value")==0||ownHouseValue.compareTo("Select Value")==0
                ||compostValue.compareTo("Select Value")==0||biogasValue.compareTo("Select Value")==0)
            return false;
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

                         /*Toast toast = Toast.makeText(getApplicationContext(),
                                ServerResponse,
                                Toast.LENGTH_LONG);
                        toast.show();*/
                        if(ServerResponse.compareTo("0")==0) {


                        }
                        else
                        {

                            if(globalVar.getMenu()==0)
                            {
                                Intent i = new Intent(HouseholdActivity.this, RespondentProfileActivity.class);

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
                        //Toast.makeText(HouseholdActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("ubaid", ubaid);


                params.put("category", categoryValue);
                params.put("povertystatus", povertyStatusValue);
                params.put("ownhouse", ownHouseValue);
                params.put("typeofhouse", typeHouseValue);
                params.put("toilet", toiletValue);
                params.put("drainage",drainageValue);
                params.put("wastecollection", wastageValue);
                params.put("compostpit", compostValue);
                params.put("biogasplant", biogasValue);
                params.put("annualincome", annualIncomeValue);


                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(HouseholdActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    public void setValuetoForm(String jsonString){

        try {
            JSONObject jobj = new JSONObject(jsonString);
            householdID=jobj.getString("householdid");
            if(householdID.compareTo("null")==0)
                householdID= "";
            household_headNameValue=jobj.getString("nameofthehead");
            if(household_headNameValue.compareTo("null")==0)
                household_headNameValue = "";

            categoryValue = jobj.getString("category");
            if(categoryValue.compareTo("null")==0)
                    categoryValue = "Select Value";
            povertyStatusValue = jobj.getString("povertystatus");
                    if(povertyStatusValue.compareTo("null")==0)
                                povertyStatusValue = "Select Value";
            ownHouseValue = jobj.getString("ownhouse");
            if(ownHouseValue.compareTo("null")==0)
                  ownHouseValue= "Select Value";
            toiletValue =jobj.getString("toilet");

            if(toiletValue.compareTo("null")==0)
                toiletValue= "Select Value";
            typeHouseValue = jobj.getString("typeofhouse");
            if(typeHouseValue.compareTo("null")==0)
                   typeHouseValue = "Select Value";
            drainageValue = jobj.getString("drainage");
            if(drainageValue.compareTo("null")==0)
            drainageValue= "Select Value";
            wastageValue = jobj.getString("wastecollection");
            if(wastageValue.compareTo("null")==0)
                  wastageValue= "Select Value";
            compostValue = jobj.getString("compostpit");
                    if(compostValue.compareTo("null")==0)
                        compostValue= "Select Value";
            biogasValue = jobj.getString("biogasplant");
            if(biogasValue.compareTo("null")==0)
                  biogasValue = "Select Value";

            annualIncomeValue = jobj.getString("annualincome");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        typeofhouseSpinnerHandler.setSelection(setSpinnerPos(typeofhouseSpinnerHandler,typeHouseValue));

        categorySpinnerHandler.setSelection(setSpinnerPos(categorySpinnerHandler,categoryValue));
        poverty_statusSpinnerHandler.setSelection(setSpinnerPos(poverty_statusSpinnerHandler,povertyStatusValue));
        ownhouseSpinnerHandler.setSelection(setSpinnerPos(ownhouseSpinnerHandler,ownHouseValue));
        toilet_column1SpinnerHandler.setSelection(setSpinnerPos(toilet_column1SpinnerHandler,toiletValue));
        drainage_SystemSpinnerHandler.setSelection(setSpinnerPos(drainage_SystemSpinnerHandler,drainageValue));
        waste_CollectionSpinnerHandler.setSelection(setSpinnerPos(waste_CollectionSpinnerHandler,wastageValue));
        biogasSpinnerHandler.setSelection(setSpinnerPos(biogasSpinnerHandler,biogasValue));
        compostSpinnerHandler.setSelection(setSpinnerPos(compostSpinnerHandler,compostValue));
        household_headNameValue_handler.setText(household_headNameValue);
        annualIncome_Handler.setText(annualIncomeValue);
        householdID_Handler.setText(householdID);

    }
    int  setSpinnerPos(Spinner spinner,String value)
    {


        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        return myAdap.getPosition(value);

        //set the default according to value
        // spinner.setSelection(spinnerPosition);
    }

}