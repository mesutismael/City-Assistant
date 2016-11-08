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

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.News;
import smartassist.appreciate.be.smartassist.model.Weather;
import smartassist.appreciate.be.smartassist.model.api.CalendarEvent;
import smartassist.appreciate.be.smartassist.model.CalendarEventClean;
import smartassist.appreciate.be.smartassist.utils.CategoryHelper;
import smartassist.appreciate.be.smartassist.utils.DateUtils;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;
import smartassist.appreciate.be.smartassist.utils.WeatherHelper;
import smartassist.appreciate.be.smartassist.views.Button;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 23/02/2015.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private OnNewsClickListener listener;
    private List<CalendarEventClean> events;
    private List<Weather> weatherItems;
    private List<News> newsItems;

    private static final int TYPE_CALENDAR = 1;
    private static final int TYPE_WEATHER = 2;
    private static final int TYPE_NEWS = 3;

    public void setListener(OnNewsClickListener listener)
    {
        this.listener = listener;
    }

    public void setEvents(List<CalendarEventClean> events)
    {
        this.events = events;
        if(this.events != null && this.events.size() > 1)
            Collections.sort(this.events);
        this.notifyItemChanged(0);
    }

    public void setWeatherItems(List<Weather> weatherItems)
    {
        this.weatherItems = weatherItems;
        this.notifyItemChanged(1);
    }

    public void setNews(List<News> newsItems)
    {
        this.newsItems = newsItems;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position)
    {
        if(position == 0)
            return TYPE_CALENDAR;
        if(position == 1)
            return TYPE_WEATHER;
        return TYPE_NEWS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        switch (viewType)
        {
            case TYPE_CALENDAR:
                View viewCalendar = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_news_calendar, viewGroup, false);
                return new NewsCalendarViewHolder(viewCalendar);

            case TYPE_WEATHER:
                View viewWeather = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_news_weather, viewGroup, false);
                return new NewsWeatherViewHolder(viewWeather);

            case TYPE_NEWS:
                View viewNews = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_news, viewGroup, false);
                return new NewsViewHolder(viewNews);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
    {
        if(viewHolder instanceof NewsWeatherViewHolder)
        {
            ((NewsWeatherViewHolder) viewHolder).bind(this.weatherItems);
        }
        else if(viewHolder instanceof NewsCalendarViewHolder)
        {
            ((NewsCalendarViewHolder) viewHolder).bind(this.events);
        }
        else if (viewHolder instanceof NewsViewHolder)
        {
            News news = this.newsItems.get(i - 2);

            ((NewsViewHolder) viewHolder).bind(news, i);
        }
    }

    @Override
    public int getItemCount()
    {
        return this.newsItems != null ? this.newsItems.size() + 2 : 2;
    }

    public class NewsCalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView textViewTime1;
        private View viewHabitant1;
        private TextView textViewDescription1;
        private View viewDivider1;
        private TextView textViewTime2;
        private View viewHabitant2;
        private TextView textViewDescription2;
        private View viewDivider2;
        private TextView textViewTime3;
        private View viewHabitant3;
        private TextView textViewDescription3;

        public NewsCalendarViewHolder(View itemView)
        {
            super(itemView);

            this.textViewTime1 = (TextView) itemView.findViewById(R.id.textView_calendarTime1);
            this.viewHabitant1 = itemView.findViewById(R.id.view_habitant1);
            this.textViewDescription1 = (TextView) itemView.findViewById(R.id.textView_calendarDescription1);
            this.viewDivider1 = itemView.findViewById(R.id.view_divider1);
            this.textViewTime2 = (TextView) itemView.findViewById(R.id.textView_calendarTime2);
            this.viewHabitant2 = itemView.findViewById(R.id.view_habitant2);
            this.textViewDescription2 = (TextView) itemView.findViewById(R.id.textView_calendarDescription2);
            this.viewDivider2 = itemView.findViewById(R.id.view_divider2);
            this.textViewTime3 = (TextView) itemView.findViewById(R.id.textView_calendarTime3);
            this.viewHabitant3 = itemView.findViewById(R.id.view_habitant3);
            this.textViewDescription3 = (TextView) itemView.findViewById(R.id.textView_calendarDescription3);

            itemView.setOnClickListener(this);
        }

        public void bind(List<CalendarEventClean> events)
        {
            CalendarEventClean event1 = events != null && events.size() > 0 ? events.get(0) : null;
            CalendarEventClean event2 = events != null && events.size() > 1 ? events.get(1) : null;
            CalendarEventClean event3 = events != null && events.size() > 2 ? events.get(2) : null;

            if(event1 != null)
            {
                int level = event1.getLevel();
                int levelResource = level == CalendarEvent.LEVEL_HABITANT_1 ? R.drawable.shape_habitant_1 : level == CalendarEvent.LEVEL_HABITANT_2 ? R.drawable.shape_habitant_2 : R.drawable.shape_habitant_flat;
                this.textViewTime1.setText(DateUtils.formatNewsCalendar(event1.getStart()));
                this.viewHabitant1.setBackgroundResource(levelResource);
                this.textViewDescription1.setText(event1.getDescription());
            }

            if(event2 != null)
            {
                int level = event2.getLevel();
                int levelResource = level == CalendarEvent.LEVEL_HABITANT_1 ? R.drawable.shape_habitant_1 : level == CalendarEvent.LEVEL_HABITANT_2 ? R.drawable.shape_habitant_2 : R.drawable.shape_habitant_flat;
                this.textViewTime2.setText(DateUtils.formatNewsCalendar(event2.getStart()));
                this.viewHabitant2.setBackgroundResource(levelResource);
                this.textViewDescription2.setText(event2.getDescription());
            }

            if(event3 != null)
            {
                int level = event3.getLevel();
                int levelResource = level == CalendarEvent.LEVEL_HABITANT_1 ? R.drawable.shape_habitant_1 : level == CalendarEvent.LEVEL_HABITANT_2 ? R.drawable.shape_habitant_2 : R.drawable.shape_habitant_flat;
                this.textViewTime3.setText(DateUtils.formatNewsCalendar(event3.getStart()));
                this.viewHabitant3.setBackgroundResource(levelResource);
                this.textViewDescription3.setText(event3.getDescription());
            }

            viewDivider1.setVisibility(event2 != null ? View.VISIBLE : View.GONE);
            viewDivider2.setVisibility(event3 != null ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v)
        {
            if(NewsAdapter.this.listener != null)
                NewsAdapter.this.listener.onCalendarClick(v);
        }
    }

    public class NewsWeatherViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textViewLocation;
        private TextView textViewCurrentTemp;
        private TextView textViewDay1;
        private ImageView imageViewIcon1;
        private TextView textViewTemp1;
        private TextView textViewDay2;
        private ImageView imageViewIcon2;
        private TextView textViewTemp2;
        private TextView textViewDay3;
        private ImageView imageViewIcon3;
        private TextView textViewTemp3;
        private TextView textViewDay4;
        private ImageView imageViewIcon4;
        private TextView textViewTemp4;

        public NewsWeatherViewHolder(View itemView)
        {
            super(itemView);

            this.textViewLocation = (TextView) itemView.findViewById(R.id.textView_location);
            this.textViewCurrentTemp = (TextView) itemView.findViewById(R.id.textView_currentTemp);
            this.textViewDay1 = (TextView) itemView.findViewById(R.id.textView_day1);
            this.imageViewIcon1 = (ImageView) itemView.findViewById(R.id.imageView_icon1);
            this.textViewTemp1 = (TextView) itemView.findViewById(R.id.textView_temp1);
            this.textViewDay2 = (TextView) itemView.findViewById(R.id.textView_day2);
            this.imageViewIcon2 = (ImageView) itemView.findViewById(R.id.imageView_icon2);
            this.textViewTemp2 = (TextView) itemView.findViewById(R.id.textView_temp2);
            this.textViewDay3 = (TextView) itemView.findViewById(R.id.textView_day3);
            this.imageViewIcon3 = (ImageView) itemView.findViewById(R.id.imageView_icon3);
            this.textViewTemp3 = (TextView) itemView.findViewById(R.id.textView_temp3);
            this.textViewDay4 = (TextView) itemView.findViewById(R.id.textView_day4);
            this.imageViewIcon4 = (ImageView) itemView.findViewById(R.id.imageView_icon4);
            this.textViewTemp4 = (TextView) itemView.findViewById(R.id.textView_temp4);
        }

        public void bind(List<Weather> weatherItems)
        {
            Calendar day = Calendar.getInstance();
            Calendar day1 = DateUtils.getCalendar(0);
            Calendar day2 = DateUtils.getCalendar(1);
            Calendar day3 = DateUtils.getCalendar(2);
            Calendar day4 = DateUtils.getCalendar(3);

            this.textViewDay1.setText(DateUtils.formatWeatherDayOfWeek(day1.getTimeInMillis()));
            this.textViewDay2.setText(DateUtils.formatWeatherDayOfWeek(day2.getTimeInMillis()));
            this.textViewDay3.setText(DateUtils.formatWeatherDayOfWeek(day3.getTimeInMillis()));
            this.textViewDay4.setText(DateUtils.formatWeatherDayOfWeek(day4.getTimeInMillis()));

            if(weatherItems != null)
            {
                for(Weather item : weatherItems)
                {
                    day.setTimeInMillis(item.getDate());

                    if (DateUtils.isSameDay(day, day1))
                    {
                        this.textViewCurrentTemp.setText(this.textViewCurrentTemp.getContext().getString(R.string.weather_temperature_current, item.getCurrentTemp()));
                        this.imageViewIcon1.setImageResource(WeatherHelper.getWeatherIcon(item.getCode(), WeatherHelper.COLOR_DARK));
                        this.textViewTemp1.setText(this.textViewTemp1.getContext().getString(R.string.weather_temperature, item.getHigh()));
                    }
                    else if (DateUtils.isSameDay(day, day2))
                    {
                        this.imageViewIcon2.setImageResource(WeatherHelper.getWeatherIcon(item.getCode(), WeatherHelper.COLOR_GRAY));
                        this.textViewTemp2.setText(this.textViewTemp1.getContext().getString(R.string.weather_temperature, item.getHigh()));
                    }
                    else if (DateUtils.isSameDay(day, day3))
                    {
                        this.imageViewIcon3.setImageResource(WeatherHelper.getWeatherIcon(item.getCode(), WeatherHelper.COLOR_GRAY));
                        this.textViewTemp3.setText(this.textViewTemp1.getContext().getString(R.string.weather_temperature, item.getHigh()));
                    }
                    else if (DateUtils.isSameDay(day, day4))
                    {
                        this.imageViewIcon4.setImageResource(WeatherHelper.getWeatherIcon(item.getCode(), WeatherHelper.COLOR_GRAY));
                        this.textViewTemp4.setText(this.textViewTemp1.getContext().getString(R.string.weather_temperature, item.getHigh()));
                    }
                }
            }

            this.textViewLocation.setText(PreferencesHelper.getCity(this.itemView.getContext()));
        }
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

        private News news;

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

            this.buttonMore.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(News news, int position)
        {
            this.news = news;

            boolean landscape = this.itemView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            boolean large = (landscape && (position % 10 == 4 || position % 10 == 8)) || (!landscape && position % 3 == 2);
            boolean blue = (landscape && (position % 10 == 1 || position % 10 == 3 || position % 10 == 6 || position % 10 == 9)) ||  (!landscape && (position % 6 == 1 || position % 6 == 3));
            String image = CategoryHelper.getNewsPhoto(news, large);

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
            this.textViewTitle.setText(news.getTitle());
            this.textViewDescription.setText(news.getBody());
            this.textViewDescription.setMaxLines(large ? 4 : 2);

            if(DateUtils.isSameDay(news.getStartDate(), news.getEndDate()))
                this.textViewDate.setText(DateUtils.formatNewsDate(news.getStartDate()));
            else
                this.textViewDate.setText(this.textViewDate.getContext().getString(R.string.news_period, DateUtils.formatNewsDate(news.getStartDate()), DateUtils.formatNewsDate(news.getEndDate())));
        }

        @Override
        public void onClick(View v)
        {
            if(NewsAdapter.this.listener != null)
                NewsAdapter.this.listener.onNewsClick(v, this.news);
        }
    }

    public interface OnNewsClickListener
    {
        void onNewsClick(View caller, News news);
        void onCalendarClick(View caller);
    }
}
