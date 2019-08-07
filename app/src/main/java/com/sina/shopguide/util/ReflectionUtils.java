package com.sina.shopguide.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class ReflectionUtils {
	public static boolean isPropertyExists(Class<?> clzz, String field) {
		if( StringUtils.isEmpty(field)) {
			return false;
		}

		try {
			String getMethodName = "get" + WordUtils.capitalize(field);
			return clzz.getMethod(getMethodName) != null;
		}
		catch (SecurityException e) {
		}
		catch (NoSuchMethodException e) {
		}

		return false;
	}

	public static Object getProperty(Object owner, String fieldName) throws Exception {
		Class<?> ownerClass = owner.getClass();

		Field field = ownerClass.getField(fieldName);

		Object property = field.get(owner);

		return property;
	}

	public static Object getStaticProperty(String className, String fieldName)
			throws Exception {
		Class<?> ownerClass = Class.forName(className);

		Field field = ownerClass.getField(fieldName);

		Object property = field.get(ownerClass);

		return property;
	}

	public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {

		Class<?> ownerClass = owner.getClass();

		Class<?>[] argsClass = new Class[args.length];

		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}

		Method method = ownerClass.getMethod(methodName, argsClass);

		return method.invoke(owner, args);
	}

	public static Object invokeStaticMethod(String className, String methodName,
			Object[] args) throws Exception {
		Class<?> ownerClass = Class.forName(className);

		Class<?>[] argsClass = new Class[args.length];

		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}

		Method method = ownerClass.getMethod(methodName, argsClass);

		return method.invoke(null, args);
	}

	public static Object newInstance(String className, Object[] args) throws Exception {
		Class<?> newoneClass = Class.forName(className);

		Class<?>[] argsClass = new Class[args.length];

		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}

		Constructor<?> cons = newoneClass.getConstructor(argsClass);

		return cons.newInstance(args);

	}

	public static boolean isInstance(Object obj, Class<?> cls) {
		return cls.isInstance(obj);
	}

	public static Object getByArray(Object array, int index) {
		return Array.get(array, index);
	}

	/**
	 * 兼容参数为基本类型的情形
	 * 
	 * @param ownerObj
	 * @param methodName
	 * @param parameterTypes
	 * @param params
	 * @return
	 */
	public static Object invokeMethod(Object ownerObj, String methodName,
			Class<?>[] parameterTypes, Object[] params) {
		try {
			Class<?> ownerType = ownerObj.getClass();
			Method method = ownerType.getMethod(methodName, parameterTypes);
			return method.invoke(ownerObj, params);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 兼容参数为基本类型的情形(会抛出异常)
	 * 
	 * @param ownerObj
	 * @param methodName
	 * @param parameterTypes
	 * @param params
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object invokeParamsMethod(Object ownerObj, String methodName,
			Class<?>[] parameterTypes, Object[] params)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Class<?> ownerType = ownerObj.getClass();
		Method method = ownerType.getMethod(methodName, parameterTypes);
		method.setAccessible(true);
		return method.invoke(ownerObj, params);
	}

	/**
	 * 兼容参数为基本类型的情形
	 * 
	 * @param ownerObj
	 * @param methodName
	 * @param parameterTypes
	 * @param params
	 * @return
	 */
	public static Object invokeStaticMethod(String className, String methodName,
			Class<?>[] parameterTypes, Object[] params) {
		try {
			Class<?> ownerClass = Class.forName(className);
			Method method = ownerClass.getMethod(methodName, parameterTypes);

			return method.invoke(null, params);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Object getPrivateProperty(Object owner, String fieldName) {
		try {
			Field field = owner.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(owner);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return null;
	}

}
