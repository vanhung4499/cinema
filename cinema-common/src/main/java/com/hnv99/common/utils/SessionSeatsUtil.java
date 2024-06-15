package com.hnv99.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class for managing session seats
 */
public class SessionSeatsUtil {
    /**
     * Change session seats based on current and selected seats
     *
     * @param curSessionSeats Current session seats
     * @param selectSeats     Selected seats
     * @return Modified session seats
     */
    public static String changeSessionSeats(String curSessionSeats, String selectSeats) {
        JSONObject curSessionSeatsJSON = JSONObject.parseObject(curSessionSeats);
        // Seats selected by timed-out orders
        Map<String, Integer> selectedSeatsMap = new LinkedHashMap<>();
        // Get seat information for the current timed-out order
        String[] selectedSeats = selectSeats.split(",");
        for (int i = 0; i < selectedSeats.length; i++) {
            String row = selectedSeats[i].substring(selectedSeats[i].indexOf("\"") + 1, selectedSeats[i].indexOf("排"));
            Integer col = Integer.parseInt(selectedSeats[i].substring(selectedSeats[i].indexOf("排") + 1, selectedSeats[i].indexOf("座")));
            selectedSeatsMap.put(row, col);
        }
        // Display coordinates of selected seats
        selectedSeatsMap.forEach((key, value) -> {
            System.out.println("key = " + key + " value=" + value);
        });

        // Copy current session seats JSON to a new map
        Map<String, Object> valueMap = new LinkedHashMap<>();
        valueMap.putAll(curSessionSeatsJSON);
        valueMap.forEach((key, value) -> System.out.println("\"" + key + "\":" + "   " + value));
        // Cancel seat selections
        selectedSeatsMap.forEach((index, value) -> {
            ((JSONArray) valueMap.get(index)).set(value - 1, 0);
        });
        JSONObject newSessionSeatsJSON = new JSONObject(valueMap);
        return JSONObject.toJSONString(newSessionSeatsJSON);
    }
}
