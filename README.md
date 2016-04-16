# Threaded_Tree

This was a project for my Data Structures and Algorithms course.  In said project we had to implement a threaded tree inorder
to store the first and last names as well as the student ids of a list of students.  The list was provided by the professor 
as was the driver code.  The ThreadedTree.java contains the implementation of the threaded tree.

On load there will be two entires that are ommited because they have repeat student ids.  A menu should then appear which
allows you to pick different options.

1 Add a new student
 2 Delete a student
 3 Find a student by ID
 4 List students by ID increasing
 5 List students by first name increasing
 6 List students by last name increasing
 7 List students by ID decreasing
 8 List students by first name decreasing
 9 List students by last name decreasing
 
 0 End



#Compile and Run

To compile the project use the following maven command:

mvn install

To run use:

java -jar tree-1.0.jar
