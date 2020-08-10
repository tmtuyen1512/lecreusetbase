package com.delfi.xmobile.lib.lecreusetbase.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.delfi.xmobile.lib.lecreusetbase.R;
import com.delfi.xmobile.lib.lecreusetbase.model.Language;

import java.util.List;

/**
 * Created by DANHPC on 6/1/2017.
 */

public class LanguageAdapter extends BaseAdapter {
    private List<Language> langArray;

    public LanguageAdapter(List<Language> objects) {
        this.langArray = objects;
    }

    @Override
    public int getCount() {
        return langArray.size();
    }

    @Override
    public Object getItem(int i) {
        return langArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_language_item, null);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.txtitem);
            holder.imgIcon = convertView.findViewById(R.id.imgitem);
            holder.imgCheckitem = convertView.findViewById(R.id.chkitem);

            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(langArray.size()>0 && position >= 0)
        {
            final Language lang = langArray.get(position);

            holder.tvName.setText(lang.toString());
            holder.imgIcon.setImageResource(lang.getResource());
            if(lang.isSelected())
                holder.imgCheckitem.setImageResource(R.mipmap.ico_done_blue);

        }
        return convertView;
    }

    private class ViewHolder{
        public TextView tvName;
        public ImageView imgIcon;
        public ImageView imgCheckitem;
    }
}
