package galosoft.com.androiddrinkshop.Utils;

import java.util.ArrayList;
import java.util.List;

import galosoft.com.androiddrinkshop.Model.Category;
import galosoft.com.androiddrinkshop.Model.Drink;
import galosoft.com.androiddrinkshop.Retrofit.IDrinkShopAPI;
import galosoft.com.androiddrinkshop.Retrofit.RetrofitClient;
import galosoft.com.androiddrinkshop.Model.User;

public class Common {
    //In emulator local hos is 10.0.2.2
    private static final String BASE_URL = "http://10.0.2.2:8080/drinkshop/";
    public static final String TOPPING_MENU_ID = "7";

    public static User currentUser = null;
    public static Category currentCategory = null;

    public static double toppingPrice = 0.0;
    public static List<String> toppingAdded = new ArrayList<>();

    public static List<Drink> toppingList = new ArrayList<>();

    //hold fields
    public static int sizeOfCup = -1; //-1 error, 0 M, 1 L
    public static  int sugar = -1;
    public static  int ice = -1;

    public static IDrinkShopAPI getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);
    }
}
