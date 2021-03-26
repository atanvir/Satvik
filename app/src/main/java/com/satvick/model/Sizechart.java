package com.satvick.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Sizechart implements Parcelable {


        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("value")
        @Expose
        private List<String> value = null;


        protected Sizechart(Parcel in) {
            key = in.readString();
            value = in.createStringArrayList();
        }

        public static final Creator<Sizechart> CREATOR = new Creator<Sizechart>() {
            @Override
            public Sizechart createFromParcel(Parcel in) {
                return new Sizechart(in);
            }

            @Override
            public Sizechart[] newArray(int size) {
                return new Sizechart[size];
            }
        };

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(key);
            dest.writeStringList(value);
        }
    }


