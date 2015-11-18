import java.util.ArrayList;
/**
 * Bishop object that extends Piece.  Can return its name and the moves it can make.
 * @author Peter Zhu
 * @version 19/01/2015
 */
public class Bishop extends Piece
{
	/**
	 * 
	 * @param row The row of this piece.  0 based.
	 * @param col The column of this piece. 0 based.
	 * @param colour The colour of this week.  1 is black, -1 is white.
	 * @param board The board this piece is on.
	 */
	public Bishop(int row, int col, int colour, BoardPanel board)
	{
		super(row, col, colour, "Bishop" + colour, board);
		this.pointValue = 3*colour;
		// TODO Auto-generated constructor stub
	}
	


	@Override
	public ArrayList<Move> getMoves()
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		boolean ok = true;
		// All the moves down right.
		int displacement = 1;
		while (ok && displacement + row < 8 && displacement + col < 8)
		{
			ok = canMoveHere(row + displacement, col + displacement);
			if (ok)
			{
				moves.add(new Move(this, row + displacement, col + displacement,board));
				displacement++;
				if(isEnemyHere(row+displacement-1,col+displacement-1))
					ok=false;
			}
		}
		// All the moves down left.
		displacement = 1;
		ok = true;
		while (ok && row + displacement < 8 && col - displacement >= 0)
		{
			ok = canMoveHere(row + displacement, col - displacement);
			if (ok)
			{
				moves.add(new Move(this, row + displacement, col - displacement,board));
				displacement++;
				if(isEnemyHere(row+displacement-1,col-displacement+1))
					ok = false;
			}
		}
		// All the moves up right.
		displacement = 1;
		ok = true;
		while (ok && row - displacement >= 0 && col + displacement < 8)
		{
			ok = canMoveHere(row - displacement, col + displacement);
			if (ok)
			{
				moves.add(new Move(this, row - displacement, col + displacement,board));
				displacement++;
				if(isEnemyHere(row-displacement+1,col+displacement-1))
					ok=false;
			}
		}
		// All the moves up left.
		displacement = 1;
		ok = true;
		while (ok && row - displacement >= 0 && col - displacement >= 0)
		{
			ok = canMoveHere(row - displacement, col - displacement);
			if (ok)
			{
				moves.add(new Move(this, row - displacement, col - displacement,board));
				displacement++;
				if(isEnemyHere(row-displacement+1,col-displacement+1))
					ok=false;
			}
		}
		return moves;

	}
	
	public String toStringName()
	{
		return "Bishop";
	}


}
