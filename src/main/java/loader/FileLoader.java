package loader;

import exceptions.ActionException;

import java.io.*;

public class FileLoader<T> {
    private String name;

    public FileLoader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void write(T object) throws ActionException {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(name))) {
            output.writeObject(object);
        } catch (IOException e) {
            throw new ActionException(e);
        }
    }

    public T read() throws ActionException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(name))) {
            return (T) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new ActionException(e);
        }
    }

}
