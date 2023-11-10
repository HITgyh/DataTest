package SwingTest;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import tree.*;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        BNode bNode = new BNode(3);
        BNode root = bNode.getLeafNode();
//        root= root.add(2,2);
//        root= root.add(1,1);
//        root= root.add(3,3);
//        root= root.add(4,4);
//        root = root.add(5,5);
//        root = root.add(6,6);
//        root = root.add(7,7);
//        root = root.delete(5);
        Scanner input = new Scanner(System.in);
        int num,komma, bomma;
        System.out.print("1.插入索引项\n2.删除索引项\n3.查找索引项\n4.区间查询\n5.可视化显示\n6.退出\n请输入你的选择：");
        while ((num = input.nextInt())!=6){
            switch (num){
                case 1:
                    System.out.print("请输入你要插入的值：");
                    komma = input.nextInt();
                    root = root.add(komma,komma);
                    break;
                case 2:
                    System.out.print("请输入你要删除的值：");
                    komma = input.nextInt();
                    root = root.delete(komma);
                    break;
                case 3:
                    System.out.print("请输入你要查找的值：");
                    komma = input.nextInt();
                    Integer temp = root.findData(komma);
                    if (temp==null){
                        System.out.println("没找到");
                    }
                    else {
                        System.out.println("该索引为"+temp);
                    }
                    break;
                case 4:
                    System.out.print("请输入你要进行区间查询的区间（均为开区间）：");
                    komma = input.nextInt();
                    bomma = input.nextInt();
                    root.intervalQuery(komma, bomma);
                    break;
                case 5:
                    System.out.print(root.toString());
                    JTree tree = new JTree(LetDraw(root));
                    JFrame jFrame = new JFrame("简单树测试");
                    jFrame.add(tree);
                    //通过pack()方法设置最佳大小
                    jFrame.pack();
                    //设置Frame的位置和大小
                    jFrame.setBounds(400,200,500,300);
                    //设置Frame可见
                    jFrame.setVisible(true);
                    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    break;
                case 6:
                    break;
                default:
                    System.out.println("输入错误");
            }
            if (num==6){
                break;
            }
            System.out.print("1.插入索引项\n2.删除索引项\n3.查找索引项\n4.区间查询\n5.可视化显示\n6.退出\n请输入你的选择：");
        }

//        JTree tree = new JTree(LetDraw(root));
//        JFrame jFrame = new JFrame("简单树测试");
//        jFrame.add(tree);
//        //通过pack()方法设置最佳大小
//        jFrame.pack();
//        //设置Frame的位置和大小
//        jFrame.setBounds(400,200,500,300);
//        //设置Frame可见
//        jFrame.setVisible(true);
//        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    public static DefaultMutableTreeNode LetDraw(BNode node){
        DefaultMutableTreeNode knode = new DefaultMutableTreeNode(node.toString());
        if (!node.isLeaf)
        {
//            for (BNode k :node.sonNode){
//                knode.add(LetDraw(k));
//            }
            for (int i = node.sonNode.size()-1; i>=0; i--){
                knode.add(LetDraw(node.sonNode.get(i)));
            }
        }
        return knode;
    }
}
