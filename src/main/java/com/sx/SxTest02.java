package com.sx;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SxTest02 {
    public static void main(String[] args) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("test01", "test01");
        System.out.println(map.size());
        System.out.println(map);
        Map<String, String> map1 = Collections.synchronizedMap(map);
        map1.put("test02", "test02");
        System.out.println(map1.size());
        System.out.println(map1);

        /**
         * 通过反射获取一个私有的构造方法并实例化一个对象
         */
        
        Constructor<Math> declaredConstructor = Math.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Math math = declaredConstructor.newInstance();
        System.out.println(math);
    }
}
