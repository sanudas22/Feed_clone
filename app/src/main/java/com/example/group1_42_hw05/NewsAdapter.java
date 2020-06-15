package com.example.group1_42_hw05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        News news = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_custom, parent, false);
            holder = new ViewHolder();
            holder.author = convertView.findViewById(R.id.author);
            holder.date = convertView.findViewById(R.id.date);
            holder.newsTitle = convertView.findViewById(R.id.newsTitle);
            holder.urlToImage = convertView.findViewById(R.id.urlToImage);
            holder.description = convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.author.setText(news.author);
        holder.newsTitle.setText(news.title);
        holder.date.setText(news.publishedAt);
        Picasso.get().load(news.urlToImage).into(holder.urlToImage);
        holder.description.setText("Description:  "+news.description);
        return convertView;
    }

    private static class ViewHolder{
        ImageView urlToImage;
        TextView newsTitle, author, date, description;
    }
}

