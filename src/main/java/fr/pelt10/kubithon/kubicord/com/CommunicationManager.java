package fr.pelt10.kubithon.kubicord.com;

import fr.pelt10.kubithon.kubicord.KubiCord;

import java.util.ArrayList;
import java.util.List;

public class CommunicationManager {
    private List<CommunicationMessage> messages = new ArrayList<>();

    public CommunicationManager(KubiCord kubiCord) {
        new Thread(new MessagesPubSub(kubiCord.getJedisUtils(), this)).start();
    }

    public void registerMessage(CommunicationMessage message) {
        if(messages.stream().map(CommunicationMessage::getClass).anyMatch(name -> name.equals(message.getClass()))) {
            throw new UnsupportedOperationException(message.getClass().getCanonicalName() + " is already register !");
        }
        messages.add(message);
    }

    public void executeMessage(String command, String[] args) {
        messages.stream().filter(message -> message.redisKeys().equals(command)).forEach(message -> message.receive(args));
    }
}