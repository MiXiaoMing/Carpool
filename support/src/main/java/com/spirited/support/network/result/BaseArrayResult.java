package com.spirited.support.network.result;


import com.spirited.support.network.validate.IAfterDeserializeAction;

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
