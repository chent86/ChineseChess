package com.example.ct.chinesschess;

public class node {
    boolean type; // true:极大层  false:极小层
    boolean update;
    int ex;
    String val;
    node parent;
    String choose;
    node(int[] val, boolean type, node parent) {
        this.update = false;
        this.val = "";
        for(int i = 0; i < val.length; i++)
            this.val = this.val + val[i]+ ".";
        this.type = type;
        this.parent = parent;
    }
    void update(int ex) {
        this.ex = ex;
    }
}
