import java.awt.*;

import java.util.ArrayList;


import javax.swing.ImageIcon;
/**
 * Abstract object that stores a piece's position on the board, its images and its value for the ai calculations.
 * Can return its point value, a list of moves it can make, make a move, and draw itself.
 * @author Peter, Sina , Christian
 *
 */
public abstract class Piece
{
	private int side;
	protected int noOfMoves;
	protected int pointValue;
	private final int WIDTH = 90;
	private final int HEIGHT = 90;
	protected BoardPanel board;
	private boolean selected;
	protected int row;
	protected int col;
	private Image unSelectedImage;

	private Image capturedImage;
	private boolean hasMoved;
	// -1 is white. 1 is black.
	protected int colour;
	private final Image SELECTED_TILE_PIECE = new ImageIcon(
			"img\\selectedTilePiece.png").getImage();

	public Piece(int row, int col, int colour, String imageName,
			BoardPanel board)
	{
		hasMoved = false;
		this.row = row;
		this.col = col;
		this.colour = colour;
		this.unSelectedImage = new ImageIcon("img\\" + imageName + ".png")
				.getImage();
		this.capturedImage = new ImageIcon("img\\" + imageName + ".png")
				.getImage().getScaledInstance(70, 60, 100);
		selected = false;
		this.board = board;
	}


	/**
	 * 
	 * @param other
	 * @return
	 */

	public int distanceFromCentre()
	{
		return -colour * (Math.abs(2 * row - 7) + Math.abs(2 * col - 7));
	}

	public int pointValue()
	{

		return this.pointValue;
	}

	public int controlledSquares()
	{
		return getMoves().size() * colour;
	}

	public boolean isEnemy(Piece other)
	{
		if (this.colour * -1 == other.colour)
			return true;
		return false;
	}

	public boolean isEnemyHere(int row, int col)
	{
		return board.isEnemy(row, col, this);
	}

	public void undo()
	{
		noOfMoves -= 2;
	}

	public void moved(boolean moved)
	{
		hasMoved = !moved;
	}

	/**
	 * 
	 * @param g
	 */
	public void draw(Graphics g)
	{
		if (selected)
			g.drawImage(SELECTED_TILE_PIECE, col * 90, row * 90 - 1, null);
		g.drawImage(unSelectedImage, col * WIDTH, row * HEIGHT, null);

	}

	public void draw(Graphics g, int row, int col)
	{
		g.drawImage(unSelectedImage, row, col, null);
	}

	/**
	 * Draws captured Pieces
	 * 
	 * @param g
	 * @param place
	 * @param player
	 */
	public void draw(Graphics g, int place, double player)
	{
		if (player == 1.0)
			g.drawImage(capturedImage, 720 + 40 * (place % 3),
					340 + 60 * (place / 3), null);
		else
			g.drawImage(capturedImage, 860 + 40 * (place % 3),
					340 + 60 * (place / 3), null);
	}

	public abstract ArrayList<Move> getMoves();

	public void removeInCheckMoves(ArrayList<Move> moves)
	{
		board.removeInCheckMoves(moves);
	}

	/**
	 * 
	 * @param row The row on the board this piece is checking if it can move to.
	 * @param col The column on the board this piece is checking if it can move
	 *            to.
	 * @param board The board in which the piece is on.
	 * @return True if this piece can move to the given position on the given
	 *         board and false otherwise.
	 */
	public boolean canMoveHere(int row, int col)
	{
		if (board.isEmpty(row, col))
			return true;
		return (board.isEnemy(row, col, this));
	}

	// public boolean equals(Piece other)
	// {
	// if (other == null)
	// return false;
	// return (this.row == other.row && this.col == other.col);
	//
	// }

	/**
	 * Specific check for a pawn's can move as it is different.
	 * 
	 * @param rowTo
	 * @param colTo
	 * @return
	 */
	public boolean canPawnMove(int rowTo, int colTo)
	{
		if (colTo != this.col)
		{
			if (board.getDoubleMovedPawn() != null)
			{
				Pawn dMPawn = board.getDoubleMovedPawn();

				if (dMPawn.col == colTo && dMPawn.row - dMPawn.colour == rowTo)
				{
					return true;
				}
			}
			return (board.isEnemy(rowTo, colTo, this));
		}
		return (board.isEmpty(rowTo, colTo));

	}

	/**
	 * Moves this piece to a given position. Called by the move class.
	 * 
	 * @param rowTo
	 * @param colTo
	 */
	public void makeMove(int rowTo, int colTo)
	{

		board.makeMove(rowTo, colTo, this, row, col);
		row = rowTo;
		col = colTo;
		if ((this instanceof Pawn))
		{
			if (colour == 1 && rowTo == 7)
			{
				board.promote(row, col, colour);
			}
			else if (colour == -1 && rowTo == 0)
			{
				board.promote(row, col, colour);
			}
		}
		noOfMoves++;

	}

	public void madeMove()
	{
		hasMoved = true;
	}

	public boolean hasMoved()
	{
		return noOfMoves != 0;
	}

	/**
	 * Sets this piece as selected.
	 */
	public void select()
	{
		selected = true;
	}

	/**
	 * Sets this piece as unselected.
	 */
	public void unselect()
	{
		selected = false;
	}

	public boolean belongsToPlayer(int selectedPieceColour)
	{
		return (this.colour == selectedPieceColour);
	}

	public String toStringName()
	{
		return null;
	}

	public String toString()
	{
		return "row: " + row + " col:" + col;
	}
}
