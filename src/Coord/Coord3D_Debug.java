package Coord;

public class Coord3D_Debug extends Coord3D{

	public int debugIndex;
	public int debugDir1;
	public int debugDir2;
	
	public Coord3D_Debug(int i, int j, int k, int debugIndex, int debugDir1, int debugDir2) {
		super(i, j, k);
		this.debugIndex = debugIndex;
		this.debugDir1 = debugDir1;
		this.debugDir2 = debugDir2;
	}
		
		
}
