package Model;


import Coord.CoordWithRotationAndIndex;

public class NeighbourGraphCreator {

	//Goal:
	// Give CuboidToFoldOn a graph/state machine to work with, so it can easily keep track of state.
	// This goal seems to already be acomplished and what's returned looks good.
	// TODO: I still need to test it against a 1x1x1 cube though...
	
	public static int NUM_NEIGHBOURS = 6;

	public static CoordWithRotationAndIndex[][] initNeighbourhood(int a, int b, int c, int d) {

		//TODO: this is not intuitive to me...
		CoordWithRotationAndIndex neighbours[][] = new CoordWithRotationAndIndex[Utils.getSurfaceVolume(a, b, c, d)][NUM_NEIGHBOURS];
		
		if(b == 1 && c == 1 && d == 1) {
			neighbours[0][0] = new CoordWithRotationAndIndex(1, -1);
			neighbours[0][1] = new CoordWithRotationAndIndex(4, -1);
			neighbours[0][2] = new CoordWithRotationAndIndex(6, -1);
			neighbours[0][3] = new CoordWithRotationAndIndex(2, -1);
			neighbours[0][4] = new CoordWithRotationAndIndex(5, -1);
			neighbours[0][5] = new CoordWithRotationAndIndex(3, -1);
			
			for(int i=0; i<a; i++) {
				int startIndex = 1 + 6*i;
				
				//TODO
				
			}
			
			int lastIndex = Utils.getSurfaceVolume(a, b, c, d) - 1;
			neighbours[lastIndex][0] = new CoordWithRotationAndIndex(1, -1);
			neighbours[lastIndex][1] = new CoordWithRotationAndIndex(4, -1);
			neighbours[lastIndex][2] = new CoordWithRotationAndIndex(6, -1);
			neighbours[lastIndex][3] = new CoordWithRotationAndIndex(2, -1);
			neighbours[lastIndex][4] = new CoordWithRotationAndIndex(5, -1);
			neighbours[lastIndex][5] = new CoordWithRotationAndIndex(3, -1);
			
			
		} else if(b == 2 && c == 1 && d == 1) {
			neighbours[0][0] = new CoordWithRotationAndIndex(1, -1);
			neighbours[0][1] = new CoordWithRotationAndIndex(4, -1);
			neighbours[0][2] = new CoordWithRotationAndIndex(6, -1);
			neighbours[0][3] = new CoordWithRotationAndIndex(2, -1);
			neighbours[0][4] = new CoordWithRotationAndIndex(5, -1);
			neighbours[0][5] = new CoordWithRotationAndIndex(3, -1);
			
			for(int i=0; i<a; i++) {
				int startIndex = 1 + 10*i;

				//TODO
				
			}
			
			int lastIndex = Utils.getSurfaceVolume(a, b, c, d) - 1;
			neighbours[lastIndex][0] = new CoordWithRotationAndIndex(1, -1);
			neighbours[lastIndex][1] = new CoordWithRotationAndIndex(4, -1);
			neighbours[lastIndex][2] = new CoordWithRotationAndIndex(6, -1);
			neighbours[lastIndex][3] = new CoordWithRotationAndIndex(2, -1);
			neighbours[lastIndex][4] = new CoordWithRotationAndIndex(5, -1);
			neighbours[lastIndex][5] = new CoordWithRotationAndIndex(3, -1);
			
		} else {
			System.out.println("Not ready for different dimensions yet");
			System.exit(1);
		}
		
		return neighbours;
	}
	
	
	
}
