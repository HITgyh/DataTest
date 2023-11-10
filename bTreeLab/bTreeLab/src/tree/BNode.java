package tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 *
 *
 M阶B+树是一种多叉树，是一种平衡树，具有以下特性：

 1. 根节点至少有两个子女。
 2. 每个非叶子节点都有k-1个关键字，其中k为树的阶，即内部节点最多有k个孩子。
 3. 所有关键字分别按从小到大的顺序排列，每个关键字都和其子树根节点的关键字范围有重合。
 4. 所有的数据节点都在同一层上，且都位于树的底层。
 5. 所有叶子节点都包含相同的信息，从左到右链接形成一个链表。
 6. 数据节点对应于所存储数据的实际信息，同时包含一个指向父节点的指针。
 7. 数据节点中的元素按照相应关键字的大小顺序排列。
 8. 内部节点只存储索引信息，不存储实际数据。
 */
public class BNode {

    @Override
    public String toString() {
        return keys.toString();
    }

    /**
     * 是否是叶子节点
     */
    public boolean isLeaf;

    /**
     * 父节点
     */
    public BNode faNode;
    /**
     * 所有的儿子节点
     */
    public List<BNode> sonNode;
    /**
     * 当前节点存放的key
     *  key 与 datas 一一对应
     */
    public List<Integer> keys;
    /**
     * 当前节点key对应的 数据
     */
    public List<Integer> datas;
    /**
     * 左右叶子节点
     */
    public BNode leftNode;
    public BNode rightNode;


    public static int order=3;
    //    public static int minSize = (order-1)/2;
    public static int minSize = (int) Math.ceil(order / 2.0) -1;

    public BNode( ){

    }
    public BNode(int order){
        BNode.order =order;
        BNode.minSize = (int) Math.ceil(order / 2.0) -1;
    }


    /**
     * 添加叶子节点数据
     * @param key
     * @param value
     * @return
     */
    public BNode addLeafNode(Integer key,Integer value){
        // 找到叶子节点key 的插入位置
        // 找到第一个大于等于key的位置
        int index = findIndex(key, keys);
        // 加入指定位置
        keys.add(index,key);
        datas.add(index,value);
        // 如果当前key 阶数大于指定值，进行分裂
        if(keys.size() < order){
            return this;
        }
        // 开始分裂成左右节点
        int mid = keys.size()/2;
        Integer upKey = keys.get(mid);
        // 创建一个新节点
        BNode rightBNode = getLeafNode();
        // 将右边的值拆分
        rightBNode.keys.addAll( keys.subList(mid , keys.size()));
        rightBNode.datas .addAll(datas.subList(mid , datas.size()));

        BNode leftBNode = getLeafNode();
        leftBNode.keys .addAll(keys.subList(0, mid));
        leftBNode.datas .addAll(datas.subList(0,mid));

        // 分裂了一个左右节点，保证叶子结点的链表有序
        if(leftNode!=null){
            leftNode.rightNode = leftBNode;
        }

        leftBNode.leftNode= leftNode;
        leftBNode.rightNode = rightBNode;
        if(rightNode !=null){
            rightNode.leftNode=rightBNode;
        }
        rightBNode.rightNode = rightNode;
        rightBNode.leftNode = leftBNode;

        if(this.faNode == null){
            faNode = getNotLeafNode();
        }
        rightBNode.faNode = faNode;
        leftBNode.faNode = faNode;
        // 需要重新平衡父节点中的元素
        faNode.balanceNode(this,leftBNode,rightBNode,upKey);

        return this;
    }

