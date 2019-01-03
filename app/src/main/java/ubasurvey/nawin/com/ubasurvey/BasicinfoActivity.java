package ubasurvey.nawin.com.ubasurvey;

//Location Library
import android.Manifest;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
//Location Library End

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class BasicinfoActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    
    //location variables
    private Location location;
    private TextView locationTv,villageName;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    //Location variables end
     ChoiceApplication globalVar;

    boolean success;
   // Creating Progress dialog.
    ProgressDialog progressDialog;

    // Storing server url into String variable.
    String HttpInsertUrl;// = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubainsertformone.php";
    String HttpSelectUrl;// = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubagetformone.php";
    String HttpUpdatetUrl;// = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateformone.php";


    Button btn_submit;
    HashMap<String,String> blockHashMap;
    HashMap<String,String> villageHashMap;
    HashMap<String,String> DistrictHashMap;

    Double latitute,longitute;
String basicInfo[];
    String ubaid,username,household_headNameValue,genderValue,householdIDValue,villageSpinnerValue, districtSpinnerValue,blockSpinnerValue,wardNoSpinnerValue,streetValue,gramPanchayatSpinnerValue, stateSpinnerValue,villageCode,districtCode,blockCode,latitudeValue,longitutevalue;
    Spinner villageSpinnerHandler, districtSpinnerHandler,genderSpinnerHandler,blockSpinnerHandler,wardNoSpinnerHandler,gramPanchayatSpinnerHandler, stateSpinnerHandler;
    EditText household_headNameValue_handler,householdIDEditTextHandler,streetEdithandler;


    ArrayAdapter<CharSequence> panchayat1_adapter;
    ArrayAdapter<CharSequence> panchayat2_adapter;
    ArrayAdapter<CharSequence> empty_adapter;
    private CoordinatorLayout coordinatorLayout;
    private DatabaseHelper ubadb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_basic_info);
        basicInfo=new String[17];
        ubadb= DatabaseHelper.getInstance(this);

        HttpInsertUrl=getString(R.string.url)+"ubainsertformone.php";
        HttpSelectUrl=getString(R.string.url)+"ubagetformone.php";
        HttpUpdatetUrl=getString(R.string.url)+"ubaupdateformone.php";

        progressDialog = new ProgressDialog(BasicinfoActivity.this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .basiccoordinatorLayout);
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enable GPS", Snackbar.LENGTH_LONG)
                    .setAction("Start Again", new View.OnClickListener() {
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
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(BasicinfoActivity.this).toBundle()
            );
            finish();
        }


        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();
        //Location initialize code ends

        villageHashMap = new HashMap<String, String>();
        villageHashMap.put("Anjur","ANJ");
        villageHashMap.put("Pattaravakkam","PAT");
        villageHashMap.put("Thenmelpakkam","THE");
        villageHashMap.put("Orathur","ORA");
        villageHashMap.put("Nattarasampattu","NAT");

        DistrictHashMap = new HashMap<String, String>();
        DistrictHashMap.put("Kancheepuram","KPM");

       blockHashMap = new HashMap<String, String>();
       blockHashMap.put("Kattankulathur","KTR");
       blockHashMap.put("Kundrathur","KDR");


        locationTv = findViewById(R.id.location);

        btn_submit = findViewById(R.id.basic_btn_submit);
        //villageSpinnerHandler = findViewById(R.id.villageSpinner);
        //districtSpinnerHandler = findViewById(R.id.districtspinner);
        blockSpinnerHandler = findViewById(R.id.BlockSpinner);
        gramPanchayatSpinnerHandler = findViewById(R.id.grampanachayatspinner);
        wardNoSpinnerHandler = findViewById(R.id.wardNoSpinner);
        genderSpinnerHandler=findViewById(R.id.spinner_Gender);
        villageName =(TextView) findViewById(R.id.villagename);
        household_headNameValue_handler = findViewById(R.id.household_HeadName);
        householdIDEditTextHandler = (EditText)findViewById(R.id.householdID);
        streetEdithandler=(EditText)findViewById(R.id.street);
        globalVar=(ChoiceApplication)getApplicationContext();
        username=globalVar.getVolunteerID();
         panchayat1_adapter =ArrayAdapter.createFromResource(this, R.array.GramPanachayat_option,
                android.R.layout.simple_spinner_item);

         panchayat2_adapter =ArrayAdapter.createFromResource(this, R.array.GramPanachayat_optiono,
                android.R.layout.simple_spinner_item);
         empty_adapter =ArrayAdapter.createFromResource(this, R.array.GramPanachayat_optionu,
                android.R.layout.simple_spinner_item);

        panchayat1_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        panchayat2_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        empty_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> blockadapter =ArrayAdapter.createFromResource(this, R.array.Block_option,
                android.R.layout.simple_spinner_item);


        blockSpinnerHandler.setAdapter(blockadapter);
        gramPanchayatSpinnerHandler.setAdapter(empty_adapter);
        blockSpinnerHandler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1) {
                    gramPanchayatSpinnerHandler.setAdapter(panchayat1_adapter);
                   // panchayat1_adapter.notifyDataSetChanged();

                }
                else if(position==2)
                {
;
                    gramPanchayatSpinnerHandler.setAdapter(panchayat2_adapter);
                }
                else
                    gramPanchayatSpinnerHandler.setAdapter(empty_adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gramPanchayatSpinnerHandler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                villageName.setText(gramPanchayatSpinnerHandler.getAdapter().getItem(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(globalVar.getMenu()==1) {
           // selectDatafromDB(globalVar.getUbaid());
           setValuetoForm(globalVar.getJsonString());
           /* Toast toast = Toast.makeText(getApplicationContext(),
                    "Basic info"+globalVar.getJsonString(),
                    Toast.LENGTH_LONG);

            toast.show();*/
            btn_submit.setText("Update");
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                // Calling method to get value from EditText.
              if( GetValueFromForm())
                  if(globalVar.getMenu()==0)
                  {
                      if(globalVar.getMode())
                      {
                          insertToDB();
                          Intent i = new Intent(BasicinfoActivity.this, HouseholdActivity.class);
                          i.putExtra("houseid",householdIDValue);
                          i.putExtra("househeadname",household_headNameValue);
                          startActivity(i);
                          finish();
                      }

                      else
                         insertToDB(HttpInsertUrl);
                  }

                  else
                  {
                      if(globalVar.getMode())
                      {
                          updateToDB();
                          finish();
                      }

                      else
                          updateToDB(HttpUpdatetUrl);
                  }



              else
              {
                  YoYo.with(Techniques.BounceInUp)
                          .duration(700)
                          .playOn(findViewById(R.id.basic_btn_submit));
              }

            }
        });


    }
    // Creating method to get value from EditText.
    public boolean GetValueFromForm(){

       // villageSpinnerValue =  villageSpinnerHandler.getSelectedItem().toString();
        basicInfo[0]= districtSpinnerValue ="Kancheepuram" ;//districtSpinnerHandler.getSelectedItem().toString();
        basicInfo[1]=blockSpinnerValue = blockSpinnerHandler.getSelectedItem().toString();
        basicInfo[2]= gramPanchayatSpinnerValue =  villageSpinnerValue=gramPanchayatSpinnerHandler.getSelectedItem().toString();
        villageName.setText(gramPanchayatSpinnerValue);
        basicInfo[3]=wardNoSpinnerValue = wardNoSpinnerHandler.getSelectedItem().toString();
        basicInfo[4]=stateSpinnerValue = "TN";//stateSpinnerHandler.getSelectedItem().toString();
        basicInfo[5]=villageCode=villageHashMap.get(gramPanchayatSpinnerValue);
        basicInfo[6]=districtCode="KPM";//DistrictHashMap.get(districtSpinnerValue);
        basicInfo[7]=blockCode=blockHashMap.get(blockSpinnerValue);
        basicInfo[8]=streetValue=String.valueOf(streetEdithandler.getText());
        basicInfo[9]=household_headNameValue = String.valueOf(household_headNameValue_handler.getText());

        basicInfo[10]=genderValue = genderSpinnerHandler.getSelectedItem().toString();
        basicInfo[11]=householdIDValue=String.valueOf(householdIDEditTextHandler.getText());
        basicInfo[12]= ubaid=stateSpinnerValue+districtCode+blockCode+villageCode+wardNoSpinnerValue+householdIDValue;
        try {
            basicInfo[13]= latitudeValue = latitute.toString();
            basicInfo[14]=longitutevalue = longitute.toString();
        } catch (Exception e) {
            e.printStackTrace();
            basicInfo[13]= latitudeValue = "0.0";
            basicInfo[14]= longitutevalue = "0.0";
        }
     basicInfo[15]=username;
        basicInfo[16]=globalVar.getUbaid();
        if(household_headNameValue.length()==0){
            household_headNameValue_handler.setError("Cannot be empty");
        }
        if(householdIDValue.length()==0){
            householdIDEditTextHandler.setError("Cannot be empty");
        }
        if(streetValue.length()==0){
            streetEdithandler.setError("Cannot be empty");
        }


        if(blockSpinnerValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)blockSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(wardNoSpinnerValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)wardNoSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(gramPanchayatSpinnerValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)gramPanchayatSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }
        if(genderValue.compareTo("Select Value")==0)
        {
            TextView errorText = (TextView)genderSpinnerHandler.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select a Value");//changes the selected item text to this
        }


        if(villageSpinnerValue.compareTo("Select Value")==0||blockSpinnerValue.compareTo("Select Value")==0 ||wardNoSpinnerValue.compareTo("Select Value")==0||gramPanchayatSpinnerValue.compareTo("Select Value")==0||genderValue.compareTo("Select Value")==0||streetValue.compareTo("")==0||householdIDValue.compareTo("")==0||household_headNameValue.compareTo("")==0)
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

                       globalVar.setUbaid(ubaid);
                     /* Toast toast = Toast.makeText(getApplicationContext(),
                               ServerResponse,
                               Toast.LENGTH_LONG);

                       toast.show();*/

                       Intent i = new Intent(BasicinfoActivity.this, HouseholdActivity.class);
                       i.putExtra("houseid",householdIDValue);
                       i.putExtra("househeadname",household_headNameValue);
                       startActivity(i);
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
               params.put("village", villageSpinnerValue);
               params.put("grampanchayat", gramPanchayatSpinnerValue);
               params.put("street", streetValue);
               params.put("wardno", wardNoSpinnerValue);
               params.put("block", blockSpinnerValue);
               params.put("district", districtSpinnerValue);
               params.put("state", stateSpinnerValue);
               params.put("nameofthehead",household_headNameValue);
               params.put("gender", genderValue);
               params.put("householdid",householdIDValue);
               params.put("latitude",latitudeValue);
               params.put("longitude",longitutevalue);
               params.put("username",username);

               return params;
           }

       };

       // Creating RequestQueue.
       RequestQueue requestQueue = Volley.newRequestQueue(BasicinfoActivity.this);

       // Adding the StringRequest object into requestQueue.
       requestQueue.add(stringRequest);

   }
    public void insertToDB() {

        ubadb.insertData(basicInfo);
    }
    void  updateToDB(String HttpUrl)
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
/*                      Toast toast = Toast.makeText(getApplicationContext(),
                                ServerResponse,
                                Toast.LENGTH_LONG);

                        toast.show();*/

                        globalVar.setUbaid(ubaid);

                        if(ServerResponse.compareTo("0")==0)
                        {
                           // selectDatafromDB(ubaid);
                        }

                        else
                        {
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
                params.put("oldubaid",globalVar.getUbaid());
                params.put("ubaid", ubaid);
                params.put("village", villageSpinnerValue);
                params.put("grampanchayat", gramPanchayatSpinnerValue);
                params.put("street", streetValue);
                params.put("wardno", wardNoSpinnerValue);
                params.put("block", blockSpinnerValue);
                params.put("district", districtSpinnerValue);
                params.put("state", stateSpinnerValue);
                params.put("nameofthehead",household_headNameValue);
                params.put("gender", genderValue);
                params.put("householdid",householdIDValue);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(BasicinfoActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
    public void updateToDB() {

        ubadb.updateData(basicInfo);
        globalVar.setJsonString(ubadb.getRecordData(ubaid));
    }

    public void setValuetoForm(String jsonString){



       try {
            JSONObject jobj = new JSONObject(jsonString);
            householdIDValue=jobj.getString("householdid");
            villageSpinnerValue = jobj.getString("village");
            districtSpinnerValue = jobj.getString("district");
            blockSpinnerValue =jobj.getString("block");
            wardNoSpinnerValue = jobj.getString("wardno");
            gramPanchayatSpinnerValue = jobj.getString("grampanchayat");
            streetValue=jobj.getString("street");
            stateSpinnerValue =jobj.getString("state");
           household_headNameValue=jobj.getString("nameofthehead");
           if(household_headNameValue.compareTo("null")==0)
               household_headNameValue = "";
           else
               household_headNameValue_handler.setText(household_headNameValue);
           genderValue = jobj.getString("gender");
           if(genderValue.compareTo("null")==0)
               genderValue= "Select Value";

        } catch (JSONException e) {
            e.printStackTrace();
        }


       // villageSpinnerHandler.setSelection(setSpinnerPos(villageSpinnerHandler,villageSpinnerValue));
//         districtSpinnerHandler.setSelection(setSpinnerPos(districtSpinnerHandler,districtSpinnerValue));
         blockSpinnerHandler.setSelection(setSpinnerPos(blockSpinnerHandler,blockSpinnerValue));
          villageName.setText(villageSpinnerValue);
         if(blockSpinnerValue.compareTo("Kattankulathur")==0) {
             gramPanchayatSpinnerHandler.setAdapter(panchayat1_adapter);
            // panchayat1_adapter.notifyDataSetChanged();

         }
         else {
             gramPanchayatSpinnerHandler.setAdapter(panchayat2_adapter);
            // panchayat2_adapter.notifyDataSetChanged();

         }
  /*      Toast toast = Toast.makeText(getApplicationContext(),
               "setVlue " +gramPanchayatSpinnerValue,
                Toast.LENGTH_LONG);
        toast.show();*/
        genderSpinnerHandler.setSelection(setSpinnerPos(genderSpinnerHandler,genderValue));
        gramPanchayatSpinnerHandler.setSelection(setSpinnerPos(gramPanchayatSpinnerHandler,gramPanchayatSpinnerValue),true);
         wardNoSpinnerHandler.setSelection(setSpinnerPos(wardNoSpinnerHandler,wardNoSpinnerValue));
//        gramPanchayatSpinnerHandler.post(new Runnable() {
//            public void run() {

//            }
//        });

        //stateSpinnerHandler.setSelection(setSpinnerPos(stateSpinnerHandler, stateSpinnerValue));
        villageCode=villageHashMap.get(villageSpinnerValue);
        districtCode="KPM";//DistrictHashMap.get(districtSpinnerValue);
        stateSpinnerValue="TN";
        householdIDEditTextHandler.setText(householdIDValue);
        streetEdithandler.setText(streetValue);

    }
    int  setSpinnerPos(Spinner spinner,String value)
    {


        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        return myAdap.getPosition(value);

        //set the default according to value
       // spinner.setSelection(spinnerPosition);
    }



    //LOcation codes
    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            locationTv.setText("You need to install Google Play Services to use the App properly");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop location updates
        if (googleApiClient != null  &&  googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            locationTv.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
            latitute=location.getLatitude();
            longitute=location.getLongitude();
            latitudeValue=latitute.toString();
            longitutevalue=longitute.toString();
        }

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            locationTv.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
            latitute=location.getLatitude();
            longitute=location.getLongitude();
            latitudeValue=latitute.toString();
            longitutevalue=longitute.toString();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(BasicinfoActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }

                break;
        }
    }


}
