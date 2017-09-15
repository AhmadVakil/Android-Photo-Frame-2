package com.example.ahmadrezapc.photoframe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class NewActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.new_activity);

        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this));



       /* gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(NewActivity.this, DetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });*/
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
           // Toast.makeText(NewActivity.this, "aaaa" +position,Toast.LENGTH_SHORT).show();

               /* if(position==0){
                    Intent iinent= new Intent(NewActivity.this,FrameView.class);
                    startActivity(iinent);
                }*/

                // Launch ViewImage.java using intent
                Intent i = new Intent(NewActivity.this, FrameView.class);

                // Show the item position using toast
                Toast.makeText(NewActivity.this, "Position " + position,
                        Toast.LENGTH_SHORT).show();

                // Send captured position to ViewImage.java
                i.putExtra("id", position);

                // Start ViewImage.java
                startActivity(i);
        }

    });

}
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_grid_view, menu);
        return true;
    }
*/


}