package Model;
import Coord.CoordWithRotationAndIndex;
import Coord.Neighbour3DDesc;

public class CuboidToFoldOn4D {


	//Constants:
	public static final int NUM_NEIGHBOURS = 6;

	public static final int neighbourIndexToUse[][][] = NeighbourGraphCreator.setupNeighToUse();
	public static final int glabalAxisIndexToUse[][][] = NeighbourGraphCreator.setupAxisToUse();
	
	private Neighbour3DDesc[][] neighbours;
	
	public static final int NUM_AXIS = 3;
	public static final int NUM_MULT_PI_OVER_4 = 4;
	
	//TODO: Even if this is right, is it useful?
	public static int getNewCellDirAfterRotateByAxis[][][] = setupNewCellDirAfterRotateByAxis();
	
	public static int[][][] setupNewCellDirAfterRotateByAxis() {
		int newCellDirAfterRotateByAxisSetup[][][] = new int[NUM_NEIGHBOURS][NUM_MULT_PI_OVER_4][NUM_NEIGHBOURS];
		for(int i=0; i<NUM_AXIS; i++) {
			for(int j=0; j<NUM_MULT_PI_OVER_4; j++) {
				for(int k=0; k<NUM_NEIGHBOURS; k++) {
					newCellDirAfterRotateByAxisSetup[i][j][k] = setupGetNewCellDirAfterRotateByAxis(i, j, k);
				}
			}
		}
		
		//Axis 3,4,5 are just 0,1,2, but in the reverse direction:
		for(int i=0; i<NUM_AXIS; i++) {
			for(int j=0; j<NUM_MULT_PI_OVER_4; j++) {
				for(int k=0; k<NUM_NEIGHBOURS; k++) {
					newCellDirAfterRotateByAxisSetup[NUM_AXIS + i][j][k] = setupGetNewCellDirAfterRotateByAxis(i, (NUM_MULT_PI_OVER_4 - j) % NUM_MULT_PI_OVER_4, k);
				}
			}
		}

		System.out.println("Done setup?");
		return newCellDirAfterRotateByAxisSetup;
	}
	
	//I have to think...
	public static int setupGetNewCellDirAfterRotateByAxis(int axisIndex, int amountMultPIover4, int origCellDir) {

		//No need to change direction because rotation is 0:
		if(amountMultPIover4 == 0) {
			return origCellDir;
			
		}

		//No need to change direction because rotation axis on origCellDir:
		if(origCellDir % NUM_AXIS == axisIndex) {
			return origCellDir;
		}
		
		//Flip 180 degrees:
		if(amountMultPIover4 == 2) {
			return (origCellDir + NUM_AXIS) % NUM_NEIGHBOURS;
			
		}
		
		int cycle[] = null;
		if(axisIndex == 0) {
			cycle = new int[] {1, 2, 4, 5};
		} else if(axisIndex == 1) {

			cycle = new int[] {0, 5, 3, 2};
		} else if(axisIndex == 2) {

			cycle = new int[] {0, 1, 3, 4};
		} else {
			System.exit(1);
		}
		
		for(int i=0; i<cycle.length; i++) {
			if(cycle[i] == origCellDir) {
				return cycle[(i + amountMultPIover4) % 4];
			}
		}
		
		System.out.println("ERROR: We should have been able to return a value at this point.");
		System.exit(1);
		
		return -1;
	}
	

	//State variables:

	private boolean cellsUsed[];
	//Which of the 6 directions the dir 0 for the model points to in the block view (i axis +)
	private int cellDir1[];
	//Which of the 6 directions the dir 1 for the model points to in the block view  (j axis +)
	private int cellDir2[];
	
	
	private int dimensions[] = new int[4];
	
