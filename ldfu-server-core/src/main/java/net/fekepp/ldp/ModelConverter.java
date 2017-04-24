package net.fekepp.ldp;

public interface ModelConverter {

	public void convert();

	public Model getInputModel();

	public Model getOutputModel();

}