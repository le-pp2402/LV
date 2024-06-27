package com.phatpl.learnvocabulary.utils.RadixTrie;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

//Trie node class
@Getter
@Setter
public class Node {
    private List<NodeChild> children;

    //Does this node represent the last character in a word?
    //0: no word; >0: is word (termFrequencyCount)
    private long termFrequencyCount;
    private long termFrequencyCountChildMax;

    public Node(long termFrequencyCount) {
        this.termFrequencyCount = termFrequencyCount;
    }

    @Override
    public String toString() {
        return "Node [children=" + children + ", termFrequencyCount=" + termFrequencyCount
                + ", termFrequencyCountChildMax=" + termFrequencyCountChildMax + "]";
    }
}