package parul.practice.minidynamo.coordinator;

import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.transport.*;
import parul.practice.minidynamo.ring.ConsistentHashingRing;
import parul.practice.minidynamo.storage.VersionedValue;
import parul.practice.minidynamo.storage.WALService;
import parul.practice.minidynamo.transport.request.WriteReplicaRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Coordinator {
  WALService storageEngine;
  Transport transport;
  Node node;
  ConsistentHashingRing ring;
  int N = 3; // get it from the config file
    int W;
    int R;


  public Coordinator(WALService engine, Transport transport, Node node, ConsistentHashingRing ring){
      this.storageEngine = engine;
      this.transport = transport;
      this.node = node;
      this.ring = ring;
  }

  private List<Node> getPreferenceList(String key){
      List<Node> preferenceList = ring.getNodes(key, N);
      return preferenceList;
  }

  public boolean put(String key, String value) throws IOException {
      // Step 1 Compute preference list



      //compute the value using (get on storageEngine) - increment the counter
//      VersionedValue existing = storageEngine.get(key);
      Long counter = node.setCounter();
      VersionedValue v = new VersionedValue(value, counter, node.getId());



      //Step 2 send to its own local storage using storageEngine
      storageEngine.put(key, v); // what if this itself fail?

      WriteReplicaRequest request = new WriteReplicaRequest(key, v);


      //Step 3 send to all N-1 nodes using transport
      List<Node> preferenceList = getPreferenceList(key);
      List<CompletableFuture<Response>> futures = new ArrayList<>();

      for(Node n: preferenceList){
          futures.add(transport.sendAsync(n,request ));
      }


      //Step 4 wait for W acks inclduing local storage

      return true;

  }

  public VersionedValue get(String key){

  }

  private VersionedValue reconcile(String key, List<VersionedValue> values){
      return null;
  }

  // TODO: yet to think
  private CompletableFuture<Boolean> waitForAcks(List<CompletableFuture<Response>> futures){

      CompletableFuture<Boolean> quorumResult = new CompletableFuture<>();

      AtomicInteger success = new AtomicInteger(0);
      AtomicInteger failures = new AtomicInteger(0);
      boolean flag = false;
      for(CompletableFuture<Response> r: futures){
          r.whenComplete(((response, exception) -> {

              if(exception != null){
                  failures.incrementAndGet();
              }else{
                   WriteReplicaResponse wr = (WriteReplicaResponse) response;
                   if(wr.getStatus().equals(ReplicaWriteStatus.SUCCEEDED)) {
                       success.incrementAndGet();

                   }else{
                       failures.incrementAndGet();
                   }
              }
              if (success.get() >= W) {
                  quorumResult.complete(true);
              } else if (failures.get() > N - W) {
                  quorumResult.complete(false);
              }
          }));
      }

      return quorumResult;


  }

}
