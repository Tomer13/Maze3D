
package algorithms.mazeGenerators;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Maze3d {
private int rows;
private int cols;
private int levels;
private byte[] mazeAsBytes=null;
private int[][][] maze;
private Position startPosition;
private Position goalPosition;
public final int PATH=0; 
public final int WALL=1; 


public Maze3d(byte[] mazeAsBytes) {
	this.setMazeAsBytes(mazeAsBytes);
	
	ByteArrayInputStream in=new ByteArrayInputStream(mazeAsBytes);
	DataInputStream data=new DataInputStream(in);

	try {
		//get the sizes of the maze
		levels=data.readInt();
		rows=data.readInt();
		cols=data.readInt();
		
		// get and set the start position

		setStartPosition(new Position(data.readInt(),data.readInt(),data.readInt()));
		
		// get and set the goal position
		setGoalPosition(new Position(data.readInt(),data.readInt(),data.readInt()));
		maze = new int[this.levels][this.rows][this.cols];
		//save the maze
		for(int i=0;i<levels;i++){
			for(int j=0;j<rows;j++){
				for(int k=0;k<cols;k++){
					maze[i][j][k]=data.readInt();
				}
			}
		}
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public Maze3d(int level, int row, int col) {
	this.rows=row*2+1;
	this.cols=col*2+1;
	this.levels=level*2+1;
	maze = new int[this.levels][this.rows][this.cols];
}

public int getCols() {
	return cols;
}

public int getRows() {
	return rows;
}

public int getLevels() {
	return levels;
}

public int[][][] getMaze() {
	return maze;
}
public Position setRandomGoal(){
	Position p= choosRandomPosition();
	while(getCellVal(p.x, p.y, p.z)==WALL || p==startPosition){
		p=choosRandomPosition();
		
	}
	
	setFree(p.x, p.y, p.z);
	setGoalPosition(p);
	return p;
}
public Position getStartPosition() {

	return startPosition;
}
public void setStartPosition(Position startPos) {
	this.startPosition = startPos;
}
public Position getGoalPosition() {
	return goalPosition;
}
public void setGoalPosition(Position goalPos) {
	this.goalPosition = goalPos;
}
public int getCellVal(int level,int row,int col){
	return maze[level][row][col];
	
	
}
public void setCellVal(int level,int row,int col,int val){
	maze[level][row][col]=val;
}

public void setMazeAsWalls(){
	
	for(int i=0;i<levels;i++)
		for(int j=0;j<rows;j++)
			for(int k=0;k<cols;k++)
				maze[i][j][k]=WALL;
		}

public void setMazeAsPath(){
	
	for(int i=1;i<levels-1;i++)
		for(int j=1;j<rows-1;j++)
			for(int k=1;k<cols-1;k++)
				maze[i][j][k]=PATH;
		}

public Position choosRandomPosition(){
	Random rand=new Random();
	int level=0;
	int row=0;
	int col=0;
	  //only odd position are legal
		  level=rand.nextInt(levels);
		  while(level%2==0)
		  level=rand.nextInt(levels);
		  
		  row=rand.nextInt(rows);
		  while(row%2==0)
			  row=rand.nextInt(rows);
		  
		  col=rand.nextInt(cols);
		  while(col%2==0)
			  col=rand.nextInt(cols);
		  
	  Position randPosition= new Position(level,row,col);
	return randPosition;
}

public void setFree(int level,int row,int col){
	maze[level][row][col]=PATH;
	
}


public ArrayList<Directions> getUnvisitedNeighbors(Position p){
	
	ArrayList<Directions> dir=new ArrayList<Directions>();
	
	if(checkRight(p)==Directions.RIGHT)
		dir.add(Directions.RIGHT);
	
	if(checkLeft(p)==Directions.LEFT)
		dir.add(Directions.LEFT);
	
	if(checkForward(p)==Directions.FORWARD)
		dir.add(Directions.FORWARD);
	
	if(checkBackward(p)==Directions.BACKWARD)
		dir.add(Directions.BACKWARD);
	
	if(checkUp(p)==Directions.UP)
		dir.add(Directions.UP);
	
	if(checkDown(p)==Directions.DOWN)
		dir.add(Directions.DOWN);

	return dir;
	
}
	
public Directions checkRight(Position p){
	
	if((p.z+2)<cols &&  getCellVal(p.x, p.y, p.z+1)==WALL && getCellVal(p.x+1, p.y, p.z+2)==WALL){
	return Directions.RIGHT;}
	else return null;
	
}
public Directions checkLeft(Position p){
	
	if( (p.z-2)>0 && getCellVal(p.x, p.y, p.z-1)==WALL && getCellVal(p.x, p.y, p.z-2)==WALL)
		return Directions.LEFT;
	else
	return null;
	
}
public Directions checkForward(Position p){
	
	if((p.y+2)<rows && getCellVal(p.x, p.y+1, p.z)==WALL && getCellVal(p.x, p.y+2, p.z)==WALL )
		return Directions.FORWARD;
	else
	return null;
	
}	
public Directions checkBackward(Position p){
	
	if((p.y-2)>0 && getCellVal(p.x, p.y-1, p.z)==WALL && getCellVal(p.x, p.y-2, p.z)==WALL)
		return Directions.BACKWARD;
	else
	return null;
	
}
public Directions checkUp(Position p){
	
	if((p.x+2)<levels && getCellVal(p.x+1, p.y, p.z)==WALL && getCellVal(p.x+2, p.y, p.z)==WALL)
		return Directions.UP;
	else
	return null;
	
}	
public Directions checkDown(Position p){
	
	if((p.x-2)>0 && getCellVal(p.x-1, p.y, p.z)==WALL && getCellVal(p.x-2, p.y, p.z)==WALL)
		return Directions.DOWN;
	else
	return null;
	
}	
public int[][] getCrossSectionByX(int x) throws IndexOutOfBoundsException{
	
	int[][] getCrossByX;
	if(x<0 || x> levels-1)
		throw new IndexOutOfBoundsException("The input of x is not valid!");
		getCrossByX = new int[rows][levels];
		for(int i=0;i<rows;i++)
			for(int j=0;j<cols;j++){
				getCrossByX[i][j]=maze[x][i][j];
			}
	
	return getCrossByX ;
}
public int[][] getCrossSectionByY(int y) throws IndexOutOfBoundsException{
	
	int[][] getCrossByY=new int[levels][cols];
	if(y<0 || y> rows-1)
		throw new IndexOutOfBoundsException("The input of y is not valid!");	
	for(int i=0;i<levels;i++)
		for(int j=0;j<cols;j++){
			getCrossByY[i][j]=maze[i][y][j];
		}
		
	
	
	return getCrossByY ;
	

}
public int[][] getCrossSectionByZ(int z) throws IndexOutOfBoundsException{	
	int[][] getCrossByZ=new int[levels][rows];
	if(z<0 || z> cols-1)
		throw new IndexOutOfBoundsException("The input of z is not valid!");
	for(int i=0;i<levels;i++)
	for(int j=0;j<rows;j++){
	getCrossByZ[i][j]=maze[i][j][z];
		}
	
	return getCrossByZ ;
}
public ArrayList<Position> getDirectionsReturnPositions(ArrayList<Directions> dir, Position pos){
	ArrayList<Position> positions=new ArrayList<Position>();
	for(Directions direction: dir){
		if (direction==Directions.RIGHT) 
			positions.add(new Position(pos.x,pos.y,pos.z+2));
		if (direction==Directions.LEFT) 
			positions.add(new Position(pos.x,pos.y,pos.z-2));
		if (direction==Directions.FORWARD) 
			positions.add(new Position(pos.x,pos.y+2,pos.z));
		if (direction==Directions.BACKWARD) 
			positions.add(new Position(pos.x,pos.y-2,pos.z));
		if (direction==Directions.UP) 
			positions.add(new Position(pos.x+2,pos.y,pos.z));
		if (direction==Directions.DOWN) 
			positions.add(new Position(pos.x-2,pos.y,pos.z));
	}
	
	return positions;
}

public ArrayList<Directions> getPossibleMoves(Position p){
	
	ArrayList<Directions> dir=new ArrayList<Directions>();
	
	if((p.z+2)<=cols) 
		if(getCellVal(p.x, p.y, p.z+1)==PATH && getCellVal(p.x, p.y, p.z+2)==PATH)
		dir.add(Directions.RIGHT);
	if((p.z-2)>=0) 
		if(getCellVal(p.x, p.y, p.z-1)==PATH && getCellVal(p.x, p.y, p.z-2)==PATH)
		dir.add(Directions.LEFT);
	if((p.y+2)<=rows)
		if(getCellVal(p.x, p.y+1, p.z)==PATH && getCellVal(p.x, p.y+2, p.z)==PATH)
		dir.add(Directions.FORWARD);
	if((p.y-2)>=0)
		if(getCellVal(p.x, p.y-1, p.z)==PATH && getCellVal(p.x, p.y-2, p.z)==PATH)
		dir.add(Directions.BACKWARD);
	if((p.x+2)<=levels)
		if(getCellVal(p.x+1, p.y, p.z)==PATH && getCellVal(p.x+2, p.y, p.z)==PATH)
		dir.add(Directions.UP);
	if((p.x-2)>=0) 
		if(getCellVal(p.x-1, p.y, p.z)==PATH && getCellVal(p.x-2, p.y, p.z)==PATH)
		dir.add(Directions.DOWN);
	

	return dir;
	
}

@Override
public String toString() {
	StringBuilder sb = new StringBuilder();
	for (int x = 0 ; x < levels; x++)
	{
		sb.append(" \n");
		for (int y = 0; y < rows; y++) {
			for (int z = 0; z < cols; z++) {
				
				if (y == startPosition.y && x == startPosition.x && z == startPosition.z) {
					sb.append("S"+" ");
				}
				else if (y == goalPosition.y && x == goalPosition.x && z == goalPosition.z) {
					sb.append("G"+" ");
				}
				else {
					sb.append(maze[x][y][z]+" ");
				}
			}
			sb.append("\n");
		}
		sb.append("\n");
	}
	return sb.toString();
}
	
	
	

public byte[] toByteArray(){
	ByteArrayOutputStream out=new ByteArrayOutputStream();
	DataOutputStream data=new DataOutputStream(out);
	
	try { //Surround the data writing with try catch block
		
		//save maze size data
		data.writeInt(levels);
		data.writeInt(rows);
		data.writeInt(cols);
		//save start position
		data.writeInt(startPosition.x);
		data.writeInt(startPosition.y);
		data.writeInt(startPosition.z);
		//save goal position
		data.writeInt(goalPosition.x);
		data.writeInt(goalPosition.y);
		data.writeInt(goalPosition.z);
		
		//save the maze
		
		for(int i=0;i<levels;i++){
			for(int j=0;j<rows;j++){
				for(int k=0;k<cols;k++){
					data.writeInt(maze[i][j][k]);
				}
			}
		}
		
	} catch (IOException e) {// throw exception
		System.out.println("IO exception!");
		e.printStackTrace();
	}
	return out.toByteArray();
	
}

public byte[] getMazeAsBytes() {
	return mazeAsBytes;
}

public void setMazeAsBytes(byte[] mazeAsBytes) {
	this.mazeAsBytes = mazeAsBytes;
}


}