package hierarchy;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SampleNode implements HierarchyUsable<SampleNode> {

    private Long id;
    private Long parentId;
    private List<SampleNode> children = new ArrayList<>();

    public SampleNode(Long id, Long parentId) {
        this.id = id;
        this.parentId = parentId;
    }
    @Override
    public Long no() {
        return id;
    }

    @Override
    public Long parentNo() {
        return parentId;
    }

    @Override
    public List<SampleNode> children() {
        return children;
    }

    @Override
    public Long weight() {
        return id;
    }
}
