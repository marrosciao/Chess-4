import java.util.ArrayList;


public class Queen extends Piece
{

	
	public Queen(int row, int col, int colour,BoardPanel board)
	{
		super(row, col, colour, "Queen"+colour,board);
		this.pointValue = 9*colour;
		// TODO Auto-generated constructor stub
	}

	public String toStringName()
	{
		return "Queen";
	}

	@Override
	public ArrayList<Move> getMoves()
	{
//		if (board.inGame)
//		{
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
		ok = true;
		displacement = 1;
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

