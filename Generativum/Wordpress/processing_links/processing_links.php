<?php   
  
/* 
Plugin Name: Processing Links
Plugin URI: http://the-star-traveller.de 
Description: This plugin will write links in a txt-file that processing will use to generate logos and backgrounds.
Version: 0.1a
Author: Matthias Guntrum
Author URI: http://the-star-traveller.de
*/



add_action('admin_menu', 'proc_plugin_menu');

function proc_plugin_menu() {
	add_options_page('Processing Links Options', 'Processing Links', 'manage_options', 'processing_links', 'proc_plugin_options');
}

function proc_plugin_options() {
	if (!current_user_can('manage_options'))  {
		wp_die( __('You do not have sufficient permissions to access this page.') );
	}
	$successmessage = "";
	if ($_POST['hidden'] == 'Y') {
	
	$produkte =  get_page_by_title('Produkte');
	$page_childs_array=get_pages('child_of='.$produkte->ID.'&hierarchical=0&parent='.$produkte->ID.'');
	$content="";
	
	/*
		So here I do 2 queries for getting the urls from my custom texonomies
		You may change this part completly to make it work for you
		Note that the java LinkreaderII.class seperates by & 
		it expects lines like: your_categorie&http://the_link_to_the_image_of_your_category
	*/
	
		foreach($page_childs_array as $child){
			$part=($child->post_name);
			$wp_query=null;
			$myquery['post_type'] = 'imagebilder';
			$myquery['tax_query'] = array(
										array(
											'taxonomy' => 'produkte',
											'terms' => array($part),
											'field' => 'slug',
											'operator' => 'IN',
											)
										);
			$myquery['posts_per_page']= '1';
			$wp_query = new WP_Query($myquery);
			
				if ($wp_query->have_posts()) : while ($wp_query->have_posts()) : $wp_query->the_post();
				
					$image_id = get_post_thumbnail_id($post->ID);  
					$image_url = wp_get_attachment_image_src($image_id,'large');  
					$image_url = $image_url[0];
					$content=$content.$part."&".$image_url."\n";
					
				endwhile;endif;
				
			wp_reset_query();
	}
	
	$wp_query=null;
			$myquery['post_type'] = 'imagebilder';
			$myquery['tax_query'] = array(
										array(
											'taxonomy' => 'startseite',
											'terms' => array('startseite'),
											'field' => 'slug',
											'operator' => 'IN',
											)
										);
			$myquery['posts_per_page']= '1';
			$wp_query = new WP_Query($myquery);
			
				if ($wp_query->have_posts()) : while ($wp_query->have_posts()) : $wp_query->the_post();
				
					$image_id = get_post_thumbnail_id($post->ID);  
					$image_url = wp_get_attachment_image_src($image_id,'large');  
					$image_url = $image_url[0];
					$content=$content."startseite&".$image_url."\n";
					
				endwhile;endif;
				
			wp_reset_query();
	
	// this is the network command for my threaded application on the server (not on git) thait waits for this command to run the generativumIII.jar file.
	// Also possible if your server allows it: exec(path/generativumIII.jar)
	
	//<-- BEGIN The threaded server stuff replacable with exec command (Note that successmessage will stay empty)
	$dest = fopen("..\\wp-content\\plugins\\processing_links\\links.txt", 'w') ;
	fwrite ($dest,$content);
	fclose($dest);
	$fp = @fsockopen('127.0.0.1', 54290, $errno, $errstr, 2);
	
	if($errno==0){
	$successmessage ="<br> SUCCESS! Script started. This may take up to 2 minutes.\r\n";
	}else{
		$successmessage ="<br> ERROR: ".$errstr."\r\n";
		}
	
	}
	//<-- END of threaded server stuff
	echo '<div class="wrap">';
	echo '<p>Update Links for Processing Script?</p>';
	echo '<form action="'.str_replace( '%7E', '~', $_SERVER['REQUEST_URI']).'" method="post">';
	echo '<input type="hidden" name="hidden" value="Y">';
	echo '<input type="submit" value="Update">';
	echo '<br>';
	echo $successmessage;
	echo '</form>';
	echo '</div>';
	

}


?>