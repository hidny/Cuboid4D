package tests;

import java.math.BigInteger;

// Found A133130 !
// Number of 0/1 colorings of an n X n square for which no 2 by 2 subsquare is monochromatic.

//Main diagonal of:
//A181245
//T(n,k) = Number of n X k binary matrices with no 2 X 2 circuit having pattern 0101 in any orientation.

// I was thinking of the number of ways to have a grid where all vertices have 2 edges set to "on",
// leading to grids with a bunch of polygons in it that don't touch each other, but that maps to A133130 !


//LOL! I went way beyond what's on the OEIS!
//I still don't know what motivated people to put this on the OEIS...

//LOL! The 1st sequence in context of A133130 is https://oeis.org/A356610
//"Number of SAWs crossing a rhomboidal domain of the hexagonal lattice."
// Anthony J. Guttmann and Iwan Jensen, Self-avoiding walks and polygons crossing a domain on the square and hexagonal lattices, arXiv:2208.06744 [math-ph], Aug 13 2022, Table D3.


//Taking inspiration from iwen Jenson's Transfer matrix method.

//I have to write about this in the paper...

public class GetNumWaysToGridWithMultLoops {

	public static int N = 23;

	//N=23:
	//235093808618308001100994611101527413576445205415492247696869234849229717448591264986087113188145626281800778840545892121822758
	
	
	public static void main(String[] args) {

		for(int n=1; n<30; n++) {
			N = n;
			twoToN = (int)Math.pow(2, N);
			
			BigInteger answer = solve();
			
			System.out.println("N = " + n);
			System.out.println("Answer: " + answer);
			System.out.println("Number of binary digits: " + getNumDigits(answer));
			double numDigits = getNumDigits(answer) * 1.0;
			
			if(n > 1) {

				double zerothOne = numDigits/((n-1)*(n-1));
				System.out.println("Digits div (n-1)^2: " + zerothOne);
				System.out.println("Estimate lambda: " + Math.pow(2, zerothOne));
			}
			
			double firstOne = numDigits/(n*n);
			System.out.println("Digits div n^2: " + firstOne);
			System.out.println("Estimate lambda: " + Math.pow(2, firstOne));
			

			double secondOne = numDigits/((n+1)*(n+1));
			System.out.println("Digits div (n+1)^2: " + secondOne);
			System.out.println("Estimate lambda: " + Math.pow(2, secondOne));
			
			System.out.println("-----------------");
			System.out.println();
			
		}
		
	}

	//Let N be the number of points at the top.
	
	public static BigInteger solve() {
		
		BigInteger answersBySettingBefore[] = new BigInteger[(int)Math.pow(2, N + 1)];
		
		for(int i=0; i<answersBySettingBefore.length; i++) {
			answersBySettingBefore[i] = BigInteger.ZERO;
		}
		//Starting config is all empty.
		answersBySettingBefore[0] = BigInteger.ONE;
		
		for(int layer=0; layer < N; layer++) {

			for(int j=0; j<N; j++) {

				BigInteger answersBySettingAfter[] = new BigInteger[(int)Math.pow(2, N+1)];
				for(int i=0; i<answersBySettingAfter.length; i++) {
					answersBySettingAfter[i] = BigInteger.ZERO;
				}
				
				for(int configIndex=0; configIndex<answersBySettingBefore.length; configIndex++) {
					
					//help debug:
					if(answersBySettingBefore[configIndex].compareTo(BigInteger.ZERO) == 0) {
						continue;
					}
					
					boolean prevHori = getPrevHori(configIndex, N);
					boolean prevVert = getCombBranchAbove(configIndex, N, j);
					
					int newConfigIndexes[] = getNewIndexes(N, j, prevHori, prevVert, configIndex);
					
					for(int k=0; k<newConfigIndexes.length; k++) {
						// +=
						answersBySettingAfter[newConfigIndexes[k]] = 
								answersBySettingAfter[newConfigIndexes[k]].add(answersBySettingBefore[configIndex]);
					}
				}
				
				answersBySettingBefore = answersBySettingAfter;
				
				/*System.out.println("After:");
				System.out.println("Layer: " + layer);
				System.out.println("J: " + j);
				for(int i=0; i<twoToN; i++) {
					System.out.println("Config " + i + ": " + answersBySettingBefore[i]);
				}
				System.out.println();
				*/
			}
			
			//Remove all answers where the last horizontal is coded as zero, because that edge is not available.
			for(int i=twoToN; i<answersBySettingBefore.length; i++) {
				answersBySettingBefore[i] = BigInteger.ZERO;
			}
			/*System.out.println("After:");
			System.out.println("Layer: " + layer);
			for(int i=0; i<twoToN; i++) {
				System.out.println("Config " + i + ": " + answersBySettingBefore[i]);
			}
			System.out.println();
			*/
		}
		
		
		//Only include the config where the combs below last layer are inactive 
		//and the last horizontal is zero, because that edge is not available.
		BigInteger ret = answersBySettingBefore[0];
		
		
		return ret;
	}
	
	public static int tmp1[] = new int[1];
	public static int tmp2[] = new int[2];

	
	//public static int twoToN = (int)Math.pow(2, N);
	public static int twoToN = -1;
	
	public static int[] getNewIndexes(int N, int j, boolean prevHori, boolean prevVert, int configIndex) {
		int curIndex = configIndex;
		if(prevHori && prevVert) {
			//Both zero:
			
			curIndex = setHori(curIndex, N, false);
			curIndex = setVert(curIndex, N, j, false);
			tmp1[0] = curIndex;
			return tmp1;
		
		} else if(!prevHori && !prevVert) {

			curIndex = setHori(curIndex, N, false);
			curIndex = setVert(curIndex, N, j, false);
			tmp2[0] = curIndex;

			curIndex = setHori(curIndex, N, true);
			curIndex = setVert(curIndex, N, j, true);
			tmp2[1] = curIndex;
			
			return tmp2;
			
		} else if(prevHori ^ prevVert) {

			curIndex = setHori(curIndex, N, true);
			curIndex = setVert(curIndex, N, j, false);
			tmp2[0] = curIndex;

			curIndex = setHori(curIndex, N, false);
			curIndex = setVert(curIndex, N, j, true);
			tmp2[1] = curIndex;
			
			return tmp2;
			
		} else {
			System.out.println("This should never happen!");
			System.exit(1);
			return null;
		}
		
	}
	
	public static boolean getPrevHori(int prevIndex, int N) {
		return (prevIndex >> N) % 2 != 0;
	}
	
	public static boolean getCombBranchAbove(int prevIndex, int N, int j) {
		return (prevIndex >> (N - (1 + j))) % 2 != 0;
	}
	
	public static int setHori(int curIndex, int N, boolean setOn) {
		if(setOn) {
			return curIndex | twoToN;
		} else {
			return curIndex % twoToN;
		}
	}

	public static int setVert(int curIndex, int N, int j, boolean setOn) {
		if(setOn) {
			return curIndex | (twoToN >> (j + 1));
		} else {
			return curIndex & (~(twoToN >> (j + 1)));
		}
	}
	
	public static int getNumDigits(BigInteger num) {
		return num.bitLength();
	}
}
