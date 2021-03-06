package com.applikey.mattermost.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.applikey.mattermost.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChannelTypeView extends LinearLayout {

    @BindView(R.id.tv_type_public)
    TextView mTvTypePublic;

    @BindView(R.id.tvTypePrivate)
    TextView mTvTypePrivate;

    @BindView(R.id.toggle_channel_type)
    ToggleButton mToggleButton;

    private CompoundButton.OnCheckedChangeListener mExternalCheckedChangeListener;

    private CompoundButton.OnCheckedChangeListener mOnCheckedListener = (view, checked) -> {
        if (checked) {
            mTvTypePublic.setTextColor(
                    ContextCompat.getColor(getContext(), R.color.textlinkDisabled));
            mTvTypePrivate.setTextColor(
                    ContextCompat.getColor(getContext(), R.color.textlinkEnabled));

            mTvTypePublic.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_public_channel_unselected, 0, 0, 0);
            mTvTypePrivate.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_private_channel_selected, 0, 0, 0);
        } else {
            mTvTypePublic.setTextColor(
                    ContextCompat.getColor(getContext(), R.color.textlinkEnabled));
            mTvTypePrivate.setTextColor(
                    ContextCompat.getColor(getContext(), R.color.textlinkDisabled));
            mTvTypePublic.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_public_channel_selected, 0, 0, 0);
            mTvTypePrivate.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_private_channel_unselected, 0, 0, 0);

        }
        if (mExternalCheckedChangeListener != null) {
            mExternalCheckedChangeListener.onCheckedChanged(view, checked);
        }
    };

    public ChannelTypeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);

    }

    public ChannelTypeView(Context context) {
        this(context, null, 0);
    }

    public ChannelTypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.channel_type_view, this);
        ButterKnife.bind(this);
        mToggleButton.setOnCheckedChangeListener(mOnCheckedListener);
        mToggleButton.setChecked(true);
    }

    public boolean isChecked() {
        return mToggleButton.isChecked();
    }

    public void setChecked(boolean checked) {
        mToggleButton.setChecked(checked);
    }

    public void setOnCheckedChangedListener(CompoundButton.OnCheckedChangeListener
            checkedChangedListener) {
        mExternalCheckedChangeListener = checkedChangedListener;
    }

    @OnClick(R.id.tv_type_public)
    public void choosePublicType() {
        mToggleButton.setChecked(false);
    }

    @OnClick(R.id.tvTypePrivate)
    public void choosePrivateType() {
        mToggleButton.setChecked(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mToggleButton.setEnabled(enabled);
        mTvTypePrivate.setEnabled(enabled);
        mTvTypePublic.setEnabled(enabled);
    }
}
