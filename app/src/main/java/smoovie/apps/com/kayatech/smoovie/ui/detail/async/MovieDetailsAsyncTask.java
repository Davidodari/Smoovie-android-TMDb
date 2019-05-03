package smoovie.apps.com.kayatech.smoovie.ui.detail.async;

import android.os.AsyncTask;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.Reviews;
import smoovie.apps.com.kayatech.smoovie.model.Trailers;
import smoovie.apps.com.kayatech.smoovie.ui.detail.viewmodel.DetailViewModel;

/**
 * Created By blackcoder
 * On 03/05/19
 **/
public final class MovieDetailsAsyncTask extends AsyncTask<Integer, Void, Movie> {

    private DetailViewModel mDetailViewModel;
    private String lang;
    private MovieDetailsCallBack mMovieDetailsCallBack;
    private List<Trailers> trailers;
    private List<Reviews> reviews;

    public MovieDetailsAsyncTask(DetailViewModel detailViewModel, String language, MovieDetailsCallBack movieDetailsCallBack) {
        mDetailViewModel = detailViewModel;
        lang = language;
        mMovieDetailsCallBack = movieDetailsCallBack;
        trailers = null;
        reviews = null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mMovieDetailsCallBack.loading();
    }

    @Override
    protected Movie doInBackground(Integer... integers) {
        Movie vMovie = mDetailViewModel.getMovieDetails(integers[0], lang);
        reviews = mDetailViewModel.getMovieReviewsResponse(integers[0], lang).getReviews();
        trailers = mDetailViewModel.getMovieTrailersResponse(integers[0], lang).getTrailers();
        vMovie.setReviews(reviews);
        vMovie.setTrailers(trailers);
        return vMovie;
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        mMovieDetailsCallBack.complete(movie);
    }
}
