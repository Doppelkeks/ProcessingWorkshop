package generativumiii;

import processing.core.PApplet;


public class GenerativumIII extends PApplet {

	/*
	 * Finally the class where all comes together!
	 * This class will make use of Genlogo, Genbackground and Linkreader
	 * 
	 * Lnkreader will read out a txt file and get the needed links for the source images
	 * Genlogo and Genbackground will use the links to generate logos and backgrounds for different categories
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	//The String Array in which we will store all links
	private String[] links;

	//Folder on where to store generated logos and backgrounds
	private static final String folder_output="F:\\xampp\\xampp\\htdocs\\wordpress_dev\\wp-content\\themes\\sBasis\\images\\";
	//Folder on where to find the tet file that is needed by the Linkreader class
	private static final String folder_links="F:\\xampp\\xampp\\htdocs\\wordpress_dev\\wp-content\\plugins\\processing_links\\links.txt";

	//Called Objects
	LinkreaderII query;
	GenlogoII logo;
	GenbackgroundII bg;
	
	public void setup() {
		//We only want to do it once
		noLoop();
		
		query = new LinkreaderII(this,folder_links);
		//Get all links stored in the .txt file
		links=query.getLinks();
		logo = new GenlogoII(this,folder_output);
		bg = new GenbackgroundII(this,folder_output);
	}

	public void draw() {
		try{
		//GENERATE IMAGES FOR ALL LINKS
			for(int i=0; i<links.length; i++){
			// Where to find the image used for processing
			String img_url = query.getImageUrl(links[i]);
			// Name for the generated image
			String output_name = query.getImageName(links[i]);
			
			//Print to console
			System.out.println("NAME: "+output_name+" | LINK:"+img_url);
			
				logo.setImg(img_url);
				logo.setImgName(output_name);
				logo.createLogo(true);
				
				//Print logo success message to console
				System.out.println("Logo created.");
			
				bg.setImg(img_url);
				bg.setImgName(output_name);
				bg.createbg();
				
				//Print BG success message to console
				System.out.println("BG created.");
				
		}
		
		//Exit the program after it finished
		this.exit();
		}catch(Exception e){
			System.out.println("Something went wrong. Update Links?");
			this.exit();
		}
		
	}
		
	public static void main(String _args[]) {
		PApplet.main(new String[] { generativumiii.GenerativumIII.class.getName() });
	}
}
