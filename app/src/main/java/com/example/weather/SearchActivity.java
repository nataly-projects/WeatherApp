package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


public class SearchActivity extends AppCompatActivity {

    private EditText cityText;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        cityText = findViewById(R.id.edit_text);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cityToSearch = cityText.getText().toString().trim();

                Intent replyIntent = new Intent();
                replyIntent.putExtra("city_to_search", cityToSearch);
                setResult(RESULT_OK, replyIntent);
                finish();

            }
        });
    }

}
