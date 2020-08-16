package com.createdinam.pbwatches.Model;

public class AttachmentModel {
    String Name,CreateDate;

    public AttachmentModel(String name, String createDate) {
        Name = name;
        CreateDate = createDate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }
}
