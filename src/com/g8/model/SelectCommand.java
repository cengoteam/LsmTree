package com.g8.model;

import java.util.ArrayList;
import java.util.List;

public class SelectCommand {
    private List<Condition> conditions;

    public SelectCommand(){
        conditions = new ArrayList<>();
    }

    public void addCondition(Condition condition){
        conditions.add(condition);
    }

    public List<Condition> getConditions() {
        return conditions;
    }
}
