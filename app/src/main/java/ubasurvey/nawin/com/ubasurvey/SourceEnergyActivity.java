package ubasurvey.nawin.com.ubasurvey;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceEnergyActivity extends AppCompatActivity {

    TextView electItems;
    private List<ElectricalappDetails> electappDetailsList = new ArrayList<>();
    private RecyclerView recyclerView;
    
    private ElectricalDetailsAdapter mAdapter;
    String electricappRecord[];
    Switch electricitySwitch_Handler;
    EditText electricityAvailEdit_Handler,electricitySourceEdit_Handler,cookingSourceEdit_Handler;
    ProgressDialog progressDialog;
    ChoiceApplication globalVar;
    Button energysource_btn_submit_handler,electricadd_button;

    MultiSelectionSpinner electricitySourceSpinnerHandler,cookingSourceSpinnerHandler;
    String ubaid, electricityAvailValue,electricitySourceValue,cookingSourceValue,otherlightingsourceValue,othercookingsourceValue;

    // Storing server url into String variable.
    String HttpSelectUrl;// = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaselectelectricalappdetails.php";
    String HttpInsertUrl;// = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateformsix.php";
    private CoordinatorLayout coordinatorLayout;
    private int request_Code = 1;


    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private SourceEnergyActivity.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final SourceEnergyActivity.ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request_Code) {
            if (resultCode == RESULT_OK) {
                // if(positionSeleced < 0) {
                prepareRecordData();
                // electItems.setText(data.getStringExtra("value"));//globalVar.getFamilyMemCount() + " Family Members Added");
               /* }
                else
                {

                    electricappRecord[positionSeleced]=data.getData().toString();


                    positionSeleced=-1;
                }*/
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_energy);
        HttpInsertUrl =getString(R.string.url)+ "ubaupdateformsix.php";
                HttpSelectUrl =getString(R.string.url)+"ubaselectelectricalappdetails.php";
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .energysourcecoordinatorLayout);
        progressDialog = new ProgressDialog(SourceEnergyActivity.this);
        globalVar=(ChoiceApplication)getApplicationContext();
        ubaid=globalVar.getUbaid();
        electricitySwitch_Handler = (Switch) findViewById(R.id.electricity_switch);
        electricitySourceSpinnerHandler=(MultiSelectionSpinner)findViewById(R.id.lightingtypeSpinner);
        List<String> lightingSourcelist = new ArrayList<String>();

        lightingSourcelist.add("Electricity");
        lightingSourcelist.add("SolarPower");
        lightingSourcelist.add("Kerosene");
        electricitySourceSpinnerHandler.setItems(lightingSourcelist);
        electricityAvailEdit_Handler = (EditText)findViewById(R.id.electricity_hours);
        electricityAvailEdit_Handler.setVisibility(View.GONE);
        electricitySwitch_Handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String statusSwitch1, statusSwitch2;
                if (electricitySwitch_Handler.isChecked()) {
                    //statusSwitch1 = pipedWaterSource_Handler.getTextOn().toString();
                    electricityAvailEdit_Handler.setVisibility(View.VISIBLE);
                }
                else
                    electricityAvailEdit_Handler.setVisibility(View.GONE);
                //else
                // statusSwitch1 = pipedWaterSource_Handler.getTextOff().toString();
                //Toast.makeText(getApplicationContext(), "Switch1 :" + statusSwitch1 + "\n", Toast.LENGTH_LONG).show(); // display the current state for switch's
            }
        });
     electricitySourceEdit_Handler= (EditText)findViewById(R.id.otherlightingsource);
        cookingSourceSpinnerHandler=(MultiSelectionSpinner)findViewById(R.id.cookingtypeSpinner);
        List<String> cookingSourcelist = new ArrayList<String>();

        cookingSourcelist.add("LPG");
        cookingSourcelist.add("Biogas");
        cookingSourcelist.add("Kerosene");
        cookingSourcelist.add("Wood");
        cookingSourcelist.add("Cowd_Dung");
        cookingSourcelist.add("Agro_Residues");
        cookingSourcelist.add("Electricity");
        cookingSourceSpinnerHandler.setItems(cookingSourcelist);


        cookingSourceEdit_Handler = (EditText) findViewById(R.id.othercookingsource);
        energysource_btn_submit_handler = (Button)findViewById(R.id.energysource_btn_submit);

        electItems=(TextView)findViewById(R.id.electapp_Textview) ;
        electricadd_button=(Button)findViewById(R.id.elecadd_btn);
        electricadd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SourceEnergyActivity.this, ElectappDetailsActivity.class);
                startActivityForResult(i,request_Code);

            }
        });
        if(globalVar.getMenu()==1) {
            setValuetoForm(globalVar.getJsonString());
            energysource_btn_submit_handler.setText("Update");
        }

        energysource_btn_submit_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( getValueFromForm()) {

                    insertToDB(HttpInsertUrl);


                }
                else
                {
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .playOn(findViewById(R.id.energysource_btn_submit));
                }


                //Intent i = new Intent(HouseholdActivity.this, FamilyMembersInfo.class);
                // Starts TargetActivity
                // startActivity(i);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.electapprecycler_view);

        recyclerView.setVisibility(View.VISIBLE);
        mAdapter = new ElectricalDetailsAdapter(electappDetailsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        if(globalVar.getMenu()==1) {

            prepareRecordData();

        }


        recyclerView.addOnItemTouchListener(new SourceEnergyActivity.RecyclerTouchListener(this, recyclerView, new SourceEnergyActivity.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                //positionSeleced=position;
                ElectricalappDetails electricappDetail=electappDetailsList.get(position);
               // Toast.makeText(getApplicationContext(), "Selected: " + electricappDetail.getName() + ", " + electricappDetail.getUbaelecno().toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(SourceEnergyActivity.this, ElectappDetailsActivity.class);

                i.putExtra("elecrecord",electricappRecord[position] );//electricappRecord
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(SourceEnergyActivity.this).toBundle()
                );

            }

            @Override
            public void onLongClick(View view, int position) {
               /* Toast.makeText(SelectRecordActivity.this, "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();*/
            }
        }));




    }

    private boolean getValueFromForm() {
        if(electricitySwitch_Handler.isChecked()) {
            electricityAvailValue = String.valueOf(electricityAvailEdit_Handler.getText());
            if(electricityAvailValue.length()==0)
            electricityAvailEdit_Handler.setError("Cannot be empty");
        }
        else
            electricityAvailValue ="NA";


        electricitySourceValue=electricitySourceSpinnerHandler.buildSelectedItemString();
        cookingSourceValue = cookingSourceSpinnerHandler.buildSelectedItemString();
        otherlightingsourceValue=String.valueOf(electricitySourceEdit_Handler.getText());
        othercookingsourceValue=String.valueOf(cookingSourceEdit_Handler.getText());


        if(electricitySwitch_Handler.isChecked()&&electricityAvailValue.compareTo("")==0)
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
                                   Intent i = new Intent(SourceEnergyActivity.this, LandInfoActivity.class);

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
                params.put("electricityavail",electricityAvailValue);
                params.put("electricitysource", electricitySourceValue);
                params.put("otherlightingsource", otherlightingsourceValue);
                params.put("cookingsource", cookingSourceValue);
                params.put("othercookingsource", othercookingsourceValue);


                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(SourceEnergyActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }


    public void setValuetoForm(String jsonString){

        try {
            JSONObject jobj = new JSONObject(jsonString);


            electricityAvailValue=jobj.getString("electricityavail");
            electricitySourceValue = jobj.getString("electricitysource");
            otherlightingsourceValue=jobj.getString("otherlightingsource");
            cookingSourceValue = jobj.getString("cookingsource");
            othercookingsourceValue=jobj.getString("othercookingsource");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(electricityAvailValue.compareTo("NA")==0||electricityAvailValue.compareTo("no")==0)
        {
            electricitySwitch_Handler.setChecked(false);
            electricitySourceEdit_Handler.setText("");


        }
        else {
            electricitySwitch_Handler.setChecked(true);
            electricityAvailEdit_Handler.setVisibility(View.VISIBLE);
            electricityAvailEdit_Handler.setText(electricityAvailValue);
        }




        if(electricitySourceValue.compareTo("no")==0)
            electricitySourceSpinnerHandler.setSelection(0);

        else {
            List<String> items = Arrays.asList(electricitySourceValue.split("\\s*,\\s*"));
            electricitySourceSpinnerHandler.setSelection(items);
        }
        if(cookingSourceValue.compareTo("no")==0)
            cookingSourceSpinnerHandler.setSelection(0);

        else {
            List<String> items = Arrays.asList(cookingSourceValue.split("\\s*,\\s*"));
            cookingSourceSpinnerHandler.setSelection(items);
        }
        if(otherlightingsourceValue.compareTo("no")==0)
        {
            electricitySourceEdit_Handler.setText("");
        }
        else
            electricitySourceEdit_Handler.setText(otherlightingsourceValue);

        if(othercookingsourceValue.compareTo("no")==0)
        {
            cookingSourceEdit_Handler.setText("");
        }
        else
            cookingSourceEdit_Handler.setText(othercookingsourceValue);



    }
    int  setSpinnerPos(Spinner spinner,String value)
    {


        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        return myAdap.getPosition(value);

        //set the default according to value
        // spinner.setSelection(spinnerPosition);
    }

    private void prepareRecordData() {
        electappDetailsList.clear();

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpSelectUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        /*Toast toast = Toast.makeText(getApplicationContext(),
                                ServerResponse,
                                Toast.LENGTH_LONG);
                        toast.show();*/
                        // globalVar.setFamilyjsonString(ServerResponse);
                        try {

                            JSONArray jsonarray = new JSONArray(ServerResponse);
                            //  JSONArray familycountjsonarray=jsonarray.getJSONArray(0);

                            //  JSONArray familyRecordsjsonarray=jsonarray.getJSONArray(1);
                            globalVar.setFamilyMemCount(jsonarray.length()+1);
                            electricappRecord=new String[jsonarray.length()];

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                electricappRecord[i]=jsonobject.toString();
                                Integer ubaelecno = jsonobject.getInt("ubaelecno");
                                String name = jsonobject.getString("appliances");

                                ElectricalappDetails Record = new ElectricalappDetails(ubaelecno,name);
                                electappDetailsList.add(Record);
                            }
                            mAdapter.notifyDataSetChanged();
                            electItems.setText((globalVar.getFamilyMemCount()-1)+" Appliances Added");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("ubaid", globalVar.getUbaid());

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(SourceEnergyActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        globalVar.resetElectIncrement();
        super.onBackPressed();
    }

}
