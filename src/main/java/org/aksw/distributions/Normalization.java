/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aksw.distributions;

import java.util.List;

/**
 *
 * @author ngonga
 */
public interface Normalization {
    List<Fact> computeSmoothing(List<Fact> facts);  
}
