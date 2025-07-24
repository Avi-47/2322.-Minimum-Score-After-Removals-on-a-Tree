class Solution {
    public int minimumScore(int[] nums, int[][] edges) {
        List<List<Integer>> li = new ArrayList<>();
        int n = nums.length;
        for(int i=0;i<n;i++) li.add(new ArrayList<>());
        for(int[] a:edges){
            li.get(a[0]).add(a[1]);
            li.get(a[1]).add(a[0]);
        }
        int res = Integer.MAX_VALUE;
        int[] sum = new int[n];
        int[] in = new int[n];
        int[] out = new int[n];
        int[] cnt = new int[n];
        dfs(0,-1,nums,li,sum,in,out,cnt);
        for(int i=1;i<n;i++){
            for(int j=i+1;j<n;j++){
                if(in[i]<in[j] && in[j]<out[i]){
                    res = Math.min(res,func(sum[0]^sum[i],sum[i]^sum[j],sum[j]));
                }else if(in[j]<in[i] && in[i]<out[j]){
                    res = Math.min(res,func(sum[0]^sum[j],sum[j]^sum[i],sum[i]));
                }else{
                    res = Math.min(res,func(sum[0]^sum[i]^sum[j],sum[i],sum[j]));
                }
            }
        }
        return res;
    }
    public int func(int a, int b, int c){
        return Math.max(a,Math.max(b,c))-Math.min(a,Math.min(b,c));
    }
    public void dfs(int id,int par,int[] num, List<List<Integer>> li,int[] sum,int[] in,int[] out,int[] cnt){
        sum[id] = num[id];
        in[id] = cnt[0]++;
        for(int child:li.get(id)){
            if(child==par) continue;
            dfs(child,id,num,li,sum,in,out,cnt);
            sum[id]^=sum[child];
        }
        out[id] = cnt[0];
    }
}
