public class node {
    boolean type; // true:�����  false:��С��
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
}
