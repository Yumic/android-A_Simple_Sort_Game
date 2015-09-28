package com.example.oathkeeper.android_a_simple_sort_game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import android.widget.GridLayout;
import android.widget.ImageView;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.button_open_album)
    Button buttonOpenAlbum;
    @Bind(R.id.imageView_picture)
    ImageView imageViewPicture;
    @Bind(R.id.game_1)
    ImageView game1;
    @Bind(R.id.game_2)
    ImageView game2;
    @Bind(R.id.game_3)
    ImageView game3;
    @Bind(R.id.game_4)
    ImageView game4;
    @Bind(R.id.game_5)
    ImageView game5;
    @Bind(R.id.game_6)
    ImageView game6;
    @Bind(R.id.game_7)
    ImageView game7;
    @Bind(R.id.game_8)
    ImageView game8;
    @Bind(R.id.game_9)
    ImageView game9;
    @Bind(R.id.gridLayout_game)
    GridLayout gridLayoutGame;
    //定义手势检测器实例
    private GestureDetector detector;
    private DisplayMetrics displayMetrics;
    private int mScreenWidth;
    private Uri outputUri;
    private Context context;
    private int[] randomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        //获取屏幕宽高
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;// 获取屏幕分辨率宽

        randomList= RandomNum.getSequence(9);
       detector = new GestureDetector(this, new MyGestureListener(this));
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
        Log.d("test", "Action:" + event.getAction());
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
        //选择图片时
        if (requestCode != Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String fileName = System.currentTimeMillis() + ".jpg";
            outputUri = Uri.fromFile(new File(FileUtils.getDiskCacheDir(context), fileName));
            Crop.of(uri, outputUri).asSquare().start(this);

        }
        //获得Crop裁剪出的图片
        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            //将Crop的outputUri转换为真实路径
            String path;
            //由于Uri的转换在4.4前后不同，因此区别对待
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                path = FileUtils.getPathAfterKitkat(this, outputUri);
            } else {
                path = FileUtils.getPathBeforeKitkat(this, outputUri);
            }
            //将保存在路径中的图片显示出来
            Bitmap bitmap = BitmapUtils.compressImageFromFile(path, mScreenWidth);

            //初始化九块图片
            List<ImagePiece> pieces = BitmapUtils.split(bitmap, 3, 3);
            for (int i = 1; i < 9; i++) {

                ImageView[] imageViews={game1,game2,game3,game4,game5,game6,
                        game7,game8,game9};

                if(imageViews[i]!=null )
                {
                    imageViews[i].setImageBitmap(pieces.get(randomList[i]).bitmap);
                    Log.d("test","now:___________________________"+i);
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}