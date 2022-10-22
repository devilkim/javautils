package hierarchy;

public interface HierarchyRetrieving<T> {

    void retrieve(T current, T parent);

}
