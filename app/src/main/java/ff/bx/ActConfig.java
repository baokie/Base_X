package ff.bx;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnLongClickListener;
import android.view.View;
import ff.bx.dealx.ShPref;
import android.view.View.OnClickListener;

public class ActConfig extends Activity {
    //取消，保存，提示
    private TextView cancel,save,txt;
    //全部设置
    private TextView txtx[]=new TextView[32];
    //主界面：字体大小，Config，Edit Base，Base_X，内距
    private EditText txtsize,txtconfig,txteditbase,txtbasex,txtpadding;
    //编辑字符：字体大小，SVG，Code，内距
    private EditText edtxtsizemain,txtsvg,txtcode,edtxtpaddingmain;
    //Config字体大小，间距
    private EditText cfgtxtsize,cfgtxtpadding;
    //SVG
    private EditText svgsize,svgpadding,svgtipsize,svgpreside,svgpresize,svgpretipsize,svggridheight;
    private EditText svgitem1side,svgitem1size,svgitem1tipsize,svgitem2side,svgitem2size,svgitem2tipsize,svgeditsize;
    private EditText svgdefnewshape,svgshapeside,svglinewidth,svgdrawdistance,svgdrawangle,svgloadtag,svgdrawtag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        //隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //初始化
        txtx[0]=findViewById(R.id.activityconfigTextView1);
        txtx[1]=findViewById(R.id.activityconfigTextView2);
        txtx[2]=findViewById(R.id.activityconfigTextView3);
        txtx[3]=findViewById(R.id.activityconfigTextView4);
        txtx[4]=findViewById(R.id.activityconfigTextView5);
        txtx[5]=findViewById(R.id.activityconfigTextView6);
        txtx[6]=findViewById(R.id.activityconfigTextView7);
        txtx[7]=findViewById(R.id.activityconfigTextView8);
        txtx[8]=findViewById(R.id.activityconfigTextView9);
        txtx[9]=findViewById(R.id.activityconfigTextView10);
        txtx[10]=findViewById(R.id.activityconfigTextView11);
        txtx[11]=findViewById(R.id.svg_t1);
        txtx[12]=findViewById(R.id.svg_t2);
        txtx[13]=findViewById(R.id.svg_t3);
        txtx[14]=findViewById(R.id.svg_t4);
        txtx[15]=findViewById(R.id.svg_t5);
        txtx[16]=findViewById(R.id.svg_t6);
        txtx[17]=findViewById(R.id.svg_t7);
        txtx[18]=findViewById(R.id.svg_t8);
        txtx[19]=findViewById(R.id.svg_t9);
        txtx[20]=findViewById(R.id.svg_t10);
        txtx[21]=findViewById(R.id.svg_t11);
        txtx[22]=findViewById(R.id.svg_t12);
        txtx[23]=findViewById(R.id.svg_t13);
        txtx[24]=findViewById(R.id.svg_t14);
        txtx[25]=findViewById(R.id.svg_t15);
        txtx[26]=findViewById(R.id.svg_t16);
        txtx[27]=findViewById(R.id.svg_t17);
        txtx[28]=findViewById(R.id.svg_t18);
        txtx[29]=findViewById(R.id.svg_t19);
        txtx[30]=findViewById(R.id.svg_t20);
        txtx[31]=findViewById(R.id.svg_t21);
        
        cancel=findViewById(R.id.cancel);
        save=findViewById(R.id.save);
        txt=findViewById(R.id.txt);
        
        txtsize=findViewById(R.id.txtsize);
        txtconfig=findViewById(R.id.txtconfig);
        txteditbase=findViewById(R.id.txteditbase);
        txtbasex=findViewById(R.id.txtbasex);
        txtpadding=findViewById(R.id.txtpadding);
        
        cfgtxtsize=findViewById(R.id.txtsizeconfig);
        cfgtxtpadding=findViewById(R.id.txtpaddingconfig);
        