    /**
     * 添加叶子节点后重新 平衡父节点
     * @param oldNode 分裂前的节点
     * @param upLeftNode 分裂后的左节点
     * @param upRightNode 分裂后的右节点
     * @param upKey 上报的中间 key
     */
    public void balanceNode(BNode oldNode , BNode upLeftNode,BNode upRightNode,Integer upKey){
        // 叶子节点不能够进行平衡
        if(isLeaf){
            return;
        }

        // 将key 插入合适的位置, keyIndex
        int keyIndex = findIndex(upKey, keys);
        // 加入指定位置
        keys.add(keyIndex,upKey);

        // 找到原节点所在位置
        int index = this.sonNode.indexOf(oldNode);
        if(index<0){
            // 找不到原来的位置, 意味着 当前节点是新增的
            this.sonNode.add(upLeftNode);
            this.sonNode.add(upRightNode);
        }else{
            // 删除旧节点
            this.sonNode.remove(index);
            // 原来节点的位置
            this.sonNode.add(index,upRightNode);
            this.sonNode.add(index,upLeftNode);

        }

        // 如果当前key 阶数大于指定值，进行分裂
        if(keys.size() < order){
            return ;
        }

        BNode bNode = getNotLeafNode();
        BNode leafBNode = getNotLeafNode();
        // 开始分裂成左右节点
        int mid = keys.size()/2;
        // 儿子也要 对半向上取整 拆分
        // 因为 每个中间节点都至少包含N个孩子。 其中 Math.ceil(m / 2) <= N <=m
        int midSonNode = (int) Math.ceil(sonNode.size() /2.0);
//        int midSonNode = minSize;

        Integer key = keys.get(mid);
        // 非叶子加点上报key 后需要移除当前节点
        // 移除后不需要重新计算值
        keys.remove(key);

        bNode.keys.addAll( keys.subList(mid,keys.size()));
        bNode.sonNode .addAll(sonNode.subList(midSonNode,sonNode.size()));


        leafBNode.keys.addAll(keys.subList(0, mid)) ;
        leafBNode.sonNode .addAll(sonNode.subList(0,midSonNode));

        // 如果父节点为空
        if(this.faNode == null){
            this.faNode = getNotLeafNode();
        }
        bNode.faNode = this.faNode;
        leafBNode.faNode = this.faNode;

        // 拆分成功后  左右节点进行重新指定父节点
        for(BNode bNode1 : bNode.sonNode){
            bNode1.faNode = bNode;
        }
        for(BNode bNode1 : leafBNode.sonNode){
            bNode1.faNode = leafBNode;
        }

        // 递归 平衡父节点
        this.faNode.balanceNode(this,leafBNode,bNode,key);

    }


