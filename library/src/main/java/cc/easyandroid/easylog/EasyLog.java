package cc.easyandroid.easylog;

import android.util.Log;

import java.util.Locale;

/**
 * android Log.isLoggable方法的使用

 android 动态控制logcat日志开关,通过Log.isLoggable（TAG,level）方法动态控制，
 1.添加日志的时候加入判断，
 String TAG="EasyLog";
 boolean isDbug=Log.isLoggable(TAG, Log.VERBOSE);
 if (isDbug) {
 Log.w(TAG, "log");
 }
 2.通过设置属性值来控制该日志开关。

 >adb shell setprop log.tag.EasyLog VERBOSE 设置该TAG的输出级别为VERBOSE。 则Log.isLoggable("EasyLog", Log.VERBOSE) level为VERBOSE以上的都返回true，每设置一次，只能用于一部手机没有重启的情况，如果换一部或者重启要重新设置一下；这样的好处是，自己 开发的手机设置一次，都能打印VERBOSE，编译给别的手机就不能打印VERBOSE信息，这样就不用每次正式发布时要把isDbug设置为false
 该属性值取值顺序为【V,D,I,W,E,A,S】 A表示最高级别的日志，即assert；S表示Suppress，即停止该日志的输出。

 也可以将该属性添加在data/local.prop属性文件中,不同的是，只要存在local.prop，该手机重启与否都一样，可以打印VERBOSE
 */
public class EasyLog {
    public static String TAG = "EasyLog";


    public static boolean DEBUG = Log.isLoggable(TAG, Log.VERBOSE) || BuildConfig.DEBUG;

    /**
     * Customize the log tag for your application, so that other apps using
     * Volley don't mix their logs with yours. <br />
     * Enable the log property for your tag before starting your app: <br />
     * {@code adb shell setprop log.tag.&lt;tag&gt;}
     */
    public static void setTag(String tag) {
        d("Changing log tag to %s", tag);
        TAG = tag;

        // Reinitialize the DEBUG "constant"
        DEBUG = Log.isLoggable(TAG, Log.VERBOSE) || BuildConfig.DEBUG;
    }

    public static void v(String format, Object... args) {
        if (DEBUG) {
            Log.v(TAG, buildMessage(format, args));
        }
    }

    public static void d(String format, Object... args) {
        if (DEBUG) {
            Log.d(TAG, buildMessage(format, args));
        }
    }

    public static void e(String format, Object... args) {
        if (DEBUG) {
            Log.e(TAG, buildMessage(format, args));
        }
    }

    public static void e(Throwable tr, String format, Object... args) {
        if (DEBUG) {
            Log.e(TAG, buildMessage(format, args), tr);
        }
    }

    public static void wtf(String format, Object... args) {
        if (DEBUG) {
            Log.wtf(TAG, buildMessage(format, args));
        }
    }

    public static void wtf(Throwable tr, String format, Object... args) {
        if (DEBUG) {
            Log.wtf(TAG, buildMessage(format, args), tr);
        }
    }   //text

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        // Walk up the stack looking for the first caller outside of VolleyLog.
        // It will be at least two frames up, so start there.
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(EasyLog.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName();
                break;  //
            }
        }
        return String.format(Locale.US, "[%d] %s: %s", Thread.currentThread().getId(), caller, msg);
    }

}
