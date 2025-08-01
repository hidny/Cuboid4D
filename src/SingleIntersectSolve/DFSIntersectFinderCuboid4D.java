package SingleIntersectSolve;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import Coord.Coord3D_Debug;

//TODO: clean up and make a nice algo that will get A001931

import Model.CuboidToFoldOn4D;

//import Coord.Coord2D;
//import Coord.CoordWithRotationAndIndex;

//import Cuboid.SymmetryResolver.SymmetryResolver;

//import SolutionResolver.SolutionResolverInterface;
//import SolutionResolver.StandardResolverForSmallIntersectSolutions;
//import SolutionResolver.StandardResolverUsingMemory;

//import SymmetryResolver.SymmetryResolver;
//import GraphUtils.PivotCellDescription;
//import Model.CuboidToFoldOn;
import Model.Utils;
import SymmetryResolver.SymmetryResolverSimple;
import solutionResolver.SolutionResolverInterface;
import solutionResolver.SolutionResolverMemBasic;
//import Model.Utils;

public class DFSIntersectFinderCuboid4D {

	
	public static final int NUM_ROTATIONS = 6;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;

	
	public static void solveCuboidIntersections(CuboidToFoldOn4D cuboidToBuild/*, CuboidToFoldOn cuboidToBringAlong*/
			) {
		SolutionResolverInterface solutionResolver = new SolutionResolverMemBasic();
		
		
		/*if(Utils.getTotalArea(cuboidToBuild.getDimensions()) != Utils.getTotalArea(cuboidToBringAlong.getDimensions())) {
			System.out.println("ERROR: The two cuboid to intersect don't have the same area.");
			System.exit(1);
		}*/
		
		// Set the solution resolver to different things depending on the size of the cuboid:
		
		
		/*if(Utils.cuboidDimensionsMatch(cuboidToBuild, cuboidToBringAlong)) {
			//solutionResolver = new StandardResolverUsingMemory();
			solutionResolver = new StandardResolverForSmallIntersectSolutions();
		} else {
			solutionResolver = new StandardResolverForSmallIntersectSolutions();
		}*/
		
		
		solveCuboidIntersections(cuboidToBuild, solutionResolver, 0, 0, 1);
		
		//Tested just for fun:
		// I got 3799547, so it works:
		//solveCuboidIntersections(cuboidToBuild, solutionResolver, 1, 3, 4);
		
		/*System.out.println("Counting the number of solutions starting from anywhere:");
		for(int i=0; i<cuboidToBuild.getNumCellsToFill(); i++) {
			for(int j=0; j<NUM_NEIGHBOURS; j++) {
				for(int k=0; k<NUM_NEIGHBOURS; k++) {
					
					if((j-k) % 3 == 0) {
						continue;
					}
					solveCuboidIntersections(cuboidToBuild, solutionResolver, i, j, k);
					
				}
			}
		}*/
	}

	
	public static void solveCuboidIntersections(CuboidToFoldOn4D cuboidToBuild, /*CuboidToFoldOn cuboidToBringAlong, */SolutionResolverInterface solutionResolver,
			int startIndex, int startBlockDir1, int startBlockDir2) {
		
		System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
		
		Coord3D_Debug paperToDevelop[] = new Coord3D_Debug[cuboidToBuild.getNumCellsToFill()];
		for(int i=0; i<paperToDevelop.length; i++) {
			paperToDevelop[i] = null;
		}
		
		//int GRID_SIZE = 2*Utils.getSurfaceVolume(cuboidToBuild.getDimensions());
		int GRID_SIZE = 4*cuboidToBuild.getNumCellsToFill();
		
		Hashtable <Integer, Integer> indexCuboidOnPaper = new Hashtable<Integer, Integer>();
		
		//Default start location GRID_SIZE / 2, GRID_SIZE / 2, GRID_SIZE / 2
		int START_I = GRID_SIZE/2;
		int START_J = GRID_SIZE/2;
		int START_K = GRID_SIZE/2;
		
		//Once this reaches the total area, we're done!
		int numCellsUsedDepth = 0;

		cuboidToBuild.clearState();
		
		//TODO: test by switching this around and making sure it's the same:
		// also: (param2 - param3) % 3 != 0
		cuboidToBuild.initializeFirstCell(startIndex, startBlockDir1, startBlockDir2);
		
		//Clearing solutions found:
		SolutionResolverMemBasic.solutionsFound = new HashSet <BigInteger>();
		
		paperToDevelop[numCellsUsedDepth] = new Coord3D_Debug(START_I, START_J, START_K, startIndex, startBlockDir1, startBlockDir2);
		
		indexCuboidOnPaper.put(Utils.toHashNum(START_I, START_J, START_K, GRID_SIZE), startIndex);
		numCellsUsedDepth += 1;
		
		
		//TODO: Later try intersecting with all of them at once, so it's easier to get distinct solutions,
		// and maybe it's faster?

		//TODO: 2nd one
		//ArrayList<PivotCellDescription> startingPointsAndRotationsToCheck = PivotCellDescription.getUniqueRotationListsWithCellInfo(cuboidToBringAlong);
		
		//System.out.println("Num starting points and rotations to check: " + startingPointsAndRotationsToCheck.size());
		
		//(Set i=1 for non-trial Nx1x1 self-intersections (This is just a side-problem))
		//for(int i=1; i<startingPointsAndRotationsToCheck.size(); i++) {
		//for(int i=0; i<startingPointsAndRotationsToCheck.size(); i++) {
			//if(i != 0 && i != 4 && i != 6 && i != 10 && i != 14) {
			//	continue;
			//}
		//for(int i=0; i<1; i++) {
		
			//int startIndex2ndCuboid =startingPointsAndRotationsToCheck.get(i).getCellIndex();
			//int startRotation2ndCuboid = startingPointsAndRotationsToCheck.get(i).getRotationRelativeToCuboidMap();
			int startIndex2ndCuboid =0;
			int startRotation2ndCuboid = 0;
			
			//CuboidToFoldOn cuboidToBringAlongStartRot = new CuboidToFoldOn(cuboidToBringAlong);

			//cuboidToBringAlongStartRot.setCell(startIndex2ndCuboid, startRotation2ndCuboid);
			//indexCuboidOnPaper2ndCuboid[START_I][START_J][START_K] = startIndex2ndCuboid;
			
			//long debugIterations[] = new long[Utils.getTotalArea(cuboidToBuild.getDimensions())];
			long debugIterations[] = new long[cuboidToBuild.getNumCellsToFill()];
			
			HashMap<Integer, Integer> CellIndexToOrderOfDev = new HashMap <Integer, Integer>();
			CellIndexToOrderOfDev.put(startIndex, 0);
			
			long numSolutions = doDepthFirstSearch(paperToDevelop, indexCuboidOnPaper, GRID_SIZE, numCellsUsedDepth, solutionResolver, false, debugIterations, CellIndexToOrderOfDev, 0, 0, cuboidToBuild);
			

			System.out.println();
			System.out.println("Num solutions after removing dups: " + solutionResolver.getNumUniqueFound());
			
			//System.out.println("Done with trying to intersect 2nd cuboid that has a start index of " + startIndex2ndCuboid + " and a rotation index of " + startRotation2ndCuboid +".");
			System.out.println("1st cuboid stats: " + startIndex + " and a rotation index of (" + startBlockDir1 +", " + startBlockDir2 + ").");
			System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
			
		//}
		
		//TODO: end todo 2nd one
		
		
		//System.out.println("Final number of unique solutions: " + solutionResolver.getNumUniqueFound());
		
		//System.out.println("Number of iterations: " + numIterations);
	}

	
	public static final int nugdeBasedOnRotation[][] = {{1, 0, 0, -1, 0, 0}, {0, 1, 0 , 0, -1, 0}, {0, 0, 1 , 0, 0, -1}};
	public static long numIterations = 0;
	
