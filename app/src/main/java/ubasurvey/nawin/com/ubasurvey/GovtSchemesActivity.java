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

public class GovtSchemesActivity extends AppCompatActivity {

    EditText dhanyojana_EditHandler,ujjwalayojana_EditHandler,awasyojana_EditHandler,sukanyasamridhiyojana_EditHandler,mudrayojana_EditHandler,jivanjyotibimayojana_EditHandler,
            surakshabimayojana_EditHandler,atalpensionyojana_EditHandler,fasalbimayojana_EditHandler,kaushalvikasyojana_EditHandler,krishisinchaiyojana_EditHandler,janaushadiyojana_EditHandler, swachhbharatmissiontoilet_EditHandler,
    soilhealthcard_EditHandler, ladlilakshmiyojana_EditHandler, jananisurakshayojana_EditHandler, kisancreditcard_EditHandler;
    ProgressDialog progressDialog;
    ChoiceApplication globalVar;
    Button govtSchemes_btn_submit_handler;

    String ubaid, dhanyojanaValue,ujjwalayojanaValue,awasyojanaValue,sukanyasamridhiyojanaValue,mudrayojanaValue,jivanjyotibimayojanaValue,
            surakshabimayojanaValue,atalpensionyojanaValue,fasalbimayojanaValue,kaushalvikasyojanaValue,krishisinchaiyojanaValue,janaushadiyojanaValue, swachhbharatmissiontoiletValue,
            soilhealthcardValue, ladlilakshmiyojanaValue, jananisurakshayojanaValue, kisancreditcardValue;

