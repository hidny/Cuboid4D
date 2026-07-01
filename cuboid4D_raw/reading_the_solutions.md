
How to read the solutions:

The first thing to know is that the soluion comes in multiple tables. The first table is for k=0, 2nd is k=1, and so on.

For each table, the 1st row is i=0, 2nd row is i=1, and so on.
For each table, the 1st column is j=0, 2nd row is j=1, and so on.

When the field is not empty, it is filled with a cell.
The filled field has the form "a,b,c" where:
* a is the index # of the cell according to the 'canonical map' of the hypercuboid (goes from 0 to (surfacevolume -1). It is also the 1st entry in the cells filled in the 'canonical map'
* b is the direction of the face of the cell that points in the i+ direction in the canonical map. (Values from 0 to 5) (see 4D_directions.png for how the 6 orthogonal directions are numbered)
* c is the direction of the face of the cell that points in the j+ direction in the canonical map. (Values from 0 to 5) (see 4D_directions.png for how the 6 orthogonal directions are numbered)

Example solution for 1x1x1x1:

1st the 'canonical view':

Printing canonical map of the 1 x 1 x 1 x 1 cuboid:
______   ______   ______   ______   
______   0,0,1    ______   ______   
______   ______   ______   ______   



______   1,0,1    ______   ______   
2,0,1    3,0,1    4,0,1    5,0,1    
______   6,0,1    ______   ______   



______   ______   ______   ______   
______   7,0,1    ______   ______   
______   ______   ______   ______   

Notice that the 2nd and 3rd values are always 0 and 1, and we also chose the most common 3D net to cover the 4D hypercube.

Here is a solution:

Solution 2:
______   ______   ______   
______   ______   ______   
______   5,2,4    ______   
______   ______   ______   



______   1,2,1    ______   
2,0,2    0,0,1    4,0,5    
______   6,5,1    ______   
______   7,3,1    ______   



______   ______   ______   
______   3,0,1    ______   
______   ______   ______   
______   ______   ______   

Notice that the root cell is index 0, and it is "0,0,1", so it has not been rotated.
Also note that index 3 also has not been rotated.
The other cells have all been rotated. Cells 2 and 4 haven't changed where the canonical face #0 is pointing while cells 1, 6, and 7 haven't changed where the canonical face #1 is pointing. Cell index 5 is now attached to index 6 and has been rotated in a more complicated way. The 2nd and 3rd coordinates of index 5 (i.e: 2,4) describe this rotation.