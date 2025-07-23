package solutionResolver;

import Coord.Coord3D;


public interface SolutionResolverInterface {

	public long resolveSolution(Coord3D paperToDevelop[]);
	
	public long getNumUniqueFound();
}
