package com.truextend.automation.http;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class JSONUtils {
    public static boolean similarJson(String expected_, String real_) {
        if (expected_ == null && real_ == null) {
            return true;
        } else if (expected_ == null) {
            return false;
        } else if (real_ == null) {
            return false;
        } else {
            String expected = expected_.trim();
            String real = real_.trim();
            if (expected.equals(real)) {
                return true;
            }
            if (expected.startsWith("[") && real.startsWith("[")) {
                List e = JSONUtils.getList(expected);
                List r = JSONUtils.getList(real);
                return similarJson(e, r);
            } else if (expected.startsWith("{") && real.startsWith("{")) {
                Map e = JSONUtils.getMap(expected);
                Map r = JSONUtils.getMap(real);
                return similarJson(e, r);
            } else {
                return false;
            }
        }
    }

    public static boolean similarJson(Map<String, Object> expected_, Map<String, Object> real_) {
        Map<String, Object> expected = new LinkedHashMap<>(expected_);
        Map<String, Object> real = new LinkedHashMap<>(real_);
        for (Map.Entry<String, Object> entry : expected.entrySet()) {
            String key = entry.getKey();
            if (!real.containsKey(key)) {
                return false;
            }
            boolean similar = compareValues(entry.getValue(), real.get(key));
            if (!similar) {
                return false;
            }
        }
        return true;
    }

    public static boolean similarJson(List expected_, List real_) {
        List expected = new ArrayList(expected_);
        List real = new ArrayList(real_);
        for (int i = 0; i < expected.size(); i++) {
            Object e = expected.get(i);
            boolean found = false;
            for (int j = 0; j < real.size(); j++) {
                Object r = real.get(j);
                boolean similar = compareValues(e, r);
                if (similar) {
                    found = true;
                    real.remove(r);
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private static boolean compareValues(Object e, Object r) {
        boolean similar = false;
        if (e == null) {
            if (r == null) {
                similar = true;
            }
        } else if (e instanceof Map) {
            if (r instanceof Map) {
                similar = similarJson((Map<String, Object>) e, (Map<String, Object>) r);
            }
        } else if (e instanceof List) {
            if (r instanceof List) {
                similar = similarJson((List) e, (List) r);
            }
        } else if (e instanceof Number) {
            if (r instanceof Number) {
                Double de = ((Number) e).doubleValue();
                Double dr = ((Number) r).doubleValue();
                similar = (Math.abs(de - dr) <= 0.000001);
            }
        } else if (e instanceof Boolean) {
            similar = e.equals(r);
        } else {
            if (r != null) {
                similar = e.toString().equals(r.toString());
            }
        }
        return similar;
    }

    public static List getList(String string) {
        ObjectMapper objectMapper = new ObjectMapper();
        List result = null;
        try {
            result = objectMapper.readValue(string, ArrayList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static LinkedHashMap<String, Object> getMap(String string) {
        ObjectMapper objectMapper = new ObjectMapper();
        LinkedHashMap result = null;
        try {
            result = objectMapper.readValue(string, LinkedHashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
