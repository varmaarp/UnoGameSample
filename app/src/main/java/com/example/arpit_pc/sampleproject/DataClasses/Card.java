package com.example.arpit_pc.sampleproject.DataClasses;

public class Card {

    public Card(){
    }

    public Card(int cardNumber, Color color){
        this.cardNumber = cardNumber;
        this.cardColor = color;
    }

    private int cardNumber;

    private Color cardColor;

    public Color getCardColor(){
        return cardColor;
    }

    public int getCardNumber(){
        return cardNumber;
    }
}
