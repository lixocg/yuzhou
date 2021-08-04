package com.yuzhou.log.config;

import com.yuzhou.log.annotation.EnableCommonLog;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于{@link EnableCommonLog#mode} 需要导入的配置组件{@link CommonLogConfiguration}
 *
 * @author xiongcheng.lxch
 * @see EnableCommonLog
 */
public class CommonLogConfigurationSelector extends AdviceModeImportSelector<EnableCommonLog> {

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        List<String> result = new ArrayList<String>();
        result.add(AutoProxyRegistrar.class.getName());
        result.add(CommonLogConfiguration.class.getName());
        return result.toArray(new String[result.size()]);
    }
}
