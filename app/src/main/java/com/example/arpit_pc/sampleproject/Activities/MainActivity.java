package com.example.arpit_pc.sampleproject.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arpit_pc.sampleproject.Adapters.CardAdapter;
import com.example.arpit_pc.sampleproject.DataClasses.Card;
import com.example.arpit_pc.sampleproject.DataClasses.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Card> cardsInHand;
    private ArrayList<Card> cardsWithComputer;
    private Stack<Card> deck;
    private Stack<Card> pile;

    /**
     * Method to start a new game.
     * Initializes variables required to start a new game.
     * Adds event listeners for onClick events.
     */
    public void startNewGame(){

        // Initialize deck and pile as stack of cards.
        deck = new Stack<>();
        pile = new Stack<>();

        // Initialize arrayList of Cards for user and computer
        cardsInHand = new ArrayList<>();
        cardsWithComputer = new ArrayList<>();

        // Gets a new shuffled deck of 80 cards.
        getNewDeck();

        // Get 7 cards from deck at the beginning of game.
        getNewCards(cardsInHand);
        getNewCards(cardsWithComputer);

        // Put the next card from deck on top of the pile.
        pile.push(getTopCardFromDeck(deck));
        displayCardOnPile();

        // CardAdapter to use with listview
        final CardAdapter cardAdapter = new CardAdapter(this, cardsInHand);

        ListView listView = findViewById(R.id.list);

        // Listener for click on element of list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Card clicked
                Card c = cardsInHand.get(position);

                // Play the selected card if it can be played
                if (checkTopPileCard(c)){
                    playCard(c, cardsInHand);
                    // Notify card adapter that arrayList has changed
                    cardAdapter.notifyDataSetChanged();

                    // Check if player has won
                    if (cardsInHand.size() == 0){
                        endGame("You won!");

                    }else {
                        // If player has not won, its computer's turn to play
                        computersTurn();
                    }
                }
                else{
                    // Show toast message if card cannot be played
                    Toast.makeText(getApplicationContext(), "You cannot play this card!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setAdapter(cardAdapter);

        // Set onClick Listener for click on deck.
        TextView deckTextView = findViewById(R.id.deck);
        deckTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If deck is not empty, pick the top card and put in hand
                if (!deck.empty()) {
                    addCardToHand(cardsInHand);
                    cardAdapter.notifyDataSetChanged();
                    // Computer's turn if user picked card from deck
                    computersTurn();
                }
                else{
                    // If no card remaining in deck, end game as draw.
                    Intent intent = new Intent(getApplicationContext(), GameEnd.class);
                    intent.putExtra("Winner", "No more cards in deck. Game is a draw!");
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Get 7 cards from deck in the beginning of the game.
     */
    public void getNewCards(ArrayList<Card> cards){
        for (int i=0; i<7; i++){
            addCardToHand(cards);
        }
    }

    /**
     * Gets the top card from the deck (can be used with deck or pile).
     */
    public Card getTopCardFromDeck(Stack<Card> d){
        return d.pop();
    }

    /**
     * Takes the top card from deck and places it in hand (user or computer).
     */
    public void addCardToHand(ArrayList<Card> hand){
        hand.add(getTopCardFromDeck(deck));
    }

    /**
     * Generates a new deck of 80 cards. Cards are shuffled before they can be used.
     */
    public Stack<Card> getNewDeck(){
        deck.clear();
        for (int i=0; i <= 9; i++){
            for (Color color : Color.values()){
                deck.push(new Card(i,color));
                deck.push(new Card(i,color));
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    /**
     * Takes card from hand and places it on top of the pile.
     */
    public void playCard(Card c, ArrayList<Card> hand){
        pile.push(c);
        hand.remove(c);
        displayCardOnPile();
    }

    /**
     * Checks if the card can be played or not.
     * Card can be played if either its number matches to the number of top card on the pile
     * Or if the color of card is same as that of top card on the pile.
     */
    public boolean checkTopPileCard(Card c){
        Card topCardPile = pile.peek();
        return (c.getCardColor() == topCardPile.getCardColor() ||
                c.getCardNumber() == topCardPile.getCardNumber());
    }

    /**
     * Actions that the computer can perform during its turn.
     */
    public void computersTurn(){

        boolean cardPlayed = false;

        // If computer has a card that can be played, then pay that card
        for (Card c : cardsWithComputer){
            if (checkTopPileCard(c)){
                playCard(c, cardsWithComputer);
                cardPlayed = true;
                Toast.makeText(getApplicationContext(), "Computer played "+c.getCardColor()+" "+c.getCardNumber(),
                        Toast.LENGTH_SHORT).show();
                updateComputerCardsMessage();
                break;
            }
        }

        // Check if computer has won
        if (cardsWithComputer.size() == 0){
            endGame("Computer won!");
        }

        // If computer did not have any card that could be played, pick a card from the deck.
        if (!cardPlayed){
            // Check for empty deck before picking a card.
            if (!deck.empty()) {
                addCardToHand(cardsWithComputer);
                Toast.makeText(getApplicationContext(), "Computer picked a card from deck",
                        Toast.LENGTH_SHORT).show();
                updateComputerCardsMessage();
            }
            else{ // If deck is empty, end the game as draw.
                Intent intent = new Intent(getApplicationContext(), GameEnd.class);
                intent.putExtra("Winner", "No more cards in deck. Game is a draw!");
                startActivity(intent);
            }
        }

    }

    /**
     * Displays the winner on the GameEnd activity if the game has ended.
     */
    public void endGame(String winner){
        // to start GameEnd activity
        Intent intent = new Intent(this, GameEnd.class);
        intent.putExtra("Winner", winner);
        startActivity(intent);
    }

    /**
     * Updates the textView to show the number of cards remaining with the computer.
     */
    public void updateComputerCardsMessage(){
        TextView opponentCardsTextView = findViewById(R.id.opponent_cards);
        opponentCardsTextView.setText("Cards remaining with opponent: " + cardsWithComputer.size());
    }

    /**
     * Displays the top card from the pile on the screen.
     */
    public void displayCardOnPile(){

        // Look at the top card on pile.
        Card topCard = pile.peek();

        TextView pileTextView = findViewById(R.id.pile);
        pileTextView.setText(Integer.toString(topCard.getCardNumber()));

        if (topCard.getCardColor() == Color.YELLOW){
            pileTextView.setBackgroundColor(android.graphics.Color.YELLOW);
        }
        else if (topCard.getCardColor() == Color.GREEN){
            pileTextView.setBackgroundColor(android.graphics.Color.GREEN);
        }
        else if (topCard.getCardColor() == Color.RED){
            pileTextView.setBackgroundColor(android.graphics.Color.RED);
        }
        else if (topCard.getCardColor() == Color.BLUE){
            pileTextView.setBackgroundColor(android.graphics.Color.BLUE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Starts a new game.
        startNewGame();
    }

}
