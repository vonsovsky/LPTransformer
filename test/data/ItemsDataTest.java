package data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ItemsDataTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetDistance() {
        ItemsData itemsData = new ItemsData(prepareItems());

        assertEquals(itemsData.getDistance(0, 0), 0, .8);
        assertEquals(itemsData.getDistance(0, 1), 0.5, .8);
        assertEquals(itemsData.getDistance(0, 2), 0.7071, 0.0001);
        assertEquals(itemsData.getDistance(1, 0), 0.5, .8);
        assertEquals(itemsData.getDistance(1, 1), 0, .8);
        assertEquals(itemsData.getDistance(1, 2), 0.5, .8);
        assertEquals(itemsData.getDistance(2, 0), 0.7071, 0.0001);
        assertEquals(itemsData.getDistance(2, 1), 0.5, .8);
        assertEquals(itemsData.getDistance(2, 2), 0, .8);
    }

    private List<Variable> prepareItems() {
        List<Variable> items = new ArrayList<>();

        items.add(new Variable(0, 0));
        items.add(new Variable(0.5, 0));
        items.add(new Variable(0.5, 0.5));

        return items;
    }

    @Test
    public void testGetDistance_OutOfRange() {
        ItemsData itemsData = new ItemsData(prepareItems());

        thrown.expect(IllegalArgumentException.class);
        itemsData.getDistance(1, 3);
    }

    @Test
    public void testVectorize() {
        ItemsData itemsData = new ItemsData(prepareItems());

        List<DistanceObject> distanceObjects = itemsData.vectorize();

        assertEquals(distanceObjects.get(0), new DistanceObject(0.5, "x1", "x2"));
        assertEquals(distanceObjects.get(1), new DistanceObject(0.7071, "x1", "x3"));
        assertEquals(distanceObjects.get(2), new DistanceObject(0.5, "x2", "x3"));
    }

}
