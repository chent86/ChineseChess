package com.example.ct.chinesschess;

import java.util.ArrayList;
import java.util.List;

public class node {
    boolean type; // true:极大层  false:极小层
    boolean update;
    int a;
    int b;
    String val;
    node parent;
    List<node> children;
    int choose;
    node(int[] val, boolean type, node parent) {
        this.update = false;
        this.val = "";
        for(int i = 0; i < val.length; i++)
            this.val = this.val + val[i]+ ".";
        this.type = type;
        this.parent = parent;
        this.children = new ArrayList<>();
    }
}
