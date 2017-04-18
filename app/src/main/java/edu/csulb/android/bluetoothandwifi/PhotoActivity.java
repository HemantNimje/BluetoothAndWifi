package edu.csulb.android.bluetoothandwifi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 1;
    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private String selectedImagePath;
    private ImageView selectedImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Button btnAttach = (Button) findViewById(R.id.btn_attach);

        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the app have permission to access the external storage
                permissionCheck();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            selectedImagePath = getPath(selectedImageUri);
            //Toast.makeText(getApplicationContext(), selectedImagePath, Toast.LENGTH_SHORT).show();

            // Attach the selectedImage to the ImageView
            selectedImage = (ImageView) findViewById(R.id.selected_image);
            selectedImage.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));

        }

    }

    /*
    * Retrieve the path of the selected image URI
    * */
    public String getPath(Uri uri) {
        if (uri == null) {
            Toast.makeText(getApplicationContext(), "Uri is null", Toast.LENGTH_SHORT).show();
            return null;
        }

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    /*
    * Check if the permission to access external storage is allowed or not
    * Request permission from user to access external storage for the first time the app is installed
    * Once permission is granted move on with the process to get image from the gallery
    * */
    public void permissionCheck() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Add your explanation for the user here.
                Toast.makeText(this, "You have declined the permissions. Please allow them first to proceed.", Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission
                ActivityCompat.requestPermissions(this, new String[]
                                {android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            // Request image from the gallery app
            requestImageFromGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Do the external storage related work here.

                    requestImageFromGallery();
                } else {
                    // Permission is denied.
                    Toast.makeText(this, "Can't Proceed. You rejected the permission.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /*
    * Create a new intent to start the gallery app and wait for the result to be retrieved
    * */
    public void requestImageFromGallery() {
        Intent attachImageIntent = new Intent();
        attachImageIntent.setType("image/*");
        attachImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(attachImageIntent, "Select Picture"), SELECT_IMAGE);
    }
}
