package org.storemate.pos.utils;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Utility class for applying smooth entrance/exit transitions to JavaFX nodes.
 * <p>
 * Currently includes a TikTok-style entrance animation with fade, slide, and scale effects.
 * </p>
 * @author Khushi
 */
public class TransitionUtil {

    /**
     * Applies a TikTok-style entrance animation to the given JavaFX Node.
     * <p>
     * The node will start slightly to the right, transparent, and scaled down,
     * then animate to its original position, full opacity, and normal scale.
     * </p>
     *
     * @param node the JavaFX Node to animate
     */

    public static void applyTikTokTransition(Node node) {

        node.setOpacity(0);
        node.setTranslateX(60);  // start slightly off-screen (right)
        node.setScaleX(0.97);    // soft zoom-in effect
        node.setScaleY(0.97);

        // TikTok-style cubic-bezier curve
        Interpolator tikTokEase = Interpolator.SPLINE(0.22, 1, 0.36, 1);

        // Fade animation
        FadeTransition fade = new FadeTransition(Duration.millis(900), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setInterpolator(tikTokEase);

        // Slide animation
        TranslateTransition slide = new TranslateTransition(Duration.millis(900), node);
        slide.setFromX(60);
        slide.setToX(0);
        slide.setInterpolator(tikTokEase);

        // Soft zoom pop
        ScaleTransition scale = new ScaleTransition(Duration.millis(900), node);
        scale.setFromX(0.97);
        scale.setFromY(0.97);
        scale.setToX(1);
        scale.setToY(1);
        scale.setInterpolator(tikTokEase);

        ParallelTransition transition = new ParallelTransition(fade, slide, scale);
        transition.play();
    }
}
