package message;

import org.joda.time.LocalTime;

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
    public void writeMessage(Message message) {
        try (FileWriter fileWriter = new FileWriter(messagesBaseFile, true)) {
            TextMessage textMessage = (TextMessage) message;
            fileWriter.append("On " + textMessage.getMessageTime());
            fileWriter.append(" user " + textMessage.getMessageAuthor() + " wrote - ");
            fileWriter.append(textMessage.getMessageText());
            fileWriter.append(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TextMessage> getNTextMessages(int messagesAmount) {
        List<TextMessage> messages = new ArrayList<>();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(messagesBaseFile, "r")) {
            String line;
            while ((line = randomAccessFile.readLine()) != null) {
                String messageText = handleMessageText(line);
                String author = handleAuthor(line);
                LocalTime time = handleTime(line);
                messages.add(new TextMessage(messageText, author, time));
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

    private org.joda.time.LocalTime handleTime(String line) {
        return null;
    }

    private String handleAuthor(String line) {
        return null;
    }

    private String handleMessageText(String line) {
        return null;
    }
}
