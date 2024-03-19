/*
 * Jamison Schmidt
 * 1/24/2024
 * Assignment 1 - Word Search
 * 
 * Allows the user to do basic operations for a word search puzzle
 * Operations include the following:
 * - generating a word search with custom words
 * - displaying the word search
 * - showing the solutions to the word search
 * 
 * Extra credit:
 *  - OVERLAPPING
 *  - TRY/CATCH (uncertain)
 *  - ENUM (uncertain)
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class WordSearchClient {
    /*
     * Main class that interacts with the user
     * Takes and parses user input
     * User input is given to the WordSearchGame class for actions
     */

    enum UserCommand {
        /*
         * Basic enumerator that lists available commands for user input
         */
        Generate,   // Generate a new word search
        Display,    // Display the word search
        Solutions,  // Show the solutions for the word search
        Quit,       // Quit the application
    }

    // Scanner for user input
    public static Scanner scanner = new Scanner(System.in);

    // The default word search game
    // Is empty
    public static WordSearchGame wordSearch = new WordSearchGame();

    public static void main(String[] args) {
        // Start program by introducing the user
        printIntroduction();

        // Run the program forever until the user wants to quit
        boolean run_program = true;
        while (run_program) {
            // List available commands to the user each iteration
            listCommands();
            // Execute command with user input
            executeCommand(getCommandInput());
        }

    }

    public static void printIntroduction() {
        /*
         * Gives a short introduction of the program to the user
         */
        System.out.println("Welcome to my word search generator!");
        System.out.println("This program allows you to generate your own custom word search.");
    }

    public static void listCommands() {
        /*
         * Lists the available commands to the user
         */
        System.out.println("--------------------------------------------------");
        System.out.println("Please select an option:");
        System.out.println("Generate a new word search (g)");
        System.out.println("Display your word search (d)");
        System.out.println("Show the solutions to your word search (s)");
        // I had plans to implement the following commands but decided not to
        // System.out.println("Add words to your current word search (a)");
        // System.out.println("Load word search from file (l)");
        // System.out.println("Save your word search to a file (s)");
        System.out.println("Quit the program (q)");
    }

    public static UserCommand getCommandInput() {
        /*
         * Gets command input from user input from the console
         * User input is parsed to a command
         * Repeats until valid input is given
         */

        // By default there will be no valid command
        UserCommand ret = null;

        // Repeat until valid input is given from the user
        while (ret == null) {
            System.out.print("> ");  // Nice statement to suggest user input
            try {
                // Get the first character of user input
                char input = scanner.nextLine().charAt(0);
                // Check the first character
                switch (input) {
                    // Check user input for valid command char
                    case 'g': ret = UserCommand.Generate;  break;
                    case 'd': ret = UserCommand.Display;   break;
                    case 's': ret = UserCommand.Solutions; break;
                    case 'q': ret = UserCommand.Quit;      break;
                    default:
                        // User did not provide valid command char
                        // Notify user of error and repeat
                        System.out.println("Unknown command (" + input + ").");
                        break;
                }
            } catch (IndexOutOfBoundsException e) {
                // Called if the user does not provide any console input
                // Notify user of error and repeat
                System.out.println("Please provide a command.");
            }
        }
        return ret;  // Return valid input from user
    }

    public static String[] getWordListInput() {
        /*
         * Interperets and parses words from the user
         * This is used to generate a new WordSearchGame
         */

        // Give instructions to the user on how to list words
        System.out.println("What words would you like in your word search?");
        System.out.println("The words must have at least 4 letters to be accepted.");
        System.out.println("Any non-alphabetic characters will be removed.");
        System.out.println("These can be space and/or row delimitted.");
        System.out.println("Press (q) when you are done.");

        // Create ArrayList to collect valid words from user
        ArrayList<String> words = new ArrayList<String>();

        // Repeat until the user is done supplying words
        boolean collect_input = true;
        while (collect_input) {
            // Gets user input, converts it to lowercase, removes non-letters and spaces,
            // separates words by space
            System.out.print("> ");
            
            // 1. Get user input from console
            // 2. Set input to lower case
            // 3. Remove all not alphabetic characters (except spaces)
            // 4. Split into multiple words for each space character
            String[] input = scanner.nextLine().toLowerCase().replaceAll("[^a-z ]", "").split(" ");

            // Iterate through each given word from user
            for (String word : input) {
                if (word.length() >= 4) {
                    // Short tutorial on java regex: https://www.w3schools.com/java/java_regex.asp
                    // If the word is at least 4 characters long, add it to the word list
                    words.add(word);
                } else if (word.equals("q")) {
                    // The user is done supplying words
                    if (words.size() >= 1) {
                        // If the user supplied at least one word, exit
                        collect_input = false;
                        break;
                    } else {
                        // If user did not provide any words
                        // Notify user of error and repeat
                        System.out.println("You need to provide more words.");
                    }
                }
            }
        }

        return words.toArray(new String[0]);  // Return valid words as an String array
    }

    public static void executeCommand(UserCommand cmd) {
        /*
         * Executes a given command on the current wordSearch object
         */
        switch (cmd) {
            case Generate:  // Generate new word search
                String[] wordsList = getWordListInput();     // Get words list from user
                wordSearch = new WordSearchGame(wordsList);  // Create new object for word search
                break;
            case Display:  // Display current word search
                wordSearch.display();    // Display current word search
                wordSearch.listWords();  // List words in word search
                break;
            case Solutions:  // Show solutions to current word search
                wordSearch.displaySolutions();
                break;
            case Quit:  // Quit the application
                System.out.println("Exiting application...");
                System.exit(0);
                break;
            default:  // Program should never reach this point, but it is here for easy debug
                System.out.println("Unknown command (" + cmd + ") called in executeCommand.");
                break;
        }
    }
}

