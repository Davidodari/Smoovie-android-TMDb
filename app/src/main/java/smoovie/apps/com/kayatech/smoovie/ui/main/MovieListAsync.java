package smoovie.apps.com.kayatech.smoovie.ui.main;

import android.os.AsyncTask;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.Category;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.ui.main.callbacks.MovieListCallBack;
import smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel.MainViewModel;

/**
 * Created By blackcoder
 * On 01/05/19
 **/
public final class MovieListAsync extends AsyncTask<Void, Void, List<Movie>> {
    private MainViewModel mMainViewModel;
    private MovieListCallBack mMovieListCallBack;
    private Category mCategory;

    MovieListAsync(MainViewModel mainViewModel, Category category, MovieListCallBack movieListCallBack) {
        mMainViewModel = mainViewModel;
        mMovieListCallBack = movieListCallBack;
        mCategory = category;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mMovieListCallBack.inProgress();
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {
//                TODO Load Language From Preferences
//                TODO Add Pagination on Scrolling page to page
//                TODO Sort By Category
        return mMainViewModel.getMovies(mCategory, "en-US", 1);
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        mMovieListCallBack.onFinished(movies, mCategory);
    }
}
