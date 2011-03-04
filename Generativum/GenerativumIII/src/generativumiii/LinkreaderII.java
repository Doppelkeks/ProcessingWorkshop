package generativumiii;

import java.io.File;

import processing.core.PApplet;

public class LinkreaderII{
	PApplet parent;
	private String path=null;
	
	/*
	 * This class will read out a .txt file specified by a given url. 
	 * To keep things simple the txt file will consist of a name and url separated by &
	 * 
	 * Eample:
	 * anzuge&http://localhost/wordpress_dev/wp-content/uploads/2011/01/jacken_platzh.jpg
	 * 
	 * The name will be used inside the Genlogo and Genbackground classes to generate images with an
	 * accordingly named image.
	 * 
	 * Example:
	 * anzuge will become: anzuge_logo.png and anzuge_bg.png
	 * this will be of important value for the wordpress theme,
	 * since it will call the images by their page names.
	 * 
	 * 
	 */
	
	//The lovly constructor
	public LinkreaderII(PApplet p,String url){
		parent=p;
		path=url;
	}

	public String[] getLinks(){
		String lines[] = PApplet.loadStrings(new File(path));
		return lines;
	}
	
	public String getImageUrl(String line){
		return line.split("&")[1];
	}
	
	public String getImageName(String line){
		return line.split("&")[0];
	}

}