	public static long origFixedNumberDebug = 0;
	
	public static long debugNumSolutionsPrevPhase = 0;

	public static long doDepthFirstSearch(Coord3D_Debug paperToDevelop[], Hashtable <Integer, Integer> indexCuboidOnPaper, int GRID_SIZE,/* CuboidToFoldOn cuboid, */int numCellsUsedDepth,
			SolutionResolverInterface solutionResolver, /*CuboidToFoldOn cuboidToBringAlongStartRot, int indexCuboidOnPaper2ndCuboid[][][],*/
			boolean debugNope, long debugIterations[],
			HashMap <Integer, Integer> CellIndexToOrderOfDev, int minIndexToUse, int minRotationToUse,
			CuboidToFoldOn4D cuboidToBuild ) {

		numIterations++;
		boolean debugPrintedNewPhaseInCurFunction = false;
		
		if(numCellsUsedDepth == cuboidToBuild.getNumCellsToFill()) {
			
			origFixedNumberDebug++;
			return solutionResolver.resolveSolution(paperToDevelop);
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
			
			if(numCellsUsedDepth - 1 == 1) {
				System.out.println("DEBUG");
			}
			
		}
		//End display debug/what's-going-on update
		
		long retDuplicateSolutions = 0L;
		
		debugIterations[numCellsUsedDepth] = numIterations;
		
		//DEPTH-FIRST START:
		for(int curOrderedIndexToUse=minIndexToUse; curOrderedIndexToUse<numCellsUsedDepth && curOrderedIndexToUse<paperToDevelop.length && paperToDevelop[curOrderedIndexToUse] != null; curOrderedIndexToUse++) {

			
			int indexToUse =indexCuboidOnPaper.get(Utils.toHashNum(paperToDevelop[curOrderedIndexToUse], GRID_SIZE));


			 /*if(SymmetryResolver.skipSearchBecauseOfASymmetryArgDontCareAboutRotation
					(cuboid, paperToDevelop, indexCuboidonPaper, curOrderedIndexToUse, indexToUse)
				&& skipSymmetries) {
				continue;

				
			} else if( 2*numCellsUsedDepth < paperToDevelop.length && SymmetryResolver.skipSearchBecauseCuboidCouldProvablyNotBeBuiltThisWay
					(cuboid, paperToDevelop, indexCuboidonPaper, curOrderedIndexToUse, indexToUse, topBottombridgeUsedNx1x1, CellIndexToOrderOfDev) && skipSymmetries) {
				
				break;
				
				//Maybe put this right after the contains key if condition? (regions[regionIndex].getCellIndexToOrderOfDev().containsKey(indexToUse))
			}*/

			//CoordWithRotationAndIndex neighbours[] = cuboid.getNeighbours(indexToUse);
			
			//int curRotation = cuboid.getRotationPaperRelativeToMap(indexToUse);
			
			//int indexToUse2 = indexCuboidOnPaper2ndCuboid[paperToDevelop[curOrderedIndexToUse].i][paperToDevelop[curOrderedIndexToUse].j];
			//int curRotationCuboid2 = cuboidToBringAlongStartRot.getRotationPaperRelativeToMap(indexToUse2);
			
			//Try to attach a cell onto indexToUse using all 4 rotations:
			for(int dirNewCellAdd=0; dirNewCellAdd<NUM_ROTATIONS; dirNewCellAdd++) {
				
				//int neighbourArrayIndex = (dirNewCellAdd - curRotation + NUM_ROTATIONS) % NUM_ROTATIONS;
				
				/*if(cuboid.isCellIndexUsed(neighbours[neighbourArrayIndex].getIndex())) {
					
					//Don't reuse a used cell:
					continue;
					
				} else */
				int indexNewCell = cuboidToBuild.getNeighbourIndex(indexToUse, dirNewCellAdd);
				
				
				if(CellIndexToOrderOfDev.containsKey(indexToUse) &&
						CellIndexToOrderOfDev.get(indexToUse) == minIndexToUse
						&& dirNewCellAdd <  minRotationToUse) {
					continue;
				} else if( ! cuboidToBuild.couldAttachCell(indexToUse, dirNewCellAdd)) {
					continue;
				}

				//int indexNewCell2 = cuboidToBringAlongStartRot.getNeighbours(indexToUse2)[neighbourIndexCuboid2].getIndex();
				
				/*if(cuboidToBringAlongStartRot.isCellIndexUsed(indexNewCell2)) {
					//no good!
					continue;
				}*/
				
				
				int new_i = paperToDevelop[curOrderedIndexToUse].i + nugdeBasedOnRotation[0][dirNewCellAdd];
				int new_j = paperToDevelop[curOrderedIndexToUse].j + nugdeBasedOnRotation[1][dirNewCellAdd];
				int new_k = paperToDevelop[curOrderedIndexToUse].k + nugdeBasedOnRotation[2][dirNewCellAdd];

				//int indexNewCell = neighbours[neighbourArrayIndex].getIndex();
				
				if(indexCuboidOnPaper.containsKey(Utils.toHashNum(new_i, new_j, new_k, GRID_SIZE))) {
					//Cell we are considering to add is already there...
					continue;
				}
				
				
				//int rotationNeighbourPaperRelativeToMap = (curRotation - neighbours[neighbourArrayIndex].getRot() + NUM_ROTATIONS) % NUM_ROTATIONS;
				//int rotationNeighbourPaperRelativeToMap2 = (curRotationCuboid2 - cuboidToBringAlongStartRot.getNeighbours(indexToUse2)[neighbourIndexCuboid2].getRot() + NUM_ROTATIONS)  % NUM_ROTATIONS;
				
				
				/*if(SymmetryResolver.skipSearchBecauseOfASymmetryArg
						(cuboid, paperToDevelop, curOrderedIndexToUse, indexCuboidonPaper, dirNewCellAdd, curRotation, paperUsed, indexToUse, indexNewCell)
					&& skipSymmetries == true) {
					continue;
				}*/
				
				boolean cantAddCellBecauseOfOtherPaperNeighbours = cantAddCellBecauseOfOtherPaperNeighbours(paperToDevelop, indexCuboidOnPaper,
						//ALLOW_HOLES_cantAddCellBecauseOfOtherPaperNeighbours(paperToDevelop, indexCuboidonPaper,
						GRID_SIZE, cuboidToBuild, numCellsUsedDepth,
						CellIndexToOrderOfDev, minIndexToUse, minRotationToUse,
						indexNewCell, new_i, new_j, new_k, curOrderedIndexToUse
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
					//cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
					//cuboidToBringAlongStartRot.setCell(indexNewCell2, rotationNeighbourPaperRelativeToMap2);
					
					indexCuboidOnPaper.put(Utils.toHashNum(new_i, new_j, new_k, GRID_SIZE), indexNewCell);
					
					cuboidToBuild.attachCell(indexToUse, dirNewCellAdd);
					
					paperToDevelop[numCellsUsedDepth] = new Coord3D_Debug(new_i, new_j, new_k, indexNewCell, cuboidToBuild.debugGetCellDir1(indexNewCell), cuboidToBuild.debugGetCellDir2(indexNewCell));

					//indexCuboidOnPaper2ndCuboid[new_i][new_j] = indexNewCell2;

					
					
					//End setup

				
					CellIndexToOrderOfDev.put(indexNewCell, numCellsUsedDepth);
					numCellsUsedDepth += 1;
					
					
					retDuplicateSolutions += doDepthFirstSearch(paperToDevelop, indexCuboidOnPaper, GRID_SIZE/*, cuboid */,numCellsUsedDepth, solutionResolver/*, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid*/, debugNope, debugIterations, CellIndexToOrderOfDev, curOrderedIndexToUse, dirNewCellAdd, cuboidToBuild);

					//Tear down (undo add of new cell)
					numCellsUsedDepth -= 1;

					CellIndexToOrderOfDev.remove(indexNewCell);
					
					indexCuboidOnPaper.remove(Utils.toHashNum(new_i, new_j, new_k, GRID_SIZE));
					paperToDevelop[numCellsUsedDepth] = null;

					//indexCuboidOnPaper2ndCuboid[new_i][new_j][new_k] = -1;

					cuboidToBuild.removeCellAttachment(indexToUse, dirNewCellAdd);
					//cuboidToBringAlongStartRot.removeCell(indexNewCell2);
					
					//End tear down


				} // End recursive if cond
			} // End loop rotation
		} //End loop index

		return retDuplicateSolutions;
	}
	
