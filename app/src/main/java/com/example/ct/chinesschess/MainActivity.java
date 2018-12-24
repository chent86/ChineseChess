package com.example.ct.chinesschess;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean my_turn = true;
    int last_click = -1;
    private int[] board = {
            1, 3, 5, 7,16, 8, 6, 4, 2,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 9, 0, 0, 0, 0, 0,10, 0,
            11, 0,12, 0,13, 0,14, 0,15,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            27, 0,28, 0,29, 0,30, 0,31,
            0,25, 0, 0, 0, 0, 0,26, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            17,19,21,23,32,24,22,20,18
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    boolean can_move(int from_index, int to_index) {
        int from = board[from_index];
        int to = board[to_index];
        if(from < 17)
            return false; // 只能移动红方
        if(to >= 17)
            return false; // 不能自残
        // 兵
        if((from == 27 || from == 28 || from == 29 || from == 30 || from == 31)){
            if( to_index != from_index-1 && to_index != from_index+1 && to_index != from_index-9 ) // 不能后退
                return false;
            if( from_index > 44 && (to_index == from_index-1 || to_index == from_index+1)) // 过河前不能左右移动
                return false;
        }
        // 车
        if(from == 17 || from == 18) {
            if(from_index/9 != to_index/9 && from_index%9 != to_index%9) // 只能横向或者纵向移动
                return false;
            if(from_index/9 == to_index/9) { // 路径上不能有其他棋子
                if(from_index < to_index) {
                    for(int i = from_index+1; i < to_index; i++)
                        if(board[i] != 0)
                            return false;
                } else {
                    for(int i = from_index-1; i > to_index; i--)
                        if(board[i] != 0)
                            return false;
                }
            } else {
                if(from_index < to_index) {
                    for(int i = from_index+9; i < to_index; i+=9)
                        if(board[i] != 0)
                            return false;
                } else {
                    for(int i = from_index-9; i > to_index; i-=9)
                        if(board[i] != 0)
                            return false;
                }
            }
        }
        // 炮
        if(from == 25 || from == 26) {
            if(from_index/9 != to_index/9 && from_index%9 != to_index%9) // 只能横向或者纵向移动
                return false;
            if(from_index/9 == to_index/9) {  // 移动路径中最多跨一个棋子
                int count = 0;
                if(from_index < to_index) {
                    for(int i = from_index+1; i < to_index; i++)
                        if(board[i] != 0)
                            count++;
                } else {
                    for(int i = from_index-1; i > to_index; i--)
                        if(board[i] != 0)
                            count++;
                }
                if(count > 1)
                    return false;
                if(count == 0 && board[to_index] != 0) // 不跨子不能吃
                    return false;
            } else {
                int count = 0;
                if(from_index < to_index) {
                    for(int i = from_index+9; i < to_index; i+=9)
                        if(board[i] != 0)
                            count++;
                } else {
                    for(int i = from_index-9; i > to_index; i-=9)
                        if(board[i] != 0)
                            count++;
                }
                if(count > 1)
                    return false;
                if(count == 0 && board[to_index] != 0) // 不跨子不能吃
                    return false;
            }
        }
        // 马
        if(from == 19 || from == 20) {
            // 走日字
            if(to_index != from_index+1-18 && to_index != from_index+2-9 && to_index != from_index+2+9 && to_index != from_index+1+18
                    && to_index != from_index-1-18 && to_index != from_index-2-9 && to_index != from_index-2+9 && to_index != from_index-1+18)
                return false;
            // 卡马脚
            if((to_index == from_index+1-18 && board[from_index-9] != 0) || (to_index == from_index+2-9 && board[from_index+1] != 0) ||
                    (to_index == from_index+2+9 && board[from_index+1] != 0) || (to_index == from_index+1+18 && board[from_index+9] != 0) ||
                    (to_index == from_index-1-18 && board[from_index-9] != 0) || (to_index == from_index-2-9 && board[from_index-1] != 0) ||
                    (to_index == from_index-2+9 && board[from_index-1] != 0) || (to_index == from_index-1+18 && board[from_index+9] != 0)) {
                return false;
            }
        }
        // 象
        if(from == 21 || from == 22) {
            // 不能过河
            if(to_index < 45)
                return false;
            // 走田字
            if(to_index != from_index+2-18 && to_index != from_index+2+18 && to_index != from_index-2-18 && to_index != from_index-2+18)
                return false;
            // 卡象脚
            if((to_index == from_index+2-18 && board[from_index+1-9] != 0) || (to_index == from_index+2+18 && board[from_index+1+9] != 0) ||
                    (to_index == from_index-2-18 && board[from_index-1-9] != 0) || (to_index == from_index-2+18 && board[from_index-1+9] != 0)) {
                return false;
            }
        }
        // 士
        if(from == 23 || from == 24) {
            // 不能出王宫
            if(to_index/9 < 7 || to_index%9 <3 || to_index%9 > 5)
                return false;
            // 走对角
            if(to_index != from_index+1-9 && to_index != from_index+1+9 && to_index != from_index-1-9 && to_index != from_index-1+9)
                return false;
        }
        // 帅
        if(from == 32) {
            // 不能出王宫
            if(to_index/9 < 7 || to_index%9 <3 || to_index%9 > 5)
                return false;
            // 走直线
            if(to_index != from_index+1 && to_index != from_index-1 && to_index != from_index+9 && to_index != from_index-9)
                return false;
        }
        return true;
    }

    void click_chess(View view) {
        if(my_turn) {
            ConstraintLayout c = (ConstraintLayout)view.getParent();
            int id = c.indexOfChild(view);
            if(last_click == -1) {
                if(board[id] != 0 && board[id] > 16) {
                    ImageView target = (ImageView) c.getChildAt(id);
                    set_choose(target.getBackground());
                    last_click = id;
                }
            } else {
                if(board[id] > 16) {
                    ImageView target = (ImageView) c.getChildAt(id);
                    set_choose(target.getBackground());
                    last_click = id;
                }
                else if(can_move(last_click, id)) {
                    ImageView from = (ImageView) c.getChildAt(last_click);
                    Drawable from_img = from.getBackground();
                    c.getChildAt(last_click).setBackground(null);
                    c.getChildAt(id).setBackground(from_img);
                    board[id] = board[last_click];
                    board[last_click] = 0;
                    set_choose(null);
                    last_click = -1;
                }
            }
        }
    }

    void set_choose(Drawable d) {
        ImageView choose = findViewById(R.id.choose);
        choose.setBackground(d);
    }
}
