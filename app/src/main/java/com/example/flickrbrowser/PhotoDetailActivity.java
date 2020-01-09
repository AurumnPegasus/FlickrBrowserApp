package com.example.flickrbrowser;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolBar(true);
        Intent intent = getIntent();
        Photo photo = (Photo)intent.getSerializableExtra(PHOTO_TRANSFER);
        if(photo!=null)
        {
            TextView photo_title = findViewById(R.id.photo_title);
            photo_title.setText(photo.getMemberTitle());
            TextView photo_tags = findViewById(R.id.photo_tags);
            photo_tags.setText("Tags " + photo.getMemberTags());
            TextView photo_author = findViewById(R.id.photo_author);
            photo_author.setText(photo.getMemberAuthor());
            ImageView photo_image = findViewById(R.id.photo_image);
            Picasso.get().load(photo.getMemberLink())
                    .error(R.drawable.place_holder)
                    .placeholder(R.drawable.place_holder)
                    .into(photo_image);
        }
    }

}
