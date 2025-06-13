package com.trigyn.jws.dbutils.entities;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

public class PatternFlagResolver {

	private static final Map<String, Integer> FLAG_MAP = new HashMap<>();

    static {
        for (Field field : Pattern.class.getDeclaredFields()) {
            if (field.getType() == int.class &&
                java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
                java.lang.reflect.Modifier.isPublic(field.getModifiers())) {

                try {
                    FLAG_MAP.put(field.getName(), field.getInt(null));
                } catch (IllegalAccessException ignored) {}
            }
        }
    }

    public static int getFlagsFromNames(String flagsCSV) {
        int flags = 0;
        for (String name : flagsCSV.split(",")) {
            Integer flag = FLAG_MAP.get(name.trim().toUpperCase());
            if (flag != null) {
                flags |= flag;
            }
        }
        return flags;
    }
}