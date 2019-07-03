package de.openknowledge.playground.api.rest.security.domain.tweet;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Embeddable
public class PublicationDate {

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date pubDate;

    public PublicationDate() {
        //for JPA
    }

    public PublicationDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }
}
