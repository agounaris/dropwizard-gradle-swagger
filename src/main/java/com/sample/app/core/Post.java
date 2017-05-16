package com.sample.app.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="post")
public class Post {

    /**
     * Entity's unique identifier.
     */
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Date created;

    @Column(nullable = false)
    private Date modified;

    @Override
    public boolean equals(Object oobj) {
        if (this == oobj) {
            return true;
        }
        if (oobj == null || getClass() != oobj.getClass()) {
            return false;
        }
        Post that = (Post) oobj;
        return Objects.equal(created, that.created) && Objects.equal(modified, that.modified);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
