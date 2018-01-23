/*
 * The values of M,N and S change as according to given in the assignment question.
 * The value of S is adjusted as it should not become very fast.
 * As a result the factor S is divided by 10 on every assignment.
 * FS remains the same as given in the assignment question.
 * The code has some features from the code given at:	www.oschina.net/code/snippet_660055_27792
 */ 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class Tetris extends JFrame{
	public static int sf=1;		// Static for Score Factor
	public static int ln=20;	// Static for Lines for level up
	public static float spf=0.1F;	// Static for Speed Factor
	public static int wd=10;	// Static for width
	public static int hi=20;	// Static for height
	public static float bs=3;	// Static for blocksize
	public static int change=0;	// Static flag for indicating change
	public Tetris()
    {
		Tetrisblok a = new Tetrisblok();	// Calls a new game
		addKeyListener(a);
		add(a);
	}
    public static void main(String[] args) {
		
		Tetris frame = new Tetris();	// New frame
		JMenuBar menu = new JMenuBar();	// Create menu
        frame.setJMenuBar(menu);	
		JMenu game = new JMenu("Start Game");
        JSlider SF_S = new JSlider(1,10,1);	// Slider for Score factor
        SF_S.addChangeListener(new ChangeListener()
        {
			
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if(!source.getValueIsAdjusting())
                {
                    sf = source.getValue();
                    change=1;	// Indicate change
                }
            }
		});
        JSlider LN_S = new JSlider(20,50,20);	// Slider for Lines required
        LN_S.addChangeListener(new ChangeListener()
        {
			
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if(!source.getValueIsAdjusting())
                {
					change=1;
                    ln = source.getValue();
                }
            }
		});
        JSlider SPF_S = new JSlider(1,10,1);	// Slider for speed factor
        SPF_S.addChangeListener(new ChangeListener()
        {
			
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if(!source.getValueIsAdjusting())
                {
					change=1;
                    spf = source.getValue();
                    spf=spf/10;			// Adjusting the factor
                    //System.out.println("Change"+ spf);
                }
            }
		});
        JSlider WD_S = new JSlider(5,20,10);	// Slider for width
        WD_S.addChangeListener(new ChangeListener()
        {
			
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if(!source.getValueIsAdjusting())
                {
					change=1;
                    wd = source.getValue();
                }
            }
		});
        JSlider HI_S = new JSlider(10,40,20);	// Slider for height
        HI_S.addChangeListener(new ChangeListener()
        {
			
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if(!source.getValueIsAdjusting())
                {
					change=1;
                    hi = source.getValue();
                }
            }
		});
        JSlider BS_S = new JSlider(1,5,3);	// Slider for blocksize
        BS_S.addChangeListener(new ChangeListener()
        {
			
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if(!source.getValueIsAdjusting())
                {
					change=1;
                    bs = source.getValue();
                }
            }
		});
        SF_S.setPaintTicks(true);		// Paint the ticks for the sliders
        SF_S.setMinorTickSpacing(1);	// Add spacing
		SF_S.setPaintLabels(true);
		LN_S.setPaintTicks(true);
        LN_S.setMinorTickSpacing(1);
		LN_S.setPaintLabels(true);
        SPF_S.setPaintTicks(true);
        SPF_S.setMinorTickSpacing(1);
		SPF_S.setPaintLabels(true);
        WD_S.setPaintTicks(true);
        WD_S.setMinorTickSpacing(1);
		WD_S.setPaintLabels(true);
        HI_S.setPaintTicks(true);
        HI_S.setMinorTickSpacing(1);
		HI_S.setPaintLabels(true);
        BS_S.setPaintTicks(true);
        BS_S.setMinorTickSpacing(1);
		BS_S.setPaintLabels(true);
        JMenuItem SF = game.add("Scoring factor:(1-10)");	// Declare names and indicate values
        game.add(SF_S);										// Add the sliders to the menu
        JMenuItem LN = game.add("Lines for Next Level:(20-50)");
        game.add(LN_S);
        JMenuItem SPF = game.add("Speed Factor:(0.1-1.0)");
        game.add(SPF_S);
        JMenuItem WD = game.add("Width:(5-20)");
        game.add(WD_S);
        JMenuItem HI = game.add("Height:(10-40)");
        game.add(HI_S);
        JMenuItem BS = game.add("Blocksize:(1-5)");
        game.add(BS_S);
        menu.add(game);
		frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);	// Set size of frame
        frame.setTitle("Tetris");
        frame.setVisible(true);
        frame.setResizable(true);	// Set properties of the frame
        
	}
}

