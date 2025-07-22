package solutionResolver;

import java.math.BigInteger;
import java.util.HashSet;

import Coord.Coord3D;
import Coord.Coord3D_Debug;

public class SolutionResolverMemBasic implements SolutionResolverInterface {

	
	public static HashSet <BigInteger> solutionsFound = new HashSet <BigInteger>();
	public BigInteger lastSolution = null;
	
	public BigInteger powersOfTwo[];
	
	public static final BigInteger TWO = new BigInteger("2");
	
	public SolutionResolverMemBasic() {
	
		powersOfTwo = new BigInteger[10000];
		
		BigInteger cur = BigInteger.ONE;
		
		for(int i=0; i<powersOfTwo.length; i++) {
			powersOfTwo[i] = cur;
			cur = cur.multiply(TWO);
		}
	}
	
	public BigInteger getPowTwo(int pow) {
		
		if(pow < powersOfTwo.length) {
			return powersOfTwo[pow];
		} else {
			return TWO.pow(pow);
		}
	}
	
	@Override
	public long getNumUniqueFound() {
		System.out.println("Getting numUniqueFound");
		return solutionsFound.size();
	}
	
	public static int NUM_PERM_3 = 6;
	public static int TWO_POW_3 = 8;


	public long resolveSolution(Coord3D paperToDevelop[]) {
		
		int borders[][] = getBorders(paperToDevelop);
		
		BigInteger ret = getScoreForPerm(paperToDevelop, borders, 0, 0);
		
		for(int i=0; i<NUM_PERM_3; i++) {
			for(int j=0; j<TWO_POW_3; j++) {
				BigInteger tmp = getScoreForPerm(paperToDevelop, borders, i, j);
				
				if(tmp.compareTo(ret) < 0) {
					ret = tmp;
				}
			}
		}
		
		if( ! solutionsFound.contains(ret)) {
			
			solutionsFound.add(ret);
			lastSolution = ret;
			printSolution(paperToDevelop);
			
			return 1;
		} else {
			return 0;
		}
		
	}
	
	public static final boolean DEBUG_SHOW_ROTS = true;
	
