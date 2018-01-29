package transformation;

import data.Position;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LPTransformerTest {

    @Mock
    BufferedWriter bufferedWriter;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testTransform_CloseTransformations() throws IOException {
        LPTransformer lpTransformer = new LPTransformer(prepareItems(), bufferedWriter);

        lpTransformer.transform();

        verify(bufferedWriter).write("Minimize");
        verify(bufferedWriter).write(" obj: error1 + error2 + error3");
        verify(bufferedWriter).write("Subject To");
        verify(bufferedWriter).write(" x1_1 - x2_1 - 0.2 error1 <= 1");
        verify(bufferedWriter).write(" x2_1 - x1_1 - 0.2 error1 <= 1");
        verify(bufferedWriter).write(" x1_2 - x2_2 - 0.2 error1 <= 1");
        verify(bufferedWriter).write(" x2_2 - x1_2 - 0.2 error1 <= 1");
        verify(bufferedWriter).write(" x1_1 - x3_1 - 0.2 error2 <= 1");
        verify(bufferedWriter).write(" x3_1 - x1_1 - 0.2 error2 <= 1");
        verify(bufferedWriter).write(" x1_2 - x3_2 - 0.2 error2 <= 1");
        verify(bufferedWriter).write(" x3_2 - x1_2 - 0.2 error2 <= 1");
        verify(bufferedWriter).write(" x2_1 - x3_1 - 0.2 error3 <= 1");
        verify(bufferedWriter).write(" x3_1 - x2_1 - 0.2 error3 <= 1");
        verify(bufferedWriter).write(" x2_2 - x3_2 - 0.2 error3 <= 1");
        verify(bufferedWriter).write(" x3_2 - x2_2 - 0.2 error3 <= 1");
        verify(bufferedWriter).write("Bounds");
        verify(bufferedWriter).write(" 0.00 <= x1_1");
        verify(bufferedWriter).write(" 0.00 <= x1_2");
        verify(bufferedWriter).write(" 0.00 <= x2_1");
        verify(bufferedWriter).write(" 0.00 <= x2_2");
        verify(bufferedWriter).write(" 0.00 <= x3_1");
        verify(bufferedWriter).write(" 0.00 <= x3_2");
        verify(bufferedWriter).write("Binary");
        verify(bufferedWriter).write(" error1 error2 error3");
        verify(bufferedWriter).write("end");
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

    @Test
    public void testTransform_DistantTransformations() throws IOException {
        LPTransformer lpTransformer = new LPTransformer(prepareDistantItems(), bufferedWriter);

        lpTransformer.transform();

        verify(bufferedWriter).write("Minimize");
        verify(bufferedWriter).write(" obj: error1 + error2 + error3 + error4 + error5 + error6");
        verify(bufferedWriter).write("Subject To");
        verify(bufferedWriter).write(" x1_1 - x2_1 - 0.2 error1 <= 1");
        verify(bufferedWriter).write(" x2_1 - x1_1 - 0.2 error1 <= 1");
        verify(bufferedWriter).write(" x1_2 - x2_2 - 0.2 error1 <= 1");
        verify(bufferedWriter).write(" x2_2 - x1_2 - 0.2 error1 <= 1");
        verify(bufferedWriter).write(" x1_1 - x3_1 + 1000 b2_1 + 0.2 error2 > 1");
        verify(bufferedWriter).write(" x3_1 - x1_1 + 1000 b2_2 + 0.2 error2 > 1");
        verify(bufferedWriter).write(" x1_2 - x3_2 + 1000 b2_3 + 0.2 error2 > 1");
        verify(bufferedWriter).write(" x3_2 - x1_2 + 1000 b2_4 + 0.2 error2 > 1");
        verify(bufferedWriter).write(" b2_1 + b2_2 + b2_3 + b2_4 = 3");
        verify(bufferedWriter).write(" x1_1 - x4_1 + 1000 b3_1 + 0.2 error3 > 1");
        verify(bufferedWriter).write(" x4_1 - x1_1 + 1000 b3_2 + 0.2 error3 > 1");
        verify(bufferedWriter).write(" x1_2 - x4_2 + 1000 b3_3 + 0.2 error3 > 1");
        verify(bufferedWriter).write(" x4_2 - x1_2 + 1000 b3_4 + 0.2 error3 > 1");
        verify(bufferedWriter).write(" b3_1 + b3_2 + b3_3 + b3_4 = 3");
        verify(bufferedWriter).write(" x2_1 - x3_1 + 1000 b4_1 + 0.2 error4 > 1");
        verify(bufferedWriter).write(" x3_1 - x2_1 + 1000 b4_2 + 0.2 error4 > 1");
        verify(bufferedWriter).write(" x2_2 - x3_2 + 1000 b4_3 + 0.2 error4 > 1");
        verify(bufferedWriter).write(" x3_2 - x2_2 + 1000 b4_4 + 0.2 error4 > 1");
        verify(bufferedWriter).write(" b4_1 + b4_2 + b4_3 + b4_4 = 3");
        verify(bufferedWriter).write(" x2_1 - x4_1 + 1000 b5_1 + 0.2 error5 > 1");
        verify(bufferedWriter).write(" x4_1 - x2_1 + 1000 b5_2 + 0.2 error5 > 1");
        verify(bufferedWriter).write(" x2_2 - x4_2 + 1000 b5_3 + 0.2 error5 > 1");
        verify(bufferedWriter).write(" x4_2 - x2_2 + 1000 b5_4 + 0.2 error5 > 1");
        verify(bufferedWriter).write(" b5_1 + b5_2 + b5_3 + b5_4 = 3");
        verify(bufferedWriter).write(" x3_1 - x4_1 - 0.2 error6 <= 1");
        verify(bufferedWriter).write(" x4_1 - x3_1 - 0.2 error6 <= 1");
        verify(bufferedWriter).write(" x3_2 - x4_2 - 0.2 error6 <= 1");
        verify(bufferedWriter).write(" x4_2 - x3_2 - 0.2 error6 <= 1");
        verify(bufferedWriter).write("Bounds");
        verify(bufferedWriter).write(" 0.00 <= x1_1");
        verify(bufferedWriter).write(" 0.00 <= x1_2");
        verify(bufferedWriter).write(" 0.00 <= x2_1");
        verify(bufferedWriter).write(" 0.00 <= x2_2");
        verify(bufferedWriter).write(" 0.00 <= x3_1");
        verify(bufferedWriter).write(" 0.00 <= x3_2");
        verify(bufferedWriter).write(" 0.00 <= x4_1");
        verify(bufferedWriter).write(" 0.00 <= x4_2");
        verify(bufferedWriter).write("Binary");
        verify(bufferedWriter).write(" error1 error2 error3 error4 error5 error6 b2_1 b2_2 b3_1 b3_2 b4_1 b4_2 b5_1 b5_2");
        verify(bufferedWriter).write("end");
    }

    private List<Position> prepareDistantItems() {
        List<Position> items = new ArrayList<>();

        items.add(new Position(0, 0));
        items.add(new Position(0.5, 0));
        items.add(new Position(0, 2));
        items.add(new Position(0.5, 2));

        return items;
    }

}