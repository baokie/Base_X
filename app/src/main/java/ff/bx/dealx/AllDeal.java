package ff.bx.dealx;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import android.content.Context;
import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.text.Editable;

public class AllDeal {
    
    //获取时间
    public static String getDateandTime_str(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
    }
    //隐藏输入法
    public static void hideSoftKeyboard(Context c, View view) {
        InputMethodManager imm = (InputMethodManager)c.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    //在输入框光标位置插入
    public static void edtInsert(String str,EditText edittext){
        int index = edittext.getSelectionStart(); //获取光标所在位置
        Editable edit = edittext.getEditableText(); //获取EditText的文字
        if (index < 0 || index >= edit.length() ){
            index=edit.length();
            edit.append(str);
        } else {
            edit.insert(index,str); //光标所在位置插入文字
        }
        edittext.setSelection(index+str.length());
    }
    //hex字符串转byte数组，排除空格和换行
    public static byte[] hexTobytex(String hex)
    {
        hex=hex.replaceAll(" ","").replaceAll("\n","");
        int num=hex.length()/2;
        if(hex.length()%2==1){
            num++;
            hex+="0";
        }
        byte[] btx=new byte[num];
        for(int i=0;i<num;i++)
            btx[i]=(byte)((Integer.parseInt(hex.substring(2*i,2*i+2),16))<<24>>>24);
        return btx;
    }
    //utf8编码转文本，排除换行空格
    public static String hexTostr_utf8(String strutf8)
    {
        strutf8=strutf8.replaceAll(" ","").replaceAll("\n","");
        int num=strutf8.length()/2;
        if(strutf8.length()%2==1){
            num++;
            strutf8+="0";
        }
        byte[] btxtext=new byte[num];
        for(int i=0;i<num;i++)
            btxtext[i]=(byte)((Integer.parseInt(strutf8.substring(2*i,2*i+2),16))<<24>>>24);
        String text="";
        try{
            text=new String(btxtext,StandardCharsets.UTF_8);
        }catch(Exception e){
            text=e.toString();
        }
        return text;
    }
    //ascii转文本，排除换行空格
    public static String hexTostr_ascii(String asc)
    {
        asc=asc.replaceAll(" ","").replaceAll("\n","");
        int num=asc.length()/2;
        if(asc.length()%2==1){
            num++;
            asc+="0";
        }
        byte[] btxtext=new byte[num];
        for(int i=0;i<num;i++)
            btxtext[i]=(byte)((Integer.parseInt(asc.substring(2*i,2*i+2),16))<<24>>>24);
        String text="";
        try{
            text=new String(btxtext,StandardCharsets.US_ASCII);
        }catch(Exception e){
            text=e.toString();
        }
        return text;
    }
    //byte数组转int数组;
    public static int[] bytexTointx(byte[] bytex)
    {
        byte[] btx = null;
        if(bytex.length%4!=0)
        {
            int i=4-bytex.length%4;
            btx=new byte[bytex.length+i];
            for(int j=0;j<bytex.length;j++)
            {btx[j]=bytex[j];}
            for(int j=0;j<i;j++)
            {btx[bytex.length+j]=0;}
        }
        else{btx=bytex;}
        int intx_length=btx.length/4;
        int[] intx=new int[intx_length];
        for(int i=0,j=0;i<intx_length;i++,j+=4)
        {intx[i]=(btx[j]&0xff)*0x1000000+(btx[j+1]&0xff)*0x10000+(btx[j+2]&0xff)*0x100+(btx[j+3]&0xff);}
        return intx;
    }
    //int数组转byte数组;
    public static byte[] intxTobytex(int[] intx)
    {
        byte[] bytex=new byte[intx.length*4];
        for(int i=0,j=0;i<intx.length;i++,j+=4)
        {bytex[j]=(byte)(intx[i]>>>24);
            bytex[j+1]=(byte)(intx[i]<<8>>>24);
            bytex[j+2]=(byte)(intx[i]<<16>>>24);
            bytex[j+3]=(byte)(intx[i]<<24>>>24);}
        return bytex;
    }
    //byte数组转list<Integer>;
    public static List<Integer> bytexTolistInteger(byte[] bytex)
    {
        byte[] btx = null;
        if(bytex.length%4!=0)
        {
            int i=4-bytex.length%4;
            btx=new byte[bytex.length+i];
            for(int j=0;j<bytex.length;j++)
            {btx[j]=bytex[j];}
            for(int j=0;j<i;j++)
            {btx[bytex.length+j]=0;}
        }
        else{btx=bytex;}
        List<Integer> list=new ArrayList<>();
        for(int j=0;j<btx.length;j+=4)
            list.add((btx[j]&0xff)*0x1000000+(btx[j+1]&0xff)*0x10000+(btx[j+2]&0xff)*0x100+(btx[j+3]&0xff));
        return list;
    }
    //list<Integer>转byte数组;
    public static byte[] listIntegerTobytex(List<Integer> list)
    {
        byte[] bytex=new byte[list.size()*4];
        for(int i=0,j=0;i<list.size();i++,j+=4)
        {bytex[j]=(byte)(list.get(i)>>>24);
            bytex[j+1]=(byte)(list.get(i)<<8>>>24);
            bytex[j+2]=(byte)(list.get(i)<<16>>>24);
            bytex[j+3]=(byte)(list.get(i)<<24>>>24);}
        return bytex;
    }
    //byte数组转(utf8编码)字符串
    public static String bytexTostr_utf8(byte[] btx){
        String str="";
        try{
            str=new String(btx,"UTF-8");
        }catch(Exception e){
            str=e.toString();
        }
        return str;
    }
    //byte数组转hex字符串，规律空格换行
    public static String bytexTohex(byte[] btx,int space,int space2,int enter){
        String str="";
        char[] hexArray="0123456789ABCDEF".toCharArray();
        int nows=0,nows2=0,nowe=0;
        for(int i=0;i<btx.length;i++){
            //空格换行
            if(space!=-1&&nows!=0&&nows%space==0)
                str+=" ";
            nows++;
            if(space2!=-1&&nows2!=0&&nows2%space2==0)
                str+=" ";
            nows2++;
            if(enter!=-1&&nowe!=0&&nowe%enter==0)
                str+="\n";
            nowe++;
            //字符
            char[] out=new char[2];
            int v=btx[i]&0xFF;
            out[0]=hexArray[v>>>4];
            out[1]=hexArray[v&0x0F];
            str+=new String(out);
        }
        return str;
    }
    //复制文件
    public static boolean copyFile_file(File source, File dest){
        try{
            InputStream is=new FileInputStream(source);
            OutputStream os=new FileOutputStream(dest);
            byte[] buffer=new byte[1024];
            int length;
            while((length=is.read(buffer))>0) 
                os.write(buffer,0,length);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    //合并五个byte数组
    public static byte[] bytexxTobytex_5(byte[] btxa,byte[] btxb,byte[] btxc,byte[] btxd,byte[] btxe)
    {
        if(btxa==null&&btxb==null&&btxc==null&&btxd==null&&btxe==null){return null;}
        else{
            if(btxa==null){btxa=new byte[]{};}
            if(btxb==null){btxb=new byte[]{};}
            if(btxc==null){btxc=new byte[]{};}
            if(btxd==null){btxd=new byte[]{};}
            if(btxe==null){btxe=new byte[]{};}
            int a=btxa.length;int b=btxb.length;int c=btxc.length;int d=btxd.length;int e=btxe.length;
            byte[] btx=new byte[a+b+c+d+e];
            for(int i=0;i<btxa.length;i++){btx[i]=btxa[i];}
            for(int i=0;i<btxb.length;i++){btx[i+a]=btxb[i];}
            for(int i=0;i<btxc.length;i++){btx[i+a+b]=btxc[i];}
            for(int i=0;i<btxd.length;i++){btx[i+a+b+c]=btxd[i];}
            for(int i=0;i<btxe.length;i++){btx[i+a+b+c+d]=btxe[i];}
            return btx;}
    }
    //合并btxx到btx
    public static byte[] bytexxtobytex_xx(byte[][] btxx){
        int count=0;
        for(int i=0;i<btxx.length;i++)
            count+=btxx[i].length;
        byte[] btx=new byte[count];
        count=0;
        for(int i=0;i<btxx.length;i++){
            for(int j=0;j<btxx[i].length;j++){
                btx[count]=btxx[i][j];
                count++;
            }
        }
        return btx;
    }
    //合并list<btx>到btx
    public static byte[] listbytextobytex_list(List<byte[]> list){
        int count=0;
        for(int i=0;i<list.size();i++)
            count+=list.get(i).length;
        byte[] btx=new byte[count];
        count=0;
        for(int i=0;i<list.size();i++){
            for(int j=0;j<list.get(i).length;j++){
                btx[count]=list.get(i)[j];
                count++;
            }
        }
        return btx;
    }
    //写入byte数组到文件;
    public static boolean saveBytexinFile_file(byte[] bits,File file)
    {
        try{
            if(file.getParentFile()!=null&&!file.getParentFile().exists()){file.getParentFile().mkdirs();}
            if(!file.exists()){file.createNewFile();}
            FileOutputStream fops = new FileOutputStream(file);
            fops.write(bits);
            fops.close();
            return true;}
        catch(Exception e){return false;}
    }
    //读取raw资源到byte数组
    public static byte[] readRawFiletoBytex_rawname(Context c, String name){
        try{
            int resourceId=c.getResources().getIdentifier(name,"raw",c.getPackageName());
            InputStream inputStream=c.getResources().openRawResource(resourceId);
            byte[] buffer=new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            return buffer;
        }catch(Exception e){
            return null;
        }
    }
    //读取raw资源到byte数组
    public static byte[] readRawFiletoBytex_rawid(Context c, int id){
        try{
            InputStream inputStream=c.getResources().openRawResource(id);
            byte[] buffer=new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            return buffer;
        }catch(Exception e){
            return null;
        }
    }
    //读取assets资源到byte数组
    public static byte[] readAssetsFiletoBytex_filename(Context c,String name){
        try {
            InputStream in = c.getResources().getAssets().open(name);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            in.close();
            return buffer;
        } catch (Exception e) {
            return null;
        }
    }
    //文件转byte数组
    public static byte[] readFiletoBytex_path(String path){
        try {
            File file = new File(path); // 根据路径创建文件对象
            FileInputStream fis = new FileInputStream(file); // 创建文件输入流
            byte[] buffer = new byte[(int) file.length()]; // 创建一个与文件长度相同的byte数组
            fis.read(buffer); // 读取文件内容到byte数组中
            fis.close(); // 关闭文件输入流
            return buffer;
        } catch (Exception e) {
            return null;
        }
    }
    //复制assets目录/文件到本地 
    public static boolean copyAssetstoPath(Context context,String assetsPath,String newPath){                    
        try {
            String fileNames[]=context.getAssets().list(assetsPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length>0){//如果是目录
                File file=new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for(String fileName:fileNames)
                    copyAssetstoPath(context,assetsPath+File.separator+ fileName,newPath+File.separator+fileName);
            }else{//如果是文件
                InputStream is=context.getAssets().open(assetsPath);
                FileOutputStream fos=new FileOutputStream(new File(newPath));
                byte[] buffer=new byte[1024];
                int byteCount=0;               
                while((byteCount=is.read(buffer))!=-1){//循环从输入流读取 buffer字节        
                    fos.write(buffer,0,byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
            return true;
        }catch (Exception e){
            return false;
        }                           
    }
    //获取标记字符串中字符串数组
    public static  String[] getmarkStringx(String markString,String mark)
    {
        int count=indexOfCount1(markString,mark)-1;
        if(count<1){return null;}
        else{
            String[] stringx=new String[count];
            int start=0,end=0;
            for(int i=0;i<count;i++)
            {
                start=markString.indexOf(mark,end);
                end=markString.indexOf(mark,start+mark.length());
                stringx[i]=markString.substring(start+mark.length(),end);
            }
            return stringx;}
    }
    //根据字符串数组生成标记字符串
    public static  String getmarkString(String[] stringx,String mark)
    {
        String markString="";
        for(int i=0;i<stringx.length;i++)
        {
            markString=markString+stringx[i]+mark;
        }
        return mark+markString;
    }
    //String在String中出现次数1
    public  static int indexOfCount1(String all,String in)
    {
        int count=0;
        int loc=0;
        while(all.indexOf(in,loc)!=-1)
        {
            count++;
            loc=all.indexOf(in,loc)+in.length();
        }
        return count;
    }
    //String在String中出现次数2
    public static  int indexOfCount2(String all,String in)
    {
        if(in.length()>all.length()){return 0;}
        else
        {
            int count=0;
            for(int i=0;i<all.length()-in.length()+1;i++)
            {
                if(in.equals(all.substring(i,i+in.length())))
                {
                    count++;
                }
            }
            return count;
        }
    }
    //取两字符串之间字符
    public static  String getBetweenString(String all,String start,String end,int loc,boolean isall)
    {
        int a_loc=all.indexOf(start,loc)+start.length();
        if(a_loc>all.length()||loc<0||loc>all.length()||all.indexOf(start,loc)==-1||all.indexOf(end,a_loc)==-1){return null;}
        else if(isall){return start+all.substring(a_loc,all.indexOf(end,a_loc))+end;}
        else{return all.substring(a_loc,all.indexOf(end,a_loc));}
    }
    //判断boolean数组是否全true
    public static boolean isAllTrue(boolean[] box){
        for(int i=0;i<box.length;i++){
            if(!box[i])
                return false;
        }
        return true;
    }
    //判断boolean数组是否全false
    public static boolean isAllFalse(boolean[] box){
        for(int i=0;i<box.length;i++){
            if(box[i])
                return false;
        }
        return true;
    }
    //判断boolean数组全true或全false
    public static int isAllTF(boolean[] box){
        for(int i=1;i<box.length;i++){
            if(box[i]!=box[0])
                return 0;
        }return box[0]?1:-1;
    }
}
