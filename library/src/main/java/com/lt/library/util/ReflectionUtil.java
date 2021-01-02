package com.lt.library.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @作者: LinTan
 * @日期: 2018/12/25 17:12
 * @版本: 1.0
 * @描述: //ReflectionUtil
 * 源址: https://blog.csdn.net/ShiXueTanLang/article/details/79512356
 * 1.0: Initial Commit
 */

public class ReflectionUtil {
    private ReflectionUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static Object get(Class<?> cls, Object obj, String filedName) throws NoSuchFieldException, IllegalAccessException {
        Field field = cls.getDeclaredField(filedName);
        field.setAccessible(true);
        return field.get(obj);
    }

    private static void set(Class<?> cls, Object obj, String filedName, Object filedValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = cls.getDeclaredField(filedName);
        field.setAccessible(true);
        field.set(obj, filedValue);
    }

    private static Object invoke(Class<?> cls, Object obj, String methodName, Class[] paramsTypes, Object[] paramsValues) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = cls.getDeclaredMethod(methodName, paramsTypes);
        method.setAccessible(true);
        return method.invoke(obj, paramsValues);
    }

    public static Object getField(String className, String filedName) {
        try {
            Class<?> cls = Class.forName(className);
            Object obj = cls.newInstance();
            return get(cls, obj, filedName);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchFieldException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }//获取成员变量, 根据类名(全)

    public static Object getField(Class<?> cls, String filedName) {
        try {
            Object obj = cls.newInstance();
            return get(cls, obj, filedName);
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }//获取成员变量, 根据类类型

    public static Object getField(Object obj, String filedName) {
        try {
            Class<?> cls = obj.getClass();
            return get(cls, obj, filedName);
        } catch (IllegalAccessException | NoSuchFieldException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }//获取成员变量, 根据对象

    public static void setField(String className, String filedName, Object filedValue) {
        try {
            Class<?> cls = Class.forName(className);
            Object obj = cls.newInstance();
            set(cls, obj, filedName, filedValue);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchFieldException | NullPointerException e) {
            e.printStackTrace();
        }
    }//设置成员变量, 根据类名(全)

    public static void setField(Class<?> cls, String filedName, Object filedValue) {
        try {
            Object obj = cls.newInstance();
            set(cls, obj, filedName, filedValue);
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | NullPointerException e) {
            e.printStackTrace();
        }
    }//设置成员变量, 根据类类型

    public static Object setField(Object obj, String filedName, Object filedValue) {
        try {
            Class<?> cls = obj.getClass();
            set(cls, obj, filedName, filedValue);
        } catch (IllegalAccessException | NoSuchFieldException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }//设置成员变量, 根据对象

    public static Object invokeMethod(String className, String methodName) {
        try {
            Class<?> cls = Class.forName(className);
            Object obj = cls.newInstance();
            return invoke(cls, obj, methodName, new Class[]{}, new Object[]{});
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }//调用方法(无参), 根据类名(全)

    public static Object invokeMethod(Class<?> cls, String methodName) {
        try {
            Object obj = cls.newInstance();
            return invoke(cls, obj, methodName, new Class[]{}, new Object[]{});
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }//调用方法(无参), 根据类类型

    public static Object invokeMethod(Object obj, String methodName) {
        try {
            Class<?> cls = obj.getClass();
            return invoke(cls, obj, methodName, new Class[]{}, new Object[]{});
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }//调用方法(无参), 根据对象

    public static Object invokeMethod(Class<?> cls, Object obj, String methodName) {
        try {
            return invoke(cls, obj, methodName, new Class[]{}, new Object[]{});
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }//调用方法(无参), 根据类类型与对象, 适用于obj不是cls实例的场景; eg: RecyclerView的ScrollBar与View

    public static Object invokeMethod(String className, String methodName, Class[] paramsTypes, Object[] paramsValues) {
        try {
            Class<?> cls = Class.forName(className);
            Object obj = cls.newInstance();
            return invoke(cls, obj, methodName, paramsTypes, paramsValues);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }//调用方法(有参), 根据类名(全)

    public static Object invokeMethod(Class<?> cls, String methodName, Class[] paramsTypes, Object[] paramsValues) {
        try {
            Object obj = cls.newInstance();
            return invoke(cls, obj, methodName, paramsTypes, paramsValues);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }//调用方法(有参), 根据类类型

    public static Object invokeMethod(Object obj, String methodName, Class[] paramsTypes, Object[] paramsValues) {
        try {
            Class<?> cls = obj.getClass();
            return invoke(cls, obj, methodName, paramsTypes, paramsValues);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }//调用方法(有参), 根据对象

    public static Object invokeMethod(Class<?> cls, Object obj, String methodName, Class[] paramsTypes, Object[] paramsValues) {
        try {
            return invoke(cls, obj, methodName, paramsTypes, paramsValues);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }//调用方法(有参), 根据类类型与对象, 适用于obj不是cls实例的场景; eg: RecyclerView的ScrollBar与View
}
