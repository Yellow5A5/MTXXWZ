package com.meitu.mtxxwz.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.meitu.mtxxwz.activities.R;
import com.meitu.mtxxwz.dialog.InputContentDialog;
import com.meitu.mtxxwz.dialog.InputContentDialog.OnInputClickListener;
import com.meitu.mtxxwz.interfaces.OnUpdataControlBtnPostion;
import com.meitu.mtxxwz.util.CalculationTool;

public class BubbleView extends RelativeLayout {

  private int FLAG_DRAW_FINISH = 0;
  private View view;
  private Context mContext;
  private AutoAjustSizeTextView labelContent;
  private ImageView imgBubble;
  private RelativeLayout rlBubble;
  private int imgId;
  /** 屏幕宽高 */
  private int displayWidth, displayHeight;
  /** temp[0]为点击位置到图片左边界的距离，temp[1]为点击位置到图片上边界的距离 */
  private int[] temp = new int[] { 0, 0 };
  /** 记录按下时对应的屏幕坐标，以计算偏移 */
  private int downX = 0, downY = 0;
  /** 中心点坐标 */
  private int centerX, centerY;
  /** 当前的图片左上角XY坐标*/
  private int currentX,currentY;
  /** 无气泡模式时固定的宽度*/
  private int fixedTextWidth;
  private int currentWidth;
  private int currentHeight;
  /** 当前旋转角度 */
  private float currentRotation = 0;
  /** 当前大小比例 */
  private float currentScale = 1;
  /** 图片原宿的边角到中心距离 */
  private float vectorStart;
  private int[] bubbleId = {R.drawable.bubble00,R.drawable.bg01,R.drawable.bg02};
  private OnUpdataControlBtnPostion mOnUpdataControlBtnPostion;

  public void setOnUpdataControlBtnPostion(OnUpdataControlBtnPostion onUpdataControlBtnPostion) {
    this.mOnUpdataControlBtnPostion = onUpdataControlBtnPostion;
  }

  public BubbleView(Context context, int imgId) {
    super(context);
    this.mContext = context;
    this.imgId = imgId;
    view = LayoutInflater.from(context).inflate(R.layout.bubble_view, this, true);
    initParameterStart();
    initView();
    initSticker(imgId);
  }

  /**
   * 基本参数初始化化
   */
  private void initParameterStart() {
    displayWidth = getResources().getDisplayMetrics().widthPixels;
    displayHeight = getResources().getDisplayMetrics().heightPixels;
    fixedTextWidth = displayWidth / 4 * 3;// 初始化宽度
    currentWidth = fixedTextWidth;
    centerX = displayWidth / 2;// 屏幕中心位置
    centerY = displayHeight / 2;
  }
  
  private void initView() {
    rlBubble = (RelativeLayout) view.findViewById(R.id.rl_bubble);
    labelContent = (AutoAjustSizeTextView) view.findViewById(R.id.label_content);
    imgBubble = (ImageView) view.findViewById(R.id.img_bubble);
  }

  private void initSticker(int imgId) {// 未设置旋转比例！TODO
    if (imgId != bubbleId[0]) {// 表示第一个气泡图标为空。
      initBubbleMode(imgId);
    } else {
      initNoBubbleMode();
    }
    if (currentHeight != FLAG_DRAW_FINISH) {//View已更新
      updataAfterRotate(currentWidth, currentHeight);
    }
    vectorStart = (int) Math.sqrt(Math.pow(currentWidth / 2, 2)
        + Math.pow(currentHeight / 2, 2));
    this.imgId = imgId;
  }

