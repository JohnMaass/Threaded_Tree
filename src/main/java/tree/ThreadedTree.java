/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tree;


import java.util.ArrayList;

/**
 * John S Maass
 11/23/2014
 Project 3
 COSC 311
 
 The ThreadedTree class contains a threaded binary search tree(BST) as well as
 a private inner class called Node.  The Nodes object are stored in the
 * BST and ordered by the String variable key in Node.  This class also 
 * contains an iterator that can iterate either forward or backward through the
 * threaded tree, but never both at the same time.
 *
 */
public class ThreadedTree  implements Iterable{
    private Node root;
    //Stores Node that points to the smallest entry
    private Node inOrderStart;
    //Stores Node that points ot the largest entry
    private Node reverseStart;
    
    /**
     * Default constructor for the IndexTree class.
     */
    public ThreadedTree(){
        root=null;
        inOrderStart=new Node("",-1,false);
        inOrderStart.start=true;
        reverseStart=new Node("",-1,false);
        reverseStart.end=true;
    }
    
    /**
     * The insert method takes an int and a String parameter, creates a new 
     * Node object and inserts the Node in its proper order in the threaded
     * BST.  
     * 
     * @param recordNumber int value of the unique key of the key String.
     * @param key String value to be stored in node.
     */
    public void insert(int recordNumber, String key){
        Node current=root;
        
        Node newNode=new Node(key,recordNumber,false);
        if(root==null){
            root=new Node(key,recordNumber,false);
            root.leftChild=inOrderStart;
            root.leftIsThread=true;
            inOrderStart.rightChild=root;
            root.rightChild=reverseStart;
            root.rightIsThread=true;
            reverseStart.rightChild=root;
        }
        else{
            while(true){
                //Values greater or equal then x
                if(current.value.compareTo(key)<=0){
                    if(current.rightIsThread){
                        //Push old thread down.
                        newNode.rightIsThread=true;
                        newNode.rightChild=current.rightChild;
                        //Thread to parent
                        newNode.leftIsThread=true;
                        newNode.leftChild=current;
                        //Remove past Thread refs
                        current.rightIsThread=false;
                        current.rightChild=newNode;
                        //Reset end if needed.
                        if(newNode.rightChild.end)
                            newNode.rightChild.rightChild=newNode;
                        break;
                    }
                    current=current.rightChild;
                }
                //Values less then x
                else{
                    if(current.leftIsThread){
                        //Push old thread down.
                        newNode.leftIsThread=true;
                        newNode.leftChild=current.leftChild;
                        //Thread to parent
                        newNode.rightIsThread=true;
                        newNode.rightChild=current;
                        //Remove past Thread refs
                        current.leftIsThread=false;
                        current.leftChild=newNode;
                        newNode.isLeftChild=true;
                        //Reset start if needed.
                        if(newNode.leftChild.start)
                            newNode.leftChild.rightChild=newNode;
                        break;
                    }
                    current=current.leftChild;
                }
            }
        }
    }
    
    /**
     * The find method searches to BST for a node that contains the String
     * parameter passed into it. It WILL NOT WORK if more then one node
     * contains the same key value in the BST.  If it is found it returns
     * the Node value recordNumber.  If it is not found, -1 is returned.
     * 
     * @param key The String value to search for.
     * @return The value of recordNumber of the Node that contains the key 
     *         value.  -1 otherwise.
     */
    public int find(String key){
        Node current=root;
        while(current!=null){
            if (current.value.equals(key)) {
                return current.recordNumber;
            } else if (current.value.compareTo(key) <= 0 
                    && !current.rightIsThread) {
                current = current.rightChild;

            } else if (current.value.compareTo(key) > 0 
                    && !current.leftIsThread) {
                current = current.leftChild;
            } else {
                current = null;
            }
        }
        return -1;
    }
    
