package com.luda.testvolley.testvolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JsonUtils: json数据处理工具包
 *
 * @author chaos
 */
public class JsonUtils {

    private static List<Map<String, Object>> listItems = null;
    private static Map<String, Object> map = null;
    private static JSONArray jsonArray = null;
    private static JSONObject jsonObject = null;
    private static JSONObject tempJsonObject = null;

    /**
     * list2JsonArray: 将list包装程json数组
     *
     * @param list
     * @return
     */
    public static JSONArray list2JsonArray(List<Map<String, Object>> list) {
        jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            try {
                jsonArray.put(i, map2JsonObject(list.get(i)));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    /**
     * jsonArray2List: 将json数组转换成List
     *
     * @param jsonArray
     * @return
     */
    public static List<Map<String, Object>> jsonArray2List(JSONArray jsonArray) {
        listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                tempJsonObject = jsonArray.getJSONObject(i);
                listItems.add(jsonObject2Map(tempJsonObject.toString()));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return listItems;
    }

    /**
     * jsonArray2List: 将json数组的字符串表示转换成List
     *
     * @param jsonString
     * @return
     */
    public static List<Map<String, Object>> jsonArray2List(String jsonString) {
        try {
            jsonArray = new JSONArray(jsonString);
            listItems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < jsonArray.length(); i++) {
                tempJsonObject = jsonArray.getJSONObject(i);
                listItems.add(jsonObject2Map(tempJsonObject.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listItems;
    }

    /**
     * list2JsonArrayString: 将list转换成json数组的字符串表示
     *
     * @param list
     * @return
     * @throws org.json.JSONException
     */
    public static String list2JsonArrayString(List<Map<String, Object>> list) {
        try {
            jsonArray = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                tempJsonObject = new JSONObject(list.get(i));
                jsonArray.put(i, tempJsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }


    /**
     * map2JsonObject: 将map转换成json对象
     *
     * @param map
     * @return
     */
    public static JSONObject map2JsonObject(Map<String, Object> map) {
        return new JSONObject(map);
    }

    /**
     * map2JsonObjectString: 将map装换成json对象的字符串表示
     *
     * @param map
     * @return
     */
    public static String map2JsonObjectString(Map<String, Object> map) {
        return new JSONObject(map).toString();
    }

    /**
     * jsonObjectString2Map: 将json对象的字符串表示转换为Map对象
     *
     * @param jsonString
     * @return
     * @throws org.json.JSONException
     */
    public static Map<String, Object> jsonObject2Map(String jsonString) {
        Map<String, Object> valueMap = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        String key;
        Object value;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            Iterator<String> keyIter = jsonObject.keys();
            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return valueMap;

    }

    public static Map<String, Object> jsonObject2Map(JSONObject jsonObject) {
        Map<String, Object> valueMap = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        Iterator<String> keyIter = jsonObject.keys();
        String key;
        Object value;
        try {
            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return valueMap;

    }
}