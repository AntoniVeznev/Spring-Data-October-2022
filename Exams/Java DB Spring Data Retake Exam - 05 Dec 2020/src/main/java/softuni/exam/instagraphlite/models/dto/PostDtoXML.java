package softuni.exam.instagraphlite.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "post")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostDtoXML {
    @XmlElement(name = "caption")
    private String caption;
    @XmlElement(name = "user")
    private UserPostDtoXML user;
    @XmlElement(name = "picture")
    private PicturePostDtoXML picture;

    public PostDtoXML() {
    }
    @NotNull
    @Size(min = 21)
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    @NotNull
    public UserPostDtoXML getUser() {
        return user;
    }

    public void setUser(UserPostDtoXML user) {
        this.user = user;
    }
    @NotNull
    public PicturePostDtoXML getPicture() {
        return picture;
    }

    public void setPicture(PicturePostDtoXML picture) {
        this.picture = picture;
    }
}
