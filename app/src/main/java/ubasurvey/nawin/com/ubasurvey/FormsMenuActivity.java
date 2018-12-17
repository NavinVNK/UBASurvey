package ubasurvey.nawin.com.ubasurvey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FormsMenuActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemListener {


    RecyclerView recyclerView;
    ArrayList<DataModel> arrayList;
    TextView ubaid;
    ChoiceApplication globalVar;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(FormsMenuActivity.this);
        globalVar=(ChoiceApplication)getApplicationContext();
        setContentView(R.layout.activity_forms_menu);
        getWindow().setFormat(PixelFormat.RGB_565);
        ubaid=(TextView)findViewById(R.id.ubaidLabel);
        ubaid.setText("UBAID :"+globalVar.getUbaid());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();
        arrayList.add(new DataModel("1.Basic Info", R.drawable.ic_assignment_black_24dp, "#408000"));
        arrayList.add(new DataModel("2.Househlod Info", R.drawable.ic_assignment_black_24dp, "#990099"));
        arrayList.add(new DataModel("3.Respondant Profile", R.drawable.ic_assignment_black_24dp, "#990000"));
        arrayList.add(new DataModel("4.Family Member Info", R.drawable.ic_assignment_black_24dp, "#003d99"));
        arrayList.add(new DataModel("5.Migration Status in Family", R.drawable.ic_assignment_black_24dp, "#e6e600"));
        arrayList.add(new DataModel("6.Goverment Scheme", R.drawable.ic_assignment_black_24dp, "#6600cc"));
        arrayList.add(new DataModel("7.Source of Water", R.drawable.ic_assignment_black_24dp, "#1ac6ff"));
        arrayList.add(new DataModel("8.Source of Energy and Power", R.drawable.ic_assignment_black_24dp, "#ff1a1a"));
        arrayList.add(new DataModel("9.Land Holding Info", R.drawable.ic_assignment_black_24dp, "#b30000"));
        arrayList.add(new DataModel("10.Agricultural Inputs", R.drawable.ic_assignment_black_24dp, "#00e600"));
        arrayList.add(new DataModel("11.Agricultural Produce", R.drawable.ic_assignment_black_24dp, "#602060"));
        arrayList.add(new DataModel("12.Livestock Numbers", R.drawable.ic_assignment_black_24dp, "#77773c"));
        arrayList.add(new DataModel("13.Major Problems", R.drawable.ic_assignment_black_24dp, "#996633"));
        arrayList.add(new DataModel("General Info", R.drawable.ic_assignment_black_24dp, "#330080"));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, arrayList, this);
        recyclerView.setAdapter(adapter);


        /**
         AutoFitGridLayoutManager that auto fits the cells by the column width defined.
         **/

        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        recyclerView.setLayoutManager(layoutManager);


        /**
         Simple GridLayoutManager that spans two columns
         **/
        /*GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);*/
    }

    @Override
    public void onItemClick(DataModel item) {

        Toast.makeText(getApplicationContext(), item.text + " is clicked", Toast.LENGTH_SHORT).show();
        switch (item.text)
        {
            case "1.Basic Info" :
                startActivity(new Intent(FormsMenuActivity.this, BasicinfoActivity.class));
                break;
            case "2.Househlod Info" :
                startActivity(new Intent(FormsMenuActivity.this, HouseholdActivity.class));
                break;
            case "3.Respondant Profile":
                startActivity(new Intent(FormsMenuActivity.this, RespondentProfileActivity.class));
                break;
            case "4.Family Member Info":
                startActivity(new Intent(FormsMenuActivity.this, FamilyInfoActivity.class));
                break;
            case "5.Migration Status in Family":
                startActivity(new Intent(FormsMenuActivity.this, MigrationStatusActivity.class));
                break;
            case "6.Goverment Scheme":
                startActivity(new Intent(FormsMenuActivity.this, GovtSchemesActivity.class));
                break;
            case "7.Source of Water":
                startActivity(new Intent(FormsMenuActivity.this, WaterSourceActivity.class));
                break;
            case "8.Source of Energy and Power":
                startActivity(new Intent(FormsMenuActivity.this, SourceEnergyActivity.class));
                break;
            case "9.Land Holding Info":
                startActivity(new Intent(FormsMenuActivity.this, LandInfoActivity.class));
                break;
            case "10.Agricultural Inputs":
                startActivity(new Intent(FormsMenuActivity.this, AgriculturalInputsActivity.class));
                break;
            case "11.Agricultural Produce":
                startActivity(new Intent(FormsMenuActivity.this, AgriProduceActivity.class));
                break;
            case "12.Livestock Numbers":
                startActivity(new Intent(FormsMenuActivity.this, LiveStockActivity.class));
                break;
            case "13.Major Problems":
                startActivity(new Intent(FormsMenuActivity.this, ProblemsVillageActivity.class));
                break;
            case "General Info":
                startActivity(new Intent(FormsMenuActivity.this, GeneralInfoActivity.class));
                break;


        }


    }

}
