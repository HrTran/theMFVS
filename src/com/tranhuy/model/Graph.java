package com.tranhuy.model;

import java.util.*;

/**
 * Create by huytt99 on 08/04/2020
 */
public class Graph implements Cloneable {
    private int V;
    private List<Node> nodes;
    private Map<Integer, List<String>> deletion; // use when check redundant in FVS
    private Map<Integer, Integer> nodeMap;

    public Graph(int V)
    {
        this.V = V;
        nodes = new ArrayList<>(V);
        deletion = new HashMap<>();
        nodeMap = new HashMap<>();

        for (int i = 0; i < V; i++)
            nodes.add(new Node(i));
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public int getV() {
        return V;
    }

    public void setV(int v) {
        V = v;
    }

    public Map<Integer, Integer> getNodeMap() {
        return nodeMap;
    }

    public Graph clone() {
        Object clone = null;

        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return (Graph) clone;
    }

    public void updateMap() {
        for(int i = 0; i < V; i++) {
            Node node = nodes.get(i);
            nodeMap.put(node.getLabel(), i);
        }
    }

    private int getNodeIndex(int label) {
        for(int i = 0; i < V; i++) {
            if(nodes.get(i).getLabel() == label) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Returns true if the graph contains a
     * cycle, else false.
     * This function is a variation of DFS() in
     * https://www.geeksforgeeks.org/archives/18212
     * @return true if graph has at least a cyclic.
     */
    private boolean isCyclicUtil(int i, boolean[] visited,
                                 boolean[] recStack)
    {

        // Mark the current node as visited and
        // part of recursion stack
        if (recStack[i])
            return true;

        if (visited[i])
            return false;

        visited[i] = true;

        recStack[i] = true;
        List<Integer> children = nodes.get(i).getAdjacents();

        int index = -1;
        for (int c: children) {
            index = nodeMap.get(c);

            if (index != -1 && isCyclicUtil(index, visited, recStack))
                return true;
        }

        recStack[i] = false;

        return false;
    }

    public void addEdge(int source, int dest) throws Exception {
        Node sourceNode = nodes.get(nodeMap.get(source));
        Node destNode = nodes.get(nodeMap.get(dest));

        if(sourceNode != null && destNode != null) {
            sourceNode.getAdjacents().add(dest);
            sourceNode.setOutDegree(sourceNode.getOutDegree() + 1);

            destNode.setInDegree(destNode.getInDegree() + 1);

        } else {
            throw new Exception(String.format("Cannot add edge %d - %d", source, dest));
        }

    }


    /**
     * Returns true if the graph contains a
     * cycle, else false.
     * This function is a variation of DFS() in
     * https://www.geeksforgeeks.org/archives/18212
     * @return true if graph has at least a cyclic.
     */
    public boolean isCyclic()
    {

        // Mark all the vertices as not visited and
        // not part of recursion stack
        boolean[] visited = new boolean[V];
        boolean[] recStack = new boolean[V];


        // Call the recursive helper function to
        // detect cycle in different DFS trees
        for (int i = 0; i < V; i++)
            if (isCyclicUtil(i, visited, recStack))
                return true;

        return false;
    }


    /**
     * remove node out of graph
     * keep record of deletion for reverse later
     * if removed node has indegree = 1 or outdegree = 1,
     * connect its predecessor(s) with its successor(s)
     * @param removeNode the node will be removed from graph, obviously
     */
    public void removeNode(Node removeNode) {
        int label = removeNode.getLabel();
        deletion.put(label, new ArrayList<>());

        Node successor = null;
        Node predecessor = null;

        removeNodeFromSuccessor(removeNode, successor, predecessor, label);
        removeNodeUpdateGraph(removeNode);
        removeNodeFromPredecessor(removeNode, successor, label);
    }

    public void removeNode(int label) {
        Node removeNode = nodes.get(nodeMap.get(label));
        deletion.put(label, new ArrayList<>());

        Node successor = null;
        Node predecessor = null;

        removeNodeFromSuccessor(removeNode, successor, predecessor, label);
        removeNodeUpdateGraph(removeNode);
        removeNodeFromPredecessor(removeNode, successor, label);
    }

    public void removeNodeFromSuccessor(Node removeNode, Node predecessor, Node successor, int label) {
        if(removeNode.getInDegree() == 1) {
            for(Node node : nodes) {
                // node which has arc pointed to removeNode
                if (node.getAdjacents().contains(label)) {
                    predecessor = node;
                }
            }
        }

        // reduce indegree from successor
        for(int i = 0; i < removeNode.getOutDegree(); i++) {
            int successorLabel = removeNode.getAdjacents().get(i);
            int index = nodeMap.get(successorLabel);

            if(index != -1) {
                successor = nodes.get(index);
                successor.setInDegree(successor.getInDegree() - 1);
                deletion.get(label).add(label + "-" + successorLabel);
            }

            // if removeNode has only 1 predecessor
            // all successor of it become adjacent nodes of its predecessor
            if(removeNode.getInDegree() == 1 && predecessor != null) {
                predecessor.getAdjacents().add(successorLabel);
                predecessor.setOutDegree(predecessor.getOutDegree() + 1);
                successor.setInDegree(successor.getInDegree() + 1);
            }
        }
    }

    public void removeNodeFromPredecessor(Node removeNode, Node successor, int label) {
        for(Node node : nodes) {
            // node which has arc pointed to removeNode
            if(node.getAdjacents().contains(label)) {

                for(int i = 0; i < node.getOutDegree(); i++) {
                    if(node.getAdjacents().get(i) == label) {
                        node.getAdjacents().remove(i);
                        deletion.get(label).add(node.getLabel() + "-" + label);
                        break;
                    }
                }

                node.setOutDegree(node.getOutDegree() - 1);

                // if removeNode has only 1 successor
                // all predecessor of it become predecessors of its successor
                // e.g: (v -> u -> (a, b)) -> ((v -> a), (v -> b))
                if(removeNode.getOutDegree() == 1 && successor != null) {
                    node.getAdjacents().add(successor.getLabel());
                    node.setOutDegree(node.getOutDegree() + 1);
                }

            }
        }
    }

    public void removeNodeUpdateGraph(Node removeNode) {
        nodes.remove(removeNode);
        V = V - 1;
        updateMap();
    }

    public void printGraph() {
        System.out.println("=================== Print Graph ======================");
        for(int i = 0; i < V; i++) {
            Node node = nodes.get(i);
            System.out.print(node.getLabel() + ": ");
            for(int j = 0; j < node.getAdjacents().size(); j++) {
                if(j != node.getAdjacents().size() - 1)
                    System.out.print(node.getAdjacents().get(j) + " - ");
                else
                    System.out.print(node.getAdjacents().get(j));
            }
            System.out.print("\n");

        }
    }



    /**
     *  A recursive function to print DFS starting from v
     *  https://www.geeksforgeeks.org/strongly-connected-components/
     * */
    public void DFSUtil(int v, boolean visited[], List<Integer> sCC)
    {
        // Mark the current node as visited and print it
        visited[v] = true;

        // if this call is for DecomposeGraphtoSCCs(), add v to sCC
        // if this call is for printSSC(), print v
        if(sCC != null) sCC.add(nodes.get(v).getLabel());
        else System.out.print(nodes.get(v).getLabel() + " ");

        int n;
        // Recur for all the vertices adjacent to this vertex
        Iterator<Integer> i = nodes.get(v).getAdjacents().iterator();
        while (i.hasNext())
        {
            n = i.next();
            int index = nodeMap.get(n);
            if (!visited[index])
                DFSUtil(index,visited, sCC);
        }
    }

    /**
     * This method will return reverse (or transpose) of this graph
     * @return the graph which is transposed
     */
    public Graph getTranspose()
    {
        Graph g = new Graph(V);
        copyLabel(g, this);
        g.updateMap();
        for (int v = 0; v < V; v++)
        {
            // Recur for all the vertices adjacent to this vertex
            Node currentNode = nodes.get(v);
            Iterator<Integer> i = currentNode.getAdjacents().listIterator();
            while(i.hasNext()){
                try {
                    g.addEdge(i.next(), currentNode.getLabel());
                } catch (Exception e) {
                    System.out.println("getTranspose(): " + e.getMessage());
                }
            }

        }

        g.updateMap();
        return g;
    }

    /**
     * This method copy node's label from this graph to another one
     * @param source the original graph
     * @param dest the destination graph, which is a copy version of the original graph
     */
    public void copyLabel(Graph dest, Graph source) {
        for(int i = 0; i < source.nodes.size(); i++) {
            dest.nodes.get(i).setLabel(source.nodes.get(i).getLabel());
        }
    }

    /**
     * This method fill node in order of strongly connected components to stack
     * @param v the index of current node
     * @param visited the array checks whether if a node is visited or not
     * @param stack the stack, of course
     */
    public void fillOrder(int v, boolean visited[], Stack stack)
    {
        // Mark the current node as visited and print it
        visited[v] = true;

        // Recur for all the vertices adjacent to this vertex
        Iterator<Integer> i = nodes.get(v).getAdjacents().iterator();
        while (i.hasNext())
        {
            int n = i.next();
            int index = nodeMap.get(n);
            if(!visited[index])
                fillOrder(index, visited, stack);
        }

        // All vertices reachable from v are processed by now,
        // push v to Stack
        stack.push(nodes.get(v).getLabel());
    }


    /**
     * The main function that finds and prints all strongly connected components
     * First DFS to put vertexes's labels into stack in order of SCC
     * Second DFS to mark visited for all vertexes which is in SCC
     * if vertex is not in the last SCC, it will remain false and get DFS again
     */
    public void printSCCs()
    {
        Stack stack = new Stack();

        // Mark all the vertices as not visited (For first DFS)
        boolean visited[] = new boolean[V];
        for(int i = 0; i < V; i++)
            visited[i] = false;

        // Fill vertices in stack according to their finishing times
        for (int i = 0; i < V; i++)
            if (visited[i] == false)
                fillOrder(i, visited, stack);

        // Create a reversed graph
        Graph gr = getTranspose();
        gr.updateMap();

        // Mark all the vertices as not visited (For second DFS)
        for (int i = 0; i < V; i++)
            visited[i] = false;

        // Now process all vertices in order defined by Stack
        while (stack.empty() == false)
        {
            // Pop a vertex from stack
            int v = (int)stack.pop();
            int index = nodeMap.get(v);

            // Print Strongly connected component of the popped vertex
            if (visited[index] == false)
            {
                gr.DFSUtil(index, visited, null);
                System.out.println();
            }
        }
    }

}
