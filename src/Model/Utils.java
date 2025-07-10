package Model;


import Coord.Coord3D;

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
	
}
