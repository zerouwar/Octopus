package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.Field;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Static utility methods pertaining to {@link CellPosition} instances.
 *
 * @author guangdao
 * Created at 2021-08-19
 * @since 1.1.5
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CellPositions {

    public static final CellPosition POSITION_ZERO_ZERO = new DefaultCellPosition(0, 0);

    public static CellPosition of(int row, int col) {
        return new DefaultCellPosition(row, col);
    }

    /**
     * Get content start position by header start position and config.
     *
     * @param headerStartPos header start position
     * @param config         config
     * @return content start position
     */
    public static CellPosition getContentStartPosition(CellPosition headerStartPos, Config config) {
        int height = getHeight(config.getFields());
        return of(headerStartPos.getRow() + height, headerStartPos.getCol());
    }

    private static int getHeight(List<Field> fields) {
        if (fields == null || fields.size() == 0) {
            return 0;
        }
        int height = 0;
        for (Field field : fields) {
            height = Math.max(height, getHeight(field.getChildren()));
        }
        return height + 1;
    }
}
