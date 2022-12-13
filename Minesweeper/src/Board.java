import java.awt.Color;
import java.lang.reflect.Array;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Board extends Application {
	public boolean isBomb = false; //verifying if it's a bomb on an specific button
	public static int[][] buttons; //initializing my mine grid to, in the future, place the buttons (numbers, cover or bombs)
	int countBombs; //counter of bombs
	int bombsInTheGame;
	int timeElapse;
	int height;
	int width;
	faceButton smiley;
	BorderPane border;
	BorderPane header;
	GridPane grid;
	static MineButtons[][] mButtons;
	Label bombsRemaining;
	int counter;
	boolean isFirstClick;
	boolean lostGame;

	private static int y;
	private static int x;

	public static void main(String[] args) {
		launch(args);		
	}

	@Override
	public void start(Stage pStage) {
		countBombs = 10; //counter of bombs
		bombsInTheGame = 10;
		timeElapse = 0;
		height = 8;
		width = 8;
		counter = 0;
		isFirstClick = true;
		lostGame = false;

		buttons = new int[height][width]; //setting size of the grid to place the images/buttons

		smiley = new faceButton();
		smiley.setGraphic(smiley.imageSmile);

		border = new BorderPane();
		header = new BorderPane();

		//printing the Time Elapsed on the right
		Label timeElap = new Label(String.format("%03d",  timeElapse));	
		header.setAlignment(timeElap, Pos.CENTER);
		header.setPadding(new Insets(0, 15, 0, 15));

		//printing the Bombs Remaining on the left
		bombsRemaining = new Label(String.format("%03d",  countBombs)); //how to change here when I change before my lambda after isFlaged
		header.setAlignment(bombsRemaining, Pos.CENTER);
		header.setPadding(new Insets(0, 15, 0, 15));

		header.setLeft(bombsRemaining);
		header.setRight(timeElap);
		header.setCenter(smiley);

		border.setTop(header);

		//BOTTOM
		//Menu Bar		

		//that beautiful border be beautiful
		final String cssDefault = "-fx-border-color: #f6f6f6 #8f8f8f #8f8f8f #f6f6f6;\n"
				+ "-fx-border-width: 5;\n"
				+ "-fx-border-radius: 1;\n"
				+ "-fx-border-insets: -1px;\n"
				+ "-fx-border-background-color: #bdbdbd\n";
		//+ "-fx-border-style: dashed;\n";

		border.setStyle(cssDefault);
		header.setStyle(cssDefault);

		grid = new GridPane();

		//if you click in the smiley face on the header -> it will restart the game and set the smiley face to neutral 
		smiley.setOnMouseClicked( e -> {
			reStart();
			smiley.setGraphic(smiley.imageSmile);
		});
		reStart();

		pStage.setScene(new Scene(border));
		pStage.show();

	}

	//if the game is over (when uncovering everything without hitting any bombs
	public boolean gameWin(MineButtons[][] mButtons) {
		for (int row = 0; row < mButtons.length; row++) {
			for (int col = 0; col < mButtons.length; col++) {
				if (!mButtons[row][col].getBomb() && mButtons[row][col].isCovered() || !mButtons[row][col].getBomb() && mButtons[row][col].isFlagged()) {
					return false;
				}
			}
		}
		return true;
	}
	public void disabling() {
			for (int row = 0; row < mButtons.length; row++) {
				for (int col = 0; col < mButtons.length; col++) {
					mButtons[row][col].isDisabled();
				}
			}
	}
	public void gameOver() {
		if (lostGame) {
			for (int row = 0; row < mButtons.length; row++) {
				for (int col = 0; col < mButtons.length; col++) {
					mButtons[row][col].isDisabled();
				}
			}
		}
	}
	public void dis() {
		for (int row = 0; row < mButtons.length; row++) {
			for (int col = 0; col < mButtons.length; col++) {
				mButtons[row][col].isDisabled();
			}
		}

	}

	//if a bomb is hit -> show all the other bombs in the grid
	public void showRemainingBombs(MineButtons[][] mButtons, MineButtons button) {
		for (int row = 0; row < mButtons.length; row++) {
			for (int col = 0; col < mButtons.length; col++) {
				if(mButtons[row][col].isCovered() && mButtons[row][col].isBomb && !mButtons[row][col].isFlagged()) { //|| mButtons[row][col].isFlag && mButtons[row][col].isBomb
					mButtons[row][col].setGraphic(new ImageView(new Image("file:src/mine-grey.png")));
				} 
				if (!mButtons[row][col].isBomb && mButtons[row][col].isFlagged()) {
					mButtons[row][col].setGraphic(new ImageView(new Image("file:src/mine-misflagged.png")));
				}
			}
		}
	}

	//restarting the game -> putting all the variables to "default" and generating new board
	public void reStart() {

		grid = new GridPane();
		buttons = randomizer();
		countBombs = 10;
		bombsRemaining.setText(String.format("%03d",  countBombs));
		mButtons = new MineButtons[height][width];
		isFirstClick = true;

		for (int row = 0; row < buttons.length; row++) { 
			for (int col = 0; col < buttons.length; col++) {

				int r = row;
				int c = col;

				MineButtons button = new MineButtons(buttons[row][col]);
				mButtons[row][col] = button;
				button.setGraphic(button.imageCover);
				button.setCover(true);
				button.setFlag(false);

				//if the button is a flag -> set to be bomb
				if (button.number == 10) {
					button.setBomb(true); 
				} else {
					button.setBomb(false);
				}

				button.setOnMousePressed( e -> { //setting smiley face in the header to be amused when a click happens
					smiley.setGraphic(smiley.imageO);
				});
				button.setOnMouseReleased ( e -> {
					smiley.setGraphic(smiley.imageSmile);
				});

				button.setOnMouseClicked( e -> {
					MouseButton mouse = e.getButton();					

					//clicking with the left button
					if (mouse == mouse.PRIMARY && !button.isFlagged() && button.isCovered()) { //if it's right click and the button is not flagged
						firstClick(button); //first click is going to be a zero
						openUp(mButtons, buttons, r, c);
						button.setNumber(button.number);
						button.setCover(false);
						counter++;


						if (button.number == 10) { // if hitting a bomb -> smiley face on the header and show all the bombs
							lostGame = true;
							//dis();
							//							smiley.setGraphic(smiley.imageDead);
							//							showRemainingBombs(mButtons, button);

						}


					} else if (mouse == mouse.PRIMARY && !button.isCovered() && !button.isFlagged()) { //opening the numbers touching flag
						flagCursion(button, r, c);
					}

					//clicking with the right button
					if (mouse == mouse.SECONDARY) {
						if (button.isCovered() && !button.isFlagged()) { //if is not flagged then put a flag on in
							button.setGraphic(button.imageFlag);
							button.setFlag(true);
							countBombs--; //placing a flag on the grid and decrementing the count
							bombsRemaining.setText(String.format("%03d",  countBombs));//updating the flag counter in the header
							System.out.println(countBombs);
						} 
						else if (button.isFlagged()) { //if it is flagged then take the flag out
							button.setGraphic(button.imageCover);
							button.setFlag(false);
							countBombs++; //taking a flag out of the grid and decrementing the count
							bombsRemaining.setText(String.format("%03d",  countBombs)); //updating the flag counter in the header
						}
					}
					if (mouse == mouse.PRIMARY && e.getClickCount() == 2) {
						System.out.println("AAAAAAAAAAAAAAAAAAA");
					}

					//if all "not bombs" are uncovered ->  smiley face to the header
					if(gameWin(mButtons)) {
						disabling();
						smiley.setGraphic(smiley.imageBossGlasses);
					}

					if(lostGame) {
						disabling();
						//gameOver();
						
						smiley.setGraphic(smiley.imageDead);
						showRemainingBombs(mButtons, button);
					}

				});
				grid.add(button, col, row);

			}
		}
		border.setCenter(grid);

		//printing to see what's happening
		System.out.println(countBombs);
		for (int a = 0; a < buttons.length; a++) {
			for (int b = 0; b < buttons.length; b++) {
				System.out.printf(" %2d ", buttons[a][b]);
			}
			System.out.println();
		}
	}

	//generating new boards until the first click is a zero
	public void firstClick(MineButtons button) {
		System.out.println(button.number);

		if (button.number != 0 && isFirstClick == true) {
			//randomizer();
			updateButtons(mButtons);
			firstClick(button);	

			//printing to see what's happening
			System.out.println(countBombs);
			for (int a = 0; a < buttons.length; a++) {
				for (int b = 0; b < buttons.length; b++) {
					System.out.printf(" %2d ", buttons[a][b]);
				}
				System.out.println();
			}

		} 
		isFirstClick = false;
	}

	//THAT RECURSION BE WORKING FIIIINE
	public void openUp(MineButtons[][] mButtons, int[][] buttons, int row, int col) {
		if (isValid(row, col, buttons) && !mButtons[row][col].isCovered()) {
			return;
		}

		if(isValid(row, col, buttons) && buttons[row][col] == 0) {

			mButtons[row][col].setCover(false);
			mButtons[row][col].setNumber(mButtons[row][col].number);

			openUp(mButtons, buttons, row-1, col-1);
			openUp(mButtons, buttons, row, col-1);
			openUp(mButtons, buttons, row+1, col-1);
			openUp(mButtons, buttons, row-1, col);
			openUp(mButtons, buttons, row+1, col);
			openUp(mButtons, buttons, row-1, col+1);
			openUp(mButtons, buttons, row, col+1);
			openUp(mButtons, buttons, row+1, col+1);

			mButtons[row][col].setCover(false);

		} else if (isValid(row, col, buttons) && buttons[row][col] != 0 && buttons[row][col] != 10) {
			mButtons[row][col].setCover(false);
			mButtons[row][col].setNumber(mButtons[row][col].number);
		}
	}



	//when clicking an open button and is touching a Flag it should open 
	public void flagCursion(MineButtons button, int row, int col) {

		//counting the flags to use in flgaCursion 
		int flagCount = 0;
		for (int r = row -1; r <= row +1; r++) { 
			for (int c = col -1; c <= col +1; c++) {
				if (isValid(r, c, buttons) && mButtons[r][c].isFlagged()) {
					flagCount++;
				}
			}
		}
		if (button.number == flagCount) {
			for (int r = row -1; r <= row +1; r++) { 
				for (int c = col -1; c <= col +1; c++) {
					if (isValid(r, c, buttons)) {
						openUp(mButtons, buttons, r, c);
					}
				}
			}

		}
	}

	//re setting the buttons to be the new array - after creating a new one until the first click is zero
	public void updateButtons(MineButtons[][] mButtons) {
		randomizer();
		for (int row = 0; row < mButtons.length; row++) { 
			for (int col = 0; col < mButtons[row].length; col++) {
				mButtons[row][col].setNumber(buttons[row][col]);
			}
		}
	}


	// placing the bombs in random spots
	public int[][] randomizer() {
		int row = 0;
		int col = 0;
		buttons = new int[height][width];
		for (int i = 0; i < bombsInTheGame; i++) {
			do {
				row = (int)(Math.random() * height);
				col = (int)(Math.random() * width);
			} while(buttons[row][col] == bombsInTheGame);
			buttons[row][col] = bombsInTheGame;
		}
		numbersOnTheGrid(buttons);
		return buttons;
	}

	//Preventing form going out of bounds
	public static boolean isValid(int r, int c, int[][] buttons) { 
		return r >= 0 && r < buttons.length && c >= 0 && c < buttons[r].length;
	}

	//placing the image of the numbers around the bombs
	public void numbersOnTheGrid(int[][] buttons) { 
		for (int row = 0; row < buttons.length; row++) { 
			for (int col = 0; col < buttons.length; col++) {

				for (int r = row-1; r <= row +1; r++) { 
					for (int c = col-1; c <= col +1; c++) {
						if(isValid(r, c, buttons) && buttons[r][c] == 10 && buttons[row][col] != 10) {
							buttons[row][col] += 1;
						}
					}
				}
			}
		}
	}


}

