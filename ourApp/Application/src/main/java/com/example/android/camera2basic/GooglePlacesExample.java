package com.example.android.camera2basic;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class GooglePlacesExample  {
	ArrayList<GooglePlace> venuesList;

	// the google key

	// ============== YOU SHOULD MAKE NEW KEYS ====================//
	final String GOOGLE_KEY = "AIzaSyDLr6ilTxPtO6dNgA6gxMUeyYCteAIVQqo";

	// we will need to take the latitude and the longitude from a certain point
	private String latitude = "12.970478";
	private String longitude = "80.243502";

	ArrayAdapter<String> myAdapter;

    public GooglePlacesExample() {
//        return new googleplaces().getNearerPlaces();
    }

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
////		super.onCreate(savedInstanceState);
////		setContentView(R.layout.activity_main);
//
//		// start the AsyncTask that makes the call for the venus search.
////		new googleplaces().execute();
//	}

//	private class googleplaces {

		String temp;

//		@Override
//		protected String doInBackground(View... urls){
//
//			// make Call to the url
//			temp = makeCall("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longitude + "&radius=100&sensor=true&key=" + GOOGLE_KEY);
//
//			//print the call in the console
//			System.out.println("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longitude + "&radius=100&sensor=true&key=" + GOOGLE_KEY);
//			return "";
//		}

        public ArrayList<GooglePlace> getNearerPlaces() {
            temp = makeCall("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longitude + "&radius=100&sensor=true&key=" + GOOGLE_KEY);

            //print the call in the console
            System.out.println("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longitude + "&radius=100&sensor=true&key=" + GOOGLE_KEY);

            venuesList = (ArrayList<GooglePlace>) parseGoogleParse(temp);
            return venuesList;

        }

//		@Override
//		protected void onPreExecute() {
//			// we can start a progress bar here
//			//get current location of the device
//			//GetCurrentLocation getCurrentLocation = new GetCurrentLocation(GooglePlacesExample.this);
//			//Location location = getCurrentLocation.getDeviceLocation();
//
//			//latitude = location.getLatitude();
//			//longitude = location.getLongitude();
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (temp == null) {
//				// we have an error to the call
//				// we can also stop the progress bar
//			} else {
//				// all things went right
//
//				// parse Google places search result
//				venuesList = (ArrayList<GooglePlace>) parseGoogleParse(temp);
//
//				List<String> listTitle = new ArrayList<String>();
//
//
//
//				for (int i = 0; i < venuesList.size(); i++) {
//					// make a list of the venus that are loaded in the list.
////                    listTitle.add(i, venuesList.get(i).getName() + "\nOpen Now: " + venuesList.get(i).getOpenNow() + "\n(" + venuesList.get(i).getCategory() + ")"+
////                            "\nLatitude: "+venuesList.get(i).getLatitude()+"\nLongitude: "+venuesList.get(i).getLongitude()+"\nIcon Image: "+venuesList.get(i).getIcon());
//                    listTitle.add(i, venuesList.get(i).getName() + "\nOpen Now: " + venuesList.get(i).getOpenNow() + "\n(" + venuesList.get(i).getCategory() + ")"+
//                            "\nLatitude: "+venuesList.get(i).getLatitude()+"\nLongitude: "+venuesList.get(i).getLongitude()+"\nIcon Image: "+venuesList.get(i).getIcon());
//                    // show the name, the category and the city
//
//				}
//
//
//				// set the results to the list
//				// and show them in the xml
////				myAdapter = new ArrayAdapter<String>(GooglePlacesExample.this, R.layout.row_layout, R.id.listText, listTitle);
////				setListAdapter(myAdapter);
//			}
//		}


	public static String makeCall(String url) {

		// string buffers the url
		StringBuffer buffer_string = new StringBuffer(url);
		String replyString = "";

		// instanciate an HttpClient
		HttpClient httpclient = new DefaultHttpClient();
		// instanciate an HttpGet
		HttpGet httpget = new HttpGet(buffer_string.toString());

		try {
			// get the responce of the httpclient execution of the url
			HttpResponse response = httpclient.execute(httpget);
			InputStream is = response.getEntity().getContent();

			// buffer input stream the result
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			// the result as a string is ready for parsing
			replyString = new String(baf.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(replyString);

		// trim the whitespaces
		return replyString.trim();
	}

	private static ArrayList<GooglePlace> parseGoogleParse(final String response) {

		ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();
		try {

			// make an jsonObject in order to parse the response
			JSONObject jsonObject = new JSONObject(response);

			// make an jsonObject in order to parse the response
			if (jsonObject.has("results")) {

				JSONArray jsonArray = jsonObject.getJSONArray("results");

				for (int i = 0; i < jsonArray.length(); i++) {
					GooglePlace poi = new GooglePlace();
					if (jsonArray.getJSONObject(i).has("name")) {
						poi.setName(jsonArray.getJSONObject(i).optString("name"));
						poi.setRating(jsonArray.getJSONObject(i).optString("rating"));
                        poi.setLatitude(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").
								optString("lat"));
						poi.setLongitude(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").
								optString("lng"));
						poi.setIcon(jsonArray.getJSONObject(i).optString("icon"));

						if (jsonArray.getJSONObject(i).has("opening_hours")) {
							if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
								if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
									poi.setOpenNow("YES");
								} else {
									poi.setOpenNow("NO");
								}
							}
						} else {
							poi.setOpenNow("Not Known");
						}
						if (jsonArray.getJSONObject(i).has("types")) {
							JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

							for (int j = 0; j < typesArray.length(); j++) {
								poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
							}
						}
					}
					temp.add(poi);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<GooglePlace>();
		}
		return temp;

	}
}