        edtxtsizemain=findViewById(R.id.edtxtsizemain);
        txtsvg=findViewById(R.id.txtsvg);
        txtcode=findViewById(R.id.txtcode);
        edtxtpaddingmain=findViewById(R.id.edtxtpaddingmain);
        
        svgsize=findViewById(R.id.svg_e1);
        svgpadding=findViewById(R.id.svg_e2);
        svgtipsize=findViewById(R.id.svg_e3);
        svgpreside=findViewById(R.id.svg_e4);
        svgpresize=findViewById(R.id.svg_e5);
        svgpretipsize=findViewById(R.id.svg_e6);
        svggridheight=findViewById(R.id.svg_e7);
        svgitem1side=findViewById(R.id.svg_e8);
        svgitem1size=findViewById(R.id.svg_e9);
        svgitem1tipsize=findViewById(R.id.svg_e10);
        svgitem2side=findViewById(R.id.svg_e11);
        svgitem2size=findViewById(R.id.svg_e12);
        svgitem2tipsize=findViewById(R.id.svg_e13);
        svgeditsize=findViewById(R.id.svg_e14);
        svgdefnewshape=findViewById(R.id.svg_e15);
        svgshapeside=findViewById(R.id.svg_e16);
        svglinewidth=findViewById(R.id.svg_e17);
        svgdrawdistance=findViewById(R.id.svg_e18);
        svgdrawangle=findViewById(R.id.svg_e19);
        svgloadtag=findViewById(R.id.svg_e20);
        svgdrawtag=findViewById(R.id.svg_e21);
        
