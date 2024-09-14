package ff.bx.dealx;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import ff.bx.R;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.View;

public class NothingActivity extends Activity {
    private LinearLayout lnl;
    private SeekBar color,size;
    private TextView txt;

    //
    private int icolor=0;//颜色
    private int isize=10;//尺寸
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nothing);
        //视图
        lnl=findViewById(R.id.lnl);
        color=findViewById(R.id.color);
        size=findViewById(R.id.size);
        txt=findViewById(R.id.txt);
        //监听
        color.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){//颜色
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    icolor=0xffffff*progress/100;
                    txt.setTextColor(0xff000000+icolor);
                    lnl.setBackgroundColor(0xffffffff-icolor);
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        size.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){//尺寸
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    txt.setTextSize(progress);
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        lnl.setOnTouchListener(new OnTouchListener(){//触摸关闭
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN){//按下
                        lnl.setBackgroundColor(0xff000000+icolor);
                    }else if(event.getAction()==MotionEvent.ACTION_UP){//抬起
                        NothingActivity.this.finish();
                    }
                    return true;
                }
            });
        //初始化
        icolor=0;//颜色
        isize=10;//尺寸
        size.setProgress(isize);
    }

}