    /**
     * 返回 删除操作后的根节点
     * @param key
     * @return
     */
    public BNode delete(Integer key){
        BNode bNode = deleteNode(key);
        // 找到真实 根节点
        while(bNode.faNode!=null && bNode.faNode.keys.size()!=0){

            bNode = bNode.faNode;
            }
        if (bNode.keys.size()==0){
            bNode = bNode.sonNode.get(0);
        }
        return bNode;
    }
    /**
     * 删除
     *
     *  叶子节点
     *      向左边 兄弟 借一个key value, 将借来的key 更新掉 父节点的 儿子.indexOf(左边兄弟)位置 的key
     *      向右边兄弟 借一个 key value   使用右边兄弟第一个key 更新掉 父节点的 儿子.indexOf(当前)位置的key
     *      合并到左边兄弟  直接删除合并， 然后判断父节点儿子数量 是否满足， 不满足走 非叶子节点更新
     *      合并到右边兄弟  直接删除合并， 然后判断父节点儿子数量 是否满足， 不满足走 非叶子节点更新
     *
     *  非叶子节点
     *      向左边兄弟 借一个儿子 ， 父节点下移一个key （ 儿子.indexOf(左边兄弟) ）给当前节点  兄弟节点上报最右边key
     *      向右边兄弟 借一个儿子 ，  父节点下移一个key （ 儿子.indexOf(当前) ）给当前节点， 兄弟上报最左边的key
     *      向左边兄弟 合并，  父节点先下移一个 key （ 儿子.indexOf(左边兄弟) ） 给左边兄弟， 然后将当前key 和儿子也并入左边
     *      向右边兄弟 合并    父节点先下移一个 key （ 儿子.indexOf(当前) ） 给右边边兄弟， 然后将当前key 和儿子也并入右边边
     *  递归处理父节点
     *
     * @param key
     * @return  返回当前操作到 哪个节点了
     */
    public BNode deleteNode(Integer key){

        // 找到叶子节点
        BNode leafNode = findLeafNode(key);
        int index = leafNode.keys.indexOf(key);
        if(index<0){
            // 叶子节点中不存在该key
            return leafNode;
        }
        leafNode.keys.remove(index);
        leafNode.datas.remove(index);

        // 判断当前节点 ，如果当前节点 key 小于指定的数量
//        int size =   (int) Math.ceil(order / 2.0) -1;
        // 移除后依旧满足特性 不用变化
        if(leafNode.keys.size() >=  minSize){
            return leafNode;
        }
        // 否则 查看左右兄弟谁有多余的节点
        // 先看左边
        BNode leftNode = leafNode.leftNode;
        BNode rightNode = leafNode.rightNode;

        //  向左边 兄弟 借一个key value, 将借来的key 更新掉 父节点的 儿子.indexOf(左边兄弟)位置 的key
        // 左边的兄弟 有充足的节点， 并且是有相同父亲的
        if(leftNode!=null && leftNode.keys.size() >minSize && leftNode.faNode == leafNode.faNode  && leafNode.keys.size()!=0){
            // 左边兄弟 移除最右边的
            int len = leftNode.keys.size();
            Integer giveKey = leftNode.keys.get(len - 1);
            Integer giveData = leftNode.datas.get(len - 1);
            leftNode.keys.remove(len-1);
            leftNode.datas.remove(len-1);
            // 分给
            leafNode.keys.add(0,giveKey);
            leafNode.datas.add(0,giveData);
            // 将从左边借过来的 key 更新掉父节点
            BNode faNode = leafNode.faNode;
            // 该儿子的下标位置， 对应这key 的位置为index;
            index = faNode.sonNode.indexOf(leftNode);
            // 更新掉位置的key
            faNode.keys.set(index,giveKey);
            return leafNode;
        }

        //  向右边兄弟 借一个 key value   使用右边兄弟第一个key 更新掉 父节点的 儿子.indexOf(当前)位置的key
        // 右边边的兄弟 有充足的节点， 并且是有相同父亲的
        if(rightNode!=null && rightNode.keys.size() >minSize && rightNode.faNode == leafNode.faNode && leafNode.keys.size()!=0){

            Integer giveKey = rightNode.keys.get(0);
            Integer giveData = rightNode.datas.get(0);
            rightNode.keys.remove(0);
            rightNode.datas.remove(0);
            // 分给
            leafNode.keys.add(giveKey);
            leafNode.datas.add(giveData);
            // 将从右边借过来的 key 更新掉父节点
            BNode faNode = rightNode.faNode;
            index = faNode.sonNode.indexOf(leafNode);
            // 更新掉 index 位置的key ，  rightNode.keys.get(0) 是获取最新的边界
            faNode.keys.set(index,rightNode.keys.get(0));
            return leafNode;
        }

//        合并到左边兄弟   父节点删除右边界值 ， 然后判断父节点儿子数量 是否满足， 不满足走 非叶子节点更新
        // 如果都没有多余的兄弟， 那么就将其合并到左边的兄弟
        if(leftNode !=null &&  leftNode.faNode == leafNode.faNode){
            leftNode.keys.addAll(leafNode.keys);
            leftNode.datas.addAll(leafNode.datas);
            // 将其移除,已经合并掉了
            // 节点合并，意味着父节点的key 也要变少
            BNode faNode = leftNode.faNode;
            index = faNode.sonNode.indexOf(leftNode);
            faNode.keys.remove(index);
            faNode.sonNode.remove(leafNode);

            // 更新链表
            if(leafNode.rightNode !=null){
                leafNode.rightNode.leftNode = leftNode;
            }
            leftNode.rightNode = leafNode.rightNode;
            // 不满足

            return  faNode.balance();
        }
        // 左边已经没有 共同父节点的兄弟了， 合并到右边,父节点删除右边界key
        if(rightNode !=null &&  rightNode.faNode == leafNode.faNode){

            leafNode.keys.addAll(rightNode.keys);
            leafNode.datas.addAll(rightNode.datas);

            BNode faNode = leafNode.faNode;
            int index1 = faNode.sonNode.indexOf(leafNode);
            faNode.keys.remove(index1);
            faNode.sonNode.remove(rightNode);
            // 更新链表
            leafNode.rightNode = rightNode.rightNode;
            if(rightNode.rightNode!=null){
                rightNode.rightNode.leftNode = leafNode;
            }

            // 平衡父节点
            return faNode.balance();
        }
        return this;
    }

