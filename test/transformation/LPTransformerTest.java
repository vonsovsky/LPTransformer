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
    public void testTransform() throws IOException {
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

}