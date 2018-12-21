package ubasurvey.nawin.com.ubasurvey;

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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyInfoActivity extends AppCompatActivity {
Button addButton,nextButton;
    TextView famMembers;
    private List<FamilyDetails> familyDetailsList = new ArrayList<>();
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;
    private FamilyDetailsAdapter mAdapter;
    ChoiceApplication globalVar;
    String familyRecord[];
    int positionSeleced;
    String HttpSelectUrl ;//= "http://navinsjavatutorial.000webhostapp.com/ucbsurvey/ubaselectfamilydetails.php";
    private CoordinatorLayout coordinatorLayout;

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

    private int request_Code = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request_Code) {
            if (resultCode == RESULT_OK) {
              // if(positionSeleced < 0) {
                    prepareRecordData();
                   // famMembers.setText(data.getStringExtra("value"));//globalVar.getFamilyMemCount() + " Family Members Added");
               /* }
                else
                {

                    familyRecord[positionSeleced]=data.getData().toString();


                    positionSeleced=-1;
                }*/
            }

            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_info);
        HttpSelectUrl=getString(R.string.url)+"ubaselectfamilydetails.php";
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .familyinfocoordinatorlayout);
        globalVar=(ChoiceApplication)getApplicationContext();
        positionSeleced=-1;

        recyclerView = (RecyclerView) findViewById(R.id.familyrecycler_view);


        recyclerView.addOnItemTouchListener(new FamilyInfoActivity.RecyclerTouchListener(this, recyclerView, new FamilyInfoActivity.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                positionSeleced=position;
                FamilyDetails familyMemberDetail=familyDetailsList.get(position);
                Toast.makeText(getApplicationContext(), "Selected: " + familyMemberDetail.getName() + ", " + familyMemberDetail.getUbaindid().toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(FamilyInfoActivity.this, FamilyDetailsActivity.class);
                i.putExtra("familyrecord",familyRecord[position]);
                startActivity(i);
             
            }

            @Override
            public void onLongClick(View view, int position) {
               /* Toast.makeText(SelectRecordActivity.this, "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();*/
            }
        }));

        progressDialog = new ProgressDialog(FamilyInfoActivity.this);

        famMembers=(TextView)findViewById(R.id.familymem_Textview) ;
        addButton=(Button)findViewById(R.id.addfamilymem_btn);
        nextButton=(Button)findViewById(R.id.next_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FamilyInfoActivity.this, FamilyDetailsActivity.class);
                startActivityForResult(i,request_Code);

            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globalVar.getMenu()==0) {
                    globalVar.resetIncrement();

                    Intent i = new Intent(FamilyInfoActivity.this, MigrationStatusActivity.class);
                    startActivity(i);
                  }

                finish();

            }
        });

            recyclerView.setVisibility(View.VISIBLE);
            mAdapter = new FamilyDetailsAdapter(familyDetailsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            recyclerView.setHasFixedSize(true);
        if(globalVar.getMenu()==1)
            nextButton.setText("Back");

        prepareRecordData();

    }
    private void prepareRecordData() {
        familyDetailsList.clear();

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

                            globalVar.setFamilyMemCount(jsonarray.length()+1);
                            familyRecord=new String[jsonarray.length()];

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                familyRecord[i]=jsonobject.toString();
                                Integer ubaindid = jsonobject.getInt("ubaindid");
                                String name = jsonobject.getString("name");

                                FamilyDetails Record = new FamilyDetails(ubaindid,name);
                                familyDetailsList.add(Record);
                            }
                            mAdapter.notifyDataSetChanged();
                            famMembers.setText((globalVar.getFamilyMemCount()-1)+" Family Members Added");


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
        RequestQueue requestQueue = Volley.newRequestQueue(FamilyInfoActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

}
