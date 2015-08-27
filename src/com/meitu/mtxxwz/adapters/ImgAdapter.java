package com.meitu.mtxxwz.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.meitu.mtxxwz.activities.R;

public class ImgAdapter extends BaseAdapter {
  
  private Context mContext;
  private LayoutInflater mInflater;
  /** 传入带有文件名的List*/
  private int[] mDatas;

  public ImgAdapter(Context context, int[] mDatas) {
    mInflater = LayoutInflater.from(context);
    this.mContext = context;
    this.mDatas = mDatas;
  }

  @Override
  public int getCount() {
    return mDatas.length;
  }

  @Override
  public Object getItem(int position) {
    return mDatas[position];
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder = null;
    if (convertView == null) {
      convertView = mInflater.inflate(R.layout.select_item, parent, false);
      viewHolder = new ViewHolder();
      viewHolder.mTextView = (ImageView) convertView.findViewById(R.id.img_tool_item);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), mDatas[position]);
    
    viewHolder.mTextView.setImageBitmap(bm);
    return convertView;
  }

  private final class ViewHolder {
    ImageView mTextView;
  }

}
