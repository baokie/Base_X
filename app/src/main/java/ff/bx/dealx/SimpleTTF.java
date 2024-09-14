package ff.bx.dealx;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import java.nio.charset.StandardCharsets;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
//支持Unicode范围0000-FFFF
public class SimpleTTF {
    /*
    *字体数据：codenum，code0num，code1000num
    *字形数据：(side)，linenum，pointnum，pointNumsL，points_xL，points_yL
    *
    */
    
    //默认表内容
    //hmtx表开头无意义*
    private final static byte[] ttf_hmtx_start=new byte[]{0x10,0,0,(byte)0x88,0,0,0,0,0x10,0,0,0,0x10,0};
    //loca表开头无意义*
    private final static byte[] ttf_loca_start=new byte[]{0,0,0,0,0,0,0,0x28,0,0,0,0x28,0,0,0,0x28};
    
    //表名
    private final static int TBL_HEAD=0,TBL_HHEA=1,TBL_HMTX=2,TBL_OSI2=3,
                             TBL_MAXP=4,TBL_CMAP=5,TBL_LOCA=6,TBL_GLYF=7,
                             TBL_NAME=8,TBL_POST=9,TBL_CVT_=10,TBL_GASP=11;
    
    
    //表名
    private final static String[] tbl_name=new String[]{"head","hhea","hmtx","OS/2","maxp","cmap","loca","glyf","name","post","cvt ","gasp"};
    private static int[] tbl_loc;//tbl_name[0]←ttf_tbls[tbl_loc[0]]
    //hex列表
    private static byte[] ttf_title,ttf_tbllist[],ttf_tbls[];//文件头，表列表，表内容
    private static int[] ttf_tbllisti[];//表列表转int数组
    private static String[] ttf_tbllists;//表列表转utf8字符串
    
    //读取TTF文件数组(固定Simple格式)
    public static void readTTFbytex(byte[] ttf_all){
        //title文件头
        int titlelength=12;//文件头长度
        ttf_title=new byte[titlelength];
        for(int i=0;i<titlelength;i++){
            ttf_title[i]=ttf_all[i];
        }
        //表数量
        int num=ttf_title[5]&0xff;//12表数量
        int listlength=16;//单个表长度
        ttf_tbllist=new byte[num][listlength];//表列表
        ttf_tbllisti=new int[num][];//表列表转int数组
        ttf_tbllists=new String[num];//表列表转utf8字符串
        ttf_tbls=new byte[num][];//表内容
        //tbllist表列表, tbls表内容
        for(int i=0;i<num;i++){
            //表列表
            for(int j=0;j<listlength;j++){
                ttf_tbllist[i][j]=ttf_all[titlelength+i*listlength+j];
            }
            //表列表转int数组
            ttf_tbllisti[i]=AllDeal.bytexTointx(ttf_tbllist[i]);
            //表列表转字符串
            ttf_tbllists[i]=AllDeal.bytexTostr_utf8(ttf_tbllist[i]);
            //表内容
            ttf_tbls[i]=new byte[ttf_tbllisti[i][3]];
            for(int k=0;k<ttf_tbllisti[i][3];k++){
                ttf_tbls[i][k]=ttf_all[ttf_tbllisti[i][2]+k];
            }
        }
        //对应关系
        tbl_loc=new int[tbl_name.length];
        for(int i=0;i<tbl_name.length;i++){
            tbl_loc[i]=-1;
            int now=0;
            while(now<ttf_tbllists.length&&(!ttf_tbllists[now].startsWith(tbl_name[i])))
                now++;
            if(now<ttf_tbllists.length){
                tbl_loc[i]=now;
            }
        }
    }
    
    //字形位置表loca
    //private static int[] ttf_locai;
    private static List<Integer> ttf_locaiL;
    //编码数量与字形
    private static int ttf_unicodenum,ttf_unicodenum0,ttf_unicodenum1000;//编码数量
    private static List<byte[]> ttf_shapes;//字形byte数组
    //glyf开头固定字节数组*
    private static byte[] ttf_glyf_start;//glyf开头无意义字形
    
    //获取编码数量与字形
    public static void getCodenumandShapes(){
        //获取位置
        //ttf_locai=AllDeal.bytexTointx(ttf_tbls[tbl_loc[TBL_LOCA]]);
        ttf_locaiL=AllDeal.bytexTolistInteger(ttf_tbls[tbl_loc[TBL_LOCA]]);
        //获取数量
        ttf_unicodenum0=0x100*(ttf_tbls[tbl_loc[TBL_CMAP]][0x2a]&0xff)+(ttf_tbls[tbl_loc[TBL_CMAP]][0x2b]&0xff)+1;
        ttf_unicodenum1000=0x100*(ttf_tbls[tbl_loc[TBL_CMAP]][0x2c]&0xff)+(ttf_tbls[tbl_loc[TBL_CMAP]][0x2d]&0xff)-0x1000+1;
        ttf_unicodenum=ttf_unicodenum0+ttf_unicodenum1000;//总数
        //获取字形(无意义开头)*
        int start=0,length=ttf_locaiL.get(3);
        ttf_glyf_start=new byte[length];
        for(int i=0;i<length;i++)
            ttf_glyf_start[i]=ttf_tbls[tbl_loc[TBL_GLYF]][i];
        //获取字形
        ttf_shapes=new ArrayList<>();
        for(int i=0;i<ttf_unicodenum;i++){
            //长度
            //start=ttf_locai[i+3];
            //length=ttf_locai[i+4]-ttf_locai[i+3];
            start=ttf_locaiL.get(i+3);
            length=ttf_locaiL.get(i+4)-start;
            byte[] shape=new byte[length];
            for(int j=0;j<length;j++){
                shape[j]=ttf_tbls[tbl_loc[TBL_GLYF]][start+j];
            }
            ttf_shapes.add(shape);
        }
    }
    //获取字形总数
    public static int getCodenum(){
        return ttf_unicodenum;
    }
    //获取一段数
    public static int getCode0num(){
        return ttf_unicodenum0;
    }
    //获取二段数
    public static int getCode1000num(){
        return ttf_unicodenum1000;
    }
    
