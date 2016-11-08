package smartassist.appreciate.be.smartassist.adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
public class PhotoPagerAdapter extends PagerAdapter
{
    private List<Photo> photos;

    public PhotoPagerAdapter()
    {
    }

    public void setPhotos(List<Photo> photos)
    {
        this.photos = photos;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return this.photos != null ? this.photos.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        Photo photo = this.photos != null && this.photos.size() > position ? this.photos.get(position) : null;

        if (photo != null)
        {
            ImageView imageView = new ImageView(container.getContext());
            int paddingHorizontal = (int) container.getContext().getResources().getDimension(R.dimen.photos_detail_arrow);
            imageView.setPadding(paddingHorizontal, 0, paddingHorizontal, 0);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            if(!TextUtils.isEmpty(photo.getImage()))
                Picasso.with(imageView.getContext())
                        .load(photo.getImage())
                        .into(imageView);

            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager) container).removeView((View) object);
    }
}
