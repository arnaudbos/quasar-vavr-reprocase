package com.arnaudbos.quasar_vavr;

import co.paralleluniverse.actors.Actor;
import co.paralleluniverse.actors.MailboxConfig;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channels;

public class Consumer extends Actor<String, Void> {

    public Consumer() {
        super(
            "Consumer",
            new MailboxConfig(-1, Channels.OverflowPolicy.THROW)
        );
    }

    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {
        for(;;) {
            System.out.println(receive());
        }
    }
}
