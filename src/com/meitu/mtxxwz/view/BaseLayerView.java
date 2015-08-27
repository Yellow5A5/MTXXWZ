package com.meitu.mtxxwz.view;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.meitu.mtxxwz.activities.R;
import com.meitu.mtxxwz.interfaces.OnUpdataControlBtnPostion;
import com.meitu.mtxxwz.util.SaveTool;

public class BaseLayerView extends RelativeLayout{

  private View view;
  private RelativeLayout rlSavePicture;
  private ImageView imgDisplay;
  private ImageView imgControl;
  private BubbleView currentBubble;
  private RelativeLayout rlBubbleContainer;
  /** 记录按下时对应的屏幕坐标，以计算偏移*/
  private int[] temp = new int[] { 0, 0 };
  private int rotationBtnX;
  private int rotationBtnY;
  
  public BaseLayerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    view = LayoutInflater.from(context).inflate(R.layout.baselayer, this, true);
    initView();
  }
  
  public void setDisplay(Uri uri){
    imgDisplay.setImageURI(uri);
  }
  
  private void initListener() {
    currentBubble.setOnUpdataControlBtnPostion(new OnUpdataControlBtnPostion() {
      @Override
      public void updataOnMoving(int x, int y) {
        BaseLayerView.this.setControlBtnPosition(rotationBtnX + x, rotationBtnY + y);
      }
      @Override
      public void updataOnUp(int x, int y) {
        rotationBtnX = rotationBtnX + x;
        rotationBtnY = rotationBtnY + y;
      }
      @Override
      public void setRotationBtnXY(int X, int Y) {
        rotationBtnX = X - imgControl.getWidth()/2;
        rotationBtnY = Y - imgControl.getHeight()/2;
        BaseLayerView.this.setControlBtnPosition(rotationBtnX, rotationBtnY);
      }
    });
  }

  private void initView() {
    rlSavePicture = (RelativeLayout) view.findViewById(R.id.rl_save_picture);
    imgDisplay = (ImageView) view.findViewById(R.id.img_display);
    imgControl = (ImageView) view.findViewById(R.id.img_control);
    rlBubbleContainer = (RelativeLayout) view.findViewById(R.id.bubble_container);
    imgControl.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {//TODO
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            temp[0] = (int) (x - v.getX());
            temp[1] = (int) (y - v.getY());
            Log.e("x+y", x+"  "+y);
            System.out.println(getX()+" get " + getY());
            break;
          case MotionEvent.ACTION_MOVE:
            rotationBtnX = x - temp[0];
            rotationBtnY = y - temp[1];
            v.setTranslationX(rotationBtnX);
            v.setTranslationY(rotationBtnY);
            currentBubble.resetScale(x - temp[0] + v.getWidth()/2, y - temp[1] + v.getHeight()/2);
            currentBubble.resetRotation(v.getX() + v.getWidth()/2, v.getY() + v.getHeight()/2);
            break;
          case MotionEvent.ACTION_UP:
            break;
          default:
            break;
        }
        return true;
      }
    });
  }
  
  
  public void savePicture(){
    rlSavePicture.setDrawingCacheEnabled(true);
    Bitmap bitmap = rlSavePicture.getDrawingCache();
    try {
      SaveTool.saveMyBitmap(bitmap);
      Toast.makeText(getContext(), "成功保存！",Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
      Toast.makeText(getContext(), "保存失败。",Toast.LENGTH_SHORT).show();
      e.printStackTrace();
    }
  }
  /**
   * 设置旋转按钮位置
   * @param x
   * @param y
   */
  public void setControlBtnPosition(int x, int y){
    imgControl.setX(x);
    imgControl.setY(y);
  }
  
  /**
   * 设置阴影
   * @param isShadow
   */
  public void setTextShadow(boolean isShadow){
    if (currentBubble != null) {
      currentBubble.setShadow(isShadow);
    }
  }
  
  /**
   * 设置粗体
   * @param isBold
   */
  public void setTextBold(boolean isBold){
    if (currentBubble != null) {
      currentBubble.setBold(isBold);
    }
  }
  
  /**
   * 设置字体颜色
   * @param color
   */
  public void setTextColor(int color){
    currentBubble.setTextColor(color);
  }
  
  /**
   * 设置字体类型
   * @param typeFace
   */
  public void setTextFontStyle(Typeface typeFace){
    if (currentBubble != null) {
      currentBubble.setFontStyle(typeFace);
    }
  }

  /**
   * 添加气泡
   * @param bubbleView
   */
  public void addBubble(BubbleView bubbleView){
    rlBubbleContainer.addView(bubbleView);
    currentBubble = bubbleView;
    initListener();
  }

  /**
   * 设置气泡图片
   * @param imgId
   */
  public void setBubbleimg(int imgId){
    currentBubble.setBubbleImg(imgId);
  }
}
