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
```text
Input: 
nums = [1, 5, 5, 4, 11]
edges = [[0,1], [1,2], [1,3], [3,4]]

Output: 9
```
Explanation:
Remove edges [1,2] and [3,4]:

Component 1: [1,3] => XOR = 5 ^ 4 = 1

Component 2: [4] => XOR = 11

Component 3: [0,1,2] => XOR = 1 ^ 5 ^ 5 = 1
Score = 11 - 2 = 9

---

🚀 Solution Idea
💡 Strategy
DFS Preprocessing:

Traverse the tree using DFS from the root.

Calculate:

sum[i]: XOR of the subtree rooted at node i.

in[i] and out[i]: timestamps for Euler Tour, used to determine if one node is in another’s subtree.

Simulate All Valid Edge Removals:

For every pair of non-root nodes i and j, simulate the effect of removing the edges above them.

Three scenarios are possible:

Nested Subtree: One is in the subtree of the other.

Independent Subtrees: Nodes i and j lie in separate branches.

Component XOR Calculation:

Use the XOR sums and parent-child relationships to calculate the XOR values of the resulting three components efficiently, without physically removing edges.

Score Calculation:

For each pair of nodes (and simulated edge removals), compute the XORs of the 3 resulting components.

Update the result with the minimum score found.



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

📌 Notes
The Euler Tour technique with in[] and out[] arrays helps determine if a node is in another node’s subtree in constant time.

Subtree XOR is critical. Instead of recomputing XORs from scratch each time, we reuse precomputed values using tree properties.

We avoid actual edge deletions and simulate the effect logically using XOR and DFS properties.

A subtle part is to recognize that removing two edges gives us exactly three components and we can precisely calculate their values without disconnecting the tree.
