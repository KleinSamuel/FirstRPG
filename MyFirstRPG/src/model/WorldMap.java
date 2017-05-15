package model;

public class WorldMap {

	public boolean[][] walkableMap;
	
	public WorldMap() {
		walkableMap = new boolean[15][15];
		for (int i = 1; i < walkableMap.length-1; i++) {
			for (int j = 1; j < walkableMap[i].length-1; j++) {
				walkableMap[i][j] = true;
			}
		}
	}
	
	public boolean[][] getPartialWalkableMap(int x, int y, int width, int height){
		
		boolean[][] out = new boolean[height][width];
		
		int out_i = 0;
		int out_j = 0;
		for (int i = y-(height/2); i <= y+(height/2) ; i++) {
			out_j = 0;
			for (int j = x-(width/2); j <= x+(width/2); j++) {
				out[out_i][out_j] = walkableMap[i][j];
				out_j++;
			}
			out_i++;
		}
		
		return out;
	}
	
	public void printWalkableMap(boolean[][] map) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if(map[i][j]) {
					System.out.print("O ");
				}else {
					System.out.print("X ");
				}
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		
		WorldMap wm = new WorldMap();
		wm.printWalkableMap(wm.walkableMap);
		
	}
	
}
