package Model;


import Coord.Coord3D;
import Coord.Coord3D_Debug;

public class Utils {

	public static final int SIDES_CUBOID = 6;
	

	public static int getSurfaceVolume(int array[]) {
		if(array.length != 4) {
			System.out.println("ERROR in getTotalArea: there isn't 4 dimensions!");
			System.exit(1);
		}
		return getSurfaceVolume(array[0], array[1], array[2], array[3]);
	}
	public static int getSurfaceVolume(int a, int b, int c, int d) {
		return 2*(b*c*d + a*c*d + a*b*d + a*b*c);
	}

	

	public static int[][] getBorders(Coord3D paperToDevelop[], int sizeArray) {
		int borders[][] = new int[3][2];
		
		borders[0][0] = paperToDevelop[0].i;
		borders[1][0] = paperToDevelop[0].j;
		borders[2][0] = paperToDevelop[0].k;

		borders[0][1] = paperToDevelop[0].i;
		borders[1][1] = paperToDevelop[0].j;
		borders[2][1] = paperToDevelop[0].k;
		
		for(int i=0; i<sizeArray; i++) {
			borders[0][0] = Math.min(borders[0][0], paperToDevelop[i].i);
			borders[1][0] = Math.min(borders[1][0], paperToDevelop[i].j);
			borders[2][0] = Math.min(borders[2][0], paperToDevelop[i].k);

			borders[0][1] = Math.max(borders[0][1], paperToDevelop[i].i);
			borders[1][1] = Math.max(borders[1][1], paperToDevelop[i].j);
			borders[2][1] = Math.max(borders[2][1], paperToDevelop[i].k);
			
			
		}
		
		return borders;
	}
	

	public static int toHashNum(int i, int j, int k, int GRID_SIZE) {
		
		return i * GRID_SIZE * GRID_SIZE + j * GRID_SIZE + k;
	}
	
	public static int toHashNum(Coord3D_Debug coord, int GRID_SIZE) {
		
		return coord.i * GRID_SIZE * GRID_SIZE + coord.j * GRID_SIZE + coord.k;
	}
	
	public static final boolean DEBUG_SHOW_ROTS = true;
	
	public static void printSolution(Coord3D paperToDevelop[], long numSolutionsSoFar, boolean isCompleteSolution, int paperToDevelopLength) {
		int borders[][] = getBorders(paperToDevelop, paperToDevelopLength);
		

		if(isCompleteSolution) {
			System.out.println("Solution " + numSolutionsSoFar + ":");
		} else {
			System.out.println("DEBUG PRINT CURRENT INCOMPLETE cuboidToBuild. Solutions so far: " + numSolutionsSoFar);
			System.out.println("Drawing of cuboid to be built:");
		}
		//(k is the first loop because that matches how I drew it in the notebook)
		for(int k=borders[2][0]; k<=borders[2][1]; k++) {
			
			for(int i=borders[0][0]; i<=borders[0][1]; i++) {
				
				for(int j=borders[1][0]; j<=borders[1][1]; j++) {
					
					boolean found = false;
					int indexFound = -1;
					for(int m=0; m<paperToDevelopLength; m++) {
						if(paperToDevelop[m].i == i && paperToDevelop[m].j == j && paperToDevelop[m].k == k) {
							found = true;
							indexFound = m;
							break;
						}
					}
					
					if(DEBUG_SHOW_ROTS) {
						if(found) {
							if(paperToDevelop[indexFound] instanceof Coord3D_Debug) {
	
								String SPACE_REMAINING = "     ".substring(("" + ((Coord3D_Debug)paperToDevelop[indexFound]).debugIndex).length());
								
								System.out.print(((Coord3D_Debug)paperToDevelop[indexFound]).debugIndex + "," + ((Coord3D_Debug)paperToDevelop[indexFound]).debugDir1 + "," + ((Coord3D_Debug)paperToDevelop[indexFound]).debugDir2 + SPACE_REMAINING);
							} else {
								System.out.print("#########");
							}
						} else {
							System.out.print("______   ");
							
						}
					} else {
						if(found) {
							if(paperToDevelop[indexFound] instanceof Coord3D_Debug) {
	
								System.out.print(((Coord3D_Debug)paperToDevelop[indexFound]).debugIndex);
							} else {
								System.out.print("#");
							}
						} else {
							System.out.print("_");
							
						}
					}
				}

				System.out.println();
			}
			
			System.out.println();
			System.out.println();
			System.out.println();
		}
		
	}
	
}
