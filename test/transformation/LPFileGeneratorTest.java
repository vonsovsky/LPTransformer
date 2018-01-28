package transformation;

import enums.Direction;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LPFileGeneratorTest {

    LPFileGenerator lpFileGenerator;

    @Before
    public void setUp() {
        lpFileGenerator = new LPFileGenerator();
    }

    @Test
    public void testAddObjectiveFunction() {
        lpFileGenerator.addObjectiveFunction(Direction.MAXIMIZE, "x1 + x2 + x3");

        assertEquals(lpFileGenerator.getLines().size(), 2);
    }

    @Test
    public void testAddConstraint() {
    }

    @Test
    public void testCheckStepAdded() {
    }

    @Test
    public void testAddBound() {
    }

    @Test
    public void testAddType() {
    }

    @Test
    public void testAddEnd() {
    }

}
