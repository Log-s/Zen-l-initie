package view;

import model.Game;
import model.Pawn;
import model.PawnColor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


/**
 * class that displays the playing board
 * 
 * @author Léo DESMONTS - IUT VANNES - 2020
 * @version 1.0
 */
public class Board extends JPanel {

	private static final long serialVersionUID = 6529685098267757690L;
	private GUISquare selected;
	private final int SIZE;
	private Game game;
	private GUISquare[][] board;

	/**
	 * class constructor that initilizes the boards squares
	 */
	public Board(Game g){

		this.setPreferredSize(new Dimension(600,600));
		this.setBorder(BorderFactory.createLineBorder(Color.decode("#402b10"), 10));
		this.game = g;
		PlayPanel.turn.setText("   "+this.game.getCurrent().getName()+", it's your turn !");
		this.SIZE = this.game.getSize();
		this.board = new GUISquare[this.SIZE][this.SIZE];
		this.setLayout(new GridLayout(this.SIZE, this.SIZE));
		for(int i=0; i<this.SIZE; i++){
			for(int j=0; j<this.SIZE; j++){
				if((j%2==0 && i%2==0) || (j%2!=0 && i%2!=0)){
					this.addSquare(Color.BLACK, j, i);
				}
				else{
					this.addSquare(Color.WHITE, j, i);
				}
			}
		}
		init();
	}

	/**
	 * Adds a square to the board
	 * @param c Color of the square (BLACK or WHITE)
	 * @param x Position X on the board
	 * @param y Position Y on the board
	 */
	private void addSquare(Color c, int x, int y){
		GUISquare square = new GUISquare(c);
		square.addMouseListener(new ListenerCase(square, this, this.game));
		this.add(square);
		this.board[y][x] = square;
	}

	/**
	 * updates the positions of the pawns after a move
	 */
	public void update() {
		for (int j=0 ; j<this.SIZE ; j++) {
			for (int i=0 ; i<this.SIZE ; i++) {
				this.board[j][i].removeAll();
			}
		}
		this.validate();
		init();
		this.revalidate();
    	this.repaint();
	}

	/**
	 * initilizes the square's color
	 */
	private void init(){
		for (Pawn p : this.game.getPawnList()) {
			GUIPawn pion;
			if (p.getColor() == PawnColor.WHITE) {
				pion = new GUIPawn(PawnColor.WHITE);
			}
			else if (p.getColor() == PawnColor.BLACK) {
				pion = new GUIPawn(PawnColor.BLACK);
			}
			else {
				pion = new GUIPawn(PawnColor.ZEN);
			}
			pion.addMouseListener(new ListenerPawn(pion, this, this.game));
			this.board[p.getYPos()][p.getXPos()].add(pion);
		}
	}

	/**
	 * Selects a square (when it's clicked)
	 * @param square square to select
	 */
	public void selectSquare(GUISquare square){
		this.selected = square;
		this.selected.select();
	}

	/**
	 * Selects a square (when the pawn on it is clicked)
	 * @param pawn over the square
	 */
	public void selectSquare(GUIPawn pawn){

		int[] coordinates = getGUIPawnCoordinates(pawn);
		int[] coordinates_bis;

		for (int j=0 ; j<this.SIZE ; j++) {
			for (int i=0 ; i<this.SIZE ; i++) {
				coordinates_bis = getGUISquareCoordinates(this.board[j][i]);
				if (coordinates[0]==coordinates_bis[0] && coordinates[1]==coordinates_bis[1]) {
					this.selected = this.board[j][i];
				}
			}
		}
		this.selected.select();
	}

	/**
	 * Deselects the current selected square
	 */
	public void deselect() {
		this.selected.select();
		this.selected = null;
	}


	/**
	 * Checks if a pawn is selected
	 * @return true is a square is selected, false otherwise
	 */
	public boolean isOneSelected() {
		
		boolean ret = true;
		if (this.selected == null) {
			ret = false;
		}

		return ret;
		
	}

	/**
	 * @return the selected square (null if there is none)
	 */
	public GUISquare getSelected() {
		return this.selected;
	}

	/**
	 * Gets the coordinates of a given square
	 * @param square 
	 * @return a int tab with the coordinates
	 */
	public int[] getGUISquareCoordinates(GUISquare square) {

		int[] ret = {-1, -1};

		for (int j=0 ; j<this.SIZE ; j++) {
			for (int i=0 ; i<this.SIZE ; i++) {
				if (this.board[j][i] == square) {
					ret[0] = i;
					ret[1] = j;
				}
			}
		}

		return ret;
	}

	/**
	 * Gets the coordinates of a given square (by passing the pawn positioned on it)
	 * @param pawn 
	 * @return a int tab with the coordinates
	 */
	public int[] getGUIPawnCoordinates(GUIPawn pawn) {

		int[] ret = {-1, -1};

		for (int j=0 ; j<this.SIZE ; j++) {
			for (int i=0 ; i<this.SIZE ; i++) {
				if (this.board[j][i].getComponents().length == 1 && this.board[j][i].getComponents()[0] == pawn) {
					ret[0] = i;
					ret[1] = j;
				}
			}
		}

		return ret;
	}
}
