
package components;

import java.util.ArrayList;

/**
 *
 * @author andrei95
 */
public class CategoryObject {
    
    public String name;
    public int id;
    public String description;
    public int numberOfStars;
    public String brightestStar;
    public String imagePath;
    public int numberOfMessierObjects;
    public String bestVisibility;
    public ArrayList<StarObject> arrayOfStars;
    
    public CategoryObject(String name, int id, String description, int numberOfStars,
                      String brightestStar, String imagePath, int numberOfMessierObjects, String bestVisibility){
        this.name = name;
        this.id = id;
        this.description = description;
        this.numberOfStars = numberOfStars;
        this.brightestStar = brightestStar;
        this.imagePath = imagePath;
        this.numberOfMessierObjects = numberOfMessierObjects;
        this.bestVisibility = bestVisibility;
        this.arrayOfStars = new ArrayList<StarObject>();
    }
    
    public void addStarObject(StarObject star) {
        this.arrayOfStars.add(star);
    }
    
    public String getDescription() {
        String description;
        description = "<html>"
                    + String.format("Name: \t %s", this.name) + "<br><br>"
                    + String.format("Number of Stars: \t %d", this.numberOfStars) + "<br><br>"
                    + String.format("Brightest Star: \t %s", this.brightestStar) + "<br><br>"
                    + String.format("Number of Messier Objects: \t %d", this.numberOfMessierObjects) + "<br><br>"
                    + String.format("Best Visibility: \t %s", this.bestVisibility) + "<br><br>"
                    + String.format("Description: \t %s", this.description) + "<br><br>"
                    + "</html>";
        return description;
    }
}
