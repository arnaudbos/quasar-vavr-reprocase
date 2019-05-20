package com.arnaudbos.quasar_vavr;

import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.ActorRegistry;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.actors.MailboxConfig;
import co.paralleluniverse.actors.behaviors.RequestReplyHelper;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.channels.Channels;
import io.vavr.control.Try;

public class Coordinator extends BasicActor<StringMessage, String> {

    public Coordinator() {
        super(
            "Coordinator",
            new MailboxConfig(-1, Channels.OverflowPolicy.THROW)
        );
    }

    @Override
    protected String doRun() throws InterruptedException, SuspendExecution {
        for(;;) {

            Try.of(() -> receive(StringMessage.class))
                .peek(this::forward);
//            forward(msg);
//            System.out.println(msg.getMessage());
        }
    }

    @Suspendable
    private void forward(StringMessage msg) {
        try {
            ActorRef<String> consumer = ActorRegistry.getOrRegisterActor(
                "Consumer",
                Consumer::new
            );
            consumer.send(msg.getMessage());
            RequestReplyHelper.reply(msg, msg.getMessage() + ": Gotcha!");
        } catch (SuspendExecution suspendExecution) {
            //suspendExecution.printStackTrace();
        }
    }
}
