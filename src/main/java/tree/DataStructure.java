/**
 * John S Maass
 * 11/24/2014
 * Project 3
 * COSC 311
 * 
 * The DataStructure class contains an unordered array of type 
 * DataStructureRecord.  DataStructureRecord is a private inner class which is
 * used to store the String values of the first name, last name and the student 
 * ID of different students.  It also contains three IndexTrees that allow the 
 * data stored in its DataStructureRecord array to be printed in either 
 * descending or ascending order based on the three different Strings stored in 
 * each DataStructureRecord.  When the class is first initialized it will load 
 * existing student data from a text into the DataStructureRecord array.  New
 * students can only be added to the DataStructureRecord array if they have
 * unique student IDs.  The class also has methods to search for a student by 
 * ID and delete a student by ID.
 * 
 */
package tree;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataStructure {
    
    final int DB_SIZE=200;
    final String FILE_NAME = "/data/Student_Data.txt";
    final String TABLE_HEADER = "      First Name | Last Name  | Stundent ID";
    
    DataStructureRecord[] dataRecords = new DataStructureRecord[DB_SIZE];
    
    ThreadedTree fName=new ThreadedTree();
    ThreadedTree lName=new ThreadedTree();
    ThreadedTree studentID=new ThreadedTree();
    
    Stack<Integer> recordDeleteStack = new Stack<>();
    
    int dRecordsPos=0;
    int currentSize=0;
    
    /**
     * Parameterless constructor for DataStructure that initializes the data
     * array with student info from the file location stored in FILE_NAME.
     */
    public DataStructure(){
        addFromTextFile(FILE_NAME);
    }

    
    /**
     * The addFromTextFile method adds new rows to the dataRecords array
     * from a text file.  It assumes that the unique data is separated onto
     * different lines and is in the order of last name, first name and student
     * ID.
     * 
     * @param fileName The String path to the text file containing the data to
     *                 be added.
     */
    private void addFromTextFile(String fileName){
        InputStream in = COSC311Driver.class.getResourceAsStream("/data/Student_Data.txt");
        BufferedReader buf = new BufferedReader(new InputStreamReader(in));
        buf.lines().forEach(str -> insert(str.split("_")[1],str.split("_")[0],str.split("_")[2]));
    }
    
    /**
     * The updateIndexes method updates the Threaded BST of the ThreadedTree
 objects when a new DataStructureRecord is added to the dataRecords array.
     * 
     * @param index int value of the index in the dataRecords array where the
     *              new DataStructureRecord was added.
     */
    public void updateIndexes(int index){
        fName.insert(index,dataRecords[index].firstName);
        lName.insert(index,dataRecords[index].lastName);
        studentID.insert(index,dataRecords[index].studentIDNumber);
        
    }
    
    
    /**
     * The search method searches for a given student ID in the studentID 
 ThreadedTree in order to determine if it is already present in the 
 studentID array.
     * 
     * @param id The String value of the ID to look for in studentIDIndex.
     * @return True if there is a IndexRecord that has a key value equal to
     *         id.  False otherwise. 
     */
    public boolean search(String id){
        return studentID.find(id)>-1;
    }
   
    /**
     * The insert method adds a new DataStructureRecord to the dataRecords 
     * array if there is space left in the array.  If no space is present a 
     * message notifying the user is printed out.
     * 
     * @param firstName String value of the first name of the student.
     * @param lastName String value of the last name of the student.
     * @param studentID String value of the ID of the student.
     */
    public void insert(String firstName, String lastName, String studentID){
        DataStructureRecord newRecord = 
                new DataStructureRecord(firstName.toLowerCase(), 
                lastName.toLowerCase(), studentID);
        int index=dRecordsPos;
        //Checks to see if db is full.
        if (currentSize >= DB_SIZE) {
            System.out.println("Could not add record: "+newRecord);
            System.out.println("The database is full.");
        }
        else if(search(newRecord.studentIDNumber)){
            System.out.println("Could not add record: "+newRecord);
            System.out.println("Student ID already exists in database.");
        }
        else if (!recordDeleteStack.empty()) {
            index=recordDeleteStack.pop();
            dataRecords[index] = newRecord;
            updateIndexes(index);
            currentSize++;
        } else {
            dataRecords[index] = newRecord;
            updateIndexes(index);
            dRecordsPos++;
            currentSize++;
        }
    }
    
    /**
     * The deleteStudent method sets the DataStructureRecord containing the 
     * student ID passed to it equal to null in the dataRecords array and
     * removes its indexes from the three IndexTrees.
     * 
     * @param studentID String value of the student ID of the student that is 
     *                  to be removed from the database.
     */
    public void deleteStudent(String sID) {
        int indexDataRec = studentID.find(sID);

        studentID.delete(sID,indexDataRec);
        fName.delete(dataRecords[indexDataRec].firstName,indexDataRec);
        lName.delete(dataRecords[indexDataRec].lastName,indexDataRec);
        recordDeleteStack.push(indexDataRec);
        currentSize--;
    }
    
    /**
     * The getStudentData method prints the first and last name as well as the
     * student ID of a student based on the String parameter passed into the 
     * method.
     * 
     * @param id The String value of the student ID.
     * @return The String representation of the DataStructureRecord of the 
     *         student.
     */
    public String getStudentData(String id){
        return dataRecords[studentID.find(id)].toString();
    }
    
    /**
     * The listIt method accepts two parameters which are used to determine
 which ThreadedTree to use and in what direction the 
 DataStructureRecords should be printed in data.
     * 
     * @param indexID int value for which index should be used. 1 for first 
     *                name, 2 for last name and 3 for student ID.
     *             
     * @param direction int value for the which direction to print in. 1 for
     *                  increasing direction and 2 for decreasing.
     */
    public void listIt(int indexID, int direction){
        ThreadedTree temp=null;
        printHeader();
        switch (indexID) {
            case 1:
                temp = fName;
                break;
            case 2:
                temp = lName;
                break;
            case 3:
                temp = studentID;
                break;
            default:
                break;
        }
        if(direction==1)
            printDataIncreasing(temp.iterator());
        else
            printDataDecreasing(temp.iterator());
        System.out.println();
    }
    
    /**
     * The printDataDecreasing prints the DataStructureRecords stored in data 
 in increasing order according to its position in the ThreadedTree.
     * 
     * @param iter The MyIterator object used to iterate over the data stored 
             in the ThreadedTree object.
     */
    public void printDataIncreasing(MyIterator iter){
        int counter=1;
        while(iter.hasNext())
            System.out.println(padNum(counter++)
                    +dataRecords[(int)iter.next()]);
    }
    
    /**
     * The printDataDecreasing prints the DataStructureRecords stored in data 
 in decreasing order according to its position in the ThreadedTree.
     * 
     * @param iter The MyIterator object used to iterate over the data stored 
             in the ThreadedTree object.
     */
    public void printDataDecreasing(MyIterator iter){
        int counter=currentSize;
        while(iter.hasPrevious())
            System.out.println(padNum(counter--)+
                    dataRecords[(int)iter.previous()]);
    }
    
    /**
     * The printHeader prints the table column names as well as a line of 
     * dashes below the column names.
     */
    public void printHeader(){
        System.out.println(TABLE_HEADER);
        for(int i =0;i<TABLE_HEADER.length();i++){
            System.out.print("-");
        }
        System.out.println();
    }
    
    /**
     * The padNumber method accepts an int, converts it to a String by adding 
     * a parentheses and a blank space to the right of the number and returns 
     * this String value.
     * @param num int value to be padded.
     * @return The String value of the padded int.
     */
    public String padNum(int num){
        String paddedNum=num+")";
        while(paddedNum.length()<5)
            paddedNum+=" ";
        return paddedNum;
    }

    /**
     * The DataStructureRecord class is used to store the String values of the
     * first name, last name and student ID of a student.  
     */
    private class DataStructureRecord{
        
        String firstName, lastName, studentIDNumber;
        
        /**
         * Default constructor for the DataStructureRecord class.  All 
         * variables are initialized to empty Strings.
         */
        public DataStructureRecord(){
            firstName="";
            lastName="";
            studentIDNumber="";
        }
        
        /**
         * Constructor that accepts three String parameters which are used to
         * initialize the values of the class variables firstname, lastName and
         * studentIDNumber.
         * 
         * @param firstName The String value to be assigned to the firstName
         *                  variable.
         * @param lastName The String value to be assigned to the lastName
         *                 variable.
         * @param studentIDNumber The String value to be assigned to the 
         *                         studentIDNumber variable.
         */
        public DataStructureRecord(String firstName, String lastName, 
                String studentIDNumber){
            this.firstName=firstName;
            this.lastName=lastName;
            this.studentIDNumber=studentIDNumber;
        }
        
        /**
         * The toString method returns a String containing the firstName, 
         * lastName and studentIDNumber variables.
         * 
         * @return String containing firstName,lastName and studentIDNumber 
         *         variables
         */
        @Override
        public String toString(){
            
            return formatString(firstName)+"|"+formatString(lastName)+"|"+
                    formatString(studentIDNumber);
        }
        
        /**
         * The formatString method pads the left side of the String passed into
         * it with white space until the String has a length of 12 characters
         * and returns the value.  If the String passed into the method is 
         * longer then 12 characters the method just returns the String without 
         * any padding.
         * @return The String value of the padded String.
         */
        public String formatString(String s){
            s=" "+s;
            while(s.length()<12)
                s+=" ";
            return s;
        }
    }
    
    
}
