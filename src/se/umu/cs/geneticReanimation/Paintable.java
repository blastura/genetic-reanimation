package se.umu.cs.geneticReanimation;

/**
 * Interface to make parts paintable by Processing.
 *
 * @author Anton Johansson
 * @version 1.0
 */
public interface Paintable { 
    /**
     * Implement to paint class to a processing.core.PApplet
     *
     * @param parent a <code>processing.core.PApplet</code> value
     */
    void paint(processing.core.PApplet parent);
}