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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.*;
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

public class WaterSourceActivity extends AppCompatActivity {
    // Creating Progress dialog.
    Switch pipedWaterSource_Handler, communityWaterTap_Handler,handPump_Handler,openWell_Handler;
    EditText pipedWaterDistance_Handler,communityWaterTapDistance_Handler,handPumpDistance_Handler,openWellDistance_Handler,otherSource_Handler;
    ProgressDialog progressDialog;
    ChoiceApplication globalVar;
    Button waterSource_btn_submit_handler;

    Spinner typeofWaterSourceSpinnerHandler;
    String ubaid, pipedWaterDistanceValue,communityWaterTapDistanceValue,handPumpDistanceValue,openWellDistanceValue,modeofWaterStorageValue,otherSourceValue;

    // Storing server url into String variable.
    String HttpInsertUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateformseven.php";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_source);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .watersourcecoordinatorLayout);
        progressDialog = new ProgressDialog(WaterSourceActivity.this);
        globalVar=(ChoiceApplication)getApplicationContext();
        ubaid=globalVar.getUbaid();

        otherSource_Handler = (EditText)findViewById(R.id.other_source_waterstorage);
        typeofWaterSourceSpinnerHandler=(Spinner)findViewById(R.id.typeofwaterstorageSpinner);
        pipedWaterDistance_Handler = (EditText)findViewById(R.id.piped_water_distance);
        pipedWaterDistance_Handler.setVisibility(View.GONE);
        pipedWaterSource_Handler = (Switch) findViewById(R.id.pipedwater_switch);
        pipedWaterSource_Handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String statusSwitch1, statusSwitch2;
                if (pipedWaterSource_Handler.isChecked()) {
                    //statusSwitch1 = pipedWaterSource_Handler.getTextOn().toString();
                    pipedWaterDistance_Handler.setVisibility(View.VISIBLE);
                }
                else{
                    pipedWaterDistance_Handler.setVisibility(View.GONE);
                pipedWaterDistance_Handler.setText("");}

