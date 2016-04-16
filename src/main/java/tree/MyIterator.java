/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tree;


import java.util.Iterator;

/**
 * John S Maass
 * 11/23/2014
 * Project 3
 * COSC 311
 * 
 * The MyIterator interface extends the Iterator interface and is used to 
 * iterate over a Collection.  It has two additional methods which allow
 * the iterator to traverse the Collection from front to back.
 * 
 */
public interface MyIterator extends Iterator{
    
    /**
     * Returns true if there are more elements in the backward direction.
     * 
     * @return True if there are more elements in the iteration.  False 
     *         otherwise.
     */
    public boolean hasPrevious();
    
     /**
     * Returns the next element, going from back to front, in the iteration.
     * 
     * @return Value of element to be returned.
     */
    public Object previous();
}
