package menus;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import base.Game;

public class HelpMenu {
	
	private BufferedImage[] instructions;
	private int index=0;

	public HelpMenu()
	{
		instructions=new BufferedImage[5];
		for(int i=0;i<instructions.length;i++)
		{
			try {
				instructions[i]=ImageIO.read(new File("./res/tutorial"+i+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void render(Graphics g)
	{
		g.drawImage(instructions[index], 0, 0, null);
	}
	public void advance()
	{
		if(index+1<instructions.length)
		{
			index++;
		}
		else
		{
			index=0;
			Game.setGameState("mainMenu");
		}
	}
}
