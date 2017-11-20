package travlendarplus.user.preferences;

public abstract class Preference {
	/**@return a string human-readable version of the Object**/
        @Override
	public abstract String toString();
        public abstract boolean isRanged();
}
