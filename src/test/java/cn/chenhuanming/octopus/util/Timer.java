package cn.chenhuanming.octopus.util;

/**
 * @author chenhuanming
 * Created at 2019-01-09
 */
public class Timer {
    public static long wastedSeconds(Runnable event) {
        long start = System.currentTimeMillis();
        event.run();
        long end = System.currentTimeMillis();
        return (end - start) / 1000;
    }

}
