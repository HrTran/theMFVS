package com.tranhuy.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Create by huytt99 on 08/04/2020
 */
public class Node {
    private List<Integer> adjacents;
    private int inDegree;
    private int outDegree;
    private int label;

    public Node(int label) {
        this.label = label;
        this.inDegree = 0;
        this.outDegree = 0;
        adjacents = new LinkedList<>();
    }

    public List<Integer> getAdjacents() {
        return adjacents;
    }

    public int getInDegree() {
        return inDegree;
    }

    public void setInDegree(int inDegree) {
        this.inDegree = inDegree;
    }

    public int getOutDegree() {
        return outDegree;
    }

    public void setOutDegree(int outDegree) {
        this.outDegree = outDegree;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
