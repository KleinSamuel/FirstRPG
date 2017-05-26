package client.gui;

import java.awt.Point;
import java.util.LinkedList;

public class Path {

	public LinkedList<Point> pathPoints;
	
	public Path() {
		this.pathPoints = new LinkedList<>();
	}
	
	public void removeAllPointsExceptForCurrent() {
		if(pathPoints.size() > 0) {
			Point retain = pathPoints.getLast();
			pathPoints.clear();
			pathPoints.add(retain);
		}
	}
	
}
