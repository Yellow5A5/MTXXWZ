package com.meitu.mtxxwz.util;

public class CalculationTool {

  /**
   * 计算两点之间的距离
   * 
   * @param X1
   *          第一个点的X坐标
   * @param Y1
   *          第一个点的Y坐标
   * @param X2
   *          第二个点的X坐标
   * @param Y2
   *          第二个点的Y坐标
   * @return 两点之间的距离
   */
  public static float vectorToPoint(float X1, float Y1, float X2, float Y2) {
    return (float) Math.sqrt(Math.pow(Math.abs(X2 - X1), 2)
        + Math.pow(Math.abs(Y2 - Y1), 2));
  }

  /**
   * cos反函数转换为角度
   * 
   * @param a
   *          double 余弦值
   * @return double 角度(360)
   */
  public static double arccos(double a) {
    double b = 90.0, c0 = 0.0, c1 = 180.0;
    if (a < 1 && a > -1) {
      do {
        if (Math.cos(b * Math.PI / 180) >= a) {
          c0 = b;
          b = (c0 + c1) / 2;
        }
        if (Math.cos(b * Math.PI / 180) <= a) {
          c1 = b;
          b = (c0 + c1) / 2;
        }
      } while (Math.abs(c0 - c1) > 0.00001);
    }
    return b;
  }
}
