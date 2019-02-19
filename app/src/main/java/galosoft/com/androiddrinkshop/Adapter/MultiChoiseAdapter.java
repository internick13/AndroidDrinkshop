package galosoft.com.androiddrinkshop.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import galosoft.com.androiddrinkshop.Model.Drink;
import galosoft.com.androiddrinkshop.R;
import galosoft.com.androiddrinkshop.Utils.Common;

public class MultiChoiseAdapter extends RecyclerView.Adapter<MultiChoiseAdapter.MultiChoiseViewHolder>{

    Context context;
    List<Drink> optionList;

    public MultiChoiseAdapter(Context context, List<Drink> optionList) {
        this.context = context;
        this.optionList = optionList;
    }

    @NonNull
    @Override
    public MultiChoiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.multi_check_layout, null);
        return new MultiChoiseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiChoiseViewHolder holder, final int position) {
        holder.checkBox.setText(optionList.get(position).Name);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                   if(b) {
                       Common.toppingAdded.add(compoundButton.getText().toString());
                       Common.toppingPrice += Double.parseDouble(optionList.get(position).Price);
                   } else {
                       Common.toppingAdded.remove(compoundButton.getText().toString());
                       Common.toppingPrice -= Double.parseDouble(optionList.get(position).Price);
                   }
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    class MultiChoiseViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public MultiChoiseViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.ckb_topping);
        }
    }
}
