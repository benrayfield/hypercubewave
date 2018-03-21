/** Ben F Rayfield offers this software opensource GNU GPL 2+ */
package hypercubewave;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import util.Time;

public class HypercubeAftransVisualizer extends JPanel{
	
	/** set of buttons held down, including MouseEvent.BUTTON1+buttonIndex and KeyEvent.VK_A */
	public final Set<Integer> buttons = new HashSet();
	
	public int mouseY, mouseX, mouseDy, mouseDx;
	
	public final Map<Integer,Fork> forks = new HashMap();
	
	boolean whitneyMusicBox = false;
	
	public HypercubeAftransVisualizer(){
		setFocusable(true); //for keylistener
		addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){}
			public void keyPressed(KeyEvent e){
				buttons.add(e.getKeyCode());
				onGameControllerInput();
			}
			public void keyReleased(KeyEvent e) {
				buttons.remove(e.getKeyCode());
				onGameControllerInput();
			}
		});
		addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				buttons.add(e.getButton());
				onGameControllerInput();
			}
			public void mouseReleased(MouseEvent e){
				buttons.remove(e.getButton());
				onGameControllerInput();
			}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
		});
		addMouseMotionListener(new MouseMotionListener(){
			public void mouseMoved(MouseEvent e){
				mouseDy = Math.max(-10,Math.min(e.getY()-mouseY,10));
				mouseDx = Math.max(-10, Math.min(e.getX()-mouseX,10));
				mouseY = e.getY();
				mouseX = e.getX();
				onGameControllerInput();
			}
			public void mouseDragged(MouseEvent e){ mouseMoved(e); }
		});
		for(int i=0; i<70; i++){
			forks.put(10000000+i*2,new Fork(0,1,.5f,.5f)); //average/bellcurve/blur
			forks.put(10000000+i*2+1,new Fork(1,0,.5f,.5f)); //average/bellcurve/blur
		}
	}
	
	protected void onGameControllerInput(){
		if(whitneyMusicBox){
			synchronized(forks){
				/*forks.clear();
				for(int i=0; i<70; i++){
					forks.put(10000000+i*2,new Fork(0,1,.5f,.5f)); //average/bellcurve/blur
					forks.put(10000000+i*2+1,new Fork(1,0,.5f,.5f)); //average/bellcurve/blur
				}*/
				double now = Time.time();
				double scaledNow = now*.1;
				//for(int inverseFreq = 1; inverseFreq<30; inverseFreq++){
				//for(int inverseFreq = 10; inverseFreq<70; inverseFreq+=5){
				for(int inverseFreq = 1; inverseFreq<20; inverseFreq++){
					if(includeInverseFreqInWhitneyMusicBox(inverseFreq)){
						float multA = (float)(1/Math.sqrt(2)), multB = -multA;
						//float radius = 5; //TODO variable freq?
						float radius = 5+.2f*inverseFreq;
						double angle = 2*Math.PI*scaledNow%inverseFreq;
						float dy = radius*(float)Math.sin(angle);
						float dx = radius*(float)Math.cos(angle);
						forks.put(100000000+inverseFreq, new Fork(dy, dx, multA, multB));
					}
				}
			}
		}
		for(int button : buttons){
			//TODO Does the scale here matter?
			float multA = (float)(1/Math.sqrt(2)), multB = -multA;
			Fork prevFork = forks.get(button);
			if(prevFork == null) prevFork = new Fork(0,0,multA,multB);
			//int dy = mouseY/10-30;
			//int dx = mouseX/10-30;
			float scaleMouse = .2f;
			forks.put(button, prevFork.setDx(prevFork.dx+mouseDx*scaleMouse).setDy(prevFork.dy+mouseDy*scaleMouse));
		}
		repaint();
	}
	
	public boolean includeInverseFreqInWhitneyMusicBox(int inverseFreq){
		return BigInteger.valueOf(inverseFreq).isProbablePrime(100);
	}
	
	public void paint(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		/*g.setXORMode(Color.white);
		g.fillRect(getWidth()/2, getHeight()/2, 1, 1);
		/*forkAftrans(g,3,5);
		forkAftrans(g,-10,17);
		forkAftrans(g,-2,9);
		*/
		float[][] f = new float[128][128];
		f[25][25] = 1;
		
		/*boolean isWave = false;
		f = fork(f,3,5,isWave);
		for(int i=0; i<10; i++){
			f = fork(f,0,1,isWave);
			f = fork(f,1,0,isWave);
		}
		isWave = true;
		f = fork(f,-10,17,isWave);
		f = fork(f,-2,9,isWave);
		f = fork(f,1,2,isWave);
		f = fork(f,20,-12,isWave);
		f = fork(f,mouseY/10-30,0,isWave);
		f = fork(f,0,mouseX/10-30,isWave);
		*/
		synchronized(forks){
			for(Fork fork : forks.values()){ //order has no effect except where it falls off edge of screen then would have come back
				f = fork(f,fork);
			}
		}
		
		
		absVal(f);
		norm(f);
		int h = getHeight(), w = getWidth();
		for(int gridY=0; gridY<f.length; gridY++){
			for(int gridX=0; gridX<f[0].length; gridX++){
				float bright = Math.max(0, Math.min(f[gridY][gridX],1));
				//bright = Rand.strongRand.nextFloat();
				g.setColor(new Color(bright,bright,bright));
				//g.fillRect(gridX*w/f[0].length, gridY*h/f.length, f[0].length/w, f.length/h);
				g.fillRect(gridX*w/f[0].length, gridY*h/f.length, w/f[0].length+1, h/f.length+1);
			}
		}
		double voxels = Math.pow(2,forks.size());
		String display;
		/*if(voxels < 1e3){
			display = voxels+" voxels";
		}else if(voxels < 1e6){
			display = voxels/1e3+" kilovoxels";
		}else if(voxels < 1e9){
			display = voxels/1e6+" megavoxels";
		}else if(voxels < 1e12){
			display = voxels/1e9+" gigavoxels";
		}else{ //TODO bigger names. its scalable
			display = voxels/1e12+" teravoxels";
		}*/
		display = display = voxels+" voxels";
		System.out.println(display);
		g.setColor(new Color(0f,1f,1f));
		g.drawString(display, 50, 50);
	}
	
	/*static void forkAftrans(Graphics g, int dy, int dx){
		g.
		TODO
	}*/
	
	public static float[][] fork(float[][] f, Fork fork){
		int h = f.length, w = f[0].length;
		float[][] ret = new float[h][w];
		weightedSumOfSameSizeImages(ret, fork.multA, f, 0, 0);
		weightedSumOfSameSizeImages(ret, fork.multB, f, (int)(fork.dy+.5f), (int)(fork.dx+.5f));
		return ret;
	}
	
	/** TODO optimize by not creating new float array, just reuse 2 of them *
	public static float[][] fork(float[][] f, int dy, int dx, boolean isWave){
		int h = f.length, w = f[0].length;
		float[][] ret = new float[h][w];
		float multA = isWave ? -.5f :  .5f;
		float multB = isWave ? .5f : .5f;
		weightedSumOfSameSizeImages(ret, multA, f, 0, 0);
		weightedSumOfSameSizeImages(ret, multB, f, dy, dx);
		return ret;
	}
	
	public static float[][] forkWave(float[][] f, int dy, int dx){
		int h = f.length, w = f[0].length;
		float[][] ret = new float[h][w];
		weightedSumOfSameSizeImages(ret, -.5f, f, 0, 0);
		weightedSumOfSameSizeImages(ret, .5f, f, dy, dx);
		return ret;
	}*/
	
	public static void weightedSumOfSameSizeImages(float sumIntoMe[][], float mult, float[][] f, int dy, int dx){
		//int yStart = Math.max(0, dy);
		//int yEnd = Math.min(f.le)
		int h = f.length, w = f[0].length;
		if(h != sumIntoMe.length || w != sumIntoMe[0].length) throw new Error("Diff size float[][]s");
		for(int y=0; y<h; y++){
			for(int x=0; x<w; x++){
				if(0 <= y+dy && y+dy < h && 0 <= x+dx && x+dx < w){ //TODO optimize by doing this outside the loop
					sumIntoMe[y+dy][x+dx] += mult*f[y][x];
				}
			}
		}
	}
	
	public static float min(float f[][]){
		int h = f.length, w = f[0].length;
		float min = Float.MAX_VALUE;
		for(int y=0; y<h; y++){
			for(int x=0; x<w; x++){
				min = Math.min(min, f[y][x]);
			}
		}
		return min;
	}
	
	public static float max(float f[][]){
		int h = f.length, w = f[0].length;
		float max = Float.MIN_VALUE;
		for(int y=0; y<h; y++){
			for(int x=0; x<w; x++){
				max = Math.max(max, f[y][x]);
			}
		}
		return max;
	}
	
	public static void multEq(float scaleMe[][], float mult){
		int h = scaleMe.length, w = scaleMe[0].length;
		for(int y=0; y<h; y++){
			for(int x=0; x<w; x++){
				scaleMe[y][x] *= mult;
			}
		}
	}
	
	public static void absVal(float absMe[][]){
		int h = absMe.length, w = absMe[0].length;
		for(int y=0; y<h; y++){
			for(int x=0; x<w; x++){
				absMe[y][x] = Math.abs(absMe[y][x]);
			}
		}
	}
	
	public static void norm(float scaleMe[][]){
		float max = max(scaleMe), min = min(scaleMe);
		//if(m <= 0) return;
		//multEq(scaleMe, 1/m);
		float range = max-min;
		int h = scaleMe.length, w = scaleMe[0].length;
		for(int y=0; y<h; y++){
			for(int x=0; x<w; x++){
				scaleMe[y][x] = (scaleMe[y][x]-min)/range;
			}
		}
	}
	
	
	
	public static void main(String[] args){
		
		HypercubeAftransVisualizer p = new HypercubeAftransVisualizer();
		
		JFrame window = new JFrame("Hold any keyboard button while moving mouse "+HypercubeAftransVisualizer.class.getName());
		window.add(p);
		int windowH = 500, windowW = 500;
		window.setSize(windowW, windowH);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(screen.width/2-windowW/2, 260);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		if(p.whitneyMusicBox){
			while(true){
				p.onGameControllerInput();
				Time.sleepNoThrow(.01);
			}
		}
	}

}
