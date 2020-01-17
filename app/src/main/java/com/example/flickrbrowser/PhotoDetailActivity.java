package com.example.flickrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import com.squareup.picasso.Picasso;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolBar(true);
        Intent intent = getIntent();
        Photo photo = (Photo)intent.getSerializableExtra(PHOTO_TRANSFER);//get the photo which was put in PHOTO_TRANSFER( to show it big )
        if(photo!=null)
        {
            TextView photo_title = findViewById(R.id.photo_title);
            Resources resources = getResources();//using the Strings file
            String text = resources.getString(R.string.photo_title_text, photo.getMemberTitle());
            photo_title.setText(text);
            TextView photo_tags = findViewById(R.id.photo_tags);
            photo_tags.setText(resources.getString(R.string.photo_tags_text, photo.getMemberTags()));
            TextView photo_author = findViewById(R.id.photo_author);
            photo_author.setText(photo.getMemberAuthor());
            ImageView photo_image = findViewById(R.id.photo_image);//loading that image via Picasso
            Picasso.get().load(photo.getMemberLink())
                    .error(R.drawable.place_holder)
                    .placeholder(R.drawable.place_holder)
                    .into(photo_image);//setting that image
        }
    }

}
