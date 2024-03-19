/*
 * Jamison Schmdt
 * 3/5/2024
 * Lab 5 - Pascal's Triangle
 * 
 * This program uses recursion to calculate all values in a given number of rows of Pascal's
 * Triangle. Recusion is the process of repeating a function until a wanted result is met.
 */

import java.util.InputMismatchException;
import java.util.Scanner;

public class PascalsTriangle {
    /*
     * This is the main class for the program. It manages everything. Include user inputs,
     * the recursive function, and the printing.
     */

    // Maps an entire grid of Pascal's Triangle
    // long used instead of int for greater depth searches
    private static long[][] triangle = {{1}};
    // The size of the latest triangle.
    private static int size;

    // Used to get user integer input
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        /*
         * This is the main loop for the program.
         * It handles the the results of the user input and the triangle grid.
         */

        // Start by introducing the user to the program.
        introduction();

        // Loop will repeat forever unless broken by user input
        while (true) {
            // Get the user's input on the size of triangle they want to generate.
            size = getUserInput();
            if (size == 0) {
                // If the user provides a 0, close the program by breaking the loop.
                System.out.println("Exiting program.");
                break;
            }
            System.out.println("Triangle level " + size + ":");

            if (size > triangle.length) {

                // Because the user is requestion a triangle bigger than previously processed,
                // a bigger grid needs to be made to capture the values of the larger triangle.
                // However, to improve the performance capabilities, we want to keep all of the
                // previously processed data
                long[][] new_triangle = new long[size][size];
                for (int r = 0; r < triangle.length; r++) {
                    for (int c = 0; c < triangle.length; c++) {
                        new_triangle[r][c] = triangle[r][c];
                    }
                }
                triangle = new_triangle;  // Update the triangle with the enlarged grid

                // Start the recursion to find the triangle
                // We do not care about the resulting from this process
                triangleRecursive(size - 1, size - 1);
            }
            
            // Print the triangle
            // It will use the size parameter set by the user at the start of the loop
            printTriangle();
        }
    }

    public static void introduction() {
        /*
         * Gives the user a brief introduction to the program.
         * This includes instructions on how it works.
         */

        System.out.println();
        System.out.println("Welcome to Pascal's Triangle Generator!");
        System.out.println("This program can generate Pascal's Triangle using recursion.");
        System.out.println("The results will be printed out to the console.");
        System.out.println("The user can provide the depth of the search of the triangle.");
        System.out.println("To quit the program, submit '0'.");
        System.out.println();
    }

    public static int getUserInput() {
        /*
         * Gets and returns an integer input from the user.
         * This will handle *hopefully* all user input errors.
         */

        // Introduce the user - let them know that they are providing input
        System.out.println(
            "Provide the depth of triangle you want to find, or enter '0' to quit.");

        int user_input = 0;
        boolean has_input = false;
        // Will repeat forever until valid user input is provided
        while (!has_input) {
            try {
                System.out.print("> ");  // Used to signify user input
                user_input = scanner.nextInt();  // Read user integer input
                // If the program reaches this point, the input must be valid
                has_input = true;
            } catch (InputMismatchException e) {
                // User provided a value that was not an integer.
                System.out.println("Please provide an integer.");
            } finally {
                // Clear the input buffer before continuing
                scanner.nextLine();
            }
        }
        // Return the valid user input
        return user_input;
    }

    private static String repeatSequence(String string, int count) {
        /*
         * This is a helper class to help with a simple for-loop to help with the beautifying
         * process. This is mostly used by the printTriangle method.
         * Takes a String sequence that is then repeated count times.
         */
        // Uses a StringBuilder because it is more efficient than recreating multiple Strings
        StringBuilder s = new StringBuilder();
        // Repeat for count number of iterations
        for (int i = 0; i < count; i++) {
            // Add the given sequence to the output
            s.append(string);
        }
        return s.toString();  // Return the results as String

    }

    public static long triangleRecursive(int row, int col) {
        /*
         * This function recursively calculates the values in the Pascal's triangle. Once as value
         * is fully calculated (meaning all of its recursion steps have been returned to it), it
         * saves its value to the 2d triangle grid. This is feature makes the program more
         * efficient because every node only needs to be calculated at most one time. Once its
         * value is known, it returns the value from the grid instead of recalculating its value.
         */

        // 

        // (row >= 0 && col >= 0) should always be true, so no need to have additional protections
        long oldVal = triangle[row][col];

        if (oldVal > 0) {
            // If value is known, return known value
            return oldVal;
        } else if (row == 0 || col == 0) {
            // If an edge has been found
            // Further recursion is not necessary, so just return 1
            triangle[row][col] = 1;  // Save results to grid
            return 1;
        } else {
            // An unknown value that's not an edge-piece needs to be calculated
            // This value is found by recursively checking the adjacent row and column
            long newVal = triangleRecursive(row - 1, col) + triangleRecursive(row, col - 1);
            triangle[row][col] = newVal;  // Save results to grid
            return newVal;  // Return overall value once recursion is complete
        }
    }

    public static void printTriangle() {
        /*
         * This methods pints the 2d grid which represents the triangle.
         * This also tries to beautify the output, so that it is clearer to see the triangle.
         */

        // Get the greatest number of digits for a number in the triangle.
        int maxLength = String.valueOf(triangle[(int) (size / 2)][(int) (size / 2)]).length();
        int separation = 2;  // Generic variable (can be anything >=0. >=2 recommended.)

        // Encloses the top of the triangle by adding a boarder
        System.out.println("+" + repeatSequence(repeatSequence("-",
                           (maxLength + separation)) + "+", size));

        for (int r = 0; r < size; r++) {
            System.out.print("|");
            for (int c = 0; c < size - r; c++) {
                // Get the value at the given point
                long value = triangle[r][c];
                // Get the number of digits for the value
                int length = String.valueOf(value).length();
                // Get the available padding for the number
                int split = maxLength - length + separation;

                // These apply the padding to the left and right side of each number
                int spaceLeft = 0;
                int spaceRight = split;
                if (split >= 2) {
                    spaceLeft = (int) (split / 2);
                    spaceRight = split - spaceLeft;
                }

                // Creates the padding and surrounds the output value
                System.out.print(repeatSequence(" ", spaceLeft) + value +
                                 repeatSequence(" ", spaceRight) + "|");
                
            }
            // Create a new line for a new row
            System.out.println();
            System.out.println("+" + repeatSequence(repeatSequence("-",
                               (maxLength + separation)) + "+", (size - r)));
        }
    }

}
