/*
Jamison Schmidt
1/23/2024
Lab 3 - Letter Inventory

This program uses two classes.
LetterInventoryClient acts as the main program which the user interacts with.
LetterInventory is a class that acts like a list for letters.
LetterInveotory includes some methods which apply various operations to the saved letters.
These methods include:
    get - get the number of occurrences for a letter
    set - set the number of occurrences for a letter
    add - add another LetterInventory to the current
    subtract - subtract another LetterInventory to the current
        Note that no count for a letter will go below zero (for example: 1 - 2 => 0)
    size - returns the size of the LetterInvenotry
These methods are called by the LetterInventoryClient from console input from the user.
 */
import java.util.InputMismatchException;
import java.util.Scanner;


class LetterInventoryClient {
    /*
     * This is the main class for the program.
     * This interacts with the user through the console.
     * It intakes user commands and parameters to operate the LetterInventory objects.
     */
    enum LetterInventoryCommand {
        // This is the list of commands available to the user
        INITIALIZE,  // For creating a letter inventory
        GET,         // Get the number of occurences of a letter
        SET,         // Set the number of occurences for a letter
        ADD,         // Add a new letter inventory to current
        SUBTRACT,    // Subtract a new letter inventory to current
        SIZE,        // Get the size of the letter inventory
        EXIT,        // Exit the program
    }

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        /*
         * This contains the main loop
         * It repeats until the program is exited
         */
        // Create an initial empty LetterInventory
        LetterInventory inventory = new LetterInventory("");

