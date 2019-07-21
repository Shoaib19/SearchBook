package com.example.shoaib.booklistingapp;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.shoaib.booklistingapp.MainActivity.TAG;

public class parsingJSON {


    public parsingJSON() {
    }

    public static List<book> fetchBookData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {

            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        // Return the list<book>
        Log.d(TAG, "fetchBookData: Before json" + jsonResponse);
        return extractBook(jsonResponse);

    }


    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";


        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            Log.d(TAG, "makeHttpRequest: " + jsonResponse);
            return jsonResponse;
        }


    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.d(TAG, "createUrl: Error creating URl");
        }

        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }

        }
        return output.toString();

    }

    //Return a list of {@link book} objects that has been built up from
    // parsing a JSON response.
    //     * //Here we created a public static method extractBook() which takes no
    //   * input but returns a ArrayList of type book
    private static List<book> extractBook(String url) {


        if (url == null) {
            Log.d(TAG, "extractBook: Url Null he be");
        }
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(url)) {
            Log.d(TAG, "extractBook: textEmpty");
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<book> books = new ArrayList<>();
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {

            String title;
            String description;
            String mImage;
            String author;

            JSONObject jsonObject = new JSONObject(url);


            JSONArray jsonArray = jsonObject.optJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                JSONObject jsonObject2 = jsonObject1.optJSONObject("volumeInfo");

                try {

                    if (jsonObject2.has("title") && jsonObject2.getString("title") != null) {
                        title = jsonObject2.getString("title");
                    } else title = "Not Available";


                    if (jsonObject2.has("title") && jsonObject2.getString("title") != null) {
                        description = jsonObject2.getString("description");
                    } else description = "Not Available";


                    if (jsonObject2.has("imageLinks")) {
                        JSONObject jsonObject3 = jsonObject2.optJSONObject("imageLinks");
                        if (jsonObject3.getString("smallThumbnail") != null) {
                            mImage = jsonObject3.getString("smallThumbnail");
                        } else mImage = null;
                    } else mImage = null;


                    if (jsonObject2.has("authors")) {
                        JSONArray jsonArray1 = jsonObject2.getJSONArray("authors");

                        if (jsonArray1.getString(0) != null) {
                            author = jsonArray1.getString(0);
                        } else author = "Not Available";

                    } else author = "Not Available";


                    Log.d(TAG, "String: Title " + title + "Description: " + description + "mImage : " + mImage + "Author: " + author);
                    books.add(new book(title, author, description, mImage));

                } catch (JSONException e) {
                    Log.d(TAG, "problem parsing " + e + "jsonArray");
                }
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Book JSON results", e);
        }
        return books;
    }


}