    /**
     * The delete method will delete the Node whose key and recordNumber 
     * variables are equal to the String and int value passed into the method
     * from the BST.
     * 
     * @param key String value of Node to be deleted.
     * @param recordNumber int value of Node to be deleted.
     * @return 
     */
    public boolean delete(String key,int recordNumber){
        Node current=root;//Node being deleted
        Node parent=root;
        Node replacement=null;
        if(root!=null){
            while(true){
                if (current.value.equals(key) && 
                        current.recordNumber==recordNumber){
                    break;
                }
                else if(current.value.compareTo(key)<=0&&!current.rightIsThread){
                    parent=current;
                    current=current.rightChild;
                }
                else if(current.value.compareTo(key)>0&&!current.leftIsThread){
                    parent=current;
                    current=current.leftChild;
                }
                else{
                    current=null;
                    break;
                }
            }
            //If it wasn't found.
            if(current==null)
                return false;
            //No children.
            else if(current.leftIsThread && current.rightIsThread){
                //If only one node in tree.
                if(current==root){
                    root=null;
                }
                else if(current.isLeftChild){
                    replacement=parent;
                    parent.leftChild=parent.leftChild.leftChild;
                    parent.leftIsThread=true;
                }
                else{
                    replacement=parent;
                    parent.rightChild=parent.rightChild.rightChild;
                    parent.rightIsThread=true;
                }
            }
            //One Child.
            else if(current.leftIsThread || current.rightIsThread ){
                //If node to be deleted is root.
                if(current==root){
                    if(!current.rightIsThread){
                        replacement=getPredecessor(root);
                        replacement.leftChild=root.leftChild;
                        root=current.rightChild;
                    }
                    else{
                        replacement=getSuccessor(root);
                        replacement.rightChild=root.rightChild;
                        root=current.leftChild;
                        root.isLeftChild=false;
                    }
                    
                }
                else{
                    if(current.isLeftChild){
                        if (current.rightIsThread) {
                            parent.leftChild=current.leftChild;
                            getSuccessor(current).rightChild=current.rightChild;
                        } else {
                            parent.leftChild=current.rightChild;
                            parent.leftChild.isLeftChild=true;
                            replacement=getPredecessor(current);
                            replacement.leftChild=current.leftChild;
                        }
                    } else {
                        if (current.leftIsThread) {
                            parent.rightChild=current.rightChild;
                            getPredecessor(current).leftChild=current.leftChild;
                        } else {
                            parent.rightChild=current.leftChild;
                            parent.rightChild.isLeftChild=false;
                            replacement=getSuccessor(current);
                            replacement.rightChild=current.rightChild;
                        }
                    }
                }
            }
            //Two children.
            else{
                replacement=getPredecessor(current);
                delete(replacement.value, replacement.recordNumber);
                if(current==root){
                    root=replacement;
                    replacement.rightChild = current.rightChild;
                    replacement.leftChild = current.leftChild;
                    replacement.leftIsThread = current.leftIsThread;
                    replacement.rightIsThread = current.rightIsThread;
                    replacement.isLeftChild = false;
                }
                else if(current.isLeftChild){
                    parent.leftChild = replacement;
                    replacement.rightChild = current.rightChild;
                    replacement.leftChild = current.leftChild;
                    replacement.leftIsThread = current.leftIsThread;
                    replacement.rightIsThread = current.rightIsThread;
                    replacement.isLeftChild = true;
                } else {
                    parent.rightChild = replacement;
                    replacement.rightChild = current.rightChild;
                    replacement.leftChild = current.leftChild;
                    replacement.leftIsThread = current.leftIsThread;
                    replacement.rightIsThread=current.rightIsThread;
                    replacement.isLeftChild = false;
                }
                if(!current.rightIsThread )
                    getPredecessor(current).leftChild=replacement;
                if(!current.leftIsThread)
                    getSuccessor(current).rightChild=replacement; 
            } 
            
            //Changes start and end pointers.
            if(replacement!=null && replacement.leftChild.start)
                replacement.leftChild.rightChild=replacement;
            if(replacement!=null && replacement.rightChild.end)
                replacement.rightChild.rightChild=replacement;
            int x=0;
            return true;
        }
        return false;
    }

