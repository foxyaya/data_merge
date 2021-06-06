package cn.ybx66.conmmon.utils;

import cn.ybx66.conmmon.enums.ExceptionEnums;
import cn.ybx66.conmmon.exception.LyException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import static cn.ybx66.conmmon.utils.IdWorker.nextSingleId;


/**
 * Author xiaomousheng
 * Date 2019/6/11 20:49
 */
public class BeanUtil {


    /**
     * Map对象转换成实体对象
     *
     * @param map
     * @param beanClass
     * @return
     */
    public static <T> T cloneMapToBean(Map<String, Object> map, Class<T> beanClass) {
        if (map == null) {
            return null;
        }
        T bean = null;
        try {
            bean = beanClass.newInstance();
            Field[] beanFields = bean.getClass().getDeclaredFields();
            for (Field field : beanFields) {
                String fieldName = field.getName();
                Object value = map.get(fieldName) == null ? map.get(fieldName.toUpperCase()) : map.get(fieldName);
                if (value != null) {
                    PropertyDescriptor pd = new PropertyDescriptor(fieldName, bean.getClass());
                    Method e = pd.getWriteMethod();
                    e.invoke(bean, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    /***
     * @Author: Fox
     * @Date: 2019/6/24 8:43
     * @Description: 插入时，自动装配某些属性
     * @Param [t]
     * @Return T
     */
    public static <T> T autoSetAttrOnInsert(T t) throws InvocationTargetException, IllegalAccessException {
        //获取实际class对象
        Class clazz = t.getClass();
        try {
            //设置分布式ID
            Method setId = clazz.getDeclaredMethod("setId",String.class);
            setId.invoke(t,  String.valueOf(nextSingleId()));

        } catch (NoSuchMethodException e) {
            throw new LyException( ExceptionEnums.ID_IS_ERROR);
        }

        try {
            //设置创建时间
            Method setLastTime = clazz.getDeclaredMethod("setLastTime",Date.class);
            setLastTime.invoke(t, new Date());
        } catch (NoSuchMethodException e) {
            throw new LyException( ExceptionEnums.TIME_IS_ERROR);
        }
        try {
            //创建逻辑位
            Method setFlag = clazz.getDeclaredMethod("setFlag",Integer.class);
            setFlag.invoke(t, 1);
        } catch (NoSuchMethodException e) {
            throw new LyException( ExceptionEnums.FLAG_IS_ERROR);
        }
        return t;
    }





}
