package cn.chenhuanming.octopus.config;

import cn.chenhuanming.octopus.formatter.Formatter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenhuanming
 * Created at 2018/12/10
 */
public abstract class CachedConfigFactory implements ConfigFactory {
    protected static final String SPLITTER = "\\|";
    protected Map<Class, Formatter> instanceMap = new HashMap<>();
    protected volatile Config config;

    @Override
    public Config getConfig() {
        if (config == null) {
            synchronized (this) {
                if (config == null) {
                    config = readConfig();
                }
            }
        }
        return config;
    }

    protected abstract Config readConfig();

    public void refresh() {
        config = readConfig();
    }

}
