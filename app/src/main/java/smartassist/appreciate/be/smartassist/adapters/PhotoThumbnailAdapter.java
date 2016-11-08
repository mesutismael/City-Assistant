package smartassist.appreciate.be.smartassist.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.Photo;

/**
 * Created by Inneke on 3/03/2015.
 */
public class PhotoThumbnailAdapter extends RecyclerView.Adapter<PhotoThumbnailAdapter.ThumbnailViewHolder>
{
    private List<Photo> photos;
    private OnThumbnailClickListener listener;

    public PhotoThumbnailAdapter()
    {
    }

    public void setPhotos(List<Photo> photos)
    {
        this.photos = photos;
        this.notifyDataSetChanged();
    }

    public void setListener(OnThumbnailClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public ThumbnailViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_photo, viewGroup, false);
        return new ThumbnailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ThumbnailViewHolder viewHolder, int i)
    {
        Photo photo = this.photos.get(i);

        viewHolder.bind(photo, i);
    }

    @Override
    public int getItemCount()
    {
        return this.photos != null ? this.photos.size() : 0;
    }

    public class ThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView imageViewThumbnail;
        private int position;

        public ThumbnailViewHolder(View itemView)
        {
            super(itemView);

            this.imageViewThumbnail = (ImageView) itemView.findViewById(R.id.imageView_thumbnail);

            this.imageViewThumbnail.setOnClickListener(this);
        }

        public void bind(Photo photo, int position)
        {
            this.position = position;

            if(!TextUtils.isEmpty(photo.getThumbnail()))
                Picasso.with(this.imageViewThumbnail.getContext())
                        .load(photo.getThumbnail())
                        .into(this.imageViewThumbnail);
        }

        @Override
        public void onClick(View v)
        {
            if(PhotoThumbnailAdapter.this.listener != null)
                PhotoThumbnailAdapter.this.listener.onThumbnailClick(v, this.position);
        }
    }

    public interface OnThumbnailClickListener
    {
        void onThumbnailClick(View caller, int position);
    }
}
