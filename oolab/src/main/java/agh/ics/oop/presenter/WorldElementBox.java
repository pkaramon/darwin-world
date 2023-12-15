package agh.ics.oop.presenter;

import agh.ics.oop.model.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

public class WorldElementBox extends VBox {
    private final WorldElement element;
    private final Label label;
    private final ImageView imageView;
    private static final ConcurrentHashMap<String, Image> images = new ConcurrentHashMap<>();

    public WorldElementBox(WorldElement element) {
        this.element = element;

        imageView = getImageView();
        label = new Label(element.getDisplayText());
        getChildren().add(imageView);
        getChildren().add(label);
        setAlignment(Pos.CENTER);
    }

    public void updateInfo() {
        label.setText(element.getDisplayText());
        imageView.setImage(getImage());
    }

    private ImageView getImageView() {
        Image image = getImage();
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;
    }

    private Image getImage() {
        String imagePath = element.getImagePath();

        if(images.containsKey(imagePath)) {
            return images.get(imagePath);
        }

        InputStream imageInputStream = getClass().getResourceAsStream(imagePath);

        if(imageInputStream == null) {
            throw new IllegalArgumentException("%s is not a valid image path".formatted(imagePath));
        }

        Image image = new Image(imageInputStream);
        images.put(imagePath, image);
        return image;
    }

}
