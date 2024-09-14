package ff.bx;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View;
import ff.bx.dealx.ShPref;
import android.content.Intent;
import ff.bx.dealx.NothingActivity;

public class ActEditBase extends Activity {
    private TextView svg,code,back;//按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editbase);
        //初始化
        svg=findViewById(R.id.svg);
        code=findViewById(R.id.code);
        back=findViewById(R.id.back);
        //监听
        svg.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ActEditBase.this,ActEd_Svg.class));
                }
            });
        code.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ActEditBase.this,NothingActivity.class));
                }
            });
        back.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    ActEditBase.this.finish();
                }
            });
        //初始化
        init();
    }
    //初始化
    private void init(){
        //获取设置
        int size=ShPref.getInt(ShPref.PRE_EDTXTSIZEMAIN,ShPref.DEF_EDTXTSIZEMAIN,ActEditBase.this);
        int padding=ShPref.getInt(ShPref.PRE_EDTXTPADDINGMAIN,ShPref.DEF_EDTXTPADDINGMAIN,ActEditBase.this);
        String str_svg=ShPref.getString(ShPref.PRE_TXTSVG,ShPref.DEF_TXTSVG,ActEditBase.this);
        String str_code=ShPref.getString(ShPref.PRE_TXTCODE,ShPref.DEF_TXTCODE,ActEditBase.this);
        //应用设置
        svg.setTextSize(size);
        code.setTextSize(size);
        back.setTextSize(size);
        svg.setPadding(padding,padding,padding,padding);
        code.setPadding(padding,padding,padding,padding);
        back.setPadding(padding,padding,padding,padding);
        svg.setText(str_svg);
        code.setText(str_code);

    }
}
