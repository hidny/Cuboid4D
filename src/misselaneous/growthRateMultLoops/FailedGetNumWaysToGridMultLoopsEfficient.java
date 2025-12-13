package misselaneous.growthRateMultLoops;

import java.math.BigInteger;

//Goal:
//A133130

//TODO: rethink this! It doesn't work because
// miamatch -> mismatch only has a 50% chance of working...
public class FailedGetNumWaysToGridMultLoopsEfficient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*for(int j=1; j<4; j++) {
			System.out.println(solve(j));
			System.out.println("----");
		}*/
		System.out.println(solve(2));
	}

	public static BigInteger pow2[] = null;
	public static BigInteger pascalsTriangle[][] = null;
	public static final BigInteger TWO = new BigInteger("2");
	
	public static void createPascalsTriangle(int n) {
		
		pascalsTriangle = new BigInteger[n+1][n+1];
		
		for(int i=0; i<pascalsTriangle.length; i++) {
			for(int j=0; j<pascalsTriangle[0].length; j++) {
				pascalsTriangle[i][j] = BigInteger.ZERO;
			}
		}
		
		pascalsTriangle[0][0] = BigInteger.ONE;
		
		for(int i=1; i<pascalsTriangle.length; i++) {
			for(int j=0; j<pascalsTriangle[0].length; j++) {
				
				if(j == 0) {
					pascalsTriangle[i][j] = BigInteger.ONE;
				} else {
					pascalsTriangle[i][j] = pascalsTriangle[i - 1][j].add(pascalsTriangle[i - 1][j - 1]);
				}
				
				//System.out.print(pascalsTriangle[i][j] + "    ");
			}
			//System.out.println();
		}
	}
	
	public static void createPow2(int n) {
		pow2 = new BigInteger[n+5];
		
		pow2[0] = BigInteger.ONE;
		for(int i=1; i<pow2.length; i++) {
			pow2[i] = pow2[i-1].multiply(TWO);
			//System.out.println(pow2[i]);
		}
	}
	
	public static BigInteger solve(int n) {
		
		if(n == 1) {
			//Edge case:
			System.out.println("Solution to n = " + n + ":");
			System.out.println(TWO);
			return TWO;
		}
		
		BigInteger stateByNumMatches[] = new BigInteger[n];
		for(int i=0; i<stateByNumMatches.length; i++) {
			stateByNumMatches[i] = BigInteger.ZERO;
		}
		stateByNumMatches[stateByNumMatches.length - 1] = TWO;
		
		createPascalsTriangle(n);
		createPow2(n);
		
		BigInteger stateByNumMatchesPrev[] = stateByNumMatches;
		
		//TODO: <= n???
		for(int vert=0; vert < n; vert++) {
			
			stateByNumMatches = new BigInteger[n];
			for(int i=0; i<stateByNumMatches.length; i++) {
				stateByNumMatches[i] = BigInteger.ZERO;
			}
			
			for(int numMatchesPrev=0; numMatchesPrev<stateByNumMatches.length; numMatchesPrev++) {
				int numMismatchesPrev = stateByNumMatches.length - 1 - numMatchesPrev;
				
				
				for(int newMatchesFromMatches=0; newMatchesFromMatches<=numMatchesPrev; newMatchesFromMatches++) {
					
					int numMismatchFromMatch = numMatchesPrev - newMatchesFromMatches;
					
					BigInteger numWays1 = pascalsTriangle[numMatchesPrev][newMatchesFromMatches];//.multiply(pow2[newMatchesFromMatches]);
					
					BigInteger numWays2 = pascalsTriangle[numMatchesPrev][numMismatchFromMatch];//.multiply(pow2[numMismatchFromMatch]);
					
					BigInteger tempNumWaysFromMatches = numWays1.multiply(numWays2);
					
					for(int newMatchesFromMismatches=0; newMatchesFromMismatches<=numMismatchesPrev; newMatchesFromMismatches++) {
						
						int numMismatchFromMismatch = numMismatchesPrev - newMatchesFromMismatches;
						
						BigInteger numWays3 = pascalsTriangle[numMismatchesPrev][newMatchesFromMismatches];//.multiply(pow2[newMatchesFromMismatches]);
						
						//Note that there's only one way to have a mismatch from a mismatch instead of the normal 2:
						BigInteger numWays4 = pascalsTriangle[numMismatchesPrev][numMismatchFromMismatch];
						
						BigInteger tempNumWaysFromMismatches = numWays3.multiply(numWays4);
						
						BigInteger numToAdd = stateByNumMatchesPrev[numMatchesPrev].multiply(tempNumWaysFromMatches.multiply(tempNumWaysFromMismatches));
						
						if(numToAdd.compareTo(BigInteger.ZERO) != 0) {
							System.out.println("DEBUG");
						}
						System.out.println("N = " + n + ", vert = " + vert + ", numMatchesPrev = " + numMatchesPrev + ", newMatchesFromMatches =" + newMatchesFromMatches + ", newMatchesFromMismatches = " + newMatchesFromMismatches);
						System.out.println("numToAdd: " + numToAdd);
						
						stateByNumMatches[newMatchesFromMatches + newMatchesFromMismatches] = stateByNumMatches[newMatchesFromMatches + newMatchesFromMismatches]
								.add(numToAdd);
					}
					
				}
				
			}
			
			System.out.println();
			stateByNumMatchesPrev = stateByNumMatches;
		}
		
		BigInteger ret = BigInteger.ZERO;
		for(int i=0; i<stateByNumMatchesPrev.length; i++) {
			ret = ret.add(stateByNumMatchesPrev[i]);
		}
		
		//System.out.println("Solution to n = " + n + ":");
		//System.out.println(ret);
		return ret;
	}

}