                //else
                // statusSwitch1 = pipedWaterSource_Handler.getTextOff().toString();
                //Toast.makeText(getApplicationContext(), "Switch1 :" + statusSwitch1 + "\n", Toast.LENGTH_LONG).show(); // display the current state for switch's
            }
        });
        communityWaterTapDistance_Handler = (EditText) findViewById(R.id.community_water_tap_distance);
        communityWaterTapDistance_Handler.setVisibility(View.GONE);
        communityWaterTap_Handler = (Switch) findViewById(R.id.communitywatertap_switch);
        communityWaterTap_Handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String statusSwitch1, statusSwitch2;
                if (communityWaterTap_Handler.isChecked()) {
                    //statusSwitch1 = pipedWaterSource_Handler.getTextOn().toString();
                    communityWaterTapDistance_Handler.setVisibility(View.VISIBLE);
                }
                else{
                    communityWaterTapDistance_Handler.setVisibility(View.GONE);
                communityWaterTapDistance_Handler.setText("");}
                //Toast.makeText(getApplicationContext(), "Switch1 :" + statusSwitch1 + "\n", Toast.LENGTH_LONG).show(); // display the current state for switch's
            }
        });

        handPump_Handler = (Switch) findViewById(R.id.handpump_switch);
        handPumpDistance_Handler = (EditText) findViewById(R.id.hand_pump_Distance);
        handPumpDistance_Handler.setVisibility(View.GONE);
        handPump_Handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String statusSwitch1, statusSwitch2;
                if (handPump_Handler.isChecked()) {
                    //statusSwitch1 = pipedWaterSource_Handler.getTextOn().toString();
                    handPumpDistance_Handler.setVisibility(View.VISIBLE);
                }
                else {
                    handPumpDistance_Handler.setVisibility(View.GONE);
                    handPumpDistance_Handler.setText("");
                }

                //statusSwitch1 = pipedWaterSource_Handler.getTextOff().toString();
                //Toast.makeText(getApplicationContext(), "Switch1 :" + statusSwitch1 + "\n", Toast.LENGTH_LONG).show(); // display the current state for switch's
            }
        });

        openWell_Handler = (Switch) findViewById(R.id.openwell_switch);
        openWellDistance_Handler = (EditText) findViewById(R.id.open_well_distance);
        openWellDistance_Handler.setVisibility(View.GONE);
        openWell_Handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String statusSwitch1, statusSwitch2;
                if (openWell_Handler.isChecked()) {
                    //statusSwitch1 = pipedWaterSource_Handler.getTextOn().toString();
                    openWellDistance_Handler.setVisibility(View.VISIBLE);
                }
                else {
                    openWellDistance_Handler.setVisibility(View.GONE);
                    openWellDistance_Handler.setText("");
                }


                //statusSwitch1 = pipedWaterSource_Handler.getTextOff().toString();
                //Toast.makeText(getApplicationContext(), "Switch1 :" + statusSwitch1 + "\n", Toast.LENGTH_LONG).show(); // display the current state for switch's
            }
        });

        waterSource_btn_submit_handler = findViewById(R.id.watersource_btn_submit);
        if(globalVar.getMenu()==1) {
            setValuetoForm(globalVar.getJsonString());
            waterSource_btn_submit_handler.setText("Update");
        }

        waterSource_btn_submit_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( getValueFromForm()) {

                        insertToDB(HttpInsertUrl);


                }
                else
                {
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .playOn(findViewById(R.id.watersource_btn_submit));
                }


                //Intent i = new Intent(HouseholdActivity.this, FamilyMembersInfo.class);
                // Starts TargetActivity
                // startActivity(i);
            }
        });





    }

    private boolean getValueFromForm() {

       if(pipedWaterSource_Handler.isChecked()) {
           pipedWaterDistanceValue = String.valueOf(pipedWaterDistance_Handler.getText());
           if(pipedWaterDistanceValue.length()==0)
               pipedWaterDistance_Handler.setError("Cannot be empty");
       }
       else
           pipedWaterDistanceValue ="NA";
       if(communityWaterTap_Handler.isChecked()) {

           communityWaterTapDistanceValue = String.valueOf(communityWaterTapDistance_Handler.getText());
           if(communityWaterTapDistanceValue.length()==0)
              communityWaterTapDistance_Handler.setError("Cannot be empty");
       }
       else
           communityWaterTapDistanceValue="NA";
       if(handPump_Handler.isChecked()) {

           handPumpDistanceValue = String.valueOf(handPumpDistance_Handler.getText());
           if(handPumpDistanceValue.length()==0)
               handPump_Handler.setError("Cannot be empty");
       }
       else
           handPumpDistanceValue="NA";
       if(openWell_Handler.isChecked()) {

           openWellDistanceValue = String.valueOf(openWellDistance_Handler.getText());
           if(openWellDistanceValue.length()==0)
               openWell_Handler.setError("Cannot be empty");
       }
       else
           openWellDistanceValue="NA";
        otherSourceValue = String.valueOf(otherSource_Handler.getText());

        modeofWaterStorageValue = typeofWaterSourceSpinnerHandler.getSelectedItem().toString();

        if(modeofWaterStorageValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)typeofWaterSourceSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }

        if(modeofWaterStorageValue.compareTo("Select Value")==0||(pipedWaterSource_Handler.isChecked()&&pipedWaterDistanceValue.compareTo("")==0)||
                (communityWaterTap_Handler.isChecked()&&communityWaterTapDistanceValue.compareTo("")==0)||
                ( handPump_Handler.isChecked()&&handPumpDistanceValue.compareTo("")==0)||
        ( openWell_Handler.isChecked()&&openWellDistanceValue.compareTo("")==0))
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
/*
                        Toast toast = Toast.makeText(getApplicationContext(),
                                ServerResponse,
                                Toast.LENGTH_LONG);

                        toast.show();*/
                        if(ServerResponse.compareTo("0")==0) {


                        }
                        else
                        {

                            if(globalVar.getMenu()==0)
                            {
                               Intent i = new Intent(WaterSourceActivity.this, SourceEnergyActivity.class);

                                // Starts TargetActivity
                                startActivity(i);
                            }

                            else
                            {
                                globalVar.setJsonString(ServerResponse);

                            }
                        }
                        // Intent i = new Intent(BasicinfoActivity.this, HouseholdActivity.class);
                        // Starts TargetActivity
                        // startActivity(i);
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
                params.put("pipedwater",pipedWaterDistanceValue);
                params.put("communitywater", communityWaterTapDistanceValue);
                params.put("handpump", handPumpDistanceValue);
                params.put("openwell", openWellDistanceValue);
                params.put("waterstorage", modeofWaterStorageValue);
                params.put("othersource", otherSourceValue);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(WaterSourceActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }


    public void setValuetoForm(String jsonString){

        try {
            JSONObject jobj = new JSONObject(jsonString);


            pipedWaterDistanceValue=jobj.getString("pipedwater");
            communityWaterTapDistanceValue = jobj.getString("communitywater");
            handPumpDistanceValue = jobj.getString("handpump");
            openWellDistanceValue = jobj.getString("openwell");
            modeofWaterStorageValue=jobj.getString("waterstorage");
            otherSourceValue = jobj.getString("othersource");




        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(pipedWaterDistanceValue.compareTo("NA")==0||pipedWaterDistanceValue.compareTo("0")==0)
        {
            pipedWaterSource_Handler.setChecked(false);
            pipedWaterDistance_Handler.setText("");
        }
        else {
            pipedWaterSource_Handler.setChecked(true);
            pipedWaterDistance_Handler.setVisibility(View.VISIBLE);
            pipedWaterDistance_Handler.setText(pipedWaterDistanceValue);
        }

        if(communityWaterTapDistanceValue.compareTo("NA")==0||communityWaterTapDistanceValue.compareTo("0")==0)
        {
            communityWaterTap_Handler.setChecked(false);
            communityWaterTapDistance_Handler.setText("");
        }
        else {
            communityWaterTap_Handler.setChecked(true);
            communityWaterTapDistance_Handler.setVisibility(View.VISIBLE);
            communityWaterTapDistance_Handler.setText(pipedWaterDistanceValue);
        }

         if(handPumpDistanceValue.compareTo("NA")==0||handPumpDistanceValue.compareTo("0")==0)
         {
             handPump_Handler.setChecked(false);
             handPumpDistance_Handler.setText("");
         }
         else {
             handPump_Handler.setChecked(true);
             handPumpDistance_Handler.setVisibility(View.VISIBLE);
             handPumpDistance_Handler.setText(handPumpDistanceValue);
         }
        if(openWellDistanceValue.compareTo("NA")==0||openWellDistanceValue.compareTo("0")==0)
        {
            openWell_Handler.setChecked(false);
            openWellDistance_Handler.setText("");
        }
        else {
            openWell_Handler.setChecked(true);
            openWellDistance_Handler.setVisibility(View.VISIBLE);
            openWellDistance_Handler.setText(openWellDistanceValue);
        }

     if(otherSourceValue.compareTo("0")==0)
         otherSource_Handler.setText("");
      else
        otherSource_Handler.setText(otherSourceValue);
      if(modeofWaterStorageValue.compareTo("null")==0)
          typeofWaterSourceSpinnerHandler.setSelection(0);

          else
              typeofWaterSourceSpinnerHandler.setSelection(setSpinnerPos(typeofWaterSourceSpinnerHandler,modeofWaterStorageValue));
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