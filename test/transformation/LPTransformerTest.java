package transformation;

import data.Variable;
import enums.Direction;
import enums.Type;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LPTransformerTest {

    @Mock
    LPFileGenerator lpFileGenerator;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
        LPTransformer.MAXIMUM_TYPES_IN_BUFFER = 512;
    }

    @Test
    public void testTransform_CloseTransformations() {
        LPTransformer lpTransformer = new LPTransformer(prepareItems(), lpFileGenerator);

        lpTransformer.transform();

        verify(lpFileGenerator).addObjectiveFunction(Direction.MINIMIZE, " obj: error1 + error2 + error3");
        verify(lpFileGenerator).addConstraint(" x1_1 - x2_1 - 0.2 error1 <= 1");
        verify(lpFileGenerator).addConstraint(" x2_1 - x1_1 - 0.2 error1 <= 1");
        verify(lpFileGenerator).addConstraint(" x1_2 - x2_2 - 0.2 error1 <= 1");
        verify(lpFileGenerator).addConstraint(" x2_2 - x1_2 - 0.2 error1 <= 1");
        verify(lpFileGenerator).addConstraint(" x1_1 - x3_1 - 0.2 error2 <= 1");
        verify(lpFileGenerator).addConstraint(" x3_1 - x1_1 - 0.2 error2 <= 1");
        verify(lpFileGenerator).addConstraint(" x1_2 - x3_2 - 0.2 error2 <= 1");
        verify(lpFileGenerator).addConstraint(" x3_2 - x1_2 - 0.2 error2 <= 1");
        verify(lpFileGenerator).addConstraint(" x2_1 - x3_1 - 0.2 error3 <= 1");
        verify(lpFileGenerator).addConstraint(" x3_1 - x2_1 - 0.2 error3 <= 1");
        verify(lpFileGenerator).addConstraint(" x2_2 - x3_2 - 0.2 error3 <= 1");
        verify(lpFileGenerator).addConstraint(" x3_2 - x2_2 - 0.2 error3 <= 1");
        verify(lpFileGenerator).addBound(0.0, "x1_1");
        verify(lpFileGenerator).addBound(0.0, "x1_2");
        verify(lpFileGenerator).addBound(0.0, "x2_1");
        verify(lpFileGenerator).addBound(0.0, "x2_2");
        verify(lpFileGenerator).addBound(0.0, "x3_1");
        verify(lpFileGenerator).addBound(0.0, "x3_2");
        verify(lpFileGenerator).addType(Type.BOOLEAN, " error1 error2 error3");
        verify(lpFileGenerator).addEnd();
    }

    /**
     * Add simple triangle
     */
    private List<Variable> prepareItems() {
        List<Variable> items = new ArrayList<>();

        items.add(new Variable(0, 0));
        items.add(new Variable(0.5, 0));
        items.add(new Variable(0.5, 0.5));

        return items;
    }

    @Test
    public void testTransform_DistantTransformations() {
        LPTransformer lpTransformer = new LPTransformer(prepareDistantItems(), lpFileGenerator);

        lpTransformer.transform();

        verify(lpFileGenerator).addObjectiveFunction(Direction.MINIMIZE,
                " obj: error1 + error2 + error3 + error4 + error5 + error6");

        verify(lpFileGenerator).addConstraint(" x1_1 - x2_1 - 0.2 error1 <= 1");
        verify(lpFileGenerator).addConstraint(" x2_1 - x1_1 - 0.2 error1 <= 1");
        verify(lpFileGenerator).addConstraint(" x1_2 - x2_2 - 0.2 error1 <= 1");
        verify(lpFileGenerator).addConstraint(" x2_2 - x1_2 - 0.2 error1 <= 1");
        verify(lpFileGenerator).addConstraint(" x1_1 - x3_1 + 1000 b2_1 + 0.2 error2 > 1");
        verify(lpFileGenerator).addConstraint(" x3_1 - x1_1 + 1000 b2_2 + 0.2 error2 > 1");
        verify(lpFileGenerator).addConstraint(" x1_2 - x3_2 + 1000 b2_3 + 0.2 error2 > 1");
        verify(lpFileGenerator).addConstraint(" x3_2 - x1_2 + 1000 b2_4 + 0.2 error2 > 1");
        verify(lpFileGenerator).addConstraint(" b2_1 + b2_2 + b2_3 + b2_4 = 3");
        verify(lpFileGenerator).addConstraint(" x1_1 - x4_1 + 1000 b3_1 + 0.2 error3 > 1");
        verify(lpFileGenerator).addConstraint(" x4_1 - x1_1 + 1000 b3_2 + 0.2 error3 > 1");
        verify(lpFileGenerator).addConstraint(" x1_2 - x4_2 + 1000 b3_3 + 0.2 error3 > 1");
        verify(lpFileGenerator).addConstraint(" x4_2 - x1_2 + 1000 b3_4 + 0.2 error3 > 1");
        verify(lpFileGenerator).addConstraint(" b3_1 + b3_2 + b3_3 + b3_4 = 3");
        verify(lpFileGenerator).addConstraint(" x2_1 - x3_1 + 1000 b4_1 + 0.2 error4 > 1");
        verify(lpFileGenerator).addConstraint(" x3_1 - x2_1 + 1000 b4_2 + 0.2 error4 > 1");
        verify(lpFileGenerator).addConstraint(" x2_2 - x3_2 + 1000 b4_3 + 0.2 error4 > 1");
        verify(lpFileGenerator).addConstraint(" x3_2 - x2_2 + 1000 b4_4 + 0.2 error4 > 1");
        verify(lpFileGenerator).addConstraint(" b4_1 + b4_2 + b4_3 + b4_4 = 3");
        verify(lpFileGenerator).addConstraint(" x2_1 - x4_1 + 1000 b5_1 + 0.2 error5 > 1");
        verify(lpFileGenerator).addConstraint(" x4_1 - x2_1 + 1000 b5_2 + 0.2 error5 > 1");
        verify(lpFileGenerator).addConstraint(" x2_2 - x4_2 + 1000 b5_3 + 0.2 error5 > 1");
        verify(lpFileGenerator).addConstraint(" x4_2 - x2_2 + 1000 b5_4 + 0.2 error5 > 1");
        verify(lpFileGenerator).addConstraint(" b5_1 + b5_2 + b5_3 + b5_4 = 3");
        verify(lpFileGenerator).addConstraint(" x3_1 - x4_1 - 0.2 error6 <= 1");
        verify(lpFileGenerator).addConstraint(" x4_1 - x3_1 - 0.2 error6 <= 1");
        verify(lpFileGenerator).addConstraint(" x3_2 - x4_2 - 0.2 error6 <= 1");
        verify(lpFileGenerator).addConstraint(" x4_2 - x3_2 - 0.2 error6 <= 1");

        verify(lpFileGenerator).addBound(0.0, "x1_1");
        verify(lpFileGenerator).addBound(0.0, "x1_2");
        verify(lpFileGenerator).addBound(0.0, "x2_1");
        verify(lpFileGenerator).addBound(0.0, "x2_2");
        verify(lpFileGenerator).addBound(0.0, "x3_1");
        verify(lpFileGenerator).addBound(0.0, "x3_2");
        verify(lpFileGenerator).addBound(0.0, "x4_1");
        verify(lpFileGenerator).addBound(0.0, "x4_2");

        verify(lpFileGenerator).addType(Type.BOOLEAN,
                " error1 error2 error3 error4 error5 error6" +
                " b2_1 b2_2 b2_3 b2_4 b3_1 b3_2 b3_3 b3_4 b4_1 b4_2 b4_3 b4_4 b5_1 b5_2 b5_3 b5_4");
        verify(lpFileGenerator).addEnd();
    }

    private List<Variable> prepareDistantItems() {
        List<Variable> items = new ArrayList<>();

        items.add(new Variable(0, 0));
        items.add(new Variable(0.5, 0));
        items.add(new Variable(0, 2));
        items.add(new Variable(0.5, 2));

        return items;
    }

    @Test
    public void testTransform_BinaryBuffer_IsDivisible() {
        LPTransformer lpTransformer = new LPTransformer(prepareDistantItems(), lpFileGenerator);
        LPTransformer.MAXIMUM_TYPES_IN_BUFFER = 2;

        lpTransformer.transform();

        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN, " error1 error2");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," error1 error2");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," error3 error4");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," error5 error6");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," b2_1 b2_2");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," b2_3 b2_4");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," b3_1 b3_2");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," b3_3 b3_4");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," b4_1 b4_2");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," b4_3 b4_4");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," b5_1 b5_2");
        verify(lpFileGenerator).addType(Type.BOOLEAN," b5_3 b5_4");
    }

    @Test
    public void testTransform_BinaryBuffer_IsNonDivisible() {
        LPTransformer lpTransformer = new LPTransformer(prepareDistantItems(), lpFileGenerator);
        LPTransformer.MAXIMUM_TYPES_IN_BUFFER = 3;

        lpTransformer.transform();

        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN, " error1 error2 error3");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," error4 error5 error6");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," b2_1 b2_2 b2_3");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," b2_4 b3_1 b3_2");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," b3_3 b3_4 b4_1");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," b4_2 b4_3 b4_4");
        verify(lpFileGenerator).addTypePartial(Type.BOOLEAN," b5_1 b5_2 b5_3");
        verify(lpFileGenerator).addType(Type.BOOLEAN," b5_4");
    }

}