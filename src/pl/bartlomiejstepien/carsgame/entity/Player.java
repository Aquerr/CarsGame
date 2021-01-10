package pl.bartlomiejstepien.carsgame.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player extends Sprite
{
    public Player(double x, double y)
    {
        setPosition(x, y);
        width = 15;
        height = 15;
    }

    @Override
    public void render(GraphicsContext graphicsContext)
    {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.fillRect(x, y, width, height);
    }
}
