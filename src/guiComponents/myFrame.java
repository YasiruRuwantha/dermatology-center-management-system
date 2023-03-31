package guiComponents;

import javax.swing.*;
import java.awt.*;

public class myFrame extends JFrame {

    //This is the base frame
    public myFrame() {

        this.setTitle("title "); //sets title of this
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit from application
        this.setResizable(false); //prevent this from being resized
        this.setSize(1280, 720); //sets the x-dimension, and y-dimension of this
        this.setVisible(true); //make this visible

    }

}
