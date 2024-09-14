package ff.bx.dealx;
import android.content.SharedPreferences;
import android.content.Context;

/**
 * 保存到本地的配置文件
 *
 */
public class ShPref {

    private static String FILLNAME = "config";// 文件名称
    private static SharedPreferences mSharedPreferences = null;
    //字体大小
    public final static String PRE_TXTSIZEMAIN = "txtsizemain";
    public final static int DEF_TXTSIZEMAIN = 16;
    //字体间距
    public final static String PRE_TXTPADDINGMAIN = "txtpaddingmain";
    public final static int DEF_TXTPADDINGMAIN = 20;
    //设置按钮文本
    public final static String PRE_TXTCONFIG = "txtconfig";
    public final static String DEF_TXTCONFIG = "Config";//设置
    //字符编辑按钮文本
    public final static String PRE_TXTEDITBASE = "txteditbase";
    public final static String DEF_TXTEDITBASE = "Edit Base";//编辑字符
    //计算器按钮文本
    public final static String PRE_TXTBASEX = "txtbasex";
    public final static String DEF_TXTBASEX = "Base_X";//计算器[任意/混合进制]

    //ed字体大小main
    public final static String PRE_EDTXTSIZEMAIN = "edtxtsizemain";
    public final static int DEF_EDTXTSIZEMAIN = 16;
    //ed间距
    public final static String PRE_EDTXTPADDINGMAIN = "edtxtpaddingmain";
    public final static int DEF_EDTXTPADDINGMAIN = 20;
    //SVG文本
    public final static String PRE_TXTSVG = "txtsvg";
    public final static String DEF_TXTSVG = "SVG";//编辑字形
    //Code文本
    public final static String PRE_TXTCODE = "txtcode";
    public final static String DEF_TXTCODE = "Code";//设置字符
    
    //cfg字体大小
    public final static String PRE_CFGTXTSIZE = "cfgtxtsize";
    public final static int DEF_CFGTXTSIZE = 15;
    //cfg间距
    public final static String PRE_CFGTXTPADDING = "cfgtxtpadding";
    public final static int DEF_CFGTXTPADDING = 18;
    
    /*SVG*/
    public final static String PRE_SVGSIZE="svgsize";
    public final static int DEF_SVGSIZE=16;
    public final static String PRE_SVGPADDING="svgpadding";
    public final static int DEF_SVGPADDING=20;
    public final static String PRE_SVGTIPSIZE="svgtipsize";
    public final static int DEF_SVGTIPSIZE=10;
    public final static String PRE_SVGPRESIDE="svgpreside";
    public final static int DEF_SVGPRESIDE=240;
    public final static String PRE_SVGPRESIZE="svgpresize";
    public final static int DEF_SVGPRESIZE=120;
    public final static String PRE_SVGPRETIPSIZE="svgpretipsize";
    public final static int DEF_SVGPRETIPSIZE=10;
    public final static String PRE_SVGGRIDHEIGHT="svggridheight";
    public final static int DEF_SVGGRIDHEIGHT=240;
    public final static String PRE_SVGITEM1SIDE="svgitem1side";
    public final static int DEF_SVGITEM1SIDE=80;
    public final static String PRE_SVGITEM1SIZE="svgitem1size";
    public final static int DEF_SVGITEM1SIZE=12;
    public final static String PRE_SVGITEM1TIPSIZE="svgitem1tipsize";
    public final static int DEF_SVGITEM1TIPSIZE=10;
    public final static String PRE_SVGITEM2SIDE="svgitem2side";
    public final static int DEF_SVGITEM2SIDE=80;
    public final static String PRE_SVGITEM2SIZE="svgitem2size";
    public final static int DEF_SVGITEM2SIZE=12;
    public final static String PRE_SVGITEM2TIPSIZE="svgitem2tipsize";
    public final static int DEF_SVGITEM2TIPSIZE=10;
    public final static String PRE_SVGEDITSIZE="svgeditsize";
    public final static int DEF_SVGEDITSIZE=12;
    public final static String PRE_SVGSHAPESIDE="svgshapeside";
    public final static int DEF_SVGSHAPESIDE=100;
    public final static String PRE_SVGDEFNEWSHAPE="svgdefnewshape";
    public final static String DEF_SVGDEFNEWSHAPE="L(50,10)(50,90)(60,90)(60,10)";
    public final static String PRE_SVGLINEWIDTH="svglinewidth";
    public final static int DEF_SVGLINEWIDTH=5;
    public final static String PRE_SVGDRAWDISTANCE="svgdrawdistance";
    public final static int DEF_SVGDRAWDISTANCE=6;
    public final static String PRE_SVGDRAWANGLE="svgdrawangle";
    public final static int DEF_SVGDRAWANGLE=6;
    public final static String PRE_SVGLOADLINESTATE="svgloadlinestate";
    public final static int DEF_SVGLOADLINESTATE=1;
    public final static String PRE_SVGDRAWENDSTATE="svgdrawendstate";
    public final static int DEF_SVGDRAWENDSTATE=1;
    /**
     * 单例模式
     */
    public static synchronized SharedPreferences getInstance(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getApplicationContext().getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    /**
     * SharedPreferences常用的10个操作方法
     */
    public static void putBoolean(String key, boolean value, Context context) {
        ShPref.getInstance(context).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue, Context context) {
        return ShPref.getInstance(context).getBoolean(key, defValue);
    }

    public static void putString(String key, String value, Context context) {
        ShPref.getInstance(context).edit().putString(key, value).apply();
    }

    public static String getString(String key, String defValue, Context context) {
        return ShPref.getInstance(context).getString(key, defValue);
    }

    public static void putInt(String key, int value, Context context) {
        ShPref.getInstance(context).edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue, Context context) {
        return ShPref.getInstance(context).getInt(key, defValue);
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(String key, Context context) {
        ShPref.getInstance(context).edit().remove(key).apply();
    }

    /**
     * 清除所有内容
     */
    public static void clear(Context context) {
        ShPref.getInstance(context).edit().clear().apply();
    }
    }

