package lei.buaa.leidrib.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lei.buaa.leidrib.R;
import lei.buaa.leidrib.activity.ShotDetailActivity;
import lei.buaa.leidrib.bean.Shot;
import lei.buaa.leidrib.config.KeyConfig;
import lei.buaa.leidrib.ui.ProgressCircleDrawable;

/**
 * Created by lei on 3/21/16.
 * email: lileibh@gmail.com
 */
public class ContentAdapter extends RecyclerView.Adapter {
    private List<Shot> mShots = null;
    private Context mContext = null;

    public ContentAdapter(Context context, List<Shot> shots) {
        this.mShots = shots;
        this.mContext = context;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return mShots.get(position).getId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View content = LayoutInflater.from(mContext).inflate(R.layout.item_content, parent, false);
        RecyclerView.ViewHolder viewHolder = new MyViewHolder(content);
        content.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Shot shot = mShots.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.mTvTitle.setText(shot.getTitle());
        if (shot.getImages() != null) {
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(mContext.getResources())
                    .setProgressBarImage(new ProgressCircleDrawable(mContext))
                    .build();
            myViewHolder.mIvContent.setHierarchy(hierarchy);
            myViewHolder.mIvContent.setImageURI(Uri.parse(shot.getImages().getNormal()));
        }
        myViewHolder.mTvGif.setVisibility(shot.getAnimated() ? View.VISIBLE : View.GONE);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShotDetailActivity.class);
                intent.putExtra(KeyConfig.SHOT_ID, mShots.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mShots != null)
            return mShots.size();
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_content)
        SimpleDraweeView mIvContent;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_gif)
        TextView mTvGif;

        public MyViewHolder(View content) {
            super(content);
            ButterKnife.bind(this, content);
        }
    }
}
