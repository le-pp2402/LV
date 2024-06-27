package com.phatpl.learnvocabulary.utils.RadixTrie;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeChild {
    private String key;
    private Node node;

    @Override
    public String toString() {
        return "NodeChild [key=" + key + ", node=" + node + "]";
    }

    public NodeChild(String key, Node node) {
        super();
        this.key = key;
        this.node = node;
    }
}