class WordSearchGame {
    /*
     * This class where the magic happens for developing a word search game
     * and its related features
     * 
     * Can generated complex word searches with an overlapping algorithm
     * Takes in a String array of custom words from the Main class to use
     * Can cleanly disply the word search and the solutions
     * Solutions use color-based text codes, which may not work in all terminals
     */

    // IDEA: Can use a seed for saved files. This way files only have to save a seed and
    // the given words. This can greatly simplify file saving and loading.

    // Create a new random generator
    private static Random random = new Random();

    // Terminal color codes for solutions
    private static final String[] COLORS = {"\u001B[40m", "\u001B[41m", "\u001B[42m",
        "\u001B[43m", "\u001B[44m", "\u001B[45m", "\u001B[46m", "\u001B[47m"};

    // Size of the board
    private int boardSize = 0;

    private char[][] board;          // Represents the chars filling the full board
    // IDEA: Consider each solution representing a different bit
    // this way multiple solutions can be tracked in a single cell from overlapping
    private int[][] boardSolutions;  // Represents all solutions in the board

    // The list of words in the wordSearch
    private String[] wordsList;

    public WordSearchGame() {
        // Use default words
        // If default words want to be used, they can be added here
    }

    public WordSearchGame(String[] words) {

        // Sorts the words from longest to shortest (Insertion Sort)
        for (int i = 1; i < words.length; i++) {
            int j = i;
            while (j > 0 && words[j-1].length() < words[j].length()) {
                String swap = words[j-1];
                words[j-1] = words[j];
                words[j] = swap;
                j -= 1;
            }
        }

        // Calculates the total amount of characters
        int totalSize = 0;
        for (String word : words) {
            totalSize += word.length();
        }

        // Longest word length + math that seems to work okay
        boardSize = words[0].length() + (int) (words.length / Math.sqrt(totalSize));
        wordsList = words;

        // Create the new board
        board = new char[boardSize][boardSize];
        boardSolutions = new int[boardSize][boardSize];

        // MAIN ALGORITHM FOR CREATING GRID //

        int wordID = 1;
        // wordsIterator:
        for (String word : words) {
            // Iterate through every word from the words list
            
            boolean wordAdded = false;  // Used to prevent word getting added multiple times
            // Get the length of the word
            // This will be important for efficiency purposes
            // We do not want to check spots where the word can't fit
            int wordLength = word.length();

            // This is how I randomly fill each cell
            int[] indexShuffleX = new int[boardSize];
            int[] indexShuffleY = new int[boardSize];

            // Fill arrays with index values (0-boardSize)
            for (int i = 0; i < boardSize; i++) {
                indexShuffleX[i] = i;
                indexShuffleY[i] = i;
            }

            // Shuffle the indexShuffles (with Fisher-Yates shuffling algorithm)
            for (int i = 0; i < boardSize - 1; i++) {
                int x = random.nextInt(boardSize - i) + i;
                int y = random.nextInt(boardSize - i) + i;

                int swapX = indexShuffleX[i];
                indexShuffleX[i] = indexShuffleX[x];
                indexShuffleX[x] = swapX;
                int swapY = indexShuffleY[i];
                indexShuffleY[i] = indexShuffleY[y];
                indexShuffleY[y] = swapY;
            }

            /* Notes on the shuffling algorithm
             * The following placement algorithm works by checking every cell and direction
             * until a given word is place on the grid. To avoid checking the same cell multiple
             * times, a randomly shuffled array of indices is used. This allows the algorithm to
             * check every cell at least once, in a random order.
             * 
             * This allows the seed method for file saving and loading to work because the
             * shuffling algorithm should shuffle like it does for other saved files.
             */

            /* directions: (not direction of word [forward vs. reverse], that is another variable)
                0: |
                1: /
                2: -
                3: \
            */
            int[] directions = {0, 1, 2, 3};  // Create an array of the four available directions for a word
            // Shuffle the direction into a random order (Fisher-Yates Shuffling Algorithm)
            for (int i = 0; i < directions.length - 1; i++) {
                int j = random.nextInt(directions.length);

                int swap = directions[i];
                directions[i] = directions[j];
                directions[j] = swap;
            }

            directionsIterator:
            for (int direction : directions) { 
                // Iterate through every direction (unless word gets placed first)

                // Determine the x and y direction of the word based on given direction
                // Making these integers will help with mathematical operations for palcement of
                // the word in the future
                int directionX = direction == 0 ? 0 : direction == 3 ? -1 : 1;
                int directionY = direction == 2 ? 0 : 1;

                // Iterate through each x-index
                // xIndexIterator:
                for (int x : indexShuffleX) {

                    // Check if the word length will be inside the grid
                    // If not, continue and check another cell
                    // This increases the efficiency of the algorithm by skipping redundant code
                    // because there will be no valid place for the word
                    int xError = x + (wordLength * directionX);
                    // Debug:
                    // System.out.println("XERROR: " + xError + "; DIRECCTIONX: " +
                    //    directionX + ", " + (wordLength * direction));
                    if (xError > boardSize || xError < 0) continue;

                    // Iterate through each y-index
                    yIndexIterator:
                    for (int y : indexShuffleY) {

                        // Check if the word length will be inside the grid
                        // If not, continue and check another cell
                        // This increases the efficiency of the algorithm by skipping redundant code
                        // because there will be no valid place for the word
                        if (y + wordLength * directionY >= boardSize) continue;

                        // Multiple checks to see if word can be placed at cell
                        // and be considered for reversal (i.e. "reverse" vs. "esrever")
                        boolean availableSlot = true;
                        boolean availableUpright = true; 
                        boolean availableInverted = true;

                        // Iterate through each character
                        for (int i = 0; i < wordLength; i++) {
                            // Get the next char position to check using x and y directions
                            int ix = x + i * directionX;
                            int iy = y + i * directionY;

                            // Check for avaialable spots
                            if (boardSolutions[ix][iy] != 0) {
                                // If spot is occupied, check for overlap, invert if needed
                                if (availableUpright && word.charAt(i) == board[ix][iy]) {
                                    // Overlap works
                                    if (word.charAt(wordLength - i - 1) != board[ix][iy]) {
                                        // Overlap works
                                        // But inversion will not work in the future
                                        availableInverted = false;
                                    }
                                } else if (availableInverted && word.charAt(wordLength - i - 1) ==
                                        board[ix][iy]) {
                                    // Overlap works if inverted (for all past sequences too)
                                    // But upright will not work in the future
                                    availableUpright = false;
                                } else {
                                    // Spot is occupied and will not work
                                    // Stop checking this cell and continue on
                                    availableSlot = false;
                                    continue yIndexIterator;
                                }
                            }
                        }

                        // Check if a slot is still available
                        if (availableSlot) {
                            // Slot is available
                            if (availableUpright && availableInverted ? random.nextBoolean() :
                                    availableInverted) {
                                // If the word is inverted (either by situation or random),
                                // invert the word
                                word = new StringBuilder(word).reverse().toString();
                            }

                            // Update the char array to include the new word
                            for (int i = 0; i < wordLength; i++) {
                                int ix = x + i * directionX;
                                int iy = y + i * directionY;

                                board[ix][iy] = word.charAt(i);
                                boardSolutions[ix][iy] = wordID;
                            }

                            wordAdded = true;
                            break directionsIterator;
                        }
                    } // END LOOP: yIndexIterator
                } // END LOOP: xIndexIterator
            } // END LOOP: directionsIterator

            // Notify user if word could be added or not
            if (!wordAdded) {
                System.out.println("FAILED TO ADD WORD! " + word);
            } else {
                System.out.println("ADDED WORD " + word);
            }
            wordID++;

            /*
            // Code snippet of basic attempt for creating loading sign
            System.out.print("|");
            for (int i = 0; i < wordID; i++) {
                System.out.print("=");
            }
            for (int i = 0; i < wordsList.length - wordID; i++) {
                System.out.print(" ");
            }
            System.out.print("|\r");
             */
            
            // Debug:
            // System.out.println(word);

        } // END LOOP: wordsIterator

        // Fill the rest of the board with random letters
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (boardSolutions[x][y] == 0) board[x][y] = (char) (random.nextInt(26) + 97);
            }
        }
    }

    public void listWords() {
        /*
         * List the words used in the word serach
         * WARNING: Words that fail to be included in the word search will still be listed
         */
        for (String word : wordsList) {
            System.out.println(word);
        }
    }

    public void display() {
        /*
         * Display the word search
         * Adds a space between each character for visual clarity
         */
        System.out.println("--------------------------------------------------");
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                System.out.print(board[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println("--------------------------------------------------");
    }

    public void displaySolutions() {
        /*
         * Displays the solutions to the word search
         * Adds a space between each character for visual clarity
         * Color codes different solutions
         * WARNING: Color codes may not be supported in all terminals but should work for VSC
         * NOTE: Color codes will appear as the latest-updated word
         */
        System.out.println("--------------------------------------------------");
        // Iterate through each row
        for (int x = 0; x < boardSize; x++) {
            // Iterate through each column
            for (int y = 0; y < boardSize; y++) {
                // Print the value and related color if it is a solution, otherwise "X"
                System.out.print(boardSolutions[x][y] != 0 ? COLORS[boardSolutions[x][y] %
                    (COLORS.length - 1) + 1] + board[x][y] + COLORS[0] : "X");
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("--------------------------------------------------");
    }
}