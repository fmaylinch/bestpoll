package may.bestpoll.entities.base;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.PrePersist;
import org.bson.types.ObjectId;

import java.util.Date;

public class Identity
{
    @Id
    private ObjectId id;

    private Date date;

    public ObjectId getId()
    {
        return id;
    }

    public void setId(ObjectId id)
    {
        this.id = id;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    @PrePersist
    void prePersist()
    {
        date = new Date();
    }
}