    /**
     * The getPredecessor method returns the Node who is the predecessor to the
     * Node that was passed into the method.
     * 
     * @param current Node whose predecessor is to be found.
     * @return Predecessor of Node passed into the method.
     */
    private Node getPredecessor(Node current) {
        Node predecessor=current.rightChild;
        while(!predecessor.leftIsThread){
            predecessor=predecessor.leftChild;
        }
        return predecessor;
    }
    
    /**
     * The getSuccessor method returns the Node who is the successor to the
     * Node that was passed into the method.
     * 
     * @param current Node whose successor is to be found.
     * @return Successor of Node passed into the method.
     */
    private Node getSuccessor(Node current){
        Node successor = current.leftChild;
        while (!successor.rightIsThread) {
            successor = successor.rightChild;
        }
        return successor;
    }
    
    /**
     * The iterator method creates and returns a new MyIterator object.
     * 
     * @return MyIterator object.
     */
    @Override
    public MyIterator iterator() {
        return new BSTIterator();
    }
    
    /**
     * The BSTIterator class implements the MyIterator interface allowing the
 BST in the ThreadedTree class to be iterated over in either the forward or
 backward directions, but never both at the same time.  
     */
    public class BSTIterator implements MyIterator{
        
        Node currentFront=inOrderStart.rightChild;
        Node currentBack=reverseStart.rightChild;
        
        /**
         * This method returns true only if there is another item in the 
         * iteration in the forward direction.
         * 
         * @return true if there is another item in the iteration.  false 
         *         otherwise.
         */
        @Override
        public boolean hasNext() {
            return currentFront.end!=true;
        }

        /**
         * This method returns the int value of the key stored in the node of 
         * the next node in the iteration as an Object.
         * 
         * @return Object of the key variable stored in the Node.
         */
        @Override
        public Object next() {
            int temp=currentFront.recordNumber;
            if(currentFront.rightIsThread){
                currentFront=currentFront.rightChild;
            }
            else{
                currentFront=getPredecessor(currentFront);
            }
            return temp;
        }
        
        /**
         * This method returns true only if there is another item in the 
         * iteration in the backward direction.
         * 
         * @return true if there is another item in the iteration.  false 
         *         otherwise.
         */
        @Override
        public boolean hasPrevious() {
            return currentBack.start!=true;
        }
        
        /**
         * This is an abstract method from the Iterator interface.  By default,
         * it has to be implemented but no code is added since it is never used.
         */
        @Override
        public void remove() {
        }

        
        /**
         * This method returns the int value of the key stored in the node of 
         * the previous node in the iteration as an Object.
         * 
         * @return Object of the key variable stored in the Node.
         */
        @Override
        public Object previous() {
            int temp=currentBack.recordNumber;
            if(currentBack.leftIsThread){
                currentBack=currentBack.leftChild;
            }
            else{
                currentBack=getSuccessor(currentBack);
            }
            return temp;
        }
    }
    
    /**
     * The Node class contains a String variable used to store either the
     * first name, last name or student ID of a student as well as a int value
     * used to store the index value of the student in the main student array
     * in DataStructure.
     */
    private class Node{
        Node rightChild;
        Node leftChild;
        boolean isLeftChild;
        boolean rightIsThread,leftIsThread;
        boolean start,end;
        String value;
        int recordNumber;

        public Node(String key, int recordNumber,boolean isLeftChild){
            this.recordNumber=recordNumber;
            this.value=key;
            this.isLeftChild=isLeftChild;
            end=false;
            start=false;
            rightIsThread=false;
            leftIsThread=false;
        }
        
    }
}
