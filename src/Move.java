import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

/**
 * Move Object that stores what Piece has moved, what Piece was taken, where the
 * moved was from and where it moved to.
 * 
 * @author PeterZhu
 *
 */
public class Move implements Comparable<Move>
{
	private final Image SELECTED_TILE = new ImageIcon("img\\selectedTile.png")
			.getImage();

	private BoardPanel board;
	private Piece moved;
	private int rowFrom;
	private int colFrom;
	private int rowTo;
	private int colTo;
	private Piece taken;
	/**
	 * Constructs a move without a piece that was taken.
	 * @param moved The piece that was moved.
	 * @param rowTo The row the piece was moved to.
	 * @param colTo The column the piece was moved to.
	 */
	public Move(Piece moved, int rowTo, int colTo)
	{
		this.board = null;
		this.moved = moved;
		rowFrom = moved.row;
		colFrom = moved.col;
		this.rowTo = rowTo;
		this.colTo = colTo;
		this.taken = null;
	}
	/**
	 * Constructs a move with a piece that was taken.
	 * @param moved The piece that was moved.
	 * @param taken The piece that was taken by the piece that was moved.
	 * @param rowTo The row the moved piece moved to.
	 * @param colTo The column the moved piece moved to.
	 */
	public Move(Piece moved, Piece taken, int rowTo, int colTo)
	{
		this.board = null;
		this.moved = moved;
		rowFrom = moved.row;
		colFrom = moved.col;
		this.rowTo = rowTo;
		this.colTo = colTo;
		this.taken = taken;
	}
	/**
	 * Creates a move with the board that this move occured in.
	 * @param moved
	 * @param rowTo
	 * @param colTo
	 * @param board
	 */
	public Move(Piece moved, int rowTo, int colTo, BoardPanel board)
	{
		if (moved.isEnemyHere(rowTo, colTo))
			taken = board.getBoard()[rowTo][colTo];
		else if ((moved instanceof Pawn)
				&& (board.getDoubleMovedPawn() != null)
				&& (board.getDoubleMovedPawn().col == colTo)
				&& (board.getDoubleMovedPawn().row == rowTo - moved.colour)
				&& board.getDoubleMovedPawn().colour == -moved.colour)
			taken = board.getBoard()[rowTo - moved.colour][colTo];
		else
			taken = null;
		this.board = board;
		this.moved = moved;
		rowFrom = moved.row;
		colFrom = moved.col;
		this.rowTo = rowTo;
		this.colTo = colTo;
	}

	public int compareTo(Move otherMove)
	{
		int otherDistance2 = Math.abs(2 * otherMove.rowTo - 7)
				+ Math.abs(2 * otherMove.colTo - 7);
		int thisDistance2 = Math.abs(2 * this.rowTo - 7)
				+ Math.abs(2 * this.colTo - 7);
		int otherDistance = Math.abs(2 * otherMove.rowFrom - 7)
				+ Math.abs(2 * otherMove.colFrom - 7);
		int thisDistance = Math.abs(2 * this.rowFrom - 7)
				+ Math.abs(2 * this.colFrom - 7);
		int otherPoints;
		int thisPoints;
		if (this.taken == null)
			thisPoints = 0;
		else
			thisPoints = this.taken.pointValue();
		if (otherMove.taken == null)
			otherPoints = 0;
		else
			otherPoints = otherMove.taken.pointValue();
		if (Math.abs(otherPoints) + 0.05 * (otherDistance - otherDistance2) > Math
				.abs(thisPoints) + 0.05 * (thisDistance - thisDistance2))
			return 1;
		else if (Math.abs(otherPoints) + 0.05
				* (otherDistance - otherDistance2) < 0.05
				* (thisDistance - thisDistance2) + Math.abs(thisPoints))
			return -1;
		else
			return 0;

	}

	public void addTaken(Piece taken)
	{
		this.taken = taken;
	}

	public void draw(Graphics g)
	{

		g.drawImage(SELECTED_TILE, colTo * 90, rowTo * 90 - 1, null);
	}

	/**
	 * moves the piece to the desired location.
	 */
	public void makeMove()
	{
		moved.makeMove(rowTo, colTo);
	}

	public String toStringPiece()
	{
		return moved.toStringName();
	}

	public String toStringTo()
	{

		return ("R: " + (rowTo + 1) + " C: " + (colTo + 1));
	}

	public String toStringFrom()
	{

		return ("R: " + (rowFrom + 1) + " C: " + (colFrom + 1));

	}

	public boolean equals(Object other)
	{
		if (!(other instanceof Move))
			return false;
		Move otherMove = (Move) other;
		return (otherMove.moved == moved && otherMove.colTo == colTo && otherMove.rowTo == rowTo);
	}

	public int getRowTo()
	{
		return rowTo;
	}

	public int getColTo()
	{
		return colTo;
	}

	public int getRowFrom()
	{
		return rowFrom;
	}

	public int getColFrom()
	{
		return colFrom;
	}

	public Piece getMoved()
	{
		return moved;
	}

	/**
	 * Moves the moved piece back to its original position
	 * 
	 * @return The piece that moved captured after its move. Null if none was
	 *         captured.
	 */
	public Piece undo()
	{

		moved.makeMove(rowFrom, colFrom);
		moved.undo();
		return taken;
	}

	/**
	 * 
	 * @return True if a pawn made a double move this turn and false otherwise.
	 */
	public boolean doubleMovedPawn()
	{
		if (moved instanceof Pawn)
		{
			if (Math.abs(rowFrom - rowTo) == 2)
			{
				return true;
			}
		}
		return false;
	}
}
