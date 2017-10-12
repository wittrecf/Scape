package model;

import java.util.ArrayList;

public class Node {
	private int x;
	private int y;
	private int dist;
	private Node prev;
	private ArrayList<Node> connections;
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		dist = 999;
		prev = null;
		connections = new ArrayList<Node>();
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
		this.dist = dist;
	}

	public Node getPrev() {
		return prev;
	}

	public void setPrev(Node prev) {
		this.prev = prev;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public ArrayList<Node> getConnections() {
		return connections;
	}
	
	public void addConnection(Node n) {
		connections.add(n);
	}
	
}