class Tetrisblok extends JPanel implements KeyListener {
	private int blockType,nextType=-1,nextState=-1;	// Blocktype for type, next____ for next type and state
    private int score = 0,M=Tetris.sf,N=Tetris.ln,lines=0,level=1,FS=1;	// M=scoring factor, N=rows for difficulty, S=speed factor
    // FS = falling speed
    private float S = Tetris.spf * 10;
    private int turnState;	// Rotation state
    private int x;	// x and y coordinate
    private int y;
    private int i = 0;
    public int j=0;
	public int flag = 0;
    public Dimension d;	// To get size
	public int maxX, maxY, xCenter, yCenter, left, right, iCenter, bottom, top;
	public float pixelSize, Size, blockSize, rWidth=400.0F,rHeight=400.0F;
	public int len=Tetris.wd+2,hei=Tetris.hi+1,pau=0;	// Use static vars for length and height. pau is the flag for pause
	public float xA,yA,xB,yB,xC,yC;	//	To get mouse location
    int[][] map;
    // S、Z、L、J、I、O、T 7
    private final int shapes[][][] = new int[][][] {
    // i
            { { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
              { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 } },
            // s
            { { 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
              { 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 } },
            // z
            { { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
              { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 } },
            // j
            { { 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
              { 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
               { 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
            // o
            { { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
            // l
            { { 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
              { 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
            // t
            { { 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
              { 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 } } };

    public void newblock() {	// To add a new block
		if(nextType==-1)
		{
			blockType = (int) (Math.random() * 1000) % 7;	// To create a new block
		}
		else
		{
			blockType=nextType;	// Set the next
		}
		if(nextState==-1)
		{
			nextState = (int) (Math.random() * 1000) % 4;	 // Random next state
		}
		else
		{
			turnState=nextState;	// Next state to be turned as shown in next box
		}
        nextType = (int) (Math.random() * 1000) % 7;	// Randomly generate numbers
        nextState = (int) (Math.random() * 1000) % 4;
        x = 4;	// x and y coordinates
        y = 0;
        if (gameover(x, y) == 1) {	// Check for gameover
            newmap();		// Create a new map if gameover
            drawwall();		// Draw the wall
            score = 0;
            JOptionPane.showMessageDialog(null, "Game Over. Try again?");
        }
    }

    public void drawwall() {			// Create wall on boundaries
        for (int i = 0; i < len; i++) {
            map[i][hei-1] = 2;
        }
        for (int j = 0; j < hei; j++) {
            map[len-1][j] = 2;
            map[0][j] = 2;
        }
    }

    public void newmap() {	// Create a new map
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < hei; j++) {
                map[i][j] = 0;
            }
        }
    }
	public void init()	// Initialize on any change or on start
	{
		len=Tetris.wd+2;hei=Tetris.hi+1;	// Get static vars
		map = new int[len+1][hei+1];	// Create new map
		N=Tetris.ln;
		M=Tetris.sf;
		blockSize=Tetris.bs;
		S = Tetris.spf * 10;
		newblock();
        newmap();
        drawwall();
        Tetris.change=0;	// Reset change to observe further change
	}
    Tetrisblok() {
		init();	// Call to initialize
		Timer timer = new Timer(1000, new TimerListener());	// Start a timer for downward motion
        timer.start();    
        addMouseMotionListener(new MouseMotionAdapter()
		{ 
			public void mouseMoved(MouseEvent evt)
			{ 
				xA = fx(evt.getX()); yA = fy(evt.getY());
				float tx=fx(len*X(blockSize)),ty=fy(hei*X(blockSize));
				if(!(xA>tx || yA<ty) && pau!=1)
				{
					pau=1;	// To check if the mouse is in the main area
				}
				if((xA>tx || yA<ty) && pau==1)
				{
					pau=0;
				}
			}
		}); 
		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent evt)
			{
				xB = fx(evt.getX()); yB = fy(evt.getY());
				
				if (evt.getButton() == MouseEvent.BUTTON3) 
				{
					right();	// Move right
                    //	Right mouse button
                }
                else if (evt.getButton() == MouseEvent.BUTTON1) 
				{
					float tx=fx((len+5)*X(blockSize)),ty=fy(top + 18*X(blockSize));
					if(xB>tx && xB<(fx((len+9)*X(blockSize))) && yB<ty && yB>(ty-(2*X(blockSize))))
					{
						System.exit(0);	// Check mouse on Quit
					}
					left();	// Move left
                    //	Left mouse button
                }
			}
		});
		addMouseWheelListener(new MouseWheelListener()
		{
			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if (e.getWheelRotation() < 0)
				{
					turn1();	// Scroll
					//	Up scroll
				} 
				else 
				{	
					turn();	// Scroll other way
					//	Down scroll
				}
				if(pau==1)
				{
					xC = fx(e.getX()); yC = fy(e.getY());
					if(xC<-(left+x*X(blockSize)) && xC>-(left+ (x+3)*X(blockSize)))// To detect the cursor on the block
					{
						float tya=fy(y*X(blockSize));
						if(yC<tya && yC>(tya-(3*X(blockSize))))
						{
							newblock();		// Check if the cursor is on the block and change it
							newblock();		// Change twice so that it does not become the shape coming up next
							score=score-level*M;	// Adjust the score
						}
						
					}
				}
			}
		});
    }

    public void turn() {	// Rotate one way
		if(pau==0)
		{
			int tempturnState = turnState;
			turnState = (turnState + 1) % 4;	// Change the state
			if (blow(x, y, blockType, turnState) == 1) {
			}
			if (blow(x, y, blockType, turnState) == 0) {
				turnState = tempturnState;
			}	
			repaint();		// Redraw
		}
    }
    public void turn1() {	// Rotate the other way
        if(pau==0)
        {
			int tempturnState = turnState;
			turnState = (4 + turnState - 1) % 4;
			if (blow(x, y, blockType, turnState) == 1) {
			}
			if (blow(x, y, blockType, turnState) == 0) {
				turnState = tempturnState;
			}
			repaint();
		}
    }
    

    public void left() {	// Move left
		if(pau==0)
		{
			if (blow(x - 1, y, blockType, turnState) == 1) {
				x = x - 1;
			}
			;
			repaint();
		}
    }

    public void right() {	// Move right
        if(pau==0)
        {
			if (blow(x + 1, y, blockType, turnState) == 1) {
				x = x + 1;
			}
			;
			repaint();
		}
    }

    public void down() {	// Move down fast
        if(pau==0)
        {
			if (blow(x, y + 1, blockType, turnState) == 1) {
				y = y + 1;
				delline();
			}
			;
			if (blow(x, y + 1, blockType, turnState) == 0) {
				add(x, y, blockType, turnState);
				newblock();
				delline();
			}
			;
        repaint();
		}
    }

    public int blow(int x, int y, int blockType, int turnState) {
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                if (((shapes[blockType][turnState][a * 4 + b] == 1) && (map[x+ b + 1][y + a] == 1)) || ((shapes[blockType][turnState][a * 4 + b] == 1) && (map[x+ b + 1][y + a] == 2)))
                {
                    return 0;
                }
            }
        }
        return 1;
    }

    public void delline() {	// Check to erase a line
        int c = 0;
        for (int b = 0; b < hei; b++) {
            for (int a = 0; a < len; a++) {
                if (map[a][b] == 1) {
                    c = c + 1;
                    if (c == len-2) {
                        score = score+level*M;	// Modify score
                        lines++;	// Increase lines
                        if(lines%N==0)
                        {
							level++;	// Check for level up
							FS=FS*(1+(int)(level*S));	// Increase falling speed
						}
                        for (int d = b; d > 0; d--) {
                            for (int e = 0; e < len-1; e++) {
                                map[e][d] = map[e][d - 1];	// Bring the upper row downwards
                            }
                        }
                    }
                }
            }
            c = 0;	// Counter for completed blocks reset
        }
    }

    public int gameover(int x, int y) {	// Check for gameover
        if (blow(x, y, blockType, turnState) == 0) {
            return 1;
        }
        return 0;
    }

    public void add(int x, int y, int blockType, int turnState) {	// Add another block
        int j = 0;
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                if (map[x + b + 1][y + a] == 0) {
                    map[x + b + 1][y + a] = shapes[blockType][turnState][j];
                }
                ;
                j++;
            }
        }
    }

	int X(float x){return Math.round(x);}	// Functions for logical coordinates
	int iX(float x){return Math.round(xCenter + x/pixelSize);}
	int iY(float y){return Math.round(yCenter - y/pixelSize);}
	float fx(int x){return (x - xCenter) * pixelSize;}
	float fy(int y){return (yCenter - y) * pixelSize;}
    public void paintComponent(Graphics g) {	// To paint everything on the screen
		if(Tetris.change==1)
		{
			init();	// Initialize on change
		}
		d = getSize();		// Get size of frame
		maxX = d.width - 1;	// Set values
		maxY = d.height - 1;
		xCenter=maxX/2;
		yCenter=maxY/2;
		pixelSize = Math.max(rWidth/maxX, rHeight/maxY);	// Set pixelsize
		Size=Math.min(maxY/rHeight,maxX/rWidth);	// Set size
		blockSize=(Tetris.bs*0.28F)*Size*15;	// Set blocksize
		left = iX(-rWidth/2);	
		right = iX(rWidth/2);
		iCenter=(left+right)/2;
		bottom = iY(-rHeight/2);
		top = iY(rHeight/2);
		if(Tetris.change==1)
		{
			Tetris.change=0;	// Reset change
		}
		super.paintComponent(g);
		if(pau==1)
		{
			g.setFont(new Font("Courier New",Font.PLAIN,X(blockSize+20)));
			g.drawString("PAUSE", 2*X(blockSize),top + 5*X(blockSize));	// Pause in main area
		}
        for (j = 0; j < 16; j++) {
            if (shapes[blockType][turnState][j] == 1) {
				//iszjolt
				if(blockType==1){g.setColor(Color.blue);}	// Set the color of the blocks
                if(blockType==2){g.setColor(Color.yellow);}
                if(blockType==3){g.setColor(Color.pink);}
                if(blockType==4){g.setColor(Color.orange);}
                if(blockType==5){g.setColor(Color.green);}
                if(blockType==6){g.setColor(Color.red);}
                if(blockType==7){g.setColor(Color.yellow);}
                g.fillRect((j % 4  + x + 1) * X(blockSize),(j / 4 + y) * X(blockSize) , X(blockSize), X(blockSize));	// Draw it
            }
        }
		g.setColor(Color.black);	// The blocks which are settled down
        for (j = 0; j < hei; j++) {
            for (i = 0; i < len; i++) {
                if (map[i][j] == 1) {
                    g.fillRect((i) * X(blockSize), (j) * X(blockSize), X(blockSize), X(blockSize));
                }
                if (map[i][j] == 2) {
                    g.drawRect((i) * X(blockSize), (j) * X(blockSize), X(blockSize), X(blockSize));
                }
            }
        }
		g.setFont(new Font("Courier New",Font.PLAIN,X(blockSize)));		// Indicators
        g.drawString("Level=" + level, (len+5)*X(blockSize), top + X(blockSize));
        g.drawString("Lines=" + lines, (len+5)*X(blockSize), top + 3*X(blockSize));
        g.drawString("Score=" + score, (len+5)*X(blockSize), top + 5*X(blockSize));
        g.drawString("Up next:", (len+5)*X(blockSize), top + 7*X(blockSize));
        g.drawString("Quit",(len+5)*X(blockSize), top + (int)(19.5*X(blockSize)));
        g.drawRect((len+5)*X(blockSize), top + 9*X(blockSize),7*X(blockSize),7*X(blockSize));	// Boxes
        g.drawRect((len+5)*X(blockSize), top + 18*X(blockSize),4*X(blockSize),2*X(blockSize));
        for (j = 0; j < 16; j++) {
            if (shapes[nextType][nextState][j] == 1) {
                g.fillRect((j % 4  + (len+6) + 1) * X(blockSize),top + (j / 4  + 11) * X(blockSize) , X(blockSize), X(blockSize));
            }
        }
    }

    public void keyPressed(KeyEvent e) {	// Check for keyboard keys
        switch (e.getKeyCode()) {
        case KeyEvent.VK_DOWN:
            down();
            break;
        case KeyEvent.VK_UP:
            turn();
            break;
        case KeyEvent.VK_RIGHT:
            right();
            break;
        case KeyEvent.VK_LEFT:
            left();
            break;
        }
    }
    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    class TimerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            repaint();	// Repaint on time increment
            if (blow(x, y + 1, blockType, turnState) == 1 && pau==0) {	// Increment y coordinate
                for(int z=0;z<FS;z++)
                {
					y = y + 1;
					delline();
				}
                
            }
            if (blow(x, y + 1, blockType, turnState) == 0) {
                if (flag == 1) {
                    add(x, y, blockType, turnState);
                    delline();
                    newblock();
                    flag = 0;
                }
                flag = 1;
            }
        }
    }
}
