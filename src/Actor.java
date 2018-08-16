import java.util.*;

public abstract class Actor {
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newActors A list to receive newly born animals.
     */
    abstract public void act(List<Actor> newActors);

    /**
     * @return true if the actor is still active(alive or present in some form)
     */
    abstract protected boolean isActive();
}