//setting the images to each box on the grid
class MineButtons extends Button { 
	int state;
	int number;
	boolean covered;
	boolean isBomb;
	boolean isFlag;
	String display;
	ImageView imageCover;
	ImageView imageBombRed;
	ImageView imageFlag;
	ImageView uncovered;

	//BUTTON LAND - WHERE BUTTONS BECOME TRUE
	public MineButtons(int number) { 
		this.number = number;
		double size = 30;
		covered = true;
		isBomb = false;

		setMinWidth(size);
		setMinHeight(size);
		setMaxWidth(size);
		setMaxHeight(size);

		imageCover = new ImageView(new Image("file:src/cover.png"));
		imageCover.setFitHeight(size);
		imageCover.setFitWidth(size);

		imageFlag = new ImageView(new Image("file:src/flag.png"));
		imageFlag.setFitHeight(size);
		imageFlag.setFitWidth(size);

		setNumber(number);

	}

	public void setNumber(int number) { //setting number and image number
		display = number == 10 ? "file:src/mine-red.png" : "file:src/" + number + ".png";
		uncovered = new ImageView(new Image(display));		
		this.number = number;
	}
	public int getNumber() { //getting number
		return this.number;
	}
	public boolean getBomb() { //verifying a bomb
		return this.isBomb;
	}
	public void setBomb(boolean isBomb) { //setting a bomb
		this.isBomb = isBomb;
	}
	public boolean isCovered() { //verifying if is it covered
		return this.covered;
	}
	public void setCover(boolean covered) { //setting cover
		this.covered = covered;
		if (!this.isCovered()) {
			setGraphic(uncovered);
		}
	}	
	public boolean isFlagged() { //verifying if it is a flag
		return this.isFlag;
	}
	public void setFlag(boolean isFlag) { //setting a flag
		this.isFlag = isFlag;
	}
}

//adding yellow faces to the main button at the header
class faceButton extends Button { 
	ImageView imageSmile;
	ImageView imageDead;
	ImageView imageBossGlasses;
	ImageView imageO;

	public faceButton() {
		double size = 40;

		setMinWidth(size);
		setMinHeight(size);
		setMaxWidth(size);
		setMaxHeight(size);

		imageSmile = new ImageView(new Image("file:src/face-smile.png"));
		imageSmile.setFitHeight(size);
		imageSmile.setFitWidth(size);

		imageDead = new ImageView(new Image("file:src/face-dead.png"));
		imageDead.setFitHeight(size);
		imageDead.setFitWidth(size);

		imageBossGlasses = new ImageView(new Image("file:src/face-win.png"));
		imageBossGlasses.setFitHeight(size);
		imageBossGlasses.setFitWidth(size);

		imageO = new ImageView(new Image("file:src/face-O.png"));
		imageO.setFitHeight(size);
		imageO.setFitWidth(size);

		setGraphic(imageSmile);
	}
}