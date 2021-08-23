package cn.chenhuanming.octopus.model;

import cn.chenhuanming.octopus.config.Config;
import cn.chenhuanming.octopus.config.annotation.AnnotationConfigFactory;
import cn.chenhuanming.octopus.entity.Applicants;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author guangdao
 * Created at 2021-08-19
 */
public class CellPositionsTest {

    @Test
    public void getContentStartPosition() {
        Config config = new AnnotationConfigFactory(Applicants.class).getConfig();

        CellPosition contentStartPos = CellPositions.getContentStartPosition(CellPositions.POSITION_ZERO_ZERO, config);

        Assert.assertEquals(CellPositions.of(4, 0), contentStartPos);
    }
}