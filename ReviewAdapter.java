package com.example.lap.popular_movies_stage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lap.popular_movies_stage1.Model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private Review[] mReviewData;
    public static TextView mReviewListTextView = null;
    public static TextView mAuthorListTextView = null;

    public ReviewAdapter(Review[] review) {
        mReviewData = review;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mReviewListTextView = (TextView) itemView.findViewById(R.id.tv_review);
            mAuthorListTextView = (TextView) itemView.findViewById(R.id.tv_author);
        }
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        //inflate list item xml into a view
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        //set the review for list item's position
        String AuthorToBind = mReviewData[position].getAuthor();
        String ReviewToBind = mReviewData[position].getContent();
        mReviewListTextView.setText(ReviewToBind);
        mAuthorListTextView.setText(AuthorToBind + " said: ");

    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) {
            return 0;
        }
        return mReviewData.length;
    }
}