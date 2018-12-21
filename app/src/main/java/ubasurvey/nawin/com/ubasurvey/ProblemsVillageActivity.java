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

public class ProblemsVillageActivity extends AppCompatActivity {
    // Creating Progress dialog.

    EditText problem1_Handler,problem2_Handler,problem3_Handler,solution1_Handler,solution2_Handler,solution3_Handler;
    ProgressDialog progressDialog;
    ChoiceApplication globalVar;
    Button problem_btn_submit_handler;
    String ubaid, problem1Value,problem2Value,problem3Value,solution1Value,solution2Value,solution3Value;

    // Storing server url into String variable.
    String HttpInsertUrl;// = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateformthirteen.php";
    //String HttpSelectUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubagetformone.php";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems_village);
        HttpInsertUrl=getString(R.string.url)+"ubaupdateformthirteen.php";
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .majorprobcoordinatelayout);
        progressDialog = new ProgressDialog(ProblemsVillageActivity.this);
        globalVar=(ChoiceApplication)getApplicationContext();
        ubaid=globalVar.getUbaid();
        problem1_Handler=(EditText)findViewById(R.id.problem1);
        problem2_Handler=(EditText)findViewById(R.id.problem2);
        problem3_Handler=(EditText)findViewById(R.id.problem3);
        solution1_Handler=(EditText)findViewById(R.id.solution1);
        solution2_Handler=(EditText)findViewById(R.id.solution2);
        solution3_Handler=(EditText)findViewById(R.id.solution3);
        problem_btn_submit_handler = findViewById(R.id.problems_btn_submit);
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

        if(!(problem1_Handler.getText().equals("")))
            problem1Value = String.valueOf(problem1_Handler.getText());
        else
            problem1Value ="None";

        if(!(problem2_Handler.getText().equals("")))
            problem2Value = String.valueOf(problem2_Handler.getText());
        else
            problem2Value ="None";
        if(!(problem3_Handler.getText().equals("")))
            problem3Value = String.valueOf(problem3_Handler.getText());
        else
            problem3Value ="None";
        if(!(solution1_Handler.getText().equals("")))
            solution1Value = String.valueOf(solution1_Handler.getText());
        else
            solution1Value ="None";
        if(!(solution2_Handler.getText().equals("")))
            solution2Value = String.valueOf(solution2_Handler.getText());
        else
            solution2Value ="None";
        if(!(solution3_Handler.getText().equals("")))
            solution3Value = String.valueOf(solution3_Handler.getText());
        else
            solution3Value ="None";

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
                        if(ServerResponse.compareTo("0")==0)
                        {

                        }

                        else
                        {
                            if(globalVar.getMenu()==0)
                            {
                                 Intent i = new Intent(ProblemsVillageActivity.this, GeneralInfoActivity.class);
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
                params.put("problem1",problem1Value);
                params.put("solution1", solution1Value);
                params.put("problem2", problem2Value);
                params.put("solution2", solution2Value);
                params.put("problem3", problem3Value);
                params.put("solution3", solution3Value);
                return params;
            }

        };
        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(ProblemsVillageActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

    public void setValuetoForm(String jsonString){

        try {
            JSONObject jobj = new JSONObject(jsonString);
            problem1Value=jobj.getString("problem1");
            solution1Value = jobj.getString("solution1");
            problem2Value = jobj.getString("problem2");
            solution2Value = jobj.getString("solution2");
            problem3Value = jobj.getString("problem3");
            solution3Value = jobj.getString("solution3");


        } catch (JSONException e) {
            e.printStackTrace();
        }


            problem1_Handler.setText(problem1Value);

            solution1_Handler.setText(solution1Value);

            problem2_Handler.setText(problem2Value);

            solution2_Handler.setText(solution2Value);

            problem3_Handler.setText(problem3Value);

            solution3_Handler.setText(solution3Value);

    }

}
