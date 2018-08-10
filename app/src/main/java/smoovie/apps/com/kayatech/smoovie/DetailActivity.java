package smoovie.apps.com.kayatech.smoovie;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.Model.Movie;
import smoovie.apps.com.kayatech.smoovie.Model.MovieDetailsCallback;
import smoovie.apps.com.kayatech.smoovie.Network.TMDBMovies;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static String MOVIE_ID = "movie_id";
    private static String IMAGE_BASE_URL_BACKDROP = "http://image.tmdb.org/t/p/w780";
    private static String IMAGE_BASE_URL_POSTER = "http://image.tmdb.org/t/p/w185";
    private int mMovieId;
    private TMDBMovies mMoviesList;

    @BindView(R.id.collapsingToolbar)CollapsingToolbarLayout cbTitle;
    @BindView(R.id.tv_rating_value)TextView mRatingValue;
    @BindView(R.id.tv_label_release_date)TextView mLabelReleased;
    @BindView(R.id.tv_label_overview)TextView mLabelOverview;
    @BindView(R.id.tv_release_date)TextView mMovieReleaseDate;
    @BindView(R.id.tv_overview)TextView mMovieOverview;
    @BindView(R.id.tv_label_movie_title)TextView mMovieTitle;
    @BindView(R.id.rb_movie_rating) RatingBar mMovieRating;
    @BindView(R.id.iv_backdrop)  ImageView mMovieBackdrop;
    @BindView(R.id.iv_poster_image)  ImageView mMoviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mMovieId = getIntent().getIntExtra(MOVIE_ID, mMovieId);

        mMoviesList = TMDBMovies.getInstance();

        Typeface custom_font_thin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        mLabelReleased.setTypeface(custom_font_thin);
        mLabelOverview.setTypeface(custom_font_thin);

        setupToolbar();
        getMovie();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
    private void getMovie() {
        final Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        final Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        //TODO SNACKBAR ON NO CONNECTION
        mMoviesList.getMovie(mMovieId, new MovieDetailsCallback() {
            @Override
            public void onSuccess(Movie movie) {

                mMovieTitle.setText(movie.getMovieTitle());
                mMovieTitle.setTypeface(custom_font);


                //Collapse Bar

                cbTitle.setTitle(movie.getMovieTitle());
                cbTitle.setCollapsedTitleTypeface(custom_font);
                cbTitle.setExpandedTitleTypeface(custom_font2);


                mMovieOverview.setText(movie.getMovieOverview());
                mMovieOverview.setTypeface(custom_font);
                mMovieRating.setVisibility(View.VISIBLE);
                mMovieRating.setRating(movie.getVoterAverage() / 2);

               float movieavg =  movie.getVoterAverage();
               String movieAvgString = Float.toString(movieavg);

                mRatingValue.setText(movieAvgString);


                mMovieReleaseDate.setText(movie.getMovieReleaseDate());
                mMovieReleaseDate.setTypeface(custom_font);
                if (!isFinishing()) {
                    Picasso.with(DetailActivity.this)
                            .load(IMAGE_BASE_URL_BACKDROP + movie.getBackdrop())
                            .error(R.color.colorPrimary)
                            .into(mMovieBackdrop);
                }
                if (!isFinishing()) {
                    Picasso.with(DetailActivity.this)
                            .load(IMAGE_BASE_URL_POSTER + movie.getMoviePoster())
                            .error(R.color.colorPrimary)
                            .into(mMoviePoster);
                }
            }

            @Override
            public void onError() {
                Log.d(TAG, "Internet Connection Error: ");
                Toast.makeText(DetailActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
//                Snackbar snackbar = Snackbar
//                        .make(cdDet, , Snackbar.LENGTH_LONG);
//
//                snackbar.show();
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
