package se.umu.cs.geneticReanimation.neuralnet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;

import static org.junit.Assert.*;

public class HopfieldNeuralNetTest {
    private HopfieldNeuralNet nn;
    private double tolerance = 0.00000000000001;
    
    @Before
    public void setUp() {
        this.nn = new HopfieldNeuralNet(new double[] {1.0, 2.0, 1.0, 2.0});
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
    
    //@Test
    public void testOutput() {
        Random r = new Random();
        double[] genotype = new double[36];
        for (int i = 0, length = genotype.length; i < length; i++) {
            genotype[i] = r.nextDouble() * 2 - 1;
        }
        
        
        //         double[] genotype = {1.0, 0.0,
        //                              1.0, 0.0};
        HopfieldNeuralNet nn = new HopfieldNeuralNet(genotype);
        double[] inputs = {-1.0, 23.0, -27, 1};
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
    public void testSetGetGenotype() {
        double[] genotype = {0.2119178640100634, -0.014885428935399903, -0.4411574961771523,
                             0.15852261892656694, -0.37414287344349284, 0.84326982040771,
                             0.06942255089170368, -0.8108819513111141, -0.4632619432968461,
                             0.6985672085013377, -0.23761922585270234, 0.3923578470296971,
                             -0.15209771393777038, 0.49246469202660204, 0.09183857869718137,
                             -0.10474596754741738, 0.04402570562084018, 0.3557705642897955,
                             0.15773493984158593, 0.7997025294138487, -0.8592796485274952,
                             0.17283545582190385, 0.15281625180206393, 0.2853919036707935,
                             0.8105070666438707};
        HopfieldNeuralNet nn = new HopfieldNeuralNet(genotype);
        double[] result = nn.getGenotype();
        assertEquals(genotype.length, result.length);
        for (int i = 0, length = result.length; i < length; i++) {
            assertEquals(genotype[i], result[i], 0.000000000000000000001);
        }

        HopfieldNeuralNet nn2 = new HopfieldNeuralNet(new double[] {1.0, 2.0, 1.0, 2.0});
        nn2.setGenotype(genotype);
        double[] result2 = nn.getGenotype();
        assertEquals(genotype.length, result.length);
        for (int i = 0, length = result2.length; i < length; i++) {
            assertEquals(genotype[i], result2[i], 0.000000000000000000001);
        }
    }
    
    @Test
    public void testSigmoid() {
        assertEquals(nn.sigmoid(1.0), Math.abs(nn.sigmoid(-1.0)), tolerance);
        assertEquals(nn.sigmoid(0.0), 0.0, 0.0);
        assertTrue(nn.sigmoid(100) > nn.sigmoid(1));
    }
}