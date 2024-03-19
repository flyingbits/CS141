/*
Jamison Schmidt
1/9/2024 - 1/19/2024
Lab 2 - Card Shuffler and Dealer

I apologize for the long file
Please note that most of this code was written before the announcement
regarding the lower threshold for recieving 100% on functionality
This may also have been in the instructions but I like to stay on the "safe side"
It was easier for me to leave most of my code as is rathan than further simplify it

This project contains three classes set up to play a game of cards
There is no functionality to play a full game but there are some other cool things you can do like:
* Generate a deck of 52 cards
* Shuffle a deck of cards with the Fisher-Yates shuffling algorithm
* Deal the cards to multiple decks/player hands
* Analyze play hands for scoring opportunities
* Print cards names to the console

This program implements the following exercises from the textbook:
* 7.16: - determine whether a hand contains...
  * a - a pair
  * b - two pairs
  * c - three of a kind
  * d - four of a kind
  * e - a flush
  * g - a full house
* 7.20 - Use enumerators to represent card face and suit values
* 7.21 - Implement the Fisher-Yates shuffling algorithm

What this project is currently set up to do is to demonstrate its capabilities
It starts with creating 10 player decks and dealing 5 cards to each
Then it will organize each player deck and analyze them
The player decks and results from their respective analysis are printed to the console

This project work by having a CardDealer class that represents the dealer
It hosts the CardDeck objects for individual players
and takes care of the shuffling and dealing

There is a CardDeck class that acts as a hand for a user/player
Has operations to organize the deck by face and analyze the deck for scoring opportunities

The Card class represents a singular card
It has a face and suit value
It can return the card name when called with .toString()
 */

import java.util.Random;

enum CardSuit {
    Hearts,
    Diamonds,
    Spades,
    Clubs,
}

enum CardFace {
    Ace, // Note that Ace is considered low
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Ten,
    Jack,
    Queen,
    King,
}

enum CardCombination {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIRS,
    THREE_OF_A_KIND,
    STRAIGHT,        // Not used
    FLUSH,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    STRAIGHT_FLUSH,  // Not used
    ROYAL_FLUSH,     // Not used
}

class CardDealer {
    /*
     * This the main class for the file and program
     * This instantiates and shuffles the card deck which the game uses
     * and instantiates individual players and deals them their cards
     */

    public static Card[] defaultDeck;

    // Creates ten player decks at the very start
    private static CardDeck[] players = new CardDeck[10];

    public static void main(String[] args) {
        /*
         * 
         */

        // Give the new players decks
        for (int i = 0; i < players.length; i++) {
            players[i] = new CardDeck();
        }

        // Create a deck of 52 cards
        createDefaultDeck();
        // Make a copy of the new deck for the game deck
        Card[] gameCards = getGameCards();

        // Shuffle the game cards
        shuffleCards(gameCards);
        // Deal the game cards to the players (deal 5 cards to each)
        dealCards(gameCards, players, 5);

        // Iterate through every player
        for (CardDeck player : players) {
            // List the player's cards to the console
            System.out.println(player.ListCards());
            // Look for any point worthy combinations and print them to the console
            System.out.println(player.EvaluateDeck());
            System.out.println();
        }
        
    }

    public static void createDefaultDeck() {
        /*
         * Creates an ordinary deck of cards with 4 suits,
         * each with 13 face cards, for a total of 52 cards
         */
        defaultDeck = new Card[CardSuit.values().length * CardFace.values().length];
        int index = 0;
        for (CardSuit suit : CardSuit.values()) {
            // Iterate through each card suit
            for (CardFace face : CardFace.values()) {
                // Iterate through each card face
                defaultDeck[index] = new Card(suit, face);
                index++;
            }
        }
    }

    public static Card[] getGameCards() {
        /*
         * Creates a copy of the default deck
         */
        return defaultDeck.clone();
    }

    public static void shuffleCards(Card[] cards) {
        /*
         * This shuffles the given array of cards with the Fisher-Yates sorting algorithm
         * No deck needs to be returned because it modifies the array through its reference
         */

        /*
        // Psuedocode of the Fisher-Yates modern shuffling algorithm
        // (https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle)
        // To shuffle an array a of n elements (indices 0..n-1):
            for i from 0 to n−2 do
                j ← random integer such that i ≤ j < n
                exchange a[i] and a[j]
         */

        // Create a random number generator
        Random random = new Random();

        // Iterate through each card in the deck
        for (int i = 0; i < cards.length - 1; i++) {
            // Pick a random card to swap
            int j = random.nextInt(cards.length - i) + i;
            // Swap cards with current index
            Card swap = cards[j];
            cards[j] = cards[i];
            cards[i] = swap;
        }
    }