	public CuboidToFoldOn4D(int a, int b, int c, int d) {

		neighbours = NeighbourGraphCreator.initNeighbourhood(a, b, c, d);
		
		cellsUsed = new boolean[Utils.getSurfaceVolume(a, b, c, d)];
		cellDir1 = new int[Utils.getSurfaceVolume(a, b, c, d)];
		cellDir2 = new int[Utils.getSurfaceVolume(a, b, c, d)];
		
		for(int i=0; i<cellsUsed.length; i++) {
			cellsUsed[i] = false;
			cellDir1[i] = -1;
			cellDir2[i] = -1;
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

	public void clearState() {
		for(int i=0; i<cellsUsed.length; i++) {
			this.cellsUsed[i] = false;
		}
	}
	
	//Create same cuboid, but remove state info:
	public CuboidToFoldOn4D(CuboidToFoldOn4D orig) {

		neighbours = orig.neighbours;
		
		cellsUsed = new boolean[orig.cellsUsed.length];

		cellDir1 = new int[orig.cellsUsed.length];
		cellDir2 = new int[orig.cellsUsed.length];
		
		for(int i=0; i<cellsUsed.length; i++) {
			cellsUsed[i] = false;
			cellDir1[i] = -1;
			cellDir2[2] = -1;
		}
		dimensions = orig.dimensions;
	}
	
	//Get dimensions for symmetry-resolver functions:
	public int[] getDimensions() {
		return dimensions;
	}
	
	public void initializeFirstCell(int index, int initialCellDir1, int initialCellDir2) {
		for(int i=0; i<cellsUsed.length; i++) {
			if(cellsUsed[i]) {
				System.out.println("ERROR: trying to call initializeFirstCell when there's already a cell used.");
				System.exit(1);
			}
		}
		
		if((initialCellDir1 - initialCellDir2) % (NUM_NEIGHBOURS / 2) == 0) {
			//(I setup the directions so that if the diff is 0 or 3, the directions go along the same axis)
			System.out.println("ERROR: trying to call initializeFirstCell when intialialCellDir1 and 2 are not orthogonal.");
			System.exit(1);
			
		}
		
		cellsUsed[index] = true;
		this.cellDir1[index] = initialCellDir1;
		this.cellDir2[index] = initialCellDir2;
		
	}
	
	public boolean couldAttachCell(int origIndex, int blockIndex) {
		int modelAttachmentIndex0To5 = neighbourIndexToUse[blockIndex][cellDir1[origIndex]][cellDir2[origIndex]];
		int newCellIndex = neighbours[origIndex][modelAttachmentIndex0To5].cellIndex;
		
		if(!cellsUsed[origIndex]) {
			System.out.println("Error: attaching cell when the cell it attaches from is not activated! (CouldattachCell)");
			System.exit(1);
		}
		
		return ! cellsUsed[newCellIndex];
	}
	
	//TODO: reeval: neighbourIndexToUse.
	public int getNeighbourIndex(int origIndex, int blockIndex) {
		int nextIndex0To5 = neighbourIndexToUse[blockIndex][cellDir1[origIndex]][cellDir2[origIndex]];
		int newCellIndex = neighbours[origIndex][nextIndex0To5].cellIndex;
		
		return newCellIndex;
	}
	
	public static final int LOCAL_DIR_1 = 0;
	public static final int LOCAL_DIR_2 = 1;
	
	public static int debug_it = 0;
	
	public int debug_get_num_cells() {
		int ret = 0;
		for(int i=0; i<this.cellsUsed.length; i++) {
			if(this.cellsUsed[i]) {
				ret++;
			}
		}
		return ret;
	}
	
	/*
	 * 
Solution:

Solution 1392:
_____   _____   _____   
_____   2,5,4   _____   
_____   7,1,5*   3,5,0   *: bad 2nd rotation? Shouldn't 4, 7, 2 be lined up? Should be 7, 2, 0? or 7,1,2?



1,3,1   _____   _____   
5,0,4   _____   _____   
6,3,1   4,1,0   _____   



_____   _____   _____   
0,0,1   _____   _____   
_____   _____   _____   


	 */

	public void attachCell(int origIndex, int blockIndex) {
		
		int modelAttachmentIndex0To5 = neighbourIndexToUse[blockIndex][cellDir1[origIndex]][cellDir2[origIndex]];
		int newCellIndex = neighbours[origIndex][modelAttachmentIndex0To5].cellIndex;
		
		//if(debug_get_num_cells() == 7 && modelAttachmentIndex0To5 != blockIndex && blockIndex != 0) {
		
		//if(debug_get_num_cells() == 7 && modelAttachmentIndex0To5 != blockIndex) {
		//if(origIndex == 6 && blockIndex == 1 && newCellIndex == 4 && cellDir1[origIndex] == 3 && cellDir2[origIndex] == 1) {
		/*if(origIndex == 6 && blockIndex == 1 && newCellIndex == 4 && cellDir1[origIndex] == 2 && cellDir2[origIndex] == 1) {
			System.out.println("----------");
			System.out.println("Test connection:");
			System.out.println("origIndex: " + origIndex);
			System.out.println("Block Index: " + blockIndex);
			System.out.println();
			System.out.println("cellDir1[origIndex]: " + cellDir1[origIndex]);
			System.out.println("cellDir2[origIndex]: " + cellDir2[origIndex]);
			System.out.println();
			System.out.println("newCellIndex: " + newCellIndex);
			
			System.out.println("modelAttachmentIndex0To5: " + modelAttachmentIndex0To5);
			//System.exit(1);
		}*/
		
		if(!cellsUsed[origIndex]) {
			System.out.println("Error: adding cell when the cell it attaches from is not activated! (attachCell 3)");
			System.exit(1);
		}
		
		if(cellsUsed[newCellIndex]) {
			System.out.println("Error: Setting cell when a cell is already activated!");
			System.exit(1);
		}

		cellsUsed[newCellIndex] = true;
		
	//TODO: this does not work! Rewrite it!
		int localRotationAxisIndex = neighbours[origIndex][modelAttachmentIndex0To5].rotAxis;
		
		int localCellDir1 = getNewCellDirAfterRotateByAxis
				[localRotationAxisIndex]
				[neighbours[origIndex][modelAttachmentIndex0To5].rotAmount]
				[LOCAL_DIR_1];
		
		int localCellDir2 = getNewCellDirAfterRotateByAxis
				[localRotationAxisIndex]
				[neighbours[origIndex][modelAttachmentIndex0To5].rotAmount]
				[LOCAL_DIR_2];
		
			//TODO: accept 6 rotation axises. 3 4, 5 are just the negative dir.
		this.cellDir1[newCellIndex] = glabalAxisIndexToUse[localCellDir1][this.cellDir1[origIndex]][this.cellDir2[origIndex]];
		this.cellDir2[newCellIndex] = glabalAxisIndexToUse[localCellDir2][this.cellDir1[origIndex]][this.cellDir2[origIndex]];
		
		//DEBUG
		/*if(this.cellDir1[newCellIndex] != localCellDir1 
		//&& debug_it == 1
		) {
			System.out.println("Test connection:");
			System.out.println("origIndex: " + origIndex);
			System.out.println("newCellIndex: " + newCellIndex);
			System.out.println();
			System.out.println("localRotationAxisIndex: " + localRotationAxisIndex);
			System.out.println("localRotationAmount: " + neighbours[origIndex][modelAttachmentIndex0To5].rotAmount);
			System.out.println("modelAttachmentIndex0To5: " + modelAttachmentIndex0To5);
			System.out.println("Local: " + localCellDir1 + ", " + localCellDir2);
			System.out.println("Global: " + this.cellDir1[newCellIndex] + ", " + this.cellDir2[newCellIndex]);
			System.exit(1);
		}*/
		debug_it++;
		
	//END TODO: this does not work! Rewrite it!
		
		if((this.cellDir1[newCellIndex] - this.cellDir2[newCellIndex] % 3) == 0) {
			System.out.println("ERROR in attachCell: the cellDirections got aligned somehow!");
			System.exit(1);
		}
	}
	
	public int debugGetCellDir1(int cellIndex) {
		return this.cellDir1[cellIndex];
	}
	
	public int debugGetCellDir2(int cellIndex) {
		return this.cellDir2[cellIndex];
	}
	
	public void removeCellAttachment(int origIndex, int blockIndex) {

		int modelAttachmentIndex0To5 = neighbourIndexToUse[blockIndex][cellDir1[origIndex]][cellDir2[origIndex]];
		int cellIndexRemove = neighbours[origIndex][modelAttachmentIndex0To5].cellIndex;

		if(!cellsUsed[origIndex]) {
			System.out.println("Error: removing cell when the cell it attaches from is not activated! (removeCellAttachment)");
			System.exit(1);
		}
		if(!cellsUsed[cellIndexRemove]) {
			System.out.println("Error: removing cell when a cell is not activated!");
			System.exit(1);
		}
		
		cellsUsed[cellIndexRemove] = false;
	}
	
	
	public int getNumCellsToFill() {
		return cellsUsed.length;
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
