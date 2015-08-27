package com.meitu.mtxxwz.interfaces;

/**
 * 更新图标位置的接口
 * @author meitu
 */
public interface OnUpdataControlBtnPostion {
  public void setRotationBtnXY(int X, int Y);
  public void updataOnMoving(int chazhiX, int chazhiY);
  public void updataOnUp(int chazhiX, int chazhiY);
}
