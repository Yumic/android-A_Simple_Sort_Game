package com.example.oathkeeper.android_a_simple_sort_game;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.button_open_album)
    Button buttonOpenAlbum;
    @Bind(R.id.imageView_picture)
    ImageView imageViewPicture;
    //定义手势检测器实例
    private GestureDetector detector;
    private DisplayMetrics displayMetrics;
    private int mScreenWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //获取屏幕宽高
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;// 获取屏幕分辨率宽
        //Crop.of();
        Log.d("test", "屏幕宽度为：" + mScreenWidth);
        detector = new GestureDetector(this,new MyGestureListener(this));
        buttonOpenAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d("test","Action:"+event.getAction());

        return detector.onTouchEvent(event);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.d("uri", uri.toString());
            ContentResolver contentResolver = this.getContentResolver();
            String path;
            if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){
                path= FileUtils.getPathAfterKitkat(this, uri);
            }else{
                path=  FileUtils.getPathBeforeKitkat(this, uri);
            }
            //从URI获取图片
            //Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
            Bitmap bitmap = BitmapUtils.compressImageFromFile(path,mScreenWidth);
            //显示图
            ImageView imageView = (ImageView) findViewById(R.id.imageView_picture);
            //将Bitmap设定到ImageView
            imageView.setImageBitmap(bitmap);
            //由于4.4以上和以下获取的uri不同，因此区别对待

        }
        super.onActivityResult(requestCode, resultCode, data);
    }








}