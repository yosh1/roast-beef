package com.developer.yoshi1125hisa.roastbeef;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class TelephoneNumber {
    public String telNum;
    public String key;
    public TelephoneNumber() {}

    public TelephoneNumber(String telNum) {
        this.telNum = telNum;
    }

    @Override
    public String toString() {
        return telNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelephoneNumber that = (TelephoneNumber) o;
        return Objects.equals(telNum, that.telNum);
    }

    @Override
    public int hashCode() {

        return Objects.hash(telNum);
    }

    public Map<String, Object> toMap() {
        return new HashMap<String, Object>() {{
            put("telNum", telNum);
        }};
    }
}
