package net.fekepp.controllers;

/**
 * @author "Felix Leif Keppmann"
 */
public interface ControllerDelegate {

	public void onControllerStarted();

	public void onControllerExecuted();

	public void onControllerStopped();

	public void onControllerStartupException(Exception e);

	public void onControllerExecutionException(Exception e);

	public void onControllerShutdownException(Exception e);

}