package smoovie.apps.com.kayatech.smoovie.util;

import android.content.Context;

import smoovie.apps.com.kayatech.smoovie.MoviesRepository;
import smoovie.apps.com.kayatech.smoovie.db.MovieDatabase;
import smoovie.apps.com.kayatech.smoovie.network.MovieApiServices;
import smoovie.apps.com.kayatech.smoovie.network.NetworkAdapter;
import smoovie.apps.com.kayatech.smoovie.util.threads.AppExecutors;

/**
 * Created By blackcoder
 * On 30/04/19
 **/
public final class InjectorUtils {

    private static MoviesRepository provideRepository(Context context) {
        MovieDatabase database = MovieDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MovieApiServices vApiServices = NetworkAdapter
                .getRetrofitInstance()
                .create(MovieApiServices.class);
        return MoviesRepository.getInstance(database.movieDAO(), vApiServices, executors);
    }
}
