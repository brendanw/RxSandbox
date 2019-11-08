package async;

import java.util.concurrent.FutureTask;

public class ActionFutureTask<T> extends FutureTask<T> implements Prioritized, Cancellable {
    private Runnable runnable;

    public ActionFutureTask(Runnable runnable, T action) {
        super(runnable, action);
        this.runnable = runnable;
    }

    @Override
    public int getPriority() {
        if (runnable instanceof Prioritized) {
            return ((Prioritized) runnable).getPriority();
        }
        return Priority.DEFAULT.ordinal();
    }

    @Override
    public boolean cancel() {
        return cancel(true);
    }

    @Override
    public boolean isCancelled() {
        if (runnable instanceof Cancellable) {
            return ((Cancellable) runnable).isCancelled() || super.isCancelled();
        }
        return false;
    }
}

