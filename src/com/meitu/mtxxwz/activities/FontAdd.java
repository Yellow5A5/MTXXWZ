package com.meitu.mtxxwz.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meitu.mtxxwz.fragments.BubbleSelect;
import com.meitu.mtxxwz.fragments.FontStyleSelect;
import com.meitu.mtxxwz.fragments.FontStyleSelect.OnFontStyleClickListener;
import com.meitu.mtxxwz.fragments.WordStyleSelect;
import com.meitu.mtxxwz.fragments.WordStyleSelect.OnWordStyleClickListener;
import com.meitu.mtxxwz.interfaces.OnWordSelectListener;
import com.meitu.mtxxwz.view.BaseLayerView;
import com.meitu.mtxxwz.view.BubbleView;

/**
 * FontAdd
 * @author HuangWeiwu <br/>
 * @功能  仿美图秀秀文字编辑功能
 * @时间 2015.08.15
*/

public class FontAdd extends Activity {

  private LinearLayout btnBubble;
  private LinearLayout btnFont;
  private LinearLayout btnWord;
  private ImageView btnUnsave;
  private ImageView btnSave;
  
  private BubbleSelect bubbleSelect;
  private FontStyleSelect fontStyleSelect;
  private WordStyleSelect wordStyleSelect;
  private BaseLayerView baseControl;
  
  private Fragment currentPagerSelect;
  private FragmentManager fm;
  private int currentImgId = R.drawable.bubble00;//TODO
  private OnClickListener onClickListener;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.font_operate);
    fm = getFragmentManager();
    initListener();
    initView();
    initFragment();
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    Uri uri = (Uri) bundle.get("uri");
    baseControl.setDisplay(uri);
  }

  private void initView() {
    btnBubble = (LinearLayout) findViewById(R.id.btn_bubble);
    btnFont = (LinearLayout) findViewById(R.id.btn_font);
    btnWord = (LinearLayout) findViewById(R.id.btn_word);
    btnUnsave = (ImageView) findViewById(R.id.btn_unsave);
    btnSave = (ImageView) findViewById(R.id.btn_save);
    baseControl = (BaseLayerView) findViewById(R.id.base_layer);
    baseControl.addBubble(new BubbleView(FontAdd.this, currentImgId));
    baseControl.setTextShadow(true);
    
    btnBubble.setOnClickListener(onClickListener);
    btnFont.setOnClickListener(onClickListener);
    btnWord.setOnClickListener(onClickListener);
    btnUnsave.setOnClickListener(onClickListener);
    btnSave.setOnClickListener(onClickListener);
    baseControl.setOnClickListener(onClickListener);
  }
  
  private void initFragment() {
    bubbleSelect = new BubbleSelect();
    fontStyleSelect = new FontStyleSelect();
    wordStyleSelect = new WordStyleSelect();
    fm.beginTransaction().add(R.id.bottom_fragment, bubbleSelect).hide(bubbleSelect).commit();
    fm.beginTransaction().add(R.id.bottom_fragment, fontStyleSelect).hide(fontStyleSelect).commit();
    fm.beginTransaction().add(R.id.bottom_fragment, wordStyleSelect).hide(wordStyleSelect).commit();
    currentPagerSelect = bubbleSelect;
    bubbleSelect.setOnWordSelectListener(new OnWordSelectListener() {
      @Override
      public void OnClick(View mView, int imgID) {
          baseControl.setBubbleimg(imgID);
      }
    });
    fontStyleSelect.setOnFontStyleClickListener(new OnFontStyleClickListener() {
      @Override
      public void OnFontStyleClick(Typeface typeFace) {
        baseControl.setTextFontStyle(typeFace);
      }
    });
    wordStyleSelect.setOnWordStyleClickListener(new OnWordStyleClickListener() {
      @Override
      public void OnShadowClick(boolean isOpen) {
        baseControl.setTextShadow(isOpen);
      }
      @Override
      public void OnBoldClick(boolean isOpen) {
        baseControl.setTextBold(isOpen);
      }
      @Override
      public void OnColorSelect(int color) {
        baseControl.setTextColor(color);
      }
    });
  }
  
  private void initListener() {
    onClickListener = new OnClickListener() {
      @Override
      public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_bubble:
          changeFragment(bubbleSelect);
          break;
        case R.id.btn_font:
          changeFragment(fontStyleSelect);
          break;
        case R.id.btn_word:
           changeFragment(wordStyleSelect);
          break;
        case R.id.btn_unsave:
          finish();
          break;
        case R.id.btn_save:
//          StickerView.PreSticker.RemoveBoder();
          baseControl.savePicture();
          break;
        case R.id.base_layer:
          if (!currentPagerSelect.isHidden()) {
            fm.beginTransaction().hide(currentPagerSelect).commit();
          }
          break;
        default:
          break;
        }
      }
    };
  }
  
  private void changeFragment(Fragment to){//TODO 代码简化一下。
      if (currentPagerSelect != to) {//点击的若不是同一个按钮
        fm.beginTransaction().show(to).hide(currentPagerSelect).commit();//隐藏现在的fragment
      }else {
        if (to.isHidden()) {
          fm.beginTransaction().show(to).commit();
        }else {
          fm.beginTransaction().hide(to).commit();//隐藏现在的fragment
        }
      }
    currentPagerSelect = to;
  }
}