        boolean runProgram = true;
        // Repeat until the program is exited
        while (runProgram) {
            // List the available commands to the user
            listCommands();
            // Get command input from the user
            LetterInventoryCommand cmd = getCommandInput();
            switch (cmd) {
                case INITIALIZE:
                    // If the command is INITIALIZE, ask for data for the new LetterInventory
                    System.out.print(
                    "What data would you like to initiate your new inventory with?\nUser input: "
                    );
                    // Update the inventory variable with new LetterInventory
                    inventory = new LetterInventory(scanner.nextLine());
                    break;
                case EXIT:
                    // If the command is EXIT, exit the program
                    System.out.println("Exiting program...");
                    runProgram = false;
                    System.exit(0);
                    break;
                default:
                    // If it was some other command, execute it through method
                    executeCommand(cmd, inventory);
                    break;
            }
            System.out.println("--------------------------------------------------");
            // Print out the latest LetterInventory
            if (inventory.isEmpty()) {
                System.out.println("[Inventory is Empty]");
            } else {
                System.out.println(inventory);
            }
        }
        
    }

    public static void listCommands() {
        /*
         * Lists the available commands and the respective keys to use to call them to the user
         */
        System.out.println("--------------------------------------------------");
        System.out.println("i - create new inventory");
        System.out.println("s - get size of the inventory");
        System.out.println("g - get number of occurrences for a character");
        System.out.println("= - set number of occurrences for a character");
        System.out.println("+ - add new inventory to current inventory");
        System.out.println("- - subtract new inventory to current inventory");
        System.out.println("X - exit the program");
        System.out.println("--------------------------------------------------");
    }

    public static LetterInventoryCommand getCommandInput() {
        /*
         * Gets console input from the user
         * Takes the first character of the user input
         * Checks if given key is an available command
         * Tries again if invalid command key is provided
         * @returns The inputted user command
         */
        LetterInventoryCommand ret = null;
        while (ret == null) {
            System.out.print("User input: ");
            try {
                // Get user input
                String input = scanner.nextLine(); 
                // Check first character of user input
                switch (input.charAt(0)) {
                    case 'i': ret = LetterInventoryCommand.INITIALIZE;  break;
                    case 's': ret = LetterInventoryCommand.SIZE;        break;
                    case 'g': ret = LetterInventoryCommand.GET;         break;
                    case '=': ret = LetterInventoryCommand.SET;         break;
                    case '+': ret = LetterInventoryCommand.ADD;         break;
                    case '-': ret = LetterInventoryCommand.SUBTRACT;    break;
                    case 'X': ret = LetterInventoryCommand.EXIT;        break;
                    default:
                        // User input was invalid
                        System.out.println(
                            input.charAt(0) + " is an invalid command. Please try again.");
                        break;
                        // Loop will re-iterate because ret is still null
                }
            } catch (StringIndexOutOfBoundsException e) {
                // User did not provide any character
                System.out.println("Invalid command.");
                // Try again
            }
        }
        // Return the command results
        return ret;
    }

    public static char getLetterInput() {
        /*
         * Used to get a single letter of input from the user
         * Tries again if invalid input is provided
         */
        System.out.println("Please provide a letter.");

        char ret = ' ';
        // Repeat until valid input is given
        boolean validInput = false;
        while (!validInput) {
            System.out.print("User input: ");
            try {
                // Check the first character from the user input
                char c = scanner.nextLine().charAt(0);
                // Check if the first character is a letter
                if (Character.isLetter(c)) {
                    // If character is a letter, stop loop and return results
                    validInput = true;
                    ret = Character.toLowerCase(c);
                } else {
                    // The character the user provided is not a letter
                    // Try again
                    System.out.println(c + " is not a valid letter. Please try again.");
                }
            } catch (StringIndexOutOfBoundsException e) {
                // User did not provide any characters
                // Try again
                System.out.println("Please provide a letter.");
            }
        }
        // Return the user-given letter
        return ret;
    }

    public static int getIntegerInput() {
        /*
         * Gets integer input from the user
         * Tries again if invalid input is provided
         */
        System.out.println("Please provide a number.");

        int ret = 0;
        // Repeat until valid input is given
        boolean validInput = false;
        while (!validInput) {
            System.out.print("User input: ");
            try {
                // Read the next integer from user
                ret = scanner.nextInt();
                // If catch statement not called at this point, input must be valid
                validInput = true;
            } catch (InputMismatchException e) {
                // User input was not an integer
                // Try again
                System.out.println("Please provide a number.");
            } finally {
                // Clear the input buffer by consuming leftover "\n"
                scanner.nextLine();
            }
        }
        // Return user-provided integer
        return ret;
    }

    public static void executeCommand(LetterInventoryCommand cmd, LetterInventory inventory) {
        /*
         * This takes a given command and calls the related operations on the given LetterInventory
         */
        switch (cmd) {
            case SIZE:
                // Get the size of the LetterInventory
                System.out.println("Inventory size: " + inventory.size());
                break;
            case GET:
                // Get the number of occurences of a given letter
                char cGet = getLetterInput();  // Gets the given letter
                System.out.println(
                    "Occurrences of letter " + cGet + ": " + inventory.get(cGet) + ".");
                break;
            case SET:
                // Set the number of occurences for a given letter
                char cSet = getLetterInput();  // Gets the given letter
                int iSet = getIntegerInput();  // Gets the wanted number for setting
                inventory.set(cSet, iSet);  // Applies operation to inventory
                System.out.println(
                    "Set occurrences of letter " + cSet + " to " + iSet + ".");
                break;
            case ADD:
                // Add a new LetterInventory to current
                System.out.print(
                    "What data would you like to add to your inventory?\nUser input: ");
                inventory.add(new LetterInventory(scanner.nextLine()));
                break;
            case SUBTRACT:
                // Subtract a new LetterInventory to current
                System.out.print(
                    "What data would you like to subtract from your inventory?\nUser input: ");
                inventory.subtract(new LetterInventory(scanner.nextLine()));
                break;
            case INITIALIZE:
            case EXIT:
                // Ignore these for now because they should have been called in the mainloop
                break;
        }
    }
}


class LetterInventory {
    /*
     * This acts as the class which stores letters
     * It works by using an array index for each letter in the alphabet
     *      a=0, b=1, ..., z=26
     * This array contains integers, which represent the number of occurences for that given char.
     * 
     * The size represents the total number of letters in the inventory.
     * It is a variable because it is faster to count a variable than index each character.
     * 
     * This class contains some operations which can be used on the characters
     *  * get - gets the number of occurences for a given character
     *  * set - sets the number of occurences for a given character
     *  * add - adds another LetterInventory to current
     *  * subtract - subtracts another LetterInventory to current
     *  * size - gets the size of the current LetterInventory (by returning size variable)
     */

