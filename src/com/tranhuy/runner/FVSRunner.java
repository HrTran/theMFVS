package com.tranhuy.runner;

import com.tranhuy.model.Graph;
import com.tranhuy.model.Node;
import com.tranhuy.utils.GraphUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by huytt99 on 08/04/2020
 */
public class FVSRunner {


    public static void main(String[] args) {
        Graph graph = new Graph(8);
        graph.updateMap();

        try {
            graph.addEdge(0, 1);
            graph.addEdge(1, 7);
            graph.addEdge(1, 2);
            graph.addEdge(2, 6);
            graph.addEdge(2, 3);
            graph.addEdge(3, 4);
            graph.addEdge(4, 2);
            graph.addEdge(4, 5);
            graph.addEdge(6, 3);
            graph.addEdge(6, 5);
            graph.addEdge(7, 6);
            graph.addEdge(7, 0);

//            Node removeNode = graph.getNodes().get(3);
//            graph.removeNode(removeNode);
//            Node removeNode2 = graph.getNodes().get(2);
//            graph.removeNode(removeNode2);

            graph.printGraph();
//
//            System.out.println("\n==================== Is contain cycle? ============================");
//            if(graph.isCyclic())
//                System.out.println("Graph contains cycle");
//            else
//                System.out.println("Graph doesn't contain cycle");
//
//            System.out.println("\n==================== Print SCC components ===============================");
//            GraphUtils utils = new GraphUtils();
//            List<List<Integer>> sCCs = utils.DecomposeGraphtoSCCs(graph);
//            for(List<Integer> sCC : sCCs) {
//                Graph g = utils.createGraphFromSCC(graph, sCC);
//                g.printGraph();
//            }

//            graph.printSCCs();

            System.out.println("\n==================== Print minimum Feedback Vertex Set ============================");
            List<Node> minimumFVS = minFVS(graph);
            System.out.print(">> List: ");
            for(Node node : minimumFVS) {
                System.out.print(node.getLabel() + " ");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * This method will return the final Feedback Vertex set
     * @param graph the original graph
     * @return the list of node which is in the FVS
     */
    public static List<Node> minFVS(Graph graph) {
        List<Node> F = new ArrayList<>();
        Node v;
        GraphUtils utils = new GraphUtils();

        // do reduction of original graph and re-update node map
        Graph reducedGraph = utils.DoReduction(graph, F);

        List<List<Integer>> sCCs = utils.DecomposeGraphtoSCCs(reducedGraph);

        for(List<Integer> sCC: sCCs) {
            Graph g = utils.createGraphFromSCC(reducedGraph, sCC);

            double[][] transition = utils.convertGraphToTransitionMatrix(g);
            int index = utils.maximumEntryofStnDist(transition);
            Node node = g.getNodes().get(index);

            F.add(node);

            // remove node and recur to find all suitable nodes inside sCC
            g.removeNode(node);
            F.addAll(minFVS(g));
        }

        return F;
    }
}
