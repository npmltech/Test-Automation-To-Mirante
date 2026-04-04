package br.com.automation.project.sysinfo;

import java.util.Locale;

public class SystemInfo {

    private final String cpu;
    private final String osName;
    private final String osNameGeneric;
    private final String osVersion;
    private final String vmName;
    private final String vmVendor;
    private final String vmVersion;
    private final String runtimeName;
    private final String runtimeVersion;
    private final long totalMemory;
    private final long freeMemory;
    private final long totalMemoryKB;
    private final long freeMemoryKB;

    public SystemInfo() {
        this.vmName = getProperty("java.vm.name");
        this.vmVendor = getProperty("java.vm.vendor");
        this.vmVersion = getProperty("java.vm.version");
        this.runtimeName = getProperty("java.runtime.name");
        this.runtimeVersion = getProperty("java.runtime.version");
        this.osName = getProperty("os.name");
        this.osNameGeneric = getOSNameGenericProperty("os.name", "generic");
        this.osVersion = getProperty("os.version");
        this.cpu = getProperty("sun.cpu.isalist");

        Runtime runtime = Runtime.getRuntime();
        totalMemory = runtime.totalMemory();
        freeMemory = runtime.freeMemory();
        totalMemoryKB = totalMemory / 1024;
        freeMemoryKB = freeMemory / 1024;
    }

    public static String getProperty(String key) {
        try {
            return System.getProperty(key, "");
        } catch (Exception ex) {
            return "no access";
        }
    }

    public static String getOSNameGenericProperty(String paramOne, String paramTwo) {
        return System.getProperty(paramOne, paramTwo).toLowerCase(Locale.ENGLISH);
    }

    public String getCpu() {
        return this.cpu;
    }

    public String getOsName() {
        return this.osName;
    }

    public String getOsNameGeneric() {
        return this.osNameGeneric;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public String getVmName() {
        return this.vmName;
    }

    public String getVmVendor() {
        return this.vmVendor;
    }

    public String getVmVersion() {
        return this.vmVersion;
    }

    public String getRuntimeName() {
        return this.runtimeName;
    }

    public String getRuntimeVersion() {
        return this.runtimeVersion;
    }

    public long getTotalMemory() {
        return this.totalMemory;
    }

    public long getFreeMemory() {
        return this.freeMemory;
    }

    public long getTotalMemoryKB() {
        return this.totalMemoryKB;
    }

    public long getFreeMemoryKB() {
        return this.freeMemoryKB;
    }
}