    //当前字形信息
    private static int shape_linenum,shape_pointnum;//线数，点数
    //private static int[] shape_range=new int[4];//范围
    //private static int[] shape_pointnums;//线的点数量
    private static List<Integer> shape_pointnumsL;//▲线的点数量
    //private static int[][] shape_points_x,shape_points_y;//线与点列表
    private static List<List<Integer>> shape_points_xL,shape_points_yL;//◆线与点列表
    
    //解析字形
    public static void getShapeByListIndex(int index){
        //当前字形字节数组
        byte[] shape=ttf_shapes.get(index);//glyf当前字形字节数组
        //获取线数
        shape_linenum=0x100*(shape[0]&0xff)+(shape[1]&0xff);
        //获取范围
        //for(int i=0;i<shape_range.length;i++)
        //    shape_range[i]=0x100*(shape[2+i*2]&0xff)+(shape[3+i*2]&0xff);
        //获取线的点数量，点总数
        //shape_pointnums=new int[shape_linenum];
        shape_pointnumsL=new ArrayList<>();//▲线的点数量
        int numed1=-1,numed2=0;//终点索引值
        for(int i=0;i<shape_linenum;i++){
            numed2=0x100*(shape[10+i*2]&0xff)+(shape[11+i*2]&0xff);
            //shape_pointnums[i]=numed2-numed1;
            shape_pointnumsL.add(numed2-numed1);//▲线的点数量
            numed1=numed2;
        }
        shape_pointnum=numed2+1;//点总数
        //获取线与点列表
        int loc=12+shape_linenum*2;//flag起点: 09 xx 09 xx
        int num=0;//累计09数量，应等于点总数
        while(num<shape_pointnum){
            num+=(shape[loc+1]&0xff)+1;
            loc+=2;//出循环时，loc为X坐标起点
        }
        //shape_points_x=new int[shape_linenum][];//x
        //shape_points_y=new int[shape_linenum][];//y
        shape_points_xL=new ArrayList<>();//◆线与点列表
        shape_points_yL=new ArrayList<>();//◆线与点列表
        int x1=0,y1=0,x2=0,y2=0;//起点，当前增量
        for(int i=0;i<shape_linenum;i++){
            //int length=shape_pointnums[i];//点数
            int length=shape_pointnumsL.get(i);//点数
            //shape_points_x[i]=new int[length];//x
            //shape_points_y[i]=new int[length];//y
            List<Integer> listx=new ArrayList<>();//◆线与点列表
            List<Integer> listy=new ArrayList<>();//◆线与点列表
            for(int j=0;j<length;j++){
                //当前点增量
                x2=0x100*(shape[loc+j*2]&0xff)+(shape[loc+j*2+1]&0xff);
                y2=0x100*(shape[loc+shape_pointnum*2+j*2]&0xff)+(shape[loc+shape_pointnum*2+j*2+1]&0xff);
                x2=(x2>>>15)==0?x2:(x2-0x10000);
                y2=(y2>>>15)==0?y2:(y2-0x10000);
                //当前点坐标
                x1=(x1+x2)<<16>>>16;
                y1=(y1+y2)<<16>>>16;
                x1=(x1>>>15)==0?x1:(x1-0x10000);
                y1=(y1>>>15)==0?y1:(y1-0x10000);
                //shape_points_x[i][j]=x1;
                //shape_points_x[i][j]=y1;
                listx.add(x1);//◆线与点列表
                listy.add(y1);//◆线与点列表
            }
            shape_points_xL.add(listx);//◆线与点列表
            shape_points_yL.add(listy);//◆线与点列表
            loc+=length*2;//下一条线x坐标起点
        }
    }
    //获取当前字形线总数
    public static int getLinenum(){
        return shape_linenum;
    }
    //获取当前字形点总数
    public static int getPointnum(){
        return shape_pointnum;
    }
    //获取当前字形每条线的点数量_列表▲
    public static List<Integer> getPointnumsL(){
        List<Integer> list=new ArrayList<>();
        list.addAll(shape_pointnumsL);
        return list;
    }
    //获取当前字形所有点的x坐标_列表◆
    public static List<List<Integer>> getPoints_xL(){
        List<List<Integer>> lists=new ArrayList<>();
        List<Integer> list;
        for(int i=0;i<shape_points_xL.size();i++){
            list=new ArrayList<>();
            list.addAll(shape_points_xL.get(i));
            lists.add(list);
        }
        return lists;
    }
    //获取当前字形所有点的y坐标_列表◆
    public static List<List<Integer>> getPoints_yL(){
        List<List<Integer>> lists=new ArrayList<>();
        List<Integer> list;
        for(int i=0;i<shape_points_yL.size();i++){
            list=new ArrayList<>();
            list.addAll(shape_points_yL.get(i));
            lists.add(list);
        }
        return lists;
    }
    
    //简单解析字形：获取线数与点数
    private static int[] getLPnumByShapebytex(byte[] shape){
        //线数
        int Lnum=0x100*(shape[0]&0xff)+(shape[1]&0xff);
        //最后线的点索引位置
        int Ploc=8+Lnum*2;
        //点数
        int Pnum=0x100*(shape[Ploc]&0xff)+(shape[Ploc+1]&0xff);
        return new int[]{Lnum,Pnum};
    }
    
