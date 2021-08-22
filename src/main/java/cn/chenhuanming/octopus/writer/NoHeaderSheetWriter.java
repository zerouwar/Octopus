package cn.chenhuanming.octopus.writer;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.model.CellPositions;

/**
 * @author guangdao
 * Created at 2021-08-20
 */
public class NoHeaderSheetWriter<T> extends DefaultSheetWriter<T> {
    public NoHeaderSheetWriter(Config config, CellPosition startPoint) {
        super(config, null, startPoint);
    }

    public NoHeaderSheetWriter(Config config) {
        super(config, null, CellPositions.POSITION_ZERO_ZERO);
    }
}
