package com.example.spaceinvaders;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SpaceInvApplication extends Application {
	
	private Pane root = new Pane();
	private GameObj player = new GameObj(300, 750, 40, 40, "player", "/com/example/spaceInvaders/spaceship_6542648.png");
	private double t = 0;
	private int deadAliensCount = 0;
	private Label deadAliensLabel = new Label("Dead Aliens : 0");
	private Label currentLevelLabel = new Label("Current Level : 1");
	private int currentLevel = 0;
	private Label gameStartLabel = new Label("Start Game !");
	private Label gameEndLabel = new Label("Game Over !");
	private boolean gameRunning = true;
	
	
	private Parent createContent(){
		root.setPrefSize(600, 800);
		
		// Create an ImageView for the background
		ImageView background = new ImageView(new Image(getClass().getResource("/com/example/spaceInvaders/starry_sky_0508.jpg").toExternalForm()));
		background.setFitWidth(600);
		background.setFitHeight(800);
		
		// Add the background to the root pane first, so it is behind other objects
		root.getChildren().add(background);
		
		// Set up the label for the dead alien count
		deadAliensLabel.setTranslateX(10); // Position the label
		deadAliensLabel.setTranslateY(10);
		deadAliensLabel.setStyle("-fx-font-size: 20;  -fx-font-weight: bold; -fx-text-fill: #f1cff8;");
		
		// set up the label for level
		currentLevelLabel.setTranslateX(500); //positioned on the right side
		currentLevelLabel.setTranslateY(10);
		currentLevelLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;-fx-text-fill: #f1cff8;");
		
		gameEndLabel.setTranslateX(170);
		gameEndLabel.setTranslateY(350);
		gameEndLabel.setStyle("-fx-font-size: 40;-fx-font-weight: bold;  -fx-text-fill: #d71347;");
		gameEndLabel.setVisible(false);
		
		gameStartLabel.setTranslateX(170);
		gameStartLabel.setTranslateY(350);
		gameStartLabel.setStyle("-fx-font-size: 40; -fx-font-weight: bold; -fx-text-fill: #71ee0f;");
		
		root.getChildren().addAll(deadAliensLabel,currentLevelLabel,player, gameStartLabel,gameEndLabel); //player on screen
		
		//smoother movement for player
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
				//update player position based on key pressed
				if(movingLeft){
					player.moveLeft();
				}
				if(movingRight){
					player.moveRight();
				}
			}
		};
		timer.start();
		
		//wait for the message game starts to display and then start the level
		
		new Thread(()->{
			try {
				Thread.sleep(2000); //wait 2 seconds
				javafx.application.Platform.runLater(() -> gameStartLabel.setVisible(false)); // Hide the label on the JavaFX thread
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}).start();
		
		return root;
	}
	
	private List<GameObj> objects(){
		return root.getChildren().stream()
		.filter(n-> n instanceof GameObj) //filter label to avoid errors
		.map(n->(GameObj)n)
		.collect(Collectors.toList());
	}
	private void update() {
		t += 0.020;
		List<GameObj> newAliens = new ArrayList<>(); // List to hold new aliens to be added
		
		objects().forEach(s -> {
			switch (s.type) {
				case "enemybullet":
					s.moveDown();
					if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
						player.dead = true;
						s.dead = true;
						gameRunning = false; //when the player is hit, stop the game
						gameEndLabel.setVisible(true); //display game over
					}
					break;
				
				case "playerbullet":
					s.moveUp(); // Add movement logic for player bullets.
					objects().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
						if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
							enemy.dead = true;
							s.dead = true; // Mark both the bullet and enemy as dead on hit.
							
							//increment and update the dead alien counter
							deadAliensCount++;
							deadAliensLabel.setText("Dead Aliens: " + deadAliensCount);
						}
					});
					break;
				
				case "enemy":
					if (t > 2) {
						if (Math.random() < 0.6) { // the frequency an enemy will shoot
							shoot(s);
						}
					}
					break;
			}
		});
		
		root.getChildren().removeIf(n -> (n instanceof GameObj) && ((GameObj) n).dead);
		
		//if all enemies are dead, start next level
		
		if(objects().stream().noneMatch(e -> e.type.equals("enemy"))){
			nextLevel();
		}
		
		if (t > 2) {
			t = 0;
		}
	}
	
	private void nextLevel() {
			currentLevel++;
			currentLevelLabel.setText("Level : " + currentLevel);
			
			int baseEnemyCount = 5; //initialize no of aliens in the 1st level
			int totalEnemies = baseEnemyCount + (currentLevel -1 )* 2; //for each level increment by 2
		
			for(int i = 0; i < totalEnemies; i++){
				
				int xPosition = 90 + (i % 5) * 100; //x position so enemies are spaced evenly
				int yPosition = 90 + (i / 5) * 40; // new row every 5 enemies
				
				GameObj s = new GameObj(xPosition,yPosition,30,30, "enemy", "/com/example/spaceInvaders/ufo_1794534.png");
				root.getChildren().add(s);
			}
	}
	
	
	public void shoot(GameObj who) {
		GameObj s = new GameObj((int) who.getTranslateX() + 20, (int) who.getTranslateY(), 40,40, who.type + "bullet","/com/example/spaceInvaders/bullet_17044062.png" );
		
		root.getChildren().add(s);
	}
	
	private boolean movingLeft = false;
	private boolean movingRight = false;
	
	@Override
	public void start(Stage stage) throws IOException {
		
		Scene scene = new Scene(createContent());
		stage.setTitle("Space Invaders Game");
		
		//move the player
		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
				case A:
					movingLeft = true;
					break;
				case D:
					movingRight = true;
					break;
					
				case SPACE:
					shoot(player);
					break;
			}
		});
		
		scene.setOnKeyReleased(e ->{
			switch (e.getCode()) {
				case A :
					movingLeft = false;
					break;
				case D:
					movingRight = false;
					break;
			}
		});
		
		stage.setScene(scene);
		stage.show();
	}
	
	public static class GameObj extends ImageView {
		boolean dead = false;
		final String type;
		double delayTimer = -1; // -1 indicates no active delay
		
		GameObj(int x, int y, int w, int h,String type, String imagePath){
			if (imagePath != null && !imagePath.isEmpty()) {
				Image image = new Image(getClass().getResource(imagePath).toExternalForm());
				setImage(image);
			}
			
			this.type = type;
			
			setFitWidth(40);
			setFitHeight(40);
			
			setTranslateX(x);
			setTranslateY(y);
			
			if (type.contains("bullet")) {
				setFitWidth(10);  //  width for bullets
				setFitHeight(20); //  height for bullets
			}
			
		}
		
		void moveLeft(){
			setTranslateX(getTranslateX()- 10); //moves the player to the left by 10 units per key press
		}
		void moveRight(){
			setTranslateX(getTranslateX()+ 10);
		}
		
		void moveUp(){
			setTranslateY(getTranslateY() - 5);
		}
		
		void moveDown(){
			setTranslateY(getTranslateY() + 5);
		}
	}
	
	public static void main(String[] args) {
		launch();
	}
}