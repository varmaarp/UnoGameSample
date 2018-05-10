package com.example.arpit_pc.sampleproject.DataClasses;

public class Card {

    public Card(){
    }

    /**
     * Constructor to initialize a new card with number and color
     */
    public Card(int cardNumber, Color color){
        this.cardNumber = cardNumber;
        this.cardColor = color;
    }

    // variable to hold card number
    private int cardNumber;

    // variable to hold card color
    private Color cardColor;

    // getter method for card color
    public Color getCardColor(){
        return cardColor;
    }

    // getter method for card number
    public int getCardNumber(){
        return cardNumber;
    }
}