    // Storing server url into String variable.
    String HttpInsertUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateformfive.php";
   // String HttpSelectUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubagetformone.php";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_govt_schemes);
        HttpInsertUrl =getString(R.string.url)+"ubaupdateformfive.php";
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .govtcoordinatelayout);
        progressDialog = new ProgressDialog(GovtSchemesActivity.this);
        globalVar=(ChoiceApplication)getApplicationContext();
        ubaid=globalVar.getUbaid();
        dhanyojana_EditHandler=(EditText)findViewById(R.id.dhanyojana);
        ujjwalayojana_EditHandler=(EditText)findViewById(R.id.ujjwalayojana);
        sukanyasamridhiyojana_EditHandler=(EditText)findViewById(R.id.sukanyasamridhiyoja);
        awasyojana_EditHandler=(EditText)findViewById(R.id.awasyojana);
        mudrayojana_EditHandler=(EditText)findViewById(R.id.mudrayojana);
        jivanjyotibimayojana_EditHandler=(EditText)findViewById(R.id.jivanjyotibimayojana);
        surakshabimayojana_EditHandler=(EditText)findViewById(R.id.surakshabimayojana);
        atalpensionyojana_EditHandler=(EditText)findViewById(R.id.atalpensionyojana);
        fasalbimayojana_EditHandler=(EditText)findViewById(R.id.fasalbimayojana);
        kaushalvikasyojana_EditHandler=(EditText)findViewById(R.id.kaushalvikasyojana);
        krishisinchaiyojana_EditHandler =(EditText)findViewById(R.id.krishisinchaiyojana);
        janaushadiyojana_EditHandler=(EditText)findViewById(R.id.janaushadiyojana);
        swachhbharatmissiontoilet_EditHandler=(EditText)findViewById(R.id.swachhbharatmissiontoilet);
        soilhealthcard_EditHandler=(EditText)findViewById(R.id.soilhealthcard);
        ladlilakshmiyojana_EditHandler=(EditText)findViewById(R.id.ladlilakshmiyojana);
        jananisurakshayojana_EditHandler=(EditText)findViewById(R.id.jananisurakshayojana);
        kisancreditcard_EditHandler=(EditText)findViewById(R.id.kisancreditcard);


        govtSchemes_btn_submit_handler = findViewById(R.id.govtscheme_btn_submit);
        if(globalVar.getMenu()==1) {
            setValuetoForm(globalVar.getJsonString());
            govtSchemes_btn_submit_handler.setText("Update");
        }

        govtSchemes_btn_submit_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( getValueFromForm()) {

                    insertToDB(HttpInsertUrl);
                    ;

                }
                else
                {
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .playOn(findViewById(R.id.govtscheme_btn_submit));
                }


                //Intent i = new Intent(HouseholdActivity.this, FamilyMembersInfo.class);
                // Starts TargetActivity
                // startActivity(i);
            }
        });


    }

    private boolean getValueFromForm() {

// EditText dhanyojana_EditHandler,ujjwalayojana_EditHandler,awasyojana_EditHandler,sukanyasamridhiyojana_EditHandler,mudrayojana_EditHandler,jivanjyotibimayojana_EditHandler;
//String ubaid, dhanyojanaValue,ujjwalayojanaValue,awasyojanaValue,sukanyasamridhiyojanaValue,mudrayojanaValue,jivanjyotibimayojanaValue;


        dhanyojanaValue = String.valueOf(dhanyojana_EditHandler.getText());
        ujjwalayojanaValue = String.valueOf(ujjwalayojana_EditHandler.getText());
        awasyojanaValue = String.valueOf(awasyojana_EditHandler.getText());
        sukanyasamridhiyojanaValue = String.valueOf(sukanyasamridhiyojana_EditHandler.getText());
        mudrayojanaValue = String.valueOf(mudrayojana_EditHandler.getText());
        jivanjyotibimayojanaValue = String.valueOf(jivanjyotibimayojana_EditHandler.getText());
        surakshabimayojanaValue=String.valueOf(surakshabimayojana_EditHandler.getText());
        fasalbimayojanaValue=String.valueOf(atalpensionyojana_EditHandler.getText());
        kaushalvikasyojanaValue=String.valueOf(fasalbimayojana_EditHandler.getText());
        atalpensionyojanaValue = String.valueOf(atalpensionyojana_EditHandler.getText());

        krishisinchaiyojanaValue = String.valueOf(krishisinchaiyojana_EditHandler.getText());
        janaushadiyojanaValue = String.valueOf(janaushadiyojana_EditHandler.getText());
        swachhbharatmissiontoiletValue = String.valueOf(swachhbharatmissiontoilet_EditHandler.getText());
        soilhealthcardValue=String.valueOf(soilhealthcard_EditHandler.getText());
        ladlilakshmiyojanaValue=String.valueOf(ladlilakshmiyojana_EditHandler.getText());
        jananisurakshayojanaValue=String.valueOf(jananisurakshayojana_EditHandler.getText());
        kisancreditcardValue = String.valueOf(kisancreditcard_EditHandler.getText());

        if(dhanyojanaValue.length()==0)
            dhanyojana_EditHandler.setError("Cannot be empty");
        if(ujjwalayojanaValue.length()==0)
            ujjwalayojana_EditHandler.setError("Cannot be empty");
        if(awasyojanaValue.length()==0)
            awasyojana_EditHandler.setError("Cannot be empty");
        if(sukanyasamridhiyojanaValue.length()==0)
            sukanyasamridhiyojana_EditHandler.setError("Cannot be empty");
        if(mudrayojanaValue.length()==0)
            mudrayojana_EditHandler.setError("Cannot be empty");
        if(jivanjyotibimayojanaValue.length()==0)
            jivanjyotibimayojana_EditHandler.setError("Cannot be empty");
        if(surakshabimayojanaValue.length()==0)
            surakshabimayojana_EditHandler.setError("Cannot be empty");
        if(atalpensionyojanaValue.length()==0)
            atalpensionyojana_EditHandler.setError("Cannot be empty");
        if(fasalbimayojanaValue.length()==0)
            fasalbimayojana_EditHandler.setError("Cannot be empty");
        if(kaushalvikasyojanaValue.length()==0)
            kaushalvikasyojana_EditHandler.setError("Cannot be empty");

        if(krishisinchaiyojanaValue.length()==0)
            krishisinchaiyojana_EditHandler.setError("Cannot be empty");
        if(janaushadiyojanaValue.length()==0)
            janaushadiyojana_EditHandler.setError("Cannot be empty");
        if(swachhbharatmissiontoiletValue.length()==0)
            swachhbharatmissiontoilet_EditHandler.setError("Cannot be empty");
        if(soilhealthcardValue.length()==0)
            soilhealthcard_EditHandler.setError("Cannot be empty");
        if(ladlilakshmiyojanaValue.length()==0)
            ladlilakshmiyojana_EditHandler.setError("Cannot be empty");
        if(jananisurakshayojanaValue.length()==0)
            jananisurakshayojana_EditHandler.setError("Cannot be empty");
        if(kisancreditcardValue.length()==0)
            kisancreditcard_EditHandler.setError("Cannot be empty");


        if(dhanyojanaValue.length()==0||ujjwalayojanaValue.length()==0||awasyojanaValue.length()==0||sukanyasamridhiyojanaValue.length()==0||mudrayojanaValue.length()==0
                ||jivanjyotibimayojanaValue.length()==0 ||surakshabimayojanaValue.length()==0||fasalbimayojanaValue.length()==0||kaushalvikasyojanaValue.length()==0||atalpensionyojanaValue.length()==0||
        krishisinchaiyojanaValue.length()==0|| janaushadiyojanaValue.length()==0|| swachhbharatmissiontoiletValue.length()==0||
        soilhealthcardValue.length()==0|| ladlilakshmiyojanaValue.length()==0|| jananisurakshayojanaValue.length()==0|| kisancreditcardValue.length()==0)

        {
            return false;
        }
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
                                 Intent i = new Intent(GovtSchemesActivity.this, WaterSourceActivity.class);

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
                params.put("dhanyojana",dhanyojanaValue);
                params.put("ujjwalayojana", ujjwalayojanaValue);
                params.put("awasyojana", awasyojanaValue);
                params.put("sukanyasamridhiyojana", sukanyasamridhiyojanaValue);
                params.put("mudrayojana", mudrayojanaValue);
                params.put("jivanjyotibimayojana", jivanjyotibimayojanaValue);
                params.put("surakshabimayojana",surakshabimayojanaValue);
                params.put("atalpensionyojana", atalpensionyojanaValue);
                params.put("fasalbimayojana",fasalbimayojanaValue);
                params.put("kaushalvikasyojana",kaushalvikasyojanaValue);

                params.put("krishisinchaiyojana", krishisinchaiyojanaValue);
                params.put("janaushadiyojana", janaushadiyojanaValue);
                params.put("swachhbharatmissiontoilet", swachhbharatmissiontoiletValue);
                params.put("soilhealthcard",soilhealthcardValue);
                params.put("ladlilakshmiyojana", ladlilakshmiyojanaValue);
                params.put("jananisurakshayojana",janaushadiyojanaValue);
                params.put("kisancreditcard",kisancreditcardValue);

                return params;
            }

        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(GovtSchemesActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

    public void setValuetoForm(String jsonString){

        try {
            JSONObject jobj = new JSONObject(jsonString);
            dhanyojanaValue=jobj.getString("dhanyojana");
            ujjwalayojanaValue = jobj.getString("ujjwalayojana");
            awasyojanaValue = jobj.getString("awasyojana");
            sukanyasamridhiyojanaValue = jobj.getString("sukanyasamridhiyojana");
            mudrayojanaValue = jobj.getString("mudrayojana");
            jivanjyotibimayojanaValue = jobj.getString("jivanjyotibimayojana");
            surakshabimayojanaValue=jobj.getString("surakshabimayojana");
            atalpensionyojanaValue=jobj.getString("atalpensionyojana");
            fasalbimayojanaValue=jobj.getString("fasalbimayojana");
            kaushalvikasyojanaValue=jobj.getString("kaushalvikasyojana");
            krishisinchaiyojanaValue = jobj.getString("krishisinchaiyojana");
            janaushadiyojanaValue = jobj.getString("janaushadiyojana");
            swachhbharatmissiontoiletValue =jobj.getString("swachhbharatmissiontoilet");
            soilhealthcardValue=jobj.getString("soilhealthcard");
            ladlilakshmiyojanaValue=jobj.getString("ladlilakshmiyojana");
            jananisurakshayojanaValue=jobj.getString("jananisurakshayojana");
            kisancreditcardValue =jobj.getString("kisancreditcard");

        } catch (JSONException e) {
            e.printStackTrace();
        }


            dhanyojana_EditHandler.setText(dhanyojanaValue);

            ujjwalayojana_EditHandler.setText(ujjwalayojanaValue);

            awasyojana_EditHandler.setText(awasyojanaValue);

            sukanyasamridhiyojana_EditHandler.setText(sukanyasamridhiyojanaValue);


            mudrayojana_EditHandler.setText(mudrayojanaValue);

            jivanjyotibimayojana_EditHandler.setText(jivanjyotibimayojanaValue);

            surakshabimayojana_EditHandler.setText(surakshabimayojanaValue);

            atalpensionyojana_EditHandler.setText(atalpensionyojanaValue);


            fasalbimayojana_EditHandler.setText(fasalbimayojanaValue);
            kaushalvikasyojana_EditHandler.setText(kaushalvikasyojanaValue);

            krishisinchaiyojana_EditHandler.setText(krishisinchaiyojanaValue);

            janaushadiyojana_EditHandler.setText(janaushadiyojanaValue);

            swachhbharatmissiontoilet_EditHandler.setText(swachhbharatmissiontoiletValue);

            soilhealthcard_EditHandler.setText(soilhealthcardValue);

            ladlilakshmiyojana_EditHandler.setText(ladlilakshmiyojanaValue);

            jananisurakshayojana_EditHandler.setText(jananisurakshayojanaValue);

            kisancreditcard_EditHandler.setText(kisancreditcardValue);

    }


}
