package cn.ybx66.conmmon.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    public static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    @Nullable
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("json序列化出错：" + obj, e);
            return null;
        }
    }

    @Nullable
    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    @Nullable
    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    @Nullable
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    @Nullable
    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    static class User{
//        private String name;
//        private String password;
//    }
//    public static void main(String[] args) {
//        User user = new User("Jeff","123456");
        //toString
//        String json = toString(user);
//        System.out.println("json = " + json);

        //反序列化
//        User user1=toBean(json,user.getClass());
//        System.out.println("user1 = " + user1);
        //toList
//        String json ="[16, -18, 6, 9]";
//        List<Integer> integers=toList(json,Integer.class);
//        System.out.println("integers = " + integers);
        //toMap
//        String json = "{\"name\": \"Jeff\",\"pssword\": \"123456\"}";
//        Map<String, String> stringStringMap = toMap(json, String.class, String.class);
//        System.out.println("stringStringMap = " + stringStringMap);
//        String json = "[{\"name\": \"Jeff\",\"pssword\": \"123456\"},{\"name\": \"Jeff\",\"pssword\": \"123456\"}]";
//        List<Map<String, String>> maps = nativeRead(json, new TypeReference<List<Map<String, String>>>() {
//        });
//        for (Map<String, String> map : maps) {
//            System.out.println("map = " + map);
//        }
//    }
}
