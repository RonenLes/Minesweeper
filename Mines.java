package mines;

import java.util.ArrayList;
import java.util.List;

public class Mines {
	
	private Place[][] mat;//board
	private int numMines;
	private boolean showAll = false;
	
	public Mines(int height, int width, int numMines) {
		this.mat = new Place[height][width];
		
		this.numMines = numMines;
		
		//place on the board
		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				mat[i][j] = new Place(i,j,".");
			}
		}
		
	}
	
	public int getHeight() {
	    return mat.length;
	}

	public int getWidth() {
	    return mat[0].length;
	}
	
	//nested class to handle a single place 
	//-------------------------------------------------------------------------------
	private class Place{
		
		private int x;
		private int y;
		private String status;//status of the point "X" ,"F" ,"","."
		private boolean isFlag = false; //is it a flag point
		private int nearbyMines=0;
		private boolean isOpen = false; //is it an open cell
		
		
		public Place(int i,int j,String status) {
			this.x=i;
			this.y=j;
			this.status=status;
			
		}
		
		private int getX() {
			return this.x;
		}
		
		private int getY() {
			return this.y;
		}
		
		private String getStatus() {
			return this.status;
		}
		
		private void setStatus(String status) {
			this.status = status;
		}
		
		private int getNearbyMines() {
			return this.nearbyMines;
		}
		
		private boolean hasOpened() {
			return this.isOpen;
		}
		
		private void setOpen(boolean b) {
			this.isOpen =b;
		}
		
		private void setFlag() {
			this.isFlag= !isFlag;
		}
		
		private boolean getFlag() {
			return this.isFlag;
		}
		
		//list the neighbors of the point 
		private List<Place> neighbours(){
			List<Place> neighbourList = new ArrayList<>();
		    int[][] directions = {
		        {-1, -1}, {-1, 0}, {-1, 1},
		        { 0, -1},          { 0, 1},
		        { 1, -1}, { 1, 0}, { 1, 1}
		    };

		    for (int[] d : directions) {
		        int newX = x + d[0];
		        int newY = y + d[1];

		        // Check bounds
		        if (inBound(newX,newY)) {
		            neighbourList.add(Mines.this.mat[newX][newY]);
		        }
		    }

		    return neighbourList;
		
		}
	}
	//-----------------------------------------------------------------------------------
	
	//help function to check if Place is inside the bounds
	private boolean inBound(int x,int y) {
		return x >= 0 && x < this.mat.length && y >= 0 && y < this.mat[0].length;
	}
	
	//add mine
	public boolean addMine(int row, int col) {
		
		//check bounds and if is already a bomb
		if(!inBound(row,col) || this.mat[row][col].getStatus().equals("X")) return false;
		
		this.mat[row][col].setStatus("X");//change status of the place
		
		//update the nearby neighbors nearbyMines
		List<Place> n = this.mat[row][col].neighbours();
		for(Place p : n) {
			p.nearbyMines++;
		}
		return true;
	}
	
	//open sort of dfs
	public boolean open(int row, int col) {
		
		if (!inBound(row, col) || mat[row][col].hasOpened()) return true;//check if its already opened
		if(mat[row][col].getStatus().equals("X")) return false;
		
		mat[row][col].setOpen(true);//open cell because it not a bomb
		
		//check cells that has 0 bomb nearby
		if(mat[row][col].nearbyMines == 0) {
			mat[row][col].setStatus("");
			
			//and check their neighbor
			for (Place neighbor : mat[row][col].neighbours()) {
				  if(!neighbor.hasOpened()) {  
				      open(neighbor.getX(),neighbor.getY());    
				   }
			}
		}
		
		
		 return true;
	}
	
	//place a flag
	public void toggleFlag(int row, int col) {
		mat[row][col].setFlag();
		
	}
	
	
	//check if the games is over and no more cells to open
	public boolean isDone() {
		
		for(int i=0;i<mat.length;i++) {
			for(int j=0;j<mat[0].length;j++) {
				Place p = mat[i][j];
				if(p.getStatus().equals("X")) {
					if(p.hasOpened()) return false;
				}
				else {
					if(!p.hasOpened()) return false;	
				}
			}
		}
		return true;
	}
	
	//get status of each place
	public String get(int row, int col) {
		
		if(!mat[row][col].hasOpened() && !showAll) {
			if(mat[row][col].getFlag()) return "F";
			return ".";
		}
		
		
		if(mat[row][col].getStatus().equals("X")) return "X";
		else if(mat[row][col].getNearbyMines()>0 ) return ""+mat[row][col].getNearbyMines();
		
		
		return " ";
	}
	
	//reveal all cells status
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}
	
	
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<mat.length;i++) {
			for(int j=0;j<mat[0].length;j++) {
				sb.append(get(i,j));
				if(i == 3&&j==3) {
					
				}
			}
			
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
