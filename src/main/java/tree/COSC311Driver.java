/**
 * John S Maass
 * 11/24/2014
 * Project 3
 * COSC 311
 * 
 * This is the main driver program for project 1.  It allows a user to add, 
 * delete and find different students stored in DataStructure.  It also allows
 * the user to print the data stored in DataStructure in either decreasing or
 * increasing order.  
 * 
 */

package tree;


/*
 * IMPORTANT!!!!!!
 * Your projects should not contain any code in this file that modifies the 
 * class DataStructure directly.
 * You may find it convenient (but not required) that the file DataStructure 
 * contain an inner class.
*/



import java.util.Collections;
import java.util.Scanner;

public class COSC311Driver
{
    static Scanner keyboard=new Scanner(System.in);
    static DataStructure myStructure = new DataStructure();
    public static void main(String[] args)
    {
        /*The following declaration declares a data structure that will change 
         * from one assignment to the next. For example, you will need to 
         * implement the following as a doubly linked list, as well as a tree.
         */ 
        int response;
//        int id=10000;
//        for(char c=97;c<123;c++ ){
//            System.out.println(""+c+c+c+c+c);
//            myStructure.insert(""+c+c+c+c+c, ""+c+c+c+c+c, ""+id);
//            myStructure.deleteStudent(""+id);
//            id+=3300;
//        }
        
        
        do
        {
            System.out.println(" 1 Add a new student");
            System.out.println(" 2 Delete a student");
            System.out.println(" 3 Find a student by ID");
            System.out.println(" 4 List students by ID increasing");
            System.out.println(" 5 List students by first name increasing");
            System.out.println(" 6 List students by last name increasing");
            System.out.println(" 7 List students by ID decreasing");
            System.out.println(" 8 List students by first name decreasing");
            System.out.println(" 9 List students by last name decreasing");
            System.out.println(" ");
            System.out.println(" 0 End");
//            
            response=keyboard.nextInt();
            
            switch (response)
            {
                case 1: addIt();
                        break;
                case 2: deleteIt();
                        break;
                case 3: findIt();
                        break;
                case 4: listItIncreasingID();
                        break;
                case 5: listItIncreasingfName();	
                        break;
                case 6: listItIncreasinglName();
                        break;
                case 7: listItDecreasingID();
                        break;
                case 8: listItDecreasingfName();
                        break;
                case 9: listItDecreasinglName();
                        break;
                default:                
            }
            
        } while (response!=0);
        
        
        
        
    }
    
    // OK Folks. I won't write all of these, but I'll give you an idea
    
    // Case 1: Add a new student. We need a unique ID number
    
    public static void addIt()
    {
        String name1,name2,tempID;
        boolean found;
        
        do
        {
            System.out.println("Enter a unique ID number to add");
            tempID=keyboard.next();
        
            //is it unique ?
            found=myStructure.search(tempID);
            if (found)
            {
                System.out.println("ID already in use");
                System.out.println("Please re-enter a unique ID");
            }
        }
       while (found);
       
       // We found a unique ID. Now ask for first and last name
       
       System.out.println("Enter first name");
       name1=keyboard.next();
       System.out.println("Enter last name");
       name2=keyboard.next();
       
       // add to our data structure
       myStructure.insert(name1,name2,tempID);
    }


    // Case 2: delete a student. A student ID must be prompted for. If the ID 
    //number does not exist in the database,
    //print out a message indicating a such, otherwise delete the entire record

    public static void deleteIt()
    {
        String studentID="";
        System.out.println("Enter a the ID of the student"+
                " you want to delete: ");
        studentID=keyboard.next();
        
        if(myStructure.search(studentID)){
            myStructure.deleteStudent(studentID);
            System.out.println("The student with the ID "+
                    " has been deleted.");
        }
        else
            System.out.println("The student ID you entered"+
                    " could not be found.");
    }

    //Case 3: find a student. A student ID must be prompted for. If the ID 
    //number is not found, print out a
    //    message indicating as such. Otherwise print out the entire record

    public static void findIt()
    {
        String studentID = "";
        System.out.println("Enter a the ID of the student you want to find: ");
        studentID = keyboard.next();
        if (myStructure.search(studentID)) {
            System.out.println("The index record for the student you were"+
                  " looking for is: " + myStructure.getStudentData(studentID));
        } else
            System.out.println("The student ID you entered"+
                    " could not be found.");
    }
        
    // Cases 4,5,6,7,8,9
    // A little programming trickery. All of the listing methods below can 
    //call the SAME method in DataStructure.
    // We'll pass 2 parameters: the first indicates which field, the second 
    //indicates which order
    // fieldNum=1 first name
    // fieldNum=2 last name
    // fieldNum=3 ID
    // orderNum=1 increasing
    // orderNum=2 decreasing
    
    public static void listItIncreasingID()
    {
        myStructure.listIt(3,1);
    }
                        
    public static void listItIncreasingfName()
    {
        myStructure.listIt(1,1);
    }
                        
    public static void listItIncreasinglName()
    {
        myStructure.listIt(2,1);
    }
     public static void listItDecreasingID()
    {
        myStructure.listIt(3,2);
    }
                        
    public static void listItDecreasingfName()
    {
        myStructure.listIt(1,2);
    }
                        
    public static void listItDecreasinglName()
    {
        myStructure.listIt(2,2);
    }      
        
}    
     

