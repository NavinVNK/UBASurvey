package ubasurvey.nawin.com.ubasurvey;

//Location Library
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
    private TextView locationTv;
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
    SharedPreferences prefs;
    boolean success;
    static final String KEY="ubaid";

    // Creating Progress dialog.
    ProgressDialog progressDialog;

    // Storing server url into String variable.
    String HttpInsertUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubainsertformone.php";
    String HttpSelectUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubagetformone.php";
    String HttpUpdatetUrl = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaupdateformone.php";


    Button btn_submit;
    HashMap<String,String> villageHashMap;
    HashMap<String,String> DistrictHashMap;

    Double latitute,longitute;

    String ubaid,householdIDValue,villageSpinnerValue, districtSpinnerValue,blockSpinnerValue,wardNoSpinnerValue,gramPanchayatSpinnerValue, stateSpinnerValue,villageCode,districtCode,latitudeValue,longitutevalue;
    Spinner villageSpinnerHandler, districtSpinnerHandler,blockSpinnerHandler,wardNoSpinnerHandler,gramPanchayatSpinnerHandler, stateSpinnerHandler;
    EditText householdIDEditTextHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_info);
        progressDialog = new ProgressDialog(BasicinfoActivity.this);

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
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
        villageHashMap.put("Anjur","anjur");
        villageHashMap.put("Pattaravakkam","patta");
        villageHashMap.put("Thenmelpakkam","thenm");
        villageHashMap.put("Orathur","orath");
        villageHashMap.put("Nattarasampattu","natta");
        DistrictHashMap = new HashMap<String, String>();
        DistrictHashMap.put("Kancheepuram","Ka");



        locationTv = findViewById(R.id.location);

        btn_submit = findViewById(R.id.basic_btn_submit);
        villageSpinnerHandler = findViewById(R.id.villageSpinner);
        districtSpinnerHandler = findViewById(R.id.districtspinner);
        blockSpinnerHandler = findViewById(R.id.BlockSpinner);
        gramPanchayatSpinnerHandler = findViewById(R.id.GramPanachayatSpinner);
        wardNoSpinnerHandler = findViewById(R.id.wardNoSpinner);
        stateSpinnerHandler = findViewById(R.id.StateSpinner);
        householdIDEditTextHandler = findViewById(R.id.householdID);
        globalVar=(ChoiceApplication)getApplicationContext();
        if(globalVar.getMenu()==1) {
           // selectDatafromDB(globalVar.getUbaid());
           setValuetoForm(globalVar.getJsonString());
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Basic info"+globalVar.getJsonString(),
                    Toast.LENGTH_LONG);

            toast.show();
            btn_submit.setText("Update");
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                // Calling method to get value from EditText.
              if( GetValueFromForm())
                  if(globalVar.getMenu()==0)
                      insertToDB(HttpInsertUrl);
                  else
                      updateToDB(HttpUpdatetUrl);


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

        villageSpinnerValue = villageSpinnerHandler.getSelectedItem().toString();
        districtSpinnerValue = districtSpinnerHandler.getSelectedItem().toString();
        blockSpinnerValue = blockSpinnerHandler.getSelectedItem().toString();
        wardNoSpinnerValue = wardNoSpinnerHandler.getSelectedItem().toString();
        gramPanchayatSpinnerValue = gramPanchayatSpinnerHandler.getSelectedItem().toString();
        stateSpinnerValue = stateSpinnerHandler.getSelectedItem().toString();
        villageCode=villageHashMap.get(villageSpinnerValue);
        districtCode=DistrictHashMap.get(districtSpinnerValue);
        householdIDValue=String.valueOf(householdIDEditTextHandler.getText());
        ubaid=stateSpinnerValue+districtCode+villageCode+householdIDValue;
        latitudeValue=latitute.toString();
        longitutevalue=longitute.toString();


        if(villageSpinnerValue.compareTo("Select Value")==0||blockSpinnerValue.compareTo("Select Value")==0 ||wardNoSpinnerValue.compareTo("Select Value")==0||gramPanchayatSpinnerValue.compareTo("Select Value")==0||householdIDValue.compareTo("")==0)
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
                      Toast toast = Toast.makeText(getApplicationContext(),
                               ServerResponse,
                               Toast.LENGTH_LONG);

                       toast.show();
                       prefs = getSharedPreferences("lastrecord", MODE_PRIVATE);
                       SharedPreferences.Editor editor = prefs.edit();
                       //---save the values in the EditText view to preferences---
                       editor.putString(KEY, ubaid);
                       //---saves the values---
                       editor.commit();
                       Intent i = new Intent(BasicinfoActivity.this, HouseholdActivity.class);
                       i.putExtra("houseid",householdIDValue);
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
                       Toast.makeText(BasicinfoActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
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
               params.put("wardno", wardNoSpinnerValue);
               params.put("block", blockSpinnerValue);
               params.put("district", districtSpinnerValue);
               params.put("state", stateSpinnerValue);
               params.put("householdid",householdIDValue);
               params.put("latitude",latitudeValue);
               params.put("longitude",longitutevalue);

               return params;
           }

       };

       // Creating RequestQueue.
       RequestQueue requestQueue = Volley.newRequestQueue(BasicinfoActivity.this);

       // Adding the StringRequest object into requestQueue.
       requestQueue.add(stringRequest);

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

                        globalVar.setUbaid(ubaid);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                ServerResponse,
                                Toast.LENGTH_LONG);

                        toast.show();
                        prefs = getSharedPreferences("lastrecord", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        //---save the values in the EditText view to preferences---
                        editor.putString(KEY, ubaid);
                        //---saves the values---
                        editor.commit();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Showing error message if something goes wrong.
                        Toast.makeText(BasicinfoActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
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
                params.put("wardno", wardNoSpinnerValue);
                params.put("block", blockSpinnerValue);
                params.put("district", districtSpinnerValue);
                params.put("state", stateSpinnerValue);
                params.put("householdid",householdIDValue);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(BasicinfoActivity.this);

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
                        //code to globar var
                        setValuetoForm(ServerResponse);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Showing error message if something goes wrong.
                        Toast.makeText(BasicinfoActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(BasicinfoActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

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
            stateSpinnerValue =jobj.getString("state");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        villageSpinnerHandler.setSelection(setSpinnerPos(villageSpinnerHandler,villageSpinnerValue));
         districtSpinnerHandler.setSelection(setSpinnerPos(districtSpinnerHandler,districtSpinnerValue));
         blockSpinnerHandler.setSelection(setSpinnerPos(blockSpinnerHandler,blockSpinnerValue));
         wardNoSpinnerHandler.setSelection(setSpinnerPos(wardNoSpinnerHandler,blockSpinnerValue));
         gramPanchayatSpinnerHandler.setSelection(setSpinnerPos(gramPanchayatSpinnerHandler,gramPanchayatSpinnerValue));
        stateSpinnerHandler.setSelection(setSpinnerPos(stateSpinnerHandler, stateSpinnerValue));
        villageCode=villageHashMap.get(villageSpinnerValue);
        districtCode=DistrictHashMap.get(districtSpinnerValue);
        householdIDEditTextHandler.setText(householdIDValue);
        //ubaid=stateSpinnerValue+districtCode+villageCode+householdIDValue;



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
