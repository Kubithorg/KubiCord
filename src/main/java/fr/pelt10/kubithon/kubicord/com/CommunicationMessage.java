package fr.pelt10.kubithon.kubicord.com;

public interface CommunicationMessage {
    void receive(String[] args);
    String redisKeys();
}