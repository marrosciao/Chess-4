import java.util.ArrayList;


public class Knight extends Piece
{


	public Knight(int row, int col, int colour,BoardPanel board)
	{
		super(row, col, colour, "Knight"+colour,board);
		this.pointValue=3*colour;
		// TODO Auto-generated constructor stub
	}
	

	public String toStringName()
	{
		return "Knight";
	}

	@Override
	/**
	 * Returns a list of all the possible moves this piece can make.
	 */
	public ArrayList<Move> getMoves()
	{
		// TODO Auto-generated method stub
		ArrayList<Move>moves = new ArrayList<Move>();
		//Up right
		if(canMoveHere(row-2,col+1))
			moves.add(new Move(this,row-2,col+1,board));		
		//Right up
		if(canMoveHere(row-1,col+2))
			moves.add(new Move(this,row-1,col+2,board));
		//Up left
		if(canMoveHere(row-2,col-1))
			moves.add(new Move(this,row-2,col-1,board));
		//Left up
		if(canMoveHere(row-1,col-2))
			moves.add(new Move(this,row-1,col-2,board));
		//Down right
		if(canMoveHere(row+2,col+1))
			moves.add(new Move(this, row+2,col+1,board));
		//Right down
		if(canMoveHere(row+1,col+2))
			moves.add(new Move(this,row+1,col+2,board));
		//Down left
		if(canMoveHere(row+2,col-1))
			moves.add(new Move(this, row+2,col-1,board));
		//Left down
		if(canMoveHere(row+1,col-2))
			moves.add(new Move(this,row+1,col-2,board));
		return moves;
	}

}
