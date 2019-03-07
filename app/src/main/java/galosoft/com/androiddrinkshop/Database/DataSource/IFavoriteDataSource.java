package galosoft.com.androiddrinkshop.Database.DataSource;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Query;
import java.util.List;
import galosoft.com.androiddrinkshop.Database.ModelDB.Favorite;
import io.reactivex.Flowable;

public interface IFavoriteDataSource {

    Flowable<List<Favorite>> getFavItems();

    int isFavorite(int itemId);

    void delete(Favorite favorite);
}
