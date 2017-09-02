package com.yqbd.yqbd.beans;

import com.yqbd.yqbd.tagview.widget.Tag;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 11022 on 2017/7/17.
 */
public class TypeBean {
    private Integer typeId;
    private String typeName;

    public TypeBean(Tag tag) {
        this.typeId = tag.getId();
        this.typeName = tag.getTitle();
    }

    public TypeBean(JSONObject jsonObject) throws JSONException{
        this.typeId = jsonObject.getInt("typeId");
        this.typeName = jsonObject.getString("typeName");
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
