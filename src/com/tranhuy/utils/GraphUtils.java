package com.tranhuy.utils;

import Jama.Matrix;
import com.tranhuy.model.Graph;
import com.tranhuy.model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Create by huytt99 on 08/04/2020
 */
public class GraphUtils {

    /**
     * Remove node with indegree or outdegree is 0 or 1
     * Remove node which has edge come to its own (v, v)
     * @param graph the original graph
     * @param feedbackVS the Feedback Vertex Set
     * @return new graph with nodes which has high probability to add in FVS
     */
    public Graph DoReduction(Graph graph, List<Node> feedbackVS) {
        Graph reducedGraph = graph.clone();
        List<Node> removeList = new ArrayList<>();

        for(Node node: reducedGraph.getNodes()) {
            if(checkLoopVertex(node)) {
                removeList.add(node);
                feedbackVS.add(node);
            }
        }

        for(Node node: reducedGraph.getNodes()) {
            if(node.getOutDegree() <= 1 || node.getInDegree() <= 1) {
                removeList.add(node);
            }
        }

        for(Node node: removeList) {
            reducedGraph.removeNode(node.getLabel());
        }
        reducedGraph.setV(reducedGraph.getNodes().size());

        return reducedGraph;
    }

    /**
     * This method create the transition matrix P, where Pij = 1/ outdegree(i)
     * @param graph the original graph
     * @return transitionMatrix
     */
    // The transition matrix P where Pij = 1/ outdegree(i)
    public double[][] convertGraphToTransitionMatrix(Graph graph) {
        int V = graph.getV();
        double[][] transitionMatrx = new double[V][V];

        // update NodeMap <label, index>
        graph.updateMap();
        Map<Integer, Integer> nodeMap = graph.getNodeMap();

        for(int i = 0; i < V; i++) {
            Node node = graph.getNodes().get(i);
            int outdegree = node.getOutDegree();
            List<Integer> dests = node.getAdjacents();

            for(int dest: node.getAdjacents()) {
                int index = nodeMap.get(dest);
                transitionMatrx[i][index] = 1/outdegree;
            }
        }

        return transitionMatrx;
    }

    /**
     * This method find maximum entry of Stationary Distribution
     * @param transMatrx the transition matrix
     * @return index of node which has maximum distribution
     */
    public int maximumEntryofStnDist(double[][] transMatrx) {
        int N = transMatrx.length;
        double max = 0.0D;
        int index = 0;

        Matrix x = new MatrixUtils().computeByLinearMethod(transMatrx, N);

        int b = 0;
        for(int i = 0 ; i < x.getRowDimension(); i++) {
            for(int j = 0 ; j < x.getColumnDimension(); j++) {
                if( x.get(i, j) >= max) {
                    max = x.get(i, j);
                    index = i; b = j;
                }
            }
        }

        return index;
    }

    /**
     * This method check if whether vertex points to itself or not
     * @param node the node is used to check
     * @return true if it is loop and vice versa
     */
    public boolean checkLoopVertex(Node node) {
        if(node.getAdjacents().contains(node.getLabel())) {
            return true;
        }
        return false;
    }

    /**
     * This method will return the list of node which will be used to re-create a graph later
     * @param graph the original graph
     * @return the list of list of labels
     */
    public List<List<Integer>> DecomposeGraphtoSCCs(Graph graph) {
        List<List<Integer>> sCCs = new ArrayList<>();

        Stack stack = new Stack();
        int V = graph.getV();

        // Mark all the vertices as not visited (For first DFS)
        boolean visited[] = new boolean[V];
        for(int i = 0; i < V; i++)
            visited[i] = false;

        // Fill vertices in stack according to their finishing times
        for (int i = 0; i < V; i++)
            if (visited[i] == false)
                graph.fillOrder(i, visited, stack);

        // Create a transposed graph
        Graph gr = graph.getTranspose();
        gr.updateMap();

        // Mark all the vertices as not visited (For second DFS)
        for (int i = 0; i < V; i++)
            visited[i] = false;

        // Now process all vertices in order defined by Stack
        while (stack.empty() == false)
        {
            // Pop a vertex from stack
            int v = (int)stack.pop();
            int index = gr.getNodeMap().get(v);

            // Find all Strongly Connected Component begin from the popped vertex
            if (visited[index] == false)
            {
                List<Integer> sCC = new ArrayList<>();
                gr.DFSUtil(index, visited, sCC);

                // if sCC is not null
                if(sCC.size() > 0) {
                    sCCs.add(sCC);
                }
            }
        }

        return sCCs;
    }

    /**
     * Given list node label
     * create a subgraph from original graph
     * @param original the original graph
     * @param sCC the list of strongly connected component
     * @return the graph made of strongly connected component
     */
    public Graph createGraphFromSCC(Graph original, List<Integer> sCC) {
        Graph graph = new Graph(sCC.size());

        // add node's label
        for(int i = 0; i < sCC.size(); i++) {
            Node node = graph.getNodes().get(i);
            node.setLabel(sCC.get(i));
        }

        // add egdes
        // which src and dest are in the SCC
        for(Node node: original.getNodes()) {
            int src = node.getLabel();
            if(sCC.contains(src)) {
                for(int dest : node.getAdjacents()) {
                    if(sCC.contains(dest)) {
                        try {
                            graph.addEdge(src, dest);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }

        return graph;
    }
}
