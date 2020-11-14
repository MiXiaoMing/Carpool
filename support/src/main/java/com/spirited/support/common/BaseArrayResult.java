package com.spirited.support.common;

import com.appframe.library.network.http.validate.IAfterDeserializeAction;

import java.io.Serializable;
import java.util.ArrayList;

public class BaseArrayResult<T> extends Result implements Serializable, IAfterDeserializeAction {
    public ArrayList<T> data;

    @Override
    public void doAfterDeserialize() {
        if (data == null) {
            data = new ArrayList<>();
        }
    }
}
