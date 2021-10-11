package entities;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractEntity implements Serializable {

    public abstract Integer getId();

    public abstract void setId(Integer id);

    public abstract String getCode();

    public abstract void setCode(String code);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
