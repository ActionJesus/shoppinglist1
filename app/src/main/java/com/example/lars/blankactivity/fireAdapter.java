package com.example.lars.blankactivity;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Lars on 13-12-2016.
 */

public class fireAdapter extends FirebaseListAdapter<Product> {


    private Set<Integer> checked;

    public Set<Integer> getChecked() {
        return checked;
    }

    public fireAdapter(Context context, DatabaseReference dbRef) {
        super((Activity) context, Product.class, android.R.layout.simple_list_item_checked, dbRef);
        this.checked = new HashSet<>();
    }


    @Override
    protected void populateView(View v, final Product model, final int position) {

        TextView txt = (TextView) v.findViewById(android.R.id.text1);
        txt.setText(model.toString());

           }
}
