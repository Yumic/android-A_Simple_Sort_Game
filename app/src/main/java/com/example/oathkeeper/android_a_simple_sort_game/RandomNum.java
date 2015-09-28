package com.example.oathkeeper.android_a_simple_sort_game;
import android.util.Log;

import java.util.Random;

/**
 * 数组乱序类
 * @author noam
 */
public class RandomNum {

    /**
     * 对给定数目的自0开始步长为1的数字序列进行乱序
     *
     * @param no 给定数目
     * @return 乱序后的数组
     */
    public static int[] getSequence(int no) {
        int[] sequence = new int[no];
        for (int i = 0; i < no; i++) {
            sequence[i] = i;
        }
        Random random = new Random();
        for (int i = 0; i < no; i++) {
            int p = random.nextInt(no);
            int tmp = sequence[i];
            sequence[i] = sequence[p];
            sequence[p] = tmp;
        }
        random = null;

        for(int i= 0;i<no;i++)
        {
            Log.d("test","Place:"+i+"   Num:"+sequence[i]);
        }
        return sequence;
    }
}