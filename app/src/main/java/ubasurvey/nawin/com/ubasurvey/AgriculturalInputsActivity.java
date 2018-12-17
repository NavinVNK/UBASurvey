package ubasurvey.nawin.com.ubasurvey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AgriculturalInputsActivity extends AppCompatActivity {
    // Creating Progress dialog.
    Switch chemicalfertilisersSwitch_Handler, chemicalInsecticidesSwitch_Handler,chemicalWeedicidesSwitch_Handler,organicManures_Switch_Handler;
    EditText chemicalFertilisersAcre_Handler,chemicalInsecticidesAcre_Handler,chemicalWeedicidesAcres_Handler,organicManuresAcres_Handler;
    ProgressDialog progressDialog;
    ChoiceApplication globalVar;
    Button agricultural_btn_submit_handler;

    Spinner typeofIrrigationSpinnerHandler,systemofIrrigationSpinnerHandler;
    String ubaid, chemicalfertilisersValue,chemicalinsecticidesValue,chemicalweedicidesValue,organicmanuresValue,modeofIrrigationValue,systemofIrrigationValue;

    // Storing server url into String variable.
    String HttpInsertUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateformnine.php";
    String HttpSelectUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubagetformone.php";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agricultural_inputs);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .agricoordinatelayout);
        progressDialog = new ProgressDialog(AgriculturalInputsActivity.this);
        chemicalFertilisersAcre_Handler = findViewById(R.id.chemical_fertilisers_text);
        chemicalInsecticidesAcre_Handler = findViewById(R.id.chemical_insecticides_text);
        chemicalWeedicidesAcres_Handler = findViewById(R.id.chemicalweedicide_text);
        organicManuresAcres_Handler = findViewById(R.id.organicmanures_text);
        typeofIrrigationSpinnerHandler=findViewById(R.id.modeofIrrigationSpinner);
        systemofIrrigationSpinnerHandler=findViewById(R.id.irrigationSystemSpinner);
        globalVar=(ChoiceApplication)getApplicationContext();
        ubaid=globalVar.getUbaid();

        agricultural_btn_submit_handler = findViewById(R.id.agricultural_btn_submit);




        chemicalfertilisersSwitch_Handler = (Switch) findViewById(R.id.chemicalfertilisers_switch);
        chemicalFertilisersAcre_Handler = (EditText) findViewById(R.id.chemical_fertilisers_text);
        chemicalfertilisersSwitch_Handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String statusSwitch1, statusSwitch2;
                if (chemicalfertilisersSwitch_Handler.isChecked()) {
                    //statusSwitch1 = pipedWaterSource_Handler.getTextOn().toString();
                    chemicalFertilisersAcre_Handler.setVisibility(View.VISIBLE);
                }
                else
                    chemicalFertilisersAcre_Handler.setVisibility(View.GONE);
                // statusSwitch1 = pipedWaterSource_Handler.getTextOff().toString();
                //Toast.makeText(getApplicationContext(), "Switch1 :" + statusSwitch1 + "\n", Toast.LENGTH_LONG).show(); // display the current state for switch's
            }
        });

        chemicalInsecticidesSwitch_Handler = (Switch) findViewById(R.id.chemicalinsecticides_switch);
        chemicalInsecticidesAcre_Handler = (EditText) findViewById(R.id.chemical_insecticides_text);
        chemicalInsecticidesSwitch_Handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String statusSwitch1, statusSwitch2;
                if (chemicalInsecticidesSwitch_Handler.isChecked()) {
                    //statusSwitch1 = pipedWaterSource_Handler.getTextOn().toString();
                    chemicalInsecticidesAcre_Handler.setVisibility(View.VISIBLE);
                }

                else
                    chemicalInsecticidesAcre_Handler.setVisibility(View.GONE);
                //statusSwitch1 = pipedWaterSource_Handler.getTextOff().toString();
                //Toast.makeText(getApplicationContext(), "Switch1 :" + statusSwitch1 + "\n", Toast.LENGTH_LONG).show(); // display the current state for switch's
            }
        });

        chemicalWeedicidesSwitch_Handler = (Switch) findViewById(R.id.chemicalweedicide_switch);
        chemicalWeedicidesAcres_Handler = (EditText) findViewById(R.id.chemicalweedicide_text);
        chemicalWeedicidesSwitch_Handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String statusSwitch1, statusSwitch2;
                if (chemicalWeedicidesSwitch_Handler.isChecked()) {
                    //statusSwitch1 = pipedWaterSource_Handler.getTextOn().toString();
                    chemicalWeedicidesAcres_Handler.setVisibility(View.VISIBLE);
                }
                else
                    chemicalWeedicidesAcres_Handler.setVisibility(View.GONE);
                //statusSwitch1 = pipedWaterSource_Handler.getTextOff().toString();
                //Toast.makeText(getApplicationContext(), "Switch1 :" + statusSwitch1 + "\n", Toast.LENGTH_LONG).show(); // display the current state for switch's
            }
        });

        organicManures_Switch_Handler = (Switch) findViewById(R.id.organicmanures_switch);
        organicManuresAcres_Handler = (EditText) findViewById(R.id.organicmanures_text);
        organicManures_Switch_Handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String statusSwitch1, statusSwitch2;
                if (organicManures_Switch_Handler.isChecked()) {
                    //statusSwitch1 = pipedWaterSource_Handler.getTextOn().toString();
                    organicManuresAcres_Handler.setVisibility(View.VISIBLE);
                }
                else
                    organicManuresAcres_Handler.setVisibility(View.GONE);
                //statusSwitch1 = pipedWaterSource_Handler.getTextOff().toString();
                //Toast.makeText(getApplicationContext(), "Switch1 :" + statusSwitch1 + "\n", Toast.LENGTH_LONG).show(); // display the current state for switch's
            }
        });
        if(globalVar.getMenu()==1) {
            setValuetoForm(globalVar.getJsonString());
            agricultural_btn_submit_handler.setText("Update");
        }

        agricultural_btn_submit_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( getValueFromForm()) {
                    if (globalVar.getMenu() == 1) {
                       insertToDB(HttpInsertUrl);
                    } else
                        insertToDB(HttpInsertUrl);
                    ;

                }
                else
                {
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .playOn(findViewById(R.id.agricultural_btn_submit));
                }


                //Intent i = new Intent(HouseholdActivity.this, FamilyMembersInfo.class);
                // Starts TargetActivity
                // startActivity(i);
            }
        });

    }

    private boolean getValueFromForm() {

        //EditText chemicalFertilisersAcre_Handler,chemicalInsecticidesAcre_Handler,chemicalWeedicidesAcres_Handler,organicManuresAcres_Handler;
        //Spinner typeofIrrigationSpinnerHandler,systemofIrrigationSpinnerHandler;
        //String ubaid, chemicalfertilisersValue,chemicalinsecticidesValue,chemicalweedicidesValue,organicmanuresValue,modeofIrrigationValue,systemofIrrigationValue;


        if(chemicalfertilisersSwitch_Handler.isChecked()) {
            chemicalfertilisersValue = String.valueOf(chemicalFertilisersAcre_Handler.getText());
            if(chemicalfertilisersValue.length()==0)
                chemicalFertilisersAcre_Handler.setError("Cannot be empty");
        }
        else
            chemicalfertilisersValue = "0";
        if(chemicalInsecticidesSwitch_Handler.isChecked()) {
            chemicalinsecticidesValue = String.valueOf(chemicalInsecticidesAcre_Handler.getText());
            if(chemicalinsecticidesValue.length()==0)
              chemicalInsecticidesAcre_Handler.setError("Cannot be empty");
        }
        else
            chemicalinsecticidesValue ="0";
        if(chemicalWeedicidesSwitch_Handler.isChecked()) {
            chemicalweedicidesValue = String.valueOf(chemicalWeedicidesAcres_Handler.getText());
            if(chemicalweedicidesValue.length()==0)
                chemicalWeedicidesAcres_Handler.setError("Cannot be empty");
        }
        else
            chemicalweedicidesValue ="0";
        if(organicManures_Switch_Handler.isChecked()) {
            organicmanuresValue = String.valueOf(organicManuresAcres_Handler.getText());
            if(organicmanuresValue.length()==0)
               organicManuresAcres_Handler.setError("Cannot be empty");
        }
        else
            organicmanuresValue ="0";
        modeofIrrigationValue = typeofIrrigationSpinnerHandler.getSelectedItem().toString();
        systemofIrrigationValue = systemofIrrigationSpinnerHandler.getSelectedItem().toString();
        if(modeofIrrigationValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)typeofIrrigationSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(systemofIrrigationValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)systemofIrrigationSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }

        if(modeofIrrigationValue.compareTo("Select Value")==0||systemofIrrigationValue.compareTo("Select Value")==0||chemicalweedicidesValue.length()==0||chemicalinsecticidesValue.length()==0||chemicalinsecticidesValue.length()==0)
            return false;
        else
            return true;

        /*if(systemofIrrigationValue.compareTo("Select Value")==0)
            return false;
        else
            return true;*/

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

                            if(globalVar.getMenu()==0)
                            {
                                 Intent i = new Intent(AgriculturalInputsActivity.this, AgriProduceActivity.class);

                                // Starts TargetActivity
                                  startActivity(i);
                            }

                            else
                            {
/*
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        ServerResponse,
                                        Toast.LENGTH_LONG);

                                toast.show();*/
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
                params.put("chemicalfertiliser",chemicalfertilisersValue);
                params.put("chemicalinsecticides", chemicalinsecticidesValue);
                params.put("chemicalweedicides", chemicalweedicidesValue);
                params.put("organicmanures", organicmanuresValue);
                params.put("irrigationMode", modeofIrrigationValue);
                params.put("irrigationSystem", systemofIrrigationValue);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(AgriculturalInputsActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }


    public void setValuetoForm(String jsonString){

        try {
            JSONObject jobj = new JSONObject(jsonString);
            chemicalfertilisersValue=jobj.getString("chemicalfertiliser");
            chemicalinsecticidesValue = jobj.getString("chemicalinsecticides");
            chemicalweedicidesValue = jobj.getString("chemicalweedicides");
            organicmanuresValue = jobj.getString("organicmanures");
            modeofIrrigationValue = jobj.getString("irrigationMode");
            systemofIrrigationValue = jobj.getString("irrigationSystem");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(chemicalfertilisersValue.compareTo("0")==0)
             chemicalfertilisersSwitch_Handler.setChecked(false);
        else {
            chemicalfertilisersSwitch_Handler.setChecked(true);
            chemicalFertilisersAcre_Handler.setVisibility(View.VISIBLE);
            chemicalFertilisersAcre_Handler.setText(chemicalfertilisersValue);
        }
        if(chemicalinsecticidesValue.compareTo("0")==0)
            chemicalInsecticidesSwitch_Handler.setChecked(false);
        else {
            chemicalInsecticidesSwitch_Handler.setChecked(true);
            chemicalInsecticidesAcre_Handler.setVisibility(View.VISIBLE);
            chemicalInsecticidesAcre_Handler.setText(chemicalinsecticidesValue);
        }
        if(chemicalweedicidesValue.compareTo("0")==0)
            chemicalWeedicidesSwitch_Handler.setChecked(false);
        else
            {
                chemicalWeedicidesSwitch_Handler.setChecked(true);
                chemicalWeedicidesAcres_Handler.setVisibility(View.VISIBLE);
                chemicalWeedicidesAcres_Handler.setText(chemicalweedicidesValue);
            }
        if(organicmanuresValue.compareTo("0")==0)
            organicManures_Switch_Handler.setChecked(false);
        else
        {
            organicManures_Switch_Handler.setChecked(true);
            organicManuresAcres_Handler.setVisibility(View.VISIBLE);
            organicManuresAcres_Handler.setText(chemicalweedicidesValue);
        }
        if(modeofIrrigationValue.compareTo("null")==0)
            typeofIrrigationSpinnerHandler.setSelection(0);
        else            
        typeofIrrigationSpinnerHandler.setSelection(setSpinnerPos(typeofIrrigationSpinnerHandler,modeofIrrigationValue));
        if(systemofIrrigationValue.compareTo("null")==0)
            systemofIrrigationSpinnerHandler.setSelection(0);
        else
            systemofIrrigationSpinnerHandler.setSelection(setSpinnerPos(systemofIrrigationSpinnerHandler,systemofIrrigationValue));
        // radio_toilet_column1_Handler.ch
        //ubaid=stateSpinnerValue+districtCode+villageCode+householdIDValue;



    }
    int  setSpinnerPos(Spinner spinner,String value)
    {


        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        return myAdap.getPosition(value);

        //set the default according to value
        // spinner.setSelection(spinnerPosition);
    }

}