    public static void dealCards(Card[] cards, CardDeck[] decks, int deckMaxCount) {
        /*
         * Deal the given cards to the give CardDecks/player's hands
         * Assumes that the deck is shuffled
         * 
         * @param cards is the deck of cards to be dealt
         * @param decks is the hands of players
         * @param deckMaxCount is the number of cards dealt to each player
         * 
         * @returns void becuase operations done through reference
         */

        // Throw exception if there are not enough cards to be dealt
        if (cards.length < decks.length * deckMaxCount) {
            System.out.println("Expected " + decks.length * deckMaxCount +
                " cards, got " + cards.length + " . ");
            //throw new NotEnoughCardsException("");
        }

        int deckIndex = 0;
        // Iterate through every given deck
        for (CardDeck deck : decks) {
            Card[] newDeck = new Card[deckMaxCount];

            // Get the cards for the deck
            for (int i = 0; i < deckMaxCount; i++) {
                newDeck[i] = cards[i * decks.length + deckIndex];
            }

            // Give the cards to the deck
            deck.SetCards(newDeck.clone());
            deckIndex ++;
        }
    }

    
} // End Class CardDealer

class CardDeck {
    /*
     * Represents a deck of cards, which can be the hand for a player
     * It stores its cards in array, and can apply operations to them
     */

    private Card[] cards;

    public CardDeck() {
        // In case the game wants to create a deck with no initial cards
        ;
    }

    public CardDeck(Card[] cards) {
        this.cards = cards;
        OrganizeDeck();
    }

    public void AddCard(Card newCard) {
        /*
         * Future interest if this project is pursued
         * The ability to update the array to contain an additional card
         * I decided not to develop this because of ineffeciencies with the current architecture
         */
        ;
    }

    public void SetCards(Card[] newCards) {
        /*
         * Sets the cards in deck to the given array of cards.
         */
        cards = newCards;
        OrganizeDeck();
    }

    public void OrganizeDeck() {
        /* 
         * Organizes the deck by face card
         * Uses an insertion sort algorithm
         */

        int i = 1;
        while (i < cards.length) {
            int j = i;
            while (j > 0 && cards[j - 1].getFaceIndex() > cards[j].getFaceIndex()) {
                Card swap = cards[j];
                cards[j] = cards[j - 1];
                cards[j - 1] = swap;
                j--;
            }
            i++;
        }
    }

    public String ListCards() {
        /*
         * Returns a string of all the cards in deck
         * Used for better readability
         */
        String ret = "";
        
        for (Card card : cards) {
            ret += card.toString() + ", ";
        }

        return ret;
    }

    public String ListCardsSimple() {
        /*
         * Similar to the function ListCards but calls Card.toStringSimple instead of Card.toString
         * Used for concise readability
         */
        String ret = "";

        for (Card card : cards) {
            ret += card.toStringSimple() + ", ";
        }

        return ret;
    }

    public boolean HasPair() {
        /*
         * Checks if the current deck contains a pair
         * @returns true if deck has a pair, otherwise false
         */
        int[] pairs = new int[13];
        boolean hasPair = true;

        for (Card card : cards) {
            int cardIndex = card.getFaceIndex();
            pairs[cardIndex]++;

            if (pairs[cardIndex] >= 2) {
                hasPair = true;
                break;
            }
        }

        return hasPair;
    }

