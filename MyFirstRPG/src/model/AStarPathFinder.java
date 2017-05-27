package model;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;

public class AStarPathFinder {

	private HashSet<Integer> walkableTiles;
	private int[][] map;

	private LinkedList<PathFindingPoint> open;
	private LinkedList<PathFindingPoint> closed;
	
	public AStarPathFinder(HashSet<Integer> walkableTiles, int[][] map) {
		this.map = map;
		this.walkableTiles = walkableTiles;
	}
	
	public LinkedList<Point> findPath(int startX, int startY, int targetX, int targetY){

		/* create open and closed lists */
		open = new LinkedList<>();
		closed = new LinkedList<>();
		
		boolean done = false;
		
		PathFindingPoint current = new PathFindingPoint(startX, startY);
		closed.add(current);
		
		while(!done) {
			
			/* check if target is reached */
			if(current.x == targetX && current.y == targetY) {
				return backTrack();
			}
			
			/* get adjacent points for current point */
			LinkedList<PathFindingPoint> adjacentToCurrent = getAdjacentWalkablePoints(startX, startY, targetX, targetY, current.x, current.y);
			
			/* add them to open list */
			for (PathFindingPoint p : adjacentToCurrent) {
				open.add(p);
			}
			
			/* get point with lowest fscore from open list */
			PathFindingPoint lowest = getPointWithLowestFScore(open);
			closed.add(lowest);
			removePointFromOpenList(lowest);
			current = lowest;
			
			/* check if open list is empty -> not path exists */
			if(open.isEmpty()) {
				return null;
			}
			
		}
		
		return null;
	}
	
	private void removePointFromOpenList(PathFindingPoint toRemove) {
		PathFindingPoint out = null;
		for(PathFindingPoint p : open) {
			if(p.x == toRemove.x && p.y == toRemove.y) {
				out = p;
				break;
			}
		}
		if(out != null) {
			open.remove(out);
		}
	}
	
	private PathFindingPoint getPointInDirection(int x, int y, int dir) {
		if(dir == 1) {
			y -= 1;
		}
		if(dir == 2) {
			x -= 1;
		}
		if(dir == 3) {
			y += 1;
		}
		if(dir == 4) {
			x += 1;
		}
		PathFindingPoint out = null;
		for(PathFindingPoint p : closed) {
			if(p.x == x && p.y == y) {
				out = p;
				break;
			}
		}
		closed.remove(out);
		return out;
	}
	
	private boolean contains(PathFindingPoint toCheck, LinkedList<PathFindingPoint> list) {
		for(PathFindingPoint p : list) {
			if(p.x == toCheck.x && p.y == toCheck.y) {
				return true;
			}
		}
		return false;
	}
	
	private LinkedList<Point> backTrack(){
		LinkedList<Point> out = new LinkedList<>();
		
		PathFindingPoint current = closed.getLast();
		closed.removeLast();
		out.add(current);
		
		while(current.fScore > 0) {
			current = getPointInDirection(current.x, current.y, current.parent);
			out.add(current);
		}
		
//		Collections.reverse(out);
		return out;
	}
	
	@SuppressWarnings("unused")
	private boolean isAdjacent(PathFindingPoint current, PathFindingPoint toCheck) {
		return (Math.abs(current.x - toCheck.x) <= 1 && Math.abs(current.y - toCheck.y) <= 1);
	}
	
	private LinkedList<PathFindingPoint> getAdjacentWalkablePoints(int startX, int startY, int targetX, int targetY, int currentX, int currentY){
		LinkedList<PathFindingPoint> out = new LinkedList<>();
		
		for (int i = -1; i <= 1; i++) {
			if(i == 0) {
				continue;
			}
			int x1 = currentX+i;
			int y1 = currentY;
			int x2 = currentX;
			int y2 = currentY+i;
			if(checkPoint(x1, y1)) {
				int fScore1 = calculateFScore(startX, startY, targetX, targetY, x1, y1);
				out.add(new PathFindingPoint(x1, y1, fScore1, getParent(x1, y1, currentX, currentY)));
			}
			if(checkPoint(x2, y2)) {
				int fScore2 = calculateFScore(startX, startY, targetX, targetY, x2, y2);
				out.add(new PathFindingPoint(x2, y2, fScore2, getParent(x2, y2, currentX, currentY)));
			}
		}
		
		return out;
	}
	
	private boolean checkPoint(int x, int y) {
		if(x < 0 || y < 0 || x >= map.length || y >= map[0].length) {
			return false;
		}
		if(contains(new PathFindingPoint(x, y), closed)) {
			return false;
		}
		if(contains(new PathFindingPoint(x, y), open)) {
			return false;
		}
		if(walkableTiles.contains(map[x][y])) {
			return true;
		}
		return false;
	}
	
	private int getParent(int currentX, int currentY, int oldX, int oldY) {
		if(currentX > oldX) {
			return 2;
		}
		if(currentX < oldX) {
			return 4;
		}
		if(currentY > oldY) {
			return 1;
		}
		if(currentY < oldY) {
			return 3;
		}
		return 0;
	}
	
	private PathFindingPoint getPointWithLowestFScore(LinkedList<PathFindingPoint> list) {
		int lowest = Integer.MAX_VALUE;
		PathFindingPoint out = null;
		for(PathFindingPoint p : list) {
			if(p.fScore < lowest) {
				lowest = p.fScore;
				out = p;
			}
		}
		return out;
	}
	
	private int calculateFScore(int startX, int startY, int targetX, int targetY, int currentX, int currentY) {
		return calculateGScore(startX, startY, currentX, currentY)+calculateHScore(targetX, targetY, currentX, currentY);
	}
	
	private int calculateGScore(int startX, int startY, int currentX, int currentY) {
		return Math.abs(startX-currentX)+Math.abs(startY-currentY);
	}
	
	private int calculateHScore(int targetX, int targetY, int currentX, int currentY) {
		return Math.abs(targetX-currentX)+Math.abs(targetY-currentY);
	}
	
	public class PathFindingPoint extends Point{
		
		private static final long serialVersionUID = 1L;
		
		public int fScore;
		/* 1: left, 2: up, 3: right, 4:down */
		public int parent;
		
		public PathFindingPoint(int x, int y) {
			super(x, y);
		}
		
		public PathFindingPoint(int x, int y, int fScore, int parent) {
			super(x, y);
			this.fScore = fScore;
			this.parent = parent;
		}
		
	}
	
}