    /*存*/
    //从 单个字形数据 到 单个字形字节数组
    public static byte[] ShapeToBytes(int linesnum,int pointsnum,List<Integer> _pointnumOflines,List<List<Integer>> _xList,List<List<Integer>> _yList){
        List<Integer> pointnumOflines=copyListInteger(_pointnumOflines);
        List<List<Integer>> xList=copyListListInteger(_xList);
        List<List<Integer>> yList=copyListListInteger(_yList);
        
        for(int i=0;i<linesnum;i++){//遍历线
            if(pointnumOflines.get(i)<3){
                linesnum--;
                pointsnum-=pointnumOflines.get(i);
                pointnumOflines.remove(i);
                xList.remove(i);
                yList.remove(i);
            }
        }if(linesnum<1)
            return null;
        //
        List<Byte> blist=new ArrayList<>();
        //线数
        blist.add((byte)(linesnum<<16>>>24));
        blist.add((byte)(linesnum<<24>>>24));
        //范围
        Byte[] rangebtx=new Byte[]{0,0,0,0,0x10,0,0x10,0};
        blist.addAll(Arrays.asList(rangebtx));
        //线终点索引
        int ed=pointnumOflines.get(0)-1;
        for(int i=0;i<linesnum;i++){
            blist.add((byte)(ed<<16>>>24));
            blist.add((byte)(ed<<24>>>24));
            if(i<linesnum-1)
                ed+=pointnumOflines.get(i+1);
        }
        //指令长度
        blist.add((byte)0);
        blist.add((byte)0);
        //flag与重复值
        int left=pointsnum;
        while(left>0xff){
            blist.add((byte)9);
            blist.add((byte)0xfe);
            left-=0xff;
        }
        blist.add((byte)9);
        blist.add((byte)(left-1));
        //xy坐标
        int x1=0,x2;
        List<Integer> xl;
        for(int i=0;i<linesnum;i++){//遍历线
            xl=xList.get(i);//当前线的点
            for(int j=0;j<pointnumOflines.get(i);j++){//遍历点
                x2=xl.get(j);
                blist.add((byte)((x2-x1)<<16>>>24));
                blist.add((byte)((x2-x1)<<24>>>24));
                x1=x2;
            }
        }
        x1=0;
        for(int i=0;i<linesnum;i++){//遍历线
            xl=yList.get(i);//当前线的点
            for(int j=0;j<pointnumOflines.get(i);j++){//遍历点
                x2=xl.get(j);
                blist.add((byte)((x2-x1)<<16>>>24));
                blist.add((byte)((x2-x1)<<24>>>24));
                x1=x2;
            }
        }
        //返回
        byte[] btx=new byte[blist.size()];
        for(int i=0;i<blist.size();i++)
            btx[i]=blist.get(i);
        return btx;
    }
    //从 单个字形数据 到 单个字形字节数组
    public static byte[] ShapeToBytes(){
        for(int i=0;i<shape_linenum;i++){//遍历线
            if(shape_pointnumsL.get(i)<3){
                shape_linenum--;
                shape_pointnum-=shape_pointnumsL.get(i);
                shape_pointnumsL.remove(i);
                shape_points_xL.remove(i);
                shape_points_yL.remove(i);
            }
        }if(shape_linenum<1)
            return null;
        //
        List<Byte> blist=new ArrayList<>();
        //线数
        blist.add((byte)(shape_linenum<<16>>>24));
        blist.add((byte)(shape_linenum<<24>>>24));
        //范围
        Byte[] rangebtx=new Byte[]{0,0,0,0,0x10,0,0x10,0};
        blist.addAll(Arrays.asList(rangebtx));
        //线终点索引
        int ed=shape_pointnumsL.get(0)-1;
        for(int i=0;i<shape_linenum;i++){
            blist.add((byte)(ed<<16>>>24));
            blist.add((byte)(ed<<24>>>24));
            if(i<shape_linenum-1)
                ed+=shape_pointnumsL.get(i+1);
        }
        //指令长度
        blist.add((byte)0);
        blist.add((byte)0);
        //flag与重复值
        int left=shape_pointnum;
        while(left>0xff){
            blist.add((byte)9);
            blist.add((byte)0xfe);
            left-=0xff;
        }
        blist.add((byte)9);
        blist.add((byte)(left-1));
        //xy坐标
        int x1=0,x2;
        List<Integer> xl;
        for(int i=0;i<shape_linenum;i++){//遍历线
            xl=shape_points_xL.get(i);//当前线的点
            for(int j=0;j<shape_pointnumsL.get(i);j++){//遍历点
                x2=xl.get(j);
                blist.add((byte)((x2-x1)<<16>>>24));
                blist.add((byte)((x2-x1)<<24>>>24));
                x1=x2;
            }
        }
        x1=0;
        for(int i=0;i<shape_linenum;i++){//遍历线
            xl=shape_points_yL.get(i);//当前线的点
            for(int j=0;j<shape_pointnumsL.get(i);j++){//遍历点
                x2=xl.get(j);
                blist.add((byte)((x2-x1)<<16>>>24));
                blist.add((byte)((x2-x1)<<24>>>24));
                x1=x2;
            }
        }
        //返回
        byte[] btx=new byte[blist.size()];
        for(int i=0;i<blist.size();i++)
            btx[i]=blist.get(i);
        return btx;
    }
    //字形：增
    public static void addShape(int newnum0,int newnum1000,int index,byte[] shape){
        ttf_unicodenum0=newnum0;//更新
        ttf_unicodenum1000=newnum1000;//更新
        ttf_unicodenum=ttf_unicodenum0+ttf_unicodenum1000;//更新
        ttf_shapes.add(index,shape);//更新：增
    }
    //字形：删
    public static void delShape(int newnum0,int newnum1000,int index){
        ttf_unicodenum0=newnum0;//更新
        ttf_unicodenum1000=newnum1000;//更新
        ttf_unicodenum=ttf_unicodenum0+ttf_unicodenum1000;//更新
        ttf_shapes.remove(index);//更新：删
    }
    //字形：改
    public static void changeShape(int index,byte[] shape){
        ttf_shapes.set(index,shape);//更新：改
    }
    //从 全部数据 到 ttf文件字节数组
    public static byte[] DataToTTFbytes(){
        /*title文件头不变*/
        /*tbls表内容*/
        //head hhea表不变
        //hmtx表：每个字形左间距：0
        byte[] hmtx_main=new byte[ttf_unicodenum*2];
        ttf_tbls[tbl_loc[TBL_HMTX]]=AllDeal.bytexxTobytex_5(ttf_hmtx_start,hmtx_main,null,null,null);
        //OS/2表：最后一个字形unicode编码
        ttf_tbls[tbl_loc[TBL_OSI2]][0x32]=(byte)((ttf_unicodenum1000+0x1000-1)<<16>>>24);
        ttf_tbls[tbl_loc[TBL_OSI2]][0x33]=(byte)((ttf_unicodenum1000+0x1000-1)<<24>>>24);
        /*tbls表内容*/
        //maxp表
        int shapeNum=ttf_unicodenum+3;//字形数量+3
        int pointNumax=8;//非复合(简单)最大点数
        int lineNumax=2;//非复合(简单)最大线数
        for(int i=0;i<ttf_shapes.size();i++){//遍历字形
            int[] LPnum=getLPnumByShapebytex(ttf_shapes.get(i));
            if(LPnum[0]>lineNumax)
                lineNumax=LPnum[0];//线数
            if(LPnum[1]>pointNumax)
                pointNumax=LPnum[1];//点数
        }
        ttf_tbls[tbl_loc[TBL_MAXP]][4]=(byte)(shapeNum<<16>>>24);
        ttf_tbls[tbl_loc[TBL_MAXP]][5]=(byte)(shapeNum<<24>>>24);
        ttf_tbls[tbl_loc[TBL_MAXP]][6]=(byte)(pointNumax<<16>>>24);
        ttf_tbls[tbl_loc[TBL_MAXP]][7]=(byte)(pointNumax<<24>>>24);
        ttf_tbls[tbl_loc[TBL_MAXP]][8]=(byte)(lineNumax<<16>>>24);
        ttf_tbls[tbl_loc[TBL_MAXP]][9]=(byte)(lineNumax<<24>>>24);
        //cmap表
        int code0ed=ttf_unicodenum0-1;//段1结束值
        int code1000ed=ttf_unicodenum1000+0x1000-1;//段2结束值
        int code1000d=(ttf_unicodenum0+3-0x1000)<<16>>>16;//段2glyf增量
        ttf_tbls[tbl_loc[TBL_CMAP]][0x2a]=(byte)(code0ed<<16>>>24);
        ttf_tbls[tbl_loc[TBL_CMAP]][0x2b]=(byte)(code0ed<<24>>>24);
        ttf_tbls[tbl_loc[TBL_CMAP]][0x2c]=(byte)(code1000ed<<16>>>24);
        ttf_tbls[tbl_loc[TBL_CMAP]][0x2d]=(byte)(code1000ed<<24>>>24);
        ttf_tbls[tbl_loc[TBL_CMAP]][0x3a]=(byte)(code1000d<<16>>>24);
        ttf_tbls[tbl_loc[TBL_CMAP]][0x3b]=(byte)(code1000d<<24>>>24);
        //loca表：每个字形在glyf中的起点
        int[] loca_maini=new int[ttf_unicodenum];
        int loc=0x28;//当前字形起点
        for(int i=0;i<ttf_unicodenum;i++){
            loc+=ttf_shapes.get(i).length;
            loca_maini[i]=loc;
        }
        ttf_tbls[tbl_loc[TBL_LOCA]]=AllDeal.bytexxTobytex_5(ttf_loca_start,AllDeal.intxTobytex(loca_maini),null,null,null);
        //glyf表：字形列表
        byte[] glyf_main=AllDeal.listbytextobytex_list(ttf_shapes);
        ttf_tbls[tbl_loc[TBL_GLYF]]=AllDeal.bytexxTobytex_5(ttf_glyf_start,glyf_main,null,null,null);
        /*tbls表内容*/
        //name post cvt_ gasp表不变
        /*tbllist表列表*/
        //表起点
        int count=ttf_title.length+ttf_tbllist.length*ttf_tbllist[0].length;
        for(int i=0;i<ttf_tbllisti.length;i++){
            ttf_tbllisti[i][2]=count;
            ttf_tbllisti[i][3]=ttf_tbls[i].length;
            count+=ttf_tbls[i].length;
            ttf_tbllist[i]=AllDeal.intxTobytex(ttf_tbllisti[i]);
        }
        /*合成*/
        return AllDeal.bytexxTobytex_5(
            ttf_title,//文件头
            AllDeal.bytexxtobytex_xx(ttf_tbllist),//表列表
            AllDeal.bytexxtobytex_xx(ttf_tbls),//表内容
            null,null);
    }
    
