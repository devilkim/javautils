package hierarchy;

import java.util.List;

public interface HierarchyUsable<T> {
    Long no();
    Long parentNo();
    List<T> children();
    Long weight();
}
