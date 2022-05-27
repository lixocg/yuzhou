package com.yuzhou.demo.tree;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

public class NodeTest {

    public static void main(String[] args) {
        //模拟从db获取的数据
        Node n01 = new Node(1, 0, 1, null);
        Node n02 = new Node(2, 0, 2, null);
        Node n03 = new Node(3, 0, 3, null);

        Node n11 = new Node(11, 1, 11, null);
        Node n12 = new Node(12, 1, 12, null);
        Node n13 = new Node(13, 1, 13, null);

        Node n21 = new Node(21, 2, 21, null);

        Node n111 = new Node(111, 11, 111, null);
        Node n112 = new Node(112, 11, 112, null);

        Node n1111 = new Node(1111, 111, 1111, null);
        Node n1112 = new Node(1112, 111, 1112, null);

        List<Node> nodeList = Lists.newArrayList();
        nodeList.add(n01);
        nodeList.add(n02);
        nodeList.add(n03);
        nodeList.add(n11);
        nodeList.add(n12);
        nodeList.add(n13);
        nodeList.add(n21);
        nodeList.add(n111);
        nodeList.add(n112);
        nodeList.add(n1111);
        nodeList.add(n1112);

        System.out.println(JSON.toJSONString(nodeList));

        //转成tree列表结构
        Map<Integer, List<Node>> nodeMap = nodeList.stream().collect(Collectors.groupingBy(Node::getParentId));

        nodeList.forEach(node -> node.setChildren(nodeMap.get(node.getId())));

        System.out.println(JSON.toJSONString(nodeList));

        List<Node> nodeTree = nodeList.stream().filter(v -> v.getParentId() == 0).collect(Collectors.toList());

        System.out.println(JSON.toJSONString(nodeTree));

        //遍历tree列表结果
        nodeTree.forEach(NodeTest::traverse);
    }

    /**
     * 队列-广度优先，栈-深度优先
     * @param node
     */
    public static void traverse(Node node) {
        Queue<Node> queue = new LinkedList<>();
        queue.offer(node);
        while (!queue.isEmpty()){
            Node poll = queue.poll();

            System.out.println(poll.getValue());

            List<Node> children = poll.getChildren();
            if(children != null){
                children.forEach(queue::offer);
            }
        }
    }

    public static String tab(int x) {
        StringBuilder tab = new StringBuilder();
        for (int i = 0; i < x; i++) {
            tab.append(" ").append(tab);
        }
        return tab.toString();
    }
}
