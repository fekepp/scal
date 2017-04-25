package net.fekepp.ldp.listener;

import java.util.HashSet;
import java.util.Set;

import net.fekepp.ldp.Format;
import net.fekepp.ldp.Model;
import net.fekepp.ldp.ResourceListener;
import net.fekepp.ldp.ResourceListenerDelegate;

public abstract class AbstractResourceListener implements ResourceListener {

	private ResourceListenerDelegate listenerDelegate;

	private Set<String> identifiers;

	private Set<String> methods;

	private Set<Model> storageModels;
	private Set<Format> storageFormats;

	private Set<Model> inputModels;
	private Set<Format> inputFormats;

	private Set<Model> outputModels;
	private Set<Format> outputFormats;

	public AbstractResourceListener() {
		this(null);
	}

	public AbstractResourceListener(ResourceListenerDelegate listenerDelegate) {
		this.listenerDelegate = listenerDelegate;
	}

	@Override
	public ResourceListenerDelegate getListenerDelegate() {
		return listenerDelegate;
	}

	@Override
	public void setListenerDelegate(ResourceListenerDelegate listenerDelegate) {
		this.listenerDelegate = listenerDelegate;
	}

	@Override
	public boolean isListeningOnIdentifier(String identifier) {
		if (identifiers == null || identifiers.isEmpty() || identifiers.contains(identifier)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnMethod(String method) {
		if (methods == null || methods.isEmpty() || methods.contains(method)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnStorage(Model model) {
		if (storageModels == null || storageModels.isEmpty() || storageModels.contains(model)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnStorage(Format format) {
		if (storageFormats == null || storageFormats.isEmpty() || storageFormats.contains(format)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnInput(Model model) {
		if (inputModels == null || inputModels.isEmpty() || inputModels.contains(model)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnInput(Format format) {
		if (inputFormats == null || inputFormats.isEmpty() || inputFormats.contains(format)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnOutput(Model model) {
		if (outputModels == null || outputModels.isEmpty() || outputModels.contains(model)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnOutput(Format format) {
		if (outputFormats == null || outputFormats.isEmpty() || outputFormats.contains(format)) {
			return true;
		}
		return false;
	}

	public Set<String> getIdentifiers() {

		// Lazy initialization
		if (identifiers == null) {
			identifiers = new HashSet<String>();
		}

		// Return
		return identifiers;

	}

	public Set<String> getMethods() {

		// Lazy initialization
		if (methods == null) {
			methods = new HashSet<String>();
		}

		// Return
		return methods;

	}

	public Set<Model> getStorageModels() {

		// Lazy initialization
		if (storageModels == null) {
			storageModels = new HashSet<Model>();
		}

		// Return
		return storageModels;

	}

	public Set<Format> getStorageFormats() {

		// Lazy initialization
		if (storageFormats == null) {
			storageFormats = new HashSet<Format>();
		}

		// Return
		return storageFormats;

	}

	public Set<Model> getInputModels() {

		// Lazy initialization
		if (inputModels == null) {
			inputModels = new HashSet<Model>();
		}

		// Return
		return inputModels;

	}

	public Set<Format> getInputFormats() {

		// Lazy initialization
		if (inputFormats == null) {
			inputFormats = new HashSet<Format>();
		}

		// Return
		return inputFormats;

	}

	public Set<Model> getOutputModels() {

		// Lazy initialization
		if (outputModels == null) {
			outputModels = new HashSet<Model>();
		}

		// Return
		return outputModels;

	}

	public Set<Format> getOutputFormats() {

		// Lazy initialization
		if (outputFormats == null) {
			outputFormats = new HashSet<Format>();
		}

		// Return
		return outputFormats;

	}

}
