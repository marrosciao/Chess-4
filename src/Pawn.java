import java.util.ArrayList;
public class Pawn extends Piece
{

	
	public Pawn(int row, int col, int colour, BoardPanel board)
	{
		super(row, col, colour, "Pawn" + colour, board);
		this.pointValue = colour;

	}



	public int controlledSquares()
	{
		int controlledSquares=0;
		if (canMoveHere(row+colour, col+1))
			controlledSquares++;
		if (canMoveHere(row+colour, col-1))
			controlledSquares++;
		return colour*controlledSquares;
	}
	@Override
	public ArrayList<Move> getMoves()
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		// Whites move up, blacks move down. 
		if (canMoveHere(row + colour, col))
		{
			moves.add(new Move(this, row + colour, col));
			if ((row == 1 && colour == 1) || (row == 6 && colour == -1))
				if (canMoveHere(row + colour * 2, col))
					moves.add(new Move(this, row + colour * 2, col));
		}
		if (canMoveHere(row + colour, col + 1))
			moves.add(new Move(this, row + colour, col + 1,board));
		if (canMoveHere(row + colour, col - 1))
			moves.add(new Move(this, row + colour, col - 1,board));

		return moves;
	}

	public String toStringName()
	{
		return "Pawn";
	}
	public boolean canMoveHere(int row, int col)
	{
		return canPawnMove(row, col);
	}

	public boolean canPromotePawn()
	{
		return ((row == 0 && colour == -1) || (row == 7 && colour == 1));
	}

}
