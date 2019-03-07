package galosoft.com.androiddrinkshop.Database.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import galosoft.com.androiddrinkshop.Database.ModelDB.Cart;
import galosoft.com.androiddrinkshop.Database.ModelDB.Favorite;

@Database(entities = {Cart.class, Favorite.class}, version = 1)
public abstract class EDTMRoomDatabase extends RoomDatabase{

    public abstract  CartDAO cartDAO();
    public abstract  FavoriteDAO favoriteDAO();
    private static EDTMRoomDatabase instance;

    public static EDTMRoomDatabase getInstance(Context context) {
        if(instance == null)
            instance = Room.databaseBuilder(context, EDTMRoomDatabase.class, "Galo_DrinkShopDB").allowMainThreadQueries().build();
        return instance;
    }
}
