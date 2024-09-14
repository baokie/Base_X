package ff.bx;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.View.OnLongClickListener;
import android.view.View;
import ff.bx.dealx.ShPref;
import android.view.View.OnClickListener;
import android.content.Intent;
import java.io.File;
import ff.bx.dealx.ValueUsed;
import android.os.Handler;
import android.util.DisplayMetrics;
import ff.bx.dealx.NothingActivity;


public class MainActivity extends AppCompatActivity {
    //按钮布局
    private static LinearLayout lnlbtn;
    //提示
    private static TextView txt,keep,clear,del,txt2;
    //-清空全部设置，+按自己设置，设置，编辑进制字符，进制计算器，返回
    private TextView config,edit_base,base_x,back;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        txt2=findViewById(R.id.txt2);
        lnlbtn=findViewById(R.id.lnlbtn);
		clear=findViewById(R.id.clear);
        keep=findViewById(R.id.keep);
        config=findViewById(R.id.config);
        edit_base=findViewById(R.id.edit_base);
        base_x=findViewById(R.id.base_x);
        back=findViewById(R.id.back);
        txt=findViewById(R.id.txt);
        del=findViewById(R.id.del);
		//监听
        clear.setOnLongClickListener(new OnLongClickListener(){//清空所有设置，恢复默认
                @Override
                public boolean onLongClick(View v) {
                    ShPref.clear(MainActivity.this);
                    init();
                    return true;
                }
            });
        del.setOnLongClickListener(new OnLongClickListener(){//清空所有文件，恢复初始
                @Override
                public boolean onLongClick(View v) {
                    ShPref.clear(MainActivity.this);
                    new File(getFilesDir()+File.separator+ValueUsed.FILES_ttf_my_custom).delete();
                    init();
                    return true;
                }
            });
        
        del.setOnClickListener(new OnClickListener(){//×
                @Override
                public void onClick(View v) {
                    txt.setText("[×] LongClick: Clear(All)");
                }
        });
        clear.setOnClickListener(new OnClickListener(){//-
                @Override
                public void onClick(View v) {
                    txt2.setText("[-] LongClick: Clear(Config)");
                }
            });
        keep.setOnClickListener(new OnClickListener(){//+，按设置启动
                @Override
                public void onClick(View v) {
                    init();
                }
            });
        
        config.setOnClickListener(new OnClickListener(){//设置
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,ActConfig.class));
                }
            });
        edit_base.setOnClickListener(new OnClickListener(){//编辑字符
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,ActEditBase.class));
                }
            });
        base_x.setOnClickListener(new OnClickListener(){//计算器
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,NothingActivity.class));
                }
            });
        back.setOnClickListener(new OnClickListener(){//返回
                @Override
                public void onClick(View v) {
                    del.setVisibility(View.VISIBLE);
                    clear.setVisibility(View.VISIBLE);
                    txt2.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.VISIBLE);
                    keep.setVisibility(View.VISIBLE);
                    txt.setText("");
                    txt2.setText("");
                    lnlbtn.setVisibility(View.GONE);
                }
            });
        //初始化
        hdl.postDelayed(rnb,50);
    }
    private Handler hdl=new Handler();
    private Runnable rnb=new Runnable(){
        @Override
        public void run() {
            DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
            int w=displayMetrics.widthPixels;
            int h=displayMetrics.heightPixels;
            int min=w<h?w:h;
            int btnWidth = (int) (min * 0.5);
            int btnHeight = (int) (min * 0.5);
            keep.setLayoutParams(new LinearLayout.LayoutParams(btnWidth, btnHeight));
        }
    };
    //初始化
    private void init(){
        del.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);
        txt2.setVisibility(View.GONE);
        txt.setVisibility(View.GONE);
        keep.setVisibility(View.GONE);
        lnlbtn.setVisibility(View.VISIBLE);
        //获取设置
        int size=ShPref.getInt(ShPref.PRE_TXTSIZEMAIN,ShPref.DEF_TXTSIZEMAIN,MainActivity.this);
        int padding=ShPref.getInt(ShPref.PRE_TXTPADDINGMAIN,ShPref.DEF_TXTPADDINGMAIN,MainActivity.this);
        String str_config=ShPref.getString(ShPref.PRE_TXTCONFIG,ShPref.DEF_TXTCONFIG,MainActivity.this);
        String str_editbase=ShPref.getString(ShPref.PRE_TXTEDITBASE,ShPref.DEF_TXTEDITBASE,MainActivity.this);
        String str_basex=ShPref.getString(ShPref.PRE_TXTBASEX,ShPref.DEF_TXTBASEX,MainActivity.this);
        //应用设置
        config.setTextSize(size);
        edit_base.setTextSize(size);
        base_x.setTextSize(size);
        back.setTextSize(size);
        config.setPadding(padding,padding,padding,padding);
        edit_base.setPadding(padding,padding,padding,padding);
        base_x.setPadding(padding,padding,padding,padding);
        back.setPadding(padding,padding,padding,padding);
        config.setText(str_config);
        edit_base.setText(str_editbase);
        base_x.setText(str_basex);
        
    }
    //外部调用
    public static void reset(){
        del.setVisibility(View.VISIBLE);
        clear.setVisibility(View.VISIBLE);
        txt2.setVisibility(View.VISIBLE);
        txt.setVisibility(View.VISIBLE);
        keep.setVisibility(View.VISIBLE);
        txt.setText("");
        txt2.setText("");
        lnlbtn.setVisibility(View.GONE);
    }
}
