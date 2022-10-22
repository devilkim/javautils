package hierarchy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HierarchyUtil<T extends HierarchyUsable> {
    private final List<T> list;
    private List<T> hierarchy = null;

    private SortType sortType = null;

    /**
     * HierarchyUsable을 구현한 구현체 list
     *
     * HierarchyUsable
     * no: 자신의 고유 값
     * parentNo: 부모 노드에 고유 값
     * weight: 정렬될 경우에 사용 되는 값, 해당 값 기준으로 오름차 순 혹은 내림차 순 제공
     * children: 빈 ArrayList,
     *
     * @param list
     */
    public HierarchyUtil(List<T> list) {
        this.list = list;
    }

    private void sortRecursive(List<T> list, Comparator<T> comparator) {
        list.sort(comparator);
        for (var item: list) {
            if (item.children().size() != 0) {
                sortRecursive(item.children(), comparator);
            }
        }
    }
    private void sortAscendingRecursive(List<T> list) {
        sortRecursive(list, Comparator.comparing(HierarchyUsable::weight));
    }
    private void sortDescendingRecursive(List<T> list) {
        sortRecursive(list, Comparator.comparing((T item) -> item.weight()).reversed());
    }

    private List<T> createHierarchy(List<T> list) {
        for (var i = 0; i < list.size(); i++) {
            var item1 = list.get(i);
            for (var j = 0; j < list.size(); j++) {
                if (i == j) {
                    continue;
                }
                var item2 = list.get(j);
                if (item1.no().equals(item2.parentNo())) {
                    var test = new ArrayList<>();
                    test.add(item2);
                    item1.children().add(item2);
                }
            }
        }
        return list.stream()
                .filter(item -> item.parentNo() == null || item.parentNo() == 0)
                .collect(Collectors.toList());
    }
    private void retrieve(T parent, List<T> list, HierarchyRetrieving<T> hierarchyRetrieving) {
        for (T item : list) {
            hierarchyRetrieving.retrieve(item, parent);
            if (item.children().size() != 0) {
                retrieve(item, item.children(), hierarchyRetrieving);
            }
        }
    }
    public void retrieve(HierarchyRetrieving<T> hierarchyRetrieving) {
        var list = getListByHierarchy();
        for (T item : list) {
            hierarchyRetrieving.retrieve(item, null);
            if (item.children().size() != 0) {
                retrieve(item, item.children(), hierarchyRetrieving);
            }
        }
    }

    /**
     * 단순 리스트를 Hierarchy 구조로 변환.
     * HierarchyUsable에서 반환되는 no()와 parentNo()에 의해 부모 자식 관계가 형성되며,
     * 형제 노드 간에는 weight()에서 반환되는 값에 의해 정렬이 된다.
     * @return Hierarchy 구조로 구조화된 리스트
     */
    public List<T> getListByHierarchy(SortType sortType) {
        if (this.hierarchy != null && this.sortType != null && this.sortType.equals(sortType)) {
            return this.hierarchy;
        }
        var list = createHierarchy(this.list);
        switch (sortType) {
            case Ascending:
                sortAscendingRecursive(list);
                break;
            case Descending:
                sortDescendingRecursive(list);
                break;
        }
        this.hierarchy = list;
        this.sortType = sortType;
        return list;
    }

    /**
     * 단순 리스트를 Hierarchy 구조로 변환.
     * HierarchyUsable에서 반환되는 no()와 parentNo()에 의해 부모 자식 관계가 형성되며,
     * 형제 노드 간에는 weight()에서 반환되는 값에 의해 정렬이 되며, 해당 메소드의 정렬은 오름차 순이다.
     * @return Hierarchy 구조로 구조화된 리스트
     */
    public List<T> getListByHierarchy() {
        return getListByHierarchy(SortType.Ascending);
    }



    public List<T> flat(Integer depth) {
        return null;
    }




    enum SortType {
        Ascending, Descending
    }
}