    /**
     * 平衡非叶子节点
     *      *  非叶子节点
     *      *      向左边兄弟 借一个儿子 ， 父节点下移一个key （ 儿子.indexOf(左边兄弟) ）给当前节点  兄弟节点上报最右边key
     *      *      向右边兄弟 借一个儿子 ，  父节点下移一个key （ 儿子.indexOf(当前) ）给当前节点， 兄弟上报最左边的key
     *      *      向左边兄弟 合并，  父节点先下移一个 key （ 儿子.indexOf(左边兄弟) ） 给左边兄弟， 然后将当前key 和儿子也并入左边
     *      *      向右边兄弟 合并    父节点先下移一个 key （ 儿子.indexOf(当前) ） 给右边边兄弟， 然后将当前key 和儿子也并入右边边
     *      *  递归处理父节点
     */
    private BNode balance() {
        // 检查 当前父节点的key 是否依旧满足最小性质，不满足，则需要从兄弟节点中借一个过来， 都没有的时候 则需要从父节点下移key
        // 不满足
        if(keys.size() >=  minSize){
            return this;
        }
        // 如果已经全删完了， 就剩一个节点了, 也不用进行平衡了
        if(faNode == null){

            return this;
        }

        // 当前在父节点的位置
        int index = faNode.sonNode.indexOf(this);
        BNode leftNode = null;
        BNode rightNode = null;
        if(index-1 >=0){
            leftNode =faNode.sonNode.get(index-1);
        }
        if(index+1 < faNode.sonNode.size()){
            rightNode =faNode.sonNode.get(index+1);
        }
        // 1. 尝试从 左边兄弟中借一个
        // 向左边兄弟 借一个儿子 ， 父节点下移一个key （ 儿子.indexOf(左边兄弟) ）给当前节点  兄弟节点上报最右边key
        // key 数量满足条件 儿子肯定也是满足的
        if(leftNode !=null && leftNode.keys.size() >minSize){
            // 获取左边兄弟的 最右边儿子
            BNode bNode = leftNode.sonNode.get(leftNode.sonNode.size() - 1);
            leftNode.sonNode.remove(leftNode.sonNode.size() - 1);

            bNode.faNode = this;
            sonNode.add(0,bNode);
            // 左边兄弟在父节点中的位置，下移给当前节点，确保key能够对应 key+1 个儿子
            int leafIndex = faNode.sonNode.indexOf(leftNode);
            keys.add(0,faNode.keys.get(leafIndex));
            // 左边兄弟上报最右边的key，更新父节点
            Integer key = leftNode.keys.get(leftNode.keys.size() - 1);
            faNode.keys.set(leafIndex, key);
            // 移除掉已经上报的
            leftNode.keys.remove(leftNode.keys.size() - 1);

            return this;
        }
        // 2. 尝试从 右边兄弟 借一个
        if(rightNode !=null && rightNode.keys.size() >minSize){
            // 获取右边兄弟，的第一个儿子
            BNode bNode = rightNode.sonNode.get(0);
            rightNode.sonNode.remove(0);
            bNode.faNode = this;
            sonNode.add(bNode);
            int leafIndex = faNode.sonNode.indexOf(this);
            keys.add(faNode.keys.get(leafIndex));
            // 右边兄弟上报最左边key ，更新父节点
            faNode.keys.set(leafIndex, rightNode.keys.get(0));
            rightNode.keys.remove(0);

            return this;
        }
// 向左边兄弟 合并，  父节点先下移一个 key （ 儿子.indexOf(左边兄弟) ） 给左边兄弟， 然后将当前key 和儿子也并入左边
// 向右边兄弟 合并    父节点先下移一个 key （ 儿子.indexOf(当前) ） 给右边边兄弟， 然后将当前key 和儿子也并入右边边
        // 3. 尝试左右都没办法借， 尝试往左兄弟合并
        if(leftNode !=null ){
            // 父节点下移 一个key
            int leftIndex= faNode.sonNode.indexOf(leftNode);
            leftNode.keys.add(faNode.keys.get(leftIndex));
            // 父节点也需要移除掉相对应的key
            faNode.keys.remove(leftIndex);
            leftNode.keys.addAll(keys);
            // 儿子统统合并到左边
            leftNode.sonNode.addAll(sonNode);
            // 重新指定 儿子的父节点
            for(int i =0 ; i<leftNode.sonNode.size();i++){
                leftNode.sonNode.get(i).faNode = leftNode;
            }
            // 移除掉已经合并的
            faNode.sonNode.remove(this);
            if(faNode.keys.size()>= minSize){
                return leftNode;
            }

            // 否则平衡父节点
            return leftNode.faNode.balance();
        }
        // 4. 尝试往右边兄弟合并 ( 将右边的兄弟合并进来)
//  向右边兄弟 合并    父节点先下移一个 key （ 儿子.indexOf(当前) ） 给右边边兄弟， 然后将当前key 和儿子也并入右边边
        if(rightNode !=null ){
            // 父节点下移一个key
            int leftIndex  = faNode.sonNode.indexOf(this);
            keys.add(faNode.keys.get(leftIndex));
            faNode.keys.remove(leftIndex);
            keys.addAll(rightNode.keys);
            sonNode.addAll(rightNode.sonNode);
            for(BNode bNode : sonNode){
                bNode.faNode = this;
            }
            // 移除 已合并的儿子节点
            faNode.sonNode.remove(rightNode);
            if(faNode.keys.size()>= minSize){
                return this;
            }
            // 否则平衡父节点
            return faNode.balance();
        }
        return this;
    }



