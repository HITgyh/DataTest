package hashK;

import java.util.*;

public class HashMap {
    private List<LinkedList<Integer>> bucket;
    //比例因子
    private double thresold;
    private int entires = 0;
    //m是哈希方案2的log2 n
    private int m;
    //阈值，超过就要分裂
    private double max;
    private int pageNum;

    public HashMap(int bucketNum, double thresold, int pageNum) {
        bucket = new ArrayList<>();
        for (int i=0; i<bucketNum; i++){
            bucket.add(new LinkedList<Integer>());
        }
        this.thresold = thresold;
        this.pageNum = pageNum;
        max =Math.floor(thresold*bucketNum*pageNum);
        m = (int) Math.pow(2,Math.floor(Math.log((double) bucket.size())/Math.log(2)));
    }

    public int hashFunction(Integer value){
        int hash = value % ( m * 2 );
        if (hash < bucket.size()){
            return hash;
        }else {
            return value % m;
        }
    }
    public void add(Integer value){
        int index = this.hashFunction(value);
        LinkedList<Integer> list = bucket.get(index);
        list.add(value);
        this.entires++;
        if (entires > max){
            split();
        }
    }
    public void split(){
        bucket.add(new LinkedList<Integer>());
        max = bucket.size()*thresold*pageNum;
        List<LinkedList<Integer>> swag = new ArrayList<>();
        for (int i=0; i<bucket.size(); i++){
            swag.add(new LinkedList<Integer>());
        }
        m = (int) Math.pow(2,Math.floor(Math.log((double) bucket.size())/Math.log(2)));
        for (int i=0; i<bucket.size()-1; i++){
            LinkedList<Integer> k = bucket.get(i);
            Iterator<Integer> iterator = k.iterator();
            while (iterator.hasNext()){
                Integer value = iterator.next();
                int index = this.hashFunction(value);
                LinkedList<Integer> t = swag.get(index);
                t.add(value);
            }
        }
        for (int i=0; i<bucket.size(); i++){
            bucket.set(i, swag.get(i));
        }
    }
    public void delete(Integer value) throws HashException {
        int index = this.hashFunction(value);
        LinkedList<Integer> list = bucket.get(index);
        boolean flag = list.remove(value);
        if (!flag){
            throw new HashException("不存在该元素");
        }
        this.entires--;
    }
    public String query(Integer value) throws HashException {
        int index = this.hashFunction(value);
        LinkedList<Integer> list = bucket.get(index);
        if (list.indexOf(value)<0){
            throw new HashException("你要查找的元素不在哈希表中");
        }
        return "该元素位于第"+index+"号桶，"+"第"+list.indexOf(value)+"个";
    }

    public int getBucketNum() {
        return bucket.size();
    }
    public int getPageNum(){ return this.pageNum;}
    public int getM(){
        return this.m;
    }
    public LinkedList<Integer> getLink(int index){
        return bucket.get(index);
    }
    public class HashException extends Exception{
        public HashException(){}
        public HashException(String message){
            super(message);
        }
    }
}