        //监听
        cancel.setOnClickListener(new OnClickListener(){//取消
                @Override
                public void onClick(View v) {
                    ActConfig.this.finish();
                }
            });
        save.setOnClickListener(new OnClickListener(){//保存
                @Override
                public void onClick(View v) {
                    String str=txtsize.getText().toString();//main
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_TXTSIZEMAIN,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_TXTSIZEMAIN,Integer.parseInt(str),ActConfig.this);
                    str=txtpadding.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_TXTPADDINGMAIN,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_TXTPADDINGMAIN,Integer.parseInt(str),ActConfig.this);
                    str=txtconfig.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_TXTCONFIG,ActConfig.this);
                    else
                        ShPref.putString(ShPref.PRE_TXTCONFIG,str,ActConfig.this);
                    str=txteditbase.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_TXTEDITBASE,ActConfig.this);
                    else
                        ShPref.putString(ShPref.PRE_TXTEDITBASE,str,ActConfig.this);
                    str=txtbasex.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_TXTBASEX,ActConfig.this);
                    else
                        ShPref.putString(ShPref.PRE_TXTBASEX,str,ActConfig.this);
                        
                    str=cfgtxtsize.getText().toString();//config
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_CFGTXTSIZE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_CFGTXTSIZE,Integer.parseInt(str),ActConfig.this);
                    str=cfgtxtpadding.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_CFGTXTPADDING,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_CFGTXTPADDING,Integer.parseInt(str),ActConfig.this);
                    
                    str=edtxtsizemain.getText().toString();//editbase
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_EDTXTSIZEMAIN,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_EDTXTSIZEMAIN,Integer.parseInt(str),ActConfig.this);
                    str=edtxtpaddingmain.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_EDTXTPADDINGMAIN,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_EDTXTPADDINGMAIN,Integer.parseInt(str),ActConfig.this);
                    str=txtsvg.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_TXTSVG,ActConfig.this);
                    else
                        ShPref.putString(ShPref.PRE_TXTSVG,str,ActConfig.this);
                    str=txtcode.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_TXTCODE,ActConfig.this);
                    else
                        ShPref.putString(ShPref.PRE_TXTCODE,str,ActConfig.this);
                    //svg
                    str=svgsize.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGSIZE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGSIZE,Integer.parseInt(str),ActConfig.this);
                    str=svgpadding.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGPADDING,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGPADDING,Integer.parseInt(str),ActConfig.this);
                    str=svgtipsize.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGTIPSIZE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGTIPSIZE,Integer.parseInt(str),ActConfig.this);
                    str=svgpreside.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGPRESIDE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGPRESIDE,Integer.parseInt(str),ActConfig.this);
                    str=svgpresize.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGPRESIZE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGPRESIZE,Integer.parseInt(str),ActConfig.this);
                    str=svgpretipsize.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGPRETIPSIZE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGPRETIPSIZE,Integer.parseInt(str),ActConfig.this);
                    str=svggridheight.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGGRIDHEIGHT,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGGRIDHEIGHT,Integer.parseInt(str),ActConfig.this);
                    str=svgitem2side.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGITEM1SIDE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGITEM1SIDE,Integer.parseInt(str),ActConfig.this);
                    str=svgitem1size.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGITEM1SIZE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGITEM1SIZE,Integer.parseInt(str),ActConfig.this);
                    str=svgitem1tipsize.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGITEM1TIPSIZE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGITEM1TIPSIZE,Integer.parseInt(str),ActConfig.this);
                    str=svgitem1side.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGITEM2SIDE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGITEM2SIDE,Integer.parseInt(str),ActConfig.this);
                    str=svgitem2size.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGITEM2SIZE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGITEM2SIZE,Integer.parseInt(str),ActConfig.this);
                    str=svgitem2tipsize.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGITEM2TIPSIZE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGITEM2TIPSIZE,Integer.parseInt(str),ActConfig.this);
                    str=svgeditsize.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGEDITSIZE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGEDITSIZE,Integer.parseInt(str),ActConfig.this);
                    str=svgshapeside.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGSHAPESIDE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGSHAPESIDE,Integer.parseInt(str),ActConfig.this);
                    str=svglinewidth.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGLINEWIDTH,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGLINEWIDTH,Integer.parseInt(str),ActConfig.this);
                    str=svgdrawdistance.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGDRAWDISTANCE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGDRAWDISTANCE,Integer.parseInt(str),ActConfig.this);
                    str=svgdrawangle.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGDRAWANGLE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGDRAWANGLE,Integer.parseInt(str),ActConfig.this);
                    str=svgloadtag.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGLOADLINESTATE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGLOADLINESTATE,Integer.parseInt(str),ActConfig.this);
                    str=svgdrawtag.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGDRAWENDSTATE,ActConfig.this);
                    else
                        ShPref.putInt(ShPref.PRE_SVGDRAWENDSTATE,Integer.parseInt(str),ActConfig.this);
                    str=svgdefnewshape.getText().toString();//
                    if(str==null||str.length()<1)
                        ShPref.remove(ShPref.PRE_SVGDEFNEWSHAPE,ActConfig.this);
                    else
                        ShPref.putString(ShPref.PRE_SVGDEFNEWSHAPE,str,ActConfig.this);
                    
                    /*初始化*/
                    MainActivity.reset();
                    ActConfig.this.finish();
                }
            });
        //初始化
        init();
    }
    //初始化
    private void init(){
        //获取设置
        int size=ShPref.getInt(ShPref.PRE_CFGTXTSIZE,ShPref.DEF_CFGTXTSIZE,ActConfig.this);
        int padding=ShPref.getInt(ShPref.PRE_CFGTXTPADDING,ShPref.DEF_CFGTXTPADDING,ActConfig.this);
        //应用设置
        for(int i=0;i<txtx.length;i++){
            txtx[i].setTextSize(size);
            txtx[i].setPadding(0,padding,0,padding);
        }
        
        txtsize.setTextSize(size);
        txtpadding.setTextSize(size);
        txtconfig.setTextSize(size);
        txteditbase.setTextSize(size);
        txtbasex.setTextSize(size);
        
        cfgtxtsize.setTextSize(size);
        cfgtxtpadding.setTextSize(size);
        
        edtxtsizemain.setTextSize(size);
        edtxtpaddingmain.setTextSize(size);
        txtsvg.setTextSize(size);
        txtcode.setTextSize(size);
        
        cancel.setTextSize(size);
        save.setTextSize(size);
        
        txtsize.setPadding(0,padding,0,padding);
        txtpadding.setPadding(0,padding,0,padding);
        txtconfig.setPadding(0,padding,0,padding);
        txteditbase.setPadding(0,padding,0,padding);
        txtbasex.setPadding(0,padding,0,padding);

        cfgtxtsize.setPadding(0,padding,0,padding);
        cfgtxtpadding.setPadding(0,padding,0,padding);

        edtxtsizemain.setPadding(0,padding,0,padding);
        edtxtpaddingmain.setPadding(0,padding,0,padding);
        txtsvg.setPadding(0,padding,0,padding);
        txtcode.setPadding(0,padding,0,padding);
        
        cancel.setPadding(padding+2,padding+2,padding+2,padding+2);
        save.setPadding(padding+2,padding+2,padding+2,padding+2);

        //svg
        svgsize.setTextSize(size);
        svgpadding.setTextSize(size);
        svgtipsize.setTextSize(size);
        svgpreside.setTextSize(size);
        svgpresize.setTextSize(size);
        svgpretipsize.setTextSize(size);
        svggridheight.setTextSize(size);
        svgitem1size.setTextSize(size);
        svgitem2side.setTextSize(size);
        svgitem1tipsize.setTextSize(size);
        svgitem1side.setTextSize(size);
        svgitem2size.setTextSize(size);
        svgitem2tipsize.setTextSize(size);
        svgeditsize.setTextSize(size);
        svgshapeside.setTextSize(size);
        svgdefnewshape.setTextSize(size);
        svglinewidth.setTextSize(size);
        svgdrawdistance.setTextSize(size);
        svgdrawangle.setTextSize(size);
        svgloadtag.setTextSize(size);
        svgdrawtag.setTextSize(size);

        svgsize.setPadding(0,padding,0,padding);
        svgpadding.setPadding(0,padding,0,padding);
        svgtipsize.setPadding(0,padding,0,padding);
        svgpreside.setPadding(0,padding,0,padding);
        svgpresize.setPadding(0,padding,0,padding);
        svgpretipsize.setPadding(0,padding,0,padding);
        svggridheight.setPadding(0,padding,0,padding);
        svgitem1size.setPadding(0,padding,0,padding);
        svgitem2side.setPadding(0,padding,0,padding);
        svgitem1tipsize.setPadding(0,padding,0,padding);
        svgitem1side.setPadding(0,padding,0,padding);
        svgitem2size.setPadding(0,padding,0,padding);
        svgitem2tipsize.setPadding(0,padding,0,padding);
        svgeditsize.setPadding(0,padding,0,padding);
        svgshapeside.setPadding(0,padding,0,padding);
        svgdefnewshape.setPadding(0,padding,0,padding);
        svglinewidth.setPadding(0,padding,0,padding);
        svgdrawdistance.setPadding(0,padding,0,padding);
        svgdrawangle.setPadding(0,padding,0,padding);
        svgloadtag.setPadding(0,padding,0,padding);
        svgdrawtag.setPadding(0,padding,0,padding);
        
        
        //text
        txtsize.setText(""+ShPref.getInt(ShPref.PRE_TXTSIZEMAIN,ShPref.DEF_TXTSIZEMAIN,ActConfig.this));
        txtpadding.setText(""+ShPref.getInt(ShPref.PRE_TXTPADDINGMAIN,ShPref.DEF_TXTPADDINGMAIN,ActConfig.this));
        txtconfig.setText(ShPref.getString(ShPref.PRE_TXTCONFIG,ShPref.DEF_TXTCONFIG,ActConfig.this));
        txteditbase.setText(ShPref.getString(ShPref.PRE_TXTEDITBASE,ShPref.DEF_TXTEDITBASE,ActConfig.this));
        txtbasex.setText(ShPref.getString(ShPref.PRE_TXTBASEX,ShPref.DEF_TXTBASEX,ActConfig.this));
        
        cfgtxtsize.setText(""+ShPref.getInt(ShPref.PRE_CFGTXTSIZE,ShPref.DEF_CFGTXTSIZE,ActConfig.this));
        cfgtxtpadding.setText(""+ShPref.getInt(ShPref.PRE_CFGTXTPADDING,ShPref.DEF_CFGTXTPADDING,ActConfig.this));
        
        edtxtsizemain.setText(""+ShPref.getInt(ShPref.PRE_EDTXTSIZEMAIN,ShPref.DEF_EDTXTSIZEMAIN,ActConfig.this));
        edtxtpaddingmain.setText(""+ShPref.getInt(ShPref.PRE_EDTXTPADDINGMAIN,ShPref.DEF_EDTXTPADDINGMAIN,ActConfig.this));
        txtsvg.setText(ShPref.getString(ShPref.PRE_TXTSVG,ShPref.DEF_TXTSVG,ActConfig.this));
        txtcode.setText(ShPref.getString(ShPref.PRE_TXTCODE,ShPref.DEF_TXTCODE,ActConfig.this));
        
        //hint
        txtsize.setHint(""+ShPref.DEF_TXTSIZEMAIN);
        txtpadding.setHint(""+ShPref.DEF_TXTPADDINGMAIN);
        txtconfig.setHint(ShPref.DEF_TXTCONFIG);
        txteditbase.setHint(ShPref.DEF_TXTEDITBASE);
        txtbasex.setHint(ShPref.DEF_TXTBASEX);
        
        cfgtxtsize.setHint(""+ShPref.DEF_CFGTXTSIZE);
        cfgtxtpadding.setHint(""+ShPref.DEF_CFGTXTPADDING);
        
        edtxtsizemain.setHint(""+ShPref.DEF_EDTXTSIZEMAIN);
        edtxtpaddingmain.setHint(""+ShPref.DEF_EDTXTPADDINGMAIN);
        txtsvg.setHint(ShPref.DEF_TXTSVG);
        txtcode.setHint(ShPref.DEF_TXTCODE);
        
        //svg
        svgsize.setText(""+ShPref.getInt(ShPref.PRE_SVGSIZE,ShPref.DEF_SVGSIZE,ActConfig.this));
        svgpadding.setText(""+ShPref.getInt(ShPref.PRE_SVGPADDING,ShPref.DEF_SVGPADDING,ActConfig.this));
        svgtipsize.setText(""+ShPref.getInt(ShPref.PRE_SVGTIPSIZE,ShPref.DEF_SVGTIPSIZE,ActConfig.this));
        svgpreside.setText(""+ShPref.getInt(ShPref.PRE_SVGPRESIDE,ShPref.DEF_SVGPRESIDE,ActConfig.this));
        svgpresize.setText(""+ShPref.getInt(ShPref.PRE_SVGPRESIZE,ShPref.DEF_SVGPRESIZE,ActConfig.this));
        svgpretipsize.setText(""+ShPref.getInt(ShPref.PRE_SVGPRETIPSIZE,ShPref.DEF_SVGPRETIPSIZE,ActConfig.this));
        svggridheight.setText(""+ShPref.getInt(ShPref.PRE_SVGGRIDHEIGHT,ShPref.DEF_SVGGRIDHEIGHT,ActConfig.this));
        svgitem1side.setText(""+ShPref.getInt(ShPref.PRE_SVGITEM1SIDE,ShPref.DEF_SVGITEM1SIDE,ActConfig.this));
        svgitem1size.setText(""+ShPref.getInt(ShPref.PRE_SVGITEM1SIZE,ShPref.DEF_SVGITEM1SIZE,ActConfig.this));
        svgitem1tipsize.setText(""+ShPref.getInt(ShPref.PRE_SVGITEM1TIPSIZE,ShPref.DEF_SVGITEM1TIPSIZE,ActConfig.this));
        svgitem2side.setText(""+ShPref.getInt(ShPref.PRE_SVGITEM2SIDE,ShPref.DEF_SVGITEM2SIDE,ActConfig.this));
        svgitem2size.setText(""+ShPref.getInt(ShPref.PRE_SVGITEM2SIZE,ShPref.DEF_SVGITEM2SIZE,ActConfig.this));
        svgitem2tipsize.setText(""+ShPref.getInt(ShPref.PRE_SVGITEM2TIPSIZE,ShPref.DEF_SVGITEM2TIPSIZE,ActConfig.this));
        svgeditsize.setText(""+ShPref.getInt(ShPref.PRE_SVGEDITSIZE,ShPref.DEF_SVGEDITSIZE,ActConfig.this));
        svgshapeside.setText(""+ShPref.getInt(ShPref.PRE_SVGSHAPESIDE,ShPref.DEF_SVGSHAPESIDE,ActConfig.this));
        svglinewidth.setText(""+ShPref.getInt(ShPref.PRE_SVGLINEWIDTH,ShPref.DEF_SVGLINEWIDTH,ActConfig.this));
        svgdrawdistance.setText(""+ShPref.getInt(ShPref.PRE_SVGDRAWDISTANCE,ShPref.DEF_SVGDRAWDISTANCE,ActConfig.this));
        svgdrawangle.setText(""+ShPref.getInt(ShPref.PRE_SVGDRAWANGLE,ShPref.DEF_SVGDRAWANGLE,ActConfig.this));
        svgloadtag.setText(""+ShPref.getInt(ShPref.PRE_SVGLOADLINESTATE,ShPref.DEF_SVGLOADLINESTATE,ActConfig.this));
        svgdrawtag.setText(""+ShPref.getInt(ShPref.PRE_SVGDRAWENDSTATE,ShPref.DEF_SVGDRAWENDSTATE,ActConfig.this));
        svgdefnewshape.setText(ShPref.getString(ShPref.PRE_SVGDEFNEWSHAPE,ShPref.DEF_SVGDEFNEWSHAPE,ActConfig.this));
        
        svgsize.setHint(""+ShPref.DEF_SVGSIZE);
        svgpadding.setHint(""+ShPref.DEF_SVGPADDING);
        svgtipsize.setHint(""+ShPref.DEF_SVGTIPSIZE);
        svgpreside.setHint(""+ShPref.DEF_SVGPRESIDE);
        svgpresize.setHint(""+ShPref.DEF_SVGPRESIZE);
        svgpretipsize.setHint(""+ShPref.DEF_SVGPRETIPSIZE);
        svggridheight.setHint(""+ShPref.DEF_SVGGRIDHEIGHT);
        svgitem1side.setHint(""+ShPref.DEF_SVGITEM1SIDE);
        svgitem1size.setHint(""+ShPref.DEF_SVGITEM1SIZE);
        svgitem1tipsize.setHint(""+ShPref.DEF_SVGITEM1TIPSIZE);
        svgitem2side.setHint(""+ShPref.DEF_SVGITEM2SIDE);
        svgitem2size.setHint(""+ShPref.DEF_SVGITEM2SIZE);
        svgitem2tipsize.setHint(""+ShPref.DEF_SVGITEM2TIPSIZE);
        svgeditsize.setHint(""+ShPref.DEF_SVGEDITSIZE);
        svgshapeside.setHint(""+ShPref.DEF_SVGSHAPESIDE);
        svglinewidth.setHint(""+ShPref.DEF_SVGLINEWIDTH);
        svgdrawdistance.setHint(""+ShPref.DEF_SVGDRAWDISTANCE);
        svgdrawangle.setHint(""+ShPref.DEF_SVGDRAWANGLE);
        svgloadtag.setHint(""+ShPref.DEF_SVGLOADLINESTATE);
        svgdrawtag.setHint(""+ShPref.DEF_SVGDRAWENDSTATE);
        svgdefnewshape.setHint(ShPref.DEF_SVGDEFNEWSHAPE);
        
    }
}
