package entities;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract Object getId();

    public abstract void setId(Object id);

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
