package generativumiii;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class GenbackgroundII{
	//PApplet - we need this to get processing functions and a class connection
	PApplet parent;
	
	//HEIGHT,WIDTH used for resizing (usually the given image size)
	private static int height;
	private static int width;
	//path and filename for the generated image
	private static String output_path;
	private static String output_data;
	
	//PGraphics object for the generated image
	PGraphics pimg;
	//PGraphics object for the vignette (ellipse as mask)
	PGraphics mask;
	//PGraphics object where things get combined and saved
	PGraphics stage;
	//the given image
	PImage img;
	
	//UID
	private static final long serialVersionUID = 1L;
	
	//the lovely constructor
	public GenbackgroundII(PApplet p,String folder){
		parent = p;
		output_path=folder;
	}
	
	public void setImg(String link){
		//load the image given by the link
		img=parent.loadImage(link);
		//width and eight will be specified by image size
		height=img.height;
		width=img.width;
		//intizialize the multiple canvas.
		pimg = parent.createGraphics(width,height,PConstants.P2D);
		mask = parent.createGraphics(width,height,PConstants.P2D);
		stage = parent.createGraphics((int)(width/2),(int)(height/2),PConstants.P2D);
	}
	
	public void setImgName(String name){
		output_data=name+"_bg.png";
	}
	
	//FUNCTION THAT WILL BE CALLED IN PARENT CLASS
	public void createbg() {
		  
		  PImage canvas;  
		  //addational value
		  float r=1;
		  
		  //MASKE erstellen (Vignette)
		  // begindraw and endraw are needed for pgraphics to work properly
		  mask.beginDraw();
		  //make a black background
		  mask.background(0);
		  //center the anchorpoint
	      mask.shapeMode(PConstants.CENTER);
		  //make a white ellipse half as large as the canvas
	      mask.fill(255);
		  mask.ellipse(width/2, height/2, width/1.3f, height/1.3f);
		  //resize for better blur compability (prevent crashes)
		  mask.resize(width/5, height/5);
		  mask.filter(PConstants.BLUR, 19);
		  //restire full size. this will blur the image even further
		  mask.resize(width/2, height/2);
		  mask.endDraw();
		  
		  
		  //CREATE Picture
		  pimg.beginDraw();
		  //make a black background
		  pimg.background(0);

		  	//this will generate bubbles specified by the image (NOT FINISHED)
		    for (int i = 0; i < img.width; i+=parent.random(200)){
				for (int j = 0; j < img.height; j+=parent.random(200)){
				pimg.fill(img.get(i, j));
				pimg.noStroke();
				pimg.shapeMode(PConstants.CENTER);
				pimg.ellipse(i+1,j+1,r,r);
				r=parent.random(0.2f,200);
				}
				r=parent.random(0.2f,200);
				}
		    
		    //resize for better blur compability (prevent crashes)
		    pimg.resize(width/5, height/5);
		    pimg.filter(PConstants.BLUR, 9);
		    
		    //create a temp canvas to use te mask
		    canvas = pimg.get();
		    canvas.filter(PConstants.BLUR, 15);
		    canvas.resize(width/2, height/2);
		    
		    //apply mask on the generated image
		    pimg.resize(width/2, height/2);
		    pimg.image(canvas, 0, 0);
		    pimg.mask(mask.get());
		    pimg.endDraw();
		    
		    //set the transparent image on a black background
		    //save the vignetted image
		    stage.beginDraw();
		    stage.image(pimg.get(),0,0);
		    stage.endDraw();
		    stage.save(output_path+output_data);	 
	}


}

