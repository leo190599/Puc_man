package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import base.Game;
import base.SpriteSheet;
import entities.Fantasma1;
import entities.Moeda;
import entities.Player;

public class World {
	
	private static int width;
	private static int height;
	private static Tile[] tiles;
	
	public World(String level, SpriteSheet sprite,Player player,Fantasma1 f1, Fantasma1 f2)
	{
			
		try {
			BufferedImage imagemLevel=ImageIO.read(new File("./res/sala"+level+".png"));
			width=imagemLevel.getWidth();
			height=imagemLevel.getHeight();
			tiles=new Tile[width*height];
			int pixels[]=new int[tiles.length];
			imagemLevel.getRGB(0, 0, width, height, pixels, 0, width);
			for(int i=0;i<width;i++)
			{
				for(int j=0;j<height;j++)
				{
					switch(pixels[i+j*width])
					{
						case 0xFF000000:
							tiles[i+j*width]=new TileFloor(i*16,j*16,16,16,sprite.getSprite(192, 0, 16, 16));
							Game.getEntidades().add(new Moeda(i*16,j*16));
						break;
						case 0xFFFFFFFF:
							tiles[i+j*width]=new TileWall(i*16,j*16,16,16,sprite.getSprite(208, 0, 16, 16));
						break;
						case 0xFF0000FF:
							tiles[i+j*width]=new TileFloor(i*16,j*16,16,16,sprite.getSprite(192, 0, 16, 16));
							player.setPosition(i*16, j*16);
						break;
						case 0xFFFF0000:
							tiles[i+j*width]=new TileFloor(i*16,j*16,16,16,sprite.getSprite(192, 0, 16, 16));
							Game.getEntidades().add(new Moeda(i*16,j*16));
							f1.setPosition(i*16, j*16);
						break;
						case 0xFFFFA000:
							tiles[i+j*width]=new TileFloor(i*16,j*16,16,16,sprite.getSprite(192, 0, 16, 16));
							Game.getEntidades().add(new Moeda(i*16,j*16));
							f2.setPosition(i*16, j*16);
						break;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void render(Graphics g)
	{
		for(int i=0;i<tiles.length;i++)
		{
			tiles[i].render(g);
		}
	}
	public Tile[] getTiles()
	{
		return tiles;
	}
	public static boolean isFree(int x,int y,int maskX,int maskY,int maskWidth, int maskHeight, int pretendedMovimentX,int pretendedMovimentY)
	{
		if(tiles[(x+maskX+pretendedMovimentX)/16+(y+maskY+pretendedMovimentY)/16*width] instanceof TileWall||
		   tiles[(x+maskX+maskWidth-1+pretendedMovimentX)/16+(y+maskY+pretendedMovimentY)/16*width] instanceof TileWall||
		   tiles[(x+maskX+pretendedMovimentX)/16+(y+maskY+maskHeight-1+pretendedMovimentY)/16*width] instanceof TileWall||
		   tiles[(x+maskX+maskWidth-1+pretendedMovimentX)/16+(y+maskY+maskHeight-1+pretendedMovimentY)/16*width] instanceof TileWall)
		{
			return false;
		}
		return true;
	}
}
