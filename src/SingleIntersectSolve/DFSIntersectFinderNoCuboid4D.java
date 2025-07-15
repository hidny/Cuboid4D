package SingleIntersectSolve;

import java.util.ArrayList;
import java.util.HashMap;

import Coord.Coord3D;

import solutionResolver.SolutionResolverInterface;

public class DFSIntersectFinderNoCuboid4D {

	
	public static final int NUM_ROTATIONS = 6;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;

	public static int DEBUG_NUM_CELLS = 10;
	//1, 6, 45, 344, 2670, 20886, 164514, 1303304, 10375830, 82947380, 
	//1, 3, 15, 86, 534, 3481, 23502, 162913, 1152870, 8294738, 60494549, 446205905, 3322769321
	//Got it:
	//A001931
	//Number of fixed 3-dimensional polycubes with n cells; lattice animals in the simple cubic lattice (6 nearest neighbors), face-connected cubes.
	//(Formerly M2996 N1213)
	
	public static void solveCuboidIntersections(/*CuboidToFoldOn cuboidToBuild, CuboidToFoldOn cuboidToBringAlong*/) {
		SolutionResolverInterface solutionResolver = null;
		
		
		
		
		solveCuboidIntersections(/*cuboidToBuild, cuboidToBringAlong, */solutionResolver);
	}

	
	public static void solveCuboidIntersections(/*CuboidToFoldOn cuboidToBuild, CuboidToFoldOn cuboidToBringAlong, */SolutionResolverInterface solutionResolver) {
		
		System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
		
		Coord3D paperToDevelop[] = new Coord3D[DEBUG_NUM_CELLS];
		for(int i=0; i<paperToDevelop.length; i++) {
			paperToDevelop[i] = null;
		}
		
		int GRID_SIZE = 4*DEBUG_NUM_CELLS;
		
		boolean paperUsed[][][] = new boolean[GRID_SIZE][GRID_SIZE][GRID_SIZE];
		int indexCuboidOnPaper[][][] = new int[GRID_SIZE][GRID_SIZE][GRID_SIZE];

		int indexCuboidOnPaper2ndCuboid[][][] = new int[GRID_SIZE][GRID_SIZE][GRID_SIZE];
		
		for(int i=0; i<paperUsed.length; i++) {
			for(int j=0; j<paperUsed[0].length; j++) {
				for(int k=0; k<paperUsed[0][0].length; k++) {
					paperUsed[i][j][k] = false;
					indexCuboidOnPaper[i][j][k] = -1;
					indexCuboidOnPaper2ndCuboid[i][j][k] = -1;
				}
			}
		}

		//Default start location GRID_SIZE / 2, GRID_SIZE / 2, GRID_SIZE / 2
		int START_I = GRID_SIZE/2;
		int START_J = GRID_SIZE/2;
		int START_K = GRID_SIZE/2;
		
		//CuboidToFoldOn cuboid = new CuboidToFoldOn(cuboidToBuild);
		
		//Insert start cell:
		
		//Once this reaches the total area, we're done!
		int numCellsUsedDepth = 0;

		int START_INDEX = 0;
		int START_ROTATION = 0;
		paperUsed[START_I][START_J][START_K] = true;
		paperToDevelop[numCellsUsedDepth] = new Coord3D(START_I, START_J, START_K);
		
		//cuboid.setCell(START_INDEX, START_ROTATION);
		indexCuboidOnPaper[START_I][START_J][START_K] = START_INDEX;
		numCellsUsedDepth += 1;
		
		
		
			int startIndex2ndCuboid =0;
			int startRotation2ndCuboid = 0;
			
		
			long debugIterations[] = new long[DEBUG_NUM_CELLS];
			
			HashMap<Integer, Integer> CellIndexToOrderOfDev = new HashMap <Integer, Integer>();
			CellIndexToOrderOfDev.put(0, 0);
			
			long numSolutions = doDepthFirstSearch(paperToDevelop, indexCuboidOnPaper, paperUsed, numCellsUsedDepth, solutionResolver, false, debugIterations, CellIndexToOrderOfDev, 0, 0);
			
			
			System.out.println("Num solutions: " + numSolutions);
			System.out.println("Num solutions corrected for translation: " + (numSolutions/DEBUG_NUM_CELLS));
			
			if(numSolutions % DEBUG_NUM_CELLS != 0) {
				System.out.println("Warning: it doesn't divide cleanly");
			}
			
			System.out.println("Done with trying to intersect 2nd cuboid that has a start index of " + startIndex2ndCuboid + " and a rotation index of " + startRotation2ndCuboid +".");
			System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
			
		
	}
	
	
	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 0, 1, 0, 0}, {0, 1, 0 , 0, -1, 0}, {0, 0, 1 , 0, 0, -1}};
	public static long numIterations = 0;

	public static long doDepthFirstSearch(Coord3D paperToDevelop[], int indexCuboidonPaper[][][], boolean paperUsed[][][],/* CuboidToFoldOn cuboid, */int numCellsUsedDepth,
			SolutionResolverInterface solutionResolver, /*CuboidToFoldOn cuboidToBringAlongStartRot, int indexCuboidOnPaper2ndCuboid[][][],*/
			boolean debugNope, long debugIterations[],
			HashMap <Integer, Integer> CellIndexToOrderOfDev, int minIndexToUse, int minRotationToUse) {

		numIterations++;
		if(numCellsUsedDepth == DEBUG_NUM_CELLS) {

			
			return 1;
		}

		//Utils.printFoldWithIndex(indexCuboidonPaper);
		
		//Display debug/what's-going-on update
		
		//if(numIterations % 10000000L == 0) {
		if(numIterations % 1000000L == 0) {
			
			System.out.println("Num iterations: " + numIterations);
			System.out.println();
			
			System.out.println("Last cell inserted: " + indexCuboidonPaper[paperToDevelop[numCellsUsedDepth - 1].i][paperToDevelop[numCellsUsedDepth - 1].j][paperToDevelop[numCellsUsedDepth - 1].k]);
			
		}
		//End display debug/what's-going-on update
		
		long retDuplicateSolutions = 0L;
		

		debugIterations[numCellsUsedDepth] = numIterations;
		
		//DEPTH-FIRST START:
		for(int curOrderedIndexToUse=minIndexToUse; curOrderedIndexToUse<numCellsUsedDepth && curOrderedIndexToUse<paperToDevelop.length && paperToDevelop[curOrderedIndexToUse] != null; curOrderedIndexToUse++) {
			
			int indexToUse = indexCuboidonPaper[paperToDevelop[curOrderedIndexToUse].i][paperToDevelop[curOrderedIndexToUse].j][paperToDevelop[curOrderedIndexToUse].k];
			
			
			//Try to attach a cell onto indexToUse using all 4 rotations:
			for(int dirNewCellAdd=0; dirNewCellAdd<NUM_ROTATIONS; dirNewCellAdd++) {
				
				if(CellIndexToOrderOfDev.containsKey(indexToUse) &&
						CellIndexToOrderOfDev.get(indexToUse) == minIndexToUse
						&& dirNewCellAdd <  minRotationToUse) {
					continue;
				}
				
				int new_i = paperToDevelop[curOrderedIndexToUse].i + nugdeBasedOnRotation[0][dirNewCellAdd];
				int new_j = paperToDevelop[curOrderedIndexToUse].j + nugdeBasedOnRotation[1][dirNewCellAdd];
				int new_k = paperToDevelop[curOrderedIndexToUse].k + nugdeBasedOnRotation[2][dirNewCellAdd];

				int indexNewCell = numCellsUsedDepth;
				
				if(paperUsed[new_i][new_j][new_k]) {
					//Cell we are considering to add is already there...
					continue;
				}
				
				
				boolean cantAddCellBecauseOfOtherPaperNeighbours = cantAddCellBecauseOfOtherPaperNeighbours(paperToDevelop, indexCuboidonPaper,
						//ALLOW_HOLES_cantAddCellBecauseOfOtherPaperNeighbours(paperToDevelop, indexCuboidonPaper,
						paperUsed/*, cuboid*/, numCellsUsedDepth,
						CellIndexToOrderOfDev, minIndexToUse, minRotationToUse,
						/*indexNewCell,*/ new_i, new_j, new_k, curOrderedIndexToUse
					);
				
				
				if( ! cantAddCellBecauseOfOtherPaperNeighbours) {
					
					//Setup for adding new cell:
					
					paperUsed[new_i][new_j][new_k] = true;
					indexCuboidonPaper[new_i][new_j][new_k] = indexNewCell;
					paperToDevelop[numCellsUsedDepth] = new Coord3D(new_i, new_j, new_k);

					//End setup

				
					CellIndexToOrderOfDev.put(indexNewCell, numCellsUsedDepth);
					numCellsUsedDepth += 1;
					
					retDuplicateSolutions += doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed/*, cuboid */,numCellsUsedDepth, solutionResolver/*, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid*/, debugNope, debugIterations, CellIndexToOrderOfDev, curOrderedIndexToUse, dirNewCellAdd);

					//Tear down (undo add of new cell)
					numCellsUsedDepth -= 1;

					CellIndexToOrderOfDev.remove(indexNewCell);
					
					paperUsed[new_i][new_j][new_k] = false;
					indexCuboidonPaper[new_i][new_j][new_k] = -1;
					paperToDevelop[numCellsUsedDepth] = null;

					

				} // End recursive if cond
			} // End loop rotation
		} //End loop index

		return retDuplicateSolutions;
	}
	
	//public static final int ONE_EIGHTY_ROTATION = 3;
	
	
	public static boolean cantAddCellBecauseOfOtherPaperNeighbours(Coord3D paperToDevelop[], int indexCuboidonPaper[][][],
			boolean paperUsed[][][],/* CuboidToFoldOn cuboid, */int numCellsUsedDepth,
			HashMap <Integer, Integer> CellIndexToOrderOfDev, int minIndexToUse, int minRotationToUse,
			/*int indexNewCell, */int new_i, int new_j, int new_k, int curOrderedIndexToUse
		) {	
	boolean cantAddCellBecauseOfOtherPaperNeighbours = false;
	
	int neighboursBasedOnRotation[][] = {{new_i-1, new_j, new_k}, {new_i, new_j+1, new_k}, {new_i, new_j, new_k - 1},{new_i+1, new_j, new_k},{new_i, new_j - 1, new_k},{new_i, new_j, new_k + 1}};

	
	for(int rotReq=0; rotReq<neighboursBasedOnRotation.length; rotReq++) {
		
		int i1 = neighboursBasedOnRotation[rotReq][0];
		int j1 = neighboursBasedOnRotation[rotReq][1];
		int k1 = neighboursBasedOnRotation[rotReq][2];
	
		if(paperToDevelop[curOrderedIndexToUse].i == i1 && paperToDevelop[curOrderedIndexToUse].j == j1 && paperToDevelop[curOrderedIndexToUse].k == k1) {
			continue;
		}
		
		//System.out.println("Paper neighbour:" + i1 + ", " + j1);
		
		if(paperUsed[i1][j1][k1]) {
			//System.out.println("Connected to another paper");
			
			int indexOtherCell = indexCuboidonPaper[i1][j1][k1];
			
			if(CellIndexToOrderOfDev.containsKey(indexOtherCell)
					&& CellIndexToOrderOfDev.get(indexOtherCell) < curOrderedIndexToUse ) {
				cantAddCellBecauseOfOtherPaperNeighbours = true;
				break;
			}
			
			
		}
	}
	return cantAddCellBecauseOfOtherPaperNeighbours;
}
	 
	
	public static void main(String args[]) {
		System.out.println("Fold Resolver Ordered Regions intersection skip symmetries Nx1x1:");

		solveCuboidIntersections();
		System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
		
	}
}
