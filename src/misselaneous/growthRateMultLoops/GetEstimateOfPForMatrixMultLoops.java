package misselaneous.growthRateMultLoops;

public class GetEstimateOfPForMatrixMultLoops {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		boolean init[][] = new boolean[4][4];
		for(int i=0; i<init.length; i++) {
			for(int j=0; j<init.length; j++) {
				init[i][j] = true;
			}
		}
		
		init[2][1] = false;
		init[1][2] = false;
		
		System.out.println(getOdds1(init));
		
		boolean cur[][] = init;
		boolean prev[][];
		
		
		
		while(true) {
			prev = cur;
			cur = doubleSize(cur);
			compareToDrawIt(cur); 
			
			System.out.println("cur size: " + cur.length);
			System.out.println("Odds of 1: " + getOdds1(cur));
			
			double formulaForOne = (Math.sqrt(17) -3.0) * ( getOdds1(cur) /  getOdds1(prev));
			System.out.println("Hopefully close to 1: " +  formulaForOne);
			
			//TODO: print to confirm that the matrix looks right.
			
			//Pen and paper: ratio looks like continued fraction: 0; 1, 8, 8, 8, 8, 8, 8, 8, 8, 8, ...
			// which solves to 1/(sqrt(17)-3)
		}
		
		
	}
	
	public static boolean[][] doubleSize(boolean init[][]) {
		
		boolean ret[][] = new boolean[2 * init.length][2 * init.length];
		
		for(int i=0; i<ret.length; i++) {
			for(int j=0; j<ret[0].length; j++) {
				
				int iQuarter = (4*i) / ret.length;
				int jQuarter = (4*j) / ret.length;
				
				if((iQuarter == 2 && jQuarter== 1) || (iQuarter == 1 && jQuarter== 2) ) {
					ret[i][j] = false;
				} else {
					ret[i][j] = init[i%init.length][j%init.length];
				}
				
			}
		}
		
		return ret;
	}
	
	public static double getOdds1(boolean curArray[][]) {
		double area = curArray.length * curArray[0].length;
		
		int numOnes = 0;
		for(int i=0; i<curArray.length; i++) {
			for(int j=0; j<curArray[0].length; j++) {
				if(curArray[i][j]) {
					numOnes++;
				}
			}
		}
		
		return (1.0 * numOnes) / area;
		
		
	}
	
	

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
	
	public static void compareToDrawIt(boolean curArray[][]) {
		
		int numBits = -1;
		for(int i=0; true; i++) {
			if(Math.pow(2, i) == curArray.length) {
				numBits = i;
				break;
			}
		}
		
		for(int i=0; i<curArray.length; i++) {
			
			boolean bitsI[] = convertToBool(i, numBits);
			
			for(int j=0; j<curArray[0].length; j++) {
				boolean bitsJ[] = convertToBool(j, numBits);
				
				boolean foundProb = false;
				for(int k=0; k<bitsI.length - 1; k++) {
					if(bitsI[k] != bitsI[k+1] && bitsI[k] == bitsJ[k+1] && bitsI[k+1] == bitsJ[k]) {
						foundProb = true;
						break;
					}
				}
				/*if(foundProb) {
					System.out.print("_");
				} else {
					System.out.print("#");
				}*/
				
				if( curArray[i][j] != (!foundProb) ) {
					System.out.println("doh!");
					System.exit(1);
				} else {
					//System.out.println("good");
				}
			}
			//System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println("Done comparing it to the draw it function!");
		
	}

}
