package com.phatpl.learnvocabulary.utils.Trie;

import com.phatpl.learnvocabulary.models.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTrie {
    public static List<Node> Trie;
    public static Map<Integer, Word> mapWords = new HashMap<>();

    public BuildTrie() {
        Trie = new ArrayList<>();
        Trie.add(new Node());
    }

    public static void addWord(Integer id, String word) {
        word = word.toLowerCase();
        int curPos = 0;
        for (int i = 0; i < word.length(); i++) {
            Node curNode = Trie.get(curPos);
            char ch = word.charAt(i);
            if (ch >= 'a' && ch <= 'z') {
                if (curNode.nextPos[ch - 'a'] == 0) {
                    Trie.add(new Node());
                    curNode.nextPos[ch - 'a'] = Trie.size() - 1;
                }
                curPos = curNode.nextPos[ch - 'a'];
            }
        }
        Node lastNode = Trie.get(curPos);
        lastNode.isEnd = true;
        lastNode.ids.add(id);
    }

    public static void merge(int id) {
        Node curNode = Trie.get(id);
        for (int ch = 0; ch < 26; ch++) {
            if (curNode.nextPos[ch] != 0) {
                merge(curNode.nextPos[ch]);
            }
        }

        for (int ch = 0; ch < 26 && curNode.ids.size() < 10; ch++) {
            int nxtPos = curNode.nextPos[ch];
            if (nxtPos != 0 && Trie.get(nxtPos).ids != null) {
                for (Integer j : Trie.get(nxtPos).ids) {
                    if (curNode.ids.size() >= 10) break;
                    curNode.ids.add(j);
                }
            }
        }
    }

    public static List<Integer> find(String word) {
        word = word.toLowerCase();
        int curPos = 0;
        for (int i = 0; i < word.length(); i++) {
            Node curNode = Trie.get(curPos);
            char ch = word.charAt(i);
            if (ch >= 'a' && ch <= 'z') {
                if (curNode.nextPos[ch - 'a'] != 0) {
                    curPos = curNode.nextPos[ch - 'a'];
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        Node lastNode = Trie.get(curPos);
        if (lastNode.isEnd) {
            return new ArrayList<>(lastNode.ids.get(0));
        } else {
            return lastNode.ids;
        }
    }
}
