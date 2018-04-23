package com.example.user.pagination.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by User on 4/23/2018.
 */

public abstract class PaginationOnScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;

    public PaginationOnScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()){

            if ((visibleItemCount+firstVisibleItemPosition)>= totalItemCount
                    && firstVisibleItemPosition>= 0
                    && totalItemCount>= getTotalPageCount()){
                loadMoreItem();
            }
        }

    }

    protected abstract void loadMoreItem();
    protected abstract int getTotalPageCount();
    protected abstract boolean isLastPage();
    protected abstract boolean isLoading();

}
