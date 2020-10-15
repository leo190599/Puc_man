package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import base.Game;
import world.World;

public class Fantasma1 extends Entidade{

	private boolean movingR=false,movingL=false,movingU=false,movingD=false;
	private boolean pretendMoveR=false, pretendMoveL=false, pretendMoveU=false, pretendMoveD=false;
	private BufferedImage[] animFrames;
	private int curFrameTime=0;
	private int curFrameIndex=0;
	
	public Fantasma1(int x, int y,int startFrameXCoordinate) {
		super(x, y);
		animFrames=new BufferedImage[2];
		for(int i=0;i<2;i++)
		{
			animFrames[i]=Game.getSpritesheet().getSprite(startFrameXCoordinate, 80+16*i, 16, 16);
		}
	}
	
	public void tick()
	{
		setMovementDirection();
		move();
	}
	
	public void setPosition(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	private void setMovementDirection()
	{
		if(pretendMoveU&&!movingD&&World.isFree(x, y, maskX, maskY, maskWidth, maskHeight, 0, -1))
		{
			movingU=true;
			movingL=false;
			movingR=false;
		}
		else 
		{
			if(pretendMoveD&&!movingU&&World.isFree(x, y, maskX, maskY, maskWidth, maskHeight, 0, 1))
			{
				movingD=true;
				movingL=false;
				movingR=false;
			}
			else
			{
				if(pretendMoveR&&!movingL&&World.isFree(x, y, maskX, maskY, maskWidth, maskHeight, 1, 0))
				{
					movingD=false;
					movingU=false;
					movingR=true;
				}
				else 
				{
					if(pretendMoveL&&!movingR&&World.isFree(x, y, maskX, maskY, maskWidth, maskHeight, -1, 0))
					{
						movingD=false;
						movingU=false;
						movingL=true;				
					}
				}
			}
		}
	}
	private void move()
	{
		if(movingU)
		{	if(World.isFree(x, y, maskX, maskY, maskWidth, maskHeight, 0,-speed))
			y-=speed;
		}
		else 
		{ 
			if(movingD)
			{
				if(World.isFree(x, y, maskX, maskY, maskWidth, maskHeight, 0,speed))
				y+=speed;
			}
			else
			{
				if(movingR)
				{
					if(World.isFree(x, y, maskX, maskY, maskWidth, maskHeight, speed,0))
					x+=speed;
				}
				else
				{
					if(movingL)
					{
						if(World.isFree(x, y, maskX, maskY, maskWidth, maskHeight, -speed,0))
						x-=speed;
					}
				}
			}
		}
	}
	
	public void moveLeft()
	{
		pretendMoveD=false;
		pretendMoveU=false;
		pretendMoveR=false;
		pretendMoveL=true;
	}
	public void moveRight()
	{
		pretendMoveD=false;
		pretendMoveU=false;
		pretendMoveR=true;
		pretendMoveL=false;
	}
	public void moveUp()
	{
		pretendMoveD=false;
		pretendMoveU=true;
		pretendMoveR=false;
		pretendMoveL=false;
	}
	public void moveDown()
	{
		pretendMoveD=true;
		pretendMoveU=false;
		pretendMoveR=false;
		pretendMoveL=false;
	}
	
	public void render(Graphics g)
	{
		//super.render(g);
		animFrame(g,animFrames);
	}
	private void animFrame(Graphics g,BufferedImage[] vectorToAnimate)
	{
		if(curFrameIndex>=vectorToAnimate.length)
		{
			curFrameIndex=0;
			curFrameTime=0;
		}
		g.drawImage(vectorToAnimate[curFrameIndex], x, y, null);
		curFrameTime++;
		if(curFrameTime>=15)
		{
			curFrameIndex++;
			curFrameTime=0;
		}
	}
}
