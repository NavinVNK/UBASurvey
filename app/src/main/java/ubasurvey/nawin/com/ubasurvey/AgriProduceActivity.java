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

public class AgriProduceActivity extends AppCompatActivity {

    EditText crop1_EditHandler,crop1area_EditHandler,crop1produce_EditHandler,crop2_EditHandler,crop2area_EditHandler,crop2produce_EditHandler,
            crop3_EditHandler,crop3area_EditHandler,crop3produce_EditHandler,crop4_EditHandler,crop4area_EditHandler,crop4produce_EditHandler, crop5_EditHandler,
            crop5area_EditHandler, crop5produce_EditHandler;
    ProgressDialog progressDialog;
    ChoiceApplication globalVar;
    Button govtSchemes_btn_submit_handler;

    String ubaid, crop1Value,crop1areaValue,crop1produceValue,crop2Value,crop2areaValue,crop2produceValue,
            crop3Value,crop3areaValue,crop3produceValue,crop4Value,crop4areaValue,crop4produceValue, crop5Value,
            crop5areaValue, crop5produceValue;

    // Storing server url into String variable.
    String HttpInsertUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateformten.php";
    String HttpSelectUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubagetformone.php";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agri_produce);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .agriprodcoordinatelayout);
        progressDialog = new ProgressDialog(AgriProduceActivity.this);
        globalVar=(ChoiceApplication)getApplicationContext();
        ubaid=globalVar.getUbaid();
        crop1_EditHandler=(EditText)findViewById(R.id.crop1);
        crop1area_EditHandler=(EditText)findViewById(R.id.crop1area);
        crop2_EditHandler=(EditText)findViewById(R.id.crop2);
        crop1produce_EditHandler=(EditText)findViewById(R.id.crop1produce);
        crop2area_EditHandler=(EditText)findViewById(R.id.crop2area);
        crop2produce_EditHandler=(EditText)findViewById(R.id.crop2produce);
        crop3_EditHandler=(EditText)findViewById(R.id.crop3);
        crop3area_EditHandler=(EditText)findViewById(R.id.crop3area);
        crop3produce_EditHandler=(EditText)findViewById(R.id.crop3produce);
        crop4_EditHandler=(EditText)findViewById(R.id.crop4);
        crop4area_EditHandler =(EditText)findViewById(R.id.crop4area);
        crop4produce_EditHandler=(EditText)findViewById(R.id.crop4produce);
        crop5_EditHandler=(EditText)findViewById(R.id.crop5);
        crop5area_EditHandler=(EditText)findViewById(R.id.crop5area);
        crop5produce_EditHandler=(EditText)findViewById(R.id.crop5produce);



