package com.phungdung.gnoc2.common.untils;

public class StringUtils {
    public static boolean isStringNotNullOrEmtry(Object temp) {
        if (temp != null && temp != "") {
            return true;
        }
        return false;
    }

    public static boolean isStringNullOrEmtry(Object temp) {
        if (temp == null || temp == "") {
            return true;
        }
        return false;
    }

    public static String convertLowerParamContains(String values) {
        String result = values.trim().toLowerCase()
                .replace("\\", "\\\\")
                .replaceAll("%", "\\\\%")
                .replaceAll("_", "\\\\_");
        return "%" + result + "%";
    }
}
