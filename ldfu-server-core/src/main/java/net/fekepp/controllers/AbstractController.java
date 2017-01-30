package net.fekepp.controllers;

/**
 * @author "Felix Leif Keppmann"
 */
public abstract class AbstractController implements Controller {

	protected final Object synchronization = new Object();

	protected boolean startup = false;
	protected boolean execution = false;
	protected boolean shutdown = false;

	protected ControllerDelegate delegate;

	@Override
	public synchronized void start() {

		if (startup || execution) {
			return;
		}

		startup = true;

		Thread thread = new Thread(this);
		thread.setName(getClass().getSimpleName());
		thread.start();

	}

	@Override
	public synchronized void startBlocking() {

		start();

		try {
			synchronized (synchronization) {
				while (startup) {
					synchronization.wait();
				}
			}
		}

		catch (InterruptedException e) {
			if (delegate != null) {
				delegate.onControllerStartupException(e);
			}
		}

	}

	@Override
	public synchronized void stop() {

		if (shutdown || !execution) {
			return;
		}

		shutdown = true;

		synchronized (synchronization) {
			synchronization.notify();
		}

	}

	@Override
	public synchronized void stopBlocking() {

		stop();

		try {
			synchronized (synchronization) {
				while (shutdown) {
					synchronization.wait();
				}
			}
		}

		catch (InterruptedException e) {
			if (delegate != null) {
				delegate.onControllerShutdownException(e);
			}
		}

	}

	@Override
	public void run() {

		try {
			startup();
		} catch (Exception e) {
			if (delegate != null) {
				delegate.onControllerStartupException(e);
			}
		}

		startup = false;
		execution = true;

		synchronized (synchronization) {
			synchronization.notify();
		}

		if (delegate != null) {
			delegate.onControllerStarted();
		}

		try {
			execute();
		}

		catch (Exception e) {
			if (delegate != null) {
				delegate.onControllerExecutionException(e);
			}
		}

		if (delegate != null) {
			delegate.onControllerExecuted();
		}

		try {
			shutdown();
		} catch (Exception e) {
			if (delegate != null) {
				delegate.onControllerShutdownException(e);
			}
		}

		shutdown = false;
		execution = false;

		synchronized (synchronization) {
			synchronization.notify();
		}

		if (delegate != null) {
			delegate.onControllerStopped();
		}

	}

	@Override
	public boolean isExecuting() {
		return execution;
	}

	@Override
	public ControllerDelegate getDelegate() {
		return delegate;
	}

	@Override
	public void setDelegate(ControllerDelegate delegate) {
		this.delegate = delegate;
	}

	protected abstract void startup() throws Exception;

	protected void execute() throws Exception {

		synchronized (synchronization) {
			while (execution && !shutdown) {
				synchronization.wait();
			}
		}

	}

	protected abstract void shutdown() throws Exception;

}