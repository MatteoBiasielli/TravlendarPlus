package travlendarplus.user.preferences;

import travlendarplus.travel.Route;

public abstract class Preference {
	/**@return a string human-readable version of the Object**/
        @Override
	public abstract String toString();
        public abstract boolean isRanged();

        public abstract boolean isRespectedBy(Route r);
}
