package Graphviz;
import hashK.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class HashTestVitual {
    public static void main(String args[]) throws HashMap.HashException {
        HashMap hashMap = new HashMap(3, 0.5, 4);
        int operate, excute;
        boolean flag = false;
        System.out.print("1.输入\n2.删除\n3.查询\n4.可视化\n请输入你要进行的操作：");
        Scanner input = new Scanner(System.in);
        while ((operate = input.nextInt())>0){
            switch (operate){
                case 1:
                    System.out.print("请输入你要插入的数：");
                    excute = input.nextInt();
                    hashMap.add(excute);
                    break;
                case 2:
                    System.out.print("请输入你要删除的数：");
                    excute = input.nextInt();
                    hashMap.delete(excute);
                    break;
                case 3:
                    System.out.print("请输入你要查找的数：");
                    excute = input.nextInt();
                    System.out.println(hashMap.query(excute));
                    break;
                case 4:
                    int k =1;
                    String dot = "nodesep=.05;  rankdir=LR;  node [shape=record,width=.1,height=.1];";
                    String cat = "node0 [label = \"";
                    List<Integer> swag = new ArrayList<>();
                    for (int i=0; i<hashMap.getBucketNum()-1; i++){
                        cat += "<f"+i+">|";
                    }
                    cat += "<f"+(hashMap.getBucketNum()-1)+">";
                    cat += "\",height=2.5];  node [width = 1.5];";
                    dot += cat;
                    for (int i=0; i<hashMap.getBucketNum(); i++){
                        LinkedList<Integer> list = hashMap.getLink(i);
                        if(list.size()<=hashMap.getPageNum()&&list.size()!=0){
                            cat = "node"+k+" [label = \"{<n> ";
                            swag.add(k);
                            k++;
                            for (Integer s : list){
                                cat += s+" |";
                            }
                            for (int i1=1; i1<=hashMap.getPageNum()-list.size()-1; i1++){
                                cat += "|";
                            }
                            cat += "<p> }\"];";
                            dot += cat;
                        }else if (list.size()==0){
                            cat = "node"+k+" [label = \"{<n>";
                            swag.add(k);
                            k++;
                            for (int i2=1; i2<=hashMap.getPageNum()-1; i2++){
                                cat+="|";
                            }
                            cat += "<p> }\"];";
                            dot +=cat;
                        }else {
                            int j=0,yu;
                            boolean bFlag = true;
                            yu = list.size()%hashMap.getPageNum();
                            for (; j<list.size()-yu; j+=hashMap.getPageNum()){
                                cat = "node"+k+" [label = \"{<n>";
                                k++;
                                int h=0;
                                for (; h<hashMap.getPageNum(); h++){
                                    cat += list.get(j+h)+" |";
                                }
                                cat+="<p> }\"];";
                                if (!bFlag){
                                    cat +="node"+(k-2)+":p ->"+"node"+(k-1)+":n;";
                                 }
                                if (bFlag){
                                    swag.add(k-1);
                                    bFlag = false;
                                }
                                dot += cat;
                            }
//                            yu = list.size()%hashMap.getPageNum();
                            if (yu!=0){
                                cat = "node"+k+" [label = \"{<n>";
                                k++;
                                for (int u=list.size()-yu; u<list.size(); u++){
                                    cat+=list.get(u)+"|";
                                }
                                for (;yu<=hashMap.getPageNum()-yu; yu++){
                                    cat +="|";
                                }
                                cat+="<p> }\"];";
                                cat += "node"+(k-2)+":p ->"+"node"+(k-1)+":n;";
                                dot += cat;
                            }
                        }
                    }
                    cat = "";
                    for (int i=0; i<hashMap.getBucketNum(); i++){
                        cat+="node0:f"+i+" -> "+"node"+swag.get(i)+":n;";
                    }
                    dot +=cat;
                    Test.createDotGraph(dot, "DotGraph");
//                    System.out.print(dot);
                    break;
                default:
                    System.out.println("您的输入有误");
                    flag = true;
                    break;
            }
            if (flag){
                break;
            }
            System.out.print("1.输入\n2.删除\n3.查询\n4.可视化\n请输入你要进行的操作：");
        }
    }


}
