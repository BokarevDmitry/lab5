package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.*;

public class StorageActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    Map<UrlWithCount, Long> testResults = new HashMap<>();

    public StorageActor() {
        testResults.put(new UrlWithCount("http://www.rambler.ru", new Integer(20)), new Long(2));
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
                    log.info("REQUEST: tests for package - " + r.getUrl() + " " + r.getCount());
                    log.info(testResults.size());
                    //getSender().tell(new TestWithResult(r, testResults.get(r)), ActorRef.noSender());
                })

                .build();
    }
}