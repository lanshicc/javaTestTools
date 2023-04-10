package tree;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class FindNestList {

    public static void main(String[] args) {
        RichNode richNode = CreateRandomRichNode();

        List<String> richNodeValueList = ExtractRichNodeValue(richNode);
        System.out.println(richNodeValueList);
    }

    private static List<String> ExtractRichNodeValue(RichNode richNode) {

        List<String> resultList = new ArrayList<>();

        Stack<StackData> stack = new Stack<>();
        StackData temp = null;
        String value = richNode.getValue();
        resultList.add(value);

        List<RichNode> nodeList = richNode.getNodeList();
        StackData stackData = new StackData();
        stackData.setN(0);
        stackData.setRichNodes(nodeList);

        // richNode下列表的第一个参数可以看作根节点, 第二个参数看为根节点的左孩子，下层列表的第一个参数看为右孩子
        // 存入根节点
        temp = stackData;
        stack.push(temp);
        if (temp.getRichNodes() == null) {
            return null;
        }
        while (temp.getN() < temp.getRichNodes().size() && stack.size() > 0) {
            // 根节点出栈
            temp = stack.pop();
            // 获取根节点
            RichNode rootNode = temp.getRichNodes().get(temp.getN());
            // 存储根节点数据
            resultList.add(rootNode.getValue());
            // 获取右孩子，入栈
            if (rootNode.getNodeList().size() > 0) {
                stack.push(new StackData(rootNode.getNodeList()));
            }
            // 获取左孩子，入栈
            if (temp.getRichNodes().size() != temp.getN() + 1) {
                stack.push(new StackData(temp.getRichNodes(), temp.getN() + 1));
            }
        }
        return resultList;

    }

    private static class StackData {
        private List<RichNode> richNodes;
        private Integer n;

        public StackData() {
        }

        public StackData(List<RichNode> richNodes) {
            this.n = 0;
            this.richNodes = richNodes;
        }

        public StackData(List<RichNode> richNodes, Integer n) {
            this.richNodes = richNodes;
            this.n = n;
        }

        public List<RichNode> getRichNodes() {
            return richNodes;
        }

        public void setRichNodes(List<RichNode> richNodes) {
            this.richNodes = richNodes;
        }

        public Integer getN() {
            return n;
        }

        public void setN(Integer n) {
            this.n = n;
        }
    }

    private static RichNode CreateRandomRichNode() {
        String str = "{\n" +
                "    \"value\": \"A\",\n" +
                "    \"nodeList\": [\n" +
                "        {\n" +
                "            \"value\": \"B\",\n" +
                "            \"nodeList\": [\n" +
                "                {\n" +
                "                    \"value\": \"E\",\n" +
                "                    \"nodeList\": [\n" +
                "                        {\n" +
                "                            \"value\": \"O\",\n" +
                "                            \"nodeList\": []\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"value\": \"N\",\n" +
                "                            \"nodeList\": []\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"F\",\n" +
                "                    \"nodeList\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"G\",\n" +
                "                    \"nodeList\": []\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"value\": \"C\",\n" +
                "            \"nodeList\": [\n" +
                "                {\n" +
                "                    \"value\": \"H\",\n" +
                "                    \"nodeList\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"I\",\n" +
                "                    \"nodeList\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"J\",\n" +
                "                    \"nodeList\": []\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"value\": \"D\",\n" +
                "            \"nodeList\": [\n" +
                "                {\n" +
                "                    \"value\": \"K\",\n" +
                "                    \"nodeList\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"L\",\n" +
                "                    \"nodeList\": []\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": \"M\",\n" +
                "                    \"nodeList\": []\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        RichNode richNode = JSONObject.parseObject(str, RichNode.class);
        return richNode;
    }
}
