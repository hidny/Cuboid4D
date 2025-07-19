package Model;


import Coord.CoordWithRotationAndIndex;
import Coord.Neighbour3DDesc;

public class NeighbourGraphCreator {

	//Goal:
	// Give CuboidToFoldOn a graph/state machine to work with, so it can easily keep track of state.
	// This goal seems to already be acomplished and what's returned looks good.
	// TODO: I still need to test it against a 1x1x1 cube though...
	
	public static int NUM_NEIGHBOURS = 6;
	public static int NUM_DIMS = 6;

	public static final int I = 0;
	public static final int J = 1;
	public static final int K = 2;
	

	public static Neighbour3DDesc[][] initNeighbourhood(int a, int b, int c, int d) {

		//TODO: this is not intuitive to me...
		Neighbour3DDesc neighbours[][] = new Neighbour3DDesc[Utils.getSurfaceVolume(a, b, c, d)][NUM_NEIGHBOURS];
		//Neighbour3DDesc(int cellIndex, int rotAxis, int rotAmount)
		
		if(b == 1 && c == 1 && d == 1) {
			
			//TODO: I'm 99.99999% sure I messed this up somehow:
			
			//TODO: I'm tried and guessed the rotations:
			neighbours[0][0] = new Neighbour3DDesc(6, J, 1);
			neighbours[0][1] = new Neighbour3DDesc(4, I, 3);
			neighbours[0][2] = new Neighbour3DDesc(3, I, 0);
			neighbours[0][3] = new Neighbour3DDesc(1, J, 3);
			neighbours[0][4] = new Neighbour3DDesc(2, I, 1);
			neighbours[0][5] = new Neighbour3DDesc(5, I, 2);

			//TODO: I'm tried and guessed the rotations:
			neighbours[1][5] = new Neighbour3DDesc(0, J, 3);
			neighbours[2][5] = new Neighbour3DDesc(0, I, 1);
			neighbours[3][5] = new Neighbour3DDesc(0, I, 0);
			neighbours[4][5] = new Neighbour3DDesc(0, I, 3);
			neighbours[5][5] = new Neighbour3DDesc(0, I, 2);
			neighbours[6][5] = new Neighbour3DDesc(0, J, 1);
			
			
			//In between connections attaching on k direction:
			for(int i=0; i<a-1; i++) {

				int baseIndex = 6*i;
				
				for(int j=0; j<6; j++) {
					int startIndex = j + baseIndex;
					int endIndex =   j + 6*(baseIndex + 1);
					
					neighbours[startIndex][2] = new Neighbour3DDesc(endIndex, 0, 0);
					neighbours[endIndex][5] =   new Neighbour3DDesc(startIndex, 0, 0);
				}
				
			}
			//In between connections attaching on i and j directions: (rotate with k axis)
			for(int i=0; i<a; i++) {
				
				int baseIndex = 6*i;
				
				neighbours[baseIndex + 1][3] = new Neighbour3DDesc(baseIndex + 5, K, 2);
				neighbours[baseIndex + 1][1] = new Neighbour3DDesc(baseIndex + 4, K, 3);
				neighbours[baseIndex + 1][0] = new Neighbour3DDesc(baseIndex + 3, K, 0);
				neighbours[baseIndex + 1][4] = new Neighbour3DDesc(baseIndex + 2, K, 1);
				

				neighbours[baseIndex + 2][3] = new Neighbour3DDesc(baseIndex + 1, K, 3);
				neighbours[baseIndex + 2][1] = new Neighbour3DDesc(baseIndex + 3, K, 0);
				neighbours[baseIndex + 2][0] = new Neighbour3DDesc(baseIndex + 6, K, 1);
				neighbours[baseIndex + 2][4] = new Neighbour3DDesc(baseIndex + 5, K, 0);
				
				neighbours[baseIndex + 3][3] = new Neighbour3DDesc(baseIndex + 1, K, 0);
				neighbours[baseIndex + 3][1] = new Neighbour3DDesc(baseIndex + 4, K, 0);
				neighbours[baseIndex + 3][0] = new Neighbour3DDesc(baseIndex + 6, K, 0);
				neighbours[baseIndex + 3][4] = new Neighbour3DDesc(baseIndex + 2, K, 0);


				neighbours[baseIndex + 4][3] = new Neighbour3DDesc(baseIndex + 1, K, 1);
				neighbours[baseIndex + 4][1] = new Neighbour3DDesc(baseIndex + 5, K, 0);
				neighbours[baseIndex + 4][0] = new Neighbour3DDesc(baseIndex + 6, K, 3);
				neighbours[baseIndex + 4][4] = new Neighbour3DDesc(baseIndex + 3, K, 0);
				

				neighbours[baseIndex + 5][3] = new Neighbour3DDesc(baseIndex + 1, K, 2);
				neighbours[baseIndex + 5][1] = new Neighbour3DDesc(baseIndex + 2, K, 0);
				neighbours[baseIndex + 5][0] = new Neighbour3DDesc(baseIndex + 6, K, 2);
				neighbours[baseIndex + 5][4] = new Neighbour3DDesc(baseIndex + 4, K, 0);
				

				neighbours[baseIndex + 6][3] = new Neighbour3DDesc(baseIndex + 3, K, 0);
				neighbours[baseIndex + 6][1] = new Neighbour3DDesc(baseIndex + 4, K, 1);
				neighbours[baseIndex + 6][0] = new Neighbour3DDesc(baseIndex + 5, K, 2);
				neighbours[baseIndex + 6][4] = new Neighbour3DDesc(baseIndex + 2, K, 3);
				
			}
			
			
			int lastIndex = Utils.getSurfaceVolume(a, b, c, d) - 1;
			int baseIndex = lastIndex - 7;
			neighbours[lastIndex][0] = new Neighbour3DDesc(baseIndex + 6, J, 1);
			neighbours[lastIndex][1] = new Neighbour3DDesc(baseIndex + 4, I, 1);
			neighbours[lastIndex][2] = new Neighbour3DDesc(baseIndex + 5, I, 2);
			neighbours[lastIndex][3] = new Neighbour3DDesc(baseIndex + 1, J, 3);
			neighbours[lastIndex][4] = new Neighbour3DDesc(baseIndex + 2, I, 3);
			neighbours[lastIndex][5] = new Neighbour3DDesc(baseIndex + 3, I, 0);
			

			//TODO: I'm tried and guessed the rotations:
			neighbours[baseIndex + 1][2] = new Neighbour3DDesc(lastIndex, J, 3);
			neighbours[baseIndex + 2][2] = new Neighbour3DDesc(lastIndex, I, 3);
			neighbours[baseIndex + 3][2] = new Neighbour3DDesc(lastIndex, I, 0);
			neighbours[baseIndex + 4][2] = new Neighbour3DDesc(lastIndex, I, 1);
			neighbours[baseIndex + 5][2] = new Neighbour3DDesc(lastIndex, I, 2);
			neighbours[baseIndex + 6][2] = new Neighbour3DDesc(lastIndex, J, 1);
			
			
		} else if(b == 2 && c == 1 && d == 1) {
			
			//TODO
			
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
