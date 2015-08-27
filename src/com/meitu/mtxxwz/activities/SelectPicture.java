package com.meitu.mtxxwz.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;


public class SelectPicture extends Activity {

  /** 单张图片按键*/
  private Button btnSinglePicture;
  private OnClickListener SingleBtnListener;
  
  /** 正确的返回码*/
  private int SUCCESS_CODE = 1;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.choose_picture);
    initListener();
    initView();
  }
    
  /**
   * 初始化控件
   */
  private void initView() {
    btnSinglePicture = (Button)this.findViewById(R.id.btn_single_picture);
    btnSinglePicture.setOnClickListener(SingleBtnListener);
  }
    
  /**
   * 监听功能初始化
   */
  private void initListener() {
    SingleBtnListener = new OnClickListener() {
      @Override
      public void onClick(View v) {
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);  
        startActivityForResult(intent, SUCCESS_CODE);
      }
    };
  }
    
  /**
   * 接受选择后返回的图片
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if ((requestCode == SUCCESS_CODE) && (resultCode == RESULT_OK)) {
      Uri uri = data.getData();
      Intent intent = new Intent(SelectPicture.this, FontAdd.class);
      intent.putExtra("uri", uri);
      startActivity(intent);
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
  
}
