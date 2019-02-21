package galosoft.com.androiddrinkshop.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import galosoft.com.androiddrinkshop.Database.ModelDB.Cart;
import galosoft.com.androiddrinkshop.Interface.ItemClickListener;
import galosoft.com.androiddrinkshop.Model.Drink;
import galosoft.com.androiddrinkshop.R;
import galosoft.com.androiddrinkshop.Utils.Common;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder>{

    Context context;
    List<Drink> drinkList;


    public DrinkAdapter(Context context, List<Drink> drinkList) {
        this.context = context;
        this.drinkList = drinkList;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.drink_item_layout, null);
        return new DrinkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, final int position) {
        holder.txt_price.setText(new StringBuilder("$").append(drinkList.get(position).Price).toString());
        holder.txt_drink_name.setText(drinkList.get(position).Name);

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddToCartDialog(position);
            }
        });

        Picasso.with(context).load(drinkList.get(position).Link).into(holder.img_product);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddToCartDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context).inflate(R.layout.add_to_cart_layout,null);

        //View
        ImageView img_product_dialog = itemView.findViewById(R.id.img_cart_product);
        final ElegantNumberButton txt_count = itemView.findViewById(R.id.txt_count);
        TextView txt_product_dialog = itemView.findViewById(R.id.txt_cart_product_name);

        EditText edt_comment = itemView.findViewById(R.id.edt_comment);

        RadioButton rdi_sizeM = itemView.findViewById(R.id.rdi_sizeM);
        RadioButton rdi_sizeL = itemView.findViewById(R.id.rdi_sizeL);

        rdi_sizeM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    Common.sizeOfCup = 0;
                }
            }
        });

        rdi_sizeL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    Common.sizeOfCup = 1;
                }
            }
        });

        RadioButton rdi_sugar_100 = itemView.findViewById(R.id.rdi_sugar_100);
        RadioButton rdi_sugar_70 = itemView.findViewById(R.id.rdi_sugar_70);
        RadioButton rdi_sugar_50 = itemView.findViewById(R.id.rdi_sugar_50);
        RadioButton rdi_sugar_30 = itemView.findViewById(R.id.rdi_sugar_30);
        RadioButton rdi_sugar_free = itemView.findViewById(R.id.rdi_sugar_free);

        rdi_sugar_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar = 30;
            }
        });

        rdi_sugar_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar = 50;
            }
        });

        rdi_sugar_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar = 70;
            }
        });

        rdi_sugar_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar = 100;
            }
        });

        rdi_sugar_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar = 0;
            }
        });

        RadioButton rdi_ice_100 = itemView.findViewById(R.id.rdi_ice_100);
        RadioButton rdi_ice_70 = itemView.findViewById(R.id.rdi_ice_70);
        RadioButton rdi_ice_50 = itemView.findViewById(R.id.rdi_ice_50);
        RadioButton rdi_ice_30 = itemView.findViewById(R.id.rdi_ice_30);
        RadioButton rdi_ice_free = itemView.findViewById(R.id.rdi_ice_free);

        rdi_ice_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.ice = 30;
            }
        });

        rdi_ice_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.ice = 50;
            }
        });

        rdi_ice_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.ice = 70;
            }
        });

        rdi_ice_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.ice = 100;
            }
        });

        rdi_ice_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.ice = 0;
            }
        });

        RecyclerView recyclerTopping = itemView.findViewById(R.id.recycler_topping);
        recyclerTopping.setLayoutManager(new LinearLayoutManager(context));
        recyclerTopping.setHasFixedSize(true);

        MultiChoiseAdapter adapter = new MultiChoiseAdapter(context, Common.toppingList);
        recyclerTopping.setAdapter(adapter);



        //Set data
        Picasso.with(context).load(drinkList.get(position).Link).into(img_product_dialog);
        txt_product_dialog.setText(drinkList.get(position).Name);
        builder.setView(itemView);

        builder.setNegativeButton("ADD TO CART", new DialogInterface.OnClickListener() {



            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(Common.sizeOfCup == -1) {
                    Toast.makeText(context, "Choose size of cup", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Common.sugar == -1) {
                    Toast.makeText(context, "Choose sugar", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Common.ice == -1) {
                    Toast.makeText(context, "Choose ice", Toast.LENGTH_SHORT).show();
                    return;
                }


                showConfirmDialog(position, txt_count.getNumber());
                dialogInterface.dismiss();
            }
        });

        builder.show();

    }

    private void showConfirmDialog(final int position, final String number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context).inflate(R.layout.confirm_add_to_cart_layout,null);

        //View
        ImageView img_product_dialog = itemView.findViewById(R.id.img_product);
        final TextView txt_product_dialog = itemView.findViewById(R.id.txt_cart_product_name);
        TextView txt_product_price = itemView.findViewById(R.id.txt_cart_product_price);
        TextView txt_sugar = itemView.findViewById(R.id.txt_sugar);
        TextView txt_ice = itemView.findViewById(R.id.txt_ice);
        final TextView txt_topping_extra = itemView.findViewById(R.id.txt_topping_extra);

        //Set data
        Picasso.with(context).load(drinkList.get(position).Link).into(img_product_dialog);
        txt_product_dialog.setText(new StringBuilder(drinkList.get(position).Name).append(" x").append(number).append(Common.sizeOfCup == 0 ? " Size M": " Size L").toString());

        txt_ice.setText(new StringBuilder("Ice: ").append(Common.ice).append("%").toString());
        txt_sugar.setText(new StringBuilder("Sugar: ").append(Common.sugar).append("%").toString());

        double price = (Double.parseDouble(drinkList.get(position).Price) * Double.parseDouble(number)) + Common.toppingPrice;

        if(Common.sizeOfCup == 1)
            price += 3.0;

        txt_product_price.setText(new StringBuilder("$").append(price));

        StringBuilder toppinng_final_comment = new StringBuilder("");
        for(String line:Common.toppingAdded)
            toppinng_final_comment.append(line).append("\n");

        txt_topping_extra.setText(toppinng_final_comment);

        final double finalPrice = price;

        builder.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Sql lite do later
                dialogInterface.dismiss();

                try {
                    //add sqlite
                    //create new Cart Item
                    Cart cartItem = new Cart();
                    cartItem.name = txt_product_dialog.getText().toString();
                    cartItem.amount = Integer.parseInt(number);
                    cartItem.ice = Common.ice;
                    cartItem.sugar = Common.sugar;
                    cartItem.price = finalPrice;
                    cartItem.toppingExtras = txt_topping_extra.getText().toString();
                    cartItem.link = drinkList.get(position).Link;

                    //add to db
                    Common.cartRepository.insertToCart(cartItem);
                    Log.d("Galo_degub", new Gson().toJson(cartItem));
                    Toast.makeText(context, "Save item to cart success", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(itemView);
        builder.show();
    }


    @Override
    public int getItemCount() {
        return drinkList.size();
    }
}
