package misselaneous.growthRateMultLoops;

public class anti2by2gridMatrixDraw {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		drawIt(10);
	}
	
	
	//TODO: what if we order it by the # of times the numbers switch from 0 to 1? Will the picture look clearer?
	//Right now, it has a fractal shape.
	public static void drawIt(int numBits) {
		
		for(int i=0; i<Math.pow(2, numBits); i++) {
			
			boolean bitsI[] = convertToBool(i, numBits);
			
			for(int j=0; j<Math.pow(2, numBits); j++) {
				boolean bitsJ[] = convertToBool(j, numBits);
				
				boolean foundProb = false;
				for(int k=0; k<bitsI.length - 1; k++) {
					if(bitsI[k] != bitsI[k+1] && bitsI[k] == bitsJ[k+1] && bitsI[k+1] == bitsJ[k]) {
						foundProb = true;
						break;
					}
				}
				if(foundProb) {
					System.out.print("_");
				} else {
					System.out.print("#");
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		
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

}
