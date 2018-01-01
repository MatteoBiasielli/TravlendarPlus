package travlendarplus.apimanager;

public enum Language {
    /**Meant to set the langauge for the google maps directions instructions.
     * It works fine and integrates perfectly with the APIManager class, but
     * the language choice has not been implemented yet on clients.
     */
    IT("it"), EN("en"), FR("fr");
    private final String lang;
    Language(String s){
        this.lang=s;
    }
    public String getLang(){
        return this.lang;
    }
	
}
