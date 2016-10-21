package com.applikey.mattermost.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applikey.mattermost.R;
import com.applikey.mattermost.models.post.Post;
import com.applikey.mattermost.models.user.User;
import com.applikey.mattermost.utils.kissUtils.utils.TimeUtil;
import com.applikey.mattermost.web.images.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class PostAdapter extends RealmRecyclerViewAdapter<Post, PostAdapter.ViewHolder> {

    private static final int MY_POST_VIEW_TYPE = 0;
    private static final int OTHERS_POST_VIEW_TYPE = 1;

    private final String mCurrentUserId;
    private final ImageLoader mImageLoader;
    private final OnLongClickListener mOnLongClickListener;

    public PostAdapter(Context context,
                       RealmResults<Post> posts,
                       String currentUserId,
                       ImageLoader imageLoader,
                       OnLongClickListener onLongClickListener) {
        super(context, posts, true);
        this.mCurrentUserId = currentUserId;
        this.mImageLoader = imageLoader;
        this.mOnLongClickListener = onLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        final int layoutId = viewType == MY_POST_VIEW_TYPE
                ? R.layout.list_item_post_my : R.layout.list_item_post;

        final View v = inflater.inflate(layoutId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Post post = getItem(position);
        final User user = post.getUser();

        boolean showDate = position == 0 ||
                !oneDatePosts(post, getItem(position - 1));

        boolean showAuthor = position == 0 ||
                !hasSameAuthor(getItem(position - 1), post) ||
                showDate;

        boolean showTime = position == getItemCount() - 1 ||
                !oneTimePosts(post, getItem(position + 1)) ||
                !hasSameAuthor(post, getItem(position + 1));

        if (isMy(post)) {
            holder.bindOwn(post, user, showAuthor, showTime, showDate, mOnLongClickListener);
        } else {
            holder.bindOther(post, user, showAuthor, showTime, showDate, mImageLoader);
        }
    }

    @Override
    public int getItemViewType(int position) {
        final Post post = getItem(position);
        return isMy(post) ? MY_POST_VIEW_TYPE : OTHERS_POST_VIEW_TYPE;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @Bind(R.id.iv_status)
        ImageView mIvStatus;

        @Nullable
        @Bind(R.id.iv_preview_image)
        ImageView mIvPreviewImage;

        @Nullable
        @Bind(R.id.iv_preview_image_layout)
        RelativeLayout mIvPreviewImageLayout;

        @Bind(R.id.tv_date)
        TextView mTvDate;

        @Bind(R.id.tv_message)
        TextView mTvMessage;

        @Bind(R.id.tv_timestamp)
        TextView mTvTimestamp;

        @Bind(R.id.tv_name)
        TextView mTvName;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        private void bind(Post post, User user, boolean showAuthor, boolean showTime, boolean showDate) {
            mTvDate.setText(TimeUtil.formatDateOnly(post.getCreatedAt()));
            mTvTimestamp.setText(TimeUtil.formatTimeOnly(post.getCreatedAt()));
            mTvName.setText(User.getDisplayableName(user));
            mTvMessage.setText(post.getMessage());

            mTvDate.setVisibility(showDate ? View.VISIBLE : View.GONE);
            mTvName.setVisibility(showAuthor ? View.VISIBLE : View.GONE);
            mTvTimestamp.setVisibility(showTime ? View.VISIBLE : View.GONE);
        }

        void bindOwn(Post post, User user, boolean showAuthor, boolean showTime, boolean showDate,
                     OnLongClickListener onLongClickListener) {
            bind(post, user, showAuthor, showTime, showDate);

            itemView.setOnLongClickListener(v -> {
                onLongClickListener.onLongClick(post);
                return true;
            });
        }

        void bindOther(Post post, User user, boolean showAuthor, boolean showTime,
                       boolean showDate, ImageLoader imageLoader) {
            bind(post, user, showAuthor, showTime, showDate);

            final String previewImagePath = user.getProfileImage();
            if (mIvPreviewImageLayout != null && mIvPreviewImage != null
                    && mIvStatus != null && previewImagePath != null
                    && !previewImagePath.isEmpty()) {
                imageLoader.displayCircularImage(previewImagePath, mIvPreviewImage);
                mIvStatus.setImageResource(User.Status.from(user.getStatus()).getDrawableId());
                mIvPreviewImageLayout.setVisibility(showAuthor ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }

    private boolean isMy(Post post) {
        return post.getUserId().equals(mCurrentUserId);
    }

    private boolean hasSameAuthor(Post post, Post nextPost) {
        return post.getUserId().equals(nextPost.getUserId());
    }

    private boolean oneTimePosts(Post post, Post nextPost) {
        return TimeUtil.sameTime(post.getCreatedAt(), nextPost.getCreatedAt());
    }

    private boolean oneDatePosts(Post post, Post nextPost) {
        return TimeUtil.sameDate(post.getCreatedAt(), nextPost.getCreatedAt());
    }

    @FunctionalInterface
    public interface OnLongClickListener {

        void onLongClick(Post post);

    }
}
