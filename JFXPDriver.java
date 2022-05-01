/*
 * @NAME JavaFX Paint
 * @AUTHOR Adam Spera
 * @AUTHOR Brey Rivera
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JFXPDriver extends Application {

  // create main pane
  Pane p = new Pane();
  // create menu options
  Button draw = new Button("Draw");
  Button erase = new Button("Erase");
  ObservableList<String> options = 
    FXCollections.observableArrayList("Size 3", "Size 6", "Size 9");
  ComboBox<String> comboBox = new ComboBox<String>(options);
  Button bucket = new Button("Bucket");
  ColorPicker colorPicker = new ColorPicker(Color.BLACK);
  Color currColor = Color.BLACK;
  Text coords = new Text();
  boolean drawing = true;
  int size = 3;

  public void start(Stage primaryStage) {
    BorderPane pane = new BorderPane();
    // set box zone background style color
    pane.setStyle("-fx-background-color: gray");
    // create option and coordinate boxes
    pane.setTop(createTopRow());
    pane.setBottom(createBottomRow());
    // set pane background style color
    p.setStyle("-fx-background-color: white;");
    pane.setMinSize(0, 0);
    pane.setCenter(p);
    // create the main scene with details
    Scene s = new Scene(pane, 800, 590);
    primaryStage.setTitle("JavaFX Paint");
    primaryStage.setScene(s);
    primaryStage.show();

    // draw button is clicked
    draw.setOnAction(e -> {
      // enable drawing
      drawing = true;
    });

    // erase button is clicked
    erase.setOnAction(e -> {
      // disable drawing
      drawing = false;
    });

    // set default display of combo box
    comboBox.setValue("Draw Size");
    // combo box changed
    comboBox.setOnAction(e -> {
      // update size by combo box
      if (comboBox.getValue().toString().equals("Size 3")) {size = 3;};
      if (comboBox.getValue().toString().equals("Size 6")) {size = 6;};
      if (comboBox.getValue().toString().equals("Size 9")) {size = 9;};
    });

    // mouse is clicked
    p.setOnMouseClicked(e -> { draw(e); });
    
    // mouse is dragged
    p.setOnMouseDragged(e -> { draw(e); });

    // mouse is moved
    p.setOnMouseMoved(e -> {
      updateCoordinates(e.getSceneX(), e.getSceneY());
    });

    // bucket is clicked
    bucket.setOnAction(e -> {
      // remove all objects
      p.getChildren().clear();
      // update pane style color
      String bucketColor = colorPicker.getValue().toString();
      p.setStyle("-fx-background-color: " + bucketColor.replace("0x", "#") + ";");
    });

    // change cursor icon in pane
    p.setOnMouseEntered(e -> s.setCursor(Cursor.CROSSHAIR));
    p.setOnMouseExited(e -> s.setCursor(Cursor.DEFAULT));

    // color is selected
    colorPicker.setOnAction(e -> currColor = colorPicker.getValue());
    
    // on pane resize change
    p.widthProperty().addListener((obs, oldVal, newVal) -> { updateCanvas(); });
	p.heightProperty().addListener((obs, oldVal, newVal) -> { updateCanvas(); });
    
  }
  
  // draw rectangle on pane
  public void draw (MouseEvent e) {
	// if mouse is above bottom bar and below top bar and within right side and left side
	if (e.getY() < p.getHeight() - size && e.getY() > 0 && e.getX() < p.getWidth() && e.getX() > 0) {
	  Rectangle shape = new Rectangle();
	  // set new shape coordinates
	  shape.setX(e.getX());
	  shape.setY(e.getY());
	  // set new shape size
	  shape.setWidth(size);
	  shape.setHeight(size);
	  // set new shape color
	  shape.setFill(drawing ? currColor : Color.WHITE);
	  // add new shape to pane
	  p.getChildren().addAll(shape);
	  // update current mouse coordinates
	  updateCoordinates(e.getSceneX(), e.getSceneY());
	} else {
	  System.out.println("Draw Out of Bounds");
	}
  }
  
  // update coordinates in bottom box
  public void updateCoordinates (double x, double y) {
	  DecimalFormat df = new DecimalFormat("#.00");
	  String newX = df.format(x);
	  String newY  = df.format(y - 51);
	  // set coordinates with formatting
	  coords.setText(newX + ", " + newY + "px");
  }
  
  public void updateCanvas () {
	// recycled node list for out of bound nodes
	ArrayList<Node> tempList = new ArrayList<Node>();
	// for each child on pane
	for (Node tempNode : p.getChildren()) {
	    if (((Rectangle) tempNode).getX() > p.getWidth() || ((Rectangle) tempNode).getY() > p.getHeight() - 10) { 
		  // add node to list
		  tempList.add(tempNode);
	    } // end of out of bounds
	} // end of for each
	// remove all out of bound nodes
	p.getChildren().removeAll(tempList);
  }

  // launch program
  public static void main(String[] args) {
    launch(args);
  }

  // create top row box
  private HBox createTopRow() {
    HBox box = new HBox(10);
    box.setPadding(new Insets(15, 15, 15, 15));
    // set box background style color
    box.setStyle("-fx-padding: 10;" + "-fx-background-color: lightgray;");
    // add all option assets to the menu box
    box.getChildren().addAll(draw, erase, colorPicker, comboBox, bucket);
    return box;
  }

  // create bottom row box
  private HBox createBottomRow() {
    HBox box = new HBox(10);
    box.setPadding(new Insets(15, 15, 15, 15));
    // set box background style color
    box.setStyle("-fx-padding: 10;" + "-fx-background-color: lightgray;");
    // add coordinates to box
    box.getChildren().add(coords);
    return box;
  }

}