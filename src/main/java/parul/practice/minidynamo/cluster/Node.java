package parul.practice.minidynamo.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Node {
    String id;
    List<VirtualNode> virtualNodeList;
    AtomicLong counter;

    public Node(String id){
        this.id = id;
        this.counter = new AtomicLong(0);
        virtualNodeList = new ArrayList<>();
        // add virtual nodes;

        for(int i =0;i<150;i++){
            virtualNodeList.add(new VirtualNode(this,id+"-vn-"+i));
        }
    }

    public List<VirtualNode> getVirtualNodeList() {
        return virtualNodeList;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public Long setCounter(){
        return counter.incrementAndGet();
    }
    public synchronized Long setCounter(Long counter){

        if(counter > this.counter.get()){
            this.counter.set(counter);
        }else{
            setCounter();
        }

        return this.counter.get();
    }
}
