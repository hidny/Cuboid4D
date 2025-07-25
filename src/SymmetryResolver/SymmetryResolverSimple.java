package SymmetryResolver;

import java.util.Hashtable;

import Coord.Coord3D_Debug;
import Model.CuboidToFoldOn4D;
import Model.Utils;

public class SymmetryResolverSimple {

	public static boolean skipSearchNx1x1BecauseTopNeighTooBig(
			Coord3D_Debug paperToDevelop[],
			Hashtable <Integer, Integer> indexCuboidOnPaper,
			int cellIndexToUse,
			Coord3D_Debug indexToUseLocation,
			CuboidToFoldOn4D cuboidToBuild,
			int GRID_SIZE) {
		
		if(cellIndexToUse == cuboidToBuild.getNumCellsToFill() - 1
			&& getNumUsedNeighbourCellonPaper(indexCuboidOnPaper, paperToDevelop[0], GRID_SIZE)
					<= getNumUsedNeighbourCellonPaper(indexCuboidOnPaper, indexToUseLocation, GRID_SIZE)) {
				
			return true;
		}
		
		return false;
		
	}
	

	public static int getNumUsedNeighbourCellonPaper(
			Hashtable <Integer, Integer> indexCuboidOnPaper,
			Coord3D_Debug inputCell,
			int GRID_SIZE) {

		
		int ret = 0;
		
		if(indexCuboidOnPaper.containsKey(Utils.toHashNum(inputCell.i + 1, inputCell.j, inputCell.k, GRID_SIZE))) {
			ret++;
		}
		if(indexCuboidOnPaper.containsKey(Utils.toHashNum(inputCell.i, inputCell.j + 1, inputCell.k, GRID_SIZE))) {
			ret++;
		}
		if(indexCuboidOnPaper.containsKey(Utils.toHashNum(inputCell.i, inputCell.j, inputCell.k + 1, GRID_SIZE))) {
			ret++;
		}
		if(indexCuboidOnPaper.containsKey(Utils.toHashNum(inputCell.i - 1, inputCell.j, inputCell.k, GRID_SIZE))) {
			ret++;
		}
		if(indexCuboidOnPaper.containsKey(Utils.toHashNum(inputCell.i, inputCell.j - 1, inputCell.k, GRID_SIZE))) {
			ret++;
		}
		if(indexCuboidOnPaper.containsKey(Utils.toHashNum(inputCell.i, inputCell.j, inputCell.k - 1, GRID_SIZE))) {
			ret++;
		}
		
		return ret;
	}
}
