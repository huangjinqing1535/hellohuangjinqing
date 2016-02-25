package com.huang.entity;

/**
 * Created by huang on 12/31/15.
 * 联系人
 */
public class Constact {


    private String name;
    private String clientid;
    private String groupName;
    private String state;

    public Constact() {
    }

    @Override
    public String toString() {
        return "Contant{" +
                "name='" + name + '\'' +
                ", clientid='" + clientid + '\'' +
                ", groupName='" + groupName + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public Constact(String name, String clientid, String groupName, String state) {
        this.name = name;
        this.clientid = clientid;
        this.groupName = groupName;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
