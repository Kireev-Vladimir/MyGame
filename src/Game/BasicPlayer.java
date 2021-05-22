package Game;

import Game.Guns.Gun;
import org.newdawn.slick.Graphics;

import java.awt.*;

enum PlayerState{
    runLeft, runRight, stop;
}

public class BasicPlayer extends Entity implements Runnable{
    double hp;
    float armor;
    Team team;
    PlayerState state;
    double maxSpeed;
    double jumpStrength;
    boolean standing;
    double speedX;
    double speedY;
    double accelerationX;
    long lastTime;
    boolean alive;
    Point mousePos;
    Point crosshair;

    boolean isCrouched;

    int money;
    String name;

    Gun[] guns = new Gun[4];
    int gunNum;
    Thread[] gunThreads = new Thread[4];

    public BasicPlayer(){
        super();
        state = PlayerState.stop;
        maxSpeed = 0.15f;
        jumpStrength = 0.5f;
        standing = true;
        speedX = 0.1f;
        speedY = 0;
        accelerationX = 0.0005f;
        alive = true;
        mousePos = new Point();
        crosshair = new Point();
        hp = 100;
        armor = 0;
    }

    protected void mainLoop(){
        lastTime = System.nanoTime();
        while(alive) {
                long curTime = System.nanoTime();
                double timeDiff = (double) (curTime - lastTime) / 1000000;
                lastTime = curTime;
                double moveX = 0;
                double moveY = 0;
                synchronized (this) {
                    switch (state) {
                        case runLeft -> {
                            double newSpeedX = speedX - timeDiff * accelerationX;
                            if (newSpeedX < -maxSpeed) {
                                newSpeedX = -maxSpeed;
                            }
                            if(speedX == -maxSpeed)
                                moveX = speedX * timeDiff;
                            else
                                moveX = speedX * timeDiff - accelerationX * timeDiff * timeDiff / 2;
                            speedX = newSpeedX;
                        }
                        case runRight -> {
                            double newSpeedX = speedX + timeDiff * accelerationX;
                            if (newSpeedX > maxSpeed) {
                                newSpeedX = maxSpeed;
                            }
                            if(speedX == maxSpeed)
                                moveX = speedX * timeDiff;
                            else
                                moveX = speedX * timeDiff + accelerationX * timeDiff * timeDiff / 2;
                            speedX = newSpeedX;
                        }
                        case stop -> {
                            if (speedX > 0) {
                                double newSpeedX = speedX - timeDiff * accelerationX * 4;
                                if (newSpeedX < 0) {
                                    newSpeedX = 0;
                                }
                                moveX = speedX * timeDiff - accelerationX * timeDiff * timeDiff * 2;
                                speedX = newSpeedX;
                            } else if (speedX < 0) {
                                double newSpeedX = speedX + timeDiff * accelerationX * 4;
                                if (newSpeedX > 0) {
                                    newSpeedX = 0;
                                }
                                moveX = speedX * timeDiff + accelerationX * timeDiff * timeDiff * 2;
                                speedX = newSpeedX;
                            }
                        }
                    }
                    if(standing){
                        if(speedY < -0.7)
                            getDamage( -(speedY + 0.7) * 20);
                        speedY = 0;
                    }
                    if (!standing) {
                        double newSpeedY = speedY - timeDiff * Gamer.gravity;
                        moveY = speedY * timeDiff - timeDiff * Gamer.gravity * Gamer.gravity / 2;
                        speedY = newSpeedY;
                    }
                    move(moveX, moveY);
                    crosshair.x = (int) (mousePos.x + posX - 780);
                    crosshair.y = (int) (mousePos.y + posY - 420);
            }
        }
    }

    private void move(double moveX, double moveY){
        posX += moveX;
        posY -= moveY;
    }

    protected synchronized void jump(){
        if(standing) {
            speedY = jumpStrength;
            standing = false;
        }
    }

    private void getDamage(double damage){
        hp -= damage;
        if(hp < 0) {
            alive = false;
        }
    }

    @Override
    public void render(Graphics graphics) {
        Gamer.camMove((int) posX - 780, (int)posY - 420);
        super.render(graphics);
        graphics.drawString(Double.toString(speedX), 1500, 60);
        graphics.drawString(Double.toString(speedY), 1500, 80);
        graphics.drawString(Double.toString(hp), 1500, 100);
        graphics.drawString(String.valueOf(crosshair.x), 1500, 120);
        graphics.drawString(String.valueOf(crosshair.y), 1500, 140);
    }

    Rectangle head;
    Rectangle body;
    Rectangle legs;

    @Override
    public void run() {
        mainLoop();
    }
}
