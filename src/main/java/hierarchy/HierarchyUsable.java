package hierarchy;

import java.util.List;

public interface HierarchyUsable<T> {
    Long no();
    Long parentNo();
    List<T> children(); // HierarchyUtil.getListByHierarchy 실행 시 자동으로 생성,
    Long weight();

}
