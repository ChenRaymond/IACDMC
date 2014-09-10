package com.iac.dmc;

import com.iac.dmc.engine.MediaItem;
import com.iac.dmc.util.DLNAUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ContentAdapter extends BaseAdapter{

    private  List<MediaItem> contentItem;
    private LayoutInflater mInflater;
    private Drawable foldIcon;
    private Drawable musicIcon;
    private Drawable picIcon;
    private Drawable videoIcon;


    public ContentAdapter(Context context, List<MediaItem>  contentItem) {
        mInflater = LayoutInflater.from(context);
        this.contentItem = contentItem;

        Resources res = context.getResources();
        foldIcon = res.getDrawable(R.drawable.ic_menu_archive);
        musicIcon = res.getDrawable(R.drawable.tab_icon_music);
        picIcon = res.getDrawable(R.drawable.tab_icon_pic);
        videoIcon = res.getDrawable(R.drawable.tab_icon_video);
    }

    public void refreshData(List<MediaItem>  contentItem)
    {
        this.contentItem = contentItem;
        notifyDataSetChanged();
    }

    public void clear()
    {
        if (contentItem != null){
            contentItem.clear();
            notifyDataSetChanged();
        }
    }
    /**
     * The number of items in the list is determined by the number of
     * speeches in our array.
     * 
     * @see android.widget.ListAdapter#getCount()
     */
    @Override
    public int getCount() {
        return contentItem.size();
    }

    /**
     * Since the data comes from an array, just returning the index is
     * sufficent to get at the data. If we were using a more complex data
     * structure, we would return whatever object represents one row in the
     * list.
     * 
     * @see android.widget.ListAdapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        return contentItem.get(position);
    }

    /**
     * Use the array index as a unique id.
     * 
     * @see android.widget.ListAdapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Make a view to hold each row.
     * 
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.content_list_item, null);
        }

        MediaItem dataItem = (MediaItem) getItem(position);
        TextView tvContent = (TextView)convertView.findViewById(R.id.tv_content);
        tvContent.setText(dataItem.getTitle());

        ImageView icon = (ImageView) convertView.findViewById(R.id.imageView);
        if (DLNAUtil.isAudioItem(dataItem)){
            icon.setBackgroundDrawable(musicIcon);
        }else if (DLNAUtil.isVideoItem(dataItem)){
            icon.setBackgroundDrawable(videoIcon);
        }else if (DLNAUtil.isPictureItem(dataItem)){
            icon.setBackgroundDrawable(picIcon);
        }else{
            icon.setBackgroundDrawable(foldIcon);
        }


        return convertView;
    }
}
