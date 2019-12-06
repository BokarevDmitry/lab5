package bokarev;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class TestPasserActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public TestPasserActor() {
    }

    public static Props props() {
        return Props.create(TestPasserActor.class);
    }

    @Override
    public void preStart() {
        log.info("Starting TestPasserActor {}", this.getSelf());
    }

    @Override
    public void postStop() {
        log.info("Stopping TestPasserActor {}", this.getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TestWithResult.class, test -> {
                    log.info("TEST IS DONE, RESULT: ");
                    //getSender().tell(new TestToStore(test.getUrl(), 2.0), ActorRef.noSender());
                })

                .build();
    }
}