    private final int ASCII_OFFSET = 97; // This is the offset used to quickly index characters
    private final int ALPHABET_LENGTH = 26;  // This is the length of the alphabet

    private int[] chars = new int[ALPHABET_LENGTH];  // Create a new array for each letter
    private int size = 0;  // The default size is zero


    public LetterInventory(String input) {
        // Convert the input to lowercase, get an input stream of characters,
        // run it in parallel for performance (order does not matter), and iterate through each
        input.toLowerCase().chars().parallel().forEach(c -> {
            if (Character.isLetter(c)) {
                // If the character is a letter, add it to the inventory
                // Assumes all characters are lowercase because input is initially set to lower case
                chars[c - ASCII_OFFSET]++;  // Increase the count for given letter
                size++;  // Increment the size because a letter was added
            }
        });
    }

    private int charToIndex(char c) {
        // Converts a given character to its index in the array (and the alphabet)
        return (int) Character.toLowerCase(c) - ASCII_OFFSET;
    }

    private char indexToChar(int i) {
        // Converts the given array index to the its related character
        return (char) (i + ASCII_OFFSET);
    }

    public int get(char target) {
        // Gets the number of occurrences for a given character
        return chars[charToIndex(target)];
    }

    public int get(int index) {
        // Gets the number of occurrences for a character at a given index
        return chars[index];
    }

    public void set(char target, int value) {
        // Sets the number of occurrences of a given character with a given value
        int index = charToIndex(target);  // Get the index of the character
        int oldValue = chars[index];  // Get the old number of occurrences (for setting the size)
        chars[index] = value;  // Update the number of occurrences
        size += value - oldValue;  // Update the size by the difference
    }

    public void add(LetterInventory inventory) {
        // Adds the letters in another LetterInventory to this LetterInventory
        // Iterate through each letter in the alphabet
        for (int i = 0; i < ALPHABET_LENGTH; i++) {
            // Get the number of occurrences in the other library and add to related letter
            chars[i] += inventory.get(i);
        }
        size += inventory.size();  // Simply update the size by adding size of other inventory
    }

    public void subtract(LetterInventory inventory) {
        // Subtract the current a given LetterInventory to this LetterInventory
        // Iterate through each letter in the alphabet
        for (int i = 0; i < ALPHABET_LENGTH; i++) {
            // Get the minimum occurrences of character from this or given inventory
            // This is to no go below the minimum of 0 for an index
            // For example 1 - 2 => 0 (because it does 1 - 1 = 0)
            int min = Math.min(chars[i], inventory.get(i));
            chars[i] -= min;  // Update the number of occurrences
            size -= min;  // Update the size
        }
    }

    public int size() {
        // Returns the current size variable
        // Hopefully it has been properly updated from the other methods
        return size;
    }

    public boolean isEmpty() {
        // Returns true if size is 0 (inventory is empty), otherwise false
        return size == 0;
    }

    public String toString() {
        // Converts the letters in the inventory to a string to be read
        StringBuilder ret = new StringBuilder();  // Use a StringBuilder because it is dynamic
        // Iterate through each index in the alphabet
        for (int i = 0; i < ALPHABET_LENGTH; i++) {
            if (chars[i] > 0) {
                // Consider if there is at least one character at index
                char c = indexToChar(i);  // Get character value at index
                for (int ii = 0; ii < chars[i]; ii++) {
                    // Append character to string for number of occurrences
                    // Note: I do not know a cleaner or nicer way to implement this
                    ret.append(c);
                }
            }
        }
        // Return the StringBuilder as a String
        return ret.toString();
    }

    public String toCompressedString() {
        // Converts the letters in the inventory to a string to be read
        // "Compressed" is supposed to make it shorter (at least for really long inventories),
        // and possibly easier to read
        StringBuilder ret = new StringBuilder();
        // Iterate through each character in the alphabet
        for (int i = 0; i < ALPHABET_LENGTH; i++) {
            if (chars[i] > 0) {
                // Consider if there is a least one character at index
                char c = indexToChar(i);  // Get character value at index
                ret.append("" + chars[i] + c + " ");  // Append it to the output
            }
        }
        // Return the StringBuilder as a String
        return ret.toString();
    }

}