package transformation;

import data.Position;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LPTransformerTest {

    @Test
    public void testTransform() {
        LPTransformer lpTransformer = new LPTransformer(prepareItems());

        lpTransformer.transform();

        String expectedString =
                "Minimize\r\n" +
                " obj: error1 + error2 + error3\r\n" +
                "Subject To\r\n" +
                " x1_1 - x2_1 - 0.2 error1 <= 1\r\n" +
                " x2_1 - x1_1 - 0.2 error1 <= 1\r\n" +
                " x1_2 - x2_2 - 0.2 error1 <= 1\r\n" +
                " x2_2 - x1_2 - 0.2 error1 <= 1\r\n" +
                " x1_1 - x3_1 - 0.2 error2 <= 1\r\n" +
                " x3_1 - x1_1 - 0.2 error2 <= 1\r\n" +
                " x1_2 - x3_2 - 0.2 error2 <= 1\r\n" +
                " x3_2 - x1_2 - 0.2 error2 <= 1\r\n" +
                " x2_1 - x3_1 - 0.2 error3 <= 1\r\n" +
                " x3_1 - x2_1 - 0.2 error3 <= 1\r\n" +
                " x2_2 - x3_2 - 0.2 error3 <= 1\r\n" +
                " x3_2 - x2_2 - 0.2 error3 <= 1\r\n" +
                "Bounds\r\n" +
                " 0.00 <= x1_1\r\n" +
                " 0.00 <= x1_2\r\n" +
                " 0.00 <= x2_1\r\n" +
                " 0.00 <= x2_2\r\n" +
                " 0.00 <= x3_1\r\n" +
                " 0.00 <= x3_2\r\n" +
                "Binary\r\n" +
                " error1 error2 error3\r\n" +
                "end";
        assertEquals(lpTransformer.toString(), expectedString);
    }

    /**
     * Add simple triangle
     */
    private List<Position> prepareItems() {
        List<Position> items = new ArrayList<>();

        items.add(new Position(0, 0));
        items.add(new Position(0.5, 0));
        items.add(new Position(0.5, 0.5));

        return items;
    }

}