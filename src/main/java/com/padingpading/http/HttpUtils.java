package com.padingpading.http;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HttpUtils {

    private static  Function<Map.Entry<String, Object>, String> QueryStringFunction = new Function<Map.Entry<String, Object>, String>() {
        @Override
        public String apply(Map.Entry<String, Object> e) {
            Object value = e.getValue();
            StringBuilder sb = new StringBuilder();
            if (value instanceof Collection) {
                Collection var = (Collection) value;
                Iterator iterator = var.iterator();
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    if (null != next && StringUtils.isNotBlank(next.toString())) {
                        sb.append(next.toString() + ",");
                    }
                }
                if(StringUtils.isBlank(sb.toString())){
                    return "";
                }
                return e.getKey() + "=" + sb.deleteCharAt(sb.lastIndexOf(",")).toString();
            } else if (value instanceof Map) {
                return "";
            } else {
                return e.getKey() + "=" + e.getValue();
            }
        }
    };

    public static String getQueryString(Object var) {
        if (var == null) {
            return StringUtils.EMPTY;
        }
        return ((JSONObject) JSONObject.toJSON(var)).entrySet().stream()
                .filter(e -> null != e.getValue() && StringUtils.isNotBlank(e.getValue().toString()))
                .map(QueryStringFunction)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("&", "?", ""));
    }
}
