package com.phatpl.learnvocabulary.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Logger {
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE

    public static void log(String text) {
        System.out.println(text);
    }

    public static void log(String color, String text) {
        System.out.print(color + text + RESET);
    }

    public static void success(String text) {
        System.out.print(GREEN + text + RESET);
    }

    public static void warning(String text) {
        System.out.print(BLUE + text + RESET);
    }

    public static void error(String text) {
        System.out.print(BLUE + text + RESET);
    }

    public static void logException(Class clazz, Exception ex, String... log) {
        try {
            if (log != null && log.length > 0) {
                log(PURPLE, log[0] + "\n");
            }
            StackTraceElement stackTraceElements[] = (new Throwable()).getStackTrace();
            String nameMethod = stackTraceElements[1].getMethodName();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String detail = sw.toString();
            String[] arr = detail.split("\n");
            Logger.warning("Có lỗi tại class: ");
            Logger.error(clazz.getName());
            Logger.warning(" - tại phương thức: ");
            Logger.error(nameMethod + "\n");
            Logger.warning("Chi tiết lỗi:\n");
            for (String str : arr) {
                Logger.error(str + "\n");
            }
            Logger.log("--------------------------------------------------------\n");
        } catch (Exception e) {

        }
    }
}
