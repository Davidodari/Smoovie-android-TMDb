package smoovie.apps.com.kayatech.smoovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.Model.Movie;
import smoovie.apps.com.kayatech.smoovie.Network.OnMoviesCallback;
import smoovie.apps.com.kayatech.smoovie.Network.TMDBMovies;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    MoviesAdapter mMoviesAdapter;
    RecyclerView mMoviesRecyclerView;
    private boolean isFetchingMovies;
    private int currentPage = 1;
    GridLayoutManager gridLayoutManager;

    private TMDBMovies movieList;
    private String sortBy = TMDBMovies.POPULAR;


    //TODO ON ROTATE MOVIE VOTE AVERAGE VOTE COUNT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = TMDBMovies.getInstance();

        Toolbar toolbarMainPage = findViewById(R.id.action_toolbar_main);
        setSupportActionBar(toolbarMainPage);


        //Reference
        mMoviesRecyclerView = findViewById(R.id.movie_recycler_view);
        mMoviesRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        mMoviesRecyclerView.setItemViewCacheSize(20);
        mMoviesRecyclerView.setDrawingCacheEnabled(true);
        mMoviesRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        getMovies(currentPage);
        //RecyclerViewScrollListener();
        mMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            // Scroll to half of the list we increment it by one which is the next page.
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //TODO 2.THESE METHODS
                //TODO SMOOTH SCROLLING

                //on ui and cached
                int totalItemCount = gridLayoutManager.getItemCount();

                //in cache
                int visibleItemCount = gridLayoutManager.getChildCount();

                //on ui
                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    //if reached the end fetch more movies
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });

    }

    private void getMovies(int page) {
        //checks if state is checking or not
        isFetchingMovies = true;
        movieList.getMovies(page,sortBy, new OnMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {

                if (mMoviesAdapter == null) {
                    mMoviesAdapter = new MoviesAdapter(getApplicationContext(), movies,clickHandler);
                    mMoviesRecyclerView.setAdapter(mMoviesAdapter);
                } else {
                    if (page == 1) {
                        mMoviesAdapter.clearMovies();
                    }
                    //appends movie results to list and updates recycler view
                    mMoviesAdapter.setMovieList(movies);
                }
                currentPage = page;
                isFetchingMovies = false;

                setTitleBar();
            }

            @Override
            public void onFailure() {
                Log.d(TAG, "getMovies Failure");
            }
        });
    }

    //Set Title Bar on action Bar depending on get request from Singleton Class
    private void setTitleBar() {
        switch (sortBy) {
            case TMDBMovies.POPULAR:
                setTitle(getString(R.string.action_sort_most_popular));
                break;
            case TMDBMovies.TOP_RATED:
                setTitle(getString(R.string.action_sort_high_ratings));
                break;
        }
    }

    MovieClickHandler clickHandler = new MovieClickHandler() {
        @Override
        public void onClick(Movie movie) {
            Intent openDetailsActivity = new Intent(MainActivity.this, DetailActivity.class);
            openDetailsActivity.putExtra(DetailActivity.MOVIE_ID, movie.getMovieId());
            startActivity(openDetailsActivity);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_sort) {
            showSortPopUpMenu();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    private void showSortPopUpMenu() {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.action_sort));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                /*
                 * Every time we sort, we need to go back to page 1
                        */
                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.action_sort_most_popular:
                        sortBy = TMDBMovies.POPULAR;
                        getMovies(currentPage);
                        return true;
                    case R.id.action_sort_high_rating:
                        sortBy = TMDBMovies.TOP_RATED;
                        getMovies(currentPage);
                        return true;
                    default:
                        return false;
                }
            }
        });
        sortMenu.inflate(R.menu.main_menu_items_sort);//What is being inflated
        sortMenu.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        movieList = TMDBMovies.getInstance();
        getMovies(currentPage);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        movieList = TMDBMovies.getInstance();
        getMovies(currentPage);
    }
}
