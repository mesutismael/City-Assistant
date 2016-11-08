package smartassist.appreciate.be.smartassist.adapters;

import android.content.res.Configuration;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.ResidentNewsItem;
import smartassist.appreciate.be.smartassist.utils.DateUtils;
import smartassist.appreciate.be.smartassist.views.Button;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by banada ismael on 25/10/2016.
 */
public class ResidentNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private OnNewsClickListener listener;
    private List<ResidentNewsItem> residentNewsItem;
    private static final int TYPE_NEWS = 1;

    public void setListener(OnNewsClickListener listener)
    {
        this.listener = listener;
    }


    public void setNews(List<ResidentNewsItem> residentNewsItem)
    {
        this.residentNewsItem = residentNewsItem;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position)
    {
        return TYPE_NEWS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        switch (viewType)
        {

            case TYPE_NEWS:
                View viewNews = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_resident_news, viewGroup, false);
                return new NewsViewHolder(viewNews);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
    {
       if (viewHolder instanceof NewsViewHolder)
        {
                ResidentNewsItem residentNewsItem = this.residentNewsItem.get(i);

                ((NewsViewHolder) viewHolder).bind(residentNewsItem, i);

        }
    }

    @Override
    public int getItemCount()
    {
        return this.residentNewsItem != null ? this.residentNewsItem.size() : 0;
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CardView cardView;
        private ImageView imageViewLarge;
        private ImageView imageViewSmall;
        private ImageView imageViewIcon;
        private TextView textViewDate;
        private TextView textViewTitle;
        private TextView textViewDescription;
        private Button buttonMore;
        private Button button_downloadFile;

        private ResidentNewsItem residentNewsItem;

        public NewsViewHolder(View itemView)
        {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardView_news);
            this.imageViewLarge = (ImageView) itemView.findViewById(R.id.imageView_large);
            this.imageViewSmall = (ImageView) itemView.findViewById(R.id.imageView_small);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView_icon);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textView_date);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textView_title);
            this.textViewDescription = (TextView) itemView.findViewById(R.id.textView_description);
            this.buttonMore = (Button) itemView.findViewById(R.id.button_more);
            this.button_downloadFile=(Button)itemView.findViewById(R.id.button_fileDownload);

            this.buttonMore.setOnClickListener(this);
            this.button_downloadFile.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(ResidentNewsItem residentNewsItem, int position)
        {
            this.residentNewsItem = residentNewsItem;

            boolean landscape = this.itemView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            boolean large = (landscape && (position % 10 == 4 || position % 10 == 8)) || (!landscape && position % 3 == 2);
            boolean blue = (landscape && (position % 10 == 1 || position % 10 == 3 || position % 10 == 6 || position % 10 == 9)) ||  (!landscape && (position % 6 == 1 || position % 6 == 3));
            String image = residentNewsItem.getFile();

            int colorWhite = ContextCompat.getColor(this.itemView.getContext(), R.color.general_text_white);
            int colorBlue = ContextCompat.getColor(this.itemView.getContext(), R.color.general_theme);
            int colorText = ContextCompat.getColor(this.itemView.getContext(), R.color.general_text);

            if(large)
            {
                this.imageViewLarge.setVisibility(!TextUtils.isEmpty(image) ? View.VISIBLE : View.GONE);
                this.imageViewSmall.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(image))
                    Picasso.with(this.imageViewLarge.getContext())
                            .load(image)
                            .into(this.imageViewLarge);
            }
            else
            {
                this.imageViewLarge.setVisibility(View.GONE);
                this.imageViewSmall.setVisibility(!TextUtils.isEmpty(image) ? View.VISIBLE : View.GONE);
                if(!TextUtils.isEmpty(image))
                    Picasso.with(this.imageViewSmall.getContext())
                            .load(image)
                            .into(this.imageViewSmall);
            }

            this.cardView.setCardBackgroundColor(blue ? colorBlue : colorWhite);
            this.textViewDate.setTextColor(blue ? colorWhite : colorText);
            this.textViewTitle.setTextColor(blue ? colorWhite : colorText);
            this.textViewDescription.setTextColor(blue ? colorWhite : colorText);
            this.imageViewIcon.setImageResource(blue ? R.drawable.ic_mail_white : R.drawable.ic_mail_blue);
            this.buttonMore.setBackgroundResource(blue ? R.drawable.selector_button_dialog : R.drawable.selector_button);
            this.buttonMore.setTextColor(ContextCompat.getColorStateList(this.buttonMore.getContext(), blue ? R.color.selector_button_dialog_text : R.color.selector_button_text));
            this.textViewTitle.setText(residentNewsItem.getTitle());
            this.textViewDescription.setText(residentNewsItem.getBody());
            this.textViewDescription.setMaxLines(large ? 4 : 2);
            this.textViewDate.setText(DateUtils.formatNewsDate(residentNewsItem.getCreateDate()));
            this.button_downloadFile.setVisibility(residentNewsItem.getFile()!=null? View.VISIBLE: View.GONE);

        }

        @Override
        public void onClick(View v)
        {
            if(ResidentNewsAdapter.this.listener != null)
                ResidentNewsAdapter.this.listener.onNewsClick(v, this.residentNewsItem);
        }
    }

    public interface OnNewsClickListener
    {
        void onNewsClick(View caller, ResidentNewsItem residentNewsItem);
    }
}
