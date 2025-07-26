package Model;


import Coord.CoordWithRotationAndIndex;
import Coord.Neighbour3DDesc;

public class NeighbourGraphCreator {

	//Goal:
	// Give CuboidToFoldOn a graph/state machine to work with, so it can easily keep track of state.
	// This goal seems to already be accomplished and what's returned looks good.
	
	public static int NUM_NEIGHBOURS = 6;
	public static int NUM_DIMS = 6;

	public static final int I = 0;
	public static final int J = 1;
	public static final int K = 2;
	

	public static Neighbour3DDesc[][] initNeighbourhood(int a, int b, int c, int d) {

		Neighbour3DDesc neighbours[][] = new Neighbour3DDesc[Utils.getSurfaceVolume(a, b, c, d)][NUM_NEIGHBOURS];
		
		if(b == 1 && c == 1 && d == 1) {
			
			neighbours[0][0] = new Neighbour3DDesc(6, J, 1);
			neighbours[0][1] = new Neighbour3DDesc(4, I, 3);
			neighbours[0][2] = new Neighbour3DDesc(3, I, 0);
			neighbours[0][3] = new Neighbour3DDesc(1, J, 3);
			neighbours[0][4] = new Neighbour3DDesc(2, I, 1);
			neighbours[0][5] = new Neighbour3DDesc(5, I, 2);

			neighbours[1][5] = new Neighbour3DDesc(0, J, 1);
			neighbours[2][5] = new Neighbour3DDesc(0, I, 3);
			neighbours[3][5] = new Neighbour3DDesc(0, I, 0);
			neighbours[4][5] = new Neighbour3DDesc(0, I, 1);
			neighbours[5][5] = new Neighbour3DDesc(0, I, 2);
			neighbours[6][5] = new Neighbour3DDesc(0, J, 3);
			
			
			//In between connections attaching on k direction:
			for(int i=0; i<a-1; i++) {

				int baseIndex = 6*i + 1;
				
				for(int j=0; j<6; j++) {
					int startIndex = j + baseIndex;
					int endIndex =   j + baseIndex + 6;
					
					neighbours[startIndex][2] = new Neighbour3DDesc(endIndex, 0, 0);
					neighbours[endIndex][5] =   new Neighbour3DDesc(startIndex, 0, 0);
				}
				
			}
			//In between connections attaching on i and j directions: (rotate with k axis)
			for(int i=0; i<a; i++) {
				
				int baseIndex = 6*i;
				
				neighbours[baseIndex + 1][3] = new Neighbour3DDesc(baseIndex + 5, K, 2);
				neighbours[baseIndex + 1][1] = new Neighbour3DDesc(baseIndex + 4, K, 1);
				neighbours[baseIndex + 1][0] = new Neighbour3DDesc(baseIndex + 3, K, 0);
				neighbours[baseIndex + 1][4] = new Neighbour3DDesc(baseIndex + 2, K, 3);
				

				neighbours[baseIndex + 2][3] = new Neighbour3DDesc(baseIndex + 1, K, 1);
				neighbours[baseIndex + 2][1] = new Neighbour3DDesc(baseIndex + 3, K, 0);
				neighbours[baseIndex + 2][0] = new Neighbour3DDesc(baseIndex + 6, K, 3);
				neighbours[baseIndex + 2][4] = new Neighbour3DDesc(baseIndex + 5, K, 0);
				
				neighbours[baseIndex + 3][3] = new Neighbour3DDesc(baseIndex + 1, K, 0);
				neighbours[baseIndex + 3][1] = new Neighbour3DDesc(baseIndex + 4, K, 0);
				neighbours[baseIndex + 3][0] = new Neighbour3DDesc(baseIndex + 6, K, 0);
				neighbours[baseIndex + 3][4] = new Neighbour3DDesc(baseIndex + 2, K, 0);


				neighbours[baseIndex + 4][3] = new Neighbour3DDesc(baseIndex + 1, K, 3);
				neighbours[baseIndex + 4][1] = new Neighbour3DDesc(baseIndex + 5, K, 0);
				neighbours[baseIndex + 4][0] = new Neighbour3DDesc(baseIndex + 6, K, 1);
				neighbours[baseIndex + 4][4] = new Neighbour3DDesc(baseIndex + 3, K, 0);
				

				neighbours[baseIndex + 5][3] = new Neighbour3DDesc(baseIndex + 1, K, 2);
				neighbours[baseIndex + 5][1] = new Neighbour3DDesc(baseIndex + 2, K, 0);
				neighbours[baseIndex + 5][0] = new Neighbour3DDesc(baseIndex + 6, K, 2);
				neighbours[baseIndex + 5][4] = new Neighbour3DDesc(baseIndex + 4, K, 0);
				

				neighbours[baseIndex + 6][3] = new Neighbour3DDesc(baseIndex + 3, K, 0);
				neighbours[baseIndex + 6][1] = new Neighbour3DDesc(baseIndex + 4, K, 3);
				neighbours[baseIndex + 6][0] = new Neighbour3DDesc(baseIndex + 5, K, 2);
				neighbours[baseIndex + 6][4] = new Neighbour3DDesc(baseIndex + 2, K, 1);
				
			}
			
			
			int lastIndex = Utils.getSurfaceVolume(a, b, c, d) - 1;
			int baseIndex = lastIndex - 7;
			neighbours[lastIndex][0] = new Neighbour3DDesc(baseIndex + 6, J, 3);
			neighbours[lastIndex][1] = new Neighbour3DDesc(baseIndex + 4, I, 1);
			neighbours[lastIndex][2] = new Neighbour3DDesc(baseIndex + 5, I, 2);
			neighbours[lastIndex][3] = new Neighbour3DDesc(baseIndex + 1, J, 1);
			neighbours[lastIndex][4] = new Neighbour3DDesc(baseIndex + 2, I, 3);
			neighbours[lastIndex][5] = new Neighbour3DDesc(baseIndex + 3, I, 0);
			

			neighbours[baseIndex + 1][2] = new Neighbour3DDesc(lastIndex, J, 3);
			neighbours[baseIndex + 2][2] = new Neighbour3DDesc(lastIndex, I, 1);
			neighbours[baseIndex + 3][2] = new Neighbour3DDesc(lastIndex, I, 0);
			neighbours[baseIndex + 4][2] = new Neighbour3DDesc(lastIndex, I, 3);
			neighbours[baseIndex + 5][2] = new Neighbour3DDesc(lastIndex, I, 2);
			neighbours[baseIndex + 6][2] = new Neighbour3DDesc(lastIndex, J, 1);
			

			//Validation check:
			for(int j=1; j<=6; j++) {
				
				if(j == 3 || j == 5) {
					validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{0, j}, {baseIndex + j, lastIndex}});
				
				} else if(j == 1 || j == 2){
					int k = -1;
					
					if(j == 1) {
						k = 6;
					} else if(j == 2) {
						k = 4;
					}
					
					validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{0, j}, {baseIndex + j, lastIndex}, {lastIndex, baseIndex + k}, {k, 0}});
					
				}
			}
			
			System.out.println("Check around:");
			validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{1, 4}, {4, 6}, {6, 2}, {2, 1}});
			
			validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{1, 3}, {3, 6}});
			validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{2, 3}, {3, 4}, {4, 5}, {5, 2}});
			
			validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{1, 5}, {5, 6}});
			
			System.out.println("END OF VALIDATION CHECKS");
			
			
		} else if(b == 2 && c == 1 && d == 1) {
			

			// Wrong: 3,2,0   13,1,0   11,5,0   1,1,0
			
			//Neighbours to index 0:
			neighbours[0][0] = new Neighbour3DDesc(10, J, 1);
			neighbours[0][1] = new Neighbour3DDesc(1, 0, 0);
			neighbours[0][2] = new Neighbour3DDesc(5, 0, 0);
			neighbours[0][3] = new Neighbour3DDesc(2, J, 3);
			neighbours[0][4] = new Neighbour3DDesc(4, I, 1);
			neighbours[0][5] = new Neighbour3DDesc(9, I, 2);


			//Going back to 0:
			neighbours[1][4] = new Neighbour3DDesc(0, 0, 0);
			
			neighbours[2][5] = new Neighbour3DDesc(0, J, 1);
			neighbours[4][5] = new Neighbour3DDesc(0, I, 3);
			neighbours[5][5] = new Neighbour3DDesc(0, I, 0);
			neighbours[9][5] = new Neighbour3DDesc(0, I, 2);
			neighbours[10][5] = new Neighbour3DDesc(0, J, 3);
			

			//Neighbours to index 1:
			neighbours[1][0] = new Neighbour3DDesc(11, J, 1);
			neighbours[1][1] = new Neighbour3DDesc(7, I, 3);
			neighbours[1][2] = new Neighbour3DDesc(6, I, 0);
			neighbours[1][3] = new Neighbour3DDesc(3, J, 3);
			
			neighbours[1][4] = new Neighbour3DDesc(0, 0, 0);
			
			neighbours[1][5] = new Neighbour3DDesc(8, I, 2);

			//Going back to 1:

			neighbours[0][1] = new Neighbour3DDesc(1, 0, 0);
			
			neighbours[3][5] = new Neighbour3DDesc(1, J, 1);
			
			
			neighbours[6][5] = new Neighbour3DDesc(1, 0, 0);
			neighbours[7][5] = new Neighbour3DDesc(1, I, 1);
			neighbours[8][5] = new Neighbour3DDesc(1, I, 2);
			neighbours[11][5] = new Neighbour3DDesc(1, J, 3);

			//In between connections attaching on k direction:
			for(int i=0; i<a-1; i++) {

				int baseIndex = 10*i + 2;
				
				for(int j=0; j<10; j++) {
					int startIndex = j + baseIndex;
					int endIndex =   j + baseIndex + 10;
					
					neighbours[startIndex][2] = new Neighbour3DDesc(endIndex, 0, 0);
					neighbours[endIndex][5] =   new Neighbour3DDesc(startIndex, 0, 0);
				}
				
			}
			
			
			//In between connections attaching on i and j directions: (rotate with k axis)
			for(int i=0; i<a; i++) {
				
				int baseIndex = 10*i;
				neighbours[baseIndex + 2][3] = new Neighbour3DDesc(baseIndex + 9, K, 2);
				neighbours[baseIndex + 2][1] = new Neighbour3DDesc(baseIndex + 3, K, 0);
				neighbours[baseIndex + 2][0] = new Neighbour3DDesc(baseIndex + 5, K, 0);
				neighbours[baseIndex + 2][4] = new Neighbour3DDesc(baseIndex + 4, K, 3);
				
				neighbours[baseIndex + 3][3] = new Neighbour3DDesc(baseIndex + 8, K, 2);
				neighbours[baseIndex + 3][1] = new Neighbour3DDesc(baseIndex + 7, K, 1);
				neighbours[baseIndex + 3][0] = new Neighbour3DDesc(baseIndex + 6, K, 0);
				neighbours[baseIndex + 3][4] = new Neighbour3DDesc(baseIndex + 2, K, 0);


				neighbours[baseIndex + 4][3] = new Neighbour3DDesc(baseIndex + 2, K, 1);
				neighbours[baseIndex + 4][1] = new Neighbour3DDesc(baseIndex + 5, K, 0);
				neighbours[baseIndex + 4][0] = new Neighbour3DDesc(baseIndex + 10, K, 3);
				neighbours[baseIndex + 4][4] = new Neighbour3DDesc(baseIndex + 9, K, 0);
				

				neighbours[baseIndex + 5][3] = new Neighbour3DDesc(baseIndex + 2, K, 0);
				neighbours[baseIndex + 5][1] = new Neighbour3DDesc(baseIndex + 6, K, 0);
				neighbours[baseIndex + 5][0] = new Neighbour3DDesc(baseIndex + 10, K, 0);
				neighbours[baseIndex + 5][4] = new Neighbour3DDesc(baseIndex + 4, K, 0);
				

				neighbours[baseIndex + 6][3] = new Neighbour3DDesc(baseIndex + 3, K, 0);
				neighbours[baseIndex + 6][1] = new Neighbour3DDesc(baseIndex + 7, K, 0);
				neighbours[baseIndex + 6][0] = new Neighbour3DDesc(baseIndex + 11, K, 0);
				neighbours[baseIndex + 6][4] = new Neighbour3DDesc(baseIndex + 5, K, 0);


				neighbours[baseIndex + 7][3] = new Neighbour3DDesc(baseIndex + 3,  K, 3);
				neighbours[baseIndex + 7][1] = new Neighbour3DDesc(baseIndex + 8,  K, 0);
				neighbours[baseIndex + 7][0] = new Neighbour3DDesc(baseIndex + 11, K, 1);
				neighbours[baseIndex + 7][4] = new Neighbour3DDesc(baseIndex + 6,  K, 0);

				neighbours[baseIndex + 8][3] = new Neighbour3DDesc(baseIndex + 3,  K, 2);
				neighbours[baseIndex + 8][1] = new Neighbour3DDesc(baseIndex + 7,  K, 0);
				neighbours[baseIndex + 8][0] = new Neighbour3DDesc(baseIndex + 11, K, 2);
				neighbours[baseIndex + 8][4] = new Neighbour3DDesc(baseIndex + 9,  K, 0);

				neighbours[baseIndex + 9][3] = new Neighbour3DDesc(baseIndex + 2,  K, 2);
				neighbours[baseIndex + 9][1] = new Neighbour3DDesc(baseIndex + 4,  K, 0);
				neighbours[baseIndex + 9][0] = new Neighbour3DDesc(baseIndex + 10, K, 2);
				neighbours[baseIndex + 9][4] = new Neighbour3DDesc(baseIndex + 8,  K, 0);

				neighbours[baseIndex + 10][3] = new Neighbour3DDesc(baseIndex +  5, K, 0);
				neighbours[baseIndex + 10][1] = new Neighbour3DDesc(baseIndex + 11, K, 0);
				neighbours[baseIndex + 10][0] = new Neighbour3DDesc(baseIndex +  9, K, 2);
				neighbours[baseIndex + 10][4] = new Neighbour3DDesc(baseIndex +  4, K, 1);


				neighbours[baseIndex + 11][3] = new Neighbour3DDesc(baseIndex +  6, K, 0);
				neighbours[baseIndex + 11][1] = new Neighbour3DDesc(baseIndex +  7, K, 3);
				neighbours[baseIndex + 11][0] = new Neighbour3DDesc(baseIndex +  8, K, 2);
				neighbours[baseIndex + 11][4] = new Neighbour3DDesc(baseIndex + 10, K, 0);
			}
			

			int lastIndex = Utils.getSurfaceVolume(a, b, c, d) - 1;
			int secondLastIndex = lastIndex - 1;
			
			int baseIndex = lastIndex - 13;
			neighbours[secondLastIndex][0] = new Neighbour3DDesc(baseIndex + 10, J, 3);
			neighbours[secondLastIndex][1] = new Neighbour3DDesc(baseIndex + 13, 0, 0);
			neighbours[secondLastIndex][2] = new Neighbour3DDesc(baseIndex + 9, I, 2);
			neighbours[secondLastIndex][3] = new Neighbour3DDesc(baseIndex + 2, J, 1);
			neighbours[secondLastIndex][4] = new Neighbour3DDesc(baseIndex + 4, I, 3);
			neighbours[secondLastIndex][5] = new Neighbour3DDesc(baseIndex + 5, 0, 0);

			neighbours[lastIndex][0] = new Neighbour3DDesc(baseIndex + 11, J, 3);
			neighbours[lastIndex][1] = new Neighbour3DDesc(baseIndex + 7, I, 1);
			neighbours[lastIndex][2] = new Neighbour3DDesc(baseIndex + 8, I, 2);
			neighbours[lastIndex][3] = new Neighbour3DDesc(baseIndex + 3, J, 1);
			neighbours[lastIndex][4] = new Neighbour3DDesc(baseIndex + 12, 0, 0);
			neighbours[lastIndex][5] = new Neighbour3DDesc(baseIndex + 6, 0, 0);
			

			neighbours[baseIndex + 2][2] = new Neighbour3DDesc(secondLastIndex, J, 3);
			neighbours[baseIndex + 3][2] = new Neighbour3DDesc(lastIndex, J, 3);
			
			
			neighbours[baseIndex + 4][2] = new Neighbour3DDesc(secondLastIndex, I, 1);
			
			neighbours[baseIndex + 5][2] = new Neighbour3DDesc(secondLastIndex, I, 0);
			neighbours[baseIndex + 6][2] = new Neighbour3DDesc(lastIndex, I, 0);
			
			neighbours[baseIndex + 7][2] = new Neighbour3DDesc(lastIndex, I, 3);
			
			neighbours[baseIndex + 8][2] = new Neighbour3DDesc(lastIndex, I, 2);
			neighbours[baseIndex + 9][2] = new Neighbour3DDesc(secondLastIndex, I, 2);
			
			neighbours[baseIndex + 10][2] = new Neighbour3DDesc(secondLastIndex, J, 1);
			neighbours[baseIndex + 11][2] = new Neighbour3DDesc(lastIndex, J, 1);
			
			
			
			//TODO: redo validation check.

			validateBidirectional(neighbours);

			//Validation check:
			for(int j=2; j<=11; j++) {
				
				if(j == 5 || j == 6 || j == 8 || j == 9) {
					
					int shouldAdd1 = 0;
					if( j == 6 || j == 8) {
						shouldAdd1 = 1;
					}
					
					validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{0 + shouldAdd1, j}, {baseIndex + j, secondLastIndex + shouldAdd1}});
				
				} else if(j == 2 || j == 4 || j == 3){
					int k = -1;
					int firstTopIndexToUse = -1;
					int secondTopIndexToUse = -1;
					
					int firstBottomIndexToUse = -1;
					int secondBottomIndexToUse = -1;
					
					if(j == 2) {
						k = 10;
						firstTopIndexToUse = 0;
						secondTopIndexToUse = 0;
						
						firstBottomIndexToUse = secondLastIndex;
						secondBottomIndexToUse = secondLastIndex;
						
					} else if(j == 4) {
					    k = 7;

						firstTopIndexToUse = 0;
						secondTopIndexToUse = 1;
						
						firstBottomIndexToUse = secondLastIndex;
						secondBottomIndexToUse = lastIndex;
						
					} else if( j == 3) {
						k = 11;
						validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{1, 3}, {0, 2}});

						firstTopIndexToUse = 1;
						secondTopIndexToUse = 1;
						firstBottomIndexToUse = lastIndex;
						secondBottomIndexToUse = lastIndex;
						
					}
					
					
					validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{firstTopIndexToUse, j}, {baseIndex + j, firstBottomIndexToUse}, {secondBottomIndexToUse, baseIndex + k}, {k, secondTopIndexToUse}});
					
					
				}
			}
			
			System.out.println("Check around:");
			validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{3, 7}, {7, 11}, {10, 4}, {4, 2}});
			
			validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{2, 5}, {5, 10}, {3, 6}, {6, 11}, {4, 5}});
			validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{4, 5}, {5, 6}, {7, 8}, {8, 9}, {9, 4}, {2, 5}, {3, 6}, {5, 10}, {6, 11}});
			
			validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{2, 9}, {9, 10}, {3, 8}, {8, 11}});
			

			validationCheckSameRotationAxisAndAmt(neighbours, new int[][] {{0, 1}, {secondLastIndex, lastIndex}, {0, 5}, {1, 6}, {secondLastIndex - 7, secondLastIndex}, {lastIndex - 7, lastIndex}});

			
			System.out.println("END OF VALIDATION CHECKS");
			
			
			
		} else {
			System.out.println("Not ready for different dimensions yet");
			System.exit(1);
		}

		validateBidirectional(neighbours);
		
		return neighbours;
	}
	
	public static void validateBidirectional(Neighbour3DDesc neighbours[][]) {
		
		for(int i=0; i<neighbours.length; i++) {
			for(int j=0; j<neighbours[0].length; j++) {
				int indexNeigh = neighbours[i][j].cellIndex;
				
				if(indexNeigh == i) {
					System.out.println("ERROR in validateBidirectional: cell index pointing to itself.");
					System.exit(1);
				}
				
				boolean isBidirectional = false;
				for(int k=0; k<neighbours[0].length; k++) {
				
					if(neighbours[indexNeigh][k].cellIndex == i) {
						isBidirectional = true;
					}
				}
				

				if(! isBidirectional) {
					System.out.println("ERROR in validateBidirectional: cell index " + i + " points to index " + indexNeigh + ", but that neighbour does not point back to cell index "+ i);
					System.exit(1);
				}
				
			}
		}
	}
	
	//The idea of this is to reduce the potential for magic number entry error in initNeighbourhood.
	public static void validationCheckSameRotationAxisAndAmt(Neighbour3DDesc neighbours[][], int listNeighbours[][]) {
		
		validationCheckSameRotationAxisAndAmtPosDir(neighbours, listNeighbours);
		
		int revListNeighbours[][] = new int[listNeighbours.length][2];
		
		for(int i=0; i<revListNeighbours.length; i++) {
			for(int j=0; j<revListNeighbours[0].length; j++) {
				revListNeighbours[i][j] = listNeighbours[revListNeighbours.length - 1 - i][revListNeighbours[0].length - 1 - j];
			}
		}
		validationCheckSameRotationAxisAndAmtPosDir(neighbours, revListNeighbours);
		
		validationCheckSameRotationAxisAndAmtForwardAndRev(neighbours, listNeighbours);
	}
	
	private static void validationCheckSameRotationAxisAndAmtForwardAndRev(Neighbour3DDesc neighbours[][], int listNeighbours[][]) {

		System.out.println("validationCheckSameRotationAxisAndAmtForwardAndRev:");
		
		for(int i=0; i<listNeighbours.length; i++) {
			int firstRotAxis = -1;
			int firstRotAmount = -1;
			
			for(int j=0; j<NUM_NEIGHBOURS; j++) {
				if(neighbours[listNeighbours[i][0]][j].cellIndex == listNeighbours[i][1]) {
					firstRotAxis = neighbours[listNeighbours[i][0]][j].rotAxis;
					firstRotAmount = neighbours[listNeighbours[i][0]][j].rotAmount;
				}
			}
			
			if(firstRotAxis == -1) {
				System.out.println("ERROR: rotAmount for index " + i + " does not exist in validationCheckSameRotationAxisAndAmtForwardAndRev does not exist.");
				
				System.out.println("Relevant link: " + listNeighbours[i][0] + " -> " + listNeighbours[i][1]);
				printList(listNeighbours);
				System.exit(1);
			}
			int secondRotAxis = -1;
			int secondRotAmount = -1;
			for(int j=0; j<NUM_NEIGHBOURS; j++) {
				if(neighbours[listNeighbours[i][1]][j].cellIndex == listNeighbours[i][0]) {
					secondRotAxis = neighbours[listNeighbours[i][1]][j].rotAxis;
					secondRotAmount = neighbours[listNeighbours[i][1]][j].rotAmount;
				}
			}
			
			if(secondRotAxis == -1) {
				System.out.println("ERROR: rotAmount for index " + i + " going backwards does not exist in validationCheckSameRotationAxisAndAmtForwardAndRev does not exist.");
				
				System.out.println("Relevant link: " + listNeighbours[i][1] + " -> " + listNeighbours[i][0]);
				printList(listNeighbours);
				System.exit(1);
			}
			
			if((firstRotAmount + secondRotAmount) % 4 != 0) {
				System.out.println("ERROR: Amounts for index " + i + " do not add up to a multiple of 4 (or 360 degrees) in validationCheckSameRotationAxisAndAmtForwardAndRev does not exist.");
				printList(listNeighbours);
				System.exit(1);
				
			}
			if(firstRotAxis != secondRotAxis) {
				System.out.println("ERROR: rotAxis for index " + i + " going backwards does not match the axis going forward in validationCheckSameRotationAxisAndAmtForwardAndRev does not exist.");
				printList(listNeighbours);
				System.exit(1);
				
			}
		}

		System.out.println("Done validationCheckSameRotationAxisAndAmtForwardAndRev() for: ");
		printList(listNeighbours);
		System.out.println();
	}

	private static void validationCheckSameRotationAxisAndAmtPosDir(Neighbour3DDesc neighbours[][], int listNeighbours[][]) {
		
		System.out.println("ValidationCheckSameRotationAxisAndAmt:");
		int rotAxis = -1;
		int rotAmount = -1;
		
		for(int j=0; j<NUM_NEIGHBOURS; j++) {
			if(neighbours[listNeighbours[0][0]][j].cellIndex == listNeighbours[0][1]) {
				rotAxis = neighbours[listNeighbours[0][0]][j].rotAxis;
				rotAmount = neighbours[listNeighbours[0][0]][j].rotAmount;
			}
		}
		if(rotAxis == -1) {
			System.out.println("ERROR: first connection in sanitySameRotationAxisAndAmt does not exist.");
			System.out.println("Relevant link: " + listNeighbours[0][0] + " -> " + listNeighbours[0][1]);
			printList(listNeighbours);
			System.exit(1);
		}
		
		for(int i=1; i<listNeighbours.length; i++) {
			
			int tmpRotAxis = -1;
			int tmpRotAmount = -1;
			for(int j=0; j<NUM_NEIGHBOURS; j++) {
				
				System.out.println("i,j: " + i + ", " + j);
				if(neighbours[listNeighbours[i][0]][j].cellIndex == listNeighbours[i][1]) {
					tmpRotAxis = neighbours[listNeighbours[i][0]][j].rotAxis;
					tmpRotAmount = neighbours[listNeighbours[i][0]][j].rotAmount;
					
					if(tmpRotAxis != rotAxis) {
						System.out.println("ERROR: rotAxis for index " + i + " does not match 1st rotAxis in sanitySameRotationAxisAndAmt does not exist.");
						printList(listNeighbours);
						System.out.println("Relevant link: " + listNeighbours[i][0] + " -> " + listNeighbours[i][1]);
						System.exit(1);
					}

					if(tmpRotAmount != rotAmount) {
						System.out.println("ERROR: rotAmount for index " + i + " does not match 1st rotAmount in sanitySameRotationAxisAndAmt does not exist.");
						printList(listNeighbours);
						System.out.println("Relevant link: " + listNeighbours[i][0] + " -> " + listNeighbours[i][1]);
						System.exit(1);
					}
					
					break;
				}
			}
			if(tmpRotAxis == -1) {
				System.out.println("ERROR: rotAmount for index " + i + " does not exist in sanitySameRotationAxisAndAmt does not exist.");
				printList(listNeighbours);
				System.out.println("Relevant link: " + listNeighbours[i][0] + " -> " + listNeighbours[i][1]);
				System.exit(1);
			}
			
		}
		
		System.out.println("Done ValidationCheckSameRotationAxisAndAmt() for: ");
		printList(listNeighbours);
		System.out.println();
		
	}
	
	private static void printList(int listNeighbours[][]) {
		System.out.println("List neighbours with same rotAxis and rotation Axis Amount:");
		for(int i=0; i<listNeighbours.length; i++) {
			
			if(i < listNeighbours.length - 1) {
				System.out.print(listNeighbours[i][0] + " -> " + listNeighbours[i][1] + ", ");
			} else {

				System.out.print(listNeighbours[i][0] + " -> " + listNeighbours[i][1] + ".");
			}
		}
		System.out.println();
	}
	
	
	// post: neighToUse[][][]
	// where:
	// 1st index is the globalIndex
	// 2nd one is the block index where model index 0 points. (0: i + direction)
	// 3rd one is the block index where model index 1 points. (1: j + direction)
	// Note that the 2nd and 3rd one will always be orthogonal.
	// Output: which local block index to attach new cell to.
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
						
					} else if(i == j) {
						neighToUse[i][j][k] = 0;
						
						if (! sameVector(dirsByGlobalIndex[i], mainDirRelativeStandard)) {
							System.out.println("ERROR 2");
							System.exit(1);
						}
						
					} else if(i == k) {
						neighToUse[i][j][k] = 1;

						if (! sameVector(dirsByGlobalIndex[i], secondaryDirRelativeStandard)) {
							System.out.println("ERROR 3");
							System.exit(1);
						}
						
					} else if(sameVector(dirsByGlobalIndex[i], crossProd)) {
						neighToUse[i][j][k] = 2;
						
					} else if(sameVector(dirsByGlobalIndex[i], multVectorMinus1(mainDirRelativeStandard))) {
						neighToUse[i][j][k] = 3;
						
					} else if(sameVector(dirsByGlobalIndex[i], multVectorMinus1(secondaryDirRelativeStandard))) {
						neighToUse[i][j][k] = 4;
						
					} else if(sameVector(dirsByGlobalIndex[i], multVectorMinus1(crossProd))) {
						neighToUse[i][j][k] =  5;
						
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
		

		System.out.println("#######################");
		System.out.println("#######################");
		System.out.println("#######################");
		System.out.println("#######################");
		System.out.println("#######################");
		System.out.println("Axis Test");
		System.out.println("Axis Test");
		System.out.println("Axis Test");
		System.out.println("Axis Test");
		System.out.println("Axis Test");
		result = setupAxisToUse();
		
		
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
	
	

	
	// post: setupAxisToUse[][][]
	// where:
	// 1st index is the localIndex
	// 2nd one is the block index where model index 0 points.
	// 3rd one is the block index where model index 1 points.
	// Note that the 2nd and 3rd one will always be orthogonal.
	// Output: which global block index to attach the rotation index
	
	//Note that in some ways, it's the inverse of neighToUse
	public static int[][][] setupAxisToUse() {
		int axisToUse[][][] = new int[NUM_NEIGHBOURS][NUM_NEIGHBOURS][NUM_NEIGHBOURS];
		
		int neighToUse[][][] = setupNeighToUse();
		
		for(int i=0; i<NUM_NEIGHBOURS; i++) {
			for(int j=0; j<NUM_NEIGHBOURS; j++) {
				for(int k=0; k<NUM_NEIGHBOURS; k++) {
					axisToUse[i][j][k] = -1;
				}
			}
		}
		

		for(int i=0; i<NUM_NEIGHBOURS; i++) {
			for(int j=0; j<NUM_NEIGHBOURS; j++) {
				for(int k=0; k<NUM_NEIGHBOURS; k++) {
					
					if(neighToUse[i][j][k] >= 0) {
						axisToUse[neighToUse[i][j][k]][j][k] = i;
					}
				}
			}
		}
		
		return axisToUse;
	}
}
