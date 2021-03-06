package me.vivh.socialtravelapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo";
    File photoFile;
    ParseFile parseFile;
    private boolean tookPhoto;
    private EditProfileFragment.OnFragmentInteractionListener mListener;
    ParseUser currentUser;

    private Unbinder unbinder;
    @BindView(R.id.btnCamera) Button cameraBtn;
    @BindView(R.id.ivProfilePicPreview) ImageView ivPreview;
    @BindView(R.id.btnSave) Button saveBtn;
    @BindView(R.id.pbLoading) ProgressBar pb;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etDisplayName) EditText etDisplayName;
    @BindView(R.id.etHomeLoc) EditText etHomeLoc;
    @BindView(R.id.etBio) EditText etBio;

    public interface OnFragmentInteractionListener {
        void onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_edit_profile,
                container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        unbinder = ButterKnife.bind(this, rootView);
        tookPhoto = false;
        currentUser = ParseUser.getCurrentUser();

        // load existing username and profile pic
        final ParseUser currentUser = ParseUser.getCurrentUser();
        tvUsername.setText(currentUser.getUsername());
        String profilePicUrl = "";
        if (currentUser.getParseFile("profilePic") != null) {
            profilePicUrl = currentUser.getParseFile("profilePic").getUrl();
        }
        Glide.with(getContext()).load(profilePicUrl)
                .apply(new RequestOptions().placeholder(R.drawable.user_outline_24))
                .apply(RequestOptions.circleCropTransform())
                .into(ivPreview);

        setCurrentInfo();

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onLaunchCamera(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final ParseUser currUser = ParseUser.getCurrentUser();
                    // TODO - redirect to profile fragment after, even if photo not changed
                    if (etUsername.getText().toString() != null && !etUsername.getText().toString().isEmpty()) {
                        currentUser.put("username",etUsername.getText().toString());
                    }
                    if (etDisplayName.getText().toString() != null && !etDisplayName.getText().toString().isEmpty()) {
                        currentUser.put("displayName",etDisplayName.getText().toString());
                    }
                    if (etHomeLoc.getText().toString() != null && !etHomeLoc.getText().toString().isEmpty()) {
                        currentUser.put("home",etHomeLoc.getText().toString());
                    }
                    if (etBio.getText().toString() != null && !etBio.getText().toString().isEmpty()) {
                        currentUser.put("bio",etBio.getText().toString());
                    }
                    if (tookPhoto){
                        // save photo
                        final File file = photoFile;
                        parseFile = new ParseFile(file);
                        postPhoto(parseFile, currUser);
                    } else {
                        // TODO - redirect in a better way
                        // replace existing fragment with profile fragment inside the frame
                        ViewPager vp= (ViewPager) getActivity().findViewById(R.id.viewPager);
                        vp.setCurrentItem(3, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Please take a new picture first", Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }

    public void setCurrentInfo(){
        etUsername.setText(currentUser.getUsername());
        etDisplayName.setText(currentUser.getString("displayName"));
        etHomeLoc.setText(currentUser.getString("home"));
        etBio.setText(currentUser.getString("bio"));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditProfileFragment.OnFragmentInteractionListener) {
            mListener = (EditProfileFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void onLaunchCamera(View view) throws IOException {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName + ".jpg");

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "me.vivh.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // RESIZE BITMAP
                // Create a File reference to access to future access
                photoFile = getPhotoFileUri(photoFileName + ".jpg");
                // rotate bitmap orientation
                Bitmap takenImage = rotateBitmapOrientation(photoFile.getPath());
                // by this point we have the camera photo on disk

                // wrap File object into a content provider
                Uri fileProvider = FileProvider.getUriForFile(getContext(), "me.vivh.fileprovider", photoFile);
                // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
                Bitmap resizedBitmap = BitmapScaler.scaleToFill(takenImage, 500,500);

                // Configure byte output stream
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                // Compress the image further
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
                File resizedFile = getPhotoFileUri(photoFileName + "_resized.jpg");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(resizedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // Write the bytes of the bitmap to file
                try {
                    fos.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Load the taken image into a preview
                ivPreview.setImageBitmap(resizedBitmap);
                tookPhoto = true;
            } else { // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void postPhoto(final ParseFile imageFile, final ParseUser user){
        pb.setVisibility(ProgressBar.VISIBLE);
        imageFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    user.put("profilePic", imageFile);
                    user.saveInBackground();
                    // once background job is complete
                    pb.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(getContext(),"Photo changed!",Toast.LENGTH_LONG).show();

                    // TODO - redirect in a better way
                    // replace existing fragment with profile fragment inside the frame
                    ViewPager vp= (ViewPager) getActivity().findViewById(R.id.viewPager);
                    vp.setCurrentItem(3, false);
                } else {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Failed to change photo",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }
}
