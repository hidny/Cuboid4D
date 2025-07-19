package Coord;

public class Neighbour3DDesc {

	//Index 0 to Area(or 3D surface volume) - 1
	public int cellIndex = -1;
	
	// 0: i
	// 1: j
	// 2: k
	public int rotAxis;
	
	// 0: not rotation
	// 1: 90 degrees via left-hand curl where thumb is axis dir
	// 2: 180 degrees via left-hand curl where thumb is axis dir
	// 3: 270 degrees via left-hand curl where thumb is axis dir
	public int rotAmount;

	public Neighbour3DDesc(int cellIndex, int rotAxis, int rotAmount) {
		super();
		this.cellIndex = cellIndex;
		this.rotAxis = rotAxis;
		this.rotAmount = rotAmount;
	}

}
