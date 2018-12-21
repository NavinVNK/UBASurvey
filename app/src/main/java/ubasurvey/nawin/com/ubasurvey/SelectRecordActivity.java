package ubasurvey.nawin.com.ubasurvey;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import  android.view.MotionEvent;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.databinding.DataBindingUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectRecordActivity extends AppCompatActivity implements RecordAdapter.RecordsAdapterListener{
    private  TextView emptyView;
    private SearchView searchView;
    private List<Record> RecordList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecordAdapter mAdapter;
    ChoiceApplication globalVar;
    private LinearLayout linearLayout;
    String searchString="";

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

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


    // Creating Progress dialog.
    ProgressDialog progressDialog;


    String HttpSelectUrl;// = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaselectrecords.php";
    String HttpSelectUrl1;// = "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubagetformone.php";
    String HttpDeleteUrl;//="http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubadeleterecord.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_record);
        globalVar=(ChoiceApplication)getApplicationContext();
        HttpSelectUrl=getString(R.string.url)+"ubaselectrecords.php";
        HttpSelectUrl1=getString(R.string.url)+"ubagetformone.php";
        HttpDeleteUrl=getString(R.string.url)+"ubadeleterecord.php";
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        linearLayout=(LinearLayout)findViewById(R.id.linearlayout);
        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyView = (TextView) findViewById(R.id.empty_view);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Record record=mAdapter.getRecordListFiltered().get(position);
                Toast.makeText(getApplicationContext(), "Selected: " + record.getTitle() + ", " + record.getGenre(), Toast.LENGTH_LONG).show();
               globalVar.setUbaid(record.getTitle());
                selectDatafromDB(record.getTitle());
               // mAdapter.getFilter().filter("tnkaor");
            }

            @Override
            public void onLongClick(View view, int position) {
/*                if(globalVar.getVolunteerID().compareTo("admin")==0)
                {
                    Record record=mAdapter.getRecordListFiltered().get(position);
                    Toast.makeText(getApplicationContext(), "Delected: " + record.getTitle() + ", " + record.getGenre(), Toast.LENGTH_LONG).show();
                    globalVar.setUbaid(record.getTitle());
                    deleteDatafromDB(record.getTitle());

                }*/
                if (globalVar.getVolunteerID().compareTo("admin") == 0) {
                    final Record record = mAdapter.getRecordListFiltered().get(position);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SelectRecordActivity.this);
                    // Setting Alert Dialog Title
                    alertDialogBuilder.setTitle("Delete Record");
                    // Icon Of Alert Dialog
                    alertDialogBuilder.setIcon(R.drawable.uba);
                    // Setting Alert Dialog Message
                    alertDialogBuilder.setMessage("Do you want to delete record with UBAID :"+record.getTitle()+ " With HouseHeadName :"+record.getGenre());
                    // alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {

                            //Toast.makeText(getApplicationContext(), "Delected: " + record.getTitle() + ", " + record.getGenre(), Toast.LENGTH_LONG).show();
                            globalVar.setUbaid(record.getTitle());
                            deleteDatafromDB(record.getTitle());
                            dialog.cancel();
                        }
                    });

                    alertDialogBuilder.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    //Setting the title manually
                    alertDialog.show();


                }
            }
        }));
        // white background notification bar
        whiteNotificationBar(recyclerView);
        mAdapter = new RecordAdapter(this,RecordList,this);
        mAdapter.setOnItemClickListener(new RecordAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
               // Log.d(TAG, "onItemClick position: " + position);
                Record record=RecordList.get(position);
                Toast.makeText(getApplicationContext(), "Selected: " + record.getTitle() + ", " + record.getGenre(), Toast.LENGTH_LONG).show();


            }

            @Override
            public void onItemLongClick(int position, View v) {
               // Log.d(TAG, "onItemLongClick pos = " + position);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());


        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator()); recyclerView.setAdapter(mAdapter); recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        progressDialog = new ProgressDialog(SelectRecordActivity.this);
        prepareRecordData();
    }
    private void prepareRecordData() {

      RecordList.clear();
        progressDialog.setMessage("Loading Records...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HttpSelectUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                    try {

                        JSONArray jsonarray = new JSONArray(response);
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String ubaid = jsonobject.getString("ubaid");
                            String nameofHead = jsonobject.getString("nameofthehead");
                            String gramPanchayat = jsonobject.getString("grampanchayat");
                            Record record = new Record(ubaid, nameofHead, gramPanchayat);
                            RecordList.add(record);
                        }
                        //mAdapter.notifyDataSetChanged();
                        mAdapter.getFilter().filter(searchString);


                    } catch (JSONException e) {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Toast.makeText(SelectRecordActivity.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "No internet connection!", Snackbar.LENGTH_LONG)
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
        }){
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("username", globalVar.getVolunteerID());

                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
             // Toast.makeText(SelectRecordActivity.this, "Error" + query, Toast.LENGTH_SHORT).show();
                searchString=query;
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
               // Toast.makeText(SelectRecordActivity.this,  query, Toast.LENGTH_SHORT).show();

                      mAdapter.getFilter().filter(query);
                     searchString=query;

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onRecordSelected(Record record) {
        Toast.makeText(getApplicationContext(), "Selected: " + record.getTitle() + ", " + record.getGenre(), Toast.LENGTH_LONG).show();
    }

    void  selectDatafromDB(final String ubaidlocal)
    {
        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait, We are fetching data from Server");
        progressDialog.show();
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpSelectUrl1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        //code to globar var
                        //setValuetoForm(ServerResponse);
                        globalVar.setJsonString(ServerResponse);


                     /*   Toast toast = Toast.makeText(getApplicationContext(),
                                "Menu "+globalVar.getJsonString(),
                                Toast.LENGTH_LONG);

                        toast.show();*/
                        startActivity(new Intent(SelectRecordActivity.this, FormsMenuActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        globalVar.setJsonString("");

                        // Showing error message if something goes wrong.
                        Toast.makeText(SelectRecordActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                        //finish();;
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
        RequestQueue requestQueue = Volley.newRequestQueue(SelectRecordActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    void  deleteDatafromDB(final String ubaidlocal)
    {
        // Showing progress dialog at user registration time.
        progressDialog.setMessage("Please Wait, We are Deleting your Data on Server");
        progressDialog.show();
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpDeleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        //code to globar var
                        //setValuetoForm(ServerResponse);
                        //globalVar.setJsonString(ServerResponse);

                        prepareRecordData();


                     /*   Toast toast = Toast.makeText(getApplicationContext(),
                                "Menu "+globalVar.getJsonString(),
                                Toast.LENGTH_LONG);

                        toast.show();*/

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        globalVar.setJsonString("");

                        // Showing error message if something goes wrong.
                        Toast.makeText(SelectRecordActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                        //finish();;
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
        RequestQueue requestQueue = Volley.newRequestQueue(SelectRecordActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
