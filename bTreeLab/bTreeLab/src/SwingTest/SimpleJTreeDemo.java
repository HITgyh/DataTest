package SwingTest;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import tree.*;

public class SimpleJTreeDemo {
    JFrame jFrame = new JFrame("简单树");
    public void init() {
        BNode Broot = new BNode(3);
        BNode rroot = Broot.getLeafNode();
        rroot.add(1,1);
        rroot.add(2,2);
        rroot.add(3,3);
        rroot.add(4,4);
        //创建DefaultMutableTreeNode对象代表结点
//        DefaultMutableTreeNode root = new DefaultMutableTreeNode("1 2 3 4 5");
//        DefaultMutableTreeNode guangDong = new DefaultMutableTreeNode("广东");
//        DefaultMutableTreeNode guangXi = new DefaultMutableTreeNode("广西");
//        DefaultMutableTreeNode fuShan = new DefaultMutableTreeNode("佛山");
//        DefaultMutableTreeNode shanTou = new DefaultMutableTreeNode("汕头");
//        DefaultMutableTreeNode guiLin = new DefaultMutableTreeNode("桂林");
//        DefaultMutableTreeNode nanNing = new DefaultMutableTreeNode("南宁");
//
//        //组装结点之间的关系
//        root.add(guangDong);
//        root.add(guangXi);
//
//        guangDong.add(fuShan);
//        guangDong.add(shanTou);
//
//        guangXi.add(guiLin);
//        guangXi.add(nanNing);
        JTree tree = new JTree(LetDraw(rroot));
        JFrame jFrame = new JFrame("简单树测试");
        jFrame.add(tree);

        //通过pack()方法设置最佳大小
        jFrame.pack();
        //设置Frame的位置和大小
        jFrame.setBounds(400,200,500,300);
        //设置Frame可见
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public DefaultMutableTreeNode LetDraw(BNode node){
        DefaultMutableTreeNode knode = new DefaultMutableTreeNode(node.toString());
        System.out.println(node.isLeaf);
        if (!node.isLeaf)
        {
            for (BNode k :node.sonNode){
                knode.add(LetDraw(k));
            }
        }
        return knode;
    }
    public static void main(String[] args) {
        new SimpleJTreeDemo().init();
    }
}