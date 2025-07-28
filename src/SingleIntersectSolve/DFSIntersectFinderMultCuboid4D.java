package SingleIntersectSolve;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import Coord.Coord3D_Debug;

import Model.CuboidToFoldOn4D;

import Model.Utils;
import SymmetryResolver.SymmetryResolverSimple;
import solutionResolver.SolutionResolverInterface;
import solutionResolver.SolutionResolverMemBasic;

public class DFSIntersectFinderMultCuboid4D {

	
	public static final int NUM_ROTATIONS = 6;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;

	
	public static void solveCuboidIntersections(CuboidToFoldOn4D cuboidToBuild, CuboidToFoldOn4D cuboidToBringAlong
			) {
		SolutionResolverInterface solutionResolver = new SolutionResolverMemBasic();
		
		
		if(Utils.getSurfaceVolume(cuboidToBuild.getDimensions()) != Utils.getSurfaceVolume(cuboidToBringAlong.getDimensions())) {
			System.out.println("ERROR: The two cuboid to intersect don't have the same surface volume.");
			System.exit(1);
		}
		
		int firstStartIndex = 7;
		int firstStartBlockDir1 = 0;
		int FirstStartBlockDir2 = 2;
		
		int firstCuboidParams[] = new int[] {firstStartIndex, firstStartBlockDir1, FirstStartBlockDir2};
		
		int secondStartIndex = 0;
		int secondStartBlockDir1 = 0;
		int secondStartBlockDir2 = 1;
		
		int secondCuboidParams[] = new int[] {secondStartIndex, secondStartBlockDir1, secondStartBlockDir2};
		
		System.out.println("Map of Cuboid 1:");
		cuboidToBuild.printCanonicalCuboid4d( getStandardGridSize(cuboidToBuild));
		
		System.out.println("Map of Cuboid 2:");
		cuboidToBringAlong.printCanonicalCuboid4d( getStandardGridSize(cuboidToBuild));
		
		solveCuboidIntersections(cuboidToBuild, cuboidToBringAlong, solutionResolver, firstCuboidParams, secondCuboidParams);
		
	}

	
	public static void solveCuboidIntersections(CuboidToFoldOn4D cuboidToBuild, CuboidToFoldOn4D cuboidToBringAlong, SolutionResolverInterface solutionResolver,
			int firstCuboidParams[], int secondCuboidParams[]) {
		
		System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
		
		Coord3D_Debug paperToDevelop[] = new Coord3D_Debug[cuboidToBuild.getNumCellsToFill()];
		for(int i=0; i<paperToDevelop.length; i++) {
			paperToDevelop[i] = null;
		}
		
		Coord3D_Debug paperToDevelopFor2ndCuboidDEBUG[] = new Coord3D_Debug[cuboidToBuild.getNumCellsToFill()];
		for(int i=0; i<paperToDevelopFor2ndCuboidDEBUG.length; i++) {
			paperToDevelopFor2ndCuboidDEBUG[i] = null;
		}
		
		int GRID_SIZE = getStandardGridSize(cuboidToBuild);
		
		Hashtable <Integer, Integer> indexCuboidOnPaper = new Hashtable<Integer, Integer>();
		Hashtable <Integer, Integer> indexCuboidOnPaper2ndCuboid = new Hashtable<Integer, Integer>();
		
		//Default start location: GRID_SIZE / 2, GRID_SIZE / 2, GRID_SIZE / 2
		int START_I = GRID_SIZE/2;
		int START_J = GRID_SIZE/2;
		int START_K = GRID_SIZE/2;
		
		//Once this reaches the total area, we're done!
		int numCellsUsedDepth = 0;

		cuboidToBuild.clearState();
		cuboidToBringAlong.clearState();
		
		cuboidToBuild.initializeFirstCell(firstCuboidParams[0], firstCuboidParams[1], firstCuboidParams[2]);
		cuboidToBringAlong.initializeFirstCell(secondCuboidParams[0], secondCuboidParams[1], secondCuboidParams[2]);
		
		//Clearing solutions found:
		SolutionResolverMemBasic.solutionsFound = new HashSet <BigInteger>();
		
		paperToDevelop[numCellsUsedDepth] = new Coord3D_Debug(START_I, START_J, START_K, firstCuboidParams[0], firstCuboidParams[1], firstCuboidParams[2]);
		paperToDevelopFor2ndCuboidDEBUG[numCellsUsedDepth] = new Coord3D_Debug(START_I, START_J, START_K, secondCuboidParams[0], secondCuboidParams[1], secondCuboidParams[2]);
		
		indexCuboidOnPaper.put(Utils.toHashNum(START_I, START_J, START_K, GRID_SIZE), firstCuboidParams[0]);
		indexCuboidOnPaper2ndCuboid.put(Utils.toHashNum(START_I, START_J, START_K, GRID_SIZE), secondCuboidParams[0]);
		numCellsUsedDepth += 1;
		
		long debugIterations[] = new long[cuboidToBuild.getNumCellsToFill()];
		
		HashMap<Integer, Integer> CellIndexToOrderOfDev = new HashMap <Integer, Integer>();
		CellIndexToOrderOfDev.put(firstCuboidParams[0], 0);
		
		long numSolutions = doDepthFirstSearchIntersect(paperToDevelop, indexCuboidOnPaper, indexCuboidOnPaper2ndCuboid, GRID_SIZE, numCellsUsedDepth, solutionResolver, false, debugIterations, CellIndexToOrderOfDev, 0, 0, cuboidToBuild, cuboidToBringAlong, paperToDevelopFor2ndCuboidDEBUG);
		

		System.out.println();
		System.out.println("numSolutions in function output: " + numSolutions + " (Probably not relevant???)");
		System.out.println();
		System.out.println("Num solutions after removing dups: " + solutionResolver.getNumUniqueFound());
		
		//System.out.println("Done with trying to intersect 2nd cuboid that has a start index of " + startIndex2ndCuboid + " and a rotation index of " + startRotation2ndCuboid +".");
		System.out.println("1st cuboid stats: " + firstCuboidParams[0] + " and a rotation index of (" + firstCuboidParams[1] +", " + firstCuboidParams[2] + ").");
		System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
		
	}
	