    /*转换*/
    //从字形数据到规则文本：使用边长，线数，每线点数，每线x列表，每线y列表
    public static String shapeData2str(int shapeside,int linenum,List<Integer> _pointnumsL,List<List<Integer>> _points_xL,List<List<Integer>> _points_yL){
        List<Integer> pointnumsL=copyListInteger(_pointnumsL);
        List<List<Integer>> points_xL=copyListListInteger(_points_xL);
        List<List<Integer>> points_yL=copyListListInteger(_points_yL);
        
        double sca=1.0*shapeside/0x1000;//缩放比例
        String str="";//规则文本◆
        for(int i=0;i<linenum;i++){//遍历线
            str+="L";//文本规则◆
            int pNum=pointnumsL.get(i);//当前线的点数
            List<Integer> xL=points_xL.get(i);//当前线的x
            List<Integer> yL=points_yL.get(i);//当前线的y
            for(int j=0;j<pNum;j++){//遍历当前线的点
                str+="("+(int)Math.round(xL.get(j)*sca)+","+(int)Math.round(yL.get(j)*sca)+")";//文本规则◆
            }
            str+="\n";//文本规则◆
        }
        return str;//规则文本◆
    }
    //**auto：从字形数据到auto文本：线宽，使用边长，线数，每线点数，每线x列表，每线y列表
    public static String shapeData2autostr(int linewidth,int shapeside,int linenum,List<Integer> _pointnumsL,List<List<Integer>> _points_xL,List<List<Integer>> _points_yL){
        List<Integer> pointnumsL=copyListInteger(_pointnumsL);
        List<List<Integer>> points_xL=copyListListInteger(_points_xL);
        List<List<Integer>> points_yL=copyListListInteger(_points_yL);
        
        double sca=1.0*shapeside/0x1000;//缩放比例
        String str="R="+(int)Math.round(linewidth*sca)+"\n";//规则文本◆
        for(int i=0;i<linenum;i++){//遍历线
            int pNum=pointnumsL.get(i);//当前线的点数
            if(pNum<3)//不足3个点忽略
                continue;
            str+="L";//文本规则◆
            List<Integer> xL=points_xL.get(i);//当前线的x
            List<Integer> yL=points_yL.get(i);//当前线的y
            for(int j=0;j<Math.ceil(pNum/2.0);j++){//遍历当前线的点前一半
                str+="("+(int)Math.round(xL.get(j)*sca)+","+(int)Math.round(yL.get(j)*sca)+")";//文本规则◆
            }
            str+="\n";//文本规则◆
        }if(str.indexOf("L")==-1)//没有线，默认
            str="cannot be converted\n"
                +str
                +"\nL(0,0)("+shapeside+","+shapeside+")";
        return str;//规则文本◆
    }
    //**auto：从auto数据到auto文本：线宽，使用边长，线数，每线点数，每线x列表，每线y列表
    public static String autoData2autostr(int linewidth,int shapeside,int linenum,List<Integer> _pointnumsL,List<List<Integer>> _points_xL,List<List<Integer>> _points_yL){
        List<Integer> pointnumsL=copyListInteger(_pointnumsL);
        List<List<Integer>> points_xL=copyListListInteger(_points_xL);
        List<List<Integer>> points_yL=copyListListInteger(_points_yL);
        
        double sca=1.0*shapeside/0x1000;//缩放比例
        String str="R="+(int)Math.round(linewidth*sca)+"\n";//规则文本◆
        for(int i=0;i<linenum;i++){//遍历线
            int pNum=pointnumsL.get(i);//当前线的点数
            str+="L";//文本规则◆
            List<Integer> xL=points_xL.get(i);//当前线的x
            List<Integer> yL=points_yL.get(i);//当前线的y
            for(int j=0;j<pNum;j++){//遍历当前线的点
                str+="("+(int)Math.round(xL.get(j)*sca)+","+(int)Math.round(yL.get(j)*sca)+")";//文本规则◆
            }
            str+="\n";//文本规则◆
        }
        return str;//规则文本◆
    }
    //从规则文本到字形数据：规则文本，使用边长
    //格式：L <0-9 >^0-9
    public static boolean str2shapeData(String str,int shapeside){
        if(str==null)
            return false;
        double sca=1.0*0x1000/shapeside;//缩放比例
        //需要内容
        int lnum=0,pnum=0;//线数，点数
        List<Integer> psnum=new ArrayList<>();//线的点数
        List<List<Integer>> xlist=new ArrayList<>();//x
        List<List<Integer>> ylist=new ArrayList<>();//y
        //所有线字符串
        String[] lines=AllDeal.getmarkStringx(str+"L","L");
        if(lines==null){
            return false;
        }
        //遍历线匹配
        Pattern pattern=Pattern.compile("\\d+");//匹配规则：数字
        Matcher matcher;//匹配源
        List<Integer> list;//当前线xy坐标序列
        List<Integer> xl,yl;
        for(int i=0;i<lines.length;i++){//遍历线
            list=new ArrayList<>();
            xl=new ArrayList<>();
            yl=new ArrayList<>();
            matcher=pattern.matcher(lines[i]);//匹配源
            while(matcher.find()){//匹配到
                list.add(Integer.parseInt(matcher.group()));//添加到序列
            }
            int lp=list.size()/2;//点数
            if(lp>2){//每条线至少三个点
                lnum++;//当前线数
                pnum+=lp;//当前总点数
                psnum.add(lp);//当前线点数
                for(int j=0;j<lp;j++){//遍历每对坐标
                    xl.add((int)Math.round(list.get(j*2)*sca));
                    yl.add((int)Math.round(list.get(j*2+1)*sca));
                }
                xlist.add(xl);//x
                ylist.add(yl);//y
            }
        }
        //数据转换完毕
        if(lnum==0){//没有线
            return false;
        }else{//至少一条线
            shape_linenum=lnum;//线数
            shape_pointnum=pnum;//点数
            shape_pointnumsL=psnum;//每线点数
            shape_points_xL=xlist;//每线x列表
            shape_points_yL=ylist;//每线y列表
            return true;
        }
    }
    //**auto：从auto文本到auto数据：auto文本，使用边长
    //格式：R 0-9 L <0-9 >^0-9
    public static int autostr2autoData(String str,int shapeside){
        if(str==null)
            return -1;
        double sca=1.0*0x1000/shapeside;//缩放比例
        //获取R
        int rloc=str.indexOf("R");
        int lloc=str.indexOf("L");
        if(rloc==-1||lloc==-1||lloc-rloc<2)
            return -1;
        String r=str.substring(rloc,lloc).replaceAll("[^0-9]","");
        if(r==null||r.length()<1)
            return -1;
        int linewidth=(int)Math.round(Integer.parseInt(r)*sca);//线宽R
        if(linewidth<1) linewidth++;
        
        str=str.substring(lloc);
        //需要内容
        int lnum=0,pnum=0;//线数，点数
        List<Integer> psnum=new ArrayList<>();//线的点数
        List<List<Integer>> xlist=new ArrayList<>();//x
        List<List<Integer>> ylist=new ArrayList<>();//y
        //所有线字符串
        String[] lines=AllDeal.getmarkStringx(str+"L","L");
        if(lines==null){
            return -1;
        }
        //遍历线匹配
        Pattern pattern=Pattern.compile("\\d+");//匹配规则：数字
        Matcher matcher;//匹配源
        List<Integer> list;//当前线xy坐标序列
        List<Integer> xl,yl;
        for(int i=0;i<lines.length;i++){//遍历线
            list=new ArrayList<>();
            xl=new ArrayList<>();
            yl=new ArrayList<>();
            matcher=pattern.matcher(lines[i]);//匹配源
            while(matcher.find()){//匹配到
                list.add(Integer.parseInt(matcher.group()));//添加到序列
            }
            int lp=list.size()/2;//点数
            if(lp>1){//每条线至少2个点
                lnum++;//当前线数
                pnum+=lp;//当前总点数
                psnum.add(lp);//当前线点数
                for(int j=0;j<lp;j++){//遍历每对坐标
                    xl.add((int)Math.round(list.get(j*2)*sca));
                    yl.add((int)Math.round(list.get(j*2+1)*sca));
                }
                xlist.add(xl);//x
                ylist.add(yl);//y
            }
        }
        //数据转换完毕
        if(lnum==0){//没有线
            return -1;
        }else{//至少一条线
            shape_linenum=lnum;//线数
            shape_pointnum=pnum;//点数
            shape_pointnumsL=psnum;//每线点数
            shape_points_xL=xlist;//每线x列表
            shape_points_yL=ylist;//每线y列表
            return linewidth;
        }
    }
    //*从字形数据到图像画板：
    public static Bitmap shapeData2img(int shapeside,int linenum,List<Integer> _pointnumsL,List<List<Integer>> _points_xL,List<List<Integer>> _points_yL){
        List<Integer> pointnumsL=copyListInteger(_pointnumsL);
        List<List<Integer>> points_xL=copyListListInteger(_points_xL);
        List<List<Integer>> points_yL=copyListListInteger(_points_yL);
        
        float sca=1.0f*shapeside/0x1000;//缩放比例
        Bitmap bi=Bitmap.createBitmap(shapeside,shapeside,Bitmap.Config.ARGB_8888);//位图
        Canvas ca=new Canvas(bi);//画布
        Paint pa=new Paint();//画笔
        //填充
        pa.setColor(0xff000000);//画笔颜色
        pa.setStrokeWidth(1);//画笔宽度
        pa.setStyle(Paint.Style.FILL);//画笔填充样式
        pa.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        Path path=new Path();//路径
        for(int i=0;i<linenum;i++){//遍历线
            List<Integer> xL=points_xL.get(i);//当前线的x列表
            List<Integer> yL=points_yL.get(i);//当前线的y列表
            path.moveTo(xL.get(0)*sca,shapeside-yL.get(0)*sca);//路径：当前线起点
            for(int j=1;j<pointnumsL.get(i);j++){//遍历当前线剩余点
                path.lineTo(xL.get(j)*sca,shapeside-yL.get(j)*sca);//路径
            }
            path.close();//路径：关闭
        }
        ca.drawPath(path,pa);//绘制当前线
        return bi;
    }
    //**auto：从auto数据到字形数据，两数据字形尺寸一样
    public static boolean auto2shapeData(int linewidth,int autolnum,int autopnum,List<Integer> _autopnumsL,List<List<Integer>> _autoxlist,List<List<Integer>> _autoylist){
        List<Integer> autopnumsL=copyListInteger(_autopnumsL);
        List<List<Integer>> autoxlist=copyListListInteger(_autoxlist);
        List<List<Integer>> autoylist=copyListListInteger(_autoylist);
        
        if(linewidth<1) linewidth=1;
        List<Integer> _pointnumsL=new ArrayList<>();
        List<List<Integer>> _points_xL=new ArrayList<>();
        List<List<Integer>>  _points_yL=new ArrayList<>();
        int pnum=0;//累计点数
        List<Integer> xl,yl,axl,ayl;//当前线计算后，计算前
        int autolpnum;//当前线点数
        float[] xy;//计算点
        for(int i=0;i<autolnum;i++){//遍历线
            autolpnum=autopnumsL.get(i);
            if(autolpnum<2){//点数小于2
                continue;
            }
            axl=autoxlist.get(i);
            ayl=autoylist.get(i);
            xl=new ArrayList<>();
            xl.addAll(axl);
            yl=new ArrayList<>();
            yl.addAll(ayl);
            //起点
            xy=getstart(axl.get(autolpnum-1),ayl.get(autolpnum-1),axl.get(autolpnum-2),ayl.get(autolpnum-2),linewidth);
            xl.add(Math.round(xy[0]));
            yl.add(Math.round(xy[1]));
            //中间点
            for(int j=0;j<autolpnum-2;j++){
                xy=getmid(axl.get(autolpnum-1-j),ayl.get(autolpnum-1-j),axl.get(autolpnum-2-j),ayl.get(autolpnum-2-j),axl.get(autolpnum-3-j),ayl.get(autolpnum-3-j),linewidth);
                xl.add(Math.round(xy[0]));
                yl.add(Math.round(xy[1]));
            }
            //终点
            xy=getend(axl.get(1),ayl.get(1),axl.get(0),ayl.get(0),linewidth);
            xl.add(Math.round(xy[0]));
            yl.add(Math.round(xy[1]));
            _points_xL.add(xl);
            _points_yL.add(yl);
            _pointnumsL.add(autolpnum*2);
            pnum+=autolpnum*2;
        }
        int _linenum=_pointnumsL.size();//线数
        int _pointnum=pnum;//点数
        if(_linenum<1)
            return false;
        else{
            shape_linenum=_linenum;
            shape_pointnum=_pointnum;
            shape_pointnumsL=_pointnumsL;
            shape_points_xL=_points_xL;
            shape_points_yL=_points_yL;
            return true;
        }
    }
    //**auto：从auto数据到字形数据，两数据字形尺寸一样
    public static boolean auto2shapeData(int linewidth){
        if(linewidth<1) linewidth=1;
        List<Integer> _pointnumsL=new ArrayList<>();
        List<List<Integer>> _points_xL=new ArrayList<>();
        List<List<Integer>>  _points_yL=new ArrayList<>();
        int pnum=0;//累计点数
        List<Integer> xl,yl,axl,ayl;//当前线计算后，计算前
        int autolpnum;//当前线点数
        float[] xy;//计算点
        for(int i=0;i<shape_linenum;i++){//遍历线
            autolpnum=shape_pointnumsL.get(i);
            if(autolpnum<2){//点数小于2
                continue;
            }
            axl=shape_points_xL.get(i);
            ayl=shape_points_yL.get(i);
            xl=new ArrayList<>();
            xl.addAll(axl);
            yl=new ArrayList<>();
            yl.addAll(ayl);
            //起点
            xy=getstart(axl.get(autolpnum-1),ayl.get(autolpnum-1),axl.get(autolpnum-2),ayl.get(autolpnum-2),linewidth);
            xl.add(Math.round(xy[0]));
            yl.add(Math.round(xy[1]));
            //中间点
            for(int j=0;j<autolpnum-2;j++){
                xy=getmid(axl.get(autolpnum-1-j),ayl.get(autolpnum-1-j),axl.get(autolpnum-2-j),ayl.get(autolpnum-2-j),axl.get(autolpnum-3-j),ayl.get(autolpnum-3-j),linewidth);
                xl.add(Math.round(xy[0]));
                yl.add(Math.round(xy[1]));
            }
            //终点
            xy=getend(axl.get(1),ayl.get(1),axl.get(0),ayl.get(0),linewidth);
            xl.add(Math.round(xy[0]));
            yl.add(Math.round(xy[1]));
            _points_xL.add(xl);
            _points_yL.add(yl);
            _pointnumsL.add(autolpnum*2);
            pnum+=autolpnum*2;
        }
        int _linenum=_pointnumsL.size();//线数
        int _pointnum=pnum;//点数
        if(_linenum<1)
            return false;
        else{
            shape_linenum=_linenum;
            shape_pointnum=_pointnum;
            shape_pointnumsL=_pointnumsL;
            shape_points_xL=_points_xL;
            shape_points_yL=_points_yL;
            return true;
        }
    }
    //**auto：从字形数据到auto数据，两数据字形尺寸一样
    public static int shapeData2auto(int lnum,int pnum,List<Integer> _pnumsL,List<List<Integer>> _xList,List<List<Integer>> _yList){
        List<Integer> pnumsL=copyListInteger(_pnumsL);
        List<List<Integer>> xList=copyListListInteger(_xList);
        List<List<Integer>> yList=copyListListInteger(_yList);
        
        int linewidth=-1;//线宽*
        int _alnum=0,_apnum=0;
        List<Integer> _apnumsL=new ArrayList<>();
        List<List<Integer>> _axList=new ArrayList<>(),_ayList=new ArrayList<>();
        List<Integer> xl,yl,axl,ayl;
        int num;//当前线点数
        for(int i=0;i<lnum;i++){//遍历线
            num=pnumsL.get(i);
            if(num<3)//不足三个点忽略
                continue;
            xl=xList.get(i);
            yl=yList.get(i);
            axl=new ArrayList<>();
            ayl=new ArrayList<>();
            num=(int)Math.ceil(num/2.0);//新点数
            for(int j=0;j<num;j++){
                axl.add(xl.get(j));
                ayl.add(yl.get(j));
            }
            _axList.add(axl);
            _ayList.add(ayl);
            _apnumsL.add(num);
            _alnum++;
            _apnum+=num;
            //线宽*
            if(linewidth==-1){
                linewidth=Math.round(getlength(xl.get(0)-xl.get(xl.size()-1),yl.get(0)-yl.get(yl.size()-1)));
            }
        }if(_alnum<1){
            shape_linenum=1;
            shape_pointnum=2;
            shape_pointnumsL=new ArrayList<>();
            shape_pointnumsL.add(2);
            shape_points_xL=new ArrayList<>();
            xl=new ArrayList<>();
            xl.add(0,0x1000);
            shape_points_xL.add(xl);
            shape_points_yL=new ArrayList<>();
            yl=new ArrayList<>();
            yl.add(0,0x1000);
            shape_points_yL.add(yl);
        }else{
            shape_linenum=_alnum;
            shape_pointnum=_apnum;
            shape_pointnumsL=_apnumsL;
            shape_points_xL=_axList;
            shape_points_yL=_ayList;
        }
        return linewidth;
    }
    //**auto：从字形数据到auto数据，两数据字形尺寸一样
    public static int shapeData2auto(){
        int linewidth=-1;//线宽*
        
        int _alnum=0,_apnum=0;
        List<Integer> _apnumsL=new ArrayList<>();
        List<List<Integer>> _axList=new ArrayList<>(),_ayList=new ArrayList<>();
        List<Integer> xl,yl,axl,ayl;
        int num;//当前线点数
        for(int i=0;i<shape_linenum;i++){//遍历线
            num=shape_pointnumsL.get(i);
            if(num<3)//不足三个点忽略
                continue;
            xl=shape_points_xL.get(i);
            yl=shape_points_yL.get(i);
            axl=new ArrayList<>();
            ayl=new ArrayList<>();
            num=(int)Math.ceil(num/2.0);//新点数
            for(int j=0;j<num;j++){
                axl.add(xl.get(j));
                ayl.add(yl.get(j));
            }
            _axList.add(axl);
            _ayList.add(ayl);
            _apnumsL.add(num);
            _alnum++;
            _apnum+=num;
            //线宽*
            if(linewidth==-1){
                linewidth=Math.round(getlength(xl.get(0)-xl.get(xl.size()-1),yl.get(0)-yl.get(yl.size()-1)));
            }
        }if(_alnum<1){
            shape_linenum=1;
            shape_pointnum=2;
            shape_pointnumsL=new ArrayList<>();
            shape_pointnumsL.add(2);
            shape_points_xL=new ArrayList<>();
            xl=new ArrayList<>();
            xl.add(0,0x1000);
            shape_points_xL.add(xl);
            shape_points_yL=new ArrayList<>();
            yl=new ArrayList<>();
            yl.add(0,0x1000);
            shape_points_yL.add(yl);
        }else{
            shape_linenum=_alnum;
            shape_pointnum=_apnum;
            shape_pointnumsL=_apnumsL;
            shape_points_xL=_axList;
            shape_points_yL=_ayList;
        }
        return linewidth;
    }
    /**/
    //计算点到原点距离**
    public static float getlength(float x,float y){
        return (float)Math.sqrt(x*x+y*y);
    }//缩放到单位圆的坐标**
    public static float[] getunit(float x,float y){
        float d=getlength(x,y);
        return new float[]{x/d,y/d};
    }//缩放到指定半径圆**
    public static float[] getunitR(float x,float y,float r){
        float[] xy=getunit(x,y);
        xy[0]*=r;xy[1]*=r;
        return xy;
    }//获取线宽起点
    public static float[] getstart(float x0,float y0,float x1,float y1,float r){
        if(x0==x1&&y0==y1) return new float[]{x0,y0};
        float[] xy=new float[2];
        x1-=x0;y1-=y0;//平移坐标系
        xy[0]=-y1;xy[1]=x1;//逆时针旋转90度
        xy=getunitR(xy[0],xy[1],r);//缩放
        xy[0]+=x0;xy[1]+=y0;//平移坐标系
        return xy;
    }//获取线宽中间点
    public static float[] getmid(float x0,float y0,float x1,float y1,float x2,float y2,float r){
        if(x0==x1&&y0==y1) return getstart(x0,y0,x2,y2,r);
        if(x1==x2&&y1==y2) return getend(x0,y0,x2,y2,r);
        float[] xy=new float[2];
        x0-=x1;y0-=y1;x2-=x1;y2-=y1;//平移坐标系
        float cross=x0*y2-x2*y0;//叉乘：>0逆时针，<0顺时针，=0共线
        if(cross==0) return getstart(x1,y1,x2,y2,r);//*试试end〖〗
        float[] xy0=getunit(x0,y0);//缩放0
        float[] xy2=getunit(x2,y2);//缩放2
        xy[0]=xy0[0]+xy2[0];xy[1]=xy0[1]+xy2[1];//角平分线
        if(cross>0){xy[0]=-xy[0];xy[1]=-xy[1];}//顺时针角平分线
        xy=getunitR(xy[0],xy[1],r);//缩放
        xy[0]+=x1;xy[1]+=y1;//平移坐标系
        return xy;
    }//获取线宽终点
    public static float[] getend(float x1,float y1,float x2,float y2,float r){
        if(x2==x1&&y2==y1) return new float[]{x2,y2};
        float[] xy=new float[2];
        x1-=x2;y1-=y2;//平移坐标系
        xy[0]=y1;xy[1]=-x1;//顺时针旋转90度
        xy=getunitR(xy[0],xy[1],r);//缩放
        xy[0]+=x2;xy[1]+=y2;//平移坐标系
        return xy;
    }//获取线宽起点
    public static float[] getstart2(float x0,float y0,float x1,float y1,float r){
        if(x0==x1&&y0==y1) return new float[]{x0,y0};
        float[] xy=new float[2];
        x1-=x0;y1-=y0;//平移坐标系
        xy[0]=y1;xy[1]=-x1;//逆时针旋转90度
        xy=getunitR(xy[0],xy[1],r);//缩放
        xy[0]+=x0;xy[1]+=y0;//平移坐标系
        return xy;
    }//获取线宽中间点
    public static float[] getmid2(float x0,float y0,float x1,float y1,float x2,float y2,float r){
        if(x0==x1&&y0==y1) return getstart2(x0,y0,x2,y2,r);
        if(x1==x2&&y1==y2) return getend2(x0,y0,x2,y2,r);
        float[] xy=new float[2];
        x0-=x1;y0-=y1;x2-=x1;y2-=y1;//平移坐标系
        float cross=x0*y2-x2*y0;//叉乘：>0逆时针，<0顺时针，=0共线
        if(cross==0) return getstart2(x1,y1,x2,y2,r);//*试试end〖〗
        float[] xy0=getunit(x0,y0);//缩放0
        float[] xy2=getunit(x2,y2);//缩放2
        xy[0]=xy0[0]+xy2[0];xy[1]=xy0[1]+xy2[1];//角平分线
        if(cross<0){xy[0]=-xy[0];xy[1]=-xy[1];}//顺时针角平分线
        xy=getunitR(xy[0],xy[1],r);//缩放
        xy[0]+=x1;xy[1]+=y1;//平移坐标系
        return xy;
    }//获取线宽终点
    public static float[] getend2(float x1,float y1,float x2,float y2,float r){
        if(x2==x1&&y2==y1) return new float[]{x2,y2};
        float[] xy=new float[2];
        x1-=x2;y1-=y2;//平移坐标系
        xy[0]=-y1;xy[1]=x1;//顺时针旋转90度
        xy=getunitR(xy[0],xy[1],r);//缩放
        xy[0]+=x2;xy[1]+=y2;//平移坐标系
        return xy;
    }
    
