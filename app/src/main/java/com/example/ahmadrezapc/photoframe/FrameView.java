package com.example.ahmadrezapc.photoframe;

import android.app.Activity;
import android.app.Notification;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
//import com.example.ahmadrezapc.photoframe.NewActivity.ImageAdapter;


/**
 * Created by Ahmadreza pc on 14/07/2017.
 */

public class FrameView extends Activity implements View.OnTouchListener {
    //OnClickListener (For button)


    // Image loading result to pass to startActivityForResult method.
    private static int LOAD_IMAGE_RESULTS = 1;

    // GUI components
    private Button button;  // The button
    private ImageView image;// ImageView

    // Scale stuff
    private ImageView img_scale;
    private float mScaleFactor = 0.5f;
    private float mRotationDegree = 0.f;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;
    private int mScreenHeight;
    private int mScreenWidth;
    private Matrix matrix = new Matrix();//Các lớp Matrix giữ một ma trận 3x3 để di chuyển tọa độ.
    private int mImageWidth, mImageHeight;
    private ScaleGestureDetector mScaleDetector;
    private RotateGestureDetector mRotateDetector;
    private MoveGestureDetector mMoveDetector;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.photo_select);

        // Find references to the GUI objects
        button = (Button)findViewById(R.id.button);
        image = (ImageView)findViewById(R.id.mainImage);

        // Set button's onClick listener object.
        //button.setOnTouchListener(this);

        // Get position from intent passed from MainActivity.java
        Intent i = getIntent();

        int position = i.getExtras().getInt("id");

        // Open the Image adapter
        ImageAdapter imageAdapter = new ImageAdapter(this);

        // Locate the ImageView in single_item_view.xml
        ImageView imageView = (ImageView) findViewById(R.id.image);

        // Get image and position from ImageAdapter.java and set into ImageView
        imageView.setImageResource(imageAdapter.mThumsIda[position]);

        img_scale = (ImageView) findViewById(R.id.mainImage);
        img_scale.setOnTouchListener(this);
        //// Lấy kích thước màn hình bằng pixel.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;

        //load anh mau
        Bitmap loadTempImg = BitmapFactory.decodeResource(getResources(), R.drawable.portrait);
        mImageHeight = loadTempImg.getHeight();
        mImageWidth = loadTempImg.getWidth();
        img_scale.setImageBitmap(loadTempImg);

        //view anh thu nho lai boi ma tran so voi anh goc
        matrix.postScale(mScaleFactor, mScaleFactor);
        img_scale.setImageMatrix(matrix);


        mScaleDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
        mRotateDetector = new RotateGestureDetector(getApplication(), new RotateListener());
        mMoveDetector = new MoveGestureDetector(getApplication(), new MoveListener());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            // Now we need to set the GUI ImageView data with data read from the picked file.
            image.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
    }

  //  @Override
    public void onClick(View v) {
        // Create the Intent for Image Gallery.
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, LOAD_IMAGE_RESULTS);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 1.0f));
            return true;
        }
    }

    private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            mRotationDegree -= detector.getRotationDegreesDelta();
            return true;
        }
    }

    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {
            PointF d = detector.getFocusDelta();
            mFocusX += d.x;
            mFocusY += d.y;

            return true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        mRotateDetector.onTouchEvent(event);
        mMoveDetector.onTouchEvent(event);
        float scaleImageCenterX = (mImageWidth * mScaleFactor) / 2;
        float scaleImageCenterY = (mImageHeight * mScaleFactor) / 2;

        matrix.reset();//Thiết lập ma trận để tính
        matrix.postScale(mScaleFactor, mScaleFactor);//Bài concats ma trận với quy mô quy định.
        matrix.postRotate(mRotationDegree, scaleImageCenterX, scaleImageCenterY);//Postconcats ma trận với vòng xoay cảng quy định.
        matrix.postTranslate(mFocusX - scaleImageCenterX, mFocusY - scaleImageCenterY);//Postconcats ma trận với các dịch quy định.

        ImageView view = (ImageView) v;
        view.setImageMatrix(matrix);
        return true;
    }


   /* public void change(){

    }*/
}
