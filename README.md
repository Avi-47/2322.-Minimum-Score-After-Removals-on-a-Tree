# 2322. Minimum Score After Removals on a Tree

[🔗 LeetCode Problem](https://leetcode.com/problems/minimum-score-after-removals-on-a-tree/)  

![Difficulty](https://img.shields.io/badge/Difficulty-Hard-red)
![Status](https://img.shields.io/badge/Status-Solved-brightgreen.svg)
![Language](https://img.shields.io/badge/Language-Java-blue.svg)

---

## 📌 Problem Statement

You're given a connected **tree** of `n` nodes labeled `0` to `n - 1`, with `n - 1` edges and an array `nums` where `nums[i]` represents the value of the `i-th` node.

You must **remove two different edges** to form **three connected components**, and compute the **XOR sum** of each component. The **score** is the difference between the maximum and minimum XOR values across the components.

Return the **minimum possible score** across all valid edge removal combinations.

---

## 🔍 Example

### Example 1:
Input:
nums = [1,5,5,4,11]
edges = [[0,1],[1,2],[1,3],[3,4]]

Output: 9

Explanation:
Remove edges [1,2] and [3,4]:

Component 1: [1,3] => XOR = 5 ^ 4 = 1

Component 2: [4] => XOR = 11

Component 3: [0,1,2] => XOR = 1 ^ 5 ^ 5 = 1
Score = 11 - 2 = 9


---

## 🚀 Solution Idea

We want to **remove 2 edges** in a **tree** to split it into **3 components** and then calculate the XOR value of each.

### Strategy

1. **DFS Traversal:**
   - Pre-compute the **XOR sum of all subtrees** and track **in/out times** for each node to help identify ancestor relationships.

2. **Pairwise Edge Simulation:**
   - For every **pair of nodes** `i` and `j`, simulate removing the edges above them (excluding root).
   - Use `in[i] < in[j] < out[i]` to determine if `j` is in the subtree of `i`.
   - Depending on whether nodes are nested or independent, compute the resulting component XORs differently.

3. **Minimize the Score:**
   - Calculate the score as `max(xor1, xor2, xor3) - min(xor1, xor2, xor3)` for each case and return the **minimum**.

---

## 🧾 Code

```java
class Solution {
    public int minimumScore(int[] nums, int[][] edges) {
        List<List<Integer>> li = new ArrayList<>();
        int n = nums.length;
        for(int i = 0; i < n; i++) li.add(new ArrayList<>());
        for(int[] a : edges) {
            li.get(a[0]).add(a[1]);
            li.get(a[1]).add(a[0]);
        }
        int res = Integer.MAX_VALUE;
        int[] sum = new int[n];
        int[] in = new int[n];
        int[] out = new int[n];
        int[] cnt = new int[n];
        dfs(0, -1, nums, li, sum, in, out, cnt);
        
        for(int i = 1; i < n; i++) {
            for(int j = i + 1; j < n; j++) {
                if (in[i] < in[j] && in[j] < out[i]) {
                    res = Math.min(res, func(sum[0] ^ sum[i], sum[i] ^ sum[j], sum[j]));
                } else if (in[j] < in[i] && in[i] < out[j]) {
                    res = Math.min(res, func(sum[0] ^ sum[j], sum[j] ^ sum[i], sum[i]));
                } else {
                    res = Math.min(res, func(sum[0] ^ sum[i] ^ sum[j], sum[i], sum[j]));
                }
            }
        }
        return res;
    }

    public int func(int a, int b, int c) {
        return Math.max(a, Math.max(b, c)) - Math.min(a, Math.min(b, c));
    }

    public void dfs(int id, int par, int[] num, List<List<Integer>> li, int[] sum, int[] in, int[] out, int[] cnt) {
        sum[id] = num[id];
        in[id] = cnt[0]++;
        for(int child : li.get(id)) {
            if (child == par) continue;
            dfs(child, id, num, li, sum, in, out, cnt);
            sum[id] ^= sum[child];
        }
        out[id] = cnt[0];
    }
}
```
🧠 Complexity Analysis
Time Complexity: O(n^2)

DFS to calculate subtree sums: O(n)

Nested loop for all pairs of nodes: O(n^2)

Space Complexity: O(n)

For storing sums, in/out times, and the tree adjacency list.

🧠 Notes
in[] and out[] are used to determine ancestor-descendant relationships using Euler Tour technique.

XOR is a non-commutative and bitwise operation, which makes prefix XOR and subtree XOR particularly useful.

This approach avoids actually removing edges — instead, it simulates the logical separation based on subtree analysis.
