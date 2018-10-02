package br.elois.maktaba.fabreveal.widget;

/**
 * Created by leo on 09/02/18.
 */

public interface AnimatedFab {

    /**
     * Shows the FAB.
     */
    void show();

    /**
     * Shows the FAB and sets the FAB's translation.
     *
     * @param translationX translation X value
     * @param translationY translation Y value
     */
    void show(float translationX, float translationY);

    /**
     * Hides the FAB.
     */
    void hide();

}