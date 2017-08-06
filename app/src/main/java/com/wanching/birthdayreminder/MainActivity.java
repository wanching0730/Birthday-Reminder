package com.wanching.birthdayreminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    public static final String EXTRA_ID = "com.wanching.birthdayreminder.Birthdat.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddBirthdayActivity.class);
                if(intent.resolveActivity(getPackageManager()) != null)
                    startActivity(intent);

            }
        });

        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, ViewBirthdayActivity.class);
                intent.putExtra(EXTRA_ID, cursor.getLong(cursor.getColumnIndex(BirthdayContract.BirthdayEntry._ID)));
                startActivity(intent);}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        BirthdayDbQueries dbq = new BirthdayDbQueries(new BirthdayDbHelper(getApplicationContext()));

        String [] columns = {
                BirthdayContract.BirthdayEntry._ID,
                BirthdayContract.BirthdayEntry.COLUMN_NAME_NAME,
                BirthdayContract.BirthdayEntry.COLUMN_NAME_EMAIL,
                BirthdayContract.BirthdayEntry.COLUMN_NAME_PHONE,
                BirthdayContract.BirthdayEntry.COLUMN_NAME_IMAGE,
                BirthdayContract.BirthdayEntry.COLUMN_NAME_DATE,
                BirthdayContract.BirthdayEntry.COLUMN_NAME_NOTIFY};

        Cursor cursor = dbq.read(columns, null, null, null, null, BirthdayContract.BirthdayEntry.COLUMN_NAME_DATE + " ASC");

        BirthdayCursorAdapter adapter = new BirthdayCursorAdapter(this, cursor,0);
        listView.setAdapter(adapter);
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
        if (id == R.id.action_delete_all) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder .setCancelable(false)
                    .setMessage("Are you sure you want to delete all records?")
                    .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            BirthdayDbQueries dbq = new BirthdayDbQueries(new BirthdayDbHelper(getApplicationContext()));
                            dbq.deleteAll();
                            Toast.makeText(getApplicationContext(), "Deleted All ", Toast.LENGTH_SHORT).show();
                            onResume();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("WARNING");
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
