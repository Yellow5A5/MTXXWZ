package com.meitu.mtxxwz.view;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

public class AutoAjustSizeTextView extends TextView
{
  private static float DEFAULT_MIN_TEXT_SIZE = 10;
  private static float DEFAULT_MAX_TEXT_SIZE = 16;

  private Paint testPaint;
  private float minTextSize, maxTextSize;
  private boolean isFixedOneLine = true;//设置是否固定为一行。
  private int textLength;
  private int currentHeight;
  private int currentWidth;
  
  public AutoAjustSizeTextView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    initialise();
  }

  private void initialise()
  {
    testPaint = new Paint();
    testPaint.set(this.getPaint());
    maxTextSize = this.getTextSize();
    if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE)
    {
      maxTextSize = DEFAULT_MAX_TEXT_SIZE;
    }
    minTextSize = DEFAULT_MIN_TEXT_SIZE;
  };

  /**
   * 多行数调整字体大小
   * @param text
   * @param textWidth
   */
  private void refitText(String text, int textWidth)
  {
    if (textWidth > 0)
    {
      int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
      float trySize = maxTextSize;
      testPaint.setTextSize(trySize);
      while ((trySize > minTextSize) && (testPaint.measureText(text) > availableWidth))
      {
        trySize -= 1;
        if (trySize <= minTextSize)
        {
          trySize = minTextSize;
          break;
        }
        testPaint.setTextSize(trySize);
      }
      this.setTextSize(trySize);
    }
  };

  /**
   * 单行数调整字体大小
   * @param text
   * @param width
   */
  public void reSize(String text,int width){
    try {
      textLength = text.getBytes("GBK").length;//字符数
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    currentWidth = width;
    setWidth(width);
    float singleWidth = width/(textLength+1)*2;
    setTextSize(TypedValue.COMPLEX_UNIT_PX, singleWidth);//以PX为单位设置字符 大小
    currentHeight = (int) singleWidth*2;
    setHeight(currentHeight);//调整高度
  }
  
  public void setFixedFlag(boolean isFixedOneLine){
    this.isFixedOneLine = isFixedOneLine;
    if (isFixedOneLine) {
      reSize(getText().toString(), currentWidth);
      Log.e("currentHeight  in", currentHeight+"");
    }else {
      refitText(getText().toString(), currentWidth);
    }
  }
  
  public int getcurrentHeight(){
    return currentHeight;
  }
  
  @Override
  protected void onTextChanged(CharSequence text, int start, int before, int after)
  {
    super.onTextChanged(text, start, before, after);
    if (isFixedOneLine) {
      reSize(text.toString(), this.getWidth());
    }else {
      refitText(text.toString(), this.getWidth());
    }
  }
  
  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh)
  {
    if (w != oldw)
    {
      if (isFixedOneLine) {
        reSize(getText().toString(), this.getWidth());
      }else {
        refitText(getText().toString(), this.getWidth());
      }
    }
  }
}

