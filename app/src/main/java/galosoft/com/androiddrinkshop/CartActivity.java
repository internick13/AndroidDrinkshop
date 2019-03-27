package galosoft.com.androiddrinkshop;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import galosoft.com.androiddrinkshop.Adapter.CartAdapter;
import galosoft.com.androiddrinkshop.Adapter.FavoriteAdapter;
import galosoft.com.androiddrinkshop.Database.ModelDB.Cart;
import galosoft.com.androiddrinkshop.Database.ModelDB.Favorite;
import galosoft.com.androiddrinkshop.Retrofit.IDrinkShopAPI;
import galosoft.com.androiddrinkshop.Utils.Common;
import galosoft.com.androiddrinkshop.Utils.RecyclerItemTouchHelper;
import galosoft.com.androiddrinkshop.Utils.RecyclerItemTouchHelperListener;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener{

    RecyclerView recycler_cart;
    Button btn_place_holder;
    List<Cart> cartList = new ArrayList<>();
    CompositeDisposable compositeDisposable;
    CartAdapter cartAdapter;
    RelativeLayout rootLayout;
    IDrinkShopAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        compositeDisposable =  new CompositeDisposable();
        mService = Common.getAPI();

        recycler_cart = findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback  = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

        btn_place_holder = findViewById(R.id.btn_place_order);
        btn_place_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeHolder();
            }
        });


        rootLayout = findViewById(R.id.rootLayout);

        loadCartItems();
    }

    private void placeHolder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submit Order");

        View submit_order_layout = LayoutInflater.from(this).inflate(R.layout.submit_order_layout, null);

        final EditText edt_comment = submit_order_layout.findViewById(R.id.edt_comment);
        final EditText edt_other_address = submit_order_layout.findViewById(R.id.edt_other_address);

        final RadioButton rdi_user_address = submit_order_layout.findViewById(R.id.rdi_user_address);
        final RadioButton rdi_other_address = submit_order_layout.findViewById(R.id.rdi_other_address);

        //Event
        rdi_user_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    edt_other_address.setEnabled(false);
            }
        });

        rdi_other_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    edt_other_address.setEnabled(true);
            }
        });

        builder.setView(submit_order_layout);

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String orderCommnet = edt_comment.getText().toString();
                final String orderAddresss;
                if(rdi_user_address.isChecked())
                    orderAddresss = Common.currentUser.getAddress();
                else if(rdi_other_address.isChecked())
                    orderAddresss = edt_other_address.getText().toString();
                else
                    orderAddresss = "";

                //submit order
                compositeDisposable.add(
                  Common.cartRepository.getCartItems()
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribeOn(Schedulers.io())
                  .subscribe(new Consumer<List<Cart>>() {
                      @Override
                      public void accept(List<Cart> carts) throws Exception {
                          if(!TextUtils.isEmpty(orderAddresss))
                              sendOrderToServer(Common.cartRepository.sumPrice(), carts, orderCommnet, orderAddresss);
                          else
                              Toast.makeText(CartActivity.this, "Order Address can be null...", Toast.LENGTH_SHORT).show();
                      }
                  })
                );
            }
        });
        builder.show();
    }

    private void sendOrderToServer(float sumPrice, List<Cart> carts, String orderCommnet, String orderAddresss) {

        if(carts.size() > 0) {
            String orderDetail = new Gson().toJson(carts);
            mService.submitOrder(sumPrice, orderDetail, orderCommnet, orderAddresss, Common.currentUser.getPhone()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Toast.makeText(CartActivity.this, "Order submit", Toast.LENGTH_SHORT).show();
                    Common.cartRepository.emptyCart();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("ERROR", t.getMessage());
                }
            });
        }

    }


    private void loadCartItems() {
        compositeDisposable.add(Common.cartRepository.getCartItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
            @Override
            public void accept(List<Cart> carts) throws Exception {
                displayCartItem(carts);
            }
        }));
    }

    private void displayCartItem(List<Cart> carts) {
        cartList = carts;
        cartAdapter = new CartAdapter(this, carts);
        recycler_cart.setAdapter(cartAdapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }



    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CartAdapter.CartViewHolder) {
            String name = cartList.get(viewHolder.getAdapterPosition()).name;
            final Cart deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            cartAdapter.removeItem(deletedIndex);
            Common.cartRepository.deleteCartItem(deletedItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append(" remove from favorite list").toString(), Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdapter.restoreItem(deletedItem, deletedIndex);
                    Common.cartRepository.insertToCart(deletedItem);
                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
































