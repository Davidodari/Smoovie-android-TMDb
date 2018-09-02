package smoovie.apps.com.kayatech.smoovie.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.viewmodel.MovieDetailsCallback;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.viewmodel.MovieDetailViewModel;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String MOVIE_ID = "movie_id";


    @BindView(R.id.tv_rating_value)
    TextView mRatingValue;
    @BindView(R.id.tv_label_release_date)
    TextView mLabelReleased;
    @BindView(R.id.tv_label_overview)
    TextView mLabelOverview;
    @BindView(R.id.tv_release_date)
    TextView mMovieReleaseDate;
    @BindView(R.id.tv_overview)
    TextView mMovieOverview;
    @BindView(R.id.tv_label_movie_title)
    TextView mMovieTitle;
    @BindView(R.id.rb_movie_rating)
    RatingBar mMovieRating;
    @BindView(R.id.iv_backdrop)
    ImageView mMovieBackdrop;
    @BindView(R.id.iv_poster_image)
    ImageView mMoviePoster;
    @BindView(R.id.iv_favcon)
    ImageView mFavcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Bind Views
        ButterKnife.bind(this);


        Typeface custom_font_thin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        mLabelReleased.setTypeface(custom_font_thin);
        mLabelOverview.setTypeface(custom_font_thin);

        //Check if intent contains extras
        if (getIntent().getExtras() != null) {
            int mMovieId = Parcels.unwrap(getIntent().getParcelableExtra(MOVIE_ID));
            Log.d("Movie Being Passed", "" + mMovieId);
            setupToolbar();

            MovieDetailViewModel movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
            movieDetailViewModel.init(mMovieId, new MovieDetailsCallback() {
                @Override
                public void onSuccess(Movie movie) {
                    if (movie != null) {

                        updateInterface(movie);

                        // Toast.makeText(DetailActivity.this,""+movie.getMovieTitle(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError() {

                }
            });
            movieDetailViewModel.getMovie().observe(this, new Observer<Movie>() {
                @Override
                public void onChanged(@Nullable Movie movie) {
                    if (movie != null) {
                        Log.d(TAG, "TITLE::" + movie.toString());
                        updateInterface(movie);
                    }
                }
            });

        }

        mFavcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavourites(v);
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
    }

    private void updateInterface(Movie movie) {
        String IMAGE_BASE_URL_BACKDROP = "http://image.tmdb.org/t/p/w780";
        String IMAGE_BASE_URL_POSTER = "http://image.tmdb.org/t/p/w185";

        final Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        mMovieTitle.setText(movie.getMovieTitle());
        mMovieTitle.setTypeface(custom_font);
        mMovieOverview.setText(movie.getMovieOverview());
        mMovieOverview.setTypeface(custom_font);
        mMovieRating.setVisibility(View.VISIBLE);
        mMovieRating.setRating(movie.getVoterAverage()/2);

        float movieavg = movie.getVoterAverage();
        String movieAvgString = Float.toString(movieavg);

        mRatingValue.setText(movieAvgString);
        mMovieReleaseDate.setText(movie.getMovieReleaseDate());
        mMovieReleaseDate.setTypeface(custom_font);
        if (!isFinishing()) {
            Picasso.with(DetailActivity.this)
                    .load(IMAGE_BASE_URL_BACKDROP + movie.getBackdrop())
                    .error(R.drawable.test_back)
                    .placeholder(R.drawable.test_back)
                    .into(mMovieBackdrop);
        }
        if (!isFinishing()) {
            Picasso.with(DetailActivity.this)
                    .load(IMAGE_BASE_URL_POSTER + movie.getMoviePoster())
                    .error(R.drawable.test)
                    .placeholder(R.drawable.test)
                    .into(mMoviePoster);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void displayError() {
        new StyleableToast
                .Builder(DetailActivity.this)
                .text(getString(R.string.error_network_message))
                .textColor(Color.WHITE)
                .backgroundColor(Color.rgb(66, 165, 245))
                .length(Toast.LENGTH_LONG)
                .show();
        finish();

    }

    private void addFavourites(View v){
//         Drawable currentDrawable = mFavcon.getDrawable();
//         if(currentDrawable == getDrawable(R.drawable.ic_favorite_true)){
//             mFavcon.setImageDrawable(getDrawable(R.drawable.ic_favorite_false));
//             new StyleableToast
//                     .Builder(DetailActivity.this)
//                     .text("Removed from Favourites")
//                     .textColor(Color.WHITE)
//                     .backgroundColor(Color.rgb(66, 165, 245))
//                     .length(Toast.LENGTH_SHORT)
//                     .show();
//
//         }else {
//             mFavcon.setImageDrawable(getDrawable(R.drawable.ic_favorite_true));
//             new StyleableToast
//                     .Builder(DetailActivity.this)
//                     .text("Added to Favourites")
//                     .textColor(Color.WHITE)
//                     .backgroundColor(Color.rgb(66, 165, 245))
//                     .length(Toast.LENGTH_SHORT)
//                     .show();
//         }
    }
}
