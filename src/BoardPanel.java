import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.*;
import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class BoardPanel extends JPanel implements MouseListener,
		MouseMotionListener, Serializable
{
	private boolean clickedEffect;
	private boolean AI;
	private int side;
	private boolean soundOn;
	private boolean snowFlakeOn;
	static int replaces = 0;
	protected boolean inGame;
	private ArrayList<Snow> snowList;
	private int delay;
	private Timer timer;
	static int branchingFactor = 0;
	private Move bestMove;
	protected boolean isPieceSelected;
	private Piece[][] board;
	private final int WHITE = -1;
	private final int BLACK = 1;
	private final int SQUARE_LENGTH = 90;
	private int currentPlayer;
	private ArrayList<Move> pieceMoves;
	private Piece selectedPiece;
	private LinkedList<Move> moves;
	private ChessMain frame;
	protected ArrayList<Piece> capturedBlacks;
	protected ArrayList<Piece> capturedWhites;
	private boolean undoSelected;
	private boolean newGameSelected;
	private boolean menuSelected;
	private boolean helpSelected;
	private boolean soundSelected;
	private final Image SNOWFLAKE_ON = new ImageIcon("img\\snowFlakeOn.png")
			.getImage();
	private final Image SNOWFLAKE_OFF = new ImageIcon("img\\snowFlakeOff.png")
			.getImage();
	private final Image SOUND_ON = new ImageIcon("img\\soundOn.png").getImage();
	private final Image SOUND_OFF = new ImageIcon("img\\soundOff.png")
			.getImage();

	private final Image GAME_BOARD = new ImageIcon("img\\gameBoard.png")
			.getImage();
	private final Image UNDOSELECTED = new ImageIcon(
			"img\\undoButtonSelected.png").getImage();
	private final Image UNDOUNSELECTED = new ImageIcon(
			"img\\undoButtonUnselected.png").getImage();
	private final Image NEWGAME_SELECTED = new ImageIcon(
			"img\\newGameSelected.png").getImage();
	private final Image NEWGAME_UNSELECTED = new ImageIcon(
			"img\\newGameUnselected.png").getImage();
	private final Image MENU_SELECTED = new ImageIcon("img\\menuSelected.png")
			.getImage();
	private final Image MENU_UNSELECTED = new ImageIcon(
			"img\\menuUnselected.png").getImage();
	private final Image HELP_SELECTED = new ImageIcon("img\\helpSelected.png")
			.getImage();
	private final Image HELP_UNSELECTED = new ImageIcon(
			"img\\helpUnselected.png").getImage();

	public BoardPanel(ChessMain frame)
	{
		// Add listeners.
		setFocusable(true);
		requestFocusInWindow();
		this.frame = frame;
		snowList = new ArrayList<Snow>();
		addMouseListener(this);
		addMouseMotionListener(this);
		newGame();
	}

	public Piece[][] getBoard()
	{
		return this.board;
	}
	/**
	 * Sets the options for this board's current game.
	 * @param AI true if it's a single player game and false otherwise.
	 * @param side The colour
	 * @param difficulty
	 */
	public void setOptions(boolean AI, int side, int difficulty)
	{
		this.AI = AI;
		this.side = side;

		if (AI)
			currentPlayer = side;
		else
			currentPlayer = WHITE;

		if (side == 1)
			AIWhite();

	}
	/**
	 * 
	 */
	public void newGame()
	{
		soundSelected = false;
		soundOn = frame.isSoundOn();
		clickedEffect = false;
		inGame = true;
		newGameSelected = false;
		snowFlakeOn = frame.isSnowOn();		
		delay = 0;
		timer = new Timer(5, new TimerEventHandler());
		timer.start();
		undoSelected = false;
		pieceMoves = new ArrayList<Move>();
		isPieceSelected = false;
		selectedPiece = null;
		moves = new LinkedList<Move>();
		capturedBlacks = new ArrayList<Piece>();
		capturedWhites = new ArrayList<Piece>();
		board = new Piece[8][8];
		for (int col = 0; col < 8; col++)
		{
			board[1][col] = new Pawn(1, col, BLACK, this);
			board[6][col] = new Pawn(6, col, WHITE, this);
		}
		board[0][0] = new Rook(0, 0, BLACK, this);
		board[0][7] = new Rook(0, 7, BLACK, this);
		board[0][1] = new Knight(0, 1, BLACK, this);
		board[0][6] = new Knight(0, 6, BLACK, this);
		board[0][2] = new Bishop(0, 2, BLACK, this);
		board[0][5] = new Bishop(0, 5, BLACK, this);
		board[0][4] = new King(0, 4, BLACK, this);
		board[0][3] = new Queen(0, 3, BLACK, this);
		board[7][0] = new Rook(7, 0, WHITE, this);
		board[7][7] = new Rook(7, 7, WHITE, this);
		board[7][1] = new Knight(7, 1, WHITE, this);
		board[7][6] = new Knight(7, 6, WHITE, this);
		board[7][2] = new Bishop(7, 2, WHITE, this);
		board[7][5] = new Bishop(7, 5, WHITE, this);
		board[7][4] = new King(7, 4, WHITE, this);
		board[7][3] = new Queen(7, 3, WHITE, this);

	}

	public void AIWhite()
	{

		System.out.println("swag");
		findBestMove();
		bestMove.getMoved().makeMove(bestMove.getRowTo(), bestMove.getColTo());
		bestMove = null;
	}

	public boolean inGame()
	{
		return inGame;
	}
	private class TimerEventHandler implements ActionListener
	{

		public void actionPerformed(ActionEvent event)
		{
			repaint();
			if (frame != null)
				if (snowFlakeOn && frame.currentScreen == frame.GAME
						&& !frame.inHelp)
				{
					if (delay == 0)
					{
						for (int numSnow = 1; numSnow < 3; numSnow++)
							snowList.add(new Snow((int) (Math.random() * 1000)));
					}
					delay++;
					if (delay == 50)
						delay = 0;
				}

		}

	}

	public void removeInCheckMoves(ArrayList<Move> moves)
	{
		for (int moveIndex = 0; moveIndex < moves.size(); moveIndex++)
		{
			Move thisMove = moves.get(moveIndex);

			if (thisMove.getMoved().isEnemyHere(thisMove.getRowTo(),
					thisMove.getColTo()))
			{
				thisMove.addTaken(board[thisMove.getRowTo()][thisMove
						.getColTo()]);
			}

			else if (selectedPiece instanceof Pawn
					&& getDoubleMovedPawn() != null
					&& (getDoubleMovedPawn().row - getDoubleMovedPawn().colour == thisMove
							.getRowTo() && getDoubleMovedPawn().col == thisMove
							.getColTo()))
			{

				thisMove.addTaken(getDoubleMovedPawn());
			}
			thisMove.getMoved().makeMove(thisMove.getRowTo(),
					thisMove.getColTo());
			this.moves.add(thisMove);
			if (inCheck(thisMove.getMoved().colour))
			{
				moves.remove(thisMove);
				moveIndex--;
			}
			undo();
		}
	}

	/**
	 * Checks if a space on the board is empty.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isEmpty(int row, int col)
	{
		if (row > 7 || row < 0 || col > 7 || col < 0)
			return false;
		if (board[row][col] == null)
			return true;
		return false;
	}

	/**
	 * 
	 * @return null if the previous move did not have a double move pawn and the
	 *         double moved pawn if there was one.
	 */
	public Pawn getDoubleMovedPawn()
	{

		if (moves.isEmpty() || (!moves.getLast().doubleMovedPawn()))
			return null;
		else
		{
			return (Pawn) moves.getLast().getMoved();
		}
	}

	/**
	 * Checks if a space on the board is occupied by the enemy of a given piece.
	 * 
	 * @param row
	 * @param col
	 * @param other
	 * @return
	 */
	public boolean isEnemy(int row, int col, Piece other)
	{
		if (row > 7 || row < 0 || col > 7 || col < 0)
			return false;
		if (board[row][col] == null)
			return false;
		return board[row][col].isEnemy(other);

	}

	/**
	 * Removes a Piece from the Board.
	 * 
	 * @param row
	 * @param col
	 */
	private void removePiece(int row, int col)
	{
		if (board[row][col].colour == BLACK)
			capturedBlacks.add(board[row][col]);
		else
			capturedWhites.add(board[row][col]);
		board[row][col] = null;

	}

	/**
	 * 
	 * @param rowTo The destination row.
	 * @param colTo The destination column.
	 * @param piece The piece moving.
	 * @param fromRow
	 * @param fromCol
	 */
	public void makeMove(int rowTo, int colTo, Piece piece, int fromRow,
			int fromCol)
	{
		if (isEnemy(rowTo, colTo, piece))
			removePiece(rowTo, colTo);
		else if (piece instanceof Pawn)
		{
			Pawn doubleMoved = getDoubleMovedPawn();
			if (doubleMoved != null)
			{
				if (doubleMoved.col == colTo
						&& doubleMoved.row - doubleMoved.colour == rowTo
						&& doubleMoved.colour == -piece.colour)
				{
					removePiece(doubleMoved.row, doubleMoved.col);
				}
			}
		}
		board[rowTo][colTo] = piece;
		board[fromRow][fromCol] = null;
	}

	/**
	 * Moves a piece on the board form one position to another. Note, it does
	 * not update the piece's position variables and so this is never called on
	 * its own, instead call piece's make move which calls this
	 * 
	 * @param thisMove
	 */
	public void makeMove(Move thisMove)
	{
		int rowTo = thisMove.getRowTo();
		int colTo = thisMove.getColTo();
		Piece piece = thisMove.getMoved();
		int fromRow = thisMove.getRowFrom();
		int fromCol = thisMove.getColFrom();
		if (isEnemy(rowTo, colTo, piece))
			removePiece(rowTo, colTo);
		else if (piece instanceof Pawn)
		{
			Pawn doubleMoved = getDoubleMovedPawn();
			if (doubleMoved != null)
			{
				if (doubleMoved.col == colTo
						&& doubleMoved.row - doubleMoved.colour == rowTo)
				{
					removePiece(doubleMoved.row, doubleMoved.col);
				}
			}
		}
		board[rowTo][colTo] = piece;
		board[fromRow][fromCol] = null;
	}

	public double bestMove(int depth, double alpha, double beta, int colour,
			boolean isNull)
	{

		branchingFactor++;
		double bestValue = Integer.MIN_VALUE;
		if (depth <= 0)
		{
			return colour * evaluateBoard();
		}
		else
		{
			if (!isNull)
			{

				double nullValue = -bestMove(depth - 2, -beta, -beta + 1,
						-colour, true);
				if (nullValue >= beta)
					return beta;
			}
			ArrayList<Move> allMoves = new ArrayList<Move>();

			for (int row = 0; row < 8; row++)
			{
				for (int col = 0; col < 8; col++)
				{
					if (board[row][col] != null
							&& board[row][col].belongsToPlayer(colour))
					{
						allMoves.addAll(board[row][col].getMoves());
					}

				}
			}
			removeInCheckMoves(allMoves);
			Collections.sort(allMoves);
			for (Move nextMove : allMoves)
			{
				double value = Integer.MIN_VALUE;

				if (nextMove.getMoved() instanceof King
						&& Math.abs(nextMove.getColTo() - nextMove.getColFrom()) == 2)
				{

					Move rookMove;
					if (nextMove.getColTo() - nextMove.getColFrom() == 2)
					{
						nextMove.makeMove();

						if (colour == 1)
							rookMove = new Move(board[0][7], 0, 5, this);
						else
							rookMove = new Move(board[7][7], 7, 5, this);
						rookMove.makeMove();
						moves.add(rookMove);
						moves.add(nextMove);

						value = -bestMove(depth - 1, -beta, -alpha, -colour,
								false);
						moves.removeLast();
						moves.removeLast();
						rookMove.undo();
						nextMove.undo();

					}
					else if (nextMove.getColTo() - nextMove.getColFrom() == -2)
					{
						nextMove.makeMove();

						if (colour == -1)
							rookMove = new Move(board[7][0], 7, 3, this);
						else
							rookMove = new Move(board[0][0], 0, 3, this);
						rookMove.makeMove();
						moves.add(rookMove);
						moves.add(nextMove);

						value = -bestMove(depth - 1, -beta, -alpha, -colour,
								false);
						moves.removeLast();
						moves.removeLast();
						rookMove.undo();
						nextMove.undo();

					}

				}

				else
				{
					nextMove.makeMove();
					moves.add(nextMove);
					value = -bestMove(depth - 1, -beta, -alpha, -colour, false);
					moves.removeLast();

					Piece taken = nextMove.undo();

					if (taken != null)
					{

						board[taken.row][taken.col] = taken;
						if (taken.belongsToPlayer(BLACK))
						{
							capturedBlacks.remove(taken);
						}
						else
						{
							capturedWhites.remove(taken);
						}
					}

				}

				if (value > bestValue)
				{

					if (depth == 1)
					{
						bestMove = nextMove;

					}
					bestValue = value;

				}
				// YOLO
				else if (false)
				{
					if (depth == 4)
					{

						bestMove = nextMove;
					}
				}
				if (value >= alpha)
				{
					alpha = value;
				}
				if (alpha >= beta)
					break;

			}
		}

		return bestValue;
	}

	public BoardPanel update()
	{
		return this;
	}

	public Move findBestMove()
	{
		replaces = 0;
		branchingFactor = 0;
		System.out.println(bestMove(1, Integer.MIN_VALUE + 1,
				Integer.MAX_VALUE, -side, false));

		moves.add(bestMove);
		return bestMove;
	}

	public double evaluateBoard()
	{

		int mobility = 0;
		int totalPoints = 0;
		int centreControl = 0;
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (board[row][col] != null)
				{
					Piece piece = board[row][col];
					mobility += piece.controlledSquares();
					totalPoints += piece.pointValue();
					centreControl += piece.distanceFromCentre();
				}
			}
		}
		return totalPoints;
	}

	/**
	 * Draws the board and all the pieces on it.
	 */
	public void paintComponent(Graphics g)
	{
		g.drawImage(GAME_BOARD, 0, -1, null);
		if (!undoSelected)
			g.drawImage(UNDOUNSELECTED, 925 + 10, 670 + 10, null);
		else
			g.drawImage(UNDOSELECTED, 925 + 10, 670 + 10, null);
		if (soundOn)
			g.drawImage(SOUND_ON, 780, 670 + 10, null);
		else
			g.drawImage(SOUND_OFF, 780, 670 + 10, null);
		if (!newGameSelected)
			g.drawImage(NEWGAME_UNSELECTED, 725, 670 - 4, null);
		else
			g.drawImage(NEWGAME_SELECTED, 725, 670 - 4, null);
		if (!menuSelected)
			g.drawImage(MENU_UNSELECTED, 815, 680, null);
		else
			g.drawImage(MENU_SELECTED, 815, 680, null);
		if (!helpSelected)
			g.drawImage(HELP_UNSELECTED, 935, 680 - 37, null);
		else
			g.drawImage(HELP_SELECTED, 935, 680 - 37, null);

		g.setFont(new Font("Calibri", Font.BOLD, 29));
		if (currentPlayer == -1)
		{
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("White", 905, 100);
		}
		else
		{
			g.setColor(Color.BLACK);
			g.drawString("Black", 905, 100);
		}
		// Displays the last made move.

		if (!moves.isEmpty())
		{
			g.setFont(new Font("Calibri", Font.BOLD, 20));
			Move lastMove = moves.getLast();
			if (currentPlayer == -1)
				g.setColor(Color.BLACK);
			else
				g.setColor(Color.LIGHT_GRAY);
			g.drawString(lastMove.toStringPiece(), 916, 150);
			g.drawString(lastMove.toStringFrom(), 910, 170);
			g.drawString("    to", 910, 190);
			g.drawString(lastMove.toStringTo(), 910, 210);
		}
		if (isPieceSelected)
		{
			for (Move thisMove : pieceMoves)
			{

				thisMove.draw(g);
			}
			selectedPiece.draw(g);
			// selectedPiece.draw(g, 896, 120);

			for (int x = 0; x < 8; x++)
				for (int y = 0; y < 8; y++)
					if (board[x][y] != null && board[x][y] != selectedPiece)
						board[x][y].draw(g);

		}
		else
			for (int x = 0; x < 8; x++)
				for (int y = 0; y < 8; y++)
					if (board[x][y] != null)
						board[x][y].draw(g);

		int pieceNum = 0;
		for (Piece nextPiece : capturedWhites)
		{
			nextPiece.draw(g, pieceNum++, -1.0);
		}
		pieceNum = 0;
		for (Piece nextPiece : capturedBlacks)
		{
			nextPiece.draw(g, pieceNum++, 1.0);
		}
		if (snowFlakeOn)
			g.drawImage(SNOWFLAKE_ON, 885 + 10, 670 + 10, null);
		else
			g.drawImage(SNOWFLAKE_OFF, 885 + 10, 670 + 10, null);
		Iterator<Snow> it = snowList.iterator();
		while (it.hasNext())
		{
			Snow nextFlake = it.next();
			if (!nextFlake.canRemove())
				nextFlake.draw(g);
			else
				it.remove();
		}
	}

	/**
	 * Checks if there is currently a stalemate.
	 * 
	 * @param player
	 * @return
	 */
	public boolean inStaleMate()
	{
		if ((noPossibleMoves(BLACK) && !inCheck(BLACK))
				|| (noPossibleMoves(WHITE) && !inCheck(WHITE)))
		{
			return true;
		}
		// Checks for stalemate due to insufficient material.
		Piece whiteKnight = null;
		Piece blackKnight = null;
		Piece whiteBishop = null;
		Piece blackBishop = null;
		for (Piece[] row : board)
		{
			for (Piece piece : row)
			{
				// As long as there are pawns, there will be sufficient
				// material.
				// If a side has more than one bishop or knight, there will be
				// sufficient material.
				if (piece instanceof Pawn)
				{
					return false;
				}
				if (piece instanceof Knight)
				{
					if (piece.colour == BLACK)
					{
						if (blackKnight == null)
							blackKnight = piece;
						else
							return false;
					}
					else
					{
						if (whiteKnight == null)
							whiteKnight = piece;
						else
							return false;
					}
				}
				else if (piece instanceof Bishop)
				{
					if (piece.colour == BLACK)
					{
						if (blackBishop == null)
							blackBishop = piece;
						else
							return false;
					}
					else
					{
						if (whiteBishop == null)
							whiteBishop = piece;
						else
							return false;
					}
				}
				// If a piece on the board is not one of the above, there is
				// sufficient material.
				else if (piece != null && !(piece instanceof King))
					return false;

			}
		}
		// When given each side having either 0 or 1 knight and 0 or 1 bishop.
		if (blackKnight == null)
		{
			if (blackBishop == null)
			{
				// Only non-king is a white bishop.
				if (whiteKnight == null)
					return true;
				else
				{
					// If white has both a knight and a bishop.
					if (whiteBishop == null)
						return false;
					// Only non-king is white knight
					else
						return true;
				}
			}
			else
			{
				if (whiteKnight == null)
				{
					// Only non-king is black bishop.
					if (whiteBishop == null)
						return true;
					// both sides only have 1 bishop and 1 king. Checks if they
					// are on the same tile colour.
					else
						return (whiteBishop.row % 2 * (-(whiteBishop.col % 2)) == blackBishop.row
								% 2 * (-(blackBishop.col % 2)));
				}
				// If both sides have non-king pieces and one isn't a bishop.
				else
					return false;
			}
		}
		else
		{
			if (blackBishop == null)
			{
				if (whiteKnight == null)
				{
					// Only non-king is black knight.
					if (whiteBishop == null)
						return true;
					// Black has a knight and a king.
					else
						return false;
				}
				// White has a knight and black has a knight.
				else
					return false;
			}
			// Black has a knight and a bishop, thus having suffice material.
			else
				return false;
		}

	}

	public boolean noPossibleMoves(int player)
	{
		// Looks at all pieces that belongs to the player being checked to see
		// if
		// they're in checkmate.
		for (Piece[] row : board)
		{
			for (Piece piece : row)
			{
				if (piece != null)
				{
					if (piece.belongsToPlayer(player))
					{
						ArrayList<Move> thisPieceMoves = piece.getMoves();
						for (Move move : thisPieceMoves)
						{
							Piece taken;
							if (board[move.getRowTo()][move.getColTo()] != null)
							{
								taken = board[move.getRowTo()][move.getColTo()];
								moves.add(new Move(piece, taken, move
										.getRowTo(), move.getColTo()));
							}
							else
							{
								moves.add(move);
							}
							piece.makeMove(move.getRowTo(), move.getColTo());

							if (!inCheck(player))
							{
								undo();
								return false;
							}
							undo();
						}
					}
				}
			}
		}
		return true;
	}

	public void promote(int row, int col, int colour)
	{
		board[row][col] = null;
		board[row][col] = new Queen(row, col, colour, this);

	}

	/**
	 * Checks if a player is in check.
	 * 
	 * @param player
	 * @return
	 */
	public boolean inCheck(int player)
	{

		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				Piece thisPiece = board[row][col];
				if (thisPiece != null && thisPiece.belongsToPlayer(player * -1))
				{
					ArrayList<Move> enemyMoves;
					if (!(thisPiece instanceof King))
						enemyMoves = thisPiece.getMoves();
					else
					{

						enemyMoves = ((King) thisPiece).getCheckMoves();
					}

					for (Move move : enemyMoves)
					{

						Piece destination = board[move.getRowTo()][move
								.getColTo()];
						if (destination != null && destination instanceof King
								&& destination.belongsToPlayer(player))
							return true;
					}
				}
			}
		}
		return false;
	}

	public void undo()
	{
		if (inGame)
			if (!moves.isEmpty())
			{
				Piece taken = moves.removeLast().undo();

				if (taken != null)
				{
					board[taken.row][taken.col] = taken;
					if (taken.belongsToPlayer(BLACK))
					{
						capturedBlacks.remove(taken);
					}
					else
					{
						capturedWhites.remove(taken);
					}
				}
			}

	}

	public static void playSoundRaw(String fileName)
			throws MalformedURLException, LineUnavailableException,
			UnsupportedAudioFileException, IOException
	{
		File url = new File(fileName);
		Clip clip = AudioSystem.getClip();

		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		clip.open(ais);
		clip.start();
	}

	public void playSound(String fileName)
	{
		try
		{
			playSoundRaw(fileName);
		}
		catch (MalformedURLException ex)
		{
			System.out.println(1);
		}
		catch (LineUnavailableException ex)
		{
			System.out.println(2);
		}
		catch (UnsupportedAudioFileException ex)
		{
			System.out.println(3);
		}
		catch (IOException ex)
		{
			System.out.println(55);
		}
	}

	public void mousePressed(MouseEvent event)
	{
		if (soundOn && clickedEffect)
			playSound("sounds\\clicked.wav");

		Point pressedPoint = event.getPoint();
		int pressedRow = pressedPoint.y / SQUARE_LENGTH;
		int pressedCol = pressedPoint.x / SQUARE_LENGTH;
		Rectangle helpButton = new Rectangle(935, 680 - 37, 75, 35);
		Rectangle menuButton = new Rectangle(815, 680, 75, 35);
		if (pressedRow >= 0 && pressedRow <= 7 && pressedCol >= 0
				&& pressedCol <= 7 && inGame)
		{
			Piece currentPiece = board[pressedRow][pressedCol];

			// Piece is already selected
			if (isPieceSelected)
			{

				// Makes the move if a piece was already selected and the
				// clicked square can be moved to.
				Move thisMove;

				Pawn doubleMoved = getDoubleMovedPawn();
				if (selectedPiece.isEnemyHere(pressedRow, pressedCol))
				{
					thisMove = new Move(selectedPiece,
							board[pressedRow][pressedCol], pressedRow,
							pressedCol);
				}

				else if (selectedPiece instanceof Pawn
						&& doubleMoved != null
						&& (doubleMoved.row - doubleMoved.colour == pressedRow && doubleMoved.col == pressedCol))
				{

					thisMove = new Move(selectedPiece,
							board[doubleMoved.row][doubleMoved.col],
							pressedRow, pressedCol);
				}
				else
					thisMove = new Move(selectedPiece, pressedRow, pressedCol);
				if (pieceMoves.contains(thisMove))
				{
					// Checks if the current player's move would put him in
					// check.

					// Checks if the move would put the current player in check.

					if (selectedPiece instanceof King)
					{
						if (side == -1)
						{

							if (thisMove.getColFrom() - thisMove.getColTo() == 2)
							{
								moves.add(new Move(board[7][0], 7, 3));
								board[7][0].makeMove(7, 3);

							}
							else if (thisMove.getColFrom()
									- thisMove.getColTo() == -2)
							{
								moves.add(new Move(board[7][7], 7, 5));
								board[7][7].makeMove(7, 5);
							}
						}
						else if (side == 1)
						{
							if (thisMove.getColFrom() - thisMove.getColTo() == 2)
							{
								moves.add(new Move(board[0][0], 0, 3));
								board[0][0].makeMove(0, 3);

							}
							else if (thisMove.getColFrom()
									- thisMove.getColTo() == -2)
							{
								moves.add(new Move(board[0][7], 0, 5));
								board[0][7].makeMove(0, 5);
							}
						}
						selectedPiece.makeMove(pressedRow, pressedCol);
						moves.add(thisMove);

					}

					else
					{
						if (!(board[thisMove.getRowTo()][thisMove.getColTo()] instanceof King))
						{
							selectedPiece.makeMove(pressedRow, pressedCol);
							moves.add(thisMove);
						}
					}

					// Checks for pawn promotion.
					if (selectedPiece instanceof Pawn)
					{
						if (((Pawn) selectedPiece).canPromotePawn())
						{
							if ((selectedPiece.belongsToPlayer(WHITE) && selectedPiece.row == 0)
									|| (selectedPiece.belongsToPlayer(BLACK) && selectedPiece.row == 7))
							{
								String[] promotablePieces = { "Queen",
										"Bishop", "Rook", "Knight" };
								int promotableChoice = JOptionPane
										.showOptionDialog(
												frame,
												"Please choose the piece you wish to promote the pawn to",
												"Pieces to Promote",
												JOptionPane.YES_NO_CANCEL_OPTION,
												JOptionPane.QUESTION_MESSAGE,
												null, promotablePieces,
												promotablePieces[0]);
								System.out.println(promotableChoice);
								if (promotableChoice == 0)
									board[selectedPiece.row][selectedPiece.col] = new Queen(
											selectedPiece.row,
											selectedPiece.col,
											selectedPiece.colour, this);
								else if (promotableChoice == 1)
									board[selectedPiece.row][selectedPiece.col] = new Bishop(
											selectedPiece.row,
											selectedPiece.col,
											selectedPiece.colour, this);
								else if (promotableChoice == 2)
									board[selectedPiece.row][selectedPiece.col] = new Rook(
											selectedPiece.row,
											selectedPiece.col,
											selectedPiece.colour, this);
								else
									board[selectedPiece.row][selectedPiece.col] = new Knight(
											selectedPiece.row,
											selectedPiece.col,
											selectedPiece.colour, this);

							}

						}
					}

					isPieceSelected = false;
					selectedPiece.unselect();
					this.paintImmediately(0, 0, 1024, 768);
					// Switches players after a move has been made.

					// Checks if the player who just made a move checkmated
					// the other player.
					if (!AI)
					{

						currentPlayer *= -1;
						if (noPossibleMoves(currentPlayer))
						{
							if (inCheck(currentPlayer))
							{
								inGame = false;
								if (currentPlayer == BLACK)
									JOptionPane
											.showMessageDialog(
													frame,
													"White has won. Press new game to play again or menu to change settings or exit.");
								else
									JOptionPane
											.showMessageDialog(
													frame,
													"Black has won. Press new game to play again or menu to change settings or exit.");
							}
						}
						else if (inStaleMate())
						{
							inGame = false;
							JOptionPane
									.showMessageDialog(frame,
											"Draw. Press new game to play again or menu to change settings or exit.");
						}
					}

					else
					{
						if (!noPossibleMoves(-side) && !inStaleMate())
						{

							findBestMove();
							// Accounts for castling in the operations.
							bestMove.getMoved().makeMove(bestMove.getRowTo(),
									bestMove.getColTo());
							if (bestMove.getMoved() instanceof King
									&& Math.abs(bestMove.getColTo()
											- bestMove.getColFrom()) == 2)
							{

								Move rookMove;
								if (side == -1)
								{
									if (bestMove.getColTo()
											- bestMove.getColFrom() == 2)
									{

										rookMove = new Move(board[0][7], 0, 5,
												this);
										rookMove.getMoved().makeMove(
												rookMove.getRowTo(),
												rookMove.getColTo());

									}
									else if (bestMove.getColTo()
											- bestMove.getColFrom() == -2)
									{

										rookMove = new Move(board[0][0], 0, 3,
												this);
										rookMove.getMoved().makeMove(
												rookMove.getRowTo(),
												rookMove.getColTo());

									}
								}
								else
								{
									if (bestMove.getColTo()
											- bestMove.getColFrom() == 2)
									{

										rookMove = new Move(board[7][7], 7, 5,
												this);
										rookMove.getMoved().makeMove(
												rookMove.getRowTo(),
												rookMove.getColTo());

									}
									else if (bestMove.getColTo()
											- bestMove.getColFrom() == -2)
									{

										rookMove = new Move(board[7][0], 7, 3,
												this);
										rookMove.getMoved().makeMove(
												rookMove.getRowTo(),
												rookMove.getColTo());

									}

								}

							}
							if (noPossibleMoves(side))
							{
								if (inCheck(side))
								{
									inGame = false;
									if (side == BLACK)
										JOptionPane
												.showMessageDialog(
														frame,
														"White has won. Press new game to play again or menu to change settings or exit.");
									else
										JOptionPane
												.showMessageDialog(
														frame,
														"Black has won. Press new game to play again or menu to change settings or exit.");
								}
							}
							else if (inStaleMate())
							{
								inGame = false;
								JOptionPane
										.showMessageDialog(frame,
												"Draw. Press new game to play again or menu to change settings or exit.");
							}

							bestMove = null;
						}
						else if (noPossibleMoves(-side))
						{
							if (inCheck(-side))
							{
								inGame = false;
								//White got checkmated.
								if (side == BLACK)
									JOptionPane
											.showMessageDialog(
													frame,
													"Black has won. Press new game to play again or menu to change settings or exit.");
								else
									JOptionPane
											.showMessageDialog(
													frame,
													"White has won. Press new game to play again or menu to change settings or exit.");
							}
						}
						else if (inStaleMate())
						{
							inGame = false;
							JOptionPane
									.showMessageDialog(frame,
											"Draw. Press new game to play again or menu to change settings or exit.");

						}
					}
				}
				pieceMoves.clear();
				selectedPiece.unselect();
				isPieceSelected = false;

			}
			else
			// if no piece is currently selected.
			{

				// if no piece is currently selected and the piece belongs
				// to
				// the current player

				if (currentPiece != null
						&& currentPiece.belongsToPlayer(currentPlayer))
				{

					selectedPiece = currentPiece;
					isPieceSelected = true;
					currentPiece.select();
					pieceMoves = selectedPiece.getMoves();
					removeInCheckMoves(pieceMoves);

				}

			}
		}

		// If undo button is clicked.
		else if (!moves.isEmpty() && pressedPoint.x > 935
				&& pressedPoint.x < 1012 && pressedPoint.y > 680
				&& pressedPoint.y < 716)
		{
			if (inGame)
				if (!(AI && moves.size() == 1 && side == BLACK))
				{
					if (AI)
					{
						if (moves.getLast().getMoved() instanceof King)
						{

							if (Math.abs(moves.getLast().getColFrom()
									- moves.getLast().getColTo()) == 2)
							{
								undo();
								pieceMoves.clear();
								selectedPiece.unselect();
								isPieceSelected = false;
							}
						}

						undo();
						currentPlayer *= -1;
						pieceMoves.clear();
						selectedPiece.unselect();
						isPieceSelected = false;
					}
					if (!moves.isEmpty()
							&& moves.getLast().getMoved() instanceof King)
					{

						if (Math.abs(moves.getLast().getColFrom()
								- moves.getLast().getColTo()) == 2)
						{
							undo();
							pieceMoves.clear();
							selectedPiece.unselect();
							isPieceSelected = false;
						}
					}
					undo();
					currentPlayer *= -1;
					pieceMoves.clear();
					selectedPiece.unselect();
					isPieceSelected = false;
				}

		}
		// If snowflake is toggled.
		else if (pressedPoint.x > 895 && pressedPoint.x < 895 + 36
				&& pressedPoint.y > 680 && pressedPoint.y < 716)
		{
			if (snowFlakeOn)
			{
				snowList.clear();
				snowFlakeOn = false;
			}
			else
				snowFlakeOn = true;
			frame.toggleSnow();
		}
		// If undo is clicked.
		else if (!moves.isEmpty() && pressedPoint.x > 725
				&& pressedPoint.x < 725 + 54 && pressedPoint.y > 670 - 4
				&& pressedPoint.y < 670 - 4 + 50)
		{
			newGame();
		}
		else if (soundSelected)
		{
			frame.toggleAllVolume();
			soundOn = !soundOn;

		}
		else if (helpButton.contains(pressedPoint))
		{

			frame.switchTo(frame.HELP, this);
		}
		else if (menuButton.contains(pressedPoint))
		{
			String[] options = { "Yes", "No" };
			int choice = JOptionPane.showOptionDialog(frame,
					"This will end the current game, are you sure?",
					"End Game?", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (choice == 0)

				frame.switchTo(frame.MENU, this);
		}
	}

	public void mouseReleased(MouseEvent arg0)
	{

	}

	public void mouseDragged(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseMoved(MouseEvent event)
	{
		Point mousePoint = event.getPoint();
		int pressedRow = mousePoint.y / SQUARE_LENGTH;
		int pressedCol = mousePoint.x / SQUARE_LENGTH;
		Rectangle helpButton = new Rectangle(935, 680 - 37, 75, 35);
		Rectangle menuButton = new Rectangle(815, 680, 75, 35);
		Rectangle soundButton = new Rectangle(780, 680, 33, 36);
		if (pressedRow >= 0 && pressedRow <= 7 && pressedCol >= 0
				&& pressedCol <= 7)
		{

			if (!isPieceSelected
					&& board[pressedRow][pressedCol] != null
					&& board[pressedRow][pressedCol]
							.belongsToPlayer(currentPlayer))
			{
				clickedEffect = true;
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			else if (isPieceSelected
					&& pieceMoves.contains(new Move(selectedPiece, pressedRow,
							pressedCol)))
			{
				clickedEffect = true;
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			else
			{
				clickedEffect = false;
				setCursor(Cursor.getDefaultCursor());
			}
		}
		else if (!moves.isEmpty() && mousePoint.x > 935 && mousePoint.x < 1012
				&& mousePoint.y > 680 && mousePoint.y < 716)
		{
			clickedEffect = true;
			undoSelected = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

		else if (mousePoint.x > 895 && mousePoint.x < 895 + 36
				&& mousePoint.y > 680 && mousePoint.y < 716)
		{
			clickedEffect = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else if (!moves.isEmpty() && mousePoint.x > 725
				&& mousePoint.x < 725 + 54 && mousePoint.y > 670 - 4
				&& mousePoint.y < 670 - 4 + 50)
		{
			clickedEffect = true;
			newGameSelected = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else if (helpButton.contains(mousePoint))
		{
			clickedEffect = true;
			helpSelected = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else if (menuButton.contains(mousePoint))
		{
			clickedEffect = true;
			menuSelected = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else if (soundButton.contains(mousePoint))
		{
			clickedEffect = true;
			soundSelected = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else

		{
			clickedEffect = false;
			setCursor(Cursor.getDefaultCursor());
			undoSelected = false;
			newGameSelected = false;
			menuSelected = false;
			helpSelected = false;
			soundSelected = false;
		}

	}

	public void mouseClicked(MouseEvent event)
	{

	}

	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

}
