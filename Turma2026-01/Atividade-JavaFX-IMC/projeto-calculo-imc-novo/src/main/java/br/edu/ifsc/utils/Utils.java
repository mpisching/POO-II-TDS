/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.utils;

import javafx.scene.image.Image;

/**
 *
 * @author mpisc
 */
public class Utils {
    
    //public static final Image APPLICATION_ICON = new Image("/icons/IFSC_logo_vertical.png");
    public static final Image APPLICATION_ICON = 
            new Image((new Utils()).getClass().getResourceAsStream(
                    "/icons/IFSC_logo_vertical.png"));
    
}
