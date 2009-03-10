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
        double[] genotype = {1.0, 2.0, 3.0, 4.0};
        
        HopfieldNeuralNet nn = new HopfieldNeuralNet(genotype);
        //nn.setGenotype(genotype);
        // TODO: verify
        double[] result = nn.getGenotype();
        assertEquals(1.0, result[0], tolerance);
        assertEquals(2.0, result[1], tolerance);
        assertEquals(3.0, result[2], tolerance);
        assertEquals(4.0, result[3], tolerance);
    }
    
    @Test
    public void testOutput() {
        double[] genotype = {1.0, 0.8,   0.3,   0.234,
                             1.0, 0.8,   0.3,   0.234,
                             1.0, 0.8,   0.3,   0.234,
                             1.0, 0.234, 0.346, 0.634};
        HopfieldNeuralNet nn = new HopfieldNeuralNet(genotype);
        double[] inputs = {-1.0, 23.0};
        nn.setInputs(inputs);
        
        for (int i = 0; i < 100; i++) {
            nn.step();
            double[] output = nn.getOutputs();
            for (int j = 0, length = output.length; j < length; j++) {
                System.out.println(j + " -> " + output[j]);
            }
        }
        
        System.out.println("Genotype = [");
        for (double val : nn.getGenotype()) {
            System.out.println(val + ", ");
        }
        System.out.println("]");

        
        System.out.println("Output = [");
        for (double val : nn.getOutputs()) {
            System.out.println(val + ", ");
        }
        System.out.println("]");
        
        System.out.println("sigm(sigm(1)): " + nn.sigmoid(nn.sigmoid(1)));
    }
    
    
    @Test
    public void testSigmoid() {
        assertEquals(nn.sigmoid(1.0), Math.abs(nn.sigmoid(-1.0)), tolerance);
        assertEquals(nn.sigmoid(0.0), 0.0, 0.0);
        assertTrue(nn.sigmoid(100) > nn.sigmoid(1));
    }
}