package com.example.ct.chinesschess;

public class AI {
    int che_pos[] = {
            206,208,207,213,214,213,207,208,206,
            206,212,209,216,233,216,209,212,206,
            206,208,207,214,216,214,207,208,206,
            206,213,213,216,216,216,213,213,206,
            208,211,211,214,215,214,211,211,208,
            208,212,212,214,215,214,212,212,208,
            204,209,204,212,214,212,204,209,204,
            198,208,204,212,212,212,204,208,198,
            200,208,206,212,200,212,206,208,200,
            194,206,204,212,200,212,204,206,194
    };
    int ma_pos[] = {
            90,90,90,96,90,96,90,90,90,
            90,96,103,97,94,97,103,96,90,
            92,98,99,103,99,103,99,98,92,
            93,108,100,107,100,107,100,108,93,
            90,100,99,103,104,103,99,100,90,
            90,98,101,102,103,102,101,98,90,
            92,94,98,95,98,95,98,94,92,
            93,92,94,95,92,95,94,92,93,
            85,90,92,93,78,93,92,90,85,
            88,85,90,88,90,88,90,85,88
    };
    int pao_pos[] = {
            100,100,96,91,90,91,96,100,100,
            98,98,96,92,89,92,96,98,98,
            97,97,96,91,92,91,96,97,97,
            96,99,99,98,100,98,99,99,96,
            96,96,96,96,100,96,96,96,96,
            95,96,99,96,100,96,99,96,95,
            96,96,96,96,96,96,96,96,96,
            97,96,100,99,101,99,100,96,97,
            96,97,98,98,98,98,98,97,96,
            96,96,97,99,99,99,97,96,96,
    };
    int bin_pos[] = {
            9,9,9,11,13,11,9,9,9,
            19,24,34,42,44,42,34,24,19,
            19,24,32,37,37,37,32,24,19,
            19,23,27,29,30,29,27,23,19,
            14,18,20,27,29,27,20,18,14,
            7,0,13,0,16,0,13,0,7,
            7,0,7,0,15,0,7,0,7,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0
    };
    int jiang_pos[] = {
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,1,1,1,0,0,0,
            0,0,0,2,2,2,0,0,0,
            0,0,0,11,15,11,0,0,0,
    };
    int shi_pos[] = {
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,20,0,20,0,0,0,
            0,0,0,0,23,0,0,0,0,
            0,0,0,20,0,20,0,0,0,
    };
    int xiang_pos[] = {
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,20,0,0,0,20,0,0,
            0,0,0,0,0,0,0,0,0,
            18,0,0,0,23,0,0,0,18,
            0,0,0,0,0,0,0,0,0,
            0,0,20,0,0,0,20,0,0
    };
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
    public node a_b(int[] cur, int height) {
        node root = new node(cur, true, null);
        new_process(root, height-1);
        return root;
    }

    int evaluation(String str) {
        int[] board = str_to_vec(str);
        return  evaluation_power(board)+evaluation_pos(board);
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
    // 评估棋子位置
    int evaluation_pos(int[] board) {
        int result = 0;
        for(int i = 0; i <board.length; i++) {
            if(board[i] < 17) {
                int chess = board[i];
                switch(chess) {
                    case 1:case 2:
                        result += che_pos[89-i];
                        break;
                    case 3:case 4:
                        result += ma_pos[89-i];
                        break;
                    case 5:case 6:
                        result += xiang_pos[89-i];
                        break;
                    case 7: case 8:
                        result += shi_pos[89-i];
                        break;
                    case 16:
                        result += jiang_pos[89-i];
                        break;
                    case 9: case 10:
                        result += pao_pos[89-i];
                        break;
                    case 11: case 12: case 13: case 14: case 15:
                        result += bin_pos[89-i];
                        break;
                    default:
                        break;
                }
            } else {
                int chess = board[i];
                switch(chess) {
                    case 17:case 18:
                        result -= che_pos[i];
                        break;
                    case 19:case 20:
                        result -= ma_pos[i];
                        break;
                    case 21:case 22:
                        result -= xiang_pos[i];
                        break;
                    case 23: case 24:
                        result -= shi_pos[i];
                        break;
                    case 32:
                        result -= jiang_pos[i];
                        break;
                    case 25: case 26:
                        result -= pao_pos[i];
                        break;
                    case 27: case 28: case 29: case 30: case 31:
                        result -= bin_pos[i];
                        break;
                    default:
                        break;
                }
            }
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
            if((data[i] == 0)||(p.type && data[i] > 16)||(!p.type && data[i] <= 16))
                continue;
//            int[] possible_move = get_all_pos(i, data[i]).clone();
            for(int j = 0; j < 90; j++) {
                if((p.type && data[j] <= 16 && data[j] != 0)||(!p.type && data[j] > 16))
                    continue;
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
        if(from_index == to_index)
            return false;
        int from_x = from_index%9;
        int from_y = from_index/9;
        int to_x = to_index%9;
        int to_y = to_index/9;
        if(type) {
            int from = board[from_index];
            int to = board[to_index];
            if(from > 16 || from == 0) // 不能移动空白或者红方
                return false;
            if(to < 17 && to != 0)
                return false; // 不能自残
            switch(from) {
                case 1:case 2: // 车
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
                case 3:case 4:	// 马
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
                case 5:case 6: // 象
                    // 不能过河
                    if(to_index > 44)
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
                case 7: case 8: // 士
                    // 不能出王宫
                    if(to_y > 2 || to_x <3 || to_x > 5)
                        return false;
                    // 走对角
                    if(!(to_x == from_x+1 && to_y == from_y-1)&&!(to_x == from_x+1 && to_y == from_y+1)&&
                            !(to_x == from_x-1 && to_y == from_y+1)&&!(to_x == from_x-1 && to_y == from_y-1))
                        return false;
                    break;
                case 16: // 帅
                    if(to == 32 && from_x == to_x) {
                        int i = 0;
                        for(i = 0; i < 90; i++)
                            if(board[i] == 32)
                                break;
                        if(i%9 == to_x) {
                            int count = 0;
                            for (int j = from_index + 9; j < i; j += 9)
                                if (board[j] != 0)
                                    count++;
                            if (count == 0)
                                return true;
                        }
                    }
                    // 不能出王宫
                    if(to_y > 2 || to_x <3 || to_x > 5)
                        return false;
                    // 走直线
                    if(to_index != from_index+1 && to_index != from_index-1 && to_index != from_index+9 && to_index != from_index-9)
                        return false;
                    // 将帅不能直视
                    int i = 0;
                    for(i = 0; i < 90; i++)
                        if(board[i] == 32)
                            break;
                    if(i%9 == to_x) {
                        int count = 0;
                        for(int j = from_index+9; j < i; j+=9)
                            if(board[j] != 0)
                                count++;
                        if(count==0)
                            return false;
                    }
                    break;
                case 9: case 10: // 炮
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
                case 11: case 12: case 13: case 14: case 15: // 兵
                    if( from_index < 45 && from_y == to_y && (to_x == from_x-1 || to_x == from_x+1)) // 过河前不能左右移动
                        return false;
                    if(!(from_y == to_y && from_x == to_x-1)&&!(from_y == to_y && from_x == to_x+1)&&!(from_x==to_x && to_y==from_y+1)) {
                        return false; // 不能后退
                    }
                    break;
                default:
                    break;
            }
        } else {
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
        }
        return true;
    }

}