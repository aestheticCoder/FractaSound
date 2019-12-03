package anotherTesta;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

// http://dbc-projects.net/java-sound-card-oscilloscope/
public class javaScope extends JFrame
{
	private static final long serialVersionUID = 1L;
	private float SAMPLE_RATE = 44100;
	private float SCREEN_WIDTH = 250;

	private static final int LINE_BUFF_SIZE = 10800;
	private static final int READ_BUFF_SIZE = LINE_BUFF_SIZE/2;
	private static final int SAMPLE_SIZE = READ_BUFF_SIZE/2;
	
	private JPanel contentPane;
	private JButton btnStartStop;
	private JButton btnExit;
	private JComboBox<String> cbSweepTime;
	private dispPanel canvas;
	
	private TargetDataLine inLine;
	private SourceDataLine outLine;
		
	private boolean running = false;
	private float dataBuff[] =  new float[SAMPLE_SIZE];
	private BufferedImage gridBuff = null;
	private float samplesPerPix;
	private int sampSize;
	private JLabel lblSecPerDiv;
	private JLabel lblSweepTime;
	private JCheckBox chckbxLoop;
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					javaScope frame = new javaScope();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public javaScope()
	{	
		try
		{
			gridBuff = ImageIO.read(getClass().getResource("res/grid.bmp"));		    
		} 
		catch (IOException e)
		{
			System.out.println("Can't find grid image");
		}
		samplesPerPix = (float)((10e-3 * SAMPLE_RATE)/SCREEN_WIDTH);
		sampSize = (int)(10e-3 * SAMPLE_RATE);
		for (int i = 0; i < SAMPLE_SIZE; i++) dataBuff[i] = 0.0f;

		setSize(new Dimension(570, 400));
		setTitle("Java Oscilloscope");		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		canvas = new dispPanel();
		canvas.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				
		canvas.setBackground(Color.WHITE);
		canvas.setSize(new Dimension(250, 250));
		canvas.setBounds(102, 37, 250, 250);
		contentPane.add(canvas);
		
		btnStartStop = new JButton("Start");
		btnStartStop.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (btnStartStop.getText().equals("Start"))
				{
					running = true;
					btnStartStop.setText("Stop");
					captureAudio();
				}
				else
				{
					running = false;
					btnStartStop.setText("Start");
				}
			}
		});
		btnStartStop.setBounds(409, 198, 63, 23);
		contentPane.add(btnStartStop);
		
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);
			}
		});
		btnExit.setBounds(409, 239, 63, 23);
		contentPane.add(btnExit);
		
		lblSecPerDiv = new JLabel("Time Base: 1mS/div");
		lblSecPerDiv.setBounds(158, 298, 138, 14);
		contentPane.add(lblSecPerDiv);
		
		cbSweepTime = new JComboBox<String>();
		
		cbSweepTime.setBounds(409, 116, 79, 23);
		cbSweepTime.addItem("50mS");
		cbSweepTime.addItem("20mS");
		cbSweepTime.addItem("10mS");
		cbSweepTime.addItem("5mS");
		cbSweepTime.addItem("2mS");
		cbSweepTime.addItem("1mS");
		cbSweepTime.setSelectedItem("10mS");
		contentPane.add(cbSweepTime);
		cbSweepTime.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String strSweepTime = (String) cbSweepTime.getSelectedItem();
				if (strSweepTime.equals("50mS"))
				{
					samplesPerPix = (float)((50e-3 * SAMPLE_RATE)/SCREEN_WIDTH);
					sampSize = (int)(50e-3 * SAMPLE_RATE);
					lblSecPerDiv.setText("Timebase: 5mS/Div");
				}
				if (strSweepTime.equals("20mS"))
				{
					samplesPerPix = (float)((20e-3 * SAMPLE_RATE)/SCREEN_WIDTH);
					sampSize = (int)(20e-3 * SAMPLE_RATE);
					lblSecPerDiv.setText("Timebase: 2mS/Div");
				}
				if (strSweepTime.equals("10mS"))
				{
					samplesPerPix = (float)((10e-3 * SAMPLE_RATE)/SCREEN_WIDTH);
					sampSize = (int)(20e-3 * SAMPLE_RATE);
					lblSecPerDiv.setText("Timebase: 1mS/Div");
				}
				if (strSweepTime.equals("5mS"))
				{
					samplesPerPix = (float)((5e-3 * SAMPLE_RATE)/SCREEN_WIDTH);
					sampSize = (int)(5e-3 * SAMPLE_RATE);
					lblSecPerDiv.setText("Timebase: 0.5mS/Div");
				}
				if (strSweepTime.equals("2mS"))
				{
					samplesPerPix = (float)((2e-3 * SAMPLE_RATE)/SCREEN_WIDTH);
					sampSize = (int)(2e-3 * SAMPLE_RATE);
					lblSecPerDiv.setText("Timebase: 0.2mS/Div");
				}
				if (strSweepTime.equals("1mS"))
				{
					samplesPerPix = (float)((1e-3 * SAMPLE_RATE)/SCREEN_WIDTH);
					sampSize = (int)(20e-3 * SAMPLE_RATE);
					lblSecPerDiv.setText("Timebase: 0.1mS/Div");
				}
			}
		});
		
		lblSweepTime = new JLabel("Sweep Time");
		lblSweepTime.setBounds(409, 101, 79, 14);
		contentPane.add(lblSweepTime);
		
		chckbxLoop = new JCheckBox("Loop audio");
		chckbxLoop.setSelected(true);
		chckbxLoop.setBounds(409, 289, 97, 23);
		contentPane.add(chckbxLoop);
		
	}
	
	private void captureAudio()
	{
		try
	    {
			/*
	    	//TODO: TESTBLOCK
			Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
			Mixer localMix = AudioSystem.getMixer(mixerInfo[0]);
			*/

			// Set up audio input
			final AudioFormat format = getFormat();
			DataLine.Info inInfo = new DataLine.Info(TargetDataLine.class, format);	      
			inLine = (TargetDataLine) AudioSystem.getLine(inInfo);
			inLine.open(format, LINE_BUFF_SIZE);
			inLine.start();

			// Set up audio output
			 DataLine.Info outInfo = new DataLine.Info(SourceDataLine.class, format);
			 outLine = (SourceDataLine) AudioSystem.getLine(outInfo);
			 outLine.open(format, LINE_BUFF_SIZE);
			 outLine.start();
			 
			 Runnable runner = new Runnable()
			 {
				 public void run()
				 {
					 byte inBuffer[] = new byte[READ_BUFF_SIZE];
					 byte outBuffer[] = new byte[READ_BUFF_SIZE];
					 int buffSize = inBuffer.length;
					 
					 while (running)
					 {
						 int bytesAvailable = inLine.available();
						 if (bytesAvailable >= READ_BUFF_SIZE)
						 {
							 int bytesRead = inLine.read(inBuffer, 0, buffSize);
							 // Convert  to floating point
							 for(int i = 0; i < buffSize; i += 2)
								 dataBuff[i/2] = ((inBuffer[i] & 0xFF)|(inBuffer[i + 1] << 8)) / 32768.0F;
						
							 // Convert back to PCM
							 // This section converts from floating point back to signed PCM
							 // just to prove that its possible to do a bi of processing in Java.
							 if (chckbxLoop.isSelected())
							 {
								 for (int i = 0; i < buffSize; i += 2)
								 {
									 // Saturation
									 float fSample = dataBuff[i/2];
									 fSample = Math.min(1.0F, Math.max(-1.0F, fSample));
									 // Scaling and conversion to integer
									 int nSample = Math.round(fSample * 32767.0F);
									 outBuffer[i+1] = (byte) ((nSample >> 8) & 0xFF);
									 outBuffer[i] = (byte) (nSample & 0xFF);
								 }
							 }
							 else
							 {
								 for (int i = 0; i < buffSize; i++)
								 {
									 outBuffer[i] = 0;
								 }
							 }
							 outLine.write(outBuffer, 0, bytesRead);
							 canvas.repaint();							 
						 }
					 }
					 inLine.flush();
					 inLine.stop();
					 inLine.close();
					 outLine.flush();
					 outLine.stop();
	    			 outLine.close();
				 }				  
			 };
	      
			 Thread captureThread = new Thread(runner);
			 captureThread.start();
	    } 
		catch (LineUnavailableException e)
		{
	      System.err.println("Line unavailable: " + e);
	      System.exit(-2);
	    }
	}
	
	private AudioFormat getFormat()
	 {
		 int sampleSizeInBits = 16;
		 int channels = 1;
		 int frameSize = 2;
		 int frameRate = (int)SAMPLE_RATE;
		 boolean bigEndian = false;
		 return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				 				SAMPLE_RATE,
				 				sampleSizeInBits,
				 				channels,
				 				frameSize,
				 				frameRate,
				 				bigEndian);
	 }
	
	public class dispPanel extends JPanel
	{
		
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g)
		{
			Graphics2D g2;
			
			super.paintComponent(g);
			g2 = (Graphics2D) g;
			drawScreen(g2);  			
		}
	}
	private void drawScreen(Graphics2D g2)
	{
		BufferedImage dispBuff = new BufferedImage(canvas.getWidth(),
												   canvas.getHeight(),
												   BufferedImage.TYPE_INT_RGB);
		Graphics2D g2dScreen = dispBuff.createGraphics();
		g2dScreen.drawImage(gridBuff,null,0,0);
		
		// Find trigger point
		int trig = 0;
		float trigLevel = 0.05f;
		float upperTrigLevel = trigLevel + 0.02f;
		float lowerTrigLevel = trigLevel - 0.005f;
	
		if (running)
		{
			for (int i = 1; i < SAMPLE_SIZE - 1; i++)
			{
				// If signal crosses the trigger point
				if ((dataBuff[i] >= lowerTrigLevel) &&
					(dataBuff[i] <= upperTrigLevel))
				{
					// Check slope
					if ((dataBuff[i-1] < dataBuff[i]) &&
						(dataBuff[i+1] > dataBuff[i]))				    
				    {
						trig = i;
				    }
				}
				if (trig != 0) break;
			}
		}
				
		g2dScreen.setPaint(Color.green);
		g2dScreen.setStroke(new BasicStroke(2));
		Point2D.Float ptOldPoint = new Point2D.Float();
		Point2D.Float ptNewPoint = new Point2D.Float();
		
		ptOldPoint.setLocation(0, 125 - (int)(dataBuff[trig] * 255));
	
		for (int i = 0; i < sampSize; i++)
		{
			ptNewPoint.x = (int)((float)i/samplesPerPix);
			ptNewPoint.y = 125 - (int)(dataBuff[trig+i] * 255);
			g2dScreen.draw(new Line2D.Float(ptOldPoint, ptNewPoint));
			ptOldPoint.setLocation(ptNewPoint);
		}

		g2.drawImage(dispBuff, null, 0, 0);
		g2dScreen.dispose();
	}
}