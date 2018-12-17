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

public class ElectappDetailsActivity extends AppCompatActivity {

    // Creating Progress dialog.
    ProgressDialog progressDialog;
    ChoiceApplication globalVar;
    Button electricaldetails_btn_submit_handler;
    TextView headNameText;
    EditText countEdit_handler,durationEdit_handler;
    Spinner electricalappSpinnerHandler;

    String ubaid,ubaelecno,countValue,durationValue,electricalappnameValue;
            ;
    // Storing server url into String variable.
    String HttpInsertUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubainsertelecticappdetails.php";
    String HttpupdateUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateelectricalappdetails.php";
    private boolean update;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electapp_details);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .electricalappcoordinatorLayout);
        Bundle bundle = getIntent().getExtras();
        progressDialog = new ProgressDialog(ElectappDetailsActivity.this);
        globalVar=(ChoiceApplication)getApplicationContext();
        ubaid=globalVar.getUbaid();
        update=false;


        countEdit_handler = findViewById(R.id.electricalcountt);
        durationEdit_handler = (EditText)findViewById(R.id.electricalduration);
        

        electricalappSpinnerHandler =(Spinner)findViewById(R.id.electricalappspinner);
        

        electricaldetails_btn_submit_handler = findViewById(R.id.electricaldetails_btn_submit);
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            update=true;

            // int pos=extras.getInt("position");//get from bundle
            String electricaljsonValue=extras.getString("elecrecord");
            //setValuetoForm(globalVar.getFamilyjsonString(), pos);
            setValuetoForm(electricaljsonValue);
            electricaldetails_btn_submit_handler.setText("Update");
        }
//

        electricaldetails_btn_submit_handler.setOnClickListener(new View.OnClickListener() {
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
                            .playOn(findViewById(R.id.electricaldetails_btn_submit));
                }


            }
        });
    }

    private boolean getValueFrom() {

        countValue= String.valueOf(countEdit_handler.getText());
        durationValue = String.valueOf(durationEdit_handler.getText());
        electricalappnameValue = electricalappSpinnerHandler.getSelectedItem().toString();

        if(countValue.length()==0)
            countEdit_handler.setError("Cannot be empty");
        if(durationValue.length()==0)
            durationEdit_handler.setError("Cannot be empty");
        if(electricalappnameValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)electricalappSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }

        if(countValue.compareTo("")==0||durationValue.compareTo("")==0||electricalappnameValue.compareTo("Select Value")==0)
            return false;
       /* if(bankACValue.compareTo("Select Value")==0||electricalappnameValue.compareTo("Select Value")==0||maritialValue.compareTo("Select Value")==0||
                schoolValue.compareTo("Select Value")==0||aadharValue.compareTo("Select Value")==0||durationValue.compareTo("")==0
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
/*                        Toast toast = Toast.makeText(getApplicationContext(),
                                ServerResponse,
                                Toast.LENGTH_LONG);
                        toast.show();*/

                        if(ServerResponse.compareTo("0")==0) {


                        }
                        else
                        {

                            if(!update)
                            {
                                globalVar.setElectIncrement();
                                Intent data = new Intent();
                                data.putExtra(globalVar.getElectricalappCount().toString(),"value");
                                setResult(RESULT_OK, data);

                            }

                            else
                            {

                                Intent data = new Intent();
                                //data.setData(Uri.parse("hello"));
                                data.putExtra(ServerResponse,"value");
                                setResult(RESULT_OK, data);


                            }
                        }
                        if(globalVar.getMenu()==0)
                        {
                            //Intent i = new Intent(RespondentProfileActivity.this, MigrationStatusActivity.class);

                            //Starts TargetActivity
                            // startActivity(i);

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
                    ubaelecno=globalVar.getElectricalappCount().toString();
                params.put("ubaelecno",ubaelecno);
                params.put("appliances",electricalappnameValue );
                params.put("count", countValue);
                params.put("duration",durationValue );

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(ElectappDetailsActivity.this);

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
        RequestQueue requestQueue = Volley.newRequestQueue(ElectappDetailsActivity.this);

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
            ubaelecno =jobj.getString("ubaelecno").toString();
            countValue=jobj.getString("count");
            if(countValue.compareTo("no")==0)
                countValue = "";
            durationValue = jobj.getString("duration");
            if(durationValue .compareTo("0")==0)
                durationValue="";
            electricalappnameValue = jobj.getString("appliances");
            if(electricalappnameValue.compareTo("no")==0)
                electricalappnameValue= "Select Value";





        } catch (JSONException e) {
            e.printStackTrace();
        }

        electricalappSpinnerHandler.setSelection(setSpinnerPos(electricalappSpinnerHandler,electricalappnameValue));

        countEdit_handler.setText(countValue);
        durationEdit_handler.setText(durationValue);


    }
    int  setSpinnerPos(Spinner spinner,String value)
    {


        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        return myAdap.getPosition(value);

        //set the default according to value
        // spinner.setSelection(spinnerPosition);
    }

}
