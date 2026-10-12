package teamboard;

/**
 * A captain who is registered to post notices on the team noticeboard.
 */
public class Captain {

    private String fullName;
    private String handle;

    /**
     * Creates a captain. The handle is always stored in lowercase.
     *
     * @param fullName the captain's full name (may be multiple words)
     * @param handle   the captain's handle (a single word)
     */
    public Captain(String fullName, String handle) {
        this.fullName = fullName.trim();
        this.handle = handle.trim().toLowerCase();
    }

    public String getFullName() {
        return fullName;
    }

    public String getHandle() {
        return handle;
    }

    /**
     * Checks whether this captain has the given handle (case-insensitive).
     */
    public boolean hasHandle(String otherHandle) {
        return handle.equals(otherHandle.trim().toLowerCase());
    }

    @Override
    public String toString() {
        return fullName + " (@" + handle + ")";
    }
}
