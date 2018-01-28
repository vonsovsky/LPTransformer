package transformation;

import enums.Direction;
import enums.Type;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.io.BufferedWriter;
import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LPFileGeneratorTest {

    @Mock
    private BufferedWriter bufferedWriter;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private LPFileGenerator lpFileGenerator;

    @Before
    public void setUp() {
        initMocks(this);
        lpFileGenerator = new LPFileGenerator(bufferedWriter);
    }

    @Test
    public void testAddObjectiveFunction_Maximize() throws IOException {
        lpFileGenerator.addObjectiveFunction(Direction.MAXIMIZE, "x1 + x2 + x3");

        verify(bufferedWriter).write("Maximize");
        verify(bufferedWriter).write("x1 + x2 + x3");
    }

    @Test
    public void testAddObjectiveFunction_Minimize() throws IOException {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");

        verify(bufferedWriter).write("Minimize");
        verify(bufferedWriter).write("x1 + x2 + x3");
    }

    @Test
    public void testAddConstraint_ObjectiveWasNotAdded() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        lpFileGenerator.addConstraint("x1 <= 3 x2");
    }

    @Test
    public void testAddConstraint_ObjectiveIsFirst() throws IOException {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");
        lpFileGenerator.addConstraint("x1 <= 3 x2");

        verify(bufferedWriter).write("Subject To");
        verify(bufferedWriter).write("x1 <= 3 x2");
    }

    @Test
    public void testAddConstraint_ObjectiveHasTwo() throws IOException {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");
        lpFileGenerator.addConstraint("x1 <= 3 x2");
        lpFileGenerator.addConstraint("x1 <= 3 x2");

        verify(bufferedWriter).write("Subject To");
        verify(bufferedWriter, times(2)).write("x1 <= 3 x2");
    }

    @Test
    public void testAddBound_ConstraintWasNotAdded() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        lpFileGenerator.addBound(5.0, "<=", 7.8);
    }

    @Test
    public void testAddBound_ConstraintIsFirst() throws IOException {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");
        lpFileGenerator.addConstraint("x1 <= 3 x2");

        lpFileGenerator.addBound(5.0, "x2", 7.8);

        verify(bufferedWriter).write("Bounds");
        verify(bufferedWriter).write(" 5.00 <= x2 <= 7.80");
    }

    @Test
    public void testAddBound_ConstraintHasTwo() throws IOException {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");
        lpFileGenerator.addConstraint("x1 <= 3 x2");

        lpFileGenerator.addBound(5.0, "x2", 7.8);
        lpFileGenerator.addBound(5.0, "x2");

        verify(bufferedWriter).write("Bounds");
        verify(bufferedWriter).write(" 5.00 <= x2 <= 7.80");
        verify(bufferedWriter).write(" 5.00 <= x2");
    }

    @Test
    public void testAddType_ConstraintWasNotAdded() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        lpFileGenerator.addType(Type.BOOLEAN, "x1");
    }

    @Test
    public void testAddType() throws IOException {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");
        lpFileGenerator.addConstraint("x1 <= 3 x2");

        lpFileGenerator.addType(Type.BOOLEAN, "x1 x2");

        verify(bufferedWriter).write("Binary");
        verify(bufferedWriter).write("x1 x2");
    }

    @Test
    public void testAddEnd_ConstraintWasNotAdded() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        lpFileGenerator.addEnd();
    }

    @Test
    public void testAddEnd() throws IOException {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");
        lpFileGenerator.addConstraint("x1 <= 3 x2");

        lpFileGenerator.addEnd();

        verify(bufferedWriter).write("end");
    }

}
