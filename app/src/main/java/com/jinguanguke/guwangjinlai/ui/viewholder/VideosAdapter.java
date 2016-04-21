package com.jinguanguke.guwangjinlai.ui.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jinguanguke.guwangjinlai.R;
import com.jinguanguke.guwangjinlai.model.entity.ImageInfo;
import com.jinguanguke.guwangjinlai.ui.view.RadioImageView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by jin on 16/4/13.
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHodler> {

    private Context mContext;
    private List<ImageInfo> imageInfos;
    @Bind(R.id.tv_title)
    TextView titleView;
    private OnVideoClickListener onVideoClickListener;

    public VideosAdapter(Context mContext, List<ImageInfo> imageInfos) {
        this.mContext = mContext;
        this.imageInfos = imageInfos;
    }

    @Override
    public VideosViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.videos_layout, parent, false);
        return new VideosViewHodler(view);
    }

    @Override
    public void onBindViewHolder(VideosViewHodler holder, int position) {

        loadImage(holder, imageInfos.get(position));
        //ImageInfo info = imageInfos.get(position);
        // Log.i("info", info.toString());

    }

    private void loadImage(VideosViewHodler holder, ImageInfo imageInfo) {
        holder.ivGril.setOriginalSize(imageInfo.getWidth(), imageInfo.getHeight());
        int limit = 48;
        String text = imageInfo.getTitle().length() > limit ? imageInfo.getTitle().substring(0, limit) +
                "..." : imageInfo.getTitle();

        holder.title.setText(text);
        Glide.with(mContext)
                .load(imageInfo.getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                .into(holder.ivGril);
    }

    @Override
    public int getItemCount() {
        return imageInfos.size();
    }

    class VideosViewHodler extends RecyclerView.ViewHolder {

        private RadioImageView ivGril;
        private TextView title;

        public VideosViewHodler(View itemView) {
            super(itemView);
            ivGril = (RadioImageView) itemView.findViewById(R.id.iv_gril);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onVideoClickListener.onVideoClick(v, getAdapterPosition());
                }
            });
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    onVideoClickListener.onVideoLongClick(v,getAdapterPosition());
//                    return true;
//                }
//            });
        }
    }

    public void setOnVideoClickListener(OnVideoClickListener listener) {
        this.onVideoClickListener = listener;
    }

}
