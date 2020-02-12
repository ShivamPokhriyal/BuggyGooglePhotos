package com.example.buggygooglephotos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private final int IMAGE_VIDEO_FETCH = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("video/*");
                startActivityForResult(i, IMAGE_VIDEO_FETCH);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == IMAGE_VIDEO_FETCH) {
            Uri selectedFileURI = intent.getData();
            String type = this.getContentResolver().getType(selectedFileURI);

            if (type == null || TextUtils.isEmpty(type)) {
                // This shouldn't happen right?
                // Since https://developer.android.com/training/secure-file-sharing/retrieve-info
                // mentions a FileProvider determines the file's MIME type from its filename extension. I've to add this block
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(selectedFileURI, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int colIndex = cursor.getColumnIndex(MediaStore.MediaColumns._ID);
                        type = cursor.getString(colIndex);
                    }
                } catch (IllegalArgumentException e) {
                    Log.d("Check Type :: ", type + "");
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else {
                Log.d("Type:: ", type + "");
            }
        }
    }
}
