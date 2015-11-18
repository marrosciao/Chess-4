import java.util.ArrayList;



public class King extends Piece
{

	public King(int row, int col, int colour,BoardPanel board)
	{
		super(row, col, colour, "King"+colour,board);
		this.pointValue = 1000*colour;
	}
	
	
	
	public String toStringName()
	{
		return "King";
	}
	public ArrayList<Move> getCheckMoves()
	{
		
		ArrayList<Move>moves = new ArrayList<Move>();
		//down.
		if(canMoveHere(row+1,col))
			moves.add(new Move(this,row+1,col,board));
		//Down right.
		if(canMoveHere(row+1,col+1))
			moves.add(new Move(this,row+1,col+1,board));
		//Down left.
		if(canMoveHere(row+1,col-1))
			moves.add(new Move(this,row+1,col-1,board));
		//Up
		if(canMoveHere(row-1,col))
			moves.add(new Move(this,row-1,col,board));
		//Up right.
		if(canMoveHere(row-1,col+1))
			moves.add(new Move(this,row-1,col+1,board));
		//Up left.
		if(canMoveHere(row-1,col-1))
			moves.add(new Move(this,row-1,col-1,board));
		//Left.
		if(canMoveHere(row,col+1))
			moves.add(new Move(this,row,col+1,board));
		//Right
		if(canMoveHere(row,col-1))
			moves.add(new Move(this,row,col-1,board));
	
		return moves;
	}
	@Override
	public ArrayList<Move> getMoves()
	{
		ArrayList<Move>moves = new ArrayList<Move>();
		//down.
		if(canMoveHere(row+1,col))
			moves.add(new Move(this,row+1,col,board));
		//Down right.
		if(canMoveHere(row+1,col+1))
			moves.add(new Move(this,row+1,col+1,board));
		//Down left.
		if(canMoveHere(row+1,col-1))
			moves.add(new Move(this,row+1,col-1,board));
		//Up
		if(canMoveHere(row-1,col))
			moves.add(new Move(this,row-1,col,board));
		//Up right.
		if(canMoveHere(row-1,col+1))
			moves.add(new Move(this,row-1,col+1,board));
		//Up left.
		if(canMoveHere(row-1,col-1))
			moves.add(new Move(this,row-1,col-1,board));
		//Left.
		if(canMoveHere(row,col+1))
			moves.add(new Move(this,row,col+1,board));
		//Right
		if(canMoveHere(row,col-1))
			moves.add(new Move(this,row,col-1,board));
		boolean canCastle=false;
		// needs to check for checks
		if (row==7&&col==4&&colour==-1&&!this.hasMoved())
		{
			
			
			if ((board.getBoard()[7][0] instanceof Rook)&&!board.getBoard()[7][0].hasMoved())
			{
				if (board.getBoard()[7][1]==null&&board.getBoard()[7][2]==null&&board.getBoard()[7][3]==null)
				{
					
				    Move kingMove = new Move (this, 7,3,board);
				    kingMove.makeMove();
				    if (!board.inCheck(-1))
				    {
				    	canCastle=true;
				    }
				    kingMove.undo();
				    if (canCastle)
					moves.add(new Move(this,7,2,board));
					
					
				}
			}
			if ((board.getBoard()[7][7] instanceof Rook )&&!board.getBoard()[7][7].hasMoved())
			{
				if (board.getBoard()[7][5]==null&&board.getBoard()[7][6]==null)
				{
					 Move kingMove = new Move (this, 7,5,board);
					    kingMove.makeMove();
					    if (!board.inCheck(-1))
					    {
					    	canCastle=true;
					    }
					    kingMove.undo();
					    if (canCastle)
						moves.add(new Move(this,7,6,board));
						
					
				}
			}
		}
		if (row==0&&col==4&&colour==1&&!this.hasMoved())
		{
			
			if ((board.getBoard()[0][0] instanceof Rook)&&!board.getBoard()[0][0].hasMoved())
			{
				if (board.getBoard()[0][1]==null&&board.getBoard()[0][2]==null&&board.getBoard()[0][3]==null)
				{
					 Move kingMove = new Move (this, 0,3,board);
					    kingMove.makeMove();
					    if (!board.inCheck(1))
					    {
					    	canCastle=true;
					    }
					    kingMove.undo();
					    if (canCastle)
						moves.add(new Move(this,0,2,board));
						
					
				}
			}
			if ((board.getBoard()[0][7] instanceof Rook )&&!board.getBoard()[0][7].hasMoved())
			{
				if (board.getBoard()[0][5]==null&&board.getBoard()[0][6]==null)
				{
					 Move kingMove = new Move (this, 0,5,board);
					    kingMove.makeMove();
					    if (!board.inCheck(1))
					    {
					    	canCastle=true;
					    }
					    kingMove.undo();
					    if (canCastle)
						moves.add(new Move(this,0,6,board));
						
					
				}
			}
		}
		return moves;
	}
	
	

}
