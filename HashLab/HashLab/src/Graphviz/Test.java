package Graphviz;

import java.io.File;

public class Test {
    public static void createDotGraph(String dotFormat,String fileName)
    {
        GraphViz gv=new GraphViz();
        gv.addln(gv.start_graph());
        gv.add(dotFormat);
        gv.addln(gv.end_graph());
        // png为输出格式，还可改为pdf，gif，jpg等
        String type = "png";
        // gv.increaseDpi();
        gv.decreaseDpi();
        gv.decreaseDpi();
        File out = new File(fileName+"."+ type);
        gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
    }

    public static void main(String[] args) throws Exception {
        String dotFormat="nodesep=.05;  rankdir=LR;  node [shape=record,width=.1,height=.1];  node0 [label = \"<f0> |<f1> |<f2> |<f3> |<f4> |<f5> |<f6> | \",height=2.5];  node [width = 1.5];  node1 [label = \"{<n>||<p> }\"];node0:f0 -> node1:n;   node2 [label = \"{<n> a1  | 805 |as<p> }\"];   node3 [label = \"{<n> i9  | 718 |a <p> }\"];   node4 [label = \"{<n> e5  | 989 |<p> }\"];   node5 [label = \"{<n> t20 | 959 |<p> }\"] ;   node6 [label = \"{<n> o15 | 794 |<p> }\"] ;   node7 [label = \"{<n> s19 | 659 |<p> }\"] ;     node0:f1 -> node2:n;   node0:f2 -> node3:n;   node0:f5 -> node4:n;   node0:f6 -> node5:n;   node2:p -> node6:n;   node4:p -> node7:n;";
        createDotGraph(dotFormat, "DotGraph");
    }
}

