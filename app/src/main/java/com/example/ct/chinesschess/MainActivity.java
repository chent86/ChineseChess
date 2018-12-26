package com.example.ct.chinesschess;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
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

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

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
    int last_time;
    int last_min;
    int mode = 0;

    // 悔棋
    void oh_no(View view) {
        if(!my_turn) {
            Toast.makeText( getApplicationContext(), "这不是你的回合", Toast.LENGTH_SHORT).show();
            return;
        }
        if(record.size() > 1) {
            board = record.get(record.size()-2);
            record.remove(record.size()-1);
            init_board();
        } else {
            Toast.makeText( getApplicationContext(), "您还没有落子", Toast.LENGTH_SHORT).show();
        }
    }

    void restart(View view) {
        if(!my_turn) {
            Toast.makeText( getApplicationContext(), "这不是你的回合", Toast.LENGTH_SHORT).show();
            return;
        }
        if(record.size()>1) {
            board = first_board.clone();
            init_board();
        } else {
            Toast.makeText( getApplicationContext(), "您还没有落子", Toast.LENGTH_SHORT).show();
        }
    }

    void set_info() {
        ImageView tv = findViewById(R.id.info);
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
    private int[] first_board = {
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
    List<int[]> record = new ArrayList<>();
//private int[] board = {
//        1, 0, 5, 7, 16, 8, 6, 4, 2,
//        0, 0, 0, 0, 0, 0, 0, 0, 0,
//        0, 0, 3, 0, 0, 0, 0, 10, 0,
//        11, 0, 12, 0, 0, 0, 14, 0, 15,
//        0, 0, 0, 0, 0, 0, 0, 0, 0,
//        0, 0, 0, 0, 0, 0, 0, 0, 0,
//        27, 0, 28, 9, 0, 0, 30, 0, 31,
//        0, 0, 0, 0, 0, 0, 20, 26, 0,
//        0, 0, 0, 0, 0, 0, 0, 0, 18,
//        17, 19, 21, 23, 32, 24, 22, 0, 0,
//};
    int[] AI_result;
    void init_board() {
        ConstraintLayout c = findViewById(R.id.board);
        for(int i = 0; i < 90; i++) {
            int chess = board[i];
            // 车
            if(chess == 1 || chess == 2)
                c.getChildAt(i).setBackgroundResource(R.drawable.b_c);
            if(chess == 17 || chess == 18) {
                c.getChildAt(i).setBackgroundResource(R.drawable.r_c);
            }
            // 马
            if(chess == 3 || chess == 4)
                c.getChildAt(i).setBackgroundResource(R.drawable.b_m);
            if(chess == 19 || chess == 20)
                c.getChildAt(i).setBackgroundResource(R.drawable.r_m);
            // 象
            if(chess == 5 || chess == 6)
                c.getChildAt(i).setBackgroundResource(R.drawable.b_x);
            if(chess == 21 || chess == 22)
                c.getChildAt(i).setBackgroundResource(R.drawable.r_x);
            // 士
            if(chess == 7 || chess == 8)
                c.getChildAt(i).setBackgroundResource(R.drawable.b_s);
            if(chess == 23 || chess == 24)
                c.getChildAt(i).setBackgroundResource(R.drawable.r_s);
            // 将
            if(chess == 16)
                c.getChildAt(i).setBackgroundResource(R.drawable.b_j);
            if(chess == 32)
                c.getChildAt(i).setBackgroundResource(R.drawable.r_j);
            // 炮
            if(chess == 9 || chess == 10)
                c.getChildAt(i).setBackgroundResource(R.drawable.b_p);
            if(chess == 25 || chess == 26)
                c.getChildAt(i).setBackgroundResource(R.drawable.r_p);
            // 兵
            if(chess == 11 || chess == 12 || chess == 13 || chess == 14 || chess == 15)
                c.getChildAt(i).setBackgroundResource(R.drawable.b_z);
            if(chess == 27 || chess == 28 || chess == 29 || chess == 30 || chess == 31)
                c.getChildAt(i).setBackgroundResource(R.drawable.r_z);
            if(chess == 0)
                c.getChildAt(i).setBackground(null);
        }
    }

    void back(View view) {
        this.finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        record.add(board.clone());
        Intent intent = getIntent();
        mode = (int)intent.getSerializableExtra("mode");

        init_board();
        subscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Calendar calendar = Calendar.getInstance();
                int spent = calendar.get(Calendar.SECOND)-last_time;
                int min = calendar.get(Calendar.MINUTE)-last_min;
                spent += min*60;
                TextView tv = findViewById(R.id.spent_time);
                tv.setText("AI用时: "+spent+"s");
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
                board[to_index] = board[from_index];
                board[from_index] = 0;
                init_board();
                record.add(board.clone());
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

    void record_board() {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 9; j++) {
                System.out.print(board[i*9+j]);
                System.out.print(", ");
            }
            System.out.println("");
        }
    }

    void update_action(int index) {
        record_board();
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
        if(from_index == to_index)
            return false;
        int from_x = from_index%9;
        int from_y = from_index/9;
        int to_x = to_index%9;
        int to_y = to_index/9;
        int from = board[from_index];
        int to = board[to_index];
        if(from < 17)
            return false; // 只能移动红方
        if(to >= 17)
            return false; // 不能自残
        switch(from) {
            case 17:case 18: // 车
                if(from_x != to_x && from_y != to_y) // 只能横向或者纵向移动
                    return false;
                if(from_y == to_y) { // 路径上不能有其他棋子
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
                break;
            case 19:case 20: // 马
                // 走日字
                if(!(to_x == from_x+1 && to_y == from_y-2)&&!(to_x == from_x+2 && to_y == from_y-1)&&
                        !(to_x == from_x+2 && to_y == from_y+1)&&!(to_x == from_x+1 && to_y == from_y+2)&&
                        !(to_x == from_x-1 && to_y == from_y+2)&&!(to_x == from_x-2 && to_y == from_y+1)&&
                        !(to_x == from_x-2 && to_y == from_y-1)&&!(to_x == from_x-1 && to_y == from_y-2))
                    return false;
                // 卡马脚
                if((to_index == from_index+1-18 && board[from_index-9] != 0) || (to_index == from_index+2-9 && board[from_index+1] != 0) ||
                        (to_index == from_index+2+9 && board[from_index+1] != 0) || (to_index == from_index+1+18 && board[from_index+9] != 0) ||
                        (to_index == from_index-1-18 && board[from_index-9] != 0) || (to_index == from_index-2-9 && board[from_index-1] != 0) ||
                        (to_index == from_index-2+9 && board[from_index-1] != 0) || (to_index == from_index-1+18 && board[from_index+9] != 0)) {
                    return false;
                }
                break;
            case 21:case 22: // 象
                // 不能过河
                if(to_index < 45)
                    return false;
                // 走田字
                if(!(to_x == from_x+2 && to_y == from_y-2)&&!(to_x == from_x+2 && to_y == from_y+2)&&
                        !(to_x == from_x-2 && to_y == from_y+2)&&!(to_x == from_x-2 && to_y == from_y-2))
                    return false;
                // 卡象脚
                if((to_index == from_index+2-18 && board[from_index+1-9] != 0) || (to_index == from_index+2+18 && board[from_index+1+9] != 0) ||
                        (to_index == from_index-2-18 && board[from_index-1-9] != 0) || (to_index == from_index-2+18 && board[from_index-1+9] != 0)) {
                    return false;
                }
                break;
            case 23: case 24: // 士
                // 不能出王宫
                if(to_y < 7 || to_x <3 || to_x > 5)
                    return false;
                // 走对角
                if(!(to_x == from_x+1 && to_y == from_y-1)&&!(to_x == from_x+1 && to_y == from_y+1)&&
                        !(to_x == from_x-1 && to_y == from_y+1)&&!(to_x == from_x-1 && to_y == from_y-1))
                    return false;
                break;
            case 32: // 将
                if(to == 16 && from_x == to_x) {
                    int i = 0;
                    for (i = 0; i < 90; i++)
                        if (board[i] == 16)
                            break;
                    if (i % 9 == to_x) {
                        int count = 0;
                        for (int j = from_index - 9; j > i; j -= 9)
                            if (board[j] != 0)
                                count++;
                        if (count == 0)
                            return true;
                    }
                }
                // 不能出王宫
                if(to_y < 7 || to_x <3 || to_x > 5)
                    return false;
                // 走直线
                if(to_index != from_index+1 && to_index != from_index-1 && to_index != from_index+9 && to_index != from_index-9)
                    return false;
                // 将帅不能直视
                int i = 0;
                for(i = 0; i < 90; i++)
                    if(board[i] == 16)
                        break;
                if(i%9 == to_x) {
                    int count = 0;
                    for(int j = from_index-9; j > i; j-=9)
                        if(board[j] != 0)
                            count++;
                    if(count==0)
                        return false;
                }
                break;
            case 25: case 26: // 炮
                if(from_x != to_x && from_y != to_y) // 只能横向或者纵向移动
                    return false;
                if(from_y == to_y) {  // 移动路径中最多跨一个棋子
                    int count = 0;
                    if(from_index < to_index) {
                        for(i = from_index+1; i < to_index; i++)
                            if(board[i] != 0)
                                count++;
                    } else {
                        for(i = from_index-1; i > to_index; i--)
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
                        for(i = from_index+9; i < to_index; i+=9)
                            if(board[i] != 0)
                                count++;
                    } else {
                        for(i = from_index-9; i > to_index; i-=9)
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
                break;
            case 27: case 28: case 29: case 30: case 31: // 兵
                if( from_index > 44 && from_y == to_y && (to_x == from_x-1 || to_x == from_x+1)) // 过河前不能左右移动
                    return false;
                if(!(from_y == to_y && from_x == to_x-1)&&!(from_y == to_y && from_x == to_x+1)&&!(from_x==to_x && to_y==from_y-1))
                    return false; // 不能后退
                break;
            default:
                break;
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
    boolean double_pao() {
        int pao_1 = 0, pao_2 = 0, king = 0;
        for(int i = 0; i < 90; i++) {
            if(board[i] == 25)
                pao_1 = i;
            else if(board[i] == 26)
                pao_2 = i;
            else if(board[i] == 16)
                king = i;
        }
        return (pao_1%9==pao_2%9&& pao_1%9==king%9)||(pao_1/9 == pao_2/9 && pao_1/9==king/9);
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
                    Calendar calendar = Calendar.getInstance();
                    last_time = calendar.get(Calendar.SECOND);
                    last_min = calendar.get(Calendar.MINUTE);
                    Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(final Subscriber<? super String> subscriber) {
                            int count = 0;
                            for(int i = 0; i < 90; i++)
                                if(board[i] != 0)
                                    count++;
                            node root = null;
                            if(mode == 0) {
                                root =m_AI.a_b(board, 2);
                            } else {
                                if(double_pao())
                                    root =m_AI.a_b(board, 4);
                                else
                                    root =m_AI.a_b(board, 3);
                            }

                            AI_result = str_to_vec(root.choose).clone();
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
