package layout;

/**
 * Created by ppatel on 8/4/2016.
 */
public class RedditPost {

    public String title;
    public String link;

    public RedditPost(){
        title = null;
        link = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
