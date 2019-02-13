package galosoft.com.androiddrinkshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import java.util.List;

import galosoft.com.androiddrinkshop.DrinkActivity;
import galosoft.com.androiddrinkshop.Interface.ItemClickListener;
import galosoft.com.androiddrinkshop.Model.Category;
import galosoft.com.androiddrinkshop.R;
import galosoft.com.androiddrinkshop.Utils.Common;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

    Context context;
    List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.menu_item_layout, null);
        return new CategoryViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        Picasso.with(context).load(categories.get(position).Link).into(holder.img_product);
        holder.txt_menu_name.setText(categories.get(position).Name);

        //Event
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                Common.currentCategory = categories.get(position);

                //Start new activity
                context.startActivity(new Intent(context, DrinkActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
