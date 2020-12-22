package com.lt.person_baseutil.model.entity;

public class TestBean {
    public static final int ITEM_TYPE_CONTENT = 0;
    public static final int ITEM_TYPE_SWITCH = 1;
    public static final int ITEM_TYPE_SLIDE = 2;
    private String ico;
    private String title;
    private int itemType;
    private String itemValue;

    private TestBean() {
    }

    public TestBean(Builder builder) {
        this.ico = builder.ico;
        this.title = builder.title;
        this.itemType = builder.itemType;
        this.itemValue = builder.itemValue;
    }

    public static Builder create() {
        return new Builder();
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public static class Builder {
        private String ico;
        private String title;
        private int itemType;
        private String itemValue;

        private Builder() {
        }

        public Builder setIco(String ico) {
            this.ico = ico;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setItemType(int itemType) {
            this.itemType = itemType;
            return this;
        }

        public Builder setItemValue(String itemValue) {
            this.itemValue = itemValue;
            return this;
        }

        public TestBean build() {
            return new TestBean(this);
        }
    }
}