	//public static final int ONE_EIGHTY_ROTATION = 3;
	
	
	public static boolean cantAddCellBecauseOfOtherPaperNeighbours(Coord3D_Debug paperToDevelop[], Hashtable <Integer, Integer> indexCuboidOnPaper,
			int GRID_SIZE,
			CuboidToFoldOn4D cuboid4D, int numCellsUsedDepth,
			HashMap <Integer, Integer> CellIndexToOrderOfDev, int minIndexToUse, int minRotationToUse,
			int indexNewCell, int new_i, int new_j, int new_k, int curOrderedIndexToUse
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
			//int rotationOtherCell = cuboid.getRotationPaperRelativeToMap(indexOtherCell);
			
			int neighbourIndexNeeded = (rotReq + NUM_NEIGHBOURS/2) % NUM_NEIGHBOURS;
			
			if(cuboid4D.getAttachCellIndex(indexOtherCell, neighbourIndexNeeded) != indexNewCell) {

				if(ALLOW_CUT_BETWEEN_FACES) {
					continue;
				} else {
					cantAddCellBecauseOfOtherPaperNeighbours = true;
					break;
				}
				
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

		
		//solveCuboidIntersections(new CuboidToFoldOn(13, 1, 1), new CuboidToFoldOn(3, 3, 3));
		
		//solveCuboidIntersections(new CuboidToFoldOn4D(1, 1, 1, 1));
		
		solveCuboidIntersections(new CuboidToFoldOn4D(2, 1, 1, 1));		
		
		// solveCuboidIntersections(new CuboidToFoldOn4D(1, 2, 1, 1));
		
		System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
		
		System.out.println("END OF PROGRAM");
		
		if(ALLOW_CUT_BETWEEN_FACES) {
			System.out.println("Cuts between faces was enabled.");
		} else {
			System.out.println("No Cuts between faces!");
		}
		
	}
}
