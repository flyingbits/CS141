/*
Jamison Schmidt
1/9/2024
Lab 1 - Guessing Game

An interactive guessing game.
Each round, a random number is generated.
The user tries to guess the number in as few tries as possible.
The game will give hints based on whether the answer is higher or lower than the guessed number.
When a game is completed, the user will be asked to play again.
When the user no longer wants to play, all game statistics will be posted to the console.
*/

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class GuessingGame {

    // Greatest random number generated for guess
    private static final int GUESS_RANGE_MAX = 100;
    // Minimum random number generated for guess
    private static final int GUESS_RANGE_MIN = 1;

    // Scanner to get user input
    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        // Start by introducing the user to the game
        introduction();

        int rounds_played = 0;
        int total_guesses = 0;
        int minimum_guesses = Integer.MAX_VALUE;  // Greater than the 9,999 assummed max guesses by user

        do {
            // Play rounds with the user
            int guess_count = playRound();

            // Update game statistics
            rounds_played++;
            total_guesses += guess_count;
            if (guess_count < minimum_guesses) {
                // There is a new fewest guest count
                minimum_guesses = guess_count;
            }

            // Play another round if user wants to play again
        } while (userPlayAgain());

        System.out.println("Okay, thank you for playing.");

        // Close the scanner
        scanner.close();

        // Report game statistics to the user
        reportOverallResults(rounds_played, total_guesses, minimum_guesses);
    }

    public static void introduction() {
        /*
         * Method that introduces the user.
         * Prints information about how the game works to the console.
         */
        System.out.println("Welcome to the Guessing Game!");
        System.out.println("I will think of a number between " + GUESS_RANGE_MIN + " and " + GUESS_RANGE_MAX + ".");
        System.out.println("Your goal is to guess the number that I am thinking in as few attempts as possible.");
        System.out.println("I will give you hints to the correct answer based on your guesses.");
    }

    public static int playRound() {
        /*
         * Generates a random integer between GUESS_RANGE_MIN and GUESS_RANGE_MIN
         * Gives user hints until user guesses the correct number.
         * Returns the number of guesses made by the user
         */

        // Create a Random object to generate random integers
        Random random = new Random();
        
        // Generate a random integer in the range of GUESS_RANGE_MAX and GUESS_RANGE_MIN
        int number_goal = random.nextInt(GUESS_RANGE_MAX + 1 - GUESS_RANGE_MIN) + GUESS_RANGE_MIN;

        int number_guess = GUESS_RANGE_MIN - 1;
        int try_count = 0;

        // Remind user of user of guess boundaries
        System.out.println();
        System.out.println("I have thought of a number between " + GUESS_RANGE_MIN + " and " + GUESS_RANGE_MAX + ".");

        // For Debug: Print target number
        // System.out.println(number_goal);

        // Repeat until user guesses the correct number
        while (number_goal != number_guess) {
            try_count++;

            // Continue to ask user their input until it is a valid integer.
            boolean is_valid_input = false;
            while (!is_valid_input) {
                System.out.print("Your guess: ");
                try {
                    number_guess = scanner.nextInt();
                    // If catch statement not triggered, input should be valid
                    is_valid_input = true;
                } catch (InputMismatchException e) {
                    // User did not provide an integer
                    System.out.println("Please provide an integer.");
                } finally {
                    // Clear the console input so an integer can be consumed in the following iteration
                    // Consumes the "\n" left by Scanner.nextInt()
                    scanner.nextLine();
                }
            }

            if (number_guess > number_goal) {
                // User guess it too high
                System.out.println("Guess lower.");
            } else if (number_guess < number_goal) {
                // User guess is too low
                System.out.println("Guess higher.");
            } else {
                // User guess is correct
                // Notifiy user of victory
                if (try_count == 1) {
                    // If user won on first attempt
                    System.out.println("You got it right in 1 guess!");
                } else {
                    // If user won in more than one guess
                    System.out.println("You got it right in " + try_count + " guesses!");
                }
            }
        }

        return try_count;
    }

    public static boolean userPlayAgain() {
        /*
         * Check if the user wants to play another round.
         * Returns true if yes, false if no.
         */

        boolean valid_response = false;
        boolean user_response = false;

        System.out.print("Would you like to play again? ");

        while (!valid_response) {
            String input = scanner.next().toLowerCase();

            /*
            // Using if-else statement to 
            if (in.charAt(0) == 'y') {
                // Check if the first character in response is 'y'
                // User wants to play again
                valid_response = true;
                user_response = true;
            } else if (in.charAt(0) == 'n') {
                // Check if the first chaacter in response is 'n'
                // User does not want to play again
                valid_response = true;
                user_response = false;
            } else {
                // An invalid response was submitted, ask user to retry
                System.out.println("That was an invalid response. Please respond with \"yes\" or \"no\"");
                // valid_response stays false so loop continues
            }
             */

            // Check the first character of the user input
            switch (input.charAt(0)) {
                case 'y':
                    // Check if the first character in response is 'y'
                    // User wants to play again
                    valid_response = true;
                    user_response = true;
                    break;
                case 'n':
                    // Check if the first chaacter in response is 'n'
                    // User does not want to play again
                    valid_response = true;
                    user_response = false;
                    break;
                default:
                    // An invalid response was submitted, ask user to retry
                    System.out.println("That was an invalid response. Please respond with \"yes\" or \"no\"");
                    // Consume the console input
                    scanner.nextLine();
                    // valid_response stays false so loop continues
                    break;
            }
        }

        return user_response;
    }

    public static void reportOverallResults(int total_games_played, int total_guesses_made, int smallest_guess_count) {
        /*
         * Reports the game statistics to the user
         * Things to reports:
         *      The total number of games played
         *      The total number of guesses (all games included)
         *      The average number of guess per game
         *      The fewest number of guesses for a game
         */

        // Calculate the average number of guesses per game
        double average_guesses_made = (double) total_guesses_made / total_games_played;

        // Print results to console
        System.out.println("\nGame Results:");
        System.out.println("\tGames Played: " + total_games_played);
        System.out.println("\tGuesses Made: " + total_guesses_made);
        System.out.println("\tAverage Guesses per Game: " + average_guesses_made);
        System.out.println("\tSmallest Guess Count: " + smallest_guess_count);
    }

}
