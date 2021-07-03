package dev.project276.entity;

import java.util.Comparator;

/**
 * EntityComparator implementing the Comparator interface to facilitate a priority queue for pathfinding.
 * Given a source Entity, returns 1 if the LHS Entity is closer to the source,
 * returns 0 otherwise.
 */
public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        if(o1.getTotalDistance() > o2.getTotalDistance()) {
            return 1;
        }
        else {
            return 0;
        }
    }
}
