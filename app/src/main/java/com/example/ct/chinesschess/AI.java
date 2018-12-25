package com.example.ct.chinesschess;

import java.util.ArrayList;
import java.util.List;

public class AI {
    void m_print(int[] result) {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 9; j++) {
                if(result[i*9+j] > 9)
                    System.out.print(result[i*9+j]);
                else
                    System.out.print(" "+result[i*9+j]);
                System.out.print(" ");
            }
            System.out.println("");
        }
    }
    String vec_to_str(int[] v) {
        String result = "";
        for(int i = 0; i < v.length; i++)
            result = result + v[i] + ".";
        return result;
    }
    static int[] str_to_vec(String str) {
        int[] result = new int[90];
        String[] s = str.split("\\.");
        for(int i = 0; i <90; i++)
            result[i] = Integer.parseInt(s[i]);
        return result;
    }
    // a-b剪枝
    public node a_b(int[] cur) {
        int height = 3;
        node root = new node(cur, true, null);
        new_process(root, height-1);
        return root;
    }

    int evaluation(String str) {
        int[] board = str_to_vec(str);
        return  evaluation_power(board);
    }


    // 评估棋力
    int evaluation_power(int[] board) {
        int result = 0;
        for(int i = 0; i < board.length; i++) {
            if(board[i] < 17 ) {
                result += get_power(board[i]);
            } else
                result -= get_power(board[i]);
        }
        return result;
    }
    int[] power = {
            600,600,270,270,120,120,120,120,
            285,285,30,30,30,30,30,10000,
            600,600,270,270,120,120,120,120,
            285,285,30,30,30,30,30,10000
    };
    // 获得棋力
    int get_power(int chess) {
        if(chess == 0)
            return 0;
        return power[chess-1];
    }

    boolean game_over(int[] board) {
        int count = 0;
        for(int i = 0; i < board.length; i++) {
            if(board[i] == 16 || board[i] == 32)
                count++;
        }
        return count!=2;
    }

    void new_process(node p, int height) {
//    	List<Thread> thread_pool = new ArrayList<>();
        int[] data = str_to_vec(p.val);
        if(height == -1)
            return;
        if(game_over(data)) {
            int value = evaluation(p.val);
            if(p.type) {
                p.ex = value;
            } else {
                p.ex = value;
            }
            return;
        }
        int cur;
        if(p.type) // 当前是极大层
            cur = Integer.MIN_VALUE;
        else        // 当前是极小层
            cur = Integer.MAX_VALUE;
        for(int i = 0; i < data.length; i++) {
            if((p.type && data[i] > 16)||(!p.type && data[i] <= 16))
                continue;
            for(int j = 0; j < 90; j++) {
                if(can_move(i, j, data, p.type)) {
                    int[] new_val = data.clone();
                    new_val[j] = new_val[i];
                    new_val[i] = 0;
//                    if(i == 12 && j == 39 && height==3) {
//                    	m_print(new_val);
//                    }
                    node child = new node(new_val, !p.type, p);
                    if(height == 0) {
                        int value = evaluation(child.val);
                        if(p.type) { // 极大层
                            if(value > cur) {
                                cur = value;
                                p.choose = child.val;
                            }
                            if(p.parent != null && p.parent.update == true && cur >= p.parent.ex) {
                                p.ex = cur;
                                return; // b剪枝
                            }
                        } else { // 极小层
                            if(value < cur) {
                                cur = value;
                                p.choose = child.val;
                            }
                            if(p.parent != null && p.parent.update == true && p.parent.ex >= cur) {
                                p.ex = cur;
                                return; // a减枝
                            }
                        }
//                        child = null;
                    } else {
                        new_process(child, height-1);
                        if(p.type) { // 极大层
                            if(child.ex > cur) {
                                cur = child.ex;
                                p.choose = child.val;
                            }
                            if(p.parent != null && p.parent.update == true && cur >= p.parent.ex) {
                                p.ex = cur;
                                return; // b剪枝
                            }
                        } else { // 极小层
                            if(child.ex < cur) {
                                cur = child.ex;
                                p.choose = child.val;
                            }
                            if(p.parent != null && p.parent.update == true && p.parent.ex >= cur) {
                                p.ex = cur;
                                return; // a剪枝
                            }
                        }
//                    	Thread new_thread = new Thread() {
//                    		public void run() {
//
//                    		}
//                    	};
//                    	new_thread.start();
                    }
                }
            }
        }
//        try {
//            for(int i = 0; i < thread_pool.size(); i++)
//            	thread_pool.get(i).join();
//        } catch(Exception e) {
//        	System.out.println(e.getMessage());
//        }
        if(p.parent != null) {
            if(p.parent.update == false) {
                p.parent.update = true;
                p.parent.ex = cur;
            } else {
                if(p.parent.type) {
                    if(p.parent.ex < cur)
                        p.parent.ex = cur;
                } else {
                    if(p.parent.ex > cur)
                        p.parent.ex = cur;
                }
            }
        }
        p.ex = cur;
    }

    boolean can_move(int from_index, int to_index, int[] board, boolean type) {
        if(to_index < 0 || to_index > 89)
            return false;
        if(type) {
            int from = board[from_index];
            int to = board[to_index];
            if(from_index == to_index)
                return false;
            if(from > 16 || from == 0) // 不能移动空白
                return false; // AI移动黑方
            if(to < 17 && to != 0)
                return false; // 不能自残
            // 兵
            if((from == 11 || from == 12 || from == 13 || from == 14 || from == 15)){
                if( to_index != from_index-1 && to_index != from_index+1 && to_index != from_index+9 ) // 不能后退
                    return false;
                if( from_index < 45 && (to_index == from_index-1 || to_index == from_index+1)) // 过河前不能左右移动
                    return false;
            }
            // 车
            if(from == 1 || from == 2) {
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
            if(from == 9 || from == 10) {
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
            if(from == 3 || from == 4) {
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
            if(from == 5 || from == 6) {
                // 不能过河
                if(to_index > 44)
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
            if(from == 7 || from == 8) {
                // 不能出王宫
                if(to_index/9 > 2 || to_index%9 <3 || to_index%9 > 5)
                    return false;
                // 走对角
                if(to_index != from_index+1-9 && to_index != from_index+1+9 && to_index != from_index-1-9 && to_index != from_index-1+9)
                    return false;
            }
            // 帅
            if(from == 16) {
                // 不能出王宫
                if(to_index/9 > 2 || to_index%9 <3 || to_index%9 > 5)
                    return false;
                // 走直线
                if(to_index != from_index+1 && to_index != from_index-1 && to_index != from_index+9 && to_index != from_index-9)
                    return false;
                // 将帅不能直视
                int i = 0;
                for(i = 0; i < 90; i++)
                    if(board[i] == 32)
                        break;
                if(i%9 == to_index%9) {
                    int count = 0;
                    for(int j = to_index+9; j < i; j+=9)
                        if(board[j] != 0)
                            count++;
                    if(count==0)
                        return false;
                }
            }
            return true;
        } else {
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
                // 将帅不能直视
                int i = 0;
                for(i = 0; i < 90; i++)
                    if(board[i] == 16)
                        break;
                if(i%9 == to_index%9) {
                    int count = 0;
                    for(int j = to_index-9; j > i; j-=9)
                        if(board[j] != 0)
                            count++;
                    if(count==0)
                        return false;
                }
            }
            return true;
        }
    }
}



