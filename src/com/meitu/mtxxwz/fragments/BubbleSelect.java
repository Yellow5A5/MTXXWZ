package com.meitu.mtxxwz.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.meitu.mtxxwz.activities.R;
import com.meitu.mtxxwz.adapters.ImgAdapter;
import com.meitu.mtxxwz.interfaces.OnWordSelectListener;

public class BubbleSelect extends Fragment{

  private View view;
  private GridView gVBubbleSelect;
  private int columnWidth = 120;
  private int columnPadding = 10;
  private int[] bubbleId = {R.drawable.bubble00,R.drawable.bg01,R.drawable.bg02,
      R.drawable.bubble03,R.drawable.bubble04,R.drawable.bubble05,R.drawable.bubble06,
      R.drawable.bubble07,R.drawable.bubble08,R.drawable.bubble09,R.drawable.bubble10};
  private int[] thumbnailbbubbleId = {R.drawable.bubble00,R.drawable.thumbnail01,R.drawable.thumbnail02,
      R.drawable.bubble03,R.drawable.bubble04,R.drawable.bubble05,R.drawable.bubble06,
      R.drawable.bubble07,R.drawable.bubble08,R.drawable.bubble09,R.drawable.bubble10};

  public OnWordSelectListener mOnWordSelectListener;
  
  public void setOnWordSelectListener(OnWordSelectListener mOnWordSelectListener){
    this.mOnWordSelectListener = mOnWordSelectListener;
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.bubble_select, container, false);
    initView();
    return view;
  }
  
  private void initView() {
    gVBubbleSelect = (GridView) view.findViewById(R.id.gv_bubble);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        (columnWidth+columnPadding)*bubbleId.length,LinearLayout.LayoutParams.MATCH_PARENT);
    gVBubbleSelect.setLayoutParams(params);
    gVBubbleSelect.setColumnWidth(columnWidth);
    gVBubbleSelect.setHorizontalSpacing(columnPadding);
    gVBubbleSelect.setStretchMode(GridView.NO_STRETCH);//不拉伸的模式填充
    gVBubbleSelect.setNumColumns(bubbleId.length);
    gVBubbleSelect.setAdapter(new ImgAdapter(getActivity(), thumbnailbbubbleId));
    gVBubbleSelect.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position,
          long id) {
        if (position < 3) {
        mOnWordSelectListener.OnClick(view,bubbleId[position]);
        }
      }
    });
  }
}
