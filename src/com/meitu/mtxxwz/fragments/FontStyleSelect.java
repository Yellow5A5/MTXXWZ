package com.meitu.mtxxwz.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
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

public class FontStyleSelect extends Fragment {

  private View view;
  private GridView gVFontStyle;
  private int[] fontId = {R.drawable.font_default,R.drawable.font_thumb_fz_lt,R.drawable.font_thumb_fz_mwt,R.drawable.font_thumb_fz_zhjt,
                          R.drawable.font_thumb_fz_zhsrxt,R.drawable.font_thumb_hk_pop3};
  private String[] fontPath = {"","fonts/SnellRoundhand.ttf","fonts/Georgia.ttf","fonts/Avenir-BookOblique.ttf"};
  private int columnWidth = 120;
  private int columnPadding = 10;
  
  private OnFontStyleClickListener mOnFontStyleClickListener;
  
  public interface OnFontStyleClickListener{
    public void OnFontStyleClick(Typeface typeFace);
  }
  
  public void setOnFontStyleClickListener(OnFontStyleClickListener mOnFontStyleClickListener){
    this.mOnFontStyleClickListener = mOnFontStyleClickListener;
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fontstyle_select, container, false);
    initView();
    return view;
  }

  private void initView() {
    gVFontStyle = (GridView) view.findViewById(R.id.gv_font);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        fontId.length*(columnWidth+columnPadding),LinearLayout.LayoutParams.WRAP_CONTENT);
    gVFontStyle.setLayoutParams(params);
    gVFontStyle.setColumnWidth(columnWidth);
    gVFontStyle.setHorizontalSpacing(columnPadding);
    gVFontStyle.setStretchMode(GridView.NO_STRETCH);
    gVFontStyle.setNumColumns(fontId.length);
    gVFontStyle.setAdapter(new ImgAdapter(getActivity(), fontId));
    gVFontStyle.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position,
          long id) {
        Typeface typeFace = Typeface.SANS_SERIF;
        if (position <= 3 && position != 0) {
            typeFace =Typeface.createFromAsset(getActivity().getAssets(),fontPath[position]);
        }
        mOnFontStyleClickListener.OnFontStyleClick(typeFace);
      }
    });
  }

}
