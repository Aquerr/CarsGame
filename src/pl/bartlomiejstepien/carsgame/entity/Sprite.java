package pl.bartlomiejstepien.carsgame.entity;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Sprite
{
    protected double width;
    protected double height;

    protected double x;
    protected double y;

    protected double velocityX;
    protected double velocityY;

    public Sprite()
    {

    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void update(double time)
    {
        if(velocityX > 0)
            x += velocityX + time;
        else if (velocityX < 0)
            x += velocityX - time;

        if (velocityY > 0)
            y += velocityY + time;
        else if (velocityY < 0)
            y += velocityY - time;
    }

    public void setPosition(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void addVelocity(double velocityX, double velocityY)
    {
        this.velocityX += velocityX;
        this.velocityY += velocityY;
    }

    public void setVelocity(double velocityX, double velocityY)
    {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(x, y, width, height);
    }

    public boolean intersects(final Sprite sprite)
    {
        return getBoundary().intersects(sprite.getBoundary());
    }

    public abstract void render(GraphicsContext graphicsContext);
}
