
package components;

/**
 *
 * @author andrei95
 */
public class StarObject {
    
    public String name;
    public int id;
    public int categoryId;
    public String cateogryName;
    public String latinName;
    public double magnitude;
    public int centerX;
    public int centerY;
    
    public StarObject(String name, int id, int categoryId, String categoryName,
                      String latinName, double magnitude, int centerX, int centerY){
        this.name = name;
        this.id = id;
        this.categoryId = categoryId;
        this.cateogryName = categoryName;
        this.latinName = latinName;
        this.magnitude = magnitude;
        this.centerX = centerX;
        this.centerY = centerY;
    }
    
    public String getDescription() {
        String description;
        description = "<html>"
                    + String.format("Name: \t %s", this.name) + "<br><br>"
                    + String.format("Constellation: \t %s", this.cateogryName) + "<br><br>"
                    + String.format("Latin Name: \t %s", this.latinName) + "<br><br>"
                    + String.format("Magnitude: \t %f", this.magnitude) + "<br><br>"
                    + "</html>";
        return description;
    }
    
    //Check mouse coordinates in image against the coordinats of the star in the image
    //Coordinates for the star were manually inserted
    public boolean hasUserClickedStar(int mouseX, int mouseY){
        
        double radius = 20;
        double distancesquared = (mouseX - this.centerX) * (mouseX - this.centerX) 
                               + (mouseY - this.centerY) * (mouseY - this.centerY);
        return distancesquared <= radius * radius;  
    }
    
}