        govtSchemes_btn_submit_handler = findViewById(R.id.agriproduce_btn_submit);
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
                            .playOn(findViewById(R.id.agriproduce_btn_submit));
                }


                //Intent i = new Intent(HouseholdActivity.this, FamilyMembersInfo.class);
                // Starts TargetActivity
                // startActivity(i);
            }
        });


    }

    private boolean getValueFromForm() {

// EditText crop1_EditHandler,crop1area_EditHandler,crop1produce_EditHandler,crop2_EditHandler,crop2area_EditHandler,crop2produce_EditHandler;
//String ubaid, crop1Value,crop1areaValue,crop1produceValue,crop2Value,crop2areaValue,crop2produceValue;


        crop1Value = String.valueOf(crop1_EditHandler.getText());
        crop1areaValue = String.valueOf(crop1area_EditHandler.getText());
        crop1produceValue = String.valueOf(crop1produce_EditHandler.getText());
        crop2Value = String.valueOf(crop2_EditHandler.getText());
        crop2areaValue = String.valueOf(crop2area_EditHandler.getText());
        crop2produceValue = String.valueOf(crop2produce_EditHandler.getText());
        crop3Value=String.valueOf(crop3_EditHandler.getText());
        crop3produceValue=String.valueOf(crop3area_EditHandler.getText());
        crop4Value=String.valueOf(crop3produce_EditHandler.getText());
        crop3areaValue = String.valueOf(crop3area_EditHandler.getText());

        crop4areaValue = String.valueOf(crop4area_EditHandler.getText());
        crop4produceValue = String.valueOf(crop4produce_EditHandler.getText());
        crop5Value = String.valueOf(crop5_EditHandler.getText());
        crop5areaValue=String.valueOf(crop5area_EditHandler.getText());
        crop5produceValue=String.valueOf(crop5produce_EditHandler.getText());


        if(crop1Value.length()==0)
            crop1_EditHandler.setError("Cannot be empty");
        if(crop1areaValue.length()==0)
            crop1area_EditHandler.setError("Cannot be empty");
        if(crop1produceValue.length()==0)
            crop1produce_EditHandler.setError("Cannot be empty");
        if(crop2Value.length()==0)
            crop2_EditHandler.setError("Cannot be empty");
        if(crop2areaValue.length()==0)
            crop2area_EditHandler.setError("Cannot be empty");
        if(crop2produceValue.length()==0)
            crop2produce_EditHandler.setError("Cannot be empty");
        if(crop3Value.length()==0)
            crop3_EditHandler.setError("Cannot be empty");
        if(crop3areaValue.length()==0)
            crop3area_EditHandler.setError("Cannot be empty");
        if(crop3produceValue.length()==0)
            crop3produce_EditHandler.setError("Cannot be empty");
        if(crop4Value.length()==0)
            crop4_EditHandler.setError("Cannot be empty");

        if(crop4areaValue.length()==0)
            crop4area_EditHandler.setError("Cannot be empty");
        if(crop4produceValue.length()==0)
            crop4produce_EditHandler.setError("Cannot be empty");
        if(crop5Value.length()==0)
            crop5_EditHandler.setError("Cannot be empty");
        if(crop5areaValue.length()==0)
            crop5area_EditHandler.setError("Cannot be empty");
        if(crop5produceValue.length()==0)
            crop5produce_EditHandler.setError("Cannot be empty");



        if(crop1Value.length()==0||crop1areaValue.length()==0||crop1produceValue.length()==0||crop2Value.length()==0||crop2areaValue.length()==0
                ||crop2produceValue.length()==0 ||crop3Value.length()==0||crop3produceValue.length()==0||crop4Value.length()==0||crop3areaValue.length()==0||
                crop4areaValue.length()==0|| crop4produceValue.length()==0|| crop5Value.length()==0||
                crop5areaValue.length()==0|| crop5produceValue.length()==0)

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
                                Intent i = new Intent(AgriProduceActivity.this, LiveStockActivity.class);

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
                params.put("crop1",crop1Value);
                params.put("crop1area", crop1areaValue);
                params.put("crop1produce", crop1produceValue);
                params.put("crop2", crop2Value);
                params.put("crop2area", crop2areaValue);
                params.put("crop2produce", crop2produceValue);
                params.put("crop3",crop3Value);
                params.put("crop3area", crop3areaValue);
                params.put("crop3produce",crop3produceValue);
                params.put("crop4",crop4Value);

                params.put("crop4area", crop4areaValue);
                params.put("crop4produce", crop4produceValue);
                params.put("crop5", crop5Value);
                params.put("crop5area",crop5areaValue);
                params.put("crop5produce", crop5produceValue);



                return params;
            }

        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(AgriProduceActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
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
                        setValuetoForm(ServerResponse);
/*                        Toast toast = Toast.makeText(getApplicationContext(),
                                ServerResponse,
                                Toast.LENGTH_LONG);

                        toast.show();*/
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
        RequestQueue requestQueue = Volley.newRequestQueue(AgriProduceActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
    public void setValuetoForm(String jsonString){

        try {
            JSONObject jobj = new JSONObject(jsonString);
            crop1Value=jobj.getString("crop1");
            crop1areaValue = jobj.getString("crop1area");
            crop1produceValue = jobj.getString("crop1produce");
            crop2Value = jobj.getString("crop2");
            crop2areaValue = jobj.getString("crop2area");
            crop2produceValue = jobj.getString("crop2produce");
            crop3Value=jobj.getString("crop3");
            crop3areaValue=jobj.getString("crop3area");
            crop3produceValue=jobj.getString("crop3produce");
            crop4Value=jobj.getString("crop4");
            crop4areaValue = jobj.getString("crop4area");
            crop4produceValue = jobj.getString("crop4produce");
            crop5Value =jobj.getString("crop5");
            crop5areaValue=jobj.getString("crop5area");
            crop5produceValue=jobj.getString("crop5produce");


        } catch (JSONException e) {
            e.printStackTrace();
        }


            crop1_EditHandler.setText(crop1Value);


            crop1area_EditHandler.setText(crop1areaValue);


            crop1produce_EditHandler.setText(crop1produceValue);

            crop2_EditHandler.setText(crop2Value);


            crop2area_EditHandler.setText(crop2areaValue);

            crop2produce_EditHandler.setText(crop2produceValue);


            crop3_EditHandler.setText(crop3Value);


            crop3area_EditHandler.setText(crop3areaValue);


            crop3produce_EditHandler.setText(crop3produceValue);

            crop4area_EditHandler.setText(crop4areaValue);

            crop4produce_EditHandler.setText(crop4produceValue);

            crop5_EditHandler.setText(crop5Value);

            crop5area_EditHandler.setText(crop5areaValue);

            crop5produce_EditHandler.setText(crop5produceValue);


    }


}
