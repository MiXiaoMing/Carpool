package com.spirited.support.logger;

public interface Printer {

    Printer t(String tag, int methodCount);

    Settings init(String tag);

    Settings getSettings();

    void d(String message, StackTraceElement[] sts, Object... args);

    void e(String message, StackTraceElement[] sts, Object... args);

    void e(Throwable throwable, String message, StackTraceElement[] sts, Object... args);

    void w(String message, StackTraceElement[] sts, Object... args);

    void i(String message, StackTraceElement[] sts, Object... args);

    void v(String message, StackTraceElement[] sts, Object... args);

    void wtf(String message, StackTraceElement[] sts, Object... args);

    void json(String json, StackTraceElement[] sts);

    void xml(String xml, StackTraceElement[] sts);

    void clear();
}
