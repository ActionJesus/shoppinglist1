package com.example.lars.blankactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.id.message;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "lars.blankactivity";
    private ArrayList<Product> productList = new ArrayList<Product>();
    private fireAdapter arrayAdapter;
    private String[] spinnervalues = {"1","2","3","4","5","6","7","8","9"};
    private String spinnerPosition;
    private DatabaseReference firebase;
    private int pos = 0;
    private CoordinatorLayout coordinatorLayout;
    private Product tempProduct;
    private String templiste;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = FirebaseDatabase.getInstance().getReference().child("items");
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);


        final ListView textListView = (ListView) findViewById(R.id.textListView);

        if (savedInstanceState!=null)
        {
            this.productList = savedInstanceState.getParcelableArrayList("savedList");
        }


        arrayAdapter =  new fireAdapter(this,firebase);

        textListView.setAdapter(arrayAdapter);
        fillQuantity();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, spinnervalues);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            //The AdapterView<?> type means that this can be any type,
            //so we can use both AdapterView<String> or any other
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                //So this code is called when ever the spinner is clicked


                spinnerPosition = spinnervalues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO you would normally do something here
                // for instace setting the selected item to "null"
                // or something.
            }
        });

        //This line of code can always be used to get the
        //selected position in in the spinner - the first item
        //will have an index of 0.
        int position = spinner.getSelectedItemPosition();

        //This line will get the actual seleted item -
        //in our case the values in the spinner is simply
        //strings, so we need to make a cast to a String
        String item = (String) spinner.getSelectedItem();

