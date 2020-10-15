package base;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import entities.Entidade;
import entities.Fantasma1;
import entities.Player;
import menus.GameOver;
import menus.HelpMenu;
import menus.Main_menu;
import world.World;

public class Game extends Canvas implements Runnable,KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread thread;
	private final int width=240;
	private final int height = 240;
	private final int scale = 4;
	private boolean isRunning=false;
	private Graphics g;
	private BufferStrategy bs;
	private BufferedImage baseI;
	private static List<Entidade> entidades;
	private static Player player;
	private static Fantasma1 f1;
	private static Fantasma1 f2;
	private static World world;
	private static SpriteSheet spriteSheet;
	private static String gameState="mainMenu";
	private static GameOver gameOverScreen;
	private static HelpMenu helpMenu;
	private Main_menu mainMenu;
	private static Random rand;
	
	public Game()
	{
		rand=new Random();
		mainMenu=new Main_menu(width,height);
		gameOverScreen=new GameOver(width,height);
		helpMenu=new HelpMenu();
		spriteSheet=new SpriteSheet();
		baseI=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.setPreferredSize(new Dimension(width*scale,height*scale));
		this.addKeyListener(this);
		entidades=new ArrayList<Entidade>();
		//player=new Player(0,0);
		f1=new Fantasma1(0,0,0);
		f2=new Fantasma1(0,0,16);
		//world=new World("Testes",spriteSheet,player,f1,f2);
		entidades.add(f1);
		entidades.add(f2);
		entidades.add(player);
	}
	
	public static void restartGame()
	{
		player=new Player(0,0);
		entidades.clear();
		world=new World(Integer.toString(rand.nextInt(5)),spriteSheet,player,f1,f2);
		entidades.add(f1);
		entidades.add(f2);
		entidades.add(player);
		setGameState("running");
	}
	
	public static void setGameState(String state)
	{
		gameState=state;
	}
	
	public static Player getPlayer()
	{
		return player;
	}
	
	public void tick()
	{
		if(gameState=="running") {
			for(int i=0;i<entidades.size();i++)
			{
				entidades.get(i).tick();
			}
			checkEndGame();
		}
	}
	
	private void checkEndGame()
	{
		if(player.getLife()<=0)
		{
			setGameState("gameOver");
		}
		else if(entidades.size()<=3)
		{
			setGameState("gameOver");
		}
	}
	
	public void render()
	{
		g=baseI.getGraphics();
		g.setColor(Color.black);
		//g.fillRect(0, 0,GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth(),
				//GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());
		//g.setColor(Color.blue);
		g.fillRect(0, 0, width, height);
		if(gameState=="running")
		{
			world.render(g);
			for(int i=0;i<entidades.size();i++)
			{
				entidades.get(i).render(g);
			}
			renderUI(g);
		}
		else if(gameState=="mainMenu")
		{
			mainMenu.render(g);
		}
		else if(gameState=="gameOver")
		{
			gameOverScreen.render(g);
		}
		else if(gameState=="helpMenu")
		{
			helpMenu.render(g);
		}
		g=bs.getDrawGraphics();
		g.drawImage(baseI, 0, 0,width*scale,height*scale,null);
		bs.show();
	}
	
	private void renderUI(Graphics g)
	{
		g.setFont(new Font("arial",Font.PLAIN,12));
		g.setColor(Color.white);
		g.drawString("Pac man lifes: "+Integer.toString(player.getLife()), 5, 12);
		g.drawString("Pac man points: "+Integer.toString(player.getPoints()), 115, 12);
	}
	public void start()
	{
		isRunning=true;
		thread =new Thread(this);
		thread.start();
	}
	public void stop()
	{
		isRunning=false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static List<Entidade> getEntidades()
	{
		return entidades;
	}
	
	public static SpriteSheet getSpritesheet()
	{
		return spriteSheet;
	}
	
	@Override
	public void run() {
		this.createBufferStrategy(3);
		bs=this.getBufferStrategy();
		//int frames=0;
		
		//double lasTime=System.currentTimeMillis();
		//double curTime;
		
		double lastTime=System.nanoTime();
		final int fps=60;
		final double nsTime=1000000000/fps;
		double currentTime;
		double deltaTime=0;
		this.requestFocus();
		while(isRunning)
		{
			//System.out.println(deltaTime);
			//curTime=System.currentTimeMillis();
			
			currentTime=System.nanoTime();
			deltaTime+=(currentTime-lastTime);
			lastTime=currentTime;
			if(deltaTime>=nsTime)
			{	
				tick();
				render();
				deltaTime=0;
				//frames++;
			}
			
			//if((curTime-lasTime)>=1000)
			//{
				//lasTime=curTime;
				//System.out.println(frames);
				//frames=0;
			//}
		}
		stop();
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		switch(gameState)
		{
		case "running":
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_W:
					player.moveUp();
				break;
				case KeyEvent.VK_S:
					player.moveDown();
				break;
				case KeyEvent.VK_A:
				player.moveLeft();
				break;
				case KeyEvent.VK_D:
					player.moveRight();
				break;
				case KeyEvent.VK_SPACE:
					player.setInvulnerable();
				break;
				case KeyEvent.VK_I:
					f1.moveUp();
				break;
				case KeyEvent.VK_K:
					f1.moveDown();
				break;
				case KeyEvent.VK_J:
					f1.moveLeft();
				break;
				case KeyEvent.VK_L:
					f1.moveRight();
				break;
				case KeyEvent.VK_NUMPAD8:
					f2.moveUp();
				break;
				case KeyEvent.VK_NUMPAD5:
					f2.moveDown();
				break;
				case KeyEvent.VK_NUMPAD4:
					f2.moveLeft();
				break;
				case KeyEvent.VK_NUMPAD6:
					f2.moveRight();
				break;
			}
		break;
		case "mainMenu":
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_W:
					mainMenu.optionUp();
				break;
				case KeyEvent.VK_S:
					mainMenu.optionDown();
				break;
				case KeyEvent.VK_UP:
					mainMenu.optionUp();
				break;
				case KeyEvent.VK_DOWN:
					mainMenu.optionDown();
				break;
				case KeyEvent.VK_ENTER:
					mainMenu.select();
				break;
			}
		break;
		case "gameOver":
			switch(e.getKeyCode())
			{
				default:
					Game.setGameState("mainMenu");
				break;
			}
		break;
		case "helpMenu":
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_ENTER:
					helpMenu.advance();
				break;
				default:
					Game.setGameState("mainMenu");
				break;
			}
		break;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {

		
		
	}
}
