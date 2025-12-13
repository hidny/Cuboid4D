package misselaneous.growthRateMultLoops;

public class GetEstimateOfPForMatrixMultLoops2 {

	public static void main(String[] args) {
		
		double prevProb = 1.0;
		double curProb = 1.0;
		
		for(int numBits=2; numBits<20; numBits++) {
			prevProb = curProb;
			curProb = getOdds1(numBits);
			
			System.out.println();
			System.out.println();
			System.out.println("cur size: " + Math.pow(2, numBits));
			System.out.println("Odds of 1: " + curProb);
			
			double formulaForOne = (Math.sqrt(17) -3.0) * ( curProb /  prevProb);
			System.out.println("Hopefully close to 1: " +  formulaForOne);
			
			//Pen and paper: ratio looks like the continued fraction: 0; 1, 8, 8, 8, 8, 8, 8, 8, 8, 8, ...
			// which solves to 1/(sqrt(17)-3)
		}
		
		
	}
	/*
	 * 
cur size: 131072.0
Odds of 1: 0.1537226993823424
Hopefully close to 1: 1.0000000000000162
Size: 262144

cur size: 262144.0
Odds of 1: 0.1368728780944366
Hopefully close to 1: 0.9999999999999974
Size: 524288

cur size: 524288.0
Odds of 1: 0.12186999599362025
Hopefully close to 1: 1.0000000000000004

Close enough!
	 */
	
	//Prob_1(N) = 0.9850712500726656 * (sqrt(17)-3)^(-N+1)
	// = (0.5+2/sqrt(17))*(sqrt(17)-3)^(-N+1)
	
	//https://www.wolframalpha.com/input?i=0.9850712500726656
	
	//wolfram:
	// k = 0.9850712500726656
	//k continued fraction: [0; 1, 65, 1, 64, 1, 64, 1, 64, 1]
	// closed form (1/2 + 2/sqrt(17))
	// I believe wolftram... mostly because I'm too lazy to prove it.
	//Wolfram says it's probably:
	// k = 0.985071250072665 94703
	// Why not?

	public static boolean[] convertToBool(int num, int numBinaryDigits) {
		
		boolean ret[] = new boolean[numBinaryDigits];
		
		int curIndex = numBinaryDigits -1;
		while(curIndex >= 0) {
			
			if(num % 2 == 0) {
				ret[curIndex] = false;
			} else {
				ret[curIndex] = true;
			}
			num /= 2;
			curIndex--;
			
		}
		
		return ret;
		
	}

	public static double getOdds1(int numBits) {
		
		long size = (long)Math.round(Math.pow(2, numBits));
		System.out.println("Size: " + size);
		
		long numOnes = 0L;
		
		for(int i=0; i<size; i++) {
			
			boolean bitsI[] = convertToBool(i, numBits);
			
			for(int j=0; j<size; j++) {
				boolean bitsJ[] = convertToBool(j, numBits);
				
				boolean foundZero = false;
				for(int k=0; k<bitsI.length - 1; k++) {
					if(bitsI[k] != bitsI[k+1] && bitsI[k] == bitsJ[k+1] && bitsI[k+1] == bitsJ[k]) {
						foundZero = true;
						break;
					}
				}
				
				if( ! foundZero) {
					numOnes++;
				}
				
			}
			//System.out.println();
		}
		
		return (1.0 * numOnes) / (1.0 * size * size);
		
	}

}
