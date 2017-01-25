package net.fekepp.controllers;

/**
 * @author "Felix Leif Keppmann"
 */
public interface Controller extends Runnable {

	public void start();

	public void startBlocking();

	public void stop();

	public void stopBlocking();

	public boolean isExecuting();

	public ControllerDelegate getDelegate();

	public void setDelegate(ControllerDelegate delegate);

}