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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

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
    ImageView[] imageViews;
    private Animation translateAnimation1 = null;
    private Animation translateAnimation2 = null;
    int flag =0;//指向空格

    List<ImagePiece> pieces;

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
        detector = new GestureDetector(this, new MyGestureListener(this));

        randomList= RandomNum.getSequence(9);

        buttonOpenAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pieces!=null) pieces.clear();
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
            pieces = BitmapUtils.split(bitmap, 3, 3);
            for (int i = 1; i < 9; i++) {

                ImageView[] tempImages={game1,game2,game3,game4,game5,game6,
                        game7,game8,game9};
                imageViews=tempImages;
                if(imageViews[i]!=null )
                {
                    imageViews[i].setImageBitmap(pieces.get(randomList[i]).bitmap);
                    Log.d("test","now:___________________________"+i);
                }
            }

            //创建手势监听器

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    class MyGestureListener implements GestureDetector.OnGestureListener {

        private Context context;
        private Bitmap tempBitmap;

        public MyGestureListener(Context context) {
            this.context=context;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float minMove = 120;         //最小滑动距离
            float minVelocity = 0;      //最小滑动速度
            float beginX = e1.getX();
            float endX = e2.getX();
            float beginY = e1.getY();
            float endY = e2.getY();


            if(beginX-endX>minMove&&Math.abs(velocityX)>minVelocity){   //左滑
                if(flag%3!=2)
                {
                    movePicture(flag,flag+1);
                    Toast.makeText(context, "左滑", Toast.LENGTH_SHORT);
                    //交换flag与flag+1指向的内容
                }


            }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
                if(flag%3!=0)
                {
                    movePicture(flag,flag-1);
                    //交换flag与flag-1所指向的内容
                    Toast.makeText(context,"右滑",Toast.LENGTH_SHORT);
                }
            }else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
                if(flag-6<0)
                {
                    movePicture(flag,flag+3);
                    //交换flag与flag+3所指向的内容
                    Toast.makeText(context,"上滑",Toast.LENGTH_SHORT);
                }
            }else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
                if(flag>2)
                {
                    movePicture(flag,flag-3);
                    //交换flag与flag-3所指向的内容
                    Toast.makeText(context,"下滑",Toast.LENGTH_SHORT);
                }
            }



            return false;
        }

        private void movePicture(int source,int des){
           // imageViews[des].setDrawingCacheEnabled(true);
           // tempBitmap=imageViews[source].getDrawingCache();
            imageViews[source].setImageBitmap(pieces.get(randomList[des]).bitmap);
            imageViews[des].setImageBitmap(null);
            pieces.get(randomList[source]).bitmap=pieces.get(randomList[des]).bitmap;
            pieces.get(randomList[des]).bitmap=null;
//            int temp = randomList[source];
//            randomList[source]=randomList[des];
//            randomList[des]=temp;
            flag=des;
            //imageViews[des].setDrawingCacheEnabled(false);


//            int []sourceLocation = new int[2];
//            int []desLocation = new int[2];
//            imageViews[source].getLocationOnScreen(sourceLocation);
//            imageViews[des].getLocationOnScreen(desLocation);
//            translateAnimation1 = new TranslateAnimation(sourceLocation[0], desLocation[0],
//                    sourceLocation[1],desLocation[1]);
//            translateAnimation1.setDuration(2000);
//            translateAnimation2 = new TranslateAnimation(desLocation[0], sourceLocation[0],
//                    desLocation[1],sourceLocation[1]);
//            translateAnimation2.setDuration(2000);
//            imageViews[source].startAnimation(translateAnimation1);
//            imageViews[des].startAnimation(translateAnimation2);
//            flag=des;
        }
    }

}
