package com.phatpl.learnvocabulary.utils.Trie;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Node {
    public boolean isEnd = false;
    public List<Integer> ids = new ArrayList<Integer>();
    public int[] nextPos = new int[26];
}