	public static int getStandardGridSize(CuboidToFoldOn4D cuboidToBuild) {
		return 4 * cuboidToBuild.getNumCellsToFill();
	}

	
	public static final int nugdeBasedOnRotation[][] = {{1, 0, 0, -1, 0, 0}, {0, 1, 0 , 0, -1, 0}, {0, 0, 1 , 0, 0, -1}};
	public static long numIterations = 0;
	
	public static long origFixedNumberDebug = 0;
	
	public static long debugNumSolutionsPrevPhase = 0;

	public static long doDepthFirstSearchIntersect(Coord3D_Debug paperToDevelop[], Hashtable <Integer, Integer> indexCuboidOnPaper, Hashtable <Integer, Integer> indexCuboidOnPaper2ndCuboid, int GRID_SIZE, int numCellsUsedDepth,
			SolutionResolverInterface solutionResolver,
			boolean debugNope, long debugIterations[],
			HashMap <Integer, Integer> CellIndexToOrderOfDev, int minIndexToUse, int minRotationToUse,
			CuboidToFoldOn4D cuboidToBuild,  CuboidToFoldOn4D cuboidToBringAlong, Coord3D_Debug paperToDevelopFor2ndCuboid[]) {

		numIterations++;
		boolean debugPrintedNewPhaseInCurFunction = false;
		
		if(numCellsUsedDepth == cuboidToBuild.getNumCellsToFill()) {
			
			origFixedNumberDebug++;
			long temp =  solutionResolver.resolveSolution(paperToDevelop);
			
			//TODO:

			if(temp >= 1) {
				System.out.println("found a solution:");
				System.out.println("Cuboid 1:");
				Utils.printSolution(paperToDevelop, 0, false, numCellsUsedDepth);
				System.out.println("Cuboid 2:");
				Utils.printSolution(paperToDevelopFor2ndCuboid, 0, false, numCellsUsedDepth);
				
				System.out.println();
				
				System.out.println("Solution code: " + ((SolutionResolverMemBasic)solutionResolver).lastSolution);
				System.out.println();
				System.out.println();
				if(solutionResolver.getNumUniqueFound() > 10000) {
					System.out.println("Found more than 10000 solutions. Stopping program");
					System.exit(1);
				}
			}
			//System.exit(1);
			
			return temp;
		}
		
		if(numIterations % 10000000L == 0) {
			//Display debug/what's-going-on update
			
			System.out.println("Num iterations: " + numIterations);
			//Utils.printFold(paperUsed);
			//Utils.printFoldWithIndex(indexCuboidonPaper);
			//Utils.printFoldWithIndex(indexCuboidOnPaper2ndCuboid);
			
			//System.out.println("Solutions: " + solutionResolver.getNumUniqueFound());
			System.out.println();
			
			System.out.println("Last cell inserted: " + indexCuboidOnPaper.get(Utils.toHashNum(paperToDevelop[numCellsUsedDepth - 1], GRID_SIZE)));
			
			
		}
		//End display debug/what's-going-on update
		
		long retDuplicateSolutions = 0L;
		
		debugIterations[numCellsUsedDepth] = numIterations;
		
		//DEPTH-FIRST START:
		for(int curOrderedIndexToUse=minIndexToUse; curOrderedIndexToUse<numCellsUsedDepth && curOrderedIndexToUse<paperToDevelop.length && paperToDevelop[curOrderedIndexToUse] != null; curOrderedIndexToUse++) {

			
			int indexToUse =indexCuboidOnPaper.get(Utils.toHashNum(paperToDevelop[curOrderedIndexToUse], GRID_SIZE));

			int indexToUse2 =indexCuboidOnPaper2ndCuboid.get(Utils.toHashNum(paperToDevelopFor2ndCuboid[curOrderedIndexToUse], GRID_SIZE));
			
			//Try to attach a cell onto indexToUse using all 4 rotations:
			for(int dirNewCellAdd=0; dirNewCellAdd<NUM_ROTATIONS; dirNewCellAdd++) {
				
				
				int indexNewCell = cuboidToBuild.getNeighbourIndex(indexToUse, dirNewCellAdd);
				int indexNewCell2 = cuboidToBringAlong.getNeighbourIndex(indexToUse2, dirNewCellAdd);
				
				
				if(CellIndexToOrderOfDev.containsKey(indexToUse) &&
						CellIndexToOrderOfDev.get(indexToUse) == minIndexToUse
						&& dirNewCellAdd <  minRotationToUse) {
					continue;
				} else if( ! cuboidToBuild.couldAttachCell(indexToUse, dirNewCellAdd)) {
					continue;
				} else if( ! cuboidToBringAlong.couldAttachCell(indexToUse2, dirNewCellAdd)) {
					continue;
				}
				
				int new_i = paperToDevelop[curOrderedIndexToUse].i + nugdeBasedOnRotation[0][dirNewCellAdd];
				int new_j = paperToDevelop[curOrderedIndexToUse].j + nugdeBasedOnRotation[1][dirNewCellAdd];
				int new_k = paperToDevelop[curOrderedIndexToUse].k + nugdeBasedOnRotation[2][dirNewCellAdd];

				
				if(indexCuboidOnPaper.containsKey(Utils.toHashNum(new_i, new_j, new_k, GRID_SIZE))) {
					//Cell we are considering to add is already there...
					continue;
				}
				
				
				boolean cantAddCellBecauseOfOtherPaperNeighbours = cantAddCellBecauseOfOtherPaperNeighbours(paperToDevelop, paperToDevelopFor2ndCuboid, indexCuboidOnPaper, indexCuboidOnPaper2ndCuboid,
						GRID_SIZE, cuboidToBuild, cuboidToBringAlong, numCellsUsedDepth,
						CellIndexToOrderOfDev, minIndexToUse, minRotationToUse,
						indexNewCell, new_i, new_j, new_k, curOrderedIndexToUse, indexNewCell2
					);
				
				
				if( ! cantAddCellBecauseOfOtherPaperNeighbours) {
					
					//DEBUG NEW PHASE:
					if(debugPrintedNewPhaseInCurFunction == false && minIndexToUse == 0 && curOrderedIndexToUse > 0) {
						debugPrintedNewPhaseInCurFunction = true;
						System.out.println("DEBUG NEW PHASE");
						System.out.println("Num new solutions in previous phase: " + (solutionResolver.getNumUniqueFound() - debugNumSolutionsPrevPhase));
						debugNumSolutionsPrevPhase = solutionResolver.getNumUniqueFound();
						
						Model.Utils.printSolution(paperToDevelop, solutionResolver.getNumUniqueFound(), false, numCellsUsedDepth);
						System.out.println("END DEBUG NEW PHASE");
						System.out.println();
						
					}
					//END DEBUG  NEW PHASE:
					
					if( cuboidToBuild.getDimensions()[1] == 1
							&& SymmetryResolverSimple.skipSearchNx1x1BecauseTopNeighTooBig(
						paperToDevelop,
						indexCuboidOnPaper,
						indexToUse,
						paperToDevelop[curOrderedIndexToUse],
						cuboidToBuild,
						GRID_SIZE))	{

						continue;
					}
					
					//Setup for adding new cell:
					
					indexCuboidOnPaper.put(Utils.toHashNum(new_i, new_j, new_k, GRID_SIZE), indexNewCell);
					indexCuboidOnPaper2ndCuboid.put(Utils.toHashNum(new_i, new_j, new_k, GRID_SIZE), indexNewCell2);
					
					cuboidToBuild.attachCell(indexToUse, dirNewCellAdd);
					cuboidToBringAlong.attachCell(indexToUse2, dirNewCellAdd);
					
					paperToDevelop[numCellsUsedDepth] = new Coord3D_Debug(new_i, new_j, new_k, indexNewCell, cuboidToBuild.debugGetCellDir1(indexNewCell), cuboidToBuild.debugGetCellDir2(indexNewCell));
					paperToDevelopFor2ndCuboid[numCellsUsedDepth] = new Coord3D_Debug(new_i, new_j, new_k, indexNewCell2, cuboidToBringAlong.debugGetCellDir1(indexNewCell2), cuboidToBringAlong.debugGetCellDir2(indexNewCell2));		
					
					//End setup

				
					CellIndexToOrderOfDev.put(indexNewCell, numCellsUsedDepth);
					numCellsUsedDepth += 1;
					
					
					retDuplicateSolutions += doDepthFirstSearchIntersect(
							paperToDevelop, indexCuboidOnPaper, indexCuboidOnPaper2ndCuboid,
							GRID_SIZE, numCellsUsedDepth, solutionResolver, debugNope,
							debugIterations, CellIndexToOrderOfDev, curOrderedIndexToUse,
							dirNewCellAdd, cuboidToBuild, cuboidToBringAlong, paperToDevelopFor2ndCuboid);

					//Tear down (undo add of new cell)
					numCellsUsedDepth -= 1;

					CellIndexToOrderOfDev.remove(indexNewCell);
					
					indexCuboidOnPaper.remove(Utils.toHashNum(new_i, new_j, new_k, GRID_SIZE));
					indexCuboidOnPaper2ndCuboid.remove(Utils.toHashNum(new_i, new_j, new_k, GRID_SIZE));
					
					paperToDevelop[numCellsUsedDepth] = null;
					paperToDevelopFor2ndCuboid[numCellsUsedDepth] = null;

					cuboidToBuild.removeCellAttachment(indexToUse, dirNewCellAdd);
					cuboidToBringAlong.removeCellAttachment(indexToUse2, dirNewCellAdd);
					
					//End tear down


				} // End recursive if cond
			} // End loop rotation
		} //End loop index

		return retDuplicateSolutions;
	}
	
