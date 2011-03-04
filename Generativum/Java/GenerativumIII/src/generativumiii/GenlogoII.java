package generativumiii;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class GenlogoII{
	//PApplet - we need this to get processing functions and a class connection
	PApplet parent;
	
	//Path to put the generated Logo
	private static String outputpath;
	//Name and Type (.gif | .jpeg | .tga | .png)
	private static String output_data;
	
	//Variables Logo
	//STREIFEN Array -> single block sizes
	private static int blocks[];
	//thickness of the groundline
	private static int bold;
	//length of gradient
	private static int verlauf;
	//Maximum alpha (0-255 | 255=100% | 0=0%)
	private static int alphamax;
	//Minimum brightness of the picked colors
	private static int brightness;
	//Color Treshold
	private static int treshold;
	//How many tries to find a different (random) color [used for brightness and treshold scheck]
	private static int tries_max;
	
	//UID (never mind)
	private static final long serialVersionUID = 1L;
	
	//SETUP
	// Our canvas we will draw in (allows transparency)
	PGraphics bg;
	// The image object we will pick colors from
	PImage bild;
	//some general stuff we need | r,g,b will represent the current picked colors
	int width,height,r,g,b;

	
	
	/*the lovely constructor
	 *NEEDS A IMAGE PATH (li), A FOLDER PATH (folder) AND PAPPLET (p) OF THE PARENT CLASS
	 *NOTICE: Paths can be system,url, relative, absolute
	 */
	public GenlogoII(PApplet p,String folder){
		parent=p;
		
		outputpath=folder;
		//Standard block sizes as measured from the given design
		blocks=new int[]{11,14,13,12,15,19,7,12,14,19,14,13,18,11,12};
		//Standard baseline thickness
		bold=4;
		//Standard gradient length
		verlauf=28;
		//standard min alpha value
		alphamax=125;
		//standard min brightness value
		brightness=20;
		//standard color
		treshold=50;
		//Tries
		tries_max=50;
		
		//the width of the image is defined by all block sizes together
		width=sum(blocks);
		//height is defined by gradient and baseline thickness
		height=verlauf+bold;
		//PGraphics: The canvas we will draw in gets created
		bg = parent.createGraphics(width,height,PConstants.P2D);
		
	}
	
	public void setImg(String link){
		//load the image given by the link
		bild=parent.loadImage(link);
	}
	
	public void setImgName(String name){
		output_data=name+"_header.png";
	}
	
	//FUNCTION WHICH CREATES THE LOGO (IT CAN BE DRAWN UP- OR DOWNWARDS UP=true DOWN=false)
	public void createLogo(boolean up){
		
		//temp variables
		int alpha=alphamax;
		int constant=(int) Math.ceil((float)alpha/(float)verlauf);
		int farbe[]=null;
		int counter=0;
		
		// we need to call loadpixels for our canvas to work properly
		bg.loadPixels();
		//LOOP ASLONG AS THERE ARE STREIFEN
		for(int i=0; i<blocks.length; i++){
			//get a random but unique color with a certain brightness
			farbe=getRandomPixel(farbe);
			//LOOP ASLONG AS THE WIDTH OF A SINGLE STREIFEN IS NOT REACHED
			for(int x=0; x<blocks[i]; x++){
				//UPWARDS
				if(up!=true){
				for(int y=0; y<height; y++){
					// Draw the baseline line
					if(y<bold){
						// Give pixels the picked color and corresponding alpha value
						bg.pixels[y*width+counter+x]=parent.color(farbe[0],farbe[1],farbe[2]);
					}
					//baseline has been drawn
					else{
						// Countdown the alpha value
						alpha=alpha-constant;
						// if alpha would become a minus value [because of ceil]
						if(alpha<0)
							alpha=0;
						// Give pixels the picked color and corresponding alpha value
						bg.pixels[y*width+counter+x]=parent.color(farbe[0],farbe[1],farbe[2],alpha);
					}
				}
				}
				//DOWNWARDS
				else{
				for(int y=height-1; y>0; y--){
					// Draw the opaque line
					if(y>height-bold){
						bg.pixels[y*width+counter+x]=parent.color(farbe[0],farbe[1],farbe[2]);
					}else{
						// Countdown the alpha value
						alpha=alpha-constant;
						// if alpha would become a minus value [because of ceil]
						if(alpha<0)
							alpha=0;
						// Give pixels the picked color and corresponding alpha value
						bg.pixels[y*width+counter+x]=parent.color(farbe[0],farbe[1],farbe[2],alpha);
					}
				}
				}
			//reset alpha
			alpha=alphamax;
			
			}
			//Count streifen for correct positioning
			counter=counter+blocks[i];
		}
		// updates the drawn pixels to our canvas object
		bg.updatePixels();
		// saves the generated logo to the specified path
		bg.save(outputpath+output_data);
	}
	
	// This function will get new colors and prevent the algorythm from taken already chosen ones
	public void fixColor(int[] ref){
		//will get a new random color (sets r,g,b)
		getColor();
		//will test if the picked color is within a certain threshold. if yes, it will try to find a new one
		colorTresh(ref);
	}
	
	//The main function for getting a random color
	//It will also check if the chosen color is bright enough
	public int[] getRandomPixel(int[] ref){
		
		fixColor(ref);
		int tries=0;
		
		//Brightness-check
		while(	tries < tries_max &&
				r < brightness &&
				g < brightness &&
				b < brightness  ){
			
			tries++;
			fixColor(ref);
		}
		
		//returns the color as an int array (for checking purposes)
		int array[]={r,g,b};
		return array;
	}
	
	//this will get a random color as sperate channels (r,g,b)
	public void getColor(){
		
		int farbe=bild.get((int)parent.random(bild.width),(int)parent.random(bild.height));
		
		//fucking binaries
		r=parent.color(farbe) >> 16 & 0xFF;
		g=parent.color(farbe) >> 8 & 0xFF;
		b=parent.color(farbe) & 0xFF;
	}
	
	/* this checks if the current color lies within a color threshold
	 * if yes, it tries to find a new one
	 * this is loop based to ensure that the newly generated color is outside the treshold
	 */
	public void colorTresh(int [] ref){
		//The first entry will likely be a null
		if(ref != null){
			
			int r_2=ref[0];
			int g_2=ref[1];
			int b_2=ref[2];
			int tries = 0;
			
			while(	tries < tries_max &&
					Math.abs(r-r_2)<treshold &&
					Math.abs(g-g_2)<treshold &&
					Math.abs(b-b_2)<treshold){
				tries++;
				System.out.println(tries);
				getColor();
			}
			
		}
	}
	
	//this simply sums up the values of a given int array
	public int sum(int array[]){
		int ergebnis=0;
		for(int i = 0; i<array.length; i++)
			ergebnis=ergebnis+array[i];
		return ergebnis;
	}
	
}




