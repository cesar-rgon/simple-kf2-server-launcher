package entities;

import java.io.Serializable;
import java.util.Objects;

public abstract class CommonEntity implements Serializable {

    public abstract Integer getId();

    public abstract void setId(Integer id);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonEntity that = (CommonEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
