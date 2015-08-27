package com.meitu.mtxxwz.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.meitu.mtxxwz.activities.R;

public class WordStyleSelect extends Fragment {

  private View view;
  private ImageView imgBoldWord;
  private ImageView imgShadowWord;
  private ImageView imgPinYinWord;
  private SeekBar seekBarColorSelect;
  
  private boolean boldSwitch = false;
  private boolean shadowSwitch = true;
  private boolean pinyinSwitch = false;
  private OnWordStyleClickListener mOnWordStyleClickListener;
  
  public interface OnWordStyleClickListener{
    public void OnBoldClick(boolean isOpen);
    public void OnShadowClick(boolean isOpen);
    public void OnColorSelect(int color);
  }
  
  public void setOnWordStyleClickListener(OnWordStyleClickListener mOnWordStyleClickListener){
    this.mOnWordStyleClickListener = mOnWordStyleClickListener;
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.wordstyle_select, container, false);
    initView();
    initBtnListener();
    initSeekListener();
    return view;
  }

  private void initView() {
    imgBoldWord = (ImageView) view.findViewById(R.id.img_bold_switch);
    imgShadowWord = (ImageView) view.findViewById(R.id.img_shadow_switch);
    imgPinYinWord = (ImageView) view.findViewById(R.id.img_pinyin_switch);
    seekBarColorSelect = (SeekBar) view.findViewById(R.id.seekbar_color_select);
  }
  
  private void initBtnListener() {
    OnClickListener wordOnClickListener = new OnClickListener() {
      @Override
      public void onClick(View v) {
        switch (v.getId()) {
        case R.id.img_bold_switch:
          boldSwitch = updataSwitch(boldSwitch, imgBoldWord);
          mOnWordStyleClickListener.OnBoldClick(boldSwitch);
          break;
        case R.id.img_shadow_switch:
          shadowSwitch = updataSwitch(shadowSwitch, imgShadowWord);
          mOnWordStyleClickListener.OnShadowClick(shadowSwitch);
          break;
        case R.id.img_pinyin_switch:
          pinyinSwitch = updataSwitch(pinyinSwitch, imgPinYinWord);
          break;

        default:
          break;
        }
      }
    };
    imgBoldWord.setOnClickListener(wordOnClickListener);
    imgShadowWord.setOnClickListener(wordOnClickListener);
    imgPinYinWord.setOnClickListener(wordOnClickListener);
  }

  private void initSeekListener() {
    seekBarColorSelect.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
      }
      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        
      }
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.e("progress", progress + "");
        int color = 0;
        if (progress > 50 && progress <= 100) {
          color = (int) (0Xffffff * ((progress-50)/50f)) + 0Xff000000;
        }else {
          color = (int) (0Xffff * (progress/50f)) + 0Xffff0000;
        }
        mOnWordStyleClickListener.OnColorSelect(color);
      }
    });
  }

  private boolean updataSwitch(boolean isOpen, ImageView img) {
    if (isOpen) {
      img.setImageResource(R.drawable.close_icon);
    }else {
      img.setImageResource(R.drawable.open_icon);
    }
    return !isOpen;
  }
}
