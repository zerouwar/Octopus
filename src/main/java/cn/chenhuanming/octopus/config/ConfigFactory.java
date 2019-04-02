package cn.chenhuanming.octopus.config;

/**
 * Factory of #{@link Config}
 * Implements should be thread-safe
 * @author chenhuanming
 * Created at 2018/12/10
 */
public interface ConfigFactory {
    Config getConfig();
}
