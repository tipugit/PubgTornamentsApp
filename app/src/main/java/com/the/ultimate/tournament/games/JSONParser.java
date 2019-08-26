package com.the.ultimate.tournament.games;

import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

public class JSONParser {

	private static InputStream is = null;
	private static JSONObject jObj = null;
	private static String json = "";

	private Integer status = 0;

	// constructor
	public JSONParser() {

	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public JSONObject makeHttpRequest(String url, String method,
									  Map<String, String> params) {

		//for builing a parameter
		StringBuilder result = new StringBuilder();
		boolean first = true;

		int i = 0;
		for (String key : params.keySet()) {
			try {
				if (i != 0){
					result.append("&");
				}
				result.append(key).append("=")
						.append(URLEncoder.encode(params.get(key), "UTF-8"));

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			i++;
		}

		System.out.println("string"+result.toString());

		
		// Making HTTP request
		try {
			
			// check for request method
			if(Objects.equals(method, "POST")){
				// request method is POST
				// defaultHttpClient
				URL urlr = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) urlr.openConnection();
				
				conn.setReadTimeout(10000);
				conn.setConnectTimeout(15000);
                /* for Get request */
				conn.setRequestMethod("POST");

				conn.setDoInput(true);

				// You need to set it to true if you want to send (output) a request body,
				//for example with POST or PUT requests. 
				//Sending the request body itself is done via the connection's output stream
				conn.setDoOutput(true);
				
				OutputStream os = conn.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				writer.write(result.toString());
				writer.flush();
				writer.close();
				os.close();
				
                int statusCode = conn.getResponseCode();
                /* 200 represents HTTP OK */
                if (statusCode ==  200) {

                	status = 1; // Successful

                }else{
                	status = 0; //"Failed to fetch data!";
                }

                conn.connect();
				is = conn.getInputStream();
								
			}else if(Objects.equals(method, "GET")){
				// request method is GET
				if (result.length() != 0) {
	                url += "?" + result.toString();
	            }
				
				// request method is GET
				// defaultHttpClient
				URL urlr = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) urlr.openConnection();
				
				conn.setReadTimeout(10000);
				conn.setConnectTimeout(15000);
                /* for Get request */
				conn.setRequestMethod("GET");
				
				// You need to set it to true if you want to send (output) a request body,
				//for example with POST or PUT requests. 
				//Sending the request body itself is done via the connection's output stream
				conn.setDoOutput(true);
				
				conn.connect();
				is = conn.getInputStream();

			}			
			

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}
}
