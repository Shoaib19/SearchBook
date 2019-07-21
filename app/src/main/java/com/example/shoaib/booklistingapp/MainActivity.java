package com.example.shoaib.booklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = MainActivity.class.getName();

    private ListView listView;
    private String string;
    private ProgressBar progressBar;
    private TextView textView;


    private String GOOGLE_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);


        //This something i want to Add in future i.e. if we click on the particular listView item
        //then the detailed version must be Opened with back button to navigate.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    //Here we are inflating the menu resource xml and adding it to our layout.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Inflating the menu resource file.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        //Getting the search icon in the menu For actually doing the work.
        MenuItem searchViewItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchViewItem.getActionView();


        //This search the results typed by the user and perform appropriate results.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                //Checking that the search must contains atleast two alphabets.
                if (newText.length() > 1) {
                    //Adding the search result to the url
                    string = GOOGLE_API_URL + newText + "&maxResults=11";

                    if (isNetworkAvailable(getApplicationContext())) {


                        textView.setVisibility(View.GONE);

                        progressBar.setVisibility(View.VISIBLE);

                        fetchData data = new fetchData(string, MainActivity.this);
                        data.execute();
                    } else {
                        textView.setText("Check Your Internet Connection");
                    }

                }
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    public class fetchData extends AsyncTask<Void, Void, List<book>> {

        String mUrl;
        Context context;

        fetchData(String mUrl, Context context) {
            this.mUrl = mUrl;
            this.context = context;
        }


        @Override
        protected List<book> doInBackground(Void... voids) {
            if (mUrl == null) {
                Log.d(TAG, "doInBackground: Url is empty");
            }
            List<book> books = parsingJSON.fetchBookData(mUrl);
            Log.d(TAG, "doInBackground: ke adar" + books);

            return books;
        }

        @Override
        protected void onPostExecute(List<book> books) {

            bookAdapter bookAdapter = new bookAdapter(MainActivity.this, R.layout.list_view, books);

            listView.setAdapter(bookAdapter);
            progressBar.setVisibility(View.GONE);

        }


    }


}
