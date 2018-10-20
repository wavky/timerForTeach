package com.wavky.timer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {

    TextView timerText;
    Button cancelBtn;
    Button actionBtn;

    // 标记当前右侧action button的类型
    ActionBtnType actionBtnType = ActionBtnType.New;

    Timer timer;
    // 记录一个定时器启动时使用的初始时间，如果定时器是通过点击resume按钮启动的，则初始时间为上次点击暂停的时候所停留的时间点
    long startTime = 0;
    SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerText = findViewById(R.id.timerText);
        cancelBtn = findViewById(R.id.cancelBtn);
        actionBtn = findViewById(R.id.actionBtn);

        timerText.setText("00:00");
        initCancelBtn();
        initActionBtn();
    }

    private void initCancelBtn() {
        cancelBtn.setEnabled(false);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置左侧取消按钮为不可用（不可再点击）
                cancelBtn.setEnabled(false);
                cancelBtn.setTextColor(getResources().getColor(ActionBtnType.CancelDisable.textColor));
                // 关闭timer，时间显示归零
                timer.cancel();
                timer.purge();
                timerText.setText("00:00");
                // 右侧action button更新为New状态
                changeActionBtnTo(ActionBtnType.New);
            }
        });
    }

    private void initActionBtn() {
        actionBtn.setOnClickListener(new View.OnClickListener() {

            /**
             * 创建一个每1000毫秒运行一次，更新timerTextView时间显示的任务
             */
            private TimerTask createTimerTask() {
                return new TimerTask() {
                    // 以缓存的时间起始点创建Date类型对象，方便使用dateFormat获取特定格式的时间字符串
                    Date time = new Date(startTime);

                    @Override
                    public void run() {
                        // 更新时间缓存
                        startTime += 1000;
                        time.setTime(startTime);

                        // 使用UI线程更新timerTextView时间显示
                        timerText.post(new Runnable() {
                            @Override
                            public void run() {
                                timerText.setText(dateFormat.format(time));
                            }
                        });
                    }
                };
            }

            /**
             * 创建一个每隔1000毫秒回调一次任务的timer对象
             */
            private Timer startNewTimerWith(TimerTask task) {
                Timer timer = new Timer();
                timer.schedule(task, 1000, 1000);
                return timer;
            }

            @Override
            public void onClick(View v) {
                cancelBtn.setEnabled(true);
                cancelBtn.setTextColor(getResources().getColor(ActionBtnType.CancelEnable.textColor));

                // 根据action按钮类型，筛选点击的执行功能块
                switch (actionBtnType) {
                    case New: {
                        // 当action按钮为New时，执行新的秒表计时，并将action按钮切换为暂停按钮
                        startTime = 0;
                        timer = startNewTimerWith(createTimerTask());
                        changeActionBtnTo(ActionBtnType.Pause);
                        break;
                    }
                    case Pause: {
                        // 当action按钮为暂停按钮时，废弃当前timer对象，并将action按钮切换为恢复按钮
                        timer.cancel();
                        timer.purge();
                        changeActionBtnTo(ActionBtnType.Resume);
                        break;
                    }
                    case Resume: {
                        // 当action按钮为恢复按钮时，新建timer对象并延续上次停下来的计时（startTime），并将action按钮切换为暂停按钮
                        timer = startNewTimerWith(createTimerTask());
                        changeActionBtnTo(ActionBtnType.Pause);
                        break;
                    }
                }
            }
        });
    }

    /**
     * 更新右侧action按钮的UI状态（同时更新当前缓存，用于筛选出点击时执行的对应逻辑代码）
     */
    private void changeActionBtnTo(ActionBtnType type) {
        actionBtnType = type;
        actionBtn.setBackgroundResource(type.bg);
        actionBtn.setTextColor(getResources().getColor(type.textColor));
        actionBtn.setText(type.text);
    }

    /**
     * 按action按钮的种类，打包UI更新需要的资源（按钮背景图、文字颜色、文字字符串）
     */
    enum ActionBtnType {
        Pause(R.drawable.pause_btn_bg, R.color.darkOrange, R.string.btn_pause),
        Resume(R.drawable.resume_btn_bg, R.color.darkGreen, R.string.btn_Resume),
        New(R.drawable.resume_btn_bg, R.color.darkGreen, R.string.btn_new),
        CancelEnable(0, R.color.white, R.string.btn_cancel),
        CancelDisable(0, R.color.darkerGray, R.string.btn_cancel);

        int bg, textColor, text;

        ActionBtnType(int bg, int textColor, int text) {
            this.bg = bg;
            this.textColor = textColor;
            this.text = text;
        }
    }
}
