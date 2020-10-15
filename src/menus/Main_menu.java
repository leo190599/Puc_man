package menus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import base.Game;

public class Main_menu{
	
	BufferedImage logo;
	
	private String[] options;
	private int windowWidth;
	private int windowHeight;
	private int curOption=0;
	
	public Main_menu(int width, int height) {
		
		try {
			logo=ImageIO.read(new File("./res/titulo.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		windowWidth=width;
		windowHeight=height;
		
		options=new String[3];
		
		options[0]="Jogar";
		options[1]="Como jogar";
		options[2]="Sair";

	}
	
	public void select()
	{
		switch(curOption)
		{
		 	case 0:
		 		Game.restartGame();
		 	break;
		 	case 1:
		 		Game.setGameState("helpMenu");
		 	break;
			case 2:
				System.exit(1);
			break;
		}
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.white);
		g.drawImage(logo, windowWidth/2-52, windowHeight/2-100, null);
		switch(curOption)
		{
		 	case 0:
		 		g.drawString(">", windowWidth/2-20, (windowHeight/2)+20);
		 	break;
		 	case 1:
		 		g.drawString(">", windowWidth/2-37, (windowHeight/2)+40);
			break;
		 	case 2:
		 		g.drawString(">", (windowHeight/2)-15, (windowHeight/2)+60);
			break;
		}
		
		g.drawString(options[0], windowWidth/2-10, (windowHeight/2)+20);
		g.drawString(options[1], windowWidth/2-27, (windowHeight/2)+40);
		g.drawString(options[2], (windowHeight/2)-5, (windowHeight/2)+60);
	}
	
	public void optionDown()
	{
		if(curOption+1>=options.length)
		{
			curOption=0;
		}
		else
		{
			curOption++;
		}
	}
	
	public void optionUp()
	{
		if(curOption-1<0)
		{
			curOption=options.length-1;
		}
		else
		{
			curOption--;
		}
	}
}
