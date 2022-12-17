package softuni.exam.instagraphlite.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "posts")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostRootDtoXML {
    @XmlElement(name = "post")
    private List<PostDtoXML> posts;

    public PostRootDtoXML() {
    }

    public List<PostDtoXML> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDtoXML> posts) {
        this.posts = posts;
    }
}
