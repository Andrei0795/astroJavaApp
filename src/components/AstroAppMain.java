package components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class AstroAppMain extends JFrame
                            implements ListSelectionListener {
    private JLabel label;
    //This is the same object as the one in the AstroAppPanel class
    //We need it for events and when we want to change the text
    private ArrayList<CategoryObject> categoriesArray; 
    
    //This is the AstroPanelObject - it does the image work, DB work and other
    final AstroAppPanel splitPaneDemo = new AstroAppPanel();
    
    //The mouse adapter listens to changes in the image
    private MouseAdapter mouseAdapter;
    
    //This category index is needed to change text when a new category is selected
    private int selectedCategory;
    
    public AstroAppMain() {
        super("AstroApp");

        
        JSplitPane top = splitPaneDemo.getSplitPane();
        this.categoriesArray = splitPaneDemo.categoriesArray;
        
        splitPaneDemo.getImageList().addListSelectionListener(this);
        
        
        //Add action listener to image pane in the other object class
        mouseAdapter = new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {

                            Point panelPoint = e.getPoint();
                            Point imgContext = splitPaneDemo.imgPane.toImageContext(panelPoint);

                            //report.setText("You clicked at " + panelPoint + " which is relative to the image " + imgContext);
                            for(StarObject star:categoriesArray.get(selectedCategory).arrayOfStars){
                                
                                if(star.hasUserClickedStar(imgContext.x, imgContext.y)) {
                                   System.out.println(star.name + " = name"); 
                                   changeLabel(star.getDescription());

                                }
                                
                                
                            }   
                            
                        }
                        
                    };
        splitPaneDemo.imgPane.addMouseListener(mouseAdapter);

        //XXXX: Bug #4131528, borders on nested split panes accumulate.
        //Workaround: Set the border on any split pane within
        //another split pane to null. Components within nested split
        //panes need to have their own border for this to work well.
        top.setBorder(null);

        //Create a regular old label
        label = new JLabel(categoriesArray.get(0).getDescription(),
                           JLabel.CENTER);

        //Create a split pane and put "top" (a split pane)
        //and JLabel instance in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                              top, label);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(500);

        //Provide minimum sizes for the two components in the split pane
        top.setMinimumSize(new Dimension(100, 50));
        label.setMinimumSize(new Dimension(100, 30));

        //Add the split pane to this frame
        getContentPane().add(splitPane);
    }
    

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;

        JList theList = (JList)e.getSource();
        if (theList.isSelectionEmpty()) {
            label.setText("Nothing selected.");
        } else {
            int index = theList.getSelectedIndex();
            selectedCategory = index;
            label.setText(categoriesArray.get(index).getDescription());
        }
    }
    
    //Change the text of the label according to the category or star clicked
    public void changeLabel(String s) {
        label.setText(s);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new AstroAppMain();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
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
