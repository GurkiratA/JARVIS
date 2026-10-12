package teamboard;

/**
 * A notice posted on the team noticeboard by a registered captain.
 */
public class Notice {

    private String title;
    private String body;
    private Captain captain;

    /**
     * Creates a notice.
     *
     * @param title   the notice title (a single line of words)
     * @param body    the notice body (a single line of words)
     * @param captain the captain who created the notice
     */
    public Notice(String title, String body, Captain captain) {
        this.title = title.trim();
        this.body = body.trim();
        this.captain = captain;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Captain getCaptain() {
        return captain;
    }

    /**
     * Checks whether this notice was posted by the captain with the given handle.
     */
    public boolean isPostedBy(String handle) {
        return captain.hasHandle(handle);
    }

    /**
     * Checks whether the keyword appears as a whole word in the title or body.
     * Matching ignores case but is word-by-word, so "practice" does not match
     * "practices".
     */
    public boolean containsKeyword(String keyword) {
        String target = keyword.trim();
        if (target.isEmpty()) {
            return false;
        }
        return hasWord(title, target) || hasWord(body, target);
    }

    /**
     * Tokenizes text into words (runs of alpha-numeric characters) and checks
     * whether any of them equals the target, ignoring case.
     */
    private static boolean hasWord(String text, String target) {
        String[] words = text.split("[^A-Za-z0-9]+");
        for (String word : words) {
            if (word.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Posted By: " + captain.getFullName() + " (@" + captain.getHandle() + ")\n"
                + "\n"
                + "Title: " + title + "\n"
                + "\n"
                + body + "\n";
    }
}
