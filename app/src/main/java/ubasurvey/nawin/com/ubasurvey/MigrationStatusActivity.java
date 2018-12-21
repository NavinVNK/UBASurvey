package ubasurvey.nawin.com.ubasurvey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class MigrationStatusActivity extends AppCompatActivity {
    // Creating Progress dialog.
    ProgressDialog progressDialog;
    ChoiceApplication globalVar;

LinearLayout layoutFamilymigration,layoutDaymonth,layoutYears;
    Button migration_btn_submit_handler;
    EditText familymigratednos_Handler,month_Handler,years_Handler;
    Spinner migrationstatusSpinnerHandler;
    String ubaid,migrationstatusValue,familymigratednosValue,monthValue,daysmonthValue,yearsValue;

    // Storing server url into String variable.
    String HttpInsertUrl;// = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateformfour.php";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_migration_status);
        HttpInsertUrl =getString(R.string.url)+"ubaupdateformfour.php";
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .migrationcoordinatorLayout);
        globalVar=(ChoiceApplication)getApplicationContext();
        ubaid=globalVar.getUbaid();
        progressDialog = new ProgressDialog(MigrationStatusActivity.this);
        layoutFamilymigration=(LinearLayout)findViewById(R.id.layoutfamilymigrated);
        layoutFamilymigration.setVisibility(View.GONE);
        layoutDaymonth=(LinearLayout)findViewById(R.id.layoutdaysmonths);
        layoutDaymonth.setVisibility(View.GONE);
        layoutYears=(LinearLayout)findViewById(R.id.layoutyears);
        layoutYears.setVisibility(View.GONE);
        familymigratednos_Handler = (EditText)findViewById(R.id.mirgatednumber_edittext);
        month_Handler = (EditText)findViewById(R.id.mirgatedmonths_edittext);
        years_Handler=(EditText)findViewById(R.id.mirgatedyears_edittext);
        migration_btn_submit_handler = (Button)findViewById(R.id.mirgrate_btn_submit);

        migration_btn_submit_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( getValueFromForm()) {

                    insertToDB(HttpInsertUrl);
                }
                else
                {
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .playOn(findViewById(R.id.mirgrate_btn_submit));
                }


            }
        });

        migrationstatusSpinnerHandler = (Spinner)findViewById(R.id.migrationstatus_spinner);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this, R.array.migrationstatus_option,
                android.R.layout.simple_spinner_item);
        migrationstatusSpinnerHandler.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        migrationstatusSpinnerHandler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1) {
                    layoutFamilymigration.setVisibility(View.VISIBLE);
                    layoutDaymonth.setVisibility(View.VISIBLE);
                    layoutYears.setVisibility(View.VISIBLE);
                }
                else
                {
                    layoutFamilymigration.setVisibility(View.GONE);
                    layoutDaymonth.setVisibility(View.GONE);
                    layoutYears.setVisibility(View.GONE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(globalVar.getMenu()>0) {
            /*//selectDatafromDB(globalVar.getUbaid());
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Basic info"+globalVar.getJsonString(),
                    Toast.LENGTH_LONG);

            toast.show();*/
            setValuetoForm(globalVar.getJsonString());
            migration_btn_submit_handler.setText("Update");
        }
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
                                Intent i = new Intent(MigrationStatusActivity.this, GovtSchemesActivity.class);
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
                params.put("migrateforwork",migrationstatusValue);
                params.put("familymigratednos", familymigratednosValue);
                params.put("daysmonth", daysmonthValue);
                params.put("yearsofmigration", yearsValue);



                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(MigrationStatusActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
    // Creating method to get value from EditText.
    public boolean getValueFromForm(){

        migrationstatusValue = migrationstatusSpinnerHandler.getSelectedItem().toString();
        if(migrationstatusValue.compareTo("Yes")==0) {
            familymigratednosValue = String.valueOf(familymigratednos_Handler.getText());
            daysmonthValue = String.valueOf(month_Handler.getText());
            yearsValue = String.valueOf(years_Handler.getText());

        }
        else
        {
            familymigratednosValue = "NA";
            yearsValue = "NA";
            daysmonthValue="NA";
        }



        if(migrationstatusValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)migrationstatusSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(migrationstatusValue.compareTo("Select Value")==0)
            return false;
        else if(migrationstatusValue.compareTo("Yes")==0&& (daysmonthValue.compareTo("")==0)|| yearsValue.compareTo("")==0) {

            if(familymigratednosValue.length()==0){
                familymigratednos_Handler.setError("Cannot be empty");
            }
            if(daysmonthValue.length()==0){
                month_Handler.setError("Cannot be empty");
            }
            if(yearsValue.length()==0){
                years_Handler.setError("Cannot be empty");
            }
            return false;
        }
        else
            return  true;

    }
    public void setValuetoForm(String jsonString){

        try {
            JSONObject jobj = new JSONObject(jsonString);
            migrationstatusValue=jobj.getString("migrateforwork");
            if(migrationstatusValue.compareTo("empty")==0) {
                  migrationstatusValue = "Select Value";
            }
            else if(migrationstatusValue.compareTo("Yes")==0)
            {
                familymigratednosValue = jobj.getString("familymigratednos");
                daysmonthValue=jobj.getString("daysmonth");

                yearsValue = jobj.getString("yearsofmigration");

            }
          else
            {
                familymigratednosValue = "NA";
                daysmonthValue="NA";

                yearsValue = "NA";

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        migrationstatusSpinnerHandler.setSelection(setSpinnerPos(migrationstatusSpinnerHandler,migrationstatusValue));
        if(migrationstatusValue.compareTo("Yes")==0) {
            familymigratednos_Handler.setText(familymigratednosValue);
            month_Handler.setText(daysmonthValue);
            years_Handler.setText(yearsValue);
        }


    }
    int  setSpinnerPos(Spinner spinner,String value)
    {


        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        return myAdap.getPosition(value);

    }
}
