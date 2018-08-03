package me.vivh.socialtravelapp.model;

import android.graphics.Bitmap;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

@ParseClassName("Trophy")
public class Trophy extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";


    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }
    public void setBitmap(Bitmap bitmap) {
        ParseFile photoFile = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);// can use something 70 in case u want to compress the image

        byte[] scaledData = bos.toByteArray();

        // Save the scaled image to Parse
        photoFile = new ParseFile("image_to_be_saved.jpg", scaledData);
        photoFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                if (e == null) {
                    //filed saved successfully :)
                } else {
                    e.printStackTrace();
                }
            }
        });
        put(KEY_IMAGE, photoFile);
    }

    public String getName() { return getString(KEY_NAME); }
    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public static class Query extends ParseQuery<Trophy> {
        public Query() {
            super(Trophy.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withName() {
            include(KEY_NAME);
            return this;
        }

    }

}


