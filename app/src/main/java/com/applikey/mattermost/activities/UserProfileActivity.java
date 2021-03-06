package com.applikey.mattermost.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applikey.mattermost.R;
import com.applikey.mattermost.models.channel.Channel;
import com.applikey.mattermost.models.user.User;
import com.applikey.mattermost.mvp.presenters.UserProfilePresenter;
import com.applikey.mattermost.mvp.views.UserProfileView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.applikey.mattermost.R.id.status;

public class UserProfileActivity extends BaseMvpActivity implements UserProfileView {

    private static final String USER_ID_KEY = "user-id";
    private static final int MENU_ITEM_FAVORITE = Menu.FIRST;

    @InjectPresenter
    UserProfilePresenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.container)
    LinearLayout mContainer;

    @BindView(status)
    ImageView mStatus;

    @BindView(R.id.display_name)
    TextView mDisplayName;

    @BindView(R.id.email)
    TextView mEmail;

    @BindView(R.id.avatar)
    ImageView mAvatar;

    private Menu mMenu;

    public static Intent getIntent(Context context, User user) {
        final Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(USER_ID_KEY, user.getId());
        return intent;
    }

    @ProvidePresenter
    UserProfilePresenter providePresenter() {
        return new UserProfilePresenter(getIntent().getStringExtra(USER_ID_KEY));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_FAVORITE, Menu.NONE, R.string.make_favorite)
                .setIcon(R.drawable.ic_favorite_uncheck)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        mMenu = menu;
        mPresenter.onMenuSet();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_FAVORITE:
                mPresenter.toggleFavorite();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void showBaseDetails(User user) {
        mImageLoader.displayCircularImage(user.getProfileImage(), mAvatar);
        mDisplayName.setText(User.getDisplayableName(user));
        mEmail.setText(user.getEmail());
        setStatusIcon(user);
    }

    @Override
    public void onMakeFavorite(boolean favorite) {
        final int iconRes = favorite
                ? R.drawable.ic_favorite_check
                : R.drawable.ic_favorite_uncheck;
        mMenu.getItem(0).setIcon(iconRes);
    }

    @Override
    public void openDirectChannel(Channel channel) {
        startActivity(ChatActivity.getIntent(this, channel));
    }

    @OnClick(R.id.b_direct_message)
    void onClick() {
        mPresenter.sendDirectMessage();
    }

    private void initViews() {
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(null);
        }
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setStatusIcon(User user) {
        final User.Status status = User.Status.from(user.getStatus());
        mStatus.setImageResource(status.getDrawableId());
    }
}
