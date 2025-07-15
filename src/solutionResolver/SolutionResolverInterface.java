package solutionResolver;

import Coord.Coord3D;

//TODO
//import Coord.Coord2D;

public interface SolutionResolverInterface {

	
	//public long resolveSolution(Model.CuboidToFoldOn cuboidDimensionsAndNeighbours, Coord2D paperToDevelop[], int indexCuboidonPaper[][][], boolean paperUsed[][]);
	
	public long resolveSolution(Coord3D paperToDevelop[]);
	
	public long getNumUniqueFound();
}
