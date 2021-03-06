package info.country.com.countryinfoproject;

/**
 * Created by JP on 20-04-2016.
 * Model class for Country information's
 */
public class CountryInfo {

    private String title;
    private String description;
    private String imageUrl;

    public CountryInfo() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
