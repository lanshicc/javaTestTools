package tree;

import lombok.Data;

import java.util.List;

@Data
public class RichNode {
    public List<RichNode> nodeList;
    public String value;
}
