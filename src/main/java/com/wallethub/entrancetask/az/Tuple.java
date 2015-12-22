package com.wallethub.entrancetask.az;

/**
 * @author alexey.zakharchenko@incryptex.com
 */
class Tuple<X, Y> {
    public final X x;
    public final Y y;

    Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple<?, ?> tuple = (Tuple<?, ?>) o;

        if (x != null ? !x.equals(tuple.x) : tuple.x != null) return false;
        return y != null ? y.equals(tuple.y) : tuple.y == null;

    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
}
