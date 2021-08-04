package com.yuzhou.log.log;

public class LogBuilder {
    private String format;

    private LogBuilder(String format) {
        this.format = format;
    }

    public static LogBuilder builder(String format){
        return new LogBuilder(format);
    }

    public LogBuilder replaceX(String replaceName, String replaceVal) {
        if (replaceVal == null) {
            replaceVal = "";
        }
        String replace = this.format.replace("{" + replaceName + "}", replaceVal);
        this.format = replace;
        return this;
    }

    public String build(){
        return this.format;
    }
}
