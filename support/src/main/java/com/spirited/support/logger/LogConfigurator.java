package com.spirited.support.logger;

import android.os.Environment;
import android.util.Log;


import com.spirited.support.constants.Constants;

import org.slf4j.LoggerFactory;

import java.io.File;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

public class LogConfigurator {

    public static void init(){
        String fileName = Environment.getExternalStorageDirectory() + "/Android/data/"
                + Constants.cache_dir + "/logs";
        createDir(fileName);
        configure(fileName, "wdcloud");
    }

    // 创建文件夹
    public static void createDir(String path) {
        if (path == null || path.isEmpty()) {
            Log.e(Constants.log_tag, "文件夹地址错误：" + path);
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            boolean isSuccess = file.mkdirs();
            Log.d("createDir","--->目录：" + path +" 创建结果：" + isSuccess);
        }
    }

    public static void configure(String log_dir,String prefix) {
        configureLogbackDirectlyIndex(log_dir, prefix);
    }

    /**
     * desc: 策略：每份日志文件最大900KB，真实大小可能会略大，且最多保存7天
     * author: Xubin
     * date: 2017/4/17 16:06
     * update: 2017/4/17
     */
    private static void configureLogbackDirectly(String log_dir, String filePrefix) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();


        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setContext(context);

        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
        rollingPolicy.setFileNamePattern(log_dir + "/" + filePrefix + "_%d{yyyyMMdd}_%i.log");
        rollingPolicy.setMaxHistory(7);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.setContext(context);


        SizeAndTimeBasedFNATP<ILoggingEvent> sizeAndTimeBasedFNATP = new SizeAndTimeBasedFNATP<>();
        sizeAndTimeBasedFNATP.setMaxFileSize("900KB");
        sizeAndTimeBasedFNATP.setContext(context);
        sizeAndTimeBasedFNATP.setTimeBasedRollingPolicy(rollingPolicy);

        rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(sizeAndTimeBasedFNATP);
        rollingPolicy.start();
        sizeAndTimeBasedFNATP.start();
        rollingFileAppender.setRollingPolicy(rollingPolicy);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        encoder.setContext(context);
        encoder.start();

        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();

        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.TRACE);
        root.addAppender(rollingFileAppender);
    }

    /** 
     * desc: 策略：最多存在3份日志文件，且每份日志文件最大900KB，真实大小可能会略大，
     *             超过3份后会替换第一份
     * author: Xubin
     * date: 2017/4/17 16:08
     * update: 2017/4/17
     */
    private static void configureLogbackDirectlyIndex(String log_dir, String filePrefix) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();


        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setContext(context);
        rollingFileAppender.setFile(log_dir + "/" + filePrefix + ".0.log");

        FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
        rollingPolicy.setFileNamePattern(log_dir + "/" + filePrefix + ".%i.log");
        rollingPolicy.setMinIndex(1);
        rollingPolicy.setMaxIndex(2);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.setContext(context);
        rollingPolicy.start();


        SizeBasedTriggeringPolicy<ILoggingEvent> sizeBasedTriggeringPolicy = new SizeBasedTriggeringPolicy<>();
        sizeBasedTriggeringPolicy.setMaxFileSize("900KB");
        sizeBasedTriggeringPolicy.setContext(context);
        sizeBasedTriggeringPolicy.start();

        rollingFileAppender.setRollingPolicy(rollingPolicy);
        rollingFileAppender.setTriggeringPolicy(sizeBasedTriggeringPolicy);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        encoder.setContext(context);
        encoder.start();

        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();

        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.TRACE);
        root.addAppender(rollingFileAppender);
    }
}
