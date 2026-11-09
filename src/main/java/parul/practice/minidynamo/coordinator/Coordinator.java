package parul.practice.minidynamo.coordinator;

import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.storage.StorageEngine;
import parul.practice.minidynamo.storage.VersionGenerator;
import parul.practice.minidynamo.transport.*;
import parul.practice.minidynamo.ring.ConsistentHashingRing;
import parul.practice.minidynamo.storage.VersionedValue;
import parul.practice.minidynamo.transport.request.WriteReplicaRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Coordinator {
  private final StorageEngine storageEngine;
  private final Transport transport;
  private final String nodeId;
  private final ConsistentHashingRing ring;
  private VersionGenerator versionGenerator;
  private final int N = 3;
  private final int W = 2;
  private final int R = 2;


  public Coordinator(StorageEngine engine, Transport transport, String nodeId, ConsistentHashingRing ring, VersionGenerator versionGenerator){
      this.storageEngine = engine;
      this.transport = transport;
      this.nodeId = nodeId;
      this.ring = ring;
      this.versionGenerator = versionGenerator;
  }

  private List<Node> getPreferenceList(String key){
      List<Node> preferenceList = ring.getNodes(key, N);
      return preferenceList;
  }

  public boolean put(String key, String value) throws IOException {
      // Step 1 Compute preference list



      //compute the value using (get on storageEngine) - increment the counter
//      VersionedValue existing = storageEngine.get(key);
      Long counter = versionGenerator.nextVersion();
      VersionedValue v = new VersionedValue(value, counter, nodeId);



      //Step 2 send to its own local storage using storageEngine
      boolean localWriteSucceeded = storageEngine.put(key, v);
      int initialSuccess = localWriteSucceeded ? 1 : 0;
      int initialFailure = localWriteSucceeded == false ? 1 : 0;

      WriteReplicaRequest request = new WriteReplicaRequest(key, v);


      //Step 3 send to all N-1 nodes using transport
      List<Node> preferenceList = getPreferenceList(key);
      List<CompletableFuture<Response>> futures = new ArrayList<>();

      for(Node n: preferenceList){
          if(!n.getId().equals(nodeId)){
              futures.add(transport.sendAsync(n,request ));
          }
      }


      //Step 4 wait for W acks inclduing local storage
      CompletableFuture<Boolean> future = waitForAcks(futures, W, initialSuccess, initialFailure);
      try{
          boolean result = future.get(100, TimeUnit.MILLISECONDS);
          if(result) return true;
          return false;
      } catch (ExecutionException e) {
          throw new RuntimeException(e);
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      } catch (TimeoutException e) {
          throw new RuntimeException(e);
      }

  }

  public VersionedValue get(String key){
        return null;
  }

  private VersionedValue reconcile(String key, List<VersionedValue> values){
      return null;
  }

  // TODO: yet to think
  private CompletableFuture<Boolean> waitForAcks(List<CompletableFuture<Response>> futures, int acksNeeded, int initialSuccess, int initialFailure){

      CompletableFuture<Boolean> quorumResult = new CompletableFuture<>();
      if(initialSuccess >= acksNeeded){
          quorumResult.complete(true);
          return quorumResult;
      }else if(initialFailure > (N-acksNeeded)){
          quorumResult.complete(false);
          return quorumResult;
      }

      AtomicInteger success = new AtomicInteger(initialSuccess);
      AtomicInteger failures = new AtomicInteger(initialFailure);
      for(CompletableFuture<Response> r: futures){
          r.whenComplete(((response, exception) -> {

              if(exception != null){
                  failures.incrementAndGet();
              }else{
//                   WriteReplicaResponse wr = (WriteReplicaResponse) response;
                   if(response.getStatus() == ReplicaStatus.SUCCEEDED) {
                       success.incrementAndGet();

                   }else{
                       failures.incrementAndGet();
                   }
              }
              if (success.get() >= acksNeeded) {
                  quorumResult.complete(true);
              } else if (failures.get() > N - acksNeeded) {
                  quorumResult.complete(false);
              }
          }));
      }

      return quorumResult;


  }

}
