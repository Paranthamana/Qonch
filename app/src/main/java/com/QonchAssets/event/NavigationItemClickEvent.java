package com.QonchAssets.event;

public class NavigationItemClickEvent {
    private int selectedPosition;
    public NavigationItemClickEvent(int position) {
        this.selectedPosition = position;
    }

    public int getSelectedPosition(){
        return selectedPosition;
    }
}
