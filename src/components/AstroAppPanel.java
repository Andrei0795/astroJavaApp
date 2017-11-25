package components;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;


public class AstroAppPanel extends JPanel
                          implements ListSelectionListener {
    
    private JScrollPane picture;
    public  ImagePanel imgPane;
    private JList list;
    private JSplitPane splitPane;
    private static String[] categoryNames = new String[13];
    public static ArrayList<CategoryObject> categoriesArray; 
    
    public AstroAppPanel() {
        
        //Initial array is empty
        categoriesArray = new ArrayList<CategoryObject>();
        //Connect to DB in a syncronous way
        connectToDB();


        //Create the list of images and put it in a scroll pane.
        list = new JList(categoryNames);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        JScrollPane listScrollPane = new JScrollPane(list);
        
        //Add the first image to have something on the right pane
        try {
                BufferedImage img = ImageIO.read(getImageURL("images/" + categoriesArray.get(0).imagePath + ".jpg"));
                imgPane = new ImagePanel(img);
                picture = new JScrollPane(imgPane);
            } 
            catch (IOException ex) {
                Logger.getLogger(AstroAppPanel.class.getName()).log(Level.SEVERE, null, ex);
            }        
        
        
        //Create a split pane with the two scroll panes in it.
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                   listScrollPane, picture);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        //Provide minimum sizes for the two components in the split pane.
        Dimension minimumSize = new Dimension(100, 50);
        listScrollPane.setMinimumSize(minimumSize);
        picture.setMinimumSize(minimumSize);

        //Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(800, 600));
        updateLabel(categoriesArray.get(list.getSelectedIndex()).imagePath);
    }
    
    //Listens to the list
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList)e.getSource();
        updateLabel(categoriesArray.get(list.getSelectedIndex()).imagePath);
    }
    
    //Renders the selected image
    protected void updateLabel (String name) {
        
                try {
                    BufferedImage img = ImageIO.read(getImageURL("images/" + name + ".jpg"));
                    imgPane.changeImg(img);
                    picture.setViewportView(imgPane);
                } catch (IOException ex) {
                    Logger.getLogger(AstroAppPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
        
    }

    //Used by AstroAppMain
    public JList getImageList() {
        return list;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

   
    /** Returns an URL for the image, or null if the path was invalid. */
    protected static URL getImageURL(String path) {
       java.net.URL imgURL = AstroAppPanel.class.getResource(path);
        if (imgURL != null) {
            return imgURL;
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("AstroAppPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AstroAppPanel splitPaneDemo = new AstroAppPanel();
        frame.getContentPane().add(splitPaneDemo.getSplitPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Connects to DB and also creates the required objects and puts them into the categories array
     * It is a synchronized method
     */
    private synchronized static void connectToDB() {
        System.out.println("ENTERED");

        Connection c = null;
        Statement stmt = null;
        try {
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection("jdbc:sqlite:AstroApp.db");
           c.setAutoCommit(false);
           //System.out.println("Opened database successfully");

           stmt = c.createStatement();
           ResultSet rs = stmt.executeQuery("select * from CATEGORIES;");

           while ( rs.next() ) {
             int id = rs.getInt("ID");
             String name = rs.getString("NAME");
             String description = rs.getString("DESCRIPTION");
             int numberOfStars = rs.getInt("NUMBEROFSTARS");
             int messierObjects = rs.getInt("MESSIEROBJECTS");
             String brightestStar = rs.getString("BRIGHTESTSTAR");
             String imageName = rs.getString("IMAGENAME");
             String bestVisibility = rs.getString("BESTVISIBILITY");

             CategoryObject category = new CategoryObject(name, id, description, numberOfStars, 
                                                          brightestStar, imageName, messierObjects, bestVisibility);
             categoriesArray.add(category);
             
           }
           
             //Put all the stars in each category object
             for (int i = 0; i < categoriesArray.size(); i++) {
                categoryNames[i] = categoriesArray.get(i).name;
                stmt = c.createStatement();
                rs = stmt.executeQuery("select * from STARS where CATEGORYID="+ categoriesArray.get(i).id +";");
                
                while ( rs.next() ) {
                    int id = rs.getInt("ID");
                    String name = rs.getString("NAME");
                    int categoryId = rs.getInt("CATEGORYID");
                    String categoryName = rs.getString("CATEGORYNAME");
                    String latinName = rs.getString("LATINNAME");
                    Double magnitude = rs.getDouble("MAGNITUDE");
                    int centerX = rs.getInt("CENTERX");
                    int centerY = rs.getInt("CENTERY");

                    StarObject star = new StarObject(name, id, categoryId, categoryName, 
                                                                 latinName, magnitude, centerX, centerY);
                    categoriesArray.get(i).arrayOfStars.add(star);

                  }
                
            }

           rs.close();
           stmt.close();
           c.close();
        } catch ( Exception e ) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
                
    }

      
}
