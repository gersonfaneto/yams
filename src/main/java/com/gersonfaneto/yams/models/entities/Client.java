package com.gersonfaneto.yams.models.entities;

public class Client {
    private String clientID = "Undefined";
    private String clientName;
    private String homeAddress;
    private String phoneNumber;

    public Client(String clientName, String homeAddress, String phoneNumber) {
        this.clientName = clientName;
        this.homeAddress = homeAddress;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject instanceof Client otherClient) {
            return otherClient.clientID.equals(this.clientID);
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("""
                ID: %s
                Name: %s
                Address: %s
                Contact: %s
                """, clientID, clientName, homeAddress, phoneNumber);
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
