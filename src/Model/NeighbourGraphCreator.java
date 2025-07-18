package Model;


import Coord.CoordWithRotationAndIndex;

public class NeighbourGraphCreator {

	//Goal:
	// Give CuboidToFoldOn a graph/state machine to work with, so it can easily keep track of state.
	// This goal seems to already be acomplished and what's returned looks good.
	// TODO: I still need to test it against a 1x1x1 cube though...
	
	public static int NUM_NEIGHBOURS = 6;
	public static int NUM_DIMS = 6;

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
	
	
	// post: neighToUse[][][]
	// where:
	// 1st index is the globalIndex
	// 2nd one is which model dir points at block view dir 0
	// 2nd one is which model dir points at block view dir 1
	// Note that the 2nd and 3rd one will always be orthogonal.
	public static int[][][] setupNeighToUse() {
		
		int neighToUse[][][] = new int[NUM_NEIGHBOURS][NUM_NEIGHBOURS][NUM_NEIGHBOURS];
		
		int dirsByGlobalIndex[][] = new int[NUM_NEIGHBOURS][NUM_DIMS];
		
		for(int i=0; i<dirsByGlobalIndex.length; i++) {
			for(int j=0; j<dirsByGlobalIndex[0].length; j++) {
				dirsByGlobalIndex[i][j] = 0;
			}
		}
		dirsByGlobalIndex[0][0] = 1;
		dirsByGlobalIndex[1][1] = 1;
		dirsByGlobalIndex[2][2] = 1;
		
		dirsByGlobalIndex[3][0] = -1;
		dirsByGlobalIndex[4][1] = -1;
		dirsByGlobalIndex[5][2] = -1;
		
		for(int i=0; i<NUM_NEIGHBOURS; i++) {
			
			
			for(int j=0; j<NUM_NEIGHBOURS; j++) {
				int mainDirRelativeStandard[] = dirsByGlobalIndex[j];
				
				for(int k=0; k<NUM_NEIGHBOURS; k++) {
					int secondaryDirRelativeStandard[] = dirsByGlobalIndex[k];
					int crossProd[] = crossMult(mainDirRelativeStandard, secondaryDirRelativeStandard);
					
					if(sameVector(mainDirRelativeStandard, secondaryDirRelativeStandard)
							|| sameVector(mainDirRelativeStandard, multVectorMinus1(secondaryDirRelativeStandard))) {
						
						//mainDirRelativeStandard and secondaryDirRelativeStandard must always be orthogonal by definition:
						neighToUse[i][j][k] = -1;
						
					} else if(i == 0) {
						neighToUse[i][j][k] = getIndexDir(dirsByGlobalIndex, mainDirRelativeStandard);
						
						if(neighToUse[i][j][k] != j) {
							System.out.println("ERROR 2");
							System.exit(1);
						}
						
					} else if(i == 1) {
						neighToUse[i][j][k] = getIndexDir(dirsByGlobalIndex, secondaryDirRelativeStandard);
						
						if(neighToUse[i][j][k] != k) {
							System.out.println("ERROR 3");
							System.exit(1);
						}
						
					} else if(i == 2) {
						neighToUse[i][j][k] = getIndexDir(dirsByGlobalIndex, crossProd);
						
					} else if(i == 3) {
						neighToUse[i][j][k] = getIndexDir(dirsByGlobalIndex, multVectorMinus1(mainDirRelativeStandard));
						
					} else if(i == 4) {
						neighToUse[i][j][k] = getIndexDir(dirsByGlobalIndex, multVectorMinus1(secondaryDirRelativeStandard));
						
					} else if(i == 5) {
						neighToUse[i][j][k] =  getIndexDir(dirsByGlobalIndex, multVectorMinus1(crossProd));
						
					} else {
						System.out.println("( " +i + ", " + j + " , " + k + ")");
						System.out.println("AAAH crap!");
						printVector(mainDirRelativeStandard);
						printVector(secondaryDirRelativeStandard);
						printVector(crossProd);
						System.exit(1);
					}
					
					//System.out.println("( " +i + ", " + j + " , " + k + "): " + neighToUse[i][j][k]);
				}
			}
		}
	
		return neighToUse;
	}
	
	public static int[] crossMult(int a[], int b[]) {
		int ret[] = new int[NUM_DIMS];
		
		ret[0] = a[1] * b[2] - a[2] * b[1];
		ret[1] = -(a[0] * b[2] - a[2] * b[0]);
		ret[2] = a[0] * b[1] - a[1] * b[0];
		
		
		return ret;
	}
	
	private static int[] multVectorMinus1(int a[]) {
		return new int[] {-a[0], -a[1], -a[2]};
	}
	
	private static boolean sameVector(int a[], int b[]) {
		return a[0] == b[0] && a[1] == b[1] && a[2] == b[2];
	}
	
	public static void main(String args[]) {
		int result[][][] = setupNeighToUse();
		
		
		for(int i=0; i<result.length; i++) {

			System.out.println("i = " + i);
			for(int j=0; j<result[0].length; j++) {
				for(int k=0; k<result[0].length; k++) {
					
					if(("" + result[i][j][k]).length() == 1) {
						System.out.print(" ");
					}
					System.out.print("       " + result[i][j][k]);
				}
				System.out.println();
			}
			System.out.println();
			System.out.println();
			System.out.println();
		}
	}
	
	public static int getIndexDir(int dirsByGlobalIndex[][], int a[]) {
		
		int ret = -2;
		
		for(int i=0; i<dirsByGlobalIndex.length; i++) {
			if(sameVector(dirsByGlobalIndex[i], a)) {
				return i;
			}
		}
		
		System.out.println("ERROR in getIndexDir of NeighbourGraphCreator");
		System.exit(1);
		
		return ret;
	}
	
	public static void printVector(int a[]) {
		String ret = "[";
		for(int i=0; i<a.length; i++) {
			System.out.print(" " + a[i] + ",");
		}
		System.out.println(ret + "]");
	}
}
