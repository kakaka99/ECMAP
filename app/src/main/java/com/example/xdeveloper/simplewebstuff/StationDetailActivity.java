package com.example.xdeveloper.simplewebstuff;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class StationDetailActivity extends AppCompatActivity {

    ArrayList<Comment> comment_list = new ArrayList<>();
    private CommentRecyclerViewAdapter rvAdapter;
    private RecyclerView recyclerView;
    private int mColumnCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getBaseContext()));

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name_en = intent.getStringExtra("name_en");
        String name_tc = intent.getStringExtra("name_tc");
        String image = intent.getStringExtra("image");
        String packing_no = intent.getStringExtra("packing_no");
        String type = intent.getStringExtra("type");
        String provider = intent.getStringExtra("provider");
        String address = intent.getStringExtra("address_tc") + " " + intent.getStringExtra("address_en");
        final int id_number = Integer.parseInt(id);

        volleyJson_get_comment(id_number);

        ImageView detail_image = (ImageView) findViewById(R.id.detail_image);
        TextView name_text = (TextView) findViewById(R.id.detail_name);
        TextView name_packing_no = (TextView) findViewById(R.id.packing_no);
        TextView detail_type = (TextView) findViewById(R.id.detail_type);
        TextView detail_provider = (TextView) findViewById(R.id.detail_provider);
        TextView detail_address = (TextView) findViewById(R.id.detail_address);


        Button submit = (Button) findViewById(R.id.comment_submit_button);

        Spinner spinner = (Spinner) findViewById(R.id.mark_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mark_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        new DownloadImageTask(detail_image).execute(image);

        name_text.setText(name_tc + " " + name_en);
        name_packing_no.setText(packing_no);
        detail_type.setText(type);
        detail_provider.setText(provider);
        detail_address.setText(address);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText cm = (EditText) findViewById(R.id.edittext_cm);
                Spinner spinner = (Spinner) findViewById(R.id.mark_spinner);

                String mark_value = (String) spinner.getSelectedItem();
                String comment_value = cm.getText().toString();

                String mark = "0";


                switch (mark_value) {
                    case "1/5 Mark":
                        mark = "1";
                        break;
                    case "2/5 Mark":
                        mark = "2";
                        break;
                    case "3/5 Mark":
                        mark = "3";
                        break;
                    case "4/5 Mark":
                        mark = "4";
                        break;
                    case "5/5 Mark":
                        mark = "5";
                        break;
                }

                System.out.println(mark);
                System.out.println(comment_value);
                System.out.println();


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.0.102/ecmap/api/insert_comment/";

                JSONObject jsonBody = new JSONObject();

                try {
                    jsonBody.put("comment" , comment_value);
                    jsonBody.put("mark" , mark);
                    jsonBody.put("station_id" , String.valueOf(id_number));
                } catch (JSONException e) {
                    e.printStackTrace();
                }





        //        System.out.println(jsonBody);
                System.out.println();
         //       System.out.println(url);




                JsonObjectRequest stringRequest = new JsonObjectRequest( url,  jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getApplicationContext(), "Comment Successfully", Toast.LENGTH_LONG).show();

                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                        Toast.makeText(getApplicationContext(), "!!!! THE Request HAS ERROR : " + error, Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(stringRequest);

                //id_number

            }
        });

    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    private void volleyJson_get_comment(int id) {

        String url = "http://192.168.0.102/ecmap/api/get_comment_by_station_id/" + id;

        // String url = "http://12.0.2.2/ecmap/api/get_station_json";

        // Request a string response
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            System.out.print("length:" + response.length());

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject event = (JSONObject) response.get(i);
                                Comment cm = new Comment(event.getString("comment"), event.getString("mark"), event.getString("created_at"));
                                Log.w("ItemFragment", "the station list size  :  " + comment_list.size());
                                comment_list.add(cm);

                            }

                            System.out.println(comment_list.size());

                            rvAdapter = new CommentRecyclerViewAdapter(comment_list);
                            Log.i("item_fragment", "this rvadapter is " + comment_list);
                            recyclerView.setAdapter(rvAdapter);
                            recyclerView.invalidate();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        System.out.println("Something went wrong!");
                        Toast.makeText(getApplicationContext(), "!!!! THE JSON HAS ERROR", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        // Add the request to the RequestQueue.
        Volley.newRequestQueue(this.getApplicationContext()).add(req);
    }


}
