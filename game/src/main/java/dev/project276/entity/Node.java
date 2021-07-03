package dev.project276.entity;

/**
 * Node class for pathfinding with A* search.
 * Contains a (x,y) position in the gameSpace, a reference to a parent node,
 * the distance from the parent, heuristic distance to player, and total sum of these distances.
 *
 * Overrides Object.equals and Object.hashCode to facilitate use in PriorityQueue.
 */
public class Node {
    private int x = 1;
    private int y = 1;

    private int distance = Integer.MAX_VALUE;
    private int heuristic = Integer.MAX_VALUE;
    private int total = Integer.MAX_VALUE;
    private Node parent;

    public Node() {
    }

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getDistance() { return distance; }
    public int getHeuristic() { return heuristic; }
    public int getTotalDistance() { return distance + heuristic; }
    public Node getParent() { return parent; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setDistance(int distance) { this.distance = distance; }
    public void setHeuristic(int heuristic) { this.heuristic = heuristic; }
    public void setTotalDistance() { total = distance + heuristic; }
    public void setParent(Node parent) { this.parent = parent; }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }

        if(this.getClass() != obj.getClass()) {
            return false;
        }

        Node node = (Node) obj;
        return x == node.getX() && y == node.getY();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + x;
        result = 31 * result + y;

        return result;
    }

}
