package tesi.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

public class Terminal implements Externalizable {
	
	private int id;
	private String name;
//	private Entry[] entries;
//	private Entry[] exits;
	private Entry[] entries;
	
	public Terminal() { } 
	
//	public Terminal(String name, int inNumber, int outNumber) {
//		this.name = name;
//		entries = new Entry[inNumber];
//		exits = new Entry[outNumber];
//		int counter = 1;
//		for(int i = 0; i < inNumber; ++i)
//			entries[i] = new Entry(counter++, true);
//		for(int i = 0; i < outNumber; ++i)
//			exits[i] = new Entry(counter++, false);
//	}
	
	public Terminal(String name, int in, int out) {
		this.name = name;
		int counter = 0;
		entries = new Entry[in + out];
		for(; counter < in; ++counter)
			entries[counter] = new Entry((counter + 1), true);
		for(; counter < in + out; ++counter)
			entries[counter] = new Entry((counter + 1), false);
	}
	
	public Terminal(int id, String name, int in, int out) {
		this(name, in, out);
		this.id = id;
	}
	
	public int getID() {
		return this.id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Entry[] getEntries() {
		return entries;
	}
	
	public void setEntries(Entry[] entries) {
		this.entries = entries;
	}
	
//	public Entry[] getExits() {
//		return exits;
//	}
//	
//	public void setExits(Entry[] exits) {
//		this.exits = exits;
//	}

//	@Override
//	public void writeExternal(ObjectOutput out) throws IOException {
//		out.writeInt(id);
//		out.writeObject(name);
//		out.writeInt(entries.length);
//		for(int i = 0; i < entries.length; ++i)
//			out.writeObject(entries[i]);
//		out.writeInt(exits.length);
//		for(int i = 0; i < exits.length; ++i)
//			out.writeObject(exits[i]);
//	}
//
//	@Override
//	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//		id = (int) in.readInt();
//		name = (String) in.readObject();
//		int entriesNumber = (int) in.readInt();
//		entries = new Entry[entriesNumber];
//		for(int i = 0; i < entriesNumber; ++i)
//			entries[i] = (Entry) in.readObject();
//		int exitsNumber = (int) in.readInt();
//		exits = new Entry[exitsNumber];
//		for(int i = 0; i < exitsNumber; ++i)
//			exits[i] = (Entry) in.readObject();
//	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(id);
		out.writeObject(name);
		out.writeInt(entries.length);
		for(int i = 0; i < entries.length; ++i)
			out.writeObject(entries[i]);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		id = (int) in.readInt();
		name = (String) in.readObject();
		int entriesNumber = (int) in.readInt();
		entries = new Entry[entriesNumber];
		for(int i = 0; i < entriesNumber; ++i)
			entries[i] = (Entry) in.readObject();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Terminal other = (Terminal) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return name + " [" + id + "]";
	}
}
