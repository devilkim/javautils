package hierarchy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HierarchyUtilTest {

    List<SampleNode> list = new ArrayList<>();
    HierarchyUtil<SampleNode> hierarchyUtil;

    @BeforeEach
    public void beforeEach() {
        list.add(new SampleNode(1L, null));     // Root는 1개 혹은 N개로 구성 할 수 있다.
        list.add(new SampleNode(2L, 1L));       //            현재 Tree
        list.add(new SampleNode(3L, 1L));       //              /  \
        list.add(new SampleNode(4L, null));     //             1    4          1 Depth List: [1, 4]
        list.add(new SampleNode(5L, 2L));       //            / \     \
        list.add(new SampleNode(6L, 2L));       //           2   3     9       2 Depth List [2, 3], [9]
        list.add(new SampleNode(7L, 6L));       //          / \
        list.add(new SampleNode(8L, 6L));       //         5   6               3 Depth List [5, 6]
        list.add(new SampleNode(9L, 4L));       //            / \
        hierarchyUtil = new HierarchyUtil<>(list);         //           7   8             4 Depth List [7, 8]
    }

    @Nested
    class GetListByHierarchy {
        @Test
        @DisplayName("getListByHierarchy: 성공, 기본 셋팅 해놓은 트리 구성대로 출력되는지 테스트")
        public void getListByHierarchy_성공1() {
            // 1 Depth
            var dept1List = hierarchyUtil.getListByHierarchy();
            assertThat(dept1List.size()).isEqualTo(2);
            assertThat(dept1List.get(0).no()).isEqualTo(1L);
            assertThat(dept1List.get(1).no()).isEqualTo(4L);

            // 2 Depth
            var dept2_1List = dept1List.get(0).children();
            assertThat(dept2_1List.size()).isEqualTo(2);
            assertThat(dept2_1List.get(0).no()).isEqualTo(2L);
            assertThat(dept2_1List.get(1).no()).isEqualTo(3L);

            var dept2_2List = dept1List.get(1).children();
            assertThat(dept2_2List.size()).isEqualTo(1);
            assertThat(dept2_2List.get(0).no()).isEqualTo(9L);

            // 3 Depth
            var dept3List = dept2_1List.get(0).children();
            assertThat(dept3List.size()).isEqualTo(2);
            assertThat(dept3List.get(0).no()).isEqualTo(5L);
            assertThat(dept3List.get(1).no()).isEqualTo(6L);

            // 4 Depth
            var dept4List = dept3List.get(1).children();
            assertThat(dept4List.size()).isEqualTo(2);
            assertThat(dept4List.get(0).no()).isEqualTo(7L);
            assertThat(dept4List.get(1).no()).isEqualTo(8L);
        }

        @Test
        @DisplayName("getListByHierarchy: 성공, 정렬 순서 바뀌어서 정상 동작하는지 테스트")
        public void getListByHierarchy_성공2() {
            // 1 Depth
            var dept1List = hierarchyUtil.getListByHierarchy(HierarchyUtil.SortType.Descending);
            assertThat(dept1List.size()).isEqualTo(2);
            assertThat(dept1List.get(0).no()).isEqualTo(4L);
            assertThat(dept1List.get(1).no()).isEqualTo(1L);

            // 2 Depth
            var dept2_1List = dept1List.get(0).children();
            assertThat(dept2_1List.size()).isEqualTo(1);
            assertThat(dept2_1List.get(0).no()).isEqualTo(9L);

            var dept2_2List = dept1List.get(1).children();
            assertThat(dept2_2List.size()).isEqualTo(2);
            assertThat(dept2_2List.get(0).no()).isEqualTo(3L);
            assertThat(dept2_2List.get(1).no()).isEqualTo(2L);

            // 3 Depth
            var dept3List = dept2_2List.get(1).children();
            assertThat(dept3List.size()).isEqualTo(2);
            assertThat(dept3List.get(0).no()).isEqualTo(6L);
            assertThat(dept3List.get(1).no()).isEqualTo(5L);

            // 4 Depth
            var dept4List = dept3List.get(0).children();
            assertThat(dept4List.size()).isEqualTo(2);
            assertThat(dept4List.get(0).no()).isEqualTo(8L);
            assertThat(dept4List.get(1).no()).isEqualTo(7L);
        }
    }

    @Nested
    class Retrieve {

        @Test
        @DisplayName("retrieve: 성공")
        public void retrieve_성공1() {
            var retrievedList = new ArrayList<SampleNode>();
            hierarchyUtil.retrieve((current, parent, depth) -> {
                retrievedList.add(current);
                System.out.println(current.getId() + ", " + depth);
            });
            retrievedList.sort(Comparator.comparing(SampleNode::no));

            for (var i = 0; i < retrievedList.size(); i++) {
                assertThat(list.get(i).getId()).isEqualTo(retrievedList.get(i).getId());
            }
        }

    }

    @Nested
    class Get {

        @Test
        @DisplayName("get: 성공, 부모 없는 경우")
        public void get_성공1() {
            var TEST_ID = 1L;
            var parent = hierarchyUtil.getParent(TEST_ID);
            var current = hierarchyUtil.getCurrent(TEST_ID);
            var children = hierarchyUtil.getChildren(TEST_ID);
            var depth = hierarchyUtil.getDepth(TEST_ID);

            assertThat(parent).isEmpty();
            assertThat(current.orElseThrow().getId()).isEqualTo(TEST_ID);
            assertThat(children.size()).isEqualTo(2);
            assertThat(depth.orElseThrow()).isEqualTo(1);
        }

        @Test
        @DisplayName("get: 성공, 자식 없는 경우")
        public void get_성공2() {
            var TEST_ID = 9L;
            var parent = hierarchyUtil.getParent(TEST_ID);
            var current = hierarchyUtil.getCurrent(TEST_ID);
            var children = hierarchyUtil.getChildren(TEST_ID);
            var depth = hierarchyUtil.getDepth(TEST_ID);

            assertThat(parent.orElseThrow().getId()).isEqualTo(4L);
            assertThat(current.orElseThrow().getId()).isEqualTo(TEST_ID);
            assertThat(children.size()).isEqualTo(0);
            assertThat(depth.orElseThrow()).isEqualTo(2);
        }

        @Test
        @DisplayName("get: 성공, 유효하지 않은 ID")
        public void get_성공3() {
            var TEST_ID = 99999L;
            var parent = hierarchyUtil.getParent(TEST_ID);
            var current = hierarchyUtil.getCurrent(TEST_ID);
            var children = hierarchyUtil.getChildren(TEST_ID);
            var depth = hierarchyUtil.getDepth(TEST_ID);

            assertThat(parent).isEmpty();
            assertThat(current).isEmpty();
            assertThat(children.size()).isEqualTo(0);
            assertThat(depth).isEmpty();
        }
    }

}