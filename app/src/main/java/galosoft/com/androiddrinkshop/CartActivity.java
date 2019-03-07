package galosoft.com.androiddrinkshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import galosoft.com.androiddrinkshop.Adapter.CartAdapter;
import galosoft.com.androiddrinkshop.Database.ModelDB.Cart;
import galosoft.com.androiddrinkshop.Utils.Common;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity {

    RecyclerView recycler_cart;
    Button btn_place_holder;

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        compositeDisposable =  new CompositeDisposable();

        recycler_cart = findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        btn_place_holder = findViewById(R.id.btn_place_order);

        loadCartItems();
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
        CartAdapter cartAdapter = new CartAdapter(this, carts);
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

    //Exit app when click back button
    boolean isBackButtonClicked = false;

    @Override
    public void onBackPressed() {
        if(isBackButtonClicked){
            super.onBackPressed();
            return;
        }
        this.isBackButtonClicked = true;
        Toast.makeText(this, "Click in back button again to exit...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        isBackButtonClicked = false;
    }
}
