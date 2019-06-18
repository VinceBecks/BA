package de.openknowledge.playground.api.rest.supportCode.converter.convertedClasses;

import java.util.LinkedList;
import java.util.List;

public class IntegerList {
    List<Integer> integerList;

    public IntegerList (String listOfInteger) {
        this.integerList = new LinkedList<>();
        if (listOfInteger!=null) {
            String[] list = listOfInteger.split(",");
            for (int i=0; i<list.length; i++) {
                if (!list[i].equals("")) {
                    this.integerList.add(Integer.parseInt(list[i]));
                }
            }
        }

    }

    public List<Integer> getIntegerList() {
        return integerList;
    }
}
