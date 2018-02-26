import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;


public class Main {

    public static void main(String[] args) {
        System.out.println("Starting...");
        String filename = args[0];// file path
        if (args[0].matches("help")) {
            System.out.println("Tool to collect all files from your RekordBox collection and copy them to into one directory.\n" +
                    "Finally it rewrites the database xml file.\n\n" +
                    "Usage: java -jar Rekordbox_Database_Remake <rekordboxdatabase.xml> <finaldirectory> <savedirectory>\n" +
                    "Example: java -jar Rekordbox_Database_Remake C:\\Users\\foo\\savedrekordboxdatabase.xml C:\\Users\\foo\\RekordBoxMusic D:\\RekordboxBackup\n" +
                    "\n [rekordboxdatabase.xml] Path to the the exported database of rekordbox\n" +
                    "\n [finaldirectory] Path to the Directory where the new database.xml should point.\n" +
                    "\n [savedirectory] Path to the directory where it will store your Music for example an external HDD\n" +
                    "The new database.xml can be found in the folderpath\n\n" +
                    "\n Issues:\n\n" +
                    "Before starting the program make sure your RekordBox collection is consistent and there are no missing Tracks otherwise it will fail \n "

            );

        } else {
            String new_dir = args[1] + "/RekordboxMusic";
            new_dir = new_dir.replaceAll("[\\\\]", "/");

            String save_dir = args[2] + "/RekordboxMusic";
            save_dir = save_dir.replaceAll("[\\\\]", "/");

            try {
                // Build the document with SAX and Xerces, no validation
                SAXBuilder builder = new SAXBuilder();
                // Create the document
                Document doc = builder.build(new File(filename));

                Element root = doc.getRootElement();
                Element collection = root.getChild("COLLECTION");

                List<Element> tracks = collection.getChildren();
                List<String> existing_files = new LinkedList<String>();

                String location;
                String save_location;
                String artist;
                String album;
                String name; //filename
                int filename_start;
                int filename_end;
                String type; //filetype
                int filetype_start;
                int filetype_end;

                String source_helper;

                String new_location;

                int counter = 0;
                for (Element track : tracks) {
                    counter += 1;

                    location = track.getAttributeValue("Location");

                    artist = track.getAttributeValue("Artist");
                    if (artist.matches("")) {
                        artist = "Unknown Artist";
                    }
                    artist = artist.replaceAll("[^a-zA-Z0-9()&!]", "_");

                    album = track.getAttributeValue("Album");
                    if (album.matches("")) {
                        album = "Unknown Album";
                    }
                    album = album.replaceAll("[^a-zA-Z0-9()&!]", "_");

                    filename_start = location.lastIndexOf("/") + 1;
                    filename_end = location.lastIndexOf(".");

                    filetype_start = filename_end + 1;
                    filetype_end = location.length();

                    name = location.substring(filename_start, filename_end).replaceAll("%20", "_");
                    name = name.replaceAll("[^a-zA-Z0-9()!]", "_");
                    type = location.substring(filetype_start, filetype_end);

                    new_location = "file://localhost/" + new_dir + "/" + artist + "/" + album + "/" + name + "." + type;
                    save_location = "file://localhost/" + save_dir + "/" + artist + "/" + album + "/" + name + "." + type;

                    track.setAttribute("Location", new_location);

                    //System.out.println("Location: " + location + " New Loc: "+new_location);

                    //preparing the file for URI
                    source_helper = location.replace("localhost", "");
                    source_helper = source_helper.replaceAll("#", "%23");//Hashtag support

                    Path source = Paths.get(URI.create(source_helper));
                    Path target = Paths.get(URI.create(save_location.replace("localhost", "")));

                    Path parentDir = target.getParent();
                    if (!Files.exists(parentDir))
                        Files.createDirectories(parentDir);

                    try {
                        System.out.println("Copying file:" + " (" + counter + "/" + tracks.size() + ") " + target.toString());
                        Files.copy(source, target, COPY_ATTRIBUTES);
                    } catch (FileAlreadyExistsException e) {
                        System.out.println("File: " + target.toString() + " already exists skipping it... ");
                        existing_files.add("File: " + target.toString() + " exists. TrackID: "+ track.getAttributeValue("TrackID") + "\n");
                    }
                }

                // Output the document, use standard formatter
                OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(args[2] + "\\RekordBoxMusic\\" + "rekordboxdatabase.xml"), StandardCharsets.UTF_8);
                XMLOutputter fmt = new XMLOutputter();

                fmt.output(doc, fw);

                FileWriter log_writer = new FileWriter(args[2] + "\\RekordBoxMusic\\" + "log.txt");
                for (String entry : existing_files) {
                    try {
                        log_writer.write(entry);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }


                }
                System.out.println("Successful reorganized");
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    }
}
