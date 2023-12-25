package org.stick;

import javafx.fxml.FXMLLoader;

public class FXMLLoaderFactory {

    public FXMLLoader createLoader(String resource) {
        return new FXMLLoader(getClass().getResource(resource));
    }
}

