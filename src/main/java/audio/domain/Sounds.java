package audio.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class Sounds {

    @XmlElement(name = "genre")
    private List<Genre> genres;

    public List<Genre> getGenres() {
        if (genres == null) {
            genres = new ArrayList<>();
        }
        return genres;
    }
}
