package pl.bartlomiejstepien.carsgame.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pl.bartlomiejstepien.carsgame.Game;

public class Car extends Sprite
{
    private final double startX;
    private final double startY;

    public Car(double x, double y, double velocityX)
    {
        this.startX = x;
        this.startY = y;
        setPosition(x, y);
        width = 60;
        height = 30;
        setVelocity(velocityX, 0);
    }

    @Override
    public void render(GraphicsContext graphicsContext)
    {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.fillRect(x, y, width, height);
    }

    public void restart()
    {
        setPosition(this.startX, this.startY);
        if (this.velocityX < 0)
        {
            setVelocity(-3 + (-(double) Game.RANDOM.nextInt(20) / 10), 0);
        }
        else
        {
            setVelocity(3 + ((double) Game.RANDOM.nextInt(20) / 10), 0);
        }
    }
}