	//public static final int ONE_EIGHTY_ROTATION = 3;
	
	
	public static boolean cantAddCellBecauseOfOtherPaperNeighbours(Coord3D_Debug paperToDevelop[], Coord3D_Debug paperToDevelopFor2ndCuboid[], Hashtable <Integer, Integer> indexCuboidOnPaper,
			Hashtable <Integer, Integer> indexCuboidOnPaper2ndCuboid,
			int GRID_SIZE,
			CuboidToFoldOn4D cuboid4D, CuboidToFoldOn4D cuboidToBringAlong, int numCellsUsedDepth,
			HashMap <Integer, Integer> CellIndexToOrderOfDev, int minIndexToUse, int minRotationToUse,
			int indexNewCell, int new_i, int new_j, int new_k, int curOrderedIndexToUse, int indexNewCell2ndCuboid
		) {	
	boolean cantAddCellBecauseOfOtherPaperNeighbours = false;
	
	//Note that these variable have to match up!
	//public static final int nugdeBasedOnRotation[][] = {{1, 0, 0, -1, 0, 0}, {0, 1, 0 , 0, -1, 0}, {0, 0, 1 , 0, 0, -1}};
	int neighboursBasedOnRotation[][] = {
			{new_i + 1, new_j,     new_k},
			{new_i,     new_j + 1, new_k},
			{new_i,     new_j,     new_k + 1},
			{new_i - 1, new_j,     new_k},
			{new_i,     new_j - 1, new_k},
			{new_i,     new_j,     new_k - 1}
	};
	
	
	for(int rotReq=0; rotReq<neighboursBasedOnRotation.length; rotReq++) {
		
		int i1 = neighboursBasedOnRotation[rotReq][0];
		int j1 = neighboursBasedOnRotation[rotReq][1];
		int k1 = neighboursBasedOnRotation[rotReq][2];
	
		if(paperToDevelop[curOrderedIndexToUse].i == i1 && paperToDevelop[curOrderedIndexToUse].j == j1 && paperToDevelop[curOrderedIndexToUse].k == k1) {
			continue;
		}
		
		//System.out.println("Paper neighbour:" + i1 + ", " + j1);
		
		if(indexCuboidOnPaper.containsKey(Utils.toHashNum(i1, j1, k1, GRID_SIZE))) {
			//System.out.println("Connected to another paper");
			
			int indexOtherCell = indexCuboidOnPaper.get(Utils.toHashNum(i1, j1, k1, GRID_SIZE));
			int indexOtherCell2ndCuboid = indexCuboidOnPaper2ndCuboid.get(Utils.toHashNum(i1, j1, k1, GRID_SIZE));
			
			//int rotationOtherCell = cuboid.getRotationPaperRelativeToMap(indexOtherCell);
			
			int neighbourIndexNeeded = (rotReq + NUM_NEIGHBOURS/2) % NUM_NEIGHBOURS;
			
			
			if(ALLOW_CUT_BETWEEN_FACES
					&& cuboid4D.getAttachCellIndex(indexOtherCell, neighbourIndexNeeded) != indexNewCell
					&& cuboidToBringAlong.getAttachCellIndex(indexOtherCell2ndCuboid, neighbourIndexNeeded) != indexNewCell2ndCuboid)
			{
				continue;
				
			} else if( ! ALLOW_CUT_BETWEEN_FACES
					&& (cuboid4D.getAttachCellIndex(indexOtherCell, neighbourIndexNeeded) != indexNewCell
					    || cuboidToBringAlong.getAttachCellIndex(indexOtherCell2ndCuboid, neighbourIndexNeeded) != indexNewCell2ndCuboid)
					) {

				cantAddCellBecauseOfOtherPaperNeighbours = true;
				break;
		
			} else if(CellIndexToOrderOfDev.containsKey(indexOtherCell)
					&& CellIndexToOrderOfDev.get(indexOtherCell) < curOrderedIndexToUse ) {
				cantAddCellBecauseOfOtherPaperNeighbours = true;
				break;
			}
			
		}
	}
	return cantAddCellBecauseOfOtherPaperNeighbours;
}
	 
	public static final boolean ALLOW_CUT_BETWEEN_FACES = false;
	
	public static void main(String args[]) {
		System.out.println("DFSIntersectFinderNoCuboid4d HASH:");

		//solveCuboidIntersections(new CuboidToFoldOn4D(2, 1, 1, 1), new CuboidToFoldOn4D(1, 2, 1, 1));		

		solveCuboidIntersections(new CuboidToFoldOn4D(4, 2, 1, 1), new CuboidToFoldOn4D(7, 1, 1, 1));
		System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
		
		System.out.println("END OF PROGRAM");
		
		if(ALLOW_CUT_BETWEEN_FACES) {
			System.out.println("Cuts between faces was enabled.");
		} else {
			System.out.println("No Cuts between faces!");
		}
		
	}
}