    /**/
    
    //索引(0开始)转对应字符串
    public static String listIndex2str_utf8(int index,int num0,int num1000){
        return new String(unicodeOneToUtf8bytes(listIndex2unicode(index,num0,num1000)),StandardCharsets.UTF_8);
    }
    /**/
    //unicode转索引(0开始)
    public static int unicode2listIndex(int unicode,int num0,int num1000){
        if(unicode<num0)//0 ≤ num0-1
            return unicode;
        else if(unicode<0x1000)//num0 ≤ 0xfff
            return 0;
        else if(unicode<0x1000+num1000)//0x1000 ≤ 0x1000+num1000-1
            return unicode-0x1000+num0;
        else//0x1000+num1000 ≤ ...
            return 0;
    }
    //索引(0开始)转unicode
    public static int listIndex2unicode(int index,int num0,int num1000){
        if(index<num0)//0 ≤ num0-1
            return index;
        else if(index<num0+num1000)//num0 ≤ num0+num1000-1
            return index+0x1000-num0;
        else//num0+nun1000 ≤ ...
            return 0;
    }
    /*
     00000000-0000007F 0xxxxxxx
     00000080-000007FF 110xxxxx 10xxxxxx
     00000800-0000FFFF 1110xxxx 10xxxxxx 10xxxxxx
     00010000-001FFFFF 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
    */
    //utf8转unicode(单个)
    public static int utf8bytesToUnicodeOne(byte[] utf8){
        if(utf8.length==1){//00000000 ≤ 0000007F
            return utf8[0]&0xff;
        }else if(utf8.length==2){//00000080 ≤ 000007FF
            return ((utf8[0]&0x1f)<<6)+(utf8[1]&0x3f);
        }else if(utf8.length==3){//00000800 ≤ 0000FFFF
            return ((utf8[0]&0xf)<<12)+((utf8[1]&0x3f)<<6)+(utf8[2]&0x3f);
        }else if(utf8.length==4){//00010000 ≤ 001FFFFF
            return ((utf8[0]&0x7)<<18)+((utf8[1]&0x3f)<<12)+((utf8[2]&0x3f)<<6)+(utf8[3]&0x3f);
        }else//其他
            return 0;
    }
    //unicode(单个)转utf8
    public static byte[] unicodeOneToUtf8bytes(int unicode){
        if(unicode<0x80){//00000000 ≤ 0000007F
            return new byte[]{(byte)(unicode<<24>>>24)};
        }else if(unicode<0x800){//00000080 ≤ 000007FF
            return new byte[]{(byte)(0xc0+(unicode<<21>>>27)),(byte)(0x80+(unicode<<26>>>26))};
        }else if(unicode<0x10000){//00000800 ≤ 0000FFFF
            return new byte[]{(byte)(0xe0+(unicode<<16>>>28)),(byte)(0x80+(unicode<<20>>>26)),(byte)(0x80+(unicode<<26>>>26))};
        }else if (unicode<0x200000){//00010000 ≤ 001FFFFF
            return new byte[]{(byte)(0xf0+(unicode<<11>>>29)),(byte)(0x80+(unicode<<14>>>26)),(byte)(0x80+(unicode<<20>>>26)),(byte)(0x80+(unicode<<26>>>26))};
        }else{//其他
            return new byte[]{0};
        }
    }
    
    /**/
    //复制List<Integer>
    public static List<Integer> copyListInteger(List<Integer> list){
        List<Integer> newlist=new ArrayList<>();
        newlist.addAll(list);
        return newlist;
    }
    //复制List<List<Integer>>
    public static List<List<Integer>> copyListListInteger(List<List<Integer>> llist){
        List<List<Integer>> newllist=new ArrayList<>();
        List<Integer> list;
        for(int i=0;i<llist.size();i++){
            list=new ArrayList<>();
            list.addAll(llist.get(i));
            newllist.add(list);
        }
        return newllist;
    }
    
}
