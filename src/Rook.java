import java.util.ArrayList;

public class Rook extends Piece
{

	public Rook(int row, int col, int colour, BoardPanel board)
	{
		super(row, col, colour, "Rook" + colour, board);
		this.pointValue = 5*colour;
		// TODO Auto-generated constructor stub
	}


	public String toStringName()
	{
		return "Rook";
	}
	

	@Override
	public ArrayList<Move> getMoves()
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		boolean ok = true;
		// All the moves down.
		int displacement = 1;
		while (ok && displacement + row < 8)
		{
			ok = canMoveHere(row + displacement, col);
			if (ok)
			{
				moves.add(new Move(this, row + displacement, col,board));
				displacement++;
				if(isEnemyHere(row+displacement-1,col))
					ok=false;
			}

		}
		// All the moves up.
		displacement = 1;
		ok = true;
		while (ok && row - displacement >= 0)
		{
			ok = canMoveHere(row - displacement, col);
			if (ok)
			{
				moves.add(new Move(this, row - displacement, col,board));
				displacement++;
				if(isEnemyHere(row-displacement+1,col))
					ok = false;
			}
		}
		// All the moves right.
		displacement = 1;
		ok = true;
		while (ok && col + displacement < 8)
		{
			ok = canMoveHere(row, col + displacement);
			if (ok)
			{
				moves.add(new Move(this, row, col + displacement,board));
				displacement++;
				if(isEnemyHere(row,col+displacement-1))
					ok=false;
			}
		}
		// All the moves left.
		displacement = 1;
		ok = true;
		while (ok && col - displacement >= 0)
		{
			ok = canMoveHere(row, col - displacement);
			if (ok)
			{
				moves.add(new Move(this, row, col - displacement,board));
				displacement++;
				if(isEnemyHere(row,col-displacement+1))
					ok=false;
			}
		}
		return moves;
	}

}
