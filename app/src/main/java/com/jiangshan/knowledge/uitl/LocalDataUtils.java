package com.jiangshan.knowledge.uitl;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.MemberInfo;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.entity.User;

public class LocalDataUtils {

    public static final String keySubject = "subject";
    public static final String keyCourse = "course";
    public static final String localDataName = "subject";

    public static final String localUserName = "user";
    public static final String keyUser = "user";
    public static final String keyBillid = "billid";
    public static final String keyMember = "member";

    public static final String passport = "passport";

    public static final String settingDataName = "setting";
    public static final String keyVibrator = "settingVibrator";
    public static final String keyAnsewerNext = "settingAnsewerNext";
    public static final String keyAnsewerShow = "settingAnsewerShow";
    public static final String keyResultShow = "settingResultShow";
    public static final String keyHand = "settingHand";
    public static final String keyLastQuestion = "keyLastQuestion";
    public static final String keyExamType = "keyExamType";
    public static final String keyExamName = "keyExamName";
    public static final String modelLight = "modelLight";

    public static final String anwserQuestion = "anwserQuestion";
    public static final String activityName = "activityName";

    public static final String lightValue = "lightValue";

    public static final String fontSizeValue = "fontSizeValue";
    public static final String bgColorValue = "bgColorValue";


    public static void saveLocalData(Context context, String name, String key, String data) {
        SharedPreferences sp = context.getSharedPreferences(name, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, data);
        editor.commit();
    }

    public static void saveLocalDataBoolean(Context context, String name, String key, Boolean data) {
        SharedPreferences sp = context.getSharedPreferences(name, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    public static void saveLocalDataInteger(Context context, String name, String key, int data) {
        SharedPreferences sp = context.getSharedPreferences(name, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, data);
        editor.commit();
    }

    public static String getLocalData(Context context, String name, String key) {
        SharedPreferences sp = context.getSharedPreferences(name, 0);
        String data = sp.getString(key, "");
        return data;
    }

    public static boolean getLocalDataBoolean(Context context, String name, String key) {
        SharedPreferences sp = context.getSharedPreferences(name, 0);
        boolean data;
        if (LocalDataUtils.keyResultShow.equals(key)) {
            data = sp.getBoolean(key, true);
        } else {
            data = sp.getBoolean(key, false);
        }
        return data;
    }

    public static int getLocalDataInteger(Context context, String name, String key) {
        SharedPreferences sp = context.getSharedPreferences(name, 0);
        int data = sp.getInt(key, 20);
        return data;
    }

    public static Subject getSubject(Context context) {
        String data = LocalDataUtils.getLocalData(context, LocalDataUtils.localDataName, LocalDataUtils.keySubject);
        if (null != data) {
            Subject subject = new Gson().fromJson(data, Subject.class);
            return subject;
        }
        return null;
    }

    public static Course getCourse(Context context) {
        String courseStr = LocalDataUtils.getLocalData(context, LocalDataUtils.localDataName, LocalDataUtils.keyCourse);
        Course course = new Gson().fromJson(courseStr, Course.class);
        return course;

    }

    public static User getUser(Context context) {
        User user = null;
        String userStr = LocalDataUtils.getLocalData(context, LocalDataUtils.localUserName, LocalDataUtils.keyUser);
        if (null != userStr) {
            user = new Gson().fromJson(userStr, User.class);

        }
        return user;
    }

    public static MemberInfo getMemberInfo(Context context) {
        MemberInfo memberInfo = null;
        String userStr = LocalDataUtils.getLocalData(context, LocalDataUtils.localUserName, LocalDataUtils.keyMember);
        if (null != userStr) {
            memberInfo = new Gson().fromJson(userStr, MemberInfo.class);

        }
        return memberInfo;
    }

//    /**
//     * 保存设备注册key
//     * @param context
//     * @param key
//     */
//    public static void saveKey(Activity context, String key) {
//        SharedPreferences sp = MyApplication.application.getSharedPreferences("userinfo", 0);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("key", key);
//        editor.commit();
//    }
//
//    public static void saveRec(Activity context, String key) {
//        SharedPreferences sp = MyApplication.application.getSharedPreferences("userinfo", 0);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("rec", key);
//        editor.commit();
//    }
//
//    /**
//     * 游客代码
//     * @param context
//     * @param key
//     */
//    public static void saveUserCode(Activity context, String key) {
////        SharedPreferences sp = context.getSharedPreferences("userinfo", 0);
//        SharedPreferences sp = MyApplication.application.getSharedPreferences("userinfo", 0);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("userCode", key);
//        editor.commit();
//    }
//
//    /**
//     * 获取设备注册key
//     * @param context
//     * @return
//     */
//    public static String getKey(Activity context) {
//        SharedPreferences sp = MyApplication.application.getSharedPreferences("userinfo", 0);
//        String key = sp.getString("key", "");
//        return  key;
//    }
//
//    public static String getRec(Activity context) {
//        SharedPreferences sp = MyApplication.application.getSharedPreferences("userinfo", 0);
//        String key = sp.getString("rec", "");
//        return  key;
//    }
//
//    public static String getUserCode(Activity context) {
//        SharedPreferences sp = MyApplication.application.getSharedPreferences("userinfo", 0);
//        String userCode = sp.getString("userCode", "");
//        return  userCode;
////        return  "U57176D463B793F911B71075EF9A368C8";
//    }
}
