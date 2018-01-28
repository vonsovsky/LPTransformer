package transformation;

import data.DistanceObject;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LPTransformerTest {

    @Test
    public void testTransform() {
        List<DistanceObject> distanceObjects = prepareDistances();
        LPTransformer lpTransformer = new LPTransformer(distanceObjects);

        lpTransformer.transform();

        String expectedString =
                "Minimize\r\n" +
                " obj: error1 + error2 + error3\r\n" +
                "Subject To\r\n" +
                " a - b <= 1 + 0.2 * error1\r\n" +
                " b - a <= 1 + 0.2 * error1\r\n" +
                " b - c <= 1 + 0.2 * error2\r\n" +
                " c - b <= 1 + 0.2 * error2\r\n" +
                " a - c <= 1 + 0.2 * error3\r\n" +
                " c - a <= 1 + 0.2 * error3\r\n" +
                "Bounds\r\n" +
                " 0.00 <= a\r\n" +
                " 0.00 <= b\r\n" +
                " 0.00 <= c\r\n" +
                "Binary\r\n" +
                " error1 error2 error3\r\n" +
                "end";
        assertEquals(lpTransformer.toString(), expectedString);
    }

    /**
     * Add simple triangle
     */
    private List<DistanceObject> prepareDistances() {
        List<DistanceObject> distanceObj = new ArrayList<>();
        distanceObj.add(new DistanceObject(0.5, "a", "b"));
        distanceObj.add(new DistanceObject(0.5, "b", "c"));
        distanceObj.add(new DistanceObject(0.7071, "a", "c"));

        return distanceObj;
    }

}