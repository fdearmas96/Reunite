package com.example.reunite;

import com.example.reunite.classes.LoguinFragment;
import com.example.reunite.fragments.NuevaPublicacionFragment;
import com.example.reunite.fragments.PublicacionFragment;

public interface ImplementsFragments extends  PublicacionFragment.OnFragmentInteractionListener,
        NuevaPublicacionFragment.OnFragmentInteractionListener, LoguinFragment.OnFragmentInteractionListener, ChatFerFragment.OnFragmentInteractionListener


{
}
