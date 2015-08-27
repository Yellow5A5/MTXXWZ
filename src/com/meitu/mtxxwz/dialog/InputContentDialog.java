package com.meitu.mtxxwz.dialog;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meitu.mtxxwz.activities.R;

public class InputContentDialog extends Dialog {

  private View mView;
  private EditText editInput;
  private ImageButton imgCancel;
  private ImageButton imgSave;
  private ImageView imgDelete;
  private android.view.View.OnClickListener mOnClickListener;

  private String currentInput;
  public OnInputClickListener mInputClickListener;
  
  
  public interface OnInputClickListener{
    public void OnClickOK(String input);
  };
  public void setOnInputClickListener(OnInputClickListener mInputClickListener){
    this.mInputClickListener = mInputClickListener;
  };
  
  public InputContentDialog(Context context, String currentInput) {
    super(context);
    this.currentInput = currentInput;
    LinearLayout ll = new LinearLayout(context);
    mView = LayoutInflater.from(context).inflate(R.layout.input_dialog, ll, true);
//    设置后没有效果。。？如何才能铺满横屏呢？
//    int width = context.getResources().getDisplayMetrics().widthPixels;
//    int height = context.getResources().getDisplayMetrics().heightPixels;
//    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
//    lp.width = width;
//    lp.height = height;
//    mView.setLayoutParams(lp);
  }

  public InputContentDialog(Context context, int theme) {
    super(context, theme);
  }

  public InputContentDialog(Context context, boolean cancelable,
      OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(mView);
    initView();
    initListener();
    getWindow().setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
  }
  
  //重写show 弹出软键盘
  @Override
  public void show() {
    super.show();
    popKeyBoard();
  }
  
  private void initListener() {
    mOnClickListener = new android.view.View.OnClickListener() {
      @Override
      public void onClick(View v) {
        switch (v.getId()) {
        case R.id.img_input_ok:
          if (!editInput.equals("")) {
            mInputClickListener.OnClickOK(String.valueOf(editInput.getText()));
          }
          InputContentDialog.this.dismiss();
          break;
        case R.id.img_input_cancel:
          InputContentDialog.this.dismiss();
          break;
        case R.id.img_delete:
          editInput.setText("");
          break;

        default:
          break;
        }
      }
    };
    this.setOnCancelListener(new OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
        hideKeyBoard(); 
      }
    });
    imgSave.setOnClickListener(mOnClickListener);
    imgCancel.setOnClickListener(mOnClickListener);
    imgDelete.setOnClickListener(mOnClickListener);
  }

  private void initView() {
    imgSave = (ImageButton) findViewById(R.id.img_input_ok);
    imgCancel = (ImageButton) findViewById(R.id.img_input_cancel);
    imgDelete = (ImageView) findViewById(R.id.img_delete);
    editInput = (EditText) findViewById(R.id.edit_input);
    if (!currentInput.equals("点击输入内容")) {
      editInput.setText(currentInput);
    }
    editInput.requestFocus(); //请求焦点
    popKeyBoard();
  }

  private void popKeyBoard() {
    Timer timer = new Timer(); // 设置定时器等待100毫秒UI加载
    timer.schedule(new TimerTask() {
      @Override
      public void run() { 
        // 弹出软键盘
        InputMethodManager imm = (InputMethodManager) getContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editInput, InputMethodManager.RESULT_SHOWN); //显示软键盘
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY); //切换软键盘
      }
    }, 100);
  }

  private void hideKeyBoard() {
    // 强制隐藏键盘
    InputMethodManager imm = (InputMethodManager) getContext()
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(editInput.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
  }
}
