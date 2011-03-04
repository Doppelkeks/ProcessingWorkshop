DESCRIPTION

The processing_links plugin will bridge the gap between wordpress and processing.

The plugin will write a .txt file with links to images and their belonging categories. It will then trigger a network command to the server for running a jar File wich will execute some Processing script. This script generates logos and backgrounds grounded on the images specified by the path of the txt file.

Note that you could also use the exec php function if your server supports it which mine doesn't. In my case there was the need of a threaded program which runs all day waiting for a network command to trigger the script server sided.


INSTALLATION

put processing_links in your wp-content/plugins folder