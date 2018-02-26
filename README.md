# Rekordbox_Database_Remake
Collects your rekordbox collections files and stores them in one directory. After that it rebuilds the database.xml. Perfect for moving your rekordbox to a new PC and the your mp3 files are everywhere around in your filesystem.
## Usage:
# 1. make a backup of your Collection
<code> java -jar Rekordbox_Database_Remake rekordboxdatabase.xml finaldirectory savedirectory </code>
* [rekordboxdatabase.xml] Path to the the exported database of rekordbox
* [finaldirectory] Path to the Directory where the new database.xml should point.
* [savedirectory] Path to the directory where it will store your Music for example an external HDD
## Example:
<code> java -jar Rekordbox_Database_Remake C:\Users\foo\savedrekordboxdatabase.xml C:\Users\foo\RekordBoxMusic D:\RekordboxBackup </code> 

The new database.xml can be found in the savedirectory
## known bugs:
* Before starting the program make sure your RekordBox collection is consistent and there are no missing Tracks otherwise it will fail 
* only tested with Windows10
