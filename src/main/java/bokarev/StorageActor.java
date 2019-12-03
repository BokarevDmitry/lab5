package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.*;

public class StorageActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private Map<String, Double> testResults;

    public StorageActor() {
        this.testResults = new HashMap<>();
    }

    public static Props props() {
        return Props.create(StorageActor.class);
    }

    @Override
    public void preStart() {
        log.info("Starting StorageActor {}", this);
    }

    @Override
    public void postStop() {
        log.info("Stopping StorageActor {}", this);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TestToStore.class, test -> {
                        log.info("REQUEST: store test results of  - " + test.getUrl());
                        this.testResults.put(test.getUrl(), test.getTime());
                })

                .match(UrlWithCount.class, r -> {
                    log.info("REQUEST: tests for package - " + r.getUrl());
                    if (this.testResults.containsKey(r.getUrl())) {
                        getSender().tell(this.testResults.get(r.getUrl()), getSelf());
                    } else {
                        getSender().tell("No such test", ActorRef.noSender());
                    }
                })

                .build();
    }
}