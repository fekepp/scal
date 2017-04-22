package net.fekepp.ldfu.server.data.listeners;

import java.util.HashSet;
import java.util.Set;

import net.fekepp.ldfu.server.data.Format;
import net.fekepp.ldfu.server.data.Model;

public abstract class AbstractListener implements Listener {

	private ListenerDelegate listenerDelegate;

	private Set<String> identifiers = new HashSet<String>();

	private Set<String> methods = new HashSet<String>();

	private Set<Model> inputModels = new HashSet<Model>();
	private Set<Format> inputFormats = new HashSet<Format>();

	private Set<Model> storageModels = new HashSet<Model>();
	private Set<Format> storageFormats = new HashSet<Format>();

	private Set<Model> outputModels = new HashSet<Model>();
	private Set<Format> outputFormats = new HashSet<Format>();

	public AbstractListener() {
		this(null);
	}

	public AbstractListener(ListenerDelegate listenerDelegate) {
		this.listenerDelegate = listenerDelegate;
	}

	@Override
	public ListenerDelegate getListenerDelegate() {
		return listenerDelegate;
	}

	@Override
	public void setListenerDelegate(ListenerDelegate listenerDelegate) {
		this.listenerDelegate = listenerDelegate;
	}

	@Override
	public boolean isListeningOnIdentifier(String identifier) {
		if (identifiers.isEmpty() || identifiers.contains(identifier)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnMethod(String method) {
		if (methods.isEmpty() || methods.contains(method)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnInput(Model model) {
		if (inputModels.isEmpty() || inputModels.contains(model)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnInput(Format format) {
		if (inputFormats.isEmpty() || inputFormats.contains(format)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnStorage(Model model) {
		if (storageModels.isEmpty() || storageModels.contains(model)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnStorage(Format format) {
		if (storageFormats.isEmpty() || storageFormats.contains(format)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnOutput(Model model) {
		if (outputModels.isEmpty() || outputModels.contains(model)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isListeningOnOutput(Format format) {
		if (outputFormats.isEmpty() || outputFormats.contains(format)) {
			return true;
		}
		return false;
	}

	public Set<String> getIdentifiers() {
		return identifiers;
	}

	public Set<String> getMethods() {
		return methods;
	}

	public Set<Model> getInputModels() {
		return inputModels;
	}

	public Set<Format> getInputFormats() {
		return inputFormats;
	}

	public Set<Model> getStorageModels() {
		return storageModels;
	}

	public Set<Format> getStorageFormats() {
		return storageFormats;
	}

	public Set<Model> getOutputModels() {
		return outputModels;
	}

	public Set<Format> getOutputFormats() {
		return outputFormats;
	}

}
