package com.recyclegridview;

import java.io.Serializable;
import java.util.TreeSet;

public class AlbumsModel implements Serializable{

    private static final long serialVersionUID = 1L;

	public TreeSet<String> folderImages;
	protected String folderName;
    protected String folderImagePath;

    private boolean isSelected;


    public AlbumsModel () {
		folderImages = new TreeSet<String> ();
	}

    public boolean isSelected () {
        return isSelected;
    }

    public void setSelected (boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getFolderName () {
        return folderName;
    }

    public void setFolderName (String folderName) {
        this.folderName = folderName;
    }

    public String getFolderImagePath () {
        return folderImagePath;
    }

    public void setFolderImagePath (String folderImagePath) {
        this.folderImagePath = folderImagePath;
    }
}
