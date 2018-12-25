package com.example.ct.chinesschess;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    boolean my_turn = true;
    int last_click = -1;
    boolean over = false;
    Subscriber subscriber;
    AI m_AI = new AI();
    void set_info() {
        TextView tv = findViewById(R.id.info);
        if(my_turn) {
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.INVISIBLE);
        }

    }
    static int[] str_to_vec(String str) {
        int[] result = new int[90];
        String[] s = str.split("\\.");
        for(int i = 0; i <90; i++)
            result[i] = Integer.parseInt(s[i]);
        return result;
    }
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
    int[] AI_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                int from_index = 0, to_index = 0;
                for(int i = 0; i < AI_result.length; i++) {
                    if(AI_result[i] != board[i]) {
                        if(AI_result[i] == 0 && board[i] != 0){
                            from_index = i;
                        } else {
                            to_index = i;
                        }
                    }
                }
                update_action(from_index);
                ConstraintLayout c = findViewById(R.id.board);
                ImageView from = (ImageView) c.getChildAt(from_index);
                Drawable from_img = from.getBackground();
                c.getChildAt(from_index).setBackground(null);
                c.getChildAt(to_index).setBackground(from_img);
                board[to_index] = board[from_index];
                board[from_index] = 0;
                my_turn = true;
                set_info();
                game_over(board);
            }

            @Override
            public void onCompleted() {
//                Log.d(tag, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
//                Log.d(tag, "Error!");
            }
        };
    }

    void update_action(int index) {
        TextView AI_action = findViewById(R.id.AI_action);
        TextView player_action = findViewById(R.id.player_action);
        if(my_turn) {
            player_action.setText("玩家: 动"+chess_name(board[index]));
        } else {
            AI_action.setText("AI: 动"+chess_name(board[index]));
        }
    }

    String chess_name(int chess) {
        // 车
        if(chess == 1 || chess == 2 || chess == 17 || chess == 18)
            return "车";
        // 马
        if(chess == 3 || chess == 4 || chess == 19 || chess == 20)
            return "马";
        // 象
        if(chess == 5 || chess == 6 || chess == 21 || chess == 22)
            return "象";
        // 士
        if(chess == 7 || chess == 8 || chess == 23 || chess == 24)
            return "士";
        // 将
        if(chess == 16 || chess == 32)
            return "将";
        // 炮
        if(chess == 9 || chess == 10 || chess == 25 || chess == 26)
            return "炮";
        // 兵
        if(chess == 11 || chess == 12 || chess == 13 || chess == 14 || chess == 15 || chess == 27 || chess == 28 || chess == 29 || chess == 30 || chess == 31)
            return "兵";
        return "error";
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
                if(count != 0 && board[to_index] == 0 ) // 不吃子不能跨
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
                if(count != 0 && board[to_index] == 0 ) // 不吃子不能跨
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

   void game_over(int[] board) {
        boolean win = true;
        boolean lose = true;
        for(int i = 0; i < board.length; i++) {
            if(board[i] == 16)
                win=false;
            if(board[i] == 32)
                lose=false;
        }
        if(win) {
            over = true;
            Toast.makeText(getApplicationContext(), "You win!", Toast.LENGTH_SHORT).show();
        }
        if(lose) {
            over = true;
            Toast.makeText(getApplicationContext(), "You lose!", Toast.LENGTH_SHORT).show();
        }

    }

    void click_chess(View view) {
        if(my_turn && !over) {
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
                    update_action(last_click);
                    ImageView from = (ImageView) c.getChildAt(last_click);
                    Drawable from_img = from.getBackground();
                    c.getChildAt(last_click).setBackground(null);
                    c.getChildAt(id).setBackground(from_img);
                    board[id] = board[last_click];
                    board[last_click] = 0;
                    set_choose(null);
                    last_click = -1;
                    my_turn = false;
                    set_info();

                    game_over(board);
                    if(over)
                        return;
                    Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(final Subscriber<? super String> subscriber) {
                            node root =m_AI.a_b(board);
                            AI_result = str_to_vec(root.children.get(root.choose).val).clone();
                            subscriber.onNext("OK");
                        }
                    });
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(subscriber);

                }
            }
        }
    }

    void set_choose(Drawable d) {
        ImageView choose = findViewById(R.id.choose);
        choose.setBackground(d);
    }
}
