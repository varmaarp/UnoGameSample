package com.example.arpit_pc.sampleproject.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.arpit_pc.sampleproject.Activities.R;
import com.example.arpit_pc.sampleproject.DataClasses.Card;
import com.example.arpit_pc.sampleproject.DataClasses.Color;

import java.util.ArrayList;

public class CardAdapter extends ArrayAdapter<Card> {

    /**
     * Constructor
     */
    public CardAdapter(Context context, ArrayList<Card> cards){
        super(context, 0, cards);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Current card in list
        Card currentCard = getItem(position);

        // Check for existing view being reused, else inflate a new view
        View listViewItem = convertView;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Set textView to show the number of card in list
        TextView cardNumber = listViewItem.findViewById(R.id.card_number);
        cardNumber.setText(Integer.toString(currentCard.getCardNumber()));

        // Set background to show the color of card in list
        if (currentCard.getCardColor() == Color.BLUE){
            cardNumber.setBackgroundColor(android.graphics.Color.BLUE);
        }
        else if (currentCard.getCardColor() == Color.RED){
            cardNumber.setBackgroundColor(android.graphics.Color.RED);
        }
        else if (currentCard.getCardColor() == Color.GREEN){
            cardNumber.setBackgroundColor(android.graphics.Color.GREEN);
        }
        else if (currentCard.getCardColor() == Color.YELLOW){
            cardNumber.setBackgroundColor(android.graphics.Color.YELLOW);
        }

        return listViewItem;

    }
}
