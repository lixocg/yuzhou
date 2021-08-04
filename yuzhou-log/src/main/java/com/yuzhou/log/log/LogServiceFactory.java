package com.yuzhou.log.log;

import com.yuzhou.log.common.LogType;
import com.yuzhou.log.util.ClassUtils;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志服务工厂
 *
 * @author xiongcheng.lxch
 * @see LogService
 */
public class LogServiceFactory {
    private static final Logger logger = InnerLog.getLogger();

    private final static Map<LogType, LogService> logSrvMap = new ConcurrentHashMap<>(8);

    static {
        List<Class> allLogService = getAllLogService(LogService.class);
        if (!CollectionUtils.isEmpty(allLogService)) {
            for (Class logSrv : allLogService) {
                try {
                    LogService logService = (LogService) logSrv.newInstance();
                    logSrvMap.putIfAbsent(logService.logType(), logService);
                } catch (Exception e) {
                    logger.error("{} new Instance error,supply default contructor", logSrv, e);
                }
            }
        }
    }

    public static Map<LogType, LogService> getLogServices() {
        return logSrvMap;
    }

    public static LogService getLogService(LogType logType) {
        return logSrvMap.get(logType);
    }


    private static List<Class> getAllLogService(Class interfaceCls) {
        List<Class> returnClassList = new ArrayList<Class>();
        if (interfaceCls.isInterface()) {
            String packageName = interfaceCls.getPackage().getName();
            try {
                List<Class> allClass = ClassUtils.getClasses(packageName);
                for (int i = 0; i < allClass.size(); i++) {
                    if (interfaceCls.isAssignableFrom(allClass.get(i))) {
                        if (!interfaceCls.equals(allClass.get(i)) && !LogServiceAdapter.class.equals(allClass.get(i))) {
                            returnClassList.add(allClass.get(i));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("getAllLogService", e);
            }
        }
        return returnClassList;
    }


}
