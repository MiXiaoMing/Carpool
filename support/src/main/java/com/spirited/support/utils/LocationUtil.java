package com.spirited.support.utils;

import android.text.TextUtils;

import com.spirited.support.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationUtil {
    private static String storeName = "QX-Location";

    private static String key_location = "location";

    public static String setLocation(String address) {
        String location = SharePreferences.getString(key_location);
        if (TextUtils.isEmpty(location)) {
            SharePreferences.putString(key_location, address);
            if (location == null) {
                return address;
            }
        }

        if (!location.contains(",")) {
            location += "," + address;
            SharePreferences.putString(key_location, location);
            return "";
        }

        Logger.getLogger().e("location： " + location);
        List<String> locations = new ArrayList<>(Arrays.asList(location.split(",")));
        locations.add(address);
        if (locations.size() < 10) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < locations.size(); ++i) {
                if (i != 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(locations.get(i));
            }
            SharePreferences.putString(key_location, stringBuffer);
        } else {
            Map<String, Integer> maps = new HashMap<>();
            for (String str : locations) {
                if (maps.containsKey(str)) {
                    maps.put(str, maps.get(str) + 1);
                } else {
                    maps.put(str, 1);
                }
            }

            String maxString = "";
            Integer maxCount = 0;
            for (Map.Entry<String, Integer> entry : maps.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    maxString = entry.getKey();
                }
            }

            SharePreferences.putString(key_location, "");

            return maxString;
        }

        return "";
    }
}
