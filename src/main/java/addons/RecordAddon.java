package addons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RecordAddon<T1, T2> {
    /*------------------------ FIELDS REGION ------------------------*/
    private T1 x;
    private T2 y;

    /*------------------------ METHODS REGION ------------------------*/
    public RecordAddon(T1 x, T2 y) {
        this.x = x;
        this.y = y;
    }

    public T1 getX() {
        return x;
    }

    public void setX(T1 x) {
        this.x = x;
    }

    public T2 getY() {
        return y;
    }

    public void setY(T2 y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RecordAddon<?, ?> that = (RecordAddon<?, ?>) o;

        return new EqualsBuilder()
                .append(x, that.x)
                .append(y, that.y)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(x)
                .append(y)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("axisX", x)
                .append("axisY", y)
                .toString();
    }
}
