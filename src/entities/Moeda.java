package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import base.Game;

public class Moeda extends Entidade{

	private BufferedImage[] frames;
	private int curFrameTime=0;
	private int curFrameIndex=0;
	
	public Moeda(int x, int y) {
		super(x, y);
		frames=new BufferedImage[2];
		frames[0]=Game.getSpritesheet().getSprite(224, 0, 16, 16);
		frames[1]=Game.getSpritesheet().getSprite(224, 16, 16, 16);
		this.maskWidth=2;
		this.maskX=7;
		this.maskHeight=6;
		this.maskY=5;
	}
	
	public void render(Graphics g)
	{
		//super.render(g);
		animFrame(g,frames);
	}
	
	private void animFrame(Graphics g, BufferedImage[] vectorToAnimate)
	{
		if(curFrameIndex>=vectorToAnimate.length)
		{
			curFrameIndex=0;
			curFrameTime=0;
		}
		g.drawImage(vectorToAnimate[curFrameIndex], x, y, null);
		curFrameTime++;
		if(curFrameTime>=20)
		{
			curFrameTime=0;
			curFrameIndex++;
		}
	}
}
