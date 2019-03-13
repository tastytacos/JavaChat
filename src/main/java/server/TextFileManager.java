package server;

import message.Message;
import message.TextMessage;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class TextFileManager implements MessageManager {
    private static volatile TextFileManager instance;
    private File messagesBaseFile;
    private static String messageDelimiter = "--";
    private TextFileManager(File messagesBaseFile) {
        this.messagesBaseFile = messagesBaseFile;
    }

    public static TextFileManager getInstance(File messagesBase) {
        if (instance == null) {
            synchronized (TextFileManager.class) {
                if (instance == null) {
                    instance = new TextFileManager(messagesBase);
                }
            }
        }
        return instance;
    }

    @Override
    public void writeMessage(Message message) {
        try (FileWriter fileWriter = new FileWriter(messagesBaseFile, true)) {
            TextMessage textMessage = (TextMessage) message;
            fileWriter.append(messageDelimiter + textMessage.getFormattedMessageTime(textMessage.getMessageTime()));
            fileWriter.append(messageDelimiter + textMessage.getMessageAuthor() + messageDelimiter);
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
                DateTime time = handleTime(line);
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

    private DateTime handleTime(String line) {
        String[] strings = line.split(messageDelimiter);
        DateTime localTime = Message.dateTimeFromString(strings[1]);
        return localTime;
    }

    private String handleAuthor(String line) {
        String[] strings = line.split(messageDelimiter);
        return strings[2];
    }

    private String handleMessageText(String line) {
        String[] strings = line.split(messageDelimiter);
        return strings[3];
    }
}
