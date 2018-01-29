package transformation;

import enums.Direction;
import enums.Type;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import output.Writer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LPFileGeneratorTest {

    @Mock
    private Writer writer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private LPFileGenerator lpFileGenerator;

    @Before
    public void setUp() {
        initMocks(this);
        lpFileGenerator = new LPFileGenerator(writer);
    }

    @Test
    public void testAddObjectiveFunction_Maximize() {
        lpFileGenerator.addObjectiveFunction(Direction.MAXIMIZE, "x1 + x2 + x3");

        verify(writer).addString("Maximize");
        verify(writer).addString("x1 + x2 + x3");
    }

    @Test
    public void testAddObjectiveFunction_Minimize() {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");

        verify(writer).addString("Minimize");
        verify(writer).addString("x1 + x2 + x3");
    }

    @Test
    public void testAddConstraint_ObjectiveWasNotAdded() {
        thrown.expect(IllegalArgumentException.class);
        lpFileGenerator.addConstraint("x1 <= 3 x2");
    }

    @Test
    public void testAddConstraint_ObjectiveIsFirst() {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");
        lpFileGenerator.addConstraint("x1 <= 3 x2");

        verify(writer).addString("Subject To");
        verify(writer).addString("x1 <= 3 x2");
    }

    @Test
    public void testAddConstraint_ObjectiveHasTwo() {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");
        lpFileGenerator.addConstraint("x1 <= 3 x2");
        lpFileGenerator.addConstraint("x1 <= 3 x2");

        verify(writer).addString("Subject To");
        verify(writer, times(2)).addString("x1 <= 3 x2");
    }

    @Test
    public void testAddBound_ConstraintWasNotAdded() {
        thrown.expect(IllegalArgumentException.class);
        lpFileGenerator.addBound(5.0, "<=", 7.8);
    }

    @Test
    public void testAddBound_ConstraintIsFirst() {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");
        lpFileGenerator.addConstraint("x1 <= 3 x2");

        lpFileGenerator.addBound(5.0, "x2", 7.8);

        verify(writer).addString("Bounds");
        verify(writer).addString(" 5.00 <= x2 <= 7.80");
    }

    @Test
    public void testAddBound_ConstraintHasTwo() {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");
        lpFileGenerator.addConstraint("x1 <= 3 x2");

        lpFileGenerator.addBound(5.0, "x2", 7.8);
        lpFileGenerator.addBound(5.0, "x2");

        verify(writer).addString("Bounds");
        verify(writer).addString(" 5.00 <= x2 <= 7.80");
        verify(writer).addString(" 5.00 <= x2");
    }

    @Test
    public void testAddType_ConstraintWasNotAdded() {
        thrown.expect(IllegalArgumentException.class);
        lpFileGenerator.addType(Type.BOOLEAN, "x1");
    }

    @Test
    public void testAddType() {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");
        lpFileGenerator.addConstraint("x1 <= 3 x2");

        lpFileGenerator.addType(Type.BOOLEAN, "x1 x2");

        verify(writer).addString("Binary");
        verify(writer).addStringNoNewline("x1 x2");
        verify(writer).addString("");
    }

    @Test
    public void testAddEnd_ConstraintWasNotAdded() {
        thrown.expect(IllegalArgumentException.class);
        lpFileGenerator.addEnd();
    }

    @Test
    public void testAddEnd() {
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, "x1 + x2 + x3");
        lpFileGenerator.addConstraint("x1 <= 3 x2");

        lpFileGenerator.addEnd();

        verify(writer).addString("end");
    }

}
