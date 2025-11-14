package cc.efit.process.biz.predict.keyword;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeywordTrie {

    private final TrieNode root = new TrieNode();
    public void insertKeywords(String keyword, Integer intentionId) {
        TrieNode node = root;
        for (char c : keyword.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.end = true;
        node.intentionIds.add(intentionId);
    }

    public Set<Integer> search(String keyword) {
        Set<Integer> candidates = new HashSet<>();
        TrieNode node = root;
        for (char c : keyword.toCharArray()) {
            if (!node.children.containsKey(c)) {
                break;
            }
            node = node.children.get(c);
            if (node.end) {
                candidates.addAll(node.intentionIds);
            }
        }
        return candidates;
    }
    public static class TrieNode {
        boolean end = false;
        Map<Character, TrieNode> children = new HashMap<>();
        /**
         * 关键词对应的意图 不同意图之间会重用，所以是set
         */
        Set<Integer> intentionIds = new HashSet<>();
    }

}
