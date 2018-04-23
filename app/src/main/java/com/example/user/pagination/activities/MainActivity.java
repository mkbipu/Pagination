package com.example.user.pagination.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.user.pagination.PaginationAdapter;
import com.example.user.pagination.R;
import com.example.user.pagination.api.MovieApi;
import com.example.user.pagination.api.MovieService;
import com.example.user.pagination.model.ResultsItem;
import com.example.user.pagination.model.TopRatedMovieData;
import com.example.user.pagination.utils.PaginationOnScrollListener;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.user.pagination.api.MovieApi.apikey;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    private MovieService movieService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        adapter = new PaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationOnScrollListener(linearLayoutManager) {
                                   @Override
                                   protected void loadMoreItem() {
                                       isLoading = true;
                                       currentPage += 1;

                                       new Handler().postDelayed(new Runnable() {
                                           @Override
                                           public void run() {
                                               loadNextPage();
                                           }
                                       }, 1000);

                                   }

                                   @Override
                                   protected int getTotalPageCount() {
                                       return TOTAL_PAGES;
                                   }

                                   @Override
                                   protected boolean isLastPage() {
                                       return isLastPage;
                                   }

                                   @Override
                                   protected boolean isLoading() {
                                       return isLoading;
                                   }
                               });
//                                       PaginationOnScrollListener(linearLayoutManager) {
//            @Override
//            protected void loadMoreItems() {
//                isLoading = true;
//                currentPage += 1;
//
//                // mocking network delay for API call
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadNextPage();
//                    }
//                }, 1000);
//            }
//
//            @Override
//            public int getTotalPageCount() {
//                return TOTAL_PAGES;
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return isLastPage;
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//        });

                //init service and load data
                movieService = MovieApi.getClient().create(MovieService.class);

        loadFirstPage();

    }


    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        callTopRatedMoviesApi().enqueue(new Callback<TopRatedMovieData>() {
            @Override
            public void onResponse(Call<TopRatedMovieData> call, Response<TopRatedMovieData> response) {
                // Got data. Send it to adapter

                List<ResultsItem> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<TopRatedMovieData> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });

    }

    /**
     * @param response extracts List<{@link ResultsItem>} from response
     * @return
     */
    private List<ResultsItem> fetchResults(Response<TopRatedMovieData> response) {
        TopRatedMovieData topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi().enqueue(new Callback<TopRatedMovieData>() {
            @Override
            public void onResponse(Call<TopRatedMovieData> call, Response<TopRatedMovieData> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<ResultsItem> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<TopRatedMovieData> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });
    }


    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationOnScrollListener} to load next page.
     */
    private Call<TopRatedMovieData> callTopRatedMoviesApi() {
        return movieService.getTopRatedMovies(
                apikey,
                "en_US",
                currentPage
        );
    }


}
