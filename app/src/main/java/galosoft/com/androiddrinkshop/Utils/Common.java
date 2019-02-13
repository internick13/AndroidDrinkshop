package galosoft.com.androiddrinkshop.Utils;

import galosoft.com.androiddrinkshop.Model.Category;
import galosoft.com.androiddrinkshop.Retrofit.IDrinkShopAPI;
import galosoft.com.androiddrinkshop.Retrofit.RetrofitClient;
import galosoft.com.androiddrinkshop.Model.User;

public class Common {
    //In emulator local hos is 10.0.2.2
    private static final String BASE_URL = "http://10.0.2.2:8080/drinkshop/";

    public static User currentUser = null;
    public static Category currentCategory = null;

    public static IDrinkShopAPI getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);
    }
}
