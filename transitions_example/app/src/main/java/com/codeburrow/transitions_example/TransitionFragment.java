package com.codeburrow.transitions_example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TransitionFragment extends Fragment {

    private static final String LOG_TAG = TransitionFragment.class.getSimpleName();

    /**
     * Empty Constructor.
     */
    public TransitionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.scene_start, container, false);

        /*
         * android.transition.Scene:
         * A scene represents the collection of values that various properties
         * in the View hierarchy will have when the scene is applied.
         */
        // Get the Scene described by the resource file associated with the given layoutId parameter.
        final Scene endScene = Scene.getSceneForLayout(container, R.layout.scene_end, getActivity());

        Button transitionButton = (Button) rootView.findViewById(R.id.transition_button);
        transitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToScene(endScene);
            }
        });

        return rootView;
    }

    /**
     * Helper Method.
     * <p/>
     * Changes to the given Scene using custom transitions.
     *
     * @param endScene The Scene to change to.
     */
    private void goToScene(Scene endScene) {
        /*
         * android.transition.ChangeBounds:
         * This transition captures the layout bounds of target views before
         * and after the scene change and animates those changes during the transition.
         */
        ChangeBounds changeBounds = new ChangeBounds();
        /*
         * android.transition.Fade:
         * This transition tracks changes to the visibility of target views in the start
         * and end scenes and fades views in or out when they become visible or non-visible.
          */
        // Constructs a Fade transition that will fade targets out.
        Fade fadeOut = new Fade(Fade.OUT);
        // Constructs a Fade transition that will fade targets in.
        Fade fadeIn = new Fade(Fade.IN);
        /*
         * android.transition.TransitionSet:
         * A TransitionSet is a parent of child transitions (including other TransitionSets).
         * Using TransitionSets enables more complex choreography of transitions,
         * where some sets play ORDERING_TOGETHER and others play ORDERING_SEQUENTIAL
         */
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        transitionSet
                .addTransition(fadeOut)
                .addTransition(changeBounds)
                .addTransition(fadeIn);
        /*
         * android.transition.TransitionManager:
         * This class manages the set of transitions that fire when there is a change of
         * Scene.
         */
        TransitionManager.go(endScene, transitionSet);
    }
}
