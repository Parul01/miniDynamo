package parul.practice.minidynamo.coordinator;

import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.transport.Transport;
import parul.practice.minidynamo.ring.ConsistentHashingRing;
import parul.practice.minidynamo.storage.VersionedValue;
import parul.practice.minidynamo.storage.WALService;

import java.io.IOException;
import java.util.List;

public class Coordinator {
  WALService storageEngine;
  Transport transport;
  Node node;
  ConsistentHashingRing ring;
  int N = 3; // get it from the config file


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
      storageEngine.put(key, v);


      //Step 3 send to all N-1 nodes using transport
      List<Node> preferenceList = getPreferenceList(key);


      //Step 4 wait for W acks inclduing local storage

      return true;

  }

  public VersionedValue get(String key){

  }

  private VersionedValue reconcile(String key, List<VersionedValue> values){
      return null;
  }

  // TODO: yet to think
  private void waitForAcks(){

  }

}
