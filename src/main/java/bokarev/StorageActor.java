package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.*;

public class StorageActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private Map<UrlWithCount, Long> testResults;

    public StorageActor() {
        this.testResults = new HashMap<>();
        testResults.put(new UrlWithCount("http://www.rambler.ru", 20), new Long(2));
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
                .match(TestWithResult.class, test -> {
                        log.info("REQUEST: store test results of  - " + test.getUrl());
                        testResults.put(test.getUrl(), test.getResult());
                })

                .match(UrlWithCount.class, r -> {
                    log.info("REQUEST: tests for package - " + r.getUrl());
                    log.info(testResults.get(r).toString());
                    //getSender().tell(new TestWithResult(r, testResults.get(r)), ActorRef.noSender());
                })

                .build();
    }
}