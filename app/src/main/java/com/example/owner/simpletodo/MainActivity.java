package com.example.owner.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    //First model
    ArrayAdapter<String> itemsAdapter;
    //Wires list to view
    ListView lvItems;
    //Instance of the ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
        //needs three arguments, first is the main activity, second type of item it will wrap, third is the list of items
        lvItems = (ListView) findViewById(R.id.lvItems);
        //Only accepts integer id(but our id is a string), use R class to reference id as a valid conpletion. Cast the ListView at the end
        lvItems.setAdapter(itemsAdapter);
        //wire adapter to listview

        //mock data
        //items.add("First item");
        //items.add("Second item");

        setupListViewListener();
    }
//Add functionality
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        //gives reference to EditText
        String itemText = etNewItem.getText().toString();
        //returns edited string
        itemsAdapter.add(itemText);
        //adds it to the to do list
        etNewItem.setText("");
        writeItems();
        //Clears items so user does not have to
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
        //Lets the user know item was completed
    }
//Remove functionality
    private void setupListViewListener() {
        Log.i("MainActivity", "Setting up listener on list view");
        //Useful for differentiating safety levels
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //May need to switch position in a different index, in this case it matches ArrayList
                Log.i("MainActivity", "Item removed from list:" + position);
                items.remove(position);
                //we are changing underlying list, not the adapter
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
                //This method provides result on whether return consumed longclick, it is true because we are consuming it
            }
        });
    }

    private File getDataFile() {
    //Allows access to the store model
        return new File(getFilesDir(), "todo.txt");
        //file associated with app
    }
//This gives us file reading persistence, we can save items when app stops running
    private void readItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            //reads line one at a time
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>();
            //initializes items to new array, no nunpointer
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }
    }
}
