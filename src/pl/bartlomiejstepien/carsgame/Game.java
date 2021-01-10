package pl.bartlomiejstepien.carsgame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import pl.bartlomiejstepien.carsgame.entity.Car;
import pl.bartlomiejstepien.carsgame.entity.Player;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class Game extends Application
{
    private final static int TARGET_FPS = 60;
    private final static int OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;

    private MediaPlayer mediaPlayer;

    public static Random RANDOM = new Random();

    private Group rootGroup;
    private Scene scene;
    private Canvas canvas;
    private Canvas backgroundCanvas;

    private Rectangle2D finishLine;

    private List<String> pressedKeys;

    private Player player;
    private int pointCounter;

    private Label pointCounterLabel;

//    private Group carGroup;

    private List<Car> cars;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        this.rootGroup = new Group();
        this.scene = new Scene(rootGroup, 800, 500, Color.BLACK);
        this.canvas = new Canvas(this.scene.getWidth(), this.scene.getHeight());
        this.backgroundCanvas = new Canvas(this.scene.getWidth(), this.scene.getHeight());
        this.finishLine = new Rectangle2D(0, 0, this.scene.getWidth(), 90);
        this.pointCounterLabel = new Label("Points: 0");
        this.pointCounterLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 20));
        this.pointCounterLabel.setLayoutX(50);
        this.pointCounterLabel.setLayoutY(30);

        this.rootGroup.getChildren().add(backgroundCanvas);
        this.rootGroup.getChildren().add(this.pointCounterLabel);
        this.rootGroup.getChildren().add(canvas);

        this.player = new Player(400, 450);

//        this.carGroup = new Group();

        this.cars = new ArrayList<>(5);

        //rand.nextInt((max - min) + 1) + min;

        Car car1 = new Car(-50, 110, 3 + ((double) RANDOM.nextInt(20) / 10));
        Car car2 = new Car(-50, 210, 3 + ((double) RANDOM.nextInt(20) / 10));
        Car car3 = new Car(-50, 260, 3 + ((double) RANDOM.nextInt(20) / 10));
        Car backwardsCar1 = new Car(this.canvas.getWidth(), 160, -3 + (-(double) RANDOM.nextInt(20) / 10));
        Car backwardsCar2 = new Car(this.canvas.getWidth(), 310, -3 + (-(double) RANDOM.nextInt(20) / 10));
        Car backwardsCar3 = new Car(this.canvas.getWidth(), 360,-3 + (-(double) RANDOM.nextInt(20) / 10));

        this.cars.add(car1);
        this.cars.add(car2);
        this.cars.add(car3);
        this.cars.add(backwardsCar1);
        this.cars.add(backwardsCar2);
        this.cars.add(backwardsCar3);

        drawRoadWays();

        this.pressedKeys = new ArrayList<>();

        scene.setOnKeyPressed(event ->
        {
            final String keyCode = event.getCode().toString();
            if (!pressedKeys.contains(keyCode))
                pressedKeys.add(keyCode);
        });
        scene.setOnKeyReleased(event ->
        {
            final String keyCode = event.getCode().toString();
            pressedKeys.remove(keyCode);
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Cars Game");
        primaryStage.show();
        playBackgroundMusic();

        AnimationTimer animationTimer = new AnimationTimer()
        {
            private long lastTime = System.nanoTime();
            private long lastFrameTime = 0;
            private long frameNumber = 0;

            @Override
            public void handle(long now)
            {
                double updateLength = (now - lastTime);
                System.out.println(updateLength / 1e6);
                lastTime = now;
                frameNumber++;

                double delta = updateLength / ((double)OPTIMAL_TIME);
                update(delta);

//                lastFrameTime += updateLength;
//                if(lastFrameTime >= 1_000_000_000)
//                {
//                    lastFrameTime = 0;
//                }
//
//                System.out.println("FPS: " + delta);
//
//                if(delta >= 1)
//                {
//                    update(delta);
//                }




//                update(frameTime);

                render();
            }
        };
        animationTimer.start();

    }

    private void playerAndCarCollision()
    {
        final Iterator<Car> iterator = this.cars.iterator();
        while (iterator.hasNext())
        {
            final Car car = iterator.next();
            if (player.intersects(car))
                restartPlayer();
            if (!car.getBoundary().intersects(0, 0, canvas.getWidth(), canvas.getHeight()))
            {
                car.restart();
            }
        }
    }

    private void restartPlayer()
    {
        this.player.setPosition(400, 450);
    }

    private void playerCanvasCollision()
    {
        if (player.getBoundary().intersects(finishLine))
        {
            System.out.println("Points: " + ++pointCounter);
            this.pointCounterLabel.setText("Points: " + pointCounter);
            restartPlayer();
        }

        if (canvas.getHeight() < player.getY())
        {
            player.setPosition(player.getX(), canvas.getHeight());
        }
        if(0 > player.getY())
        {
            player.setPosition(player.getX(), 0);
        }
        if(canvas.getWidth() < player.getX())
        {
            player.setPosition(canvas.getWidth(), player.getY());
        }
        if(0 > player.getX())
        {
            player.setPosition(0, player.getY());
        }
    }

    private void drawRoadWays()
    {
        final int size = this.cars.size() + 1;

        int position = 100;
        GraphicsContext graphicsContext = this.backgroundCanvas.getGraphicsContext2D();
        graphicsContext.setLineWidth(1);
        for (int i = 0; i < size; i++)
        {
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.fillRect(0, position, this.backgroundCanvas.getWidth(), 1);
            position += 50;
        }
        this.backgroundCanvas.setEffect(new GaussianBlur(5));
    }

    private void playBackgroundMusic()
    {
        final URL url = Game.class.getClassLoader().getResource("Voice_Over_Under.mp3");
        final Media media = new Media(url.toString());
        this.mediaPlayer = new MediaPlayer(media);
        this.mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
    }

    private void update(double delta)
    {
//        doubleProperty.set(doubleProperty.get() - 1);

        // Game Logic
        player.setVelocity(0, 0);
        if (pressedKeys.contains("LEFT"))
            player.addVelocity(-1.2, 0);
        if(pressedKeys.contains("RIGHT"))
            player.addVelocity(1.2, 0);
        if(pressedKeys.contains("UP"))
            player.addVelocity(0, -1.2);
        if(pressedKeys.contains("DOWN"))
            player.addVelocity(0, 1.2);

        //Update
        player.update(delta);
        for (final Car car: cars)
        {
            car.update(delta);
        }

        //Collision
        playerCanvasCollision();
        playerAndCarCollision();
    }

    private void render()
    {
        //Render
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, scene.getWidth(), scene.getHeight());

        player.render(graphicsContext);

        for (final Car car : cars)
        {
            car.render(graphicsContext);
        }
    }
}
