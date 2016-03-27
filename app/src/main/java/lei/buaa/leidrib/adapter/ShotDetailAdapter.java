package lei.buaa.leidrib.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lei.buaa.leidrib.R;
import lei.buaa.leidrib.activity.AccountActivity;
import lei.buaa.leidrib.bean.Comment;
import lei.buaa.leidrib.bean.Shot;
import lei.buaa.leidrib.config.KeyConfig;
import lei.buaa.leidrib.utils.StringUtils;

/**
 * Created by lei on 3/21/16.
 * email: lileibh@gmail.com
 */
public class ShotDetailAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private Shot mShot;

    public ShotDetailAdapter(Context context, Shot shot) {
        this.mContext = context;
        this.mShot = shot;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View content = inflater.inflate(viewType, parent, false);
        if (viewType == R.layout.item_shot_stat) {
            return new ShotStatViewHolder(content);
        } else {
            return new CommentViewHolder(content);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return R.layout.item_shot_stat;
        else
            return R.layout.item_comment;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ShotStatViewHolder) {
            ShotStatViewHolder statHolder = (ShotStatViewHolder) holder;
            statHolder.mTvViewsCount.setText(String.valueOf(mShot.getViews_count()));
            statHolder.mTvComCount.setText(String.valueOf(mShot.getComments_count()));
            statHolder.mTvLikesCount.setText(String.valueOf(mShot.getLikes_count()));
            statHolder.mTvOwner.setText(mShot.getUser().getName());
            statHolder.mTvShotName.setText(mShot.getTitle());
            statHolder.mIvAvatar.setImageURI(Uri.parse(mShot.getUser().getAvatar_url()));
            statHolder.mIvAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoAccountActivity(mShot.getUser().getId());
                }
            });
        } else {
            CommentViewHolder commentHolder = (CommentViewHolder) holder;
            final Comment comment = mShot.getCommentList().get(position - 1);
            commentHolder.mTvComment.setText(StringUtils.formatHtml(comment.getBody()));
            commentHolder.mIvAvatar.setImageURI(Uri.parse(comment.getUser().getAvatar_url()));
            commentHolder.mTvName.setText(comment.getUser().getName());
            commentHolder.mIvAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoAccountActivity(comment.getUser_id());
                }
            });
        }
    }

    private void gotoAccountActivity(long userId) {
        Intent intent = new Intent(mContext, AccountActivity.class);
        intent.putExtra(KeyConfig.USER_ID, userId);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mShot == null)
            return 0;
        List<Comment> comments = mShot.getCommentList();
        //1 for stat
        return comments == null ? 1 : comments.size() + 1;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_avatar)
        SimpleDraweeView mIvAvatar;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_comment)
        TextView mTvComment;

        public CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ShotStatViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_comments_count)
        TextView mTvComCount;
        @Bind(R.id.tv_views_Count)
        TextView mTvViewsCount;
        @Bind(R.id.tv_likes_count)
        TextView mTvLikesCount;
        @Bind(R.id.iv_avatar)
        SimpleDraweeView mIvAvatar;
        @Bind(R.id.tv_owner)
        TextView mTvOwner;
        @Bind(R.id.tv_shot_name)
        TextView mTvShotName;

        public ShotStatViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
