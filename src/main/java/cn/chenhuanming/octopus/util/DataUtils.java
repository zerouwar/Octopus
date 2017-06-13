package cn.chenhuanming.octopus.util;

import java.time.LocalDate;
import java.util.function.Function;

/**
 * Created by Administrator on 2017-04-05.
 */
public class DataUtils {

    public static <R> R equalsThenAndNot(String s,String compare,Function<String,R> then,Function<String,R> notEquals){
        return s.equals(compare)?then.apply(s):notEquals.apply(s);
    }
    //-----------------------------------------------检查null和empty字符串后做出相应动作------------------------------------------//
    /**
     * 检查数据是否为null，如果为null返回默认值
     * @param source 源数据
     * @param defaultValue 默认值
     * @param <T> 数据类型
     * @return 如果source为null，返回defaultValue，否则返回source
     */
    public static <T> T notNull(T source,T defaultValue){
        return source!=null?source:defaultValue;
    }


    /**
     * 检查数据是否为null，如果not null，回调notNull方法，否则回调isNull方法
     * @param s 源数据
     * @param notNull 当s!=null时被调用
     * @param isNull 当s==null时被调用
     * @param <S> 源数据类型
     * @param <T> 返回数据类型
     * @return if(s!=null) return notNull(S s); else return isNull(S s)
     */
    public static <S,T> T notNull(S s, Function<S,T> notNull, Function<S,T> isNull){
        return s!=null?notNull.apply(s):isNull.apply(s);
    }


    /**
     * 检查数据是否为null并且不等于空白字符串，如果为null或者等于空白字符串返回默认值
     * @param source 源数据
     * @param defaultValue 默认值
     * @param <T> 数据类型
     * @return 如果source为null或者等于空白字符串，返回defaultValue,否者返回source
     */
    public static <T> T notNullAndNotEmpty(T source,T defaultValue){
        return source!=null?(!source.equals("")?source:defaultValue):defaultValue;
    }

    /**
     * 检查数据是否为null或者等于空白字符串，如果not null或者等于空白字符串，回调notNull方法，否则回调isNull方法
     * @param s 源数据
     * @param notNullAndNotEmpty 当s!=null&&s.equals("")时被调用
     * @param nullOrEmpty 当s==null||s.equals("")时被调用
     * @param <S> 源数据类型
     * @param <T> 返回数据类型
     * @return if(s!=null&&s.equals("")) return notNull(S s); else return isNull(S s)
     */
    public static <S,T> T notNullAndNotEmpty(S s, Function<S,T> notNullAndNotEmpty, Function<S,T> nullOrEmpty){
        return s!=null?(!s.equals("")?notNullAndNotEmpty.apply(s):nullOrEmpty.apply(s)):nullOrEmpty.apply(s);
    }

    /**
     * 检查对象是否为空
     * @param s 对象
     * @param <S> 对象类型
     * @return s!=null，返回true，否则返回false
     */
    public static <S> boolean notNull(S s){
        return s!=null?true:false;
    }

    /**
     * 检查s是否等于空白字符串
     * @param s 字符串
     * @return s不等于空白字符串返回true，否则返回false
     */
    public static <S> boolean notEmpty(S s){
        return !s.equals("")?true:false;
    }

    /**
     * s不为null并且不等于空白字符串时返回true
     * @param s 参数
     * @param <S> 参数类型
     * @return s!=null&&!s.equals("") true else false
     */
    public static <S> boolean notNullAndEmpty(S s){
        return s!=null?!s.equals("")?true:false:false;
    }

    //-------------------------------------------数据格式检验-------------------------------------------------------------------------//
    public static boolean isInteger(String s){
        return isInteger(s,true);
    }

    public static boolean isInteger(String s,boolean canEmpty){
        if(canEmpty){
            if(s.equals(""))
                return true;
        }
        try {
            Integer.valueOf(s);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isDouble(String s){
        return isDouble(s,true);
    }

    public static boolean isDouble(String s,boolean canEmpty){
        if(canEmpty){
            if(s.equals(""))
                return true;
        }
        try {
            Double.valueOf(s);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isLocalDate(String s){
        return isLocalDate(s,true);
    }

    public static boolean isLocalDate(String s,boolean canEmpty){
        if(canEmpty){
            if(s.equals(""))
                return true;
        }
        try {
            LocalDate.parse(s);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static <S> boolean inOptions(S s,S... options){
        for (S option:options) {
            if(s.equals(option))
                return true;
        }
        return false;
    }

}
