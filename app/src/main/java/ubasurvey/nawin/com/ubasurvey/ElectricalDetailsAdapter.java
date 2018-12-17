package ubasurvey.nawin.com.ubasurvey;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ElectricalDetailsAdapter  extends RecyclerView.Adapter<ElectricalDetailsAdapter.MyViewHolder> {

    private List<ElectricalappDetails> electricalList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView electriclItemnmae, familyMemberID;

        public MyViewHolder(View view) {
            super(view);
            electriclItemnmae = (TextView) view.findViewById(R.id.electricalappdetail);
            // familyMemberID = (TextView) view.findViewById(R.id.familyid);
        }
    }


    public ElectricalDetailsAdapter(List<ElectricalappDetails> electricalList) {
        this.electricalList = electricalList;
    }

    @Override
    public ElectricalDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.electricaldetails_list_row, parent, false);

        return new ElectricalDetailsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ElectricalDetailsAdapter.MyViewHolder holder, int position) {
        ElectricalappDetails electricalapp = electricalList.get(position);
        holder.electriclItemnmae.setText(electricalapp.getUbaelecno().toString()+" . "+electricalapp.getName());
        //holder.familyMemberID.setText(electricalapp.getUbaindid().toString());

    }

    @Override
    public int getItemCount() {
        return electricalList.size();
    }

}