  /**
   * 无气泡初始化贴纸
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN) 
  private void initBubbleMode(int imgId) {
    if (this.imgId == imgId) {
      return;
    }
    labelContent.setFixedFlag(false);// 多行模式
    Bitmap bm = BitmapFactory.decodeResource(getResources(), imgId);
    imgBubble.setImageResource(imgId);
    labelContent.setWidth(bm.getWidth());
    labelContent.setHeight(bm.getHeight());
    if (imgId == bubbleId[1]) {
      labelContent.setPaddingRelative(0, 0, 160, 0);
    }else {
      labelContent.setPaddingRelative(128, 0, 0, 0);
    }
    currentX = centerX - bm.getWidth() / 2;// 设置XY坐标
    currentY = centerY - bm.getHeight() / 2;
    this.setX(currentX);
    this.setY(currentY);
    currentWidth = bm.getWidth();
    currentHeight = bm.getHeight();
  }

  /**
   * 存在气泡初始化贴纸
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN) 
  private void initNoBubbleMode() {
    labelContent.setFixedFlag(true);// 单行模式
    imgBubble.setImageBitmap(null);
    labelContent.setPaddingRelative(0, 0, 0, 0);
    labelContent.setWidth(fixedTextWidth);
    currentWidth = fixedTextWidth;// 单行模式宽度固定
    currentHeight = labelContent.getcurrentHeight();
    Log.e("currentWidth  currentHeight", currentWidth +  "   " +  currentHeight);
    currentX = (int) (centerX - fixedTextWidth/ 2);// 设置XY坐标
    currentY = (int) (centerY - currentHeight/ 2);
    this.setX(currentX);
    this.setY(currentY);
    Log.e("currentX", "currentX:" + currentX + "  currentY:" + currentY);
  }

  /**
   * 更新旋转后的旋转图标位置，利用三角函数进行坐标的换算
   * @param perWidth  图片宽度
   * @param peiHeight 图片高度
   */
  private void updataAfterRotate(int perWidth, int perHeight) {
    float toCenter =(int) Math.sqrt(Math.pow(currentWidth*currentScale / 2, 2)
        + Math.pow(currentHeight*currentScale / 2, 2));
//    Log.e("perWidth / perHeight", perWidth + "   " +  perHeight + "");
    float degree = (float) (Math.atan((float)perWidth / (float)perHeight) / Math.PI * 180);
    float sumDegree = degree - currentRotation;
    int diffX = (int) (toCenter * Math.sin(sumDegree / 180f * Math.PI));
    int diffY = (int) (toCenter * Math.cos(sumDegree / 180f * Math.PI));
//    Log.e("currentRotation", currentRotation + "  " + sumDegree);
//    Log.e("diffX  diffY", diffX + "   " + diffY);
//    Log.e("sin ", Math.sin(sumDegree / 180f * Math.PI) + "");
//    Log.e("cos ", Math.cos(sumDegree / 180f * Math.PI) + "");
//    Log.e("degree ", degree + "");
    if (mOnUpdataControlBtnPostion != null) {
      mOnUpdataControlBtnPostion.setRotationBtnXY((int) (centerX + diffX),
          (int) (centerY + diffY));
    }
  }

  @Override
  public void onWindowFocusChanged(boolean hasWindowFocus) {
    super.onWindowFocusChanged(hasWindowFocus);
    initSticker(imgId);//TODO
  }
  
  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int x = (int) event.getRawX();
    int y = (int) event.getRawY();

