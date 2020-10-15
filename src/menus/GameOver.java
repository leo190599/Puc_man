package menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import base.Game;

public class GameOver {
	
	private int windowWidth;
	private int windowHeight;
	
	public GameOver(int width, int height)
	{
		windowWidth=width;
		windowHeight=height;
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.PLAIN,20));
		g.drawString("Fim de jogo", windowWidth/2-50, windowHeight/2-30);
		g.setFont(new Font("arial",Font.PLAIN,14));
		if(Game.getPlayer().getLife()<=0)
		{
			g.drawString("Fantasmas venceram",windowWidth/2-65, windowHeight/2+20);
		}
		else
		{
			g.drawString("Puc Man venceu",windowWidth/2-50, windowHeight/2+20);
		}
		g.setFont(new Font("arial",Font.PLAIN,11));
		g.drawString("Pressione qualquer tecla para continuar:", windowWidth/2-95, windowHeight/2+90);
	}
}