    /**
     * 查找叶子节点
     * @param key
     * @return
     */
    public BNode findLeafNode(Integer key){
        if(isLeaf){
            // 当前节点是叶子节点了， 就可以返回了
            return this;
        }
        // 查找 key 对应的儿子位置
        int sonIndex = findIndex(key, keys);
        /*
            key的数量 永远 比儿子的数量 少一个
         */
        BNode bNode = sonNode.get(sonIndex);
        return  bNode.findLeafNode(key);
    }


    public Integer findData(Integer key){
        BNode leafNode = findLeafNode(key);
        int index = findDataIndex(key, leafNode.keys);
        if(index<0){
            return null;
        }
        return leafNode.datas.get(index);
    }


    /**
     * 添加节点
     * @param key
     * @param value
     * @return 返回root节点
     */
    public BNode add(Integer key, Integer value){
        BNode leafNode = findLeafNode(key);
        BNode bNode = leafNode.addLeafNode(key,value);
        while(bNode.faNode!=null){
            bNode = bNode.faNode;
        }
        return bNode;
    }


    public static void main1(String[] args) {

        BNode bNode = new BNode(5);
        BNode root = bNode.getLeafNode();
        root= root.add(1,1);
        root= root.add(2,2);
        root= root.add(5,5);
        root= root.add(6,6);
        root= root.add(9,9);
        root= root.add(10,10);
        root= root.add(11,11);
        root= root.add(17,17);

        BNode leafNode = root.findLeafNode(1);
        System.out.println(leafNode.datas);
        System.out.println(root.findData(1));
        System.out.println(root.findData(6));
        System.out.println(root.findData(9));
        System.out.println(root.findData(11));
        System.out.println(root.findData(10));
        System.out.println(root.findData(17));
        System.out.println(root.findData(21));

        System.out.println("=========删除");
        root.delete(1);
        root.delete(6);
        root.delete(9);
        root.delete(11);
        root.delete(10);
//        root.delete(17);
        root.delete(21);
        System.out.println(root.findData(1));
        System.out.println(root.findData(6));
        System.out.println(root.findData(9));
        System.out.println(root.findData(11));
        System.out.println(root.findData(10));
        System.out.println(root.findData(17));
        System.out.println(root.findData(21));

        System.out.println("再次插入");
        root= root.add(1,1);
        root= root.add(2,2);
        root= root.add(5,5);
        root= root.add(6,6);
        root= root.add(9,9);
        root= root.add(10,10);
        root= root.add(11,11);
        root= root.add(17,17);
        System.out.println(root.findData(1));
        System.out.println(root.findData(6));
        System.out.println(root.findData(9));
        System.out.println(root.findData(11));
        System.out.println(root.findData(10));
        System.out.println(root.findData(17));
        System.out.println(root.findData(21));


    }

