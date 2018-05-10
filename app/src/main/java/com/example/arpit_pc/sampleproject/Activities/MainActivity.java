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

    public void startNewGame(){
        deck = new Stack<>();
        pile = new Stack<>();
        cardsInHand = new ArrayList<>();
        cardsWithComputer = new ArrayList<>();

        getNewDeck();

        getNewCards(cardsInHand);
        getNewCards(cardsWithComputer);


        pile.push(getTopCardFromDeck(deck));
        displayCardOnPile(pile);

        final CardAdapter cardAdapter = new CardAdapter(this, cardsInHand);

        ListView listView = findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Card c = cardsInHand.get(position);
                System.out.println("item clicked is " + c.getCardNumber() + c.getCardColor());
                if (checkTopPileCard(c)){
                    playCard(c, cardsInHand);
                    cardAdapter.notifyDataSetChanged();

                    if (cardsInHand.size() == 0){
                        endGame("You won!");

                    }else {
                        computersTurn();
                    }
                }
                else{
                    System.out.println("cannot play this card");
                    Toast.makeText(getApplicationContext(), "You cannot play this card!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setAdapter(cardAdapter);

        TextView deckTextView = findViewById(R.id.deck);
        deckTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!deck.empty()) {
                    addCardToHand(cardsInHand);
                    cardAdapter.notifyDataSetChanged();
                    computersTurn();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), GameEnd.class);
                    intent.putExtra("Winner", "No more cards in deck. Game is a draw!");
                    startActivity(intent);
                }
            }
        });
    }

    public void getNewCards(ArrayList<Card> cards){
        for (int i=0; i<7; i++){
            addCardToHand(cards);
        }
    }

    public Card getTopCardFromDeck(Stack<Card> d){
        return d.pop();
    }

    public void addCardToHand(ArrayList<Card> hand){
        hand.add(getTopCardFromDeck(deck));
    }

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

    public void playCard(Card c, ArrayList<Card> hand){
        pile.push(c);
        hand.remove(c);
        displayCardOnPile(pile);
    }

    public boolean checkTopPileCard(Card c){
        Card topCardPile = pile.peek();
        return (c.getCardColor() == topCardPile.getCardColor() ||
                c.getCardNumber() == topCardPile.getCardNumber());
    }

    public void computersTurn(){
        boolean cardPlayed = false;
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

        if (cardsWithComputer.size() == 0){
            endGame("Computer won!");
        }

        if (!cardPlayed){
            if (!deck.empty()) {
                addCardToHand(cardsWithComputer);
                Toast.makeText(getApplicationContext(), "Computer picked a card from deck",
                        Toast.LENGTH_SHORT).show();
                updateComputerCardsMessage();
            }
            else{
                Intent intent = new Intent(getApplicationContext(), GameEnd.class);
                intent.putExtra("Winner", "No more cards in deck. Game is a draw!");
                startActivity(intent);
            }
        }

    }


    public void endGame(String winner){
        System.out.println("The winner is " + winner);
        Intent intent = new Intent(this, GameEnd.class);
        intent.putExtra("Winner", winner);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startNewGame();
    }

    public void updateComputerCardsMessage(){
        TextView opponentCardsTextView = findViewById(R.id.opponent_cards);
        opponentCardsTextView.setText("Cards remaining with opponent: " + cardsWithComputer.size());
    }

    public void displayCardOnPile(Stack<Card> pile){
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

}
