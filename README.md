# 2322. Minimum Score After Removals on a Tree

[🔗 LeetCode Problem](https://leetcode.com/problems/minimum-score-after-removals-on-a-tree/)  

![Difficulty](https://img.shields.io/badge/Difficulty-Hard-red)
![Status](https://img.shields.io/badge/Status-Solved-brightgreen.svg)
![Language](https://img.shields.io/badge/Language-Java-blue.svg)

---

## 📌 Problem Statement

You're given a connected **tree** of `n` nodes labeled `0` to `n - 1`, with `n - 1` edges and an array `nums` where `nums[i]` represents the value of the `i-th` node.

You must **remove two different edges** to form **three connected components**, and compute the **XOR sum** of each component. The **score** is defined as the difference between the **maximum** and **minimum** XOR values among the three components.

Return the **minimum possible score** among all valid combinations of two edge removals.

---

## 🔍 Example

### Example 1

```text
Input: 
nums = [1, 5, 5, 4, 11]
edges = [[0,1], [1,2], [1,3], [3,4]]

Output: 9
````

#### Explanation:

Remove edges `[1,2]` and `[3,4]`:

* **Component 1** → Nodes: `[2]`, XOR = 5
* **Component 2** → Nodes: `[4]`, XOR = 11
* **Component 3** → Nodes: `[0,1,3]`, XOR = 1 ^ 5 ^ 4 = 0

Score = `max(11, 5, 0) - min(11, 5, 0) = 11 - 0 = 11`

Another combination gives a **score of 9**, which is the minimum possible. The function returns that.

---

## 🚀 Solution Idea

### 💡 Strategy

1. **DFS Preprocessing:**

   * Traverse the tree using DFS from the root.
   * Calculate:

     * `sum[i]`: XOR of the subtree rooted at node `i`.
     * `in[i]` and `out[i]`: timestamps for Euler Tour, used to determine if one node is in another’s subtree.

2. **Simulate All Valid Edge Removals:**

   * For every pair of non-root nodes `i` and `j`, simulate the effect of removing the edges above them.
   * Three scenarios are possible:

     * **Nested Subtree**: One is in the subtree of the other.
     * **Independent Subtrees**: Nodes `i` and `j` lie in separate branches.

3. **Component XOR Calculation:**

   * Use the XOR sums and parent-child relationships to calculate the XOR values of the resulting three components efficiently, without physically removing edges.

4. **Score Calculation:**

   * For each pair of nodes (and simulated edge removals), compute the XORs of the 3 resulting components.
   * Update the result with the minimum score found.

---

## 🧾 Code

```java
class Solution {
    public int minimumScore(int[] nums, int[][] edges) {
        List<List<Integer>> li = new ArrayList<>();
        int n = nums.length;
        for (int i = 0; i < n; i++) li.add(new ArrayList<>());
        for (int[] edge : edges) {
            li.get(edge[0]).add(edge[1]);
            li.get(edge[1]).add(edge[0]);
        }

        int[] sum = new int[n];
        int[] in = new int[n];
        int[] out = new int[n];
        int[] counter = new int[1];  // acts as a timestamp
        dfs(0, -1, nums, li, sum, in, out, counter);

        int minScore = Integer.MAX_VALUE;
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (in[i] < in[j] && in[j] < out[i]) {
                    minScore = Math.min(minScore, calcScore(sum[0] ^ sum[i], sum[i] ^ sum[j], sum[j]));
                } else if (in[j] < in[i] && in[i] < out[j]) {
                    minScore = Math.min(minScore, calcScore(sum[0] ^ sum[j], sum[j] ^ sum[i], sum[i]));
                } else {
                    minScore = Math.min(minScore, calcScore(sum[0] ^ sum[i] ^ sum[j], sum[i], sum[j]));
                }
            }
        }

        return minScore;
    }

    private int calcScore(int a, int b, int c) {
        return Math.max(a, Math.max(b, c)) - Math.min(a, Math.min(b, c));
    }

    private void dfs(int node, int parent, int[] nums, List<List<Integer>> tree,
                     int[] sum, int[] in, int[] out, int[] counter) {
        sum[node] = nums[node];
        in[node] = counter[0]++;
        for (int child : tree.get(node)) {
            if (child != parent) {
                dfs(child, node, nums, tree, sum, in, out, counter);
                sum[node] ^= sum[child];  // combine subtree XORs
            }
        }
        out[node] = counter[0];
    }
}
```

---

## 🧠 Complexity Analysis

| Metric    | Value    |
| --------- | -------- |
| **Time**  | `O(n^2)` |
| **Space** | `O(n)`   |

* `O(n)` for DFS to calculate subtree XORs.
* `O(n^2)` for trying all valid pairs of nodes (edges) to simulate removal.

---

## 📌 Notes

* The **Euler Tour technique** with `in[]` and `out[]` arrays helps determine if a node is in another node’s subtree in constant time.
* **Subtree XOR** is critical. Instead of recomputing XORs from scratch each time, we **reuse** precomputed values using tree properties.
* We **avoid actual edge deletions** and **simulate the effect** logically using XOR and DFS properties.
* A subtle part is to recognize that removing two edges gives us exactly three components and we can precisely calculate their values without disconnecting the tree.

---

## ✅ Additional Resources

* [Tree Traversal with Entry/Exit Times (Euler Tour)](https://cp-algorithms.com/graph/euler_tour.html)
* [XOR Properties](https://www.geeksforgeeks.org/find-the-element-that-appears-once/)

---
If you found this helpful, consider ⭐️ starring the repository and following for more algorithm problem breakdowns!
