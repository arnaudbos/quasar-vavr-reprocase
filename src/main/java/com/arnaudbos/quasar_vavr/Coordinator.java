package com.arnaudbos.quasar_vavr;

import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.ActorRegistry;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.actors.MailboxConfig;
import co.paralleluniverse.actors.behaviors.RequestReplyHelper;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.channels.Channels;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;

public class Coordinator extends BasicActor<StringMessage, String> {

    public Coordinator() {
        super(
            "Coordinator",
            new MailboxConfig(-1, Channels.OverflowPolicy.THROW)
        );
    }

    @Override
//    @Suspendable
    protected String doRun() throws InterruptedException, SuspendExecution {
//    protected String doRun() {
        for(;;) {
//            SuspendableCallable<StringMessage> callable =
//                new SuspendableCallable<>() {
//                    @Override
//                    public StringMessage run() throws SuspendExecution, InterruptedException {
//                        return receive(StringMessage.class);
//                    }
//                };
//            SuspendableCheckedFunction0<StringMessage> fn =
//                new SuspendableCheckedFunction0<>(callable);
//            Try.of(() -> receive(StringMessage.class))
//                .peek(stringMessage -> System.out.println(stringMessage.getMessage()))
////                .peek(this::forward)
//            ;

//            StringMessage msg = receive(StringMessage.class);
            Try.of(CheckedFunction0.of(() -> receive(StringMessage.class)))
                .peek(this::forward);
//            forward(msg);
//            System.out.println(msg.getMessage());
        }
    }

//    private static class SuspendableCheckedFunction0<T> implements CheckedFunction0<T> {
//
//        private SuspendableCallable<T> callable;
//
//        public SuspendableCheckedFunction0(SuspendableCallable<T> callable) {
//            this.callable = callable;
//        }
//
//        @Override
//        @Suspendable
//        public T apply() throws Throwable {
//            return callable.run();
//        }
//    }

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
