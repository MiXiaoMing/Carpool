package com.spirited.support.utils.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * app运行时工具类
 */

public class AppRuntimeUtil {
    /**
     * 饿汉模式
     */
    private static class AppRuntimeUtilInstance {
        private static AppRuntimeUtil instance = new AppRuntimeUtil();
    }

    public static AppRuntimeUtil getInstance() {
        return AppRuntimeUtilInstance.instance;
    }

    private WeakReference<Activity> currentActivityRef = null;
    private boolean frontOrBack = false;

    /**
     * 设置当前页面对象
     * @param activity
     */
    public void setCurrentActivity(Activity activity) {
        currentActivityRef = new WeakReference<Activity>(activity);
    }

    /**
     * 获取当前页面对象
     * @return
     */
    public Activity getCurrentActivity() {
        if (currentActivityRef == null) {
            return null;
        }
        return currentActivityRef.get();
    }

    public int getPidByProcessName(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return -1;
        }
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
        if (appProcessList != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
                if (packageName.equals(appProcess.processName)) {
                    return appProcess.pid;
                }
            }
        }
        return -1;
    }

    /**
     * 获取进程名称
     */
    public String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    /**
     * 是否 主进程
     */
    public boolean isMainProcess(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return context.getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }


    public void setFrontOrBack(boolean frontOrBack) {
        this.frontOrBack = frontOrBack;
    }

    public boolean isFrontOrBack() {
        return frontOrBack;
    }

    /**
     * 当前Activity是否可用
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean isCurrentActivityAvailable() {
        Activity activity = getCurrentActivity();
        return activity != null && !activity.isDestroyed() && !activity.isFinishing();
    }

    /**
     * 进程总内存， 需要root权限
     */
    public double getMemoryUsage (int processId) {
        Process process;
        String line = "";
        double result = 0;
        String cmdMem = "dumpsys meminfo " + processId;
        DataOutputStream dos = null;
        BufferedReader br = null;
        try {
            process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes(cmdMem+"\n");
            dos.writeBytes("exit\n");
            dos.flush();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null) {
                String[] resp = line.trim().split("\\s+");
                if (line.contains("TOTAL")) {
                    // cpu 占用百分比
                    result = Double.parseDouble(resp[1])/1024;
                    break;
                }
            }
            br.close();
            dos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * cpu 比例，需要root权限
     */
    public double getCpuUsage (String partPackageName) {
        Process process;
        String line = "";
        double result  = 0;
        String cmdCpu = "dumpsys cpuinfo";
        DataOutputStream dos = null;
        BufferedReader br = null;
        try {
            process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes(cmdCpu+"\n");
            dos.writeBytes("exit\n");
            dos.flush();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null) {
                String[] resp = line.trim().split("\\s+");
                if (line.contains(partPackageName)) {
                    // cpu 占用百分比
                    result = Double.parseDouble(resp[0].substring(0, resp[0].length()-1));
                    break;
                }
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public double[] getCpuAndMemoryByTop(String name) {
        Process process;
        String line = "";
        double[] result = new double[]{0, 0};
        String partPackageName = name;
        if (name.lastIndexOf(".") != -1) {
            partPackageName = name.substring(0, name.lastIndexOf("."));
        }
        String cmd = "top -n 1 | grep "+partPackageName;
        try {
            process = Runtime.getRuntime().exec(new String[]{"sh", "-c", cmd});
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null) {
                String[] resp = line.trim().split("\\s+");
                if (line.contains(partPackageName) && !line.contains("grep") && resp.length>10) {
                    // cpu 占用百分比
                    result[0] = Double.parseDouble(resp[8]);
                    // 内存 占用百分比
//                    result[1] = Double.parseDouble(resp[9]);
                    // 物理内存大小
                    result[1] = Double.parseDouble(resp[5].substring(0, resp[5].length() - 1));
                    break;
                }
            }
            br.close();
//            process.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }


}