	public static void printSolution(Coord3D paperToDevelop[]) {
		int borders[][] = getBorders(paperToDevelop);
		

		System.out.println("Solution " + solutionsFound.size() + ":");
		//(k is the first loop because that matches how I drew it in the notebook)
		for(int k=borders[2][0]; k<=borders[2][1]; k++) {
			
			for(int i=borders[0][0]; i<=borders[0][1]; i++) {
				
				for(int j=borders[1][0]; j<=borders[1][1]; j++) {
					
					boolean found = false;
					int indexFound = -1;
					for(int m=0; m<paperToDevelop.length; m++) {
						if(paperToDevelop[m].i == i && paperToDevelop[m].j == j && paperToDevelop[m].k == k) {
							found = true;
							indexFound = m;
							break;
						}
					}
					
					if(DEBUG_SHOW_ROTS) {
						if(found) {
							if(paperToDevelop[indexFound] instanceof Coord3D_Debug) {
	
								System.out.print(((Coord3D_Debug)paperToDevelop[indexFound]).debugIndex + "," + ((Coord3D_Debug)paperToDevelop[indexFound]).debugDir1 + "," + ((Coord3D_Debug)paperToDevelop[indexFound]).debugDir2 + "   ");
							} else {
								System.out.print("########");
							}
						} else {
							System.out.print("_____   ");
							
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
		
		if(solutionsFound.size() == 3) {
			System.out.println("DEBUG");
			//System.exit(1);
		}
	}
	
	public static final BigInteger EXTENSIBLE_START = new BigInteger("3");
	public static final BigInteger BYTE_LENGTH = new BigInteger("256");
	
	public static final String orders[] = new String[] {"123", "132", "213", "231", "312", "321"};
	
	private BigInteger getScoreForPerm(Coord3D paperToDevelop[], int borders[][], int ordering_3_perm, int dirOrder) {
		
		BigInteger ret = BigInteger.ZERO;

		
		String orderToUse = orders[ordering_3_perm];
		
		//0, 1, 2, 3, 4, 5, 6, 7
		boolean iDirForward = (dirOrder /4) % 2 == 1;
		boolean jDirForward = (dirOrder /2) % 2 == 1;
		boolean kDirForward = dirOrder % 2 == 1;
		
		int lengthI = borders[0][1] - borders[0][0] + 1;
		int lengthJ = borders[1][1] - borders[1][0] + 1;
		int lengthK = borders[2][1] - borders[2][0] + 1;
		

		BigInteger metaPart1 = getPowTwo(lengthI * lengthJ * lengthK);
		
		
		for(int i=0; i<3; i++) {
			BigInteger cur = null;
			if(orderToUse.charAt(i) == '1') {
				cur = metaPart1.multiply(new BigInteger(lengthI  + ""));
				
			} else if(orderToUse.charAt(i) == '2') {
				cur = metaPart1.multiply(new BigInteger(lengthJ  + ""));
				
			} else if(orderToUse.charAt(i) == '3') {
				cur = metaPart1.multiply(new BigInteger(lengthK  + ""));
			} else {
				System.out.println("DOH! 2");
				System.exit(1);
			}
			
			ret = ret.add(cur);
			metaPart1 = metaPart1.multiply(BYTE_LENGTH);
		}
		
		ret = ret.add(metaPart1.multiply(EXTENSIBLE_START));
		
		
		
		for(int i=0; i<paperToDevelop.length; i++) {
			
			Coord3D cur = paperToDevelop[i];
			
			int numI = -1;
			
			if(iDirForward) {
				numI = (cur.i - borders[0][0]);
			} else {
				numI = lengthI - 1 - (cur.i - borders[0][0]);
			}

			int numJ = -1;
			if(jDirForward) {
				numJ = (cur.j - borders[1][0]);
			} else {
				numJ = lengthJ - 1 - (cur.j - borders[1][0]);
			}
			
			int numK = -1;
			
			if(kDirForward) {
				numK = (cur.k - borders[2][0]);
			} else {
				numK = lengthK - 1 - (cur.k - borders[2][0]);
			}
			
			//System.out.println(numI + ", " + numJ + ", " + numK);
			
			int curMult = 1;
			int numPow2 = 0;
			
			for(int j=2; j>=0; j--) {
				if(orderToUse.charAt(j) == '1') {
					numPow2 += (numI * curMult);
					curMult *= lengthI;
					
				} else if(orderToUse.charAt(j) == '2') {
					numPow2 += (numJ * curMult);
					curMult *= lengthJ;
					
				} else if(orderToUse.charAt(j) == '3') {
					numPow2 += (numK * curMult);
					curMult *= lengthK;
				} else {
					System.out.println("DOH!");
					System.exit(1);
				}
			}
			
			//System.out.println("numPow2: " + numPow2);
			BigInteger numToAdd = getPowTwo(numPow2);
			
			ret = ret.add(numToAdd);
			
		}
		
		
		
		
		return ret;
	}
	
	
	private static int[][] getBorders(Coord3D paperToDevelop[]) {
		int borders[][] = new int[3][2];
		
		borders[0][0] = paperToDevelop[0].i;
		borders[1][0] = paperToDevelop[0].j;
		borders[2][0] = paperToDevelop[0].k;

		borders[0][1] = paperToDevelop[0].i;
		borders[1][1] = paperToDevelop[0].j;
		borders[2][1] = paperToDevelop[0].k;
		
		for(int i=0; i<paperToDevelop.length; i++) {
			borders[0][0] = Math.min(borders[0][0], paperToDevelop[i].i);
			borders[1][0] = Math.min(borders[1][0], paperToDevelop[i].j);
			borders[2][0] = Math.min(borders[2][0], paperToDevelop[i].k);

			borders[0][1] = Math.max(borders[0][1], paperToDevelop[i].i);
			borders[1][1] = Math.max(borders[1][1], paperToDevelop[i].j);
			borders[2][1] = Math.max(borders[2][1], paperToDevelop[i].k);
			
			
		}
		
		return borders;
	}
}
