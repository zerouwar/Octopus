package cn.chenhuanming.octopus.writer;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.Field;
import cn.chenhuanming.octopus.model.CellPosition;
import cn.chenhuanming.octopus.util.CellUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

/**
 * @author chenhuanming
 * Created at 2018/12/16
 */
public class DefaultSheetWriter<T> extends AbstractSheetWriter<T> {
    private List<Integer> autoSizeColumn = new ArrayList<>();

    public DefaultSheetWriter(Config config, HeaderWriter headerWriter, CellPosition startPoint) {
        super(config, headerWriter, startPoint);
        this.initLeafColumnIndexes();
    }

    public DefaultSheetWriter(Config config) {
        this(config, new DefaultHeaderWriter(), CellUtils.POSITION_ZERO_ZERO);
    }

    @Override
    public CellPosition write(Sheet sheet, Collection<T> data) {
        //auto size will be slow for large data
        boolean isAdjustWidth = data.size() <= 100;
        if (isAdjustWidth && sheet instanceof SXSSFSheet) {
            this.prepareAdjustForSXSSFSheet((SXSSFSheet) sheet);
        }
        CellPosition end = super.write(sheet, data);
        if (isAdjustWidth) {
            adjustColumnWidth(sheet, end);
        }

        return end;
    }

    private void prepareAdjustForSXSSFSheet(SXSSFSheet sheet) {
        for (int i = getStartColumn(); i < autoSizeColumn.size(); i++) {
            if (autoSizeColumn.get(i) == 0) {
                sheet.trackColumnForAutoSizing(i);
            }
        }
    }

    private void adjustColumnWidth(Sheet sheet, CellPosition end) {
        for (int i = getStartColumn(); i < autoSizeColumn.size(); i++) {
            if (autoSizeColumn.get(i) == 0) {
                sheet.autoSizeColumn(i, true);
            } else {
                sheet.setColumnWidth(i, autoSizeColumn.get(i) * 256);
            }
        }
    }

    private void initLeafColumnIndexes() {
        List<Field> fields = this.config.getFields();

        Deque<Field> queue = new ArrayDeque<>();
        for (Field field : fields) {
            queue.offer(field);
            while (!queue.isEmpty()) {
                Field top = queue.poll();
                if (top.isLeaf()) {
                    autoSizeColumn.add(top.getFieldCellStyle().getWidth());
                } else {
                    for (Field child : top.getChildren()) {
                        queue.offer(child);
                    }
                }
            }
        }
    }
}
