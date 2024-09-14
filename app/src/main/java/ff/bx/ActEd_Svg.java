package ff.bx;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Adapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import ff.bx.dealx.SimpleTTF;
import ff.bx.dealx.AllDeal;
import android.content.Context;
import java.io.File;
import ff.bx.dealx.ValueUsed;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.graphics.Typeface;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import android.os.Handler;
import android.widget.Toast;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import android.widget.LinearLayout;
import android.text.Editable;
import android.view.SurfaceView;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.widget.AdapterView.OnItemLongClickListener;
import ff.bx.dealx.ShPref;


public class ActEd_Svg extends Activity implements OnClickListener,OnTouchListener {
    
    private LinearLayout lnl,lnlim;
    private TextView imgt,imgmsg;
    private RelativeLayout rlt;
    private GridView grd,grd2;
    private TextView def1,def2,def3,my;
    private TextView clear,copy,paste,submit;
    private TextView txt,txtsize,txt2;
    private SeekBar size,edtsize2,touchseek,touchangle_seek;
    private TextView change,change2,preview;
    private EditText edt;
    private TextView back,del,touchtxt,touchangle_txt;
    private RelativeLayout seeklayout,rltimg,touchrlt,touchangle_rlt;
    private TextView readlinewidth,defseleall;
    private DrawView draw;
    //
    private Context c;//上下文
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ed_svg);
        //隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //上下文
        c=ActEd_Svg.this;
        //初始化
        lnl=findViewById(R.id.lnle);
        lnlim=findViewById(R.id.lnlim);
        preview=findViewById(R.id.preview);
        edtsize2=findViewById(R.id.edtsize2);
        rlt=findViewById(R.id.imgprt);
        imgmsg=findViewById(R.id.imgmsg);
        imgt=findViewById(R.id.imgt);
        grd=findViewById(R.id.grd);
        grd2=findViewById(R.id.grd2);
        def1=findViewById(R.id.def1);
        def2=findViewById(R.id.def2);
        def3=findViewById(R.id.def3);
        my=findViewById(R.id.my);
        clear=findViewById(R.id.clear);
        copy=findViewById(R.id.copy);
        paste=findViewById(R.id.paste);
        submit=findViewById(R.id.submit);
        txt=findViewById(R.id.txt);
        txtsize=findViewById(R.id.txtsize);
        txt2=findViewById(R.id.txt2);
        size=findViewById(R.id.size);
        change=findViewById(R.id.change);
        change2=findViewById(R.id.change2);
        edt=findViewById(R.id.edt);
        back=findViewById(R.id.back);
        del=findViewById(R.id.del);
        seeklayout=findViewById(R.id.seeklayout);
        rltimg=findViewById(R.id.rltimg);
        touchrlt=findViewById(R.id.touchrlt);
        touchseek=findViewById(R.id.touchseek);
        touchtxt=findViewById(R.id.touchtxt);
        touchangle_rlt=findViewById(R.id.touchangle_rlt);
        touchangle_seek=findViewById(R.id.touchangle_seek);
        touchangle_txt=findViewById(R.id.touchangle_txt);
        readlinewidth=findViewById(R.id.readlinewidth);
        defseleall=findViewById(R.id.defseleall);
        draw=new DrawView(this);
        //监听
        grd.setOnItemClickListener(new ShapeListOnItemClick());
        grd2.setOnItemClickListener(new PointsOnItemClick());
        grd2.setOnItemLongClickListener(new PointsOnItemLongClick());
        size.setOnSeekBarChangeListener(new LineWidthSeekBar());
        def1.setOnClickListener(this);
        def2.setOnClickListener(this);
        def3.setOnClickListener(this);
        my.setOnClickListener(this);
        clear.setOnClickListener(this);
        copy.setOnClickListener(this);
        paste.setOnClickListener(this);
        submit.setOnClickListener(this);
        change.setOnClickListener(this);
        change2.setOnClickListener(this);
        back.setOnClickListener(this);
        del.setOnClickListener(this);
        preview.setOnClickListener(this);
        readlinewidth.setOnClickListener(this);
        defseleall.setOnClickListener(this);
        edtsize2.setOnSeekBarChangeListener(new EditSizeSeekBar());
        touchseek.setOnSeekBarChangeListener(new TouchRangeSeekBar());
        touchangle_seek.setOnSeekBarChangeListener(new TouchAngleSeekBar());
        //初始化
        init();
        RUN=0;
        hdl.postDelayed(rnb,50);
    }
    /*
    *初始化
    */
    //初始固定：
    private String myttfpath;//我的路径
    private String str_shapemsg;//字符含义
    private final int addsignu=0x2b;//加的符号unicode代码
    private String addsign;//加的符号
    private int drawsize,drawx0,drawy0;//画布实际尺寸，起点
    private float drawscale;//画布缩放比例
    private float shapescale;//进度条缩放比例
    //初始固定：界面设置
    private int shaperange;//输入框缩放字形尺寸为当前值
    private int grditemw,grd2itemw;//项宽高
    private int grditemtsize,grd2itemtsize;//项字体大小
    private int grditemtsize2,grd2itemtsize2;//项提示字体大小
    //初始固定：自定义数据
    private int drawendstate;//抬起是否全选，0否1是
    private int readwidth;//是否预览线宽，0否1是
    private String defnew;//默认新建
    //自定义数据
    private String strcopy;//复制内容
    private String strpre;//输入框预览切换复制
    //自定义数据
    private int width;//当前线宽，缩放至字体实际尺寸
    private int touchrange,touch_drawd;//触摸画线距离百分比，换算0x1000距离
    private int touchangle;//加入点角度百分比，换算π
    private double touchangled;//加入点角度换算π
    private int typewidth;//字体读取线宽度
    //状态
    private boolean isauto,isdraw;//是否切换为自动简单线，是否切换为画点模式
    private boolean ispre;//是否预览模式
    private int nowload;//当前加载：0<默认1> 1<默认2> 2<默认3> 3<我的>
    //字体
    private Typeface nowtypeface;//当前显示字体
    //适配器相关
    private ShapeListAdapter shapelistadapter;//grd1适配器
    private LinePointAdapter linepointadapter;//grd2适配器
    
    //初始化：延迟
    private int RUN;
    private int RUN_INIT=0,RUN_DRAW=1;
    private Handler hdl=new Handler();
    private Runnable rnb=new Runnable(){
        @Override
        public void run() {
            if(RUN==RUN_INIT){
                //init();
                //pre按钮大小
                int height = preview.getMeasuredHeight();
                ViewGroup.LayoutParams layoutParams = preview.getLayoutParams();
                layoutParams.width = height;
                preview.setLayoutParams(layoutParams);
                //开关按钮大小
                layoutParams = readlinewidth.getLayoutParams();
                layoutParams.width = height;
                readlinewidth.setLayoutParams(layoutParams);
                layoutParams = defseleall.getLayoutParams();
                layoutParams.width = height;
                defseleall.setLayoutParams(layoutParams);
                //DrawView
                rltimg.addView(draw);
                draw.setOnTouchListener(ActEd_Svg.this);
                //t
                hdlt.postDelayed(rnbt,100);
            }else if(RUN==RUN_DRAW){
                draw.draw(linenum,pointnumsL,points_xL,points_yL);
            }
        }
    };
    //初始化
    private void init(){
        /**/
        //获取我的路径
        myttfpath=getFilesDir().getPath()+File.separator+ValueUsed.FILES_ttf_my_custom;
        //我的字体文件
        if(!new File(myttfpath).exists())
            AllDeal.copyAssetstoPath(c,ValueUsed.ASSETS_ttf_my_alldefault,myttfpath);
        //字符解释文本
        str_shapemsg=new String(AllDeal.readAssetsFiletoBytex_filename(c,ValueUsed.ASSETS_symbol_msg),StandardCharsets.UTF_8);
        //加符号
        addsign=new String(SimpleTTF.unicodeOneToUtf8bytes(addsignu),StandardCharsets.UTF_8);
        //进度条缩放比例
        shapescale=0x1000/100.0f;//=width/progress
        /**/
        
        //获取设置
        int imgsize=ShPref.getInt(ShPref.PRE_SVGPRESIDE,ShPref.DEF_SVGPRESIDE,ActEd_Svg.this);//预览尺寸
        int imgtsize=ShPref.getInt(ShPref.PRE_SVGPRESIZE,ShPref.DEF_SVGPRESIZE,ActEd_Svg.this);//预览字体大小
        grditemw=ShPref.getInt(ShPref.PRE_SVGITEM1SIDE,ShPref.DEF_SVGITEM1SIDE,ActEd_Svg.this);//grd列宽
        grd2itemw=ShPref.getInt(ShPref.PRE_SVGITEM2SIDE,ShPref.DEF_SVGITEM2SIDE,ActEd_Svg.this);//grd2列宽
        int grd2size=ShPref.getInt(ShPref.PRE_SVGGRIDHEIGHT,ShPref.DEF_SVGGRIDHEIGHT,ActEd_Svg.this);//点列表尺寸
        int tsize=ShPref.getInt(ShPref.PRE_SVGSIZE,ShPref.DEF_SVGSIZE,ActEd_Svg.this);//按钮字体大小
        int padding=ShPref.getInt(ShPref.PRE_SVGPADDING,ShPref.DEF_SVGPADDING,ActEd_Svg.this);//按钮padding
        int tsize2=ShPref.getInt(ShPref.PRE_SVGTIPSIZE,ShPref.DEF_SVGTIPSIZE,ActEd_Svg.this);//提示字体大小
        int edtsize=ShPref.getInt(ShPref.PRE_SVGEDITSIZE,ShPref.DEF_SVGEDITSIZE,ActEd_Svg.this);//编辑文本框字体大小
        int imgmsgsize=ShPref.getInt(ShPref.PRE_SVGPRETIPSIZE,ShPref.DEF_SVGPRETIPSIZE,ActEd_Svg.this);//预览提示字体大小
        grditemtsize=ShPref.getInt(ShPref.PRE_SVGITEM1SIZE,ShPref.DEF_SVGITEM1SIZE,ActEd_Svg.this);//grd字体大小
        grd2itemtsize=ShPref.getInt(ShPref.PRE_SVGITEM2SIZE,ShPref.DEF_SVGITEM2SIZE,ActEd_Svg.this);//grd2字体大小
        grditemtsize2=ShPref.getInt(ShPref.PRE_SVGITEM1TIPSIZE,ShPref.DEF_SVGITEM1TIPSIZE,ActEd_Svg.this);//grd提示字体大小
        grd2itemtsize2=ShPref.getInt(ShPref.PRE_SVGITEM2TIPSIZE,ShPref.DEF_SVGITEM2TIPSIZE,ActEd_Svg.this);//grd2提示字体大小
        shaperange=ShPref.getInt(ShPref.PRE_SVGSHAPESIDE,ShPref.DEF_SVGSHAPESIDE,ActEd_Svg.this);//输入框缩放字形尺寸为当前值
        touchrange=ShPref.getInt(ShPref.PRE_SVGDRAWDISTANCE,ShPref.DEF_SVGDRAWDISTANCE,ActEd_Svg.this);//距离百分比
        touchangle=ShPref.getInt(ShPref.PRE_SVGDRAWANGLE,ShPref.DEF_SVGDRAWANGLE,ActEd_Svg.this);//角度百分比
        width=ShPref.getInt(ShPref.PRE_SVGLINEWIDTH,ShPref.DEF_SVGLINEWIDTH,ActEd_Svg.this);//*默认线宽*最大以shaperange为准

        drawendstate=ShPref.getInt(ShPref.PRE_SVGDRAWENDSTATE,ShPref.DEF_SVGDRAWENDSTATE,ActEd_Svg.this);//是否全选
        readwidth=ShPref.getInt(ShPref.PRE_SVGLOADLINESTATE,ShPref.DEF_SVGLOADLINESTATE,ActEd_Svg.this);//是否预览线宽
        defnew=ShPref.getString(ShPref.PRE_SVGDEFNEWSHAPE,ShPref.DEF_SVGDEFNEWSHAPE,ActEd_Svg.this);//默认新建
        /*
        int imgsize=300;//预览尺寸
        int imgtsize=150;//预览字体大小
        grditemw=80;//grd列宽
        grd2itemw=80;//grd2列宽
        int grd2size=300;//点列表尺寸
        int tsize=16;//按钮字体大小
        int padding=20;//按钮padding
        int tsize2=10;//提示字体大小
        int edtsize=16;//编辑文本框字体大小
        int imgmsgsize=10;//预览提示字体大小
        grditemtsize=12;//grd字体大小
        grd2itemtsize=12;//grd2字体大小
        grditemtsize2=10;//grd提示字体大小
        grd2itemtsize2=10;//grd2提示字体大小
        shaperange=100;//输入框缩放字形尺寸为当前值
        touchrange=6;//距离百分比
        touchangle=6;//角度百分比
        width=5;//*默认线宽*最大以shaperange为准
        
        drawendstate=0;//是否全选
        readwidth=0;//是否预览线宽
        defnew="L(0,0)(0,0)(0,0)(0,0)";//默认新建
        */
        //初始化值
        if(touchrange<1) touchrange=1;
        touch_drawd=Math.round(0x1000*touchrange/100.0f);//触摸画线距离0x1000换算后
        touchangled=Math.PI*touchangle/100;//触摸画线角度π换算后
        width=Math.round(1.0f*0x1000*width/shaperange);//*
        
        //界面设置
        grd.setNumColumns(GridView.AUTO_FIT);//列数
        grd2.setNumColumns(GridView.AUTO_FIT);
        size.setProgress(Math.round(width/shapescale));
        touchseek.setProgress(touchrange);
        touchtxt.setText("distance: "+touchrange+"%");
        touchangle_seek.setProgress(touchangle);
        touchangle_txt.setText("angle: "+touchangle+"%");
        if(drawendstate==1){
            defseleall.setText("■");
            defseleall.setBackground(getDrawable(R.drawable.button_x_fff_turnon));
        }
        if(readwidth==1){
            readlinewidth.setText("■");
            readlinewidth.setBackground(getDrawable(R.drawable.button_x_fff_turnon));
        }
        //应用设置
        grd.setColumnWidth(grditemw);//grd列宽
        grd2.setColumnWidth(grd2itemw);//grd2列宽
        imgt.setTextSize(imgtsize);//预览字体大小
        edtsize2.setProgress(edtsize);//seekbar2编辑框字体大小
        
        ViewGroup.LayoutParams layoutParams=rlt.getLayoutParams();
        layoutParams.width=imgsize;
        layoutParams.height=imgsize;
        rlt.setLayoutParams(layoutParams);//预览尺寸
        layoutParams=grd2.getLayoutParams();
        layoutParams.width=ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height=grd2size;
        grd2.setLayoutParams(layoutParams);//点列表尺寸
        
        imgmsg.setTextSize(imgmsgsize);
        
        def1.setTextSize(tsize);
        def2.setTextSize(tsize);
        def3.setTextSize(tsize);
        my.setTextSize(tsize);
        clear.setTextSize(tsize);
        copy.setTextSize(tsize);
        paste.setTextSize(tsize);
        submit.setTextSize(tsize);
        change.setTextSize(tsize);
        change2.setTextSize(tsize);
        preview.setTextSize(tsize);
        back.setTextSize(tsize);
        del.setTextSize(tsize);
        
        def1.setPadding(0,padding,0,padding);
        def2.setPadding(0,padding,0,padding);
        def3.setPadding(0,padding,0,padding);
        my.setPadding(0,padding,0,padding);
        clear.setPadding(0,padding,0,padding);
        copy.setPadding(0,padding,0,padding);
        paste.setPadding(0,padding,0,padding);
        submit.setPadding(0,padding,0,padding);
        change.setPadding(0,padding,0,padding);
        change2.setPadding(0,padding,0,padding);
        preview.setPadding(0,padding,0,padding);
        back.setPadding(0,padding,0,padding);
        del.setPadding(0,padding,0,padding);
        
        txt.setTextSize(tsize2);
        txt2.setTextSize(tsize2);
        txtsize.setTextSize(tsize2);
        
        edt.setTextSize(edtsize);
        
        /**/
        
        //当前加载字体
        //初始化值
        isauto=false;//是否切换为简单自动线
        isdraw=false;//是否切换为画点模式
        ispre=true;//是否预览*
        nowload=3;//当前加载
        
        if(ispre){//*
            preview.setText("◆");
            preview.setBackground(getDrawable(R.drawable.button_o_fff_choosed));
            //edt.setTypeface(nowtypeface);//字体
        }
        
        nowindex=0;
        initload(3);
    }
    //初始化字体
    private void initload(int newload){
        //更新界面：切换，按钮，字体
        updateUIloadButton(nowload,newload);
        updateUIshow(isauto,isdraw,ispre,newload);
        updateTypeface(newload,ispre);
        //加载字体
        loadTTF(newload);//加载字体，获取字形列表
        
        initindex();
    }
    //初始化字形
    private void initindex(){
        //更新界面：预览
        updateUIpreviewImg();//刷新预览

        /*状态机：isauto isdraw ispre nowload*/
        //●字形列表：grd1
        if(shapelistadapter==null) {
            shapelistadapter=new ShapeListAdapter(c);
            grd.setAdapter(shapelistadapter);
        }else{
            shapelistadapter.initdata();
            shapelistadapter.notifyDataSetChanged();
        }
        //●字形信息
        loadShape(nowindex,isauto);//加载字形
        //●界面加载
        if(!isdraw&&!ispre){//★编辑框
            if(isauto){//自动
                edt.setText(SimpleTTF.autoData2autostr(width,shaperange,linenum,pointnumsL,points_xL,points_yL));
            }else{//完整
                edt.setText(SimpleTTF.shapeData2str(shaperange,linenum,pointnumsL,points_xL,points_yL));
            }
        }if(isdraw){//★图片
            draw.sele(0,null);
            RUN=RUN_DRAW;
            hdl.postDelayed(rnb,1);
        }if(isdraw){//★线点列表
            if(linepointadapter==null) {
                linepointadapter=new LinePointAdapter(c);
                grd2.setAdapter(linepointadapter);
            }else{
                linepointadapter.initvalue();
                linepointadapter.initdata();
                linepointadapter.notifyDataSetChanged();
            }
        }if(isdraw&&isauto){//★线宽进度条
            //自动
            size.setProgress(Math.round(width/shapescale));
        }
    }
    
    /*
    *DrawView
    */
    private class DrawView extends View{
        //构造器
        public DrawView(Context context){
            super(context);
        }
        private float wscale;//缩放后width
        //绘制
        private int lnum;
        private List<Integer> pnumsL,xL,yL;
        private List<List<Integer>> pxL,pyL;
        private int lloc;//*高亮
        private boolean[] psele;//*高亮
        public void sele(int lloc,boolean[] psele){//*
            this.lloc=lloc;
            this.psele=psele;
        }
        public void draw(int lnum,List<Integer> pnumsL,List<List<Integer>> pxL,List<List<Integer>> pyL){
            //刷新视图大小并获取画布大小与起点，缩放比例
            int draww=lnlim.getMeasuredWidth();
            int drawh=lnlim.getMeasuredHeight();
            ViewGroup.LayoutParams layoutParams2 = draw.getLayoutParams();
            layoutParams2.width=draww;
            layoutParams2.height=drawh;
            draw.setLayoutParams(layoutParams2);
            drawsize=Math.min(draww,drawh);
            drawx0=(draww-drawsize)/2;
            drawy0=(drawh-drawsize)/2;
            drawscale=1.0f*drawsize/0x1000;//缩放比例
            wscale=width*drawscale;//缩放后width
            
            this.lnum=lnum;
            this.pnumsL=pnumsL;
            this.pxL=pxL;
            this.pyL=pyL;
            
            invalidate();
        }
        private Paint pa=new Paint();
        private Paint pa2=new Paint();
        public void onDraw(Canvas ca){
            //绘制边缘
            ca.drawColor(0xffcccccc);
            pa.setColor(0xffffffff);
            pa.setStrokeWidth(1);//画笔宽度
            pa.setStyle(Paint.Style.FILL);//画笔填充样式
            pa.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            ca.drawRect(drawx0,drawy0,drawx0+drawsize,drawy0+drawsize,pa);
            //绘制字形
            Path path=new Path();//路径
            Path path2=new Path();
            if(!isauto){//完整
                pa.setColor(0xc0000000);//画笔颜色
                pa.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
                pa2.setColor(0xff000000);//画笔颜色
                pa2.setStyle(Paint.Style.STROKE);
                pa2.setStrokeWidth(4);//画笔宽度
                pa2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
                for(int i=0;i<lnum;i++){//遍历线
                    xL=pxL.get(i);//当前线的x列表
                    yL=pyL.get(i);//当前线的y列表
                    path.moveTo(drawx0+xL.get(0)*drawscale,drawy0+drawsize-yL.get(0)*drawscale);//路径：当前线起点
                    for(int j=1;j<pnumsL.get(i);j++){//遍历当前线剩余点
                        path.lineTo(drawx0+xL.get(j)*drawscale,drawy0+drawsize-yL.get(j)*drawscale);//路径
                    }
                    path.close();//路径：关闭
                }
                ca.drawPath(path,pa);//绘制当前线
                ca.drawPath(path,pa2);
            }else{//自动
                /*
                pa.setColor(0xff000000);//画笔颜色
                pa.setStyle(Paint.Style.STROKE);
                pa.setStrokeWidth(2);//画笔宽度
                path=new Path();//路径
                
                Paint pa2=new Paint();
                Path path2=new Path();
                pa2.setColor(0x80cccccc);
                pa2.setStyle(Paint.Style.FILL);
                pa2.setStrokeWidth(1);
                pa2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
                
                float x0,y0,x1,y1;
                
                for(int i=0;i<lnum;i++){//遍历线
                    xL=pxL.get(i);//当前线的x列表
                    yL=pyL.get(i);//当前线的y列表
                    x0=drawx0+xL.get(0)*drawscale;
                    y0=drawy0+drawsize-yL.get(0)*drawscale;
                    path.moveTo(x0,y0);//路径：当前线起点
                    for(int j=1;j<pnumsL.get(i);j++){//遍历当前线剩余点
                        x1=drawx0+xL.get(j)*drawscale;
                        y1=drawy0+drawsize-yL.get(j)*drawscale;
                        path.lineTo(x1,y1);//路径
                        path2=new Path();
                        path2.moveTo(x0,y0);
                        path2.lineTo(x1,y1);
                        path2.lineTo(x1+wscale/1.2f,y1-wscale/2.0f);
                        path2.lineTo(x0+wscale/1.2f,y0-wscale/2.0f);
                        path2.close();
                        ca.drawPath(path2,pa2);
                        x0=x1;
                        y0=y1;
                    }
                }
                ca.drawPath(path,pa);//绘制当前线
                */
                pa.setColor(0xff000000);//画笔颜色
                pa.setStyle(Paint.Style.STROKE);
                pa.setStrokeWidth(4);//画笔宽度
                pa.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
                path=new Path();//路径

                pa2=new Paint();
                pa2.setColor(0xc0000000);
                pa2.setStyle(Paint.Style.FILL);
                pa2.setStrokeWidth(1);
                pa2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
                path2=new Path();

                float x0,y0,x1,y1,x2,y2;
                float[] xy;
                int pnum;//当前线点数

                for(int i=0;i<lnum;i++){//遍历线
                    xL=pxL.get(i);//当前线的x列表
                    yL=pyL.get(i);//当前线的y列表
                    pnum=pnumsL.get(i);//当前线点数
                    if(pnum<2) continue;
                    x0=drawx0+xL.get(0)*drawscale;
                    y0=drawy0+drawsize-yL.get(0)*drawscale;
                    
                    path.moveTo(x0,y0);//路径：当前线起点
                    //path2=new Path();//*
                    path2.moveTo(x0,y0);
                    
                    for(int j=1;j<pnum;j++){//遍历当前线剩余点
                        x0=drawx0+xL.get(j)*drawscale;
                        y0=drawy0+drawsize-yL.get(j)*drawscale;
                        path.lineTo(x0,y0);//路径
                        path2.lineTo(x0,y0);
                    }
                    //起点
                    x0=drawx0+xL.get(pnum-1)*drawscale;
                    y0=drawy0+drawsize-yL.get(pnum-1)*drawscale;
                    x1=drawx0+xL.get(pnum-2)*drawscale;
                    y1=drawy0+drawsize-yL.get(pnum-2)*drawscale;
                    xy=SimpleTTF.getstart2(x0,y0,x1,y1,wscale);
                    path2.lineTo(xy[0],xy[1]);
                    //中间点
                    for(int j=0;j<pnum-2;j++){
                        x0=drawx0+xL.get(pnum-1-j)*drawscale;
                        y0=drawy0+drawsize-yL.get(pnum-1-j)*drawscale;
                        x1=drawx0+xL.get(pnum-2-j)*drawscale;
                        y1=drawy0+drawsize-yL.get(pnum-2-j)*drawscale;
                        x2=drawx0+xL.get(pnum-3-j)*drawscale;
                        y2=drawy0+drawsize-yL.get(pnum-3-j)*drawscale;
                        xy=SimpleTTF.getmid2(x0,y0,x1,y1,x2,y2,wscale);
                        path2.lineTo(xy[0],xy[1]);
                    }
                    //终点
                    x1=drawx0+xL.get(1)*drawscale;
                    y1=drawy0+drawsize-yL.get(1)*drawscale;
                    x2=drawx0+xL.get(0)*drawscale;
                    y2=drawy0+drawsize-yL.get(0)*drawscale;
                    xy=SimpleTTF.getend2(x1,y1,x2,y2,wscale);
                    path2.lineTo(xy[0],xy[1]);
                    path2.close();//*
                    //ca.drawPath(path2,pa2);//*
                }
                ca.drawPath(path2,pa2);//*
                ca.drawPath(path,pa);//绘制当前线
            }
            
            //绘制高亮
            if(psele!=null&&!AllDeal.isAllFalse(psele)){
                pa.setStyle(Paint.Style.STROKE);
                pa.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                xL=pxL.get(lloc);
                yL=pyL.get(lloc);
                //轮廓
                pa.setColor(0xffffff00);
                if(AllDeal.isAllTrue(psele))//全选
                    pa.setStrokeWidth(8);
                else
                    pa.setStrokeWidth(4);//部分
                path=new Path();//路径
                path.moveTo(drawx0+xL.get(0)*drawscale,drawy0+drawsize-yL.get(0)*drawscale);//路径：当前线起点
                for(int i=1;i<psele.length;i++)//遍历当前线剩余点
                    path.lineTo(drawx0+xL.get(i)*drawscale,drawy0+drawsize-yL.get(i)*drawscale);//路径
                if(!isauto) path.close();//路径：关闭
                ca.drawPath(path,pa);//绘制当前线
                //点
                pa.setColor(0xffff0000);
                pa.setStrokeWidth(20);
                for(int i=0;i<psele.length;i++)
                    if(psele[i])
                        ca.drawPoint(drawx0+xL.get(i)*drawscale,drawy0+drawsize-yL.get(i)*drawscale,pa);
                //序号
                pa.setColor(0xffffffff);
                pa.setStrokeWidth(1);
                pa.setTextSize(30);
                pa.setStyle(Paint.Style.FILL);
                pa.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
                for(int i=0;i<psele.length;i++)
                    ca.drawText(""+(i+1),drawx0+xL.get(i)*drawscale,drawy0+drawsize-yL.get(i)*drawscale,pa);
            }
        }
    }
    //触摸
    private int nowtouchtag;//tag：默认0，有选择为1，无选择为2
    private int touchlloc;//线索引
    private boolean[] touchpsele;//点选择
    private boolean touchisAllfalse;//是否无选择
    private float touchx0,touchy0,touchxd,touchyd;//按下坐标
    private List<Integer> touchxL0,touchyL0,touchxL1,touchyL1;//初始，当前
    private int drawx2X(float x){
        return Math.round((x-drawx0)/drawscale);
    }private int drawy2Y(float y){
        return Math.round((drawy0+drawsize-y)/drawscale);
    }private int drawX2x(int X){
        return Math.round(drawx0+X*drawscale);
    }private int drawY2y(int Y){
        return Math.round(drawy0+drawsize-Y*drawscale);
    }private boolean isVangle(int xd0,int yd0,int xd1,int yd1,double angle){//★向量偏移角度是否大于angle
        if(xd0==0&&yd0==0) return true;
        double cosa=1.0*(xd0*xd1+yd0*yd1)/(SimpleTTF.getlength(xd0,yd0)*SimpleTTF.getlength(xd1,yd1));
        return cosa<Math.cos(angle);
    }//***画线数据***，偏移角度进度条可反复调节已画线条，直至调节完毕〖〗
    private int touch_drawx0,touch_drawy0,touch_drawx1,touch_drawy1;//基准点坐标，新点坐标
    private List<Integer> touch_drawxL,touch_drawyL;//画线坐标
    private int touch_drawpnum;//画线点数
    private int angxd0,angyd0,angxd1,angyd1;//记录初始向量★
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){//按下
            touchisAllfalse=AllDeal.isAllFalse(linepointadapter.psele);
            if(!touchisAllfalse){//有选择
                nowtouchtag=1;//tag
                touchlloc=linepointadapter.lloc;
                touchpsele=linepointadapter.psele;
                touchx0=drawx2X(event.getX());
                touchy0=drawy2Y(event.getY());
                touchxL0=points_xL.get(touchlloc);
                touchyL0=points_yL.get(touchlloc);
            }else{//没有选择，画线开始
                nowtouchtag=2;//tag
                //***画线开始***
                angxd0=0;angyd0=0;//★
                touch_drawx0=drawx2X(event.getX());
                touch_drawy0=drawy2Y(event.getY());
                touch_drawxL=new ArrayList<>();
                touch_drawyL=new ArrayList<>();
                touch_drawpnum=1;
                touch_drawxL.add(touch_drawx0);
                touch_drawyL.add(touch_drawy0);
                //字形数据
                linenum++;//线数
                pointnum++;//点数
                pointnumsL.add(touch_drawpnum);//新线点数
                points_xL.add(touch_drawxL);//x
                points_yL.add(touch_drawyL);//y
                //grd2线点网格
                boolean[] newpsele=new boolean[touch_drawpnum];
                linepointadapter.setvalue(linenum-1,touch_drawpnum,newpsele,0);
                linepointadapter.initdata();
                linepointadapter.notifyDataSetChanged();
                //绘制
                draw.sele(linenum-1,newpsele);
                RUN=RUN_DRAW;
                hdl.postDelayed(rnb,1);
            }
        }else if(event.getAction()==MotionEvent.ACTION_MOVE){//移动
            if(!touchisAllfalse&&nowtouchtag==1){//有选择
                touchxd=drawx2X(event.getX())-touchx0;
                touchyd=drawy2Y(event.getY())-touchy0;
                touchxL1=new ArrayList<>();
                touchyL1=new ArrayList<>();
                for(int i=0;i<touchpsele.length;i++){
                    if(touchpsele[i]){
                        touchxL1.add(i,(int)(touchxL0.get(i)+touchxd));
                        touchyL1.add(i,(int)(touchyL0.get(i)+touchyd));
                    }else{
                        touchxL1.add(i,touchxL0.get(i));
                        touchyL1.add(i,touchyL0.get(i));
                    }
                }
                points_xL.set(touchlloc,touchxL1);
                points_yL.set(touchlloc,touchyL1);
                RUN=RUN_DRAW;
                hdl.postDelayed(rnb,1);
            }else if(touchisAllfalse&&nowtouchtag==2){//没有选择，画线
                //***画线***
                touch_drawx1=drawx2X(event.getX());
                touch_drawy1=drawy2Y(event.getY());
                angxd1=touch_drawx1-touch_drawx0;//★
                angyd1=touch_drawy1-touch_drawy0;//★
                if(SimpleTTF.getlength(angxd1,angyd1)>touch_drawd){
                    if(isVangle(angxd0,angyd0,angxd1,angyd1,touchangled)){//★
                        angxd0=angxd1;//★
                        angyd0=angyd1;//★
                        touch_drawpnum++;
                        touch_drawxL.add(touch_drawx1);
                        touch_drawyL.add(touch_drawy1);
                        touch_drawx0=touch_drawx1;
                        touch_drawy0=touch_drawy1;
                        //字形数据
                        pointnum++;//点数
                        pointnumsL.set(linenum-1,touch_drawpnum);//新线点数
                        points_xL.set(linenum-1,touch_drawxL);//x
                        points_yL.set(linenum-1,touch_drawyL);//y
                        //grd2线点网格
                        boolean[] newpsele=new boolean[touch_drawpnum];
                        linepointadapter.setvalue(linenum-1,touch_drawpnum,newpsele,0);
                        linepointadapter.initdata();
                        linepointadapter.notifyDataSetChanged();
                        //绘制
                        draw.sele(linenum-1,newpsele);
                        RUN=RUN_DRAW;
                        hdl.postDelayed(rnb,1);
                    }else{
                        //★
                        touch_drawxL.set(touch_drawpnum-1,touch_drawx1);
                        touch_drawyL.set(touch_drawpnum-1,touch_drawy1);
                        touch_drawx0=touch_drawx1;
                        touch_drawy0=touch_drawy1;
                        //字形数据
                        points_xL.set(linenum-1,touch_drawxL);//x
                        points_yL.set(linenum-1,touch_drawyL);//y
                        //grd2线点网格
                        /*boolean[] newpsele=new boolean[touch_drawpnum];
                        linepointadapter.setvalue(linenum-1,touch_drawpnum,newpsele,0);
                        linepointadapter.initdata();
                        linepointadapter.notifyDataSetChanged();*/
                        //绘制
                        //draw.sele(linenum-1,newpsele);
                        RUN=RUN_DRAW;
                        hdl.postDelayed(rnb,1);
                    }
                }
            }
        }else if(event.getAction()==MotionEvent.ACTION_UP){//抬起
            if(touchisAllfalse&&nowtouchtag==2){//没有选择，画线结束
                //***画线结束***
                //字形数据
                /*
                 List<Integer> xl=new ArrayList<>();
                 List<Integer> yl=new ArrayList<>();
                 xl.addAll(touch_drawxL);
                 yl.addAll(touch_drawyL);
                 points_xL.set(linenum-1,xl);//x
                 points_yL.set(linenum-1,yl);//y
                 */
                //grd2线点网格
                boolean[] newpsele=new boolean[touch_drawpnum];
                if(drawendstate==1)
                    for(int i=0;i<touch_drawpnum;i++)
                        newpsele[i]=true;
                linepointadapter.setvalue(linenum-1,touch_drawpnum,newpsele,0);
                linepointadapter.initdata();
                linepointadapter.notifyDataSetChanged();
                //绘制
                draw.sele(linenum-1,newpsele);
                RUN=RUN_DRAW;
                hdl.postDelayed(rnb,1);
            }
            nowtouchtag=0;//tag
        }
        return true;
    }
    
    /*
    *字体解析：SimpleTTF
    */
    //数据
    private int codenum,code0num,code1000num;//当前字体字形总数，一段数，二段数
    private int nowindex;//●当前字体选中第几个字形 0 ≤ codenum-1
    private int linenum,pointnum;//当前字形线数，点数
    private List<Integer> pointnumsL;//当前字形每线的点数量
    private List<List<Integer>> points_xL,points_yL;//当前字形每线的点x y坐标列表
    //加载字体文件
    private void loadTTF(int load){
        //加载文件
        switch(load){
            case 0:
                SimpleTTF.readTTFbytex(AllDeal.readAssetsFiletoBytex_filename(c,ValueUsed.ASSETS_ttf_default_letter));
                break;
            case 1:
                SimpleTTF.readTTFbytex(AllDeal.readAssetsFiletoBytex_filename(c,ValueUsed.ASSETS_ttf_default_digit));
                break;
            case 2:
                SimpleTTF.readTTFbytex(AllDeal.readAssetsFiletoBytex_filename(c,ValueUsed.ASSETS_ttf_default_letteranddigit));
                break;
            case 3:
                SimpleTTF.readTTFbytex(AllDeal.readFiletoBytex_path(myttfpath));
                break;
            default:
                break;
        }
        //获取字形数量与形状列表
        SimpleTTF.getCodenumandShapes();
        codenum=SimpleTTF.getCodenum();//字形总数
        code0num=SimpleTTF.getCode0num();//一段数
        code1000num=SimpleTTF.getCode1000num();//二段数
        //更新nowload
        nowload=load;
        //nowindex=0;//当前默认加载第一个字形
        if(nowindex>=codenum)
            nowindex=codenum-1;
    }
    //加载字形
    private void loadShape(int index,boolean auto){
        //解析字形
        SimpleTTF.getShapeByListIndex(index);
        if(auto){
            int w=SimpleTTF.shapeData2auto();
            if(w>0){
                if(readwidth==1){//是否预览线宽
                    width=w;
                    size.setProgress(Math.round(width/shapescale));
                }
                typewidth=w;
                txtsize.setText("width: "+Math.round(1.0f*shaperange*width/0x1000)+" ["+Math.round(1.0f*shaperange*typewidth/0x1000)+"]");
            }
        }
        //获取线数 点数 点数量列表 点坐标列表x 点坐标列表y
        linenum=SimpleTTF.getLinenum();
        pointnum=SimpleTTF.getPointnum();
        pointnumsL=SimpleTTF.getPointnumsL();
        points_xL=SimpleTTF.getPoints_xL();
        points_yL=SimpleTTF.getPoints_yL();
    }
    
    /*
    *字形列表grd1
    */
    //适配器grd1：字形列表
    private class ShapeListAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> data,msg;//数据，提示
        private List<Integer> background;//背景
        private boolean hasxx;//是否包含++
        private boolean hasxx(){
            hasxx=!((ispre&&!isdraw)||nowload!=3);
            return hasxx;
        }
        //
        public ShapeListAdapter(Context context) {//创建实例
            mContext = context;
            initdata();//初始化数据
        }
        private void initdata(){//初始化数据，提示，背景
            data=new ArrayList<>();//数据
            msg=new ArrayList<>();//提示
            background=new ArrayList<>();//背景
            for(int i=0;i<codenum;i++){
                data.add(SimpleTTF.listIndex2str_utf8(i,code0num,code1000num));//数据
                msg.add(Integer.toHexString(SimpleTTF.listIndex2unicode(i,code0num,code1000num)));//提示
                if(i<code0num)//背景
                    background.add(R.drawable.item_x_00f);
                else
                    background.add(R.drawable.item_x_fff);
            }if(hasxx()){//如果含++
                data.add(nowindex,addsign);//数据
                data.add(nowindex+2,addsign);
                msg.add(nowindex,"");//提示
                msg.add(nowindex+2,"");
                background.add(nowindex,R.drawable.item_o_ff0);//背景
                background.add(nowindex+2,R.drawable.item_o_ff0);
            }background.set(hasxx?nowindex+1:nowindex,nowindex<code0num?R.drawable.item_x_00f_choosed:R.drawable.item_x_fff_choosed);
        }
        //自定义
        @Override//获取项视图
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grd_symbol, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textView = convertView.findViewById(R.id.item_symbol);//内容
                viewHolder.textView.setTextSize(grditemtsize);
                viewHolder.msg=convertView.findViewById(R.id.msg);//提示
                viewHolder.msg.setTextSize(grditemtsize2);
                ViewGroup.LayoutParams layoutParams=viewHolder.textView.getLayoutParams();
                layoutParams.width=grditemw;
                layoutParams.height=grditemw;
                viewHolder.textView.setLayoutParams(layoutParams);//尺寸
                layoutParams=viewHolder.msg.getLayoutParams();
                layoutParams.width=grditemw;
                layoutParams.height=grditemw;
                viewHolder.msg.setLayoutParams(layoutParams);//尺寸
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //内容
            viewHolder.textView.setText(data.get(position));
            viewHolder.textView.setTypeface(nowtypeface);//字体
            viewHolder.msg.setText(msg.get(position));
            //背景
            viewHolder.textView.setBackground(getDrawable(background.get(position)));
            return convertView;
        }
        @Override//获取总项数
        public int getCount() {
            return data.size();
        }
        @Override//获取项内容
        public Object getItem(int position) {
            return data.get(position);
        }
        @Override//获取项id
        public long getItemId(int position) {
            return position;
        }
        private class ViewHolder {//
            TextView textView;
            TextView msg;
        }
    }
    //grd1项点击
    private class ShapeListOnItemClick implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AllDeal.hideSoftKeyboard(c,view);
            int clickindex=nowindex;//点击索引，不包括++
            if(shapelistadapter.hasxx){
                if(position==nowindex){//左+
                    addGrd1(0);//新增
                }else if(position==nowindex+2){//右+
                    addGrd1(1);//新增
                }else if(position==nowindex+1){//原处点击
                    changeGrd1(clickindex);
                }else if(position<nowindex){//切换
                    clickindex=position;
                    changeGrd1(clickindex);
                }else{//切换
                    clickindex=position-2;
                    changeGrd1(clickindex);
                }
            }else{
                if(position==nowindex){//原处点击
                    changeGrd1(clickindex);
                }else{//切换
                    clickindex=position;
                    changeGrd1(clickindex);
                }
            }
        }
    }
    //grd1操作
    //grd1切换符号
    private void changeGrd1(int newindex){
        nowindex=newindex;
        initindex();
        if(ispre&&!isdraw){//预览
            updateUIinsertPreview();
            t("Input",true);
        }else{
            t("Switch",true);
        }
    }
    //grd1新增
    private void addGrd1(int LorR){
        if(!shapelistadapter.hasxx||(nowindex<code0num&&code0num>=0x1000)||(nowindex>=code0num&&code1000num>=0xf000)){
            t("cannot be increased",false);
            return;
        }
        //更新数据
        codenum++;//总字数
        if(nowindex<code0num)//一段新增
            code0num++;
        else//二段新增
            code1000num++;
        int addloc=LorR==0?0:1;//新增位置：左右
        nowindex+=addloc;
        //默认新增字形信息
        byte[] shapebtx;
        if(SimpleTTF.str2shapeData(defnew,shaperange)){
            shapebtx=SimpleTTF.ShapeToBytes();
        }else{
            int def_pointnum=ValueUsed.Def_new_shape.length;
            List<Integer> lp=new ArrayList<>(),lx0=new ArrayList<>(),ly0=new ArrayList<>();
            List<List<Integer>> lx=new ArrayList<>(),ly=new ArrayList<>();
            lp.add(def_pointnum);
            for(int i=0;i<def_pointnum;i++){
                lx0.add(ValueUsed.Def_new_shape[i][0]);
                ly0.add(ValueUsed.Def_new_shape[i][1]);
            }
            lx.add(lx0);
            ly.add(ly0);
            //新增字形字节数组
            shapebtx=SimpleTTF.ShapeToBytes(1,def_pointnum,lp,lx,ly);
        }
        SimpleTTF.addShape(code0num,code1000num,nowindex,shapebtx);
        AllDeal.saveBytexinFile_file(SimpleTTF.DataToTTFbytes(),new File(myttfpath));
        
        initload(nowload);//切换字体
        
        t("Add",true);//提示
    }
    //grd1删除
    private void delGrd1(){
        if(nowload!=3||(nowindex<code0num&&code0num<2)||(nowindex>=code0num&&code1000num<2)){
            t("cannot be deleted",false);//提示
            t2("cannot be deleted");//提示
            return;
        }
        //更新字数
        codenum--;//总字数
        if(nowindex<code0num)//一段删除
            code0num--;
        else//二段删除
            code1000num--;
        //删除字形字节数组
        SimpleTTF.delShape(code0num,code1000num,nowindex);
        AllDeal.saveBytexinFile_file(SimpleTTF.DataToTTFbytes(),new File(myttfpath));
        //更新选中
        int ad=nowindex==codenum?-1:0;
        nowindex+=ad;

        initload(nowload);//切换字体
        
        t("Del",true);//提示
        t2(AllDeal.getDateandTime_str()+" Del");//提示
    }
    
    
    /*
    *线点列表grd2
    */
    //适配器grd2：线点列表
    private class LinePointAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> data,msg;//数据，提示
        private List<Integer> background;//背景
        private int addpos;//**当前++位置，0线 >0点，不包括++
        private int lloc,pnum;//*线索引，点数量
        private boolean[] psele;//*点选择
        //
        public LinePointAdapter(Context context) {//创建实例
            mContext=context;
            initvalue();
            initdata();//初始化数据
        }
        private void initvalue(){//初始化数值
            lloc=linenum-1;//线索引，最后线
            pnum=pointnumsL.get(lloc).intValue();//点数量
            psele=new boolean[pnum];//默认全不选
            addpos=0;//默认++
        }private void setvalue(int newlloc,int newpnum,boolean[] newpsele,int newaddpos){
            lloc=newlloc;
            pnum=newpnum;
            psele=newpsele;
            addpos=newaddpos;
        }private boolean[] unseleall(){//全不选
            if(psele!=null)
                for(int i=0;i<psele.length;i++)
                    psele[i]=false;
            return psele;
        }
        private void initdata(){//初始化数据
            data=new ArrayList<>();//数据
            msg=new ArrayList<>();//提示
            background=new ArrayList<>();//背景
            for(int i=0;i<linenum;i++){//L*遍历线
                data.add("L");
                msg.add(""+(i+1));
                background.add(R.drawable.item_x_f70);//默认
            }if(AllDeal.isAllTrue(psele))//全选高亮
                background.set(lloc,R.drawable.item_x_f70_turnon);
            else//展开(仅选中)
                background.set(lloc,R.drawable.item_x_f70_choosed);
            for(int i=0,loc=0;i<pnum;i++){//P*遍历线点
                loc=lloc+1+i;//插入索引
                data.add(loc,"P");
                msg.add(loc,""+(i+1));
                if(psele[i])//选择高亮
                    background.add(loc,R.drawable.item_x_fff_turnon);
                else//默认
                    background.add(loc,R.drawable.item_x_fff);
            }if(addpos>0&&!psele[addpos-1])//仅选中
                background.set(lloc+addpos,R.drawable.item_x_fff_choosed);
            data.add(lloc+addpos,"+");
            msg.add(lloc+addpos,"");
            background.add(lloc+addpos,R.drawable.item_o_ff0);
            data.add(lloc+addpos+2,"×");
            msg.add(lloc+addpos+2,"");
            background.add(lloc+addpos+2,R.drawable.item_o_f00);
            data.add(lloc+addpos+3,"+");
            msg.add(lloc+addpos+3,"");
            background.add(lloc+addpos+3,R.drawable.item_o_ff0);
        }private int getaddpos(){
            return addpos;
        }private int getlloc(){
            return lloc;
        }private int getpnum(){
            return pnum;
        }private boolean[] getpsele(){
            return psele;
        }
        //操作
        private void changeline(int newloc){//切换线
            lloc=newloc;//线索引，最后线
            pnum=pointnumsL.get(lloc).intValue();//点数量
            psele=new boolean[pnum];//默认全选
            for(int i=0;i<psele.length;i++)
                psele[i]=true;
            addpos=0;//++
        }
        private void click(int pos,boolean islong){//点击，长按
            if(pos<lloc){//切换展开左侧L
                changeline(pos);
            }else if(pos>lloc+3+pnum){//切换展开右侧L
                changeline(pos-3-pnum);
            }else{
                if(addpos==0){//选中L
                    if(pos==lloc+1){//再次点击L
                        if(AllDeal.isAllTrue(psele)){//全亮则全灭
                            for(int i=0;i<psele.length;i++)
                                psele[i]=false;
                        }else{//否则全亮
                            for(int i=0;i<psele.length;i++)
                                psele[i]=true;
                        }
                    }else if(pos==lloc){//左+
                        linenum++;//线数
                        pointnum++;//点数
                        pointnumsL.add(lloc,1);//新线点数
                        List<Integer> xL=new ArrayList<>();
                        List<Integer> yL=new ArrayList<>();
                        xL.add(drawx2X(drawsize/2));
                        yL.add(drawy2Y(drawsize/2));
                        points_xL.add(lloc,xL);//x
                        points_yL.add(lloc,yL);//y
                        //lloc=lloc;
                        pnum=1;
                        psele=new boolean[]{true};
                        addpos=1;
                    }else if(pos==lloc+3){//右+
                        linenum++;//线数
                        pointnum++;//点数
                        pointnumsL.add(lloc+1,1);//新线点数
                        List<Integer> xL=new ArrayList<>();
                        List<Integer> yL=new ArrayList<>();
                        xL.add(drawx2X(drawsize/2));
                        yL.add(drawy2Y(drawsize/2));
                        points_xL.add(lloc+1,xL);//x
                        points_yL.add(lloc+1,yL);//y
                        lloc=lloc+1;
                        pnum=1;
                        psele=new boolean[]{true};
                        addpos=1;
                    }else if(pos==lloc+2){//删除×
                        if(linenum<2){//剩一条线
                            /*t("cannot be deleted",false);
                            return;*/
                            linenum=1;//线数
                            pointnum=1;//点数
                            pointnumsL.set(0,1);//新线点数
                            List<Integer> xL=new ArrayList<>();
                            List<Integer> yL=new ArrayList<>();
                            xL.add(drawx2X(drawsize/2));
                            yL.add(drawy2Y(drawsize/2));
                            points_xL.set(0,xL);//x
                            points_yL.set(0,yL);//y
                            lloc=0;
                            pnum=1;
                            psele=new boolean[1];
                            addpos=0;
                        }else{
                            linenum--;//线数
                            pointnum-=pointnumsL.get(lloc);//点数
                            pointnumsL.remove(lloc);//线点数
                            points_xL.remove(lloc);//x
                            points_yL.remove(lloc);//y
                            lloc=lloc>linenum-1?linenum-1:lloc;
                            pnum=pointnumsL.get(lloc);
                            psele=new boolean[pnum];
                            //addpos=0;
                        }
                    }else{//点击P
                        addpos=pos-lloc-3;
                        if(islong)
                            psele[addpos-1]=!psele[addpos-1];
                        else{
                            psele=new boolean[pnum];
                            psele[addpos-1]=true;
                        }
                    }
                }else{//未选中L
                    if(pos==lloc){//点击L
                        addpos=0;
                        if(AllDeal.isAllTrue(psele)){//全亮则全灭
                            for(int i=0;i<psele.length;i++)
                                psele[i]=false;
                        }else{//否则全亮
                            for(int i=0;i<psele.length;i++)
                                psele[i]=true;
                        }
                    }else if(pos==lloc+addpos){//左+
                        pointnum++;//点数
                        pointnumsL.set(lloc,pnum+1);//新线点数
                        List<Integer> xL=points_xL.get(lloc);
                        List<Integer> yL=points_yL.get(lloc);
                        xL.add(addpos-1,drawx2X(drawsize/2));
                        yL.add(addpos-1,drawy2Y(drawsize/2));
                        points_xL.set(lloc,xL);//x
                        points_yL.set(lloc,yL);//y
                        pnum++;
                        psele=new boolean[pnum];
                        psele[addpos-1]=true;
                    }else if(pos==lloc+addpos+3){//右+
                        pointnum++;//点数
                        pointnumsL.set(lloc,pnum+1);//新线点数
                        List<Integer> xL=points_xL.get(lloc);
                        List<Integer> yL=points_yL.get(lloc);
                        xL.add(addpos,drawx2X(drawsize/2));
                        yL.add(addpos,drawy2Y(drawsize/2));
                        points_xL.set(lloc,xL);//x
                        points_yL.set(lloc,yL);//y
                        pnum++;
                        psele=new boolean[pnum];
                        psele[addpos]=true;
                        addpos++;
                    }else if(pos==lloc+addpos+2){//删除×
                        if(linenum<2&&pnum<2){//剩一线一点
                            /*t("cannot be deleted",false);
                             return;*/
                            linenum=1;//线数
                            pointnum=1;//点数
                            pointnumsL.set(0,1);//新线点数
                            List<Integer> xL=new ArrayList<>();
                            List<Integer> yL=new ArrayList<>();
                            xL.add(drawx2X(drawsize/2));
                            yL.add(drawy2Y(drawsize/2));
                            points_xL.set(0,xL);//x
                            points_yL.set(0,yL);//y
                            lloc=0;
                            pnum=1;
                            psele=new boolean[1];
                            addpos=0;
                        }else if(pnum<2){//剩一点
                            linenum--;//线数
                            pointnum--;//点数
                            pointnumsL.remove(lloc);//线点数
                            points_xL.remove(lloc);//x
                            points_yL.remove(lloc);//y
                            lloc=lloc>linenum-1?linenum-1:lloc;
                            pnum=pointnumsL.get(lloc);
                            psele=new boolean[pnum];
                            addpos=0;
                        }else{
                            pointnum--;//点数
                            pointnumsL.set(lloc,pnum-1);//新线点数
                            List<Integer> xL=points_xL.get(lloc);
                            List<Integer> yL=points_yL.get(lloc);
                            xL.remove(addpos-1);
                            yL.remove(addpos-1);
                            points_xL.set(lloc,xL);//x
                            points_yL.set(lloc,yL);//y
                            pnum--;
                            psele=new boolean[pnum];
                            addpos=addpos>pnum?pnum:addpos;
                            psele[addpos-1]=true;
                        }
                    }else if(pos<lloc+addpos){//切换左侧P
                        addpos=pos-lloc;
                        if(islong)
                            psele[addpos-1]=!psele[addpos-1];
                        else{
                            psele=new boolean[pnum];
                            psele[addpos-1]=true;
                        }
                    }else if(pos>lloc+addpos+3){//切换右侧P
                        addpos=pos-lloc-3;
                        if(islong)
                            psele[addpos-1]=!psele[addpos-1];
                        else{
                            psele=new boolean[pnum];
                            psele[addpos-1]=true;
                        }
                    }else{//点击当前P
                        if(islong)
                            psele[addpos-1]=!psele[addpos-1];
                        else{
                            psele=new boolean[pnum];
                            psele[addpos-1]=true;
                        }
                    }
                }
            }
            initdata();
            notifyDataSetChanged();
            
            draw.sele(lloc,psele);
            RUN=RUN_DRAW;
            hdl.postDelayed(rnb,1);
        }
        //自定义
        @Override//获取项视图
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grd_symbol, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textView = convertView.findViewById(R.id.item_symbol);//内容
                viewHolder.textView.setTextSize(grd2itemtsize);
                viewHolder.msg=convertView.findViewById(R.id.msg);//提示
                viewHolder.msg.setTextSize(grd2itemtsize2);
                ViewGroup.LayoutParams layoutParams=viewHolder.textView.getLayoutParams();
                layoutParams.width=grd2itemw;
                layoutParams.height=grd2itemw;
                viewHolder.textView.setLayoutParams(layoutParams);//尺寸
                layoutParams=viewHolder.msg.getLayoutParams();
                layoutParams.width=grd2itemw;
                layoutParams.height=grd2itemw;
                viewHolder.msg.setLayoutParams(layoutParams);//尺寸
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //内容
            viewHolder.textView.setText(data.get(position));
            viewHolder.msg.setText(msg.get(position));
            //背景
            viewHolder.textView.setBackground(getDrawable(background.get(position)));
            return convertView;
        }
        @Override//获取总项数
        public int getCount() {
            return data.size();
        }
        @Override//获取项内容
        public Object getItem(int position) {
            return data.get(position);
        }
        @Override//获取项id
        public long getItemId(int position) {
            return position;
        }
        private class ViewHolder {//
            TextView textView;
            TextView msg;
        }
    }
    //grd2项点击
    private class PointsOnItemClick implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AllDeal.hideSoftKeyboard(c,view);
            linepointadapter.click(position,false);
        }
    }//grd2项长按
    private class PointsOnItemLongClick implements OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            AllDeal.hideSoftKeyboard(c,view);
            linepointadapter.click(position,true);
            return true;
        }
    }
    
    /*
    *事件
    */
    //seekbar：输入框字体大小
    private class EditSizeSeekBar implements OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            edt.setTextSize(progress);
        }@Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            AllDeal.hideSoftKeyboard(c,seekBar);
        }@Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
    //seekbar：线宽
    private class LineWidthSeekBar implements OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            width=Math.round(progress*shapescale);
            txtsize.setText("width: "+Math.round(1.0f*shaperange*width/0x1000)+" ["+Math.round(1.0f*shaperange*typewidth/0x1000)+"]");
            if(nowT>50){
                RUN=RUN_DRAW;
                hdl.postDelayed(rnb,1);
            }
        }@Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            AllDeal.hideSoftKeyboard(c,seekBar);
        }@Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            ShPref.putInt(ShPref.PRE_SVGLINEWIDTH,Math.round(1.0f*shaperange*width/0x1000),ActEd_Svg.this);
        }
    }
    //seekbar：触摸距离
    private class TouchRangeSeekBar implements OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            touchrange=progress;
            if(touchrange<1) touchrange=1;
            touch_drawd=(int)(0x1000*touchrange/100.0);
            touchtxt.setText("distance: "+touchrange+"%");
        }@Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            AllDeal.hideSoftKeyboard(c,seekBar);
        }@Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            ShPref.putInt(ShPref.PRE_SVGDRAWDISTANCE,touchrange,ActEd_Svg.this);
        }
    }
    //seekbar：触摸角度
    private class TouchAngleSeekBar implements OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            touchangle=progress;
            touchangled=Math.PI*touchangle/100;
            touchangle_txt.setText("angle: "+touchangle+"%");//*
        }@Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            AllDeal.hideSoftKeyboard(c,seekBar);
        }@Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            ShPref.putInt(ShPref.PRE_SVGDRAWANGLE,touchangle,ActEd_Svg.this);
        }
    }
    //点击
    @Override
    public void onClick(View v) {
        AllDeal.hideSoftKeyboard(c,v);
        switch(v.getId()){
            case R.id.preview://预览
                ispre=!ispre;
                updateUIshow(isauto,isdraw,ispre,nowload);
                shapelistadapter.initdata();
                shapelistadapter.notifyDataSetChanged();
                if(ispre){
                    edt.setText(strpre==null||strpre.length()<1?"":strpre);//上次预览
                    preview.setText("◆");
                    preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_o_fff_choosed));
                    edt.setTypeface(nowtypeface);//字体
                }else{
                    String strnow=edt.getText().toString();
                    if(strnow!=null&&strnow.length()>0)
                        strpre=strnow;//保存上次预览
                    loadShape(nowindex,isauto);
                    if(!isdraw&&isauto){//自动
                        edt.setText(SimpleTTF.autoData2autostr(width,shaperange,linenum,pointnumsL,points_xL,points_yL));
                    }else if(!isdraw&&!isauto){//完整
                        edt.setText(SimpleTTF.shapeData2str(shaperange,linenum,pointnumsL,points_xL,points_yL));
                    }preview.setText("◇");
                    preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_o_fff));
                    edt.setTypeface(Typeface.MONOSPACE);//字体
                }
                break;
            case R.id.def1://默认
                initload(0);
                break;
            case R.id.def2://默认
                initload(1);
                break;
            case R.id.def3://默认
                initload(2);
                break;
            case R.id.my://我的
                initload(3);
                break;
            case R.id.clear://清空
                edt.setText("");
                t("Clear",true);
                break;
            case R.id.copy://复制
                String stredt=edt.getText().toString();
                if(stredt!=null&&stredt.length()>0){
                    strcopy=stredt;
                    t("Copy",true);
                }else
                    t("no content",false);
                break;
            case R.id.paste://粘贴
                if(strcopy==null||strcopy.length()<1)
                    t("no content copied",false);
                else{
                    //edt.setText(strcopy);
                    AllDeal.edtInsert(strcopy,edt);
                    t("Paste",true);
                }
                break;
            case R.id.submit://提交
                if(isauto&&isdraw){
                    if(SimpleTTF.auto2shapeData(width,linenum,pointnum,pointnumsL,points_xL,points_yL)){
                        byte[] shapebtx=SimpleTTF.ShapeToBytes();
                        if(shapebtx!=null){//获取数据
                            ShPref.putInt(ShPref.PRE_SVGLINEWIDTH,Math.round(1.0f*shaperange*width/0x1000),ActEd_Svg.this);//*
                            SimpleTTF.changeShape(nowindex,shapebtx);
                            AllDeal.saveBytexinFile_file(SimpleTTF.DataToTTFbytes(),new File(myttfpath));
                            initload(nowload);//切换字体
                            t("Submit",true);//提示
                        }else{
                            t("unable to submit",false);
                        }
                    }else{
                        t("unable to submit",false);
                    }
                }else if(isauto&&!isdraw){
                    int r=SimpleTTF.autostr2autoData(edt.getText().toString(),shaperange);
                    if(r>0){//获取数据
                        width=r;//线宽
                        size.setProgress(Math.round(width/shapescale));
                        if(SimpleTTF.auto2shapeData(width)){
                            byte[] shapebtx=SimpleTTF.ShapeToBytes();
                            if(shapebtx!=null){//获取数据
                                ShPref.putInt(ShPref.PRE_SVGLINEWIDTH,Math.round(1.0f*shaperange*width/0x1000),ActEd_Svg.this);//*
                                SimpleTTF.changeShape(nowindex,shapebtx);
                                AllDeal.saveBytexinFile_file(SimpleTTF.DataToTTFbytes(),new File(myttfpath));
                                initload(nowload);//切换字体
                                t("Submit",true);//提示
                            }else{
                                t("unable to submit",false);
                            }
                        }else{
                            t("unable to submit",false);
                        }
                    }else{
                        t("unable to submit",false);
                    }
                }else if(!isauto&&isdraw){
                    byte[] shapebtx=SimpleTTF.ShapeToBytes(linenum,pointnum,pointnumsL,points_xL,points_yL);
                    if(shapebtx!=null){//获取数据
                        SimpleTTF.changeShape(nowindex,shapebtx);
                        AllDeal.saveBytexinFile_file(SimpleTTF.DataToTTFbytes(),new File(myttfpath));
                        initload(nowload);//切换字体
                        t("Submit",true);//提示
                    }else{
                        t("unable to submit",false);
                    }
                }else{
                    if(SimpleTTF.str2shapeData(edt.getText().toString(),shaperange)){//获取数据
                        linenum=SimpleTTF.getLinenum();
                        pointnum=SimpleTTF.getPointnum();
                        pointnumsL=SimpleTTF.getPointnumsL();
                        points_xL=SimpleTTF.getPoints_xL();
                        points_yL=SimpleTTF.getPoints_yL();
                        //修改字形字节数组
                        byte[] shapebtx=SimpleTTF.ShapeToBytes(linenum,pointnum,pointnumsL,points_xL,points_yL);
                        SimpleTTF.changeShape(nowindex,shapebtx);
                        AllDeal.saveBytexinFile_file(SimpleTTF.DataToTTFbytes(),new File(myttfpath));
                        initload(nowload);//切换字体
                        t("Submit",true);//提示
                    }else{
                        t("unable to submit",false);
                    }
                }
                break;
            case R.id.change://切换
                isauto=!isauto;
                if(isauto)
                    t("Stroke",true);
                updateUIshow(isauto,isdraw,ispre,nowload);
                initindex();
                break;
            case R.id.change2://切换等价
                isdraw=!isdraw;
                updateUIshow(isauto,isdraw,ispre,nowload);
                initindex();
                break;
            case R.id.readlinewidth://预览线宽
                if(readwidth==0){//转为预览
                    readwidth=1;
                    readlinewidth.setText("■");
                    readlinewidth.setBackground(getDrawable(R.drawable.button_x_fff_turnon));
                    
                    initindex();//*
                }else{//转为不预览
                    readwidth=0;
                    readlinewidth.setText("□");
                    readlinewidth.setBackground(getDrawable(R.drawable.button_x_fff));
                }ShPref.putInt(ShPref.PRE_SVGLOADLINESTATE,readwidth,ActEd_Svg.this);
                break;
            case R.id.defseleall://默认
                if(drawendstate==0){//转为全选
                    drawendstate=1;
                    defseleall.setText("■");
                    defseleall.setBackground(getDrawable(R.drawable.button_x_fff_turnon));
                }else{//转为全不选
                    drawendstate=0;
                    defseleall.setText("□");
                    defseleall.setBackground(getDrawable(R.drawable.button_x_fff));
                    
                    linepointadapter.unseleall();//*
                    linepointadapter.initdata();
                    linepointadapter.notifyDataSetChanged();
                    
                    draw.sele(linepointadapter.lloc,linepointadapter.psele);
                    RUN=RUN_DRAW;
                    hdl.postDelayed(rnb,1);
                }ShPref.putInt(ShPref.PRE_SVGDRAWENDSTATE,drawendstate,ActEd_Svg.this);
                break;
            case R.id.back://返回
                ActEd_Svg.this.finish();
                break;
            case R.id.del://删除
                delGrd1();
                break;
            default:
                break;
        }
    }
    
    /*
    *提示预方法
    */
    private int tcount,nowT=0;
    private Handler hdlt=new Handler();
    private Runnable rnbt=new Runnable(){
        @Override
        public void run() {
            nowT++;
            if(tcount>0)
                tcount--;
            else
                txt.setText(AllDeal.getDateandTime_str());
            hdlt.postDelayed(this,100);
        }
    };
    private void t(String s,boolean addtime){
        tcount=20;
        if(addtime)
            txt.setText(AllDeal.getDateandTime_str()+" "+s);
        else
            txt.setText(s);
    }
    private void t2(String s){
        txt2.setText(s);
    }
    private void tt(String s){
        Toast.makeText(c,s,Toast.LENGTH_LONG).show();
    }
    
    /*
    *刷新界面
    */
    //刷新预览文本框
    private void updateUIinsertPreview(){
        AllDeal.edtInsert(SimpleTTF.listIndex2str_utf8(nowindex,code0num,code1000num),edt);
    }
    //刷新预览大字
    private void updateUIpreviewImg(){
        imgt.setText(SimpleTTF.listIndex2str_utf8(nowindex,code0num,code1000num));//预览字
        imgmsg.setText(getUnicodeMsg_index(str_shapemsg,nowindex));//预览字信息
    }//从listIndex到字符信息
    private String getUnicodeMsg_index(String strMsg,int index){
        //unicode编码
        int uni=SimpleTTF.listIndex2unicode(index,code0num,code1000num);
        String msg;//字符信息
        if(uni<0x1000){//一段
            int loc=strMsg.indexOf("●"+uni+"●");
            if(loc!=-1){
                loc+=("●"+uni+"●").length()+1;
                int loc2=strMsg.indexOf("●",loc)-1;
                msg=strMsg.substring(loc,loc2);
            }else
                msg="not used";
        }else{//二段
            msg=""+(uni-0x1000);
        }
        msg+="\n"
            +"\n["+Integer.toHexString(uni)+"("+uni+")]"
            +"\n["+new String(SimpleTTF.unicodeOneToUtf8bytes(uni),StandardCharsets.UTF_8)+"]";
        return msg;
    }
    //字体有变动，刷新字体
    private void updateTypeface(int load,boolean pre){
        switch(load){
            case 0:
                nowtypeface=Typeface.createFromAsset(c.getAssets(),ValueUsed.ASSETS_ttf_default_letter);
                break;
            case 1:
                nowtypeface=Typeface.createFromAsset(c.getAssets(),ValueUsed.ASSETS_ttf_default_digit);
                break;
            case 2:
                nowtypeface=Typeface.createFromAsset(c.getAssets(),ValueUsed.ASSETS_ttf_default_letteranddigit);
                break;
            case 3:
                nowtypeface=Typeface.createFromFile(myttfpath);
                break;
            default:
                break;
        }
        imgt.setTypeface(nowtypeface);//预览字
        if(pre)//预览
            edt.setTypeface(nowtypeface);
    }
    //切换字体按钮
    private void updateUIloadButton(int exload,int newload){
        switch(exload){
            case 0:
                def1.setBackgroundDrawable(getDrawable(R.drawable.button_x_fff));
                break;
            case 1:
                def2.setBackgroundDrawable(getDrawable(R.drawable.button_x_fff));
                break;
            case 2:
                def3.setBackgroundDrawable(getDrawable(R.drawable.button_x_fff));
                break;
            case 3:
                my.setBackgroundDrawable(getDrawable(R.drawable.button_x_fff));
                break;
            default:
                break;
        }
        switch(newload){
            case 0:
                def1.setBackgroundDrawable(getDrawable(R.drawable.button_x_fff_turnon));
                break;
            case 1:
                def2.setBackgroundDrawable(getDrawable(R.drawable.button_x_fff_turnon));
                break;
            case 2:
                def3.setBackgroundDrawable(getDrawable(R.drawable.button_x_fff_turnon));
                break;
            case 3:
                my.setBackgroundDrawable(getDrawable(R.drawable.button_x_fff_turnon));
                break;
            default:
                break;
        }
    }
    //显示内容：
    private void updateUIshow(boolean auto,boolean draw,boolean pre,int load){
        int V=View.VISIBLE,G=View.GONE;
        int X=R.drawable.button_x_fff,XC=R.drawable.button_x_fff_turnon;
        int O=R.drawable.button_o_fff,OC=R.drawable.button_o_fff_choosed;
        boolean T=true,F=false;
        //isauto自动/完整 isdraw绘制/文本 ispre预览/编辑 nowload==3我的/默认
        int code=(auto?1:0)+(draw?2:0)+(pre?4:0)+(load==3?8:0);
        switch(code){
            case 0://完整 文本 编辑 默认
                updateUIshow(G,V,G,G,G,G,G,X,X,O,G,V,F);
                break;
            case 1://自动 文本 编辑 默认
                updateUIshow(G,V,G,G,G,G,G,XC,X,O,G,V,F);
                break;
            case 2://完整 绘制 编辑 默认
                updateUIshow(G,G,G,G,G,V,G,X,XC,O,V,G,F);
                break;
            case 3://自动 绘制 编辑 默认
                updateUIshow(G,G,G,G,G,V,V,XC,XC,O,V,G,F);
                break;
            case 4://完整 文本 预览 默认
                updateUIshow(V,G,G,G,G,G,G,X,X,OC,G,V,F);
                break;
            case 5://自动 文本 预览 默认
                updateUIshow(V,G,G,G,G,G,G,XC,X,OC,G,V,F);
                break;
            case 6://完整 绘制 预览 默认
                updateUIshow(G,G,G,G,G,V,G,X,XC,OC,V,G,F);
                break;
            case 7://自动 绘制 预览 默认
                updateUIshow(G,G,G,G,G,V,V,XC,XC,OC,V,G,F);
                break;
            case 8://完整 文本 编辑 我的
                updateUIshow(V,V,V,V,V,G,G,X,X,O,G,V,T);
                break;
            case 9://自动 文本 编辑 我的
                updateUIshow(V,V,V,V,V,G,G,XC,X,O,G,V,T);
                break;
            case 10://完整 绘制 编辑 我的
                updateUIshow(G,G,G,V,V,V,G,X,XC,O,V,G,T);
                break;
            case 11://自动 绘制 编辑 我的
                updateUIshow(G,G,G,V,V,V,V,XC,XC,O,V,G,T);
                break;
            case 12://完整 文本 预览 我的
                updateUIshow(V,G,G,G,V,G,G,X,X,OC,G,V,T);
                break;
            case 13://自动 文本 预览 我的
                updateUIshow(V,G,G,G,V,G,G,XC,X,OC,G,V,T);
                break;
            case 14://完整 绘制 预览 我的
                updateUIshow(G,G,G,V,V,V,G,X,XC,OC,V,G,T);
                break;
            case 15://自动 绘制 预览 我的
                updateUIshow(G,G,G,V,V,V,V,XC,XC,OC,V,G,T);
                break;
            default:
                break;
        }
        if(isauto)
            readlinewidth.setVisibility(V);
        else
            readlinewidth.setVisibility(G);
    }
    private void updateUIshow(int clearShow,int copyShow,int pasteShow,int submitShow,int delShow,int grd2Show,int sizeShow,int changeDrawable,int change2Drawable,int previewDrawable,int imgShow,int edtShow,boolean sizeEnable){
        //操作按钮
        clear.setVisibility(clearShow);
        copy.setVisibility(copyShow);
        paste.setVisibility(pasteShow);
        submit.setVisibility(submitShow);
        //删除
        del.setVisibility(delShow);
        //线点网格
        grd2.setVisibility(grd2Show);
        //线宽进度条
        seeklayout.setVisibility(sizeShow);
        //三个状态按钮
        change.setBackgroundDrawable(getResources().getDrawable(changeDrawable));
        change2.setBackgroundDrawable(getResources().getDrawable(change2Drawable));
        preview.setBackgroundDrawable(getResources().getDrawable(previewDrawable));
        //图像文本
        draw.setVisibility(imgShow);
        touchrlt.setVisibility(imgShow);
        touchangle_rlt.setVisibility(imgShow);
        defseleall.setVisibility(imgShow);
        lnl.setVisibility(edtShow);
        //线宽进度条是否可用
        //size.setEnabled(sizeEnable);
        //txtsize.setTextColor(sizeEnable?0xff000000:0xffbbbbbb);
    }
}
