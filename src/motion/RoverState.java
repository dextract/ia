package motion;

import java.util.ArrayList;
import java.util.List;

import motion.Terrain.TerrainType;
import static java.lang.Math.*;
import searchproblem.*;


public class RoverState extends State {
	
	public enum RoverOperator { W, E, N, S, NW, NE, SW, SE; }
	
	BitmapTerrain terrain;
	private int xPos;
	private int yPos;

	public RoverState(int startx, int starty, BitmapTerrain t) {
		xPos = startx;
		yPos = starty;
		terrain = t;
	}

	@Override
	public List<Arc> successorFunction() {
		List<Arc> children = new ArrayList<Arc>(8);
		for(RoverOperator action : RoverOperator.values() ) {
			if( applicableOperator(action))
				children.add(successorState(action));
		}
		return children;
	}

	@Override
	public Arc successorState(Object op) {
		RoverState child = (RoverState) this.clone();
		return new Arc(this,child,op,child.applyOperator(op));	
	}
	
	public boolean applicableOperator(Object action) {
		if ( !(action instanceof RoverOperator) )
			return false;
		
		RoverOperator op = (RoverOperator) action;
		switch (op) {
			case W: 		
				return xPos > 0;
			case E:
				return xPos < terrain.getHorizontalSize()-1;
			case N:
				return yPos > 0;
			case S:
				return yPos < terrain.getVerticalSize()-1;
			case NW: 
				return xPos > 0 && yPos > 0;
			case NE:
				return xPos < terrain.getHorizontalSize()-1 && yPos > 0;
			case SW:
				return xPos > 0 && yPos < terrain.getVerticalSize()-1;
			case SE:
				return xPos < terrain.getHorizontalSize()-1 && yPos < terrain.getVerticalSize()-1;
		}
		
		return false;
		
	}

	@Override
	public double applyOperator(Object op) {
		
		RoverOperator action = (RoverOperator) op;
		
		//variaveis actuais que irao ser mudadas no final do metodo, de forma a se poder calcular o custo.
		int oldX = xPos;
		int oldY = yPos;
		
		switch (action) {
			case W: xPos--; break;
			case E: xPos++; break;
			case N: yPos--; break;
			case S: yPos++; break;
			case NW: yPos--; xPos--; break;
			case NE: yPos--; xPos++; break;
			case SW: yPos++; xPos--; break;
			case SE: yPos++; xPos++; break;
		}
		
		//retorna o custo de ir da posiçao antiga para a recente, com base no operador
		return calcCost(oldX, oldY, xPos, yPos);
		
	}
	
	private double calcCost(int oX, int oY, int nX, int nY) {
			
		double h1 = terrain.getHeight(oX, oY); //altura da posiçao antiga
		double h2 = terrain.getHeight(nX, nY); //altura da posiçao nova
		double d = sqrt(pow(oX-nX,2) + pow(oY-nY,2) + pow(h1-h2, 2)); //distancia euclidiana
		int terrainType=1;
		
		if (terrain.getTerrainType(nX, nY).equals(TerrainType.SAND))
			terrainType = 2;
		else if (terrain.getTerrainType(nX, nY).equals(TerrainType.ROCK))
			terrainType = 3;
			
		if(abs(h1-h2) > 10)
			return 10000000;	
		
		return terrainType * d * pow(E, sqrt(abs(h1-h2)));	
	}

	@Override
	public Object clone() {
		return new RoverState(xPos,yPos,terrain);
	}

	@Override
	public int hashCode() {
		return xPos^yPos;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RoverState other = (RoverState) obj;
		if( this.xPos != other.getCoordX() || this.yPos != other.getCoordY() )
			return false;

		return true;
	}	
	
	public int getCoordX() {
		return xPos;
	}

	public int getCoordY() {
		return yPos;
	}


}