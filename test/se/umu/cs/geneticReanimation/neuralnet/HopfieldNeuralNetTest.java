package se.umu.cs.geneticReanimation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class HopfieldNeuralNetTest {
    private HopfieldNeuralNet nn;
    private double tolerance = 0.00000000000001;
    
    @Before
    public void setUp() {
        this.nn = new HopfieldNeuralNet(2);
        double[][] w = {{1.0, 1.0}, {1.0, 1.0}};
        nn.setWeightMatrix(w);
    }

    @After
    public void tearDown() {
    }

    
    
    @Test
    public void testSigmoid() {
        assertEquals(nn.sigmoid(1.0), Math.abs(nn.sigmoid(-1.0)), tolerance);
        assertEquals(nn.sigmoid(0.0), 0.0, 0.0);
        assertTrue(nn.sigmoid(100) > nn.sigmoid(1));
    }
}