package com.yuzhou.demo.tree;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Node {
    private int id;
    private int parentId;
    private Integer value;
    private List<Node> children;


}
