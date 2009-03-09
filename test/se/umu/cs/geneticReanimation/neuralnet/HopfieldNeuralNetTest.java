package se.umu.cs.geneticReanimation.neuralnet;

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
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitIllegalArgumentException() {
        try {
            new HopfieldNeuralNet(new double[0]);
            fail("IllegalArgumentException should have been raised");
        } catch (IllegalArgumentException e) {
            // Ok
        }
        try {
            new HopfieldNeuralNet(new double[3]);
            fail("IllegalArgumentException should have been raised");
        } catch (IllegalArgumentException e) {
            // Ok
        }
        try {
            new HopfieldNeuralNet(new double[5]);
            fail("IllegalArgumentException should have been raised");
        } catch (IllegalArgumentException e) {
            // Ok
        }
        try {
            new HopfieldNeuralNet(new double[6]);
            fail("IllegalArgumentException should have been raised");
        } catch (IllegalArgumentException e) {
            // Ok
        }
        try {
            new HopfieldNeuralNet(new double[8]);
            fail("IllegalArgumentException should have been raised");
        } catch (IllegalArgumentException e) {
            // Ok
        }
    }
    
    @Test
    public void testInit() {
        try {
            new HopfieldNeuralNet(new double[4]);
            new HopfieldNeuralNet(new double[9]);
            new HopfieldNeuralNet(new double[16]);
            new HopfieldNeuralNet(new double[25]);
            new HopfieldNeuralNet(new double[36]);
            new HopfieldNeuralNet(new double[333 * 333]);
            new HopfieldNeuralNet(new double[999 * 999]);
        } catch (IllegalArgumentException e) {
            fail("Shouldn't fail: " + e.getMessage());
        }
    }
    
    @Test
    public void testSetGenotype() {
        //double[][] w = {{1.0, 1.0}, {1.0, 1.0}};
        double[] genotype = {1.0, 1.0, 2.0, 1.0};
        nn.setGenotype(genotype);
        // TODO: verify
        System.out.println(nn.getGenotype().toString());
    }
    
    @Test
    public void testSigmoid() {
        assertEquals(nn.sigmoid(1.0), Math.abs(nn.sigmoid(-1.0)), tolerance);
        assertEquals(nn.sigmoid(0.0), 0.0, 0.0);
        assertTrue(nn.sigmoid(100) > nn.sigmoid(1));
    }
}