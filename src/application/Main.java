package application;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.*;
import javafx.collections.*;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Desktop;
import java.io.*;
import javafx.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.*;
import javafx.scene.image.*;
import javafx.embed.swing.*;
import java.awt.geom.*;
import java.text.*;
import javafx.scene.media.*;
import javax.imageio.ImageIO;

public class Main extends Application {
	String user;
	int gridWidth;
	File file;
	int count;
	boolean timecheck;
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Sliding puzzle");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/512x512bb.jpg")));
			new MediaPlayer(new Media((getClass().getResource("/resources/sounds/Application opens.mp3")).toString())).play();
			init(primaryStage);
		}catch(Exception e) {e.printStackTrace();}
					
					
	}
	
	 private static BufferedImage resize(BufferedImage img, int height, int width) {
	        java.awt.Image tmp = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
	        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2d = resized.createGraphics();
	        g2d.drawImage(tmp, 0, 0, null);
	        g2d.dispose();
	        return resized;
	    }
	private boolean check(Integer[][] arr) {
		int p=1;boolean flag=true;
		for(int i=0;i<arr.length;i++) {
			for(int j=0;j<arr[i].length;j++) {
				if(i==arr.length-1&&j==arr.length-1)
					break;
				else if(p==arr[i][j]) 
					p++;
				else {
					flag=false;
					break;
				}
			}}
			return flag;
		
				}
					
		public void init(Stage primaryStage) {
			
			
			FlowPane root = new FlowPane(10,50);
			Scene scene = new Scene(root,300,200);
			root.setStyle("-fx-background-color: BEIGE");
			root.setAlignment(Pos.CENTER);
			TextField inputuser=new TextField("Enter username");
			Button OK=new Button("OK");
			CheckBox timer=new CheckBox("Countdown mode");
			ComboBox<String> grid=new ComboBox<String>(FXCollections.observableArrayList("2x2","3x3","4x4","5x5","6x6","7x7","8x8","9x9"));
			FileChooser fileChooser=new FileChooser();
			fileChooser.setTitle("Select image");
			Button selimg=new Button("Select image");
			Label filename=new Label("No Image Selected");
			inputuser.setPrefWidth(160);
			grid.setPrefWidth(80);
			filename.setPrefWidth(120);
			selimg.setPrefWidth(100);
			OK.setPrefWidth(50);
			grid.setValue("2x2");
			
			selimg.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					file=fileChooser.showOpenDialog(primaryStage);
					if(file!=null) {
						filename.setText(file.getName());
						
					}
				}
			});
			OK.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					if(timer.isSelected())
						timecheck=true;
					else
						timecheck=false;
					user=inputuser.getText();
					gridWidth=grid.getValue().charAt(0)-'0';
					puzzle(primaryStage);
				}
			});
root.getChildren().addAll(inputuser,grid,filename,selimg,OK,timer);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		public void puzzle(Stage primaryStage) {
			try {
			Media tileslide=new Media(getClass().getResource("/resources/sounds/Tile slides sound.mp3").toString());	
			Media lose=new Media(getClass().getResource("/resources/sounds/Lose Give up.mp3").toString());
			Media hinthaha=new Media(getClass().getResource("/resources/sounds/Hint Haha.mp3").toString());
			Media timelow=new Media(getClass().getResource("/resources/sounds/Time low Ding.mp3").toString());
			Media winner=new Media(getClass().getResource("/resources/sounds/Win Victory.mp3").toString());
			ImageView loserduck = new ImageView(new Image(getClass().getResource("/resources/images/duck gif.gif").toExternalForm()));
			BufferedImage imgs[][]=new BufferedImage[gridWidth][gridWidth];
			BufferedImage image=null;
			try {
			if(file!=null) {
				FileInputStream fis=new FileInputStream(file);
				image=ImageIO.read(fis);
			
				int chunkWidth=image.getWidth()/gridWidth;
				int chunkHeight=image.getHeight()/gridWidth;
				int p=0,q=0;
				
				for(int x=0;x<gridWidth;x++) {
					q=0;
					for(int y=0;y<gridWidth;y++) {
						
						imgs[x][y]=image.getSubimage(q, p, chunkWidth, chunkHeight);
						q+=chunkWidth;
					}
					p+=chunkHeight;
				}
				
				
			}
			}catch(Exception e2) {
				System.err.print("Error"+e2);
			}
			BorderPane root1=new BorderPane();
			root1.setStyle("-fx-background-color :honeydew");
			Label playername=new Label(user);
			playername.setFont(new Font(20));
			playername.setStyle("-fx-text-fill: black");
			root1.setTop(playername);
			BorderPane.setAlignment(playername, Pos.CENTER);
			FlowPane field=new FlowPane();
		
			Integer arr[][]=Generator.generate(gridWidth); 
			field.setAlignment(Pos.CENTER);
			Button[][] tile=new Button[gridWidth][gridWidth];
			for(int i=0;i<gridWidth;i++) {
				for(int j=0;j<gridWidth;j++) {
					if(file==null) {
					if(arr[i][j]!=0)
						tile[i][j]=new Button(arr[i][j].toString());
					else
						tile[i][j]=new Button("");
					tile[i][j].setMinSize(100, 100);
					tile[i][j].setMaxSize(100, 100);
					tile[i][j].setStyle(" -fx-background-color: #090a0c,linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-background-radius: 5,4,3,5;-fx-background-insets: 0,1,2,0;-fx-text-fill: white; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );-fx-font-family: 'Paladino';-fx-text-fill: linear-gradient(white, #d0d0d0);-fx-font-size: 32px;-fx-padding: 10 20 10 20;");
					}
					else {
						if(arr[i][j]!=0) {
							BufferedImage temp=null;
							int count=1;
							for(int x=0;x<gridWidth;x++)
								for(int y=0;y<gridWidth;y++)
									if(count++==arr[i][j])
										temp=imgs[x][y];
							tile[i][j]=new Button(arr[i][j].toString(),new ImageView(SwingFXUtils.toFXImage(resize(temp,100,100), null)));
							
							tile[i][j].setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
						}
						else {
							tile[i][j]=new Button("");tile[i][j].setContentDisplay(ContentDisplay.GRAPHIC_ONLY);}
						tile[i][j].setMinSize(100, 100);
						tile[i][j].setMaxSize(100, 100);
					}
					
					tile[i][j].setFont(new Font(20));
					if(arr[i][j]==0)tile[i][j].setDisable(true);
						
				}
			}
			
			count=0;
			Label counter=new Label("Moves: 0");
			long timeNow;
			if(timecheck)
			timeNow=gridWidth*60*1000+System.currentTimeMillis();
			else
				timeNow=System.currentTimeMillis();
			Label timeLabel=new Label();
			DateFormat timeFormat=new SimpleDateFormat("HH:mm:ss");
			timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			final Timeline timeline;
			timeline=new Timeline(
					new KeyFrame(
							Duration.millis(500),event->{
								final long time=Math.abs(timeNow-System.currentTimeMillis());
								
								
								
								timeLabel.setText(timeFormat.format(time));
								
								
								try {
								if(timecheck) {
									
									if((timeFormat.parse(timeLabel.getText()).getTime())==(gridWidth*60*1000)/2) {
										new MediaPlayer(timelow).play();
										timeLabel.setFont(new Font(15));
										timeLabel.setStyle("-fx-font-weight:bold;-fx-text-fill: red;");
									}
									if(timeFormat.parse(timeLabel.getText()).getTime()==0) {
										new MediaPlayer(lose).play();
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Information Dialog");
										alert.setHeaderText(null);
										alert.setContentText("You LOSE!");
					                    alert.setGraphic(loserduck);
										alert.show();
										init(primaryStage);
										
									}
								}
								}catch(Exception e1) {System.err.println(e1);}
								
								
							}
							));
			timeline.setCycleCount(Animation.INDEFINITE);
			timeline.play();
		
			
			EventHandler<ActionEvent> MEHandler=new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e1) {
					new MediaPlayer(tileslide).play();
					
					Integer num=Integer.valueOf(((Button)e1.getSource()).getText());
					
					int i=0,j=0;
					for(int p=0;p<gridWidth;p++) {
						for(int q=0;q<gridWidth;q++) {
							if(arr[p][q]!=0)tile[p][q].setDisable(false);else tile[p][q].setDisable(true);
							if(arr[p][q]==num)
							{
								i=p;
								j=q;
							}
						}
					}
					if(((i>0&&arr[i-1][j]==0)||(j>0&&arr[i][j-1]==0)||(i<gridWidth-1&&arr[i+1][j]==0)||(j<gridWidth-1&&arr[i][j+1]==0))) {
						
						count++;
					}
					if(i>0&&arr[i-1][j]==0) {
						
						
						if(file!=null) {
							tile[i-1][j].setGraphic(tile[i][j].getGraphic());
							tile[i][j].setGraphic(null);
						}
						
						tile[i-1][j].setText(tile[i][j].getText());
						tile[i][j].setText("");
						
						arr[i-1][j]=arr[i][j];
						arr[i][j]=0;
						tile[i][j].setDisable(true);
						tile[i-1][j].setDisable(false);
						
					}
					else if(j>0&&arr[i][j-1]==0) {
						
						
						if(file!=null) {
							tile[i][j-1].setGraphic(tile[i][j].getGraphic());
							tile[i][j].setGraphic(null);								}
						
						tile[i][j-1].setText(tile[i][j].getText());
						tile[i][j].setText("");
						
						arr[i][j-1]=arr[i][j];
						arr[i][j]=0;
						tile[i][j].setDisable(true);
						tile[i][j-1].setDisable(false);
						
					}
					if(i<gridWidth-1&&arr[i+1][j]==0) {
						
					
						if(file!=null) {
							tile[i+1][j].setGraphic(tile[i][j].getGraphic());
							tile[i][j].setGraphic(null);								}
						
						tile[i+1][j].setText(tile[i][j].getText());
						tile[i][j].setText("");
						
						arr[i+1][j]=arr[i][j];
						arr[i][j]=0;
						tile[i][j].setDisable(true);
						tile[i+1][j].setDisable(false);
						
					}
					else if(j<gridWidth-1&&arr[i][j+1]==0) {
						
						
						if(file!=null) {
							tile[i][j+1].setGraphic(tile[i][j].getGraphic());
							tile[i][j].setGraphic(null);								}
						
						tile[i][j+1].setText(tile[i][j].getText());
						tile[i][j].setText("");
						
						arr[i][j+1]=arr[i][j];
						arr[i][j]=0;
						tile[i][j].setDisable(true);
						tile[i][j+1].setDisable(false);
					}
					
					counter.setText("Moves: "+count);
					if(check(arr)) {
						playername.setText(user+" wins!!!");
						new MediaPlayer(winner).play();
						if(file==null)
							tile[gridWidth-1][gridWidth-1].setText(Integer.toString(gridWidth*gridWidth));
						else
							tile[gridWidth-1][gridWidth-1].setGraphic(new ImageView(SwingFXUtils.toFXImage(resize(imgs[gridWidth-1][gridWidth-1],100,100), null)));
						for(i=0;i<gridWidth;i++)
							for(j=0;j<gridWidth;j++)
								tile[i][j].setDisable(true);
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Dialog");
						alert.setHeaderText(null);
						alert.setContentText(user+" WINS!");
	                    
						alert.showAndWait();
						timeline.stop();
						try{init(primaryStage);}catch(Exception e2) {}
						
					}
				}
			};
			for(int i=0;i<gridWidth;i++) {
				for(int j=0;j<gridWidth;j++) {
					tile[i][j].setOnAction(MEHandler);
					field.getChildren().add(tile[i][j]);
				}
			}
			FlowPane footbar=new FlowPane(50,10);
			footbar.getChildren().addAll(counter,timeLabel);
			Button gup=new Button("Give up :(");
			gup.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					new MediaPlayer(lose).play();
					timeline.stop();
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText(null);
					alert.setContentText("You LOSE!");
                    alert.setGraphic(loserduck);
					alert.showAndWait();
					try{init(primaryStage);}catch(Exception e2) {System.err.println(e2);}
					
				}
			});
			Button hint=new Button("Hint");
			if(file==null)
				hint.setDisable(true);
			Stage popup=new Stage();
			FlowPane root=new FlowPane();
			if(file!=null)
			root.getChildren().add(new ImageView(SwingFXUtils.toFXImage(resize(image,400,400), null)));
			
			Scene scene=new Scene(root,400,400)	;
			popup.setScene(scene);
			popup.initModality(Modality.APPLICATION_MODAL);
			popup.setTitle("Original image");
			hint.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					new MediaPlayer(hinthaha).play();
					popup.show();
					}
				
			});
		
			root1.setLeft(hint);
			root1.setRight(gup);
			BorderPane.setAlignment(gup, Pos.CENTER);
			BorderPane.setAlignment(hint, Pos.CENTER);
			field.setMaxSize(gridWidth*100, gridWidth*100);
			root1.setCenter(field);
			timeLabel.setStyle("-fx-text-fill: black");
			counter .setStyle("-fx-text-fill: magenta");
			root1.setBottom(footbar);
			Scene scene1=new Scene(root1,gridWidth*100+200,gridWidth*100+200);
			primaryStage.setScene(scene1);
			primaryStage.show();
			
		
	
	
	
} catch(Exception e) {
	e.printStackTrace();
}
		}
			
	
	public static void main(String[] args) {
		launch(args);
	}
}