    public CardCombination EvaluateDeck() {
        /*
         * Checks for scoring combonations in the deck
         * Assumes that there is at least 1 card in the deck
         * Looks for pair, two pair, three of a kind, four of a kind, and flushes
         * Does not check for straights of any kind
         * @returns The highest scoring CardCombination that was discovered in the deck
         */

        int[] faceMatches = new int[13]; // Counts the number of cards with matching face values
        int[] suitMatches = new int[4];  // Counts the number of cards with matching suit values

        for (Card card : cards) {
            faceMatches[card.getFaceIndex()]++;
            suitMatches[card.getSuitIndex()]++;
        }

        int pairCount = 0;
        int tripCount = 0;
        int quadCount = 0;

        boolean hasFlush = false;
        
        // Iterate through each counter in faceMatches
        for (int faceMatchCount : faceMatches) {
            switch (faceMatchCount) {
                case 0:
                case 1:
                    // You cannot score points with 0 to 1 cards of a single face
                    break;
                case 2:
                    // Two cards with the same face -> +1 pair
                    pairCount++;
                    break;
                case 3:
                    // Three cards with the same face -> +1 three of a kind
                    tripCount++;
                    break;
                case 4:
                    // Four cards with the same face -> +1 four of a kind
                    quadCount++;
                    break;
                default:
                    // An abnormal deck is likely being used, which is not yet supported
                    break;
            }
        }

        // Iterate through each counter in suitMatches
        for (int suitMatchCount : suitMatches) {
            if (suitMatchCount >= 5) {
                // If there are at least 5 cards with the same suit, the deck contains a flush
                // I use "at least" in case there is an abnormal deck is being used
                hasFlush = true;
            }
        }

        // Check combinations from highest scoring to lowest scoring
        if (hasFlush) {
            return CardCombination.FLUSH;
        } else if (quadCount == 1) {
            return CardCombination.FOUR_OF_A_KIND;
        } else if (tripCount == 1) {
            return (pairCount >= 1) ? CardCombination.FULL_HOUSE : CardCombination.THREE_OF_A_KIND;
        } else if (pairCount == 2) {
            return CardCombination.TWO_PAIRS;
        } else if (pairCount == 1) {
            return CardCombination.ONE_PAIR;
        } else {
            return CardCombination.HIGH_CARD;
        }
    }

} // End Class CardDeck

class Card {
    /*
     * Represents a card with a face value and a suit
     * Has functions to get the values of the card
     */

    private final CardSuit suit;
    private final CardFace face;
    

    public Card(CardSuit suit, CardFace face) {
        this.suit = suit;
        this.face = face;
    }

    public CardSuit getSuit() {
        /*
         * Getter method for the suit value of the card
         */
        return suit;
    }

    public int getSuitIndex() {
        /*
         * Converts the enumerator value for the card face to an integer identifyer
         * I did this because I was unsure if I was allowed to use maps/dicts
         * 
         * @return The integer identifyer for the card face
         * @throws InvalidCardException - if the supplied face value does not have a registered id
         */
        int suitID = -1;
        switch (suit) {
            case Hearts: suitID = 0; break;
            case Diamonds: suitID = 1; break;
            case Spades: suitID = 2; break;
            case Clubs: suitID = 3; break;
            default: break; // leave suitID as -1
            // throw new InvalidCardException("Got uncaught suit enum value.");
        }
        return suitID;
    }

    public CardFace getFace() {
        /*
         * Getting method for the face value of the card
         */
        return face;
    }

    public int getFaceIndex() {
        /*
         * Converts the enumerator value for the card face to an integer identifyer
         * I did this because I was unsure if I was allowed to use maps/dicts
         * 
         * @return The integer identifyer for the card face
         * @throws InvalidCardException - if the supplied face value does not have a registered id
         */
        int faceID = -1;
        switch (face) {
            case Ace: faceID = 0; break;  // Note that Ace is considered low
            case Two: faceID = 1; break;
            case Three: faceID = 2; break;
            case Four: faceID = 3; break;
            case Five: faceID = 4; break;
            case Six: faceID = 5; break;
            case Seven: faceID = 6; break;
            case Eight: faceID = 7; break;
            case Nine: faceID = 8; break;
            case Ten: faceID = 9; break;
            case Jack: faceID = 10; break;
            case Queen: faceID = 11; break;
            case King: faceID = 12; break;
            default: break; // leave faceId as -1
            // throw new InvalidCardException("Got uncaught face enum value.");
        };
        return faceID;
    }

    public String toString() {
        /*
         * Returns the name of the card as a string
         * Used for better readability
         */
        return face.toString() + " of " + suit.toString();
    }

    public String toStringSimple() {
        /*
         * Similar to the toString function but instead simplifies the name of the card
         * Used for concise readability
         */
        return "" + suit.toString().charAt(0) + face.toString().charAt(0);
    }

} // End Class Card

class NotEnoughCardsException extends Exception {
    /*
     * Called when the game wants to deal a certain number of cards
     * to the players but there is not enough for everyone
     */
    
    public NotEnoughCardsException(String s) {
        super(s);
        //String msg = "Expected " + expected + " cards, got " + provided + " . " + s;
        //super(msg);
    }

} // End Class NotEnoughCardsException

class InvalidCardException extends Exception {
    /*
     * Called when a card without has a Suit or Face value that has not been mapped to an integer
     */

    public InvalidCardException(String s) {
        super("Card had an unrecognized face or suit. " + s);
    }

} // End Class InvalidCardException