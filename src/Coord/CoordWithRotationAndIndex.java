package Coord;

public class CoordWithRotationAndIndex {

	//0: none
	//1: clockwise 90
	//2: opposite dir
	//3: counter-clockwise 90 
	//4: up
	//5: down
	
	private int rot;
	private int index;
	
	public CoordWithRotationAndIndex(int rot, int index) {
		this.rot = rot;
		this.index = index;
	}

	public int getRot() {
		return rot;
	}

	public int getIndex() {
		return index;
	}
	
}
