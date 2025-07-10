package Model;
import Coord.CoordWithRotationAndIndex;

public class CuboidToFoldOn4D {


	public static final int NUM_NEIGHBOURS = 6;
	
	private boolean cellsUsed[];
	private int rotationPaperRelativeToCuboidFlatMap[];
	
	private CoordWithRotationAndIndex[][] neighbours;
	
	private int dimensions[] = new int[4];

	
	public CuboidToFoldOn4D(int a, int b, int c, int d) {

		neighbours = NeighbourGraphCreator.initNeighbourhood(a, b, c, d);
		
		cellsUsed = new boolean[Utils.getSurfaceVolume(a, b, c, d)];
		rotationPaperRelativeToCuboidFlatMap = new int[Utils.getSurfaceVolume(a, b, c, d)];
		
		for(int i=0; i<cellsUsed.length; i++) {
			cellsUsed[i] = false;
			rotationPaperRelativeToCuboidFlatMap[i] = -1;
		}
		
		dimensions[0] = a;
		dimensions[1] = b;
		dimensions[2] = c;
		dimensions[3] = d;
	}

	//For debug:
	public boolean[] getCellsUsed() {
		return cellsUsed;
	}

	//Create same cuboid, but remove state info:
	public CuboidToFoldOn4D(CuboidToFoldOn4D orig) {

		neighbours = orig.neighbours;
		
		cellsUsed = new boolean[orig.cellsUsed.length];
		rotationPaperRelativeToCuboidFlatMap = new int[orig.cellsUsed.length];
		
		for(int i=0; i<cellsUsed.length; i++) {
			cellsUsed[i] = false;
			rotationPaperRelativeToCuboidFlatMap[i] = -1;
		}
		
		dimensions = orig.dimensions;
	}
	
	//Get dimensions for symmetry-resolver functions:
	public int[] getDimensions() {
		return dimensions;
	}

	public void setCell(int index, int rotation) {
		if(cellsUsed[index]) {
			System.out.println("Error: Setting cell when a cell is already activated!");
			System.exit(1);
		}
		

		cellsUsed[index] = true;
		rotationPaperRelativeToCuboidFlatMap[index] = rotation;
	}
	
	public void removeCell(int index) {
		if(!cellsUsed[index]) {
			System.out.println("Error: removing cell when a cell is not activated!");
			System.exit(1);
		}
		
		cellsUsed[index] = false;
		rotationPaperRelativeToCuboidFlatMap[index] = -1;
	}
	
	public int getNumCellsToFill() {
		return cellsUsed.length;
	}
	
	public CoordWithRotationAndIndex[] getNeighbours(int cellIndex) {
		return neighbours[cellIndex];
	}
	
	public int getRotationPaperRelativeToMap(int cellIndex) {
		return rotationPaperRelativeToCuboidFlatMap[cellIndex];
	}
	
	public boolean isCellIndexUsed(int cellIndex) {
		return cellsUsed[cellIndex];
	}
	
	public void resetState() {
		for(int i=0; i<cellsUsed.length; i++) {
			cellsUsed[i] = false;
		}
	}

	public static void main(String args[]) {
		CuboidToFoldOn4D c = new CuboidToFoldOn4D(3, 4, 5, 6);
		//CuboidToFoldOn c = new CuboidToFoldOn(1, 1, 1);
	}

}