    switch (event.getAction()) {
    case MotionEvent.ACTION_DOWN:
      downX = (int) event.getRawX();
      downY = (int) event.getRawY();
      temp[0] = (int) (x - this.getX());
      temp[1] = (int) (y - this.getY());
      System.out.println(getX() + " get " + getY());
      break;
    case MotionEvent.ACTION_MOVE:
      this.setTranslationX(x - temp[0]);
      this.setTranslationY(y - temp[1]);
      mOnUpdataControlBtnPostion.updataOnMoving(
          (int) (event.getRawX() - downX), (int) (event.getRawY() - downY));
      break;
    case MotionEvent.ACTION_UP:
      this.setTranslationX(x - temp[0]);
      this.setTranslationY(y - temp[1]);
      mOnUpdataControlBtnPostion.updataOnUp((int) (event.getRawX() - downX),
          (int) (event.getRawY() - downY));
      centerX += event.getRawX() - downX;
      centerY += event.getRawY() - downY;
      if (Math.abs(downX - event.getRawX()) < 10
          && Math.abs(downY - event.getRawY()) < 10) {
        showInputDiaLog();
      }
      break;
    default:
      break;
    }
    return true;
  }

  /**
   * 显示输入编辑框
   */
  private void showInputDiaLog() {
    InputContentDialog mInputContentDialog = new InputContentDialog(
        mContext, labelContent.getText().toString());
    mInputContentDialog.setOnInputClickListener(new OnInputClickListener() {
      @Override
      public void OnClickOK(String input) {
        if (!input.trim().equals("")) {
          labelContent.setText(input);
        } else {
          labelContent.setText("点击输入内容");
        }
      }
    });
    mInputContentDialog.show();
  }

  /**
   * 重新计算并设置当前缩放比例
   * 
   * @param nowX
   *          边界点X坐标
   * @param nowY
   *          边界点Y坐标
   */
  public void resetScale(float nowX, float nowY) {
    float vector = vectorToCenter(nowX, nowY);
    currentScale = vector / vectorStart;
    setScaleX(currentScale);
    setScaleY(currentScale);
  }

  /**
   * 根据点坐标计算点到贴纸中心的距离
   * 
   * @param X
   *          点的X坐标
   * @param Y
   *          点的Y坐标
   * @return 点到中心的距离
   */
  public float vectorToCenter(float X, float Y) {
    return CalculationTool.vectorToPoint(X, Y, centerX, centerY);
  }

  /**
   * 通过余弦定理计算出角度重置旋转
   * 
   * @param nowX
   *          当前旋转按钮的X坐标
   * @param nowY
   *          当前旋转按钮的Y坐标
   */
  public void resetRotation(float nowX, float nowY) {
    // vectorNow、vectorStart、vectorToPoint为三角形的三条边
    float vectorNow = vectorToCenter(nowX, nowY);
    float vectorToPoint = CalculationTool.vectorToPoint(nowX, nowY, centerX + currentWidth / 2,
        centerY + currentHeight / 2);
    double yuXianDingLi = (Math.pow(vectorStart, 2) + Math.pow(vectorNow, 2) - Math
        .pow(vectorToPoint, 2)) / (2 * vectorStart * vectorNow);
    if (yuXianDingLi > 0.995) {
      yuXianDingLi = 0.9999999;
    }
    while (yuXianDingLi < -1) {
      yuXianDingLi += 0.0001;
    }
    if (isPositionRotate(nowX, nowY)) {
      currentRotation = (int) CalculationTool.arccos(yuXianDingLi);
    } else {
      currentRotation = -(int) CalculationTool.arccos(yuXianDingLi);
    }
    setRotation(currentRotation);
    if (currentRotation == 0) {
      rlBubble.setBackgroundResource(R.drawable.border_img_degree_right);
    }else {
      rlBubble.setBackgroundResource(R.drawable.border_img_degree_wrong);
    }
  }

  /** 判断是否为正向旋转 */
  private boolean isPositionRotate(float nowX, float nowY) {
    float isPosition = ((-currentHeight / 2) * (nowX - centerX))
        - ((-currentWidth / 2) * (nowY - centerY));
    return isPosition > 0 ? true : false;
  }

  public String getText() {
    return String.valueOf(labelContent.getText());
  }

  public void setText(String input) {
    labelContent.setText(input);
  }

  public void setTextColor(int color) {
    labelContent.setTextColor(color);
  }

  public void setFontStyle(Typeface typeFace) {
    labelContent.setTypeface(typeFace);
  }

  public void setBold(boolean isBold) {
    TextPaint tp = labelContent.getPaint();
    tp.setFakeBoldText(isBold);
    labelContent.invalidate();
  }

  /**
   * 设置是否阴影
   * 
   * @param isShadow
   */
  public void setShadow(boolean isShadow) {
    if (isShadow) {
      labelContent.setShadowLayer(10f, 3f, 3f, Color.BLACK);
    } else {
      labelContent.setShadowLayer(0, 0, 0, Color.BLACK);
    }
  }

  public void setBubbleImg(int imgId) {
    initSticker(imgId);
  }

  public ImageView getBubbleView() {
    return imgBubble;
  }
}