    public static void main2(String[] args) {

        BNode bNode = new BNode(16);
        BNode root = bNode.getLeafNode();

        int size = 1000000;
        TreeMap<Integer,Integer> map = new TreeMap<>();
        long time = System.currentTimeMillis();
        for(int i=1;i<= size; i++){
            map.put(i,i);
        }
        System.out.println("红黑树插入消耗时间 ： " + (System.currentTimeMillis()- time));
        time = System.currentTimeMillis();
        for(int i=1;i<= size; i++){
            root= root.add(i,i);
        }
        System.out.println("B+ 插入消耗时间 ： " + (System.currentTimeMillis()- time));
        time = System.currentTimeMillis();
        for (int i = 1;i<size; i++){
            map.get(i);

        }
        System.out.println("红黑树查找完成 ： " + (System.currentTimeMillis()- time));

        time = System.currentTimeMillis();
        for (int i = 1;i<size; i++){
            root= root.delete(i);
        }
        System.out.println("B+ 删除完成 ： " + (System.currentTimeMillis()- time));

        for (int i = 1;i<=size; i++){
            Integer data = root.findData(i);
        }
        System.out.println("B+查找完成 ： " + (System.currentTimeMillis()- time));

    }


    /**
     * 二分查找 --- 找到第一个大于或者等于 key 的位置
     * @param key
     * @param list
     * @return
     */
    public static int findIndex(Integer key, List<Integer> list){
        int l =0, r= list.size();
        while (l<r){
            int mid = l+r>>1;
            if(list.get(mid) > key){
                r=mid;
            }else{
                l=mid+1;
            }
        }
        return l;
    }
    public static int findDataIndex(Integer key, List<Integer> list){
        int l =0, r= list.size();
        while (l<r){
            int mid = l+r>>1;
            if(key.equals(list.get(mid))){
                return mid;
            }
            if(list.get(mid) > key){
                r=mid;
            }else{
                l=mid+1;
            }
        }
        return -1;
    }







    /**
     * 创建一个叶子节点
     * @return
     */
    public BNode getLeafNode(){
        BNode bNode = new BNode();
        bNode.keys = new ArrayList<>();
        bNode.datas = new ArrayList<>();
        bNode.isLeaf = true;
        return bNode;
    }

    /**
     * 创建飞叶子节点
     * @return
     */
    public BNode getNotLeafNode(){
        BNode bNode = new BNode();
        bNode.keys = new ArrayList<>();
        bNode.sonNode = new ArrayList<>();
        bNode.isLeaf = false;
        return bNode;
    }

    public void intervalQuery(int key1, int key2){
        BNode node1 = findLeafNode(key1);
        BNode node2 = findLeafNode(key2);
        while (node1!=node2.rightNode&&node1!=null){
            for (Integer k:node1.datas){
                if (k>key1&&k<key2){
                    System.out.print(k+" ");
                }
            }
            node1 = node1.rightNode;
        }
        System.out.println();
    }

}

