package mines;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MinesControl {
	
	
	@FXML
	private TextArea inputWidth; //user input width of grid
	private int width;
	
	@FXML
	private TextArea inputHeight; //user input height of grid
	private int height;
	
	@FXML
	private TextArea inputMines; //user input number of mines
	private int mines;
	
	@FXML
	private StackPane boardPane; //stackpane to hold the grid of the game
	
	@FXML
	private Pane canvas;// main stage 
	
	private Mines game; //the game itself
	private GridPane board; // gridpane to hold the buttons
	
	//default sizes of the window
	private int defaultWidth = 850; 
	private int defaultHeight = 580;
	
	private Button[][] buttons;//button holder
	
    public void buildBoard() {
		
		board = new GridPane();
		
		buttons = new Button[height][width];
		
		int cellSize = 60;//default button size

		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				Button cell = new Button(".");
				buttons[i][j] = cell;//add the new button to the button holder
				
				//setting the button style
				cell.setPrefSize(cellSize, cellSize);
				cell.setMinSize(cellSize, cellSize);
				cell.setMaxSize(cellSize, cellSize);
				
				cell.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
				cell.setOnMouseClicked(new mouseClick(i,j,cell));//clicking on the button action
				board.add(cell, j, i);
			}
		}
		
		StackPane.setAlignment(board, javafx.geometry.Pos.CENTER);//put the grid in the middle of the stack pane
		
	    
		boardPane.getChildren().clear();  //delete previous board
	    boardPane.getChildren().add(board); //add the new board
	    
	    //the first reset 
	    if (boardPane.getScene() != null && boardPane.getScene().getWindow() != null) {
	        boardPane.getScene().getWindow().sizeToScene();
	    }
	}
	
    //reset button action
    @FXML
	void resetGame(ActionEvent event) {
		
    	//try to get the right input
		try {
			//get the user input
	        width = Integer.parseInt(inputWidth.getText());
	        height = Integer.parseInt(inputHeight.getText());
	        mines = Integer.parseInt(inputMines.getText());
	        int cellSize =60;
	        game = new Mines(height, width, mines);//init game
	        buildBoard();//build board
	        
	        //set the window to match the grid
	        Stage stage = (Stage) canvas.getScene().getWindow();
	        stage.setWidth(width * cellSize + 250);
	        stage.setHeight(Math.max(height * cellSize + 100, 400));
	        
	        //incase the grid is too small for window set default
	        if(width * cellSize + 250 < defaultWidth) stage.setWidth(defaultWidth);
	        if(Math.max(height * cellSize + 100, 400) < defaultHeight) stage.setHeight(defaultHeight);
	        
	        //random mines placement 
	        int countMines=0;
	        while(countMines<mines) {
	        	int mineRow = (int)(Math.random() *height);
	        	int mineCol = (int)(Math.random()*width);
	        	if(game.addMine(mineRow, mineCol)) countMines++; 
	        }
	        
	    
	    //incase of invalid input prompt the user
	    } catch (NumberFormatException e) {
	    	
	    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid input");
            alert.setContentText("try again");
            alert.showAndWait(); 
	    } 
		
	}
    
	//hand mouse click
	class mouseClick implements EventHandler<MouseEvent>{
		
		//save where it was clicked
		private final int row;
	    private final int col;
	    private final Button cell;

	    public mouseClick(int row, int col,Button cell) {
	        this.row = row;
	        this.col = col;
	        this.cell= cell;
	    }

	    @Override
	    public void handle(MouseEvent e) {
	    	//handle left click
	        if (e.getButton() == MouseButton.PRIMARY) {
	        	
	        	//open and check if not a bomb
	            if(!game.open(row, col)) {
	            	//incase it is a bomb
	            	game.setShowAll(true);//reveal bomb locations
	            	Alert alert = new Alert(Alert.AlertType.INFORMATION);
	                alert.setTitle("Game Over");
	                alert.setHeaderText("Loser");
	                alert.setContentText("try again");
	                alert.showAndWait(); 
	               
	                
	            }
	            //update board with neighbors 
	            for(int i=0;i<height;i++) {
	            	for(int j=0;j<width;j++) {
	            		String value = game.get(i, j);//get status of current button
	            		buttons[i][j].setText(value);//set the opened block to the status
	            		
	            		//change style of button text based on status
	            		if ("X".equals(value)) {
	            		    buttons[i][j].setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 20px;");
	            		} else if ("F".equals(value)) {
	            		    buttons[i][j].setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 16px;");
	            		} else {
	            		    buttons[i][j].setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 16px;");
	            		}
	            		 
	            	}
	            }
	            //check if won
	            if(game.isDone()) {
	            	Alert alert = new Alert(Alert.AlertType.INFORMATION);
	                alert.setTitle("Game Over");
	                alert.setHeaderText("OMG!!!!!!!!");
	                alert.setContentText("You won!");
	                alert.showAndWait();  
	            }
	        
	        //right click handle
	        } else if (e.getButton() == MouseButton.SECONDARY) {
	        	
	        	
	            game.toggleFlag(row, col);//change status to flag
	            String value = game.get(row, col);
	            cell.setText(value);
	            
	            //change style of button text based on status
	            if ("F".equals(value)) {
	                cell.setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 16px;");
	            } else {
	                cell.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 16px;");
	            }
	            
	             
	        }
	    }
		
	}
	
	
	
}
