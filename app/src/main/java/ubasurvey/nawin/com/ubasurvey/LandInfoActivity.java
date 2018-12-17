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

public class LandInfoActivity extends AppCompatActivity {

    // Creating Progress dialog.

    EditText totalLand_Handler,cultivable_Handler,irregated_Handler,unirregated_Handler,barrenwaste_Handler,uncultivable_Handler;
    ProgressDialog progressDialog;
    ChoiceApplication globalVar;
    Button problem_btn_submit_handler;
    String ubaid, totalLandValue,cultivableValue,irregatedValue,unirregatedValue,barrenwasteValue,uncultivableValue;

    // Storing server url into String variable.
    String HttpInsertUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateformeight.php";
    String HttpSelectUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubagetformone.php";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_info);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .landcoordinatelayout);
        progressDialog = new ProgressDialog(LandInfoActivity.this);
        globalVar=(ChoiceApplication)getApplicationContext();
        ubaid=globalVar.getUbaid();
        totalLand_Handler=(EditText)findViewById(R.id.totalland);
        cultivable_Handler=(EditText)findViewById(R.id.cultivable);
        irregated_Handler=(EditText)findViewById(R.id.irrigated);
        unirregated_Handler=(EditText)findViewById(R.id.unirregated );
        barrenwaste_Handler=(EditText)findViewById(R.id.barren);
        uncultivable_Handler=(EditText)findViewById(R.id.uncultivable);
        problem_btn_submit_handler = findViewById(R.id.landinfo_btn_submit);
        if(globalVar.getMenu()==1) {
            setValuetoForm(globalVar.getJsonString());
            problem_btn_submit_handler.setText("Update");
        }

        problem_btn_submit_handler.setOnClickListener(new View.OnClickListener() {
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
                            .playOn(findViewById(R.id.problems_btn_submit));
                }


                //Intent i = new Intent(HouseholdActivity.this, FamilyMembersInfo.class);
                // Starts TargetActivity
                // startActivity(i);
            }
        });


    }

    private boolean getValueFromForm() {

// EditText total_Cows_Handler,total_Buffalo_Handler,total_Goat_Sheep_Handler,total_Calves_Handler,total_Bullock_Handler,total_Poultry_Ducks_Handler;
//String ubaid, cowsValue,buffalosValue,goats_sheep_Value,calvesValue,bullocksValue,poultry_ducks_Value;

        if(!(totalLand_Handler.getText().equals("")))
            totalLandValue = String.valueOf(totalLand_Handler.getText());
        else
            totalLandValue ="NA";

        if(!(cultivable_Handler.getText().equals("")))
            cultivableValue = String.valueOf(cultivable_Handler.getText());
        else
            cultivableValue ="NA";
        if(!(irregated_Handler.getText().equals("")))
            irregatedValue = String.valueOf(irregated_Handler.getText());
        else
            irregatedValue ="NA";
        if(!(unirregated_Handler.getText().equals("")))
            unirregatedValue = String.valueOf(unirregated_Handler.getText());
        else
            unirregatedValue ="NA";
        if(!(barrenwaste_Handler.getText().equals("")))
            barrenwasteValue = String.valueOf(barrenwaste_Handler.getText());
        else
            barrenwasteValue ="NA";
        if(!(uncultivable_Handler.getText().equals("")))
            uncultivableValue = String.valueOf(uncultivable_Handler.getText());
        else
            uncultivableValue ="NA";

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
                        if(ServerResponse.compareTo("0")==0)
                        {
                            // selectDatafromDB(ubaid);
                        }

                        else
                        {
                            if(globalVar.getMenu()==0)
                            {
                                 Intent i = new Intent(LandInfoActivity.this, AgriculturalInputsActivity.class);
                                // Starts TargetActivity
                                 startActivity(i);

                            }
                            else
                                globalVar.setJsonString(ServerResponse);

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
                        Toast.makeText(LandInfoActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("ubaid", ubaid);
                params.put("totalland",totalLandValue);
                params.put("cultivable", cultivableValue);
                params.put("irrigated", irregatedValue);
                params.put("unirregated", unirregatedValue);
                params.put("barrenwaste", barrenwasteValue);
                params.put("uncultivable", uncultivableValue);
                return params;
            }

        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(LandInfoActivity.this);
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
                        Toast toast = Toast.makeText(getApplicationContext(),
                                ServerResponse,
                                Toast.LENGTH_LONG);

                        toast.show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(LandInfoActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
    public void setValuetoForm(String jsonString){

        try {
            JSONObject jobj = new JSONObject(jsonString);
            totalLandValue=jobj.getString("totalland");
            unirregatedValue = jobj.getString("cultivable");
            cultivableValue = jobj.getString("irrigated");
            barrenwasteValue = jobj.getString("unirregated");
            irregatedValue = jobj.getString("barrenwaste");
            uncultivableValue = jobj.getString("uncultivable");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalLandValue.compareTo("NA")==0||totalLandValue.compareTo("")==0)
        {
            totalLand_Handler.setText("");
        }
        else
            totalLand_Handler.setText(totalLandValue);

        if(unirregatedValue.compareTo("NA")==0||unirregatedValue.compareTo("")==0)
        {
            unirregated_Handler.setText("");
        }
        else
            unirregated_Handler.setText(unirregatedValue);


        if(cultivableValue.compareTo("NA")==0||cultivableValue.compareTo("")==0)
        {
            cultivable_Handler.setText("");
        }
        else
            cultivable_Handler.setText(cultivableValue);

        if(barrenwasteValue.compareTo("NA")==0||barrenwasteValue.compareTo("")==0)
        {
            barrenwaste_Handler.setText("");
        }
        else
            barrenwaste_Handler.setText(barrenwasteValue);

        if(irregatedValue.compareTo("NA")==0||irregatedValue.compareTo("")==0)
        {
            irregated_Handler.setText("");
        }
        else
            irregated_Handler.setText(irregatedValue);

        if(uncultivableValue.compareTo("NA")==0||uncultivableValue.compareTo("")==0)
        {
            uncultivable_Handler.setText("");
        }
        else
            uncultivable_Handler.setText(uncultivableValue);

    }
    int  setSpinnerPos(Spinner spinner, String value)
    {
        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        return myAdap.getPosition(value);

    }
}
