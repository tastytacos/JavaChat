package server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager implements MessageManager {
    private static volatile FileManager instance;
    private File messagesBaseFile;

    private FileManager(File messagesBaseFile) {
        this.messagesBaseFile = messagesBaseFile;
    }

    public static FileManager getInstance(File messagesBase) {
        if (instance == null) {
            synchronized (FileManager.class) {
                if (instance == null) {
                    instance = new FileManager(messagesBase);
                }
            }
        }
        return instance;
    }

    @Override
    public void writeMessage(String message) {
        try (FileWriter fileWriter = new FileWriter(messagesBaseFile, true)) {
            fileWriter.append(message);
            fileWriter.append(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getNMessages(int messagesAmount) {
        List<String> messages = new ArrayList<>();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(messagesBaseFile, "r")) {
            String message;
            while ((message = randomAccessFile.readLine()) != null) {
                messages.add(message);
            }
            if (messages.size() > messagesAmount){
                return messages.subList(messages.size() - messagesAmount, messages.size());
            }
            return messages;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