//        getActionBar().setHomeButtonEnabled(true); //this means we can click "home"

        textListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                pos=position;
              //  Object listItem = textListView.getItemAtPosition(position);

              //  int index = textListView.getCheckedItemPosition();
              //  arrayAdapter.getRef(position).setValue(null);
                deleteItem();
                System.out.println("hejhej");


            }
        });

    }

    public void deleteItem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Delete this item?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                //Before deleting, we store it
                arrayAdapter.getRef(pos).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                            String tempc = (String) dataSnapshot.child("comment").getValue();
                            String tempn = (String) dataSnapshot.child("name").getValue();
                        String tempq = (String) dataSnapshot.child("quantity").getValue().toString();

                        int i = Integer.parseInt(tempq);

                        System.out.println(tempc +"   "+tempn+"    "+tempq);
                        tempProduct = new Product(tempn,i,tempc);
                        System.out.println(tempProduct.toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                /// Aaand we delete it
                arrayAdapter.getRef(pos).setValue(null);
                regretDeleteItem();

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void regretDeleteItem(){
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Item has been deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firebase.push().setValue(tempProduct);
                        Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Item is restored!", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                });

        snackbar.show();
    }
    /*public void showToast(View view){
        Context context = getApplicationContext();
        CharSequence text = "Hello Lars!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    public void showToast2(View view){
        Context context = getApplicationContext();
        CharSequence text = "Button 2 pressed!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }*/
    public void fillQuantity(){
        EditText q = (EditText)findViewById(R.id.quantity);
        q.setText("1");
    }


    public void addToList(View view){

        EditText q = (EditText)findViewById(R.id.quantity);
        EditText e = (EditText)findViewById(R.id.addToList);
        EditText c = (EditText)findViewById(R.id.comment);
        Spinner s = (Spinner)findViewById(R.id.spinner1);
        Product p;

        if(e.getText().toString().equals("")){
            Context context = getApplicationContext();
            CharSequence text = "Product cannot be empty";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{


        // If the spinner value has been changed, we will use the spinner. Otherwise we use the quantity input
        if(Integer.parseInt(spinnerPosition)!=1){
            p = new Product(e.getText().toString(), Integer.parseInt(spinnerPosition), c.getText().toString());

        }else{

            p = new Product(e.getText().toString(), Integer.parseInt(q.getText().toString()), c.getText().toString());
        }

        firebase.push().setValue(p);




        arrayAdapter.notifyDataSetChanged();
        // Clear input fields
        fillQuantity();
        e.setText("");
        c.setText("");
        s.setSelection(0);
        }
    }
    public void deleteList(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Do you really want to clear the list?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Context context = getApplicationContext();
                CharSequence text = "The list has been deleted!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                arrayAdapter.cleanup();
                productList.clear();


                firebase.setValue(null);
                ListView textListView = (ListView) findViewById(R.id.textListView);


                arrayAdapter =  new fireAdapter(getParent(),firebase);

                textListView.setAdapter(arrayAdapter);



                //arrayAdapter.clear();
                arrayAdapter.notifyDataSetChanged();

                fillQuantity();

                arrayAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();



    }
    public void settings(){
        Intent intent = new Intent(getBaseContext(), Settings.class);

        startActivityForResult(intent, RESULT_OK);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.hasExtra("myData1")) {
                Toast.makeText(this, data.getExtras().getString("myData1"),
                        Toast.LENGTH_SHORT).show();

                TextView tw = (TextView) findViewById(R.id.teststring);
                tw.setText(tw.getText().toString());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            settings();
            return true;
        }

        if (id == R.id.item_deleteList){
            deleteList();
            return true;
        }
        if (id == R.id.item_share){
            //Get shopping data to share
            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.e("Count " ,""+snapshot.getChildrenCount());
                    String liste = "Shopping liste: ";
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        String tempc = (String) postSnapshot.child("quantity").getValue().toString();
                        String tempn = (String) postSnapshot.child("name").getValue().toString();
                        String tempq = (String) postSnapshot.child("comment").getValue().toString();

                        liste += tempc+" af "+tempn+" "+tempq+"  -  ";
                    }
                    templiste = liste;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

            //Create dialog and send messages
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater=this.getLayoutInflater();
            //this is what I did to added the layout to the alert dialog
            View layout=inflater.inflate(R.layout.dialog,null);
            alert.setView(layout);
            final EditText phonenumber=(EditText)layout.findViewById(R.id.phone);

            alert.setTitle("Enter phonenumber");
            alert.setMessage("Enter phonenumber to share list (SMS):");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value1=phonenumber.getText().toString();
                     SmsManager smsManager = SmsManager.getDefault();
                     smsManager.sendTextMessage(value1, null, templiste, null, null);
                    if(value1.equals(null))
                    {Toast.makeText(getApplicationContext(), "Sent your shoppinglist", Toast.LENGTH_SHORT).show();}
                }
            });

        alert.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    //This method is called before our activity is created
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //ALWAYS CALL THE SUPER METHOD
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
		/* Here we put code now to save the state */
        //outState.putString("savedName", name);
      //  outState.putStringArrayList("savedList", productList);
        outState.putParcelableArrayList("savedList", productList);


    }
    //this is called when our activity is recreated, but
    //AFTER our onCreate method has been called
    //EXTREMELY IMPORTANT DETAIL
    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        //MOST UI elements will automatically store the information
        //if we call the super.onRestoreInstaceState
        //but other data will be lost.
        super.onRestoreInstanceState(savedState);
        Log.i(TAG, "onRestoreInstanceState");
		/*Here we restore any state */
        //TextView savedName = (TextView) findViewById(R.id.name);
        ListView textListView = (ListView) findViewById(R.id.textListView);
        //in the line below, notice key value matches the key from onSaved
        //this is of course EXTREMELY IMPORTANT
        //this.name = savedState.getString("savedName");
        //  this.productList = savedState.getStringArrayList("savedList");
        this.productList = savedState.getParcelableArrayList("savedList");

        //since this method is called AFTER onCreate
        //we need to set the text field
        //try to comment the line below out and
        //see the effect after orientation change (after saving some name)
        //savedName.setText("Saved Name:"+name);


        textListView.setAdapter(arrayAdapter);
    }


}
