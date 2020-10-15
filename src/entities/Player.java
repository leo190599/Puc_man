package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import base.Game;
import world.World;

public class Player extends Entidade{
	
	private int life=3;
	private int points=0;
	private boolean movingR=false,movingL=false,movingU=false,movingD=false;
	private boolean pretendMoveR=false, pretendMoveL=false, pretendMoveU=false, pretendMoveD=false;
	private BufferedImage[] idleFrames;
	private BufferedImage[] movingRFrames;
	private BufferedImage[] movingDFrames;
	private BufferedImage[] movingUFrames;
	private BufferedImage[] movingLFrames;
	private BufferedImage[] idleInvFrames;
	private BufferedImage[] movingRInvFrames;
	private BufferedImage[] movingDInvFrames;
	private BufferedImage[] movingUInvFrames;
	private BufferedImage[] movingLInvFrames;
	private int curFrameTime=0;
	private int curFrameIndex=0;
	private final int invulnerablityTimeMaxTime=90;//Em frames,ou seja 1,5 segundos
	private int curinvulnerablityTime=0;
	private boolean invulnerable=false;
	
	public Player(int x, int y) {
		super(x, y);
		initializeFrames(1,2,2,2,2);
		movingDFrames[1]=Game.getSpritesheet().getSprite(0, 48, 16, 16);
		movingUFrames[1]=Game.getSpritesheet().getSprite(0, 0, 16, 16);
		movingRFrames[1]=Game.getSpritesheet().getSprite(0, 16, 16, 16);
		movingLFrames[1]=Game.getSpritesheet().getSprite(0, 32, 16, 16);
		
		movingDInvFrames[1]=Game.getSpritesheet().getSprite(32, 48, 16, 16);
		movingUInvFrames[1]=Game.getSpritesheet().getSprite(32, 0, 16, 16);
		movingRInvFrames[1]=Game.getSpritesheet().getSprite(32, 16, 16, 16);
		movingLInvFrames[1]=Game.getSpritesheet().getSprite(32, 32, 16, 16);
	}

	private void initializeFrames(int iFSize,int dFSize,int uFSize,int rFSize,int lFSize)
	{
		idleFrames=new BufferedImage[iFSize];
		movingDFrames=new BufferedImage[dFSize];
		movingUFrames=new BufferedImage[uFSize];
		movingRFrames=new BufferedImage[rFSize];
		movingLFrames=new BufferedImage[lFSize];
		
		idleInvFrames=new BufferedImage[iFSize];
		movingDInvFrames=new BufferedImage[dFSize];
		movingUInvFrames=new BufferedImage[uFSize];
		movingRInvFrames=new BufferedImage[rFSize];
		movingLInvFrames=new BufferedImage[lFSize];
		
		idleFrames[0]=Game.getSpritesheet().getSprite(16, 0, 16, 16);
		movingDFrames[0]=idleFrames[0];
		movingUFrames[0]=idleFrames[0];
		movingRFrames[0]=idleFrames[0];
		movingLFrames[0]=idleFrames[0];
		
		idleInvFrames[0]=Game.getSpritesheet().getSprite(48, 0, 16, 16);
		movingDInvFrames[0]=idleInvFrames[0];
		movingUInvFrames[0]=idleInvFrames[0];
		movingRInvFrames[0]=idleInvFrames[0];
		movingLInvFrames[0]=idleInvFrames[0];
	}
	public int getLife()
	{
		return life;
	}
	public int getPoints()
	{
		return points;
	}
	public void tick()
	{
		setMovementDirection();
		checkCollisionEntities();
		checkInvulnerability();
		move();
	}
	
	private void checkInvulnerability()
	{
		if(invulnerable)
		{
			curinvulnerablityTime++;
			if(curinvulnerablityTime>invulnerablityTimeMaxTime)
			{
				curinvulnerablityTime=0;
				invulnerable=false;
			}
		}
	}
	
	private void checkCollisionEntities()
	{
		Rectangle thisColMask=new Rectangle(x+maskX,y+maskY,maskWidth,maskHeight);
		for(int i=0;i<Game.getEntidades().size();i++)
		{
			if(Game.getEntidades().get(i).equals(this))
				continue;
			Entidade otherEntity=Game.getEntidades().get(i);
			Rectangle otherColMask=new Rectangle(otherEntity.getX()+otherEntity.getMaskX(),
					otherEntity.getY()+otherEntity.getMaskY(),
					otherEntity.getMaskWidth(),
					otherEntity.getMaskHeight());
			if(thisColMask.intersects(otherColMask))
			{
				if(otherEntity instanceof Moeda)
				{
					Game.getEntidades().remove(i);
					points+=10;
				}
				if(otherEntity instanceof Fantasma1&&!invulnerable)
				{
					life--;
					invulnerable=true;
					System.out.println(life);
				}
			}
		}
	}
	
	public void setPosition(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public void setInvulnerable()
	{
		if(points>=300)
		{
			invulnerable=true;
			points-=300;
		}
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
		frameControl(g);
		
	}
	
	private void frameControl(Graphics g)
	{
		if(!invulnerable)
		{
			if(movingU)
				animFrame(g,movingUFrames);
			else if(movingD)
				animFrame(g,movingDFrames);
			else if(movingL)
				animFrame(g,movingLFrames);
			else if(movingR)
				animFrame(g,movingRFrames);
			else 
				animFrame(g,idleFrames);
		}
		else
		{
			if(movingU)
				animFrame(g,movingUInvFrames);
			else if(movingD)
				animFrame(g,movingDInvFrames);
			else if(movingL)
				animFrame(g,movingLInvFrames);
			else if(movingR)
				animFrame(g,movingRInvFrames);
			else 
				animFrame(g,idleInvFrames);
		}
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
