package cn.chenhuanming.octopus.model;

/**
 * @author chenhuanming
 * Created at 2018/12/10
 */
public abstract class CachedConfigReader implements ConfigReader {